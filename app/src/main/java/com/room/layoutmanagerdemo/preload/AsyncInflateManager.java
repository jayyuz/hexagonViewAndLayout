package com.room.layoutmanagerdemo.preload;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.os.Trace;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用来提供子线程inflate view的功能，避免某个view层级太深太复杂，主线程inflate会耗时很长，
 * 实就是对 AsyncLayoutInflater进行了抽取和封装
 */
public class AsyncInflateManager {

    private final String TAG = getClass().getSimpleName();

    private static AsyncInflateManager sInstance;
    private ConcurrentHashMap<String, AsyncInflateItem> mInflateMap; // 保存inflateKey以及InflateItem，里面包含所有要进行inflate的任务
    private ConcurrentHashMap<String, CountDownLatch> mInflateLatchMap;
    private ExecutorService mThreadPool; // 用来进行inflate工作的线程池

    private AsyncInflateManager() {
        mThreadPool = new ThreadPoolExecutor(4, 4, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
        mInflateMap = new ConcurrentHashMap<>();
        mInflateLatchMap = new ConcurrentHashMap<>();
    }

    public static AsyncInflateManager getInstance() {
        if (sInstance == null) {
            synchronized (AsyncInflateManager.class) {
                if (sInstance == null) {
                    sInstance = new AsyncInflateManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 用来获得异步inflate出来的view
     *
     * @param context
     * @param layoutResId 需要拿的layoutId
     * @param parent container
     * @param inflateKey 每一个View会对应一个inflateKey，因为可能许多地方用的同一个 layout，但是需要inflate多个，用InflateKey进行区分
     * @param inflater 外部传进来的inflater，外面如果有inflater，尽量传进来，比如给Fragment setView回去，
     *         如果用直接new的LayoutInflater，会抛异常，需要直接用Fragment自带的Inflater
     * @return 最后inflate出来的view
     */
    @UiThread
    @NonNull
    public View getInflatedView(Context context, int layoutResId, @Nullable ViewGroup parent, String inflateKey,
            @NonNull LayoutInflater inflater) {
        if (!TextUtils.isEmpty(inflateKey) && mInflateMap.containsKey(inflateKey)) {
            AsyncInflateItem item = mInflateMap.get(inflateKey);
            CountDownLatch latch = mInflateLatchMap.get(inflateKey);
            if (item != null) {
                View resultView = item.inflatedView;
                if (resultView != null) {
                    // 拿到了view直接返回
                    removeInflateKey(inflateKey);
                    replaceContextForView(resultView, context);
                    return resultView;
                }

                if (item.isInflating() && latch != null) {
                    // 没拿到view，但是在inflate中，等待返回
                    try {
                        latch.wait();
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    removeInflateKey(inflateKey);
                    if (resultView != null) {
                        replaceContextForView(resultView, context);
                        return resultView;
                    }

                }
                // 如果还没开始inflate，则设置为false，UI线程进行inflate
                item.setCancelled(true);
            }
        }
        // 拿异步inflate的View失败，UI线程inflate
        return inflater.inflate(layoutResId, parent, false);
    }

    /**
     * inflater初始化时是传进来的application，inflate出来的view的context没法用来startActivity，
     * 因此用MutableContextWrapper进行包装，后续进行替换
     */
    private void replaceContextForView(View inflatedView, Context context) {
        if (inflatedView == null || context == null) {
            return;
        }
        Context cxt = inflatedView.getContext();
        if (cxt instanceof MutableContextWrapper) {
            ((MutableContextWrapper) cxt).setBaseContext(context);
        }
    }

    @UiThread
    public void asyncInflateViews(Context context, AsyncInflateItem... list) {
        if (list == null || list.length == 0) {
            return;
        }

        asyncInflateViews(context, Arrays.asList(list));
    }

    @UiThread
    public void asyncInflateViews(Context context, List<AsyncInflateItem> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (AsyncInflateItem item : list) {
            asyncInflate(context, item);
        }
    }

    @UiThread
    private void asyncInflate(Context context, AsyncInflateItem item) {
        if (item == null || item.layoutResId == 0 || mInflateMap.containsKey(item.inflateKey) || item.isCancelled()
                || item.isInflating()) {
            return;
        }
        onAsyncInflateReady(item);
        inflateWithThreadPool(context, item);
    }

    private void onAsyncInflateReady(AsyncInflateItem item) {
        mInflateMap.put(item.inflateKey, item);
        mInflateLatchMap.put(item.inflateKey, new CountDownLatch(1));
    }

    private void onAsyncInflateStart(AsyncInflateItem item) {
        item.setInflating(true);
    }

    private void onAsyncInflateEnd(AsyncInflateItem item, boolean success) {
        item.setInflating(false);
        CountDownLatch latch = mInflateLatchMap.get(item.inflateKey);
        if (latch != null) {
            latch.countDown();
        }
        if (success && item.callback != null) {
            removeInflateKey(item.inflateKey);
            // 回主线程调callback
            Looper.getMainLooper().getQueue().addIdleHandler(new IdleHandler() {
                @Override
                public boolean queueIdle() {
                    return false;
                }
            });
            new Handler(Looper.getMainLooper()).post(
                    () -> item.callback.onInflateFinished(item)
            );
        }
    }

    private void removeInflateKey(String inflateKey) {
        mInflateLatchMap.remove(inflateKey);
        mInflateMap.remove(inflateKey);
    }

    private void inflateWithThreadPool(Context context, AsyncInflateItem item) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (!item.isInflating() && !item.isCancelled()) {
                    try {
                        onAsyncInflateStart(item);
                        item.inflatedView = new BasicInflater(context).inflate(item.layoutResId, item.parent, false);
                        onAsyncInflateEnd(item, true);
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Failed to inflate resource in the background! Retrying on the UI thread", e);
                        onAsyncInflateEnd(item, false);
                    }
                }
            }
        });
    }

    /**
     * copy from AsyncLayoutInflater - actual inflater
     */
    private static class BasicInflater extends LayoutInflater {

        private static final String[] sClassPrefixList = new String[]{"android.widget.", "android.webkit.",
                "android.app."};

        BasicInflater(Context context) {
            super(context);
        }

        public LayoutInflater cloneInContext(Context newContext) {
            return new BasicInflater(newContext);
        }

        protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
            for (String prefix : sClassPrefixList) {
                try {
                    View view = this.createView(name, prefix, attrs);
                    if (view != null) {
                        return view;
                    }
                } catch (ClassNotFoundException ignored) {
                }
            }
            return super.onCreateView(name, attrs);
        }
    }
}
package com.room.layoutmanagerdemo.preload;

import android.content.Context;
import android.content.MutableContextWrapper;
import com.room.layoutmanagerdemo.R;

/**
 * @author zoutao
 */
public class AsyncInflateUtil {

    public static void startTask(Context applicationContext) {
        Context context = new MutableContextWrapper(applicationContext.getApplicationContext());
        AsyncInflateManager.getInstance().asyncInflateViews(context,
                new AsyncInflateItem(InflateKey.TAB_1_CONTAINER_FRAGMENT, R.layout.fragment_curtain),
                new AsyncInflateItem(InflateKey.SUB_TAB_1_FRAGMENT, R.layout.fragment_linear_layout),
                new AsyncInflateItem(InflateKey.SUB_TAB_2_FRAGMENT, R.layout.fragment_curtain),
                new AsyncInflateItem(InflateKey.SUB_TAB_3_FRAGMENT, R.layout.fragment_change_theme),
                new AsyncInflateItem(InflateKey.SUB_TAB_4_FRAGMENT, R.layout.fragment_first));
    }

    public class InflateKey {

        public static final String TAB_1_CONTAINER_FRAGMENT = "tab1";
        public static final String SUB_TAB_1_FRAGMENT = "sub1";
        public static final String SUB_TAB_2_FRAGMENT = "sub2";
        public static final String SUB_TAB_3_FRAGMENT = "sub3";
        public static final String SUB_TAB_4_FRAGMENT = "sub4";
    }
}
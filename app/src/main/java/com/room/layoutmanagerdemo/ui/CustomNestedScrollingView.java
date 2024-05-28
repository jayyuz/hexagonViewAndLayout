package com.room.layoutmanagerdemo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

public class CustomNestedScrollingView extends ViewGroup implements NestedScrollingChild {

    private NestedScrollingChildHelper mNestedScrollingChildHelper;
    private int mLastTouchY;
    private int mScrollY;
    private int[] mScrollOffset = new int[2];
    private int[] mScrollConsumed = new int[2];

    private int scrollMaxHeight = 0;

    public CustomNestedScrollingView(Context context) {
        this(context, null);
    }

    public CustomNestedScrollingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNestedScrollingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHelper();
    }

    private void initHelper() {
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
            int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int totalHeight = 0;
        int maxWidth = 0;
        final int count = getChildCount();

        // 测量每个子View，并计算总高度和最大宽度
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
//                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                totalHeight += child.getMeasuredHeight();
            }
        }
        int measuredHeight = resolveSizeAndState(totalHeight, heightMeasureSpec, 0);
        scrollMaxHeight = totalHeight - measuredHeight;

        // 设置ViewGroup的测量尺寸
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curTop = getPaddingTop();

        // 布局每个子View
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int left = getPaddingLeft();
                final int right = left + child.getMeasuredWidth();
                final int top = curTop;
                final int bottom = top + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);
                curTop = bottom;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        final int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchY = y;
                // 开始嵌套滑动
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = mLastTouchY - y;
                // 传给parent，看看parent需要消耗多少
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    // 根据parent消耗的多少，判断自己可以消耗多少
                    deltaY -= mScrollConsumed[1];
                }
                if (deltaY != 0) {
                    // 实际滑动
                    int oldScrollY = getScrollY();
                    int newScrollY = Math.max(0, oldScrollY + deltaY);
                    if (oldScrollY <= scrollMaxHeight && newScrollY < scrollMaxHeight) {
                        scrollTo(0, newScrollY);
                    }
                    int scrolledDeltaY = getScrollY() - oldScrollY;
                    Log.e("scroll",
                            String.format("onTouchEvent: oldScrollY:%d,newScrollY:%d,deltaY:%d,childTotalHeight:%d",
                                    oldScrollY, newScrollY, deltaY, scrollMaxHeight));
                    // 滑动后通知父View
                    dispatchNestedScroll(0, scrolledDeltaY, 0,
                            deltaY - scrolledDeltaY, mScrollOffset);
                    mLastTouchY = y - mScrollOffset[1];
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 停止嵌套滑动
                stopNestedScroll();
                break;
        }
        return true;
    }
}
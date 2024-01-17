package com.room.layoutmanagerdemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.customview.widget.ViewDragHelper;

public class MyCustomViewGroup extends ViewGroup implements NestedScrollingChild {

    private static final String TAG = "MyCustomViewGroup";
    private final ViewDragHelper mDragHelper;
    private NestedScrollingChildHelper mNestedScrollingChildHelper;

    public MyCustomViewGroup(Context context) {
        this(context, null);
    }

    public MyCustomViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustomViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                // 允许所有子视图被拖拽
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 控制子视图水平方向的拖拽范围
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - child.getWidth() - leftBound;

                Log.e(TAG, String.format("clampViewPositionHorizontal: cLeft:%d, left:%d, dx:%d, result:%d",
                        child.getLeft(), left,
                        dx, Math.min(Math.max(left, leftBound), rightBound)));
                return Math.min(Math.max(left, leftBound), rightBound);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                // 控制子视图垂直方向的拖拽范围
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - child.getHeight() - topBound;

                int offset = top + dy;
                if (offset > getHeight() - (getPaddingBottom() + child.getHeight())) {
                    offset = getHeight() - (getPaddingBottom() + child.getHeight());
                }
                Log.e(TAG, String.format("clampViewPositionVertical: cTop:%d, top:%d, dy:%d, result:%d,offset:%d",
                        child.getTop(),
                        top,
                        dy, Math.min(Math.max(top, topBound), bottomBound), offset));
                return Math.min(Math.max(top, topBound), bottomBound);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                // 当视图被释放时，可以在这里处理动画或者固定位置
                super.onViewReleased(releasedChild, xvel, yvel);
                // 例如，可以让视图回到初始位置
//                mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), releasedChild.getTop());
                invalidate();
            }
        });

        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 在这里布局子视图的位置
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(l, t, l + child.getMeasuredWidth(), t + child.getMeasuredHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 测量子视图的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 在这里绘制任何自定义的内容
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
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
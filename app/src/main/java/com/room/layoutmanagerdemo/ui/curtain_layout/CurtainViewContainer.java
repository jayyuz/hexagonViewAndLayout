package com.room.layoutmanagerdemo.ui.curtain_layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.room.layoutmanagerdemo.ui.curtain_layout.adapter.CurtainAdapter;
import com.room.layoutmanagerdemo.ui.curtain_layout.layoutmanager.HorizontalLayoutManager;
import com.room.layoutmanagerdemo.ui.curtain_layout.layoutmanager.ILayoutManager;
import com.room.layoutmanagerdemo.ui.curtain_layout.layoutmanager.VerticalLayoutManager;


/**
 * @auther Newki
 * @date 2023/1/14
 * @description 包裹每一个Item容器的总容器
 */
class CurtainViewContainer extends ViewGroup {

    private ILayoutManager mLayoutManager;
    private int horizontalSpacing;  //每一个Item的左右间距
    private int verticalSpacing;  //每一个Item的上下间距
    private int mRowCount = 6;   // 一行多少个Item
    private int fixedWidth;  //如果是垂直瀑布流，需要设置宽度固定
    private int fixedHeight;

    private CurtainAdapter mAdapter;

    public CurtainViewContainer(Context context) {
        this(context, null);
    }

    public CurtainViewContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurtainViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setClipChildren(false);
        setClipToPadding(false);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int childCount = getChildCount();

        if (mAdapter == null || mAdapter.getItemCount() == 0 || childCount == 0) {
            setMeasuredDimension(0, 0);
            return;
        }

        if (mLayoutManager != null && (fixedWidth > 0 || fixedHeight > 0)) {

            // 指定测量子View的宽高模式，然后让子View决定对应的宽高（指定高，子View决定自己的宽；指定宽，子View决定自己的高）
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);

                if (mLayoutManager.getLayoutDirection() == ILayoutManager.DIRECTION_VERITICAL) {
                    measureChild(childView,
                            MeasureSpec.makeMeasureSpec(fixedWidth, MeasureSpec.EXACTLY),
                            heightMeasureSpec);
                } else {
                    measureChild(childView,
                            widthMeasureSpec,
                            MeasureSpec.makeMeasureSpec(fixedHeight, MeasureSpec.EXACTLY));
                }
            }

            // 使用布局管理器确定子View的位置，并返回总共需要的宽高
            int[] dimensions = mLayoutManager.performMeasure(this, mRowCount, horizontalSpacing, verticalSpacing,
                    mLayoutManager.getLayoutDirection() == ILayoutManager.DIRECTION_VERITICAL ? fixedWidth : fixedHeight);
            // 根据布局器返回的宽高，决定本ViewGroup的宽高
            setMeasuredDimension(dimensions[0], dimensions[1]);

        } else {
            throw new RuntimeException("You need to set the layoutManager first");
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return;
        }

        if (mLayoutManager != null && (fixedWidth > 0 || fixedHeight > 0)) {
            mLayoutManager.performLayout(this, mRowCount, horizontalSpacing, verticalSpacing,
                    mLayoutManager.getLayoutDirection() == ILayoutManager.DIRECTION_VERITICAL ? fixedWidth : fixedHeight);
            performBindData();
        } else {
            throw new RuntimeException("You need to set the layoutManager first");
        }

    }


    // =======================  数据适配器 begin ↓ =========================

    public void setAdapter(CurtainAdapter adapter) {
        mAdapter = adapter;
        inflateAllViews();
    }

    public CurtainAdapter getAdapter() {
        return mAdapter;
    }

    //填充Adapter布局
    private void inflateAllViews() {
        removeAllViewsInLayout();

        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return;
        }

        //添加布局
        for (int i = 0; i < mAdapter.getItemCount(); i++) {

            int itemType = mAdapter.getItemViewType(i);

            View view = mAdapter.onCreateItemView(getContext(), this, itemType);

            addView(view);
        }

        requestLayout();
    }

    //绑定布局中的数据
    private void performBindData() {
        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return;
        }

        post(() -> {

            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                int itemType = mAdapter.getItemViewType(i);
                View view = getChildAt(i);

                mAdapter.onBindItemView(view, itemType, i);
            }

        });

    }

    // =======================  暴露的LayoutManager方法 ↓ =========================

    /**
     * 设置Layout的方向，如果是垂直瀑布流，可以改变固定的宽度
     */
    public void setLayoutDirectionVertical() {
        mLayoutManager = new VerticalLayoutManager();
    }

    public void setLayoutDirectionVertical(int fixedWidth) {
        this.fixedWidth = fixedWidth;
        mLayoutManager = new VerticalLayoutManager();
    }

    /**
     * 设置Layout的方向，如果是水平瀑布流，可以改变固定的高度
     */
    public void setLayoutDirectionHorizontal() {
        mLayoutManager = new HorizontalLayoutManager();
    }

    public void setLayoutDirectionHorizontal(int fixedHeight) {
        this.fixedHeight = fixedHeight;
        mLayoutManager = new HorizontalLayoutManager();
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    public void setRowCount(int rowCount) {
        this.mRowCount = rowCount;
    }

    public void setFixedWidth(int fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public void setFixedHeight(int fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

}

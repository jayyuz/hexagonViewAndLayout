package com.room.layoutmanagerdemo.ui.curtain_layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.room.layoutmanagerdemo.R;
import com.room.layoutmanagerdemo.ui.curtain_layout.adapter.CurtainAdapter;
import com.room.layoutmanagerdemo.ui.curtain_layout.event.TouchEventHandler;


/**
 * @auther Newki
 * @date 2023/1/14
 * @description 暴露外界使用的集成容器
 */
public class CurtainLayout extends FrameLayout {

    private final TouchEventHandler mGestureHandler;
    private CurtainViewContainer mCurtainViewContainer;
    private boolean disallowIntercept = false;

    private int horizontalSpacing;
    private int verticalSpacing;
    private int rowCount;
    private int fixedWidth;
    private int fixedHeight;
    private boolean moveInViewport;
    private boolean enableScale;
    private float maxScale;
    private float minScale;

    public CurtainLayout(@NonNull Context context) {
        this(context, null);
    }

    public CurtainLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurtainLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setClipChildren(false);
        setClipToPadding(false);

        mCurtainViewContainer = new CurtainViewContainer(getContext());
        addView(mCurtainViewContainer);

        initAttr(context, attrs);

        // 对 CurtainViewContainer 进行放缩和位置转换
        mGestureHandler = new TouchEventHandler(getContext(), mCurtainViewContainer);

        //设置是否在窗口内移动
        mGestureHandler.setKeepInViewport(moveInViewport);
        mGestureHandler.setEnableScale(enableScale);
        mGestureHandler.setMinScale(minScale);
        mGestureHandler.setMaxScale(maxScale);

        mCurtainViewContainer.setHorizontalSpacing(horizontalSpacing);
        mCurtainViewContainer.setVerticalSpacing(verticalSpacing);
        mCurtainViewContainer.setRowCount(rowCount);
        mCurtainViewContainer.setFixedWidth(fixedWidth);
        mCurtainViewContainer.setFixedHeight(fixedHeight);

        if (fixedWidth > 0 || fixedHeight > 0) {
            if (fixedWidth > 0) {
                mCurtainViewContainer.setLayoutDirectionVertical(fixedWidth);
            } else {
                mCurtainViewContainer.setLayoutDirectionHorizontal(fixedHeight);
            }
        }
    }

    /**
     * 获取自定义属性
     */
    private void initAttr(Context context, AttributeSet attrs) {

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CurtainLayout);
        this.horizontalSpacing = mTypedArray.getDimensionPixelSize(R.styleable.CurtainLayout_horizontalSpacing, 20);
        this.verticalSpacing = mTypedArray.getDimensionPixelSize(R.styleable.CurtainLayout_verticalSpacing, 20);
        this.rowCount = mTypedArray.getInteger(R.styleable.CurtainLayout_rowCount, 6);
        this.fixedWidth = mTypedArray.getDimensionPixelOffset(R.styleable.CurtainLayout_fixedWidth, 150);
        this.fixedHeight = mTypedArray.getDimensionPixelSize(R.styleable.CurtainLayout_fixedHeight, 180);
        this.moveInViewport = mTypedArray.getBoolean(R.styleable.CurtainLayout_moveInViewport, false);
        this.enableScale = mTypedArray.getBoolean(R.styleable.CurtainLayout_enableScale, true);
        this.minScale = mTypedArray.getFloat(R.styleable.CurtainLayout_minScale, 0.7f);
        this.maxScale = mTypedArray.getFloat(R.styleable.CurtainLayout_maxScale, 1.5f);

        mTypedArray.recycle();
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        this.disallowIntercept = disallowIntercept;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return (!disallowIntercept && mGestureHandler.detectInterceptTouchEvent(event)) || super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !disallowIntercept && mGestureHandler.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mGestureHandler.setViewport(w, h);
    }

    public void setMoveInViewportInViewport(boolean moveInViewport) {
        this.moveInViewport = moveInViewport;
        mGestureHandler.setKeepInViewport(moveInViewport);
    }

    public void setEnableScale(boolean enableScale) {
        this.enableScale = enableScale;
        mGestureHandler.setEnableScale(enableScale);
    }

    public void setMinScale(float minScale) {
        this.minScale = minScale;
        mGestureHandler.setMinScale(minScale);
    }

    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
        mGestureHandler.setMaxScale(maxScale);
    }


    public void setHorizontalSpacing(int horizontalSpacing) {
        mCurtainViewContainer.setHorizontalSpacing(horizontalSpacing);
    }

    public void setVerticalSpacing(int verticalSpacing) {
        mCurtainViewContainer.setVerticalSpacing(verticalSpacing);
    }

    public void setRowCount(int rowCount) {
        mCurtainViewContainer.setRowCount(rowCount);
    }

    public void setFixedWidth(int fixedWidth) {
        mCurtainViewContainer.setLayoutDirectionVertical(fixedWidth);
    }

    public void setFixedHeight(int fixedHeight) {
        mCurtainViewContainer.setLayoutDirectionHorizontal(fixedHeight);
    }

    /**
     * 设置数据适配器
     */
    public void setAdapter(CurtainAdapter adapter) {
        mCurtainViewContainer.setAdapter(adapter);
    }

    public CurtainAdapter getAdapter() {
        return mCurtainViewContainer.getAdapter();
    }

    /**
     * 设置Layout的方向，如果是垂直瀑布流，可以改变固定的宽度
     */
    public void setLayoutDirectionVertical() {
        mCurtainViewContainer.setLayoutDirectionVertical();
    }

    public void setLayoutDirectionVertical(int fixedWidth) {
        mCurtainViewContainer.setLayoutDirectionVertical(fixedWidth);
    }

    /**
     * 设置Layout的方向，如果是水平瀑布流，可以改变固定的高度
     */
    public void setLayoutDirectionHorizontal() {
        mCurtainViewContainer.setLayoutDirectionHorizontal();
    }

    public void setLayoutDirectionHorizontal(int fixedHeight) {
        mCurtainViewContainer.setLayoutDirectionHorizontal(fixedHeight);
    }

}

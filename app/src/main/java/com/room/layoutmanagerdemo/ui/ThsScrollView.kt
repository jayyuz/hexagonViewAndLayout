package com.room.layoutmanagerdemo.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.NestedScrollingParent
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import com.room.layoutmanagerdemo.R

class ThsScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), NestedScrollingParent {

    private var targetViewOnDrag: Boolean = false
    private var interceptTargetViewTouch: Boolean = false
    private var childHeight: Int = 0
    private val TAG = "ThsScrollView"
    private var mLastY: Float = 0f;
    private lateinit var mScroller: OverScroller
    private lateinit var mVelocityTracker: VelocityTracker

    private var targetViewNormalY = 0
    private var targetViewBottomY = 0
    private lateinit var targetView: View
    private lateinit var tabView: View
    private var shouldFixedInBottom = true
    private var shouldFixedInTop = false

    init {
        context.resources;
        mScroller = OverScroller(context)
        mVelocityTracker = VelocityTracker.obtain()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var modeW = MeasureSpec.getMode(widthMeasureSpec)
        var modeH = MeasureSpec.getMode(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        forEach {
            if (it.id == R.id.targetView) {
                val heightMS = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
                Log.i(TAG, "onMeasure: height=${height}, measuredHeight=${measuredHeight}")
                measureChild(it, widthMeasureSpec, heightMS)
            } else {
                measureChild(it, widthMeasureSpec, heightMeasureSpec)
            }
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            // 真正fling滚动中
            scrollTo(0, mScroller.getCurrY());
            if (mScroller.currY > targetViewNormalY + targetView.measuredHeight) {

            }
            if (mScroller.currY > targetViewBottomY) {
                shouldFixedInBottom = false
            } else {
                shouldFixedInBottom = true
            }
            if (shouldFixedInBottom) {
                targetView.layout(
                    0,
                    height - tabView.measuredHeight + scrollY,
                    width,
                    height - tabView.measuredHeight + targetView.measuredHeight + scrollY
                )
            } else {
                targetView.layout(
                    0, targetViewNormalY, width, targetViewNormalY + targetView.height
                )
            }
            postInvalidate();
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        childHeight = 0;
        forEachIndexed { index, view ->
            view.layout(l, childHeight, r, childHeight + view.measuredHeight)
            if (view.id == R.id.targetView) {
                targetView = view
                targetViewNormalY = childHeight
                tabView = targetView.findViewById(R.id.tabView)
                targetViewBottomY = targetViewNormalY - measuredHeight + tabView.measuredHeight
                if (shouldFixedInBottom) {
                    view.layout(
                        l,
                        height - tabView.measuredHeight + scrollY,
                        r,
                        height - tabView.measuredHeight + targetView.measuredHeight + scrollY
                    )
                }
            }
            childHeight += view.measuredHeight
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            mScroller.forceFinished(true)
            if (targetView.y.toInt() != targetViewNormalY) {
                if (ev.y > height - tabView.height) {
                    // targetView位于底部, 拦截掉触摸事件
                    interceptTargetViewTouch = true
                    shouldFixedInBottom = false
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i(TAG, "onTouchEvent: event.action:${event.action}, event.y:${event.y}")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                targetViewOnDrag = false
                mLastY = event.y
                mVelocityTracker.clear()
                mVelocityTracker.addMovement(event)
                mScroller.forceFinished(true)
            }

            MotionEvent.ACTION_MOVE -> {
                var deltaY = event.y - mLastY
                if (interceptTargetViewTouch) {
                    // 布局targetView
                    var offset = (deltaY + targetView.y).toInt()
                    if (offset < scrollY) {
                        offset = scrollY
                    }
                    targetViewOnDrag = true
                    targetView.layout(
                        0, offset, width,
                        offset + targetView.height
                    )
                    Log.i(TAG, "layoutBy: touch event, offset=$offset")
                } else {
                    targetViewOnDrag = false
                    scrollBy(0, -deltaY.toInt())
                    mVelocityTracker.addMovement(event)
                    Log.i(TAG, "scrollBy: touch event")
                }
                mLastY = event.y
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mLastY = 0f
                if (!interceptTargetViewTouch) {
                    mVelocityTracker.addMovement(event)
                    mVelocityTracker.computeCurrentVelocity(-1000)
                    val velocity = mVelocityTracker.yVelocity
                    var maxY = 0.coerceAtLeast(childHeight - height) // 计算最大滚动距离
                    Log.i(
                        TAG,
                        "onTouchEvent: maxY=$maxY, childHeight - height - scrollY=${childHeight - height - scrollY}, targetViewNormalY=$targetViewNormalY, targetViewHeight=${targetView.height}"
                    )
                    mScroller.fling(0, scrollY, 0, velocity.toInt(), 0, 0, 0, maxY);
                    invalidate();
                }
                interceptTargetViewTouch = false;

                if (targetViewOnDrag) {
                    Log.i(TAG, "targetViewOnDrag, scrollY=$scrollY, targetView.y=${targetView.y.toInt()}")
                    if (scrollY <= targetView.y.toInt()) {
//                        shouldFixedInTop = true
                        shouldFixedInTop = false
//                        shouldFixedInBottom = false
                    }
                }
                targetViewOnDrag = false
            }
        }

        if (shouldFixedInTop) {
            targetView.layout(
                0,
                scrollY,
                width,
                targetView.measuredHeight + scrollY
            )
        } else {
            if (scrollY > targetViewBottomY) {
                shouldFixedInBottom = false
            } else {
                shouldFixedInBottom = true
            }
            if (shouldFixedInBottom && !interceptTargetViewTouch) {
                targetView.layout(
                    0,
                    height - tabView.measuredHeight + scrollY,
                    width,
                    height - tabView.measuredHeight + targetView.measuredHeight + scrollY
                )
            } else {
                if (!interceptTargetViewTouch) {
                    targetView.layout(
                        0, targetViewNormalY, width, targetViewNormalY + targetView.height
                    )
                }
            }
        }
        return true
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        super.onNestedScrollAccepted(child, target, axes)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
//        super.onNestedPreScroll(target, dx, dy, consumed)
        // scrollY + height < targetViewNormalY + targetView.height
        //
        // 1. targetView 处于线性布局正常位置, 事件完全被 parentView 消耗
        // 2. targetView 不处于线性布局正常位置, 事件先让 targetView 消耗

        // targetView 固定在底部:
        //   height - tabView.measuredHeight + scrollY == targetView.Y != targetViewNormalY
        // targetView 跟随:
        //   targetView.Y  == targetViewNormalY

        if (isTargetViewAllShowed()) {
            // 说明没有将 targetView 完全显示出来
            val maxCanScroll = targetViewNormalY + targetView.height - (scrollY + height)
            val canScroll = maxCanScroll.coerceAtMost(dy)
            consumed[1] = canScroll
            scrollBy(0, canScroll)
            Log.i(TAG, "scrollBy1: dx=$dx, dy=$dy, canScroll=$canScroll, not show all, consume all")
        }
    }

    private fun isTargetViewAllShowed() =
        targetView.y.toInt() == targetViewNormalY && scrollY + height < targetViewNormalY + targetView.height

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        Log.i(
            TAG,
            "onNestedScroll: dyConsumed=$dyConsumed, dyUnconsumed=$dyUnconsumed, dxUnconsumed=$dxUnconsumed, dyUnconsumed=$dyUnconsumed"
        )
        Log.i(
            TAG,
            "measure: scrollY=$scrollY, height=$height, childHeight=$childHeight, scrollY + height=${scrollY + height}"
        )
        if (dyUnconsumed < 0) {
            scrollBy(0, dyUnconsumed)
            Log.i(
                TAG, "shouldConsume: dyUnconsumed=$dyUnconsumed"
            )
        }
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        if (scrollY != childHeight - height) {
            // 说明targetView位于顶部，完全暴露出来了
            Log.i(
                TAG, "xxx1"
            )
            val maxY = 0.coerceAtLeast(childHeight - height) // 计算最大滚动距离
            mScroller.fling(0, scrollY, 0, velocityY.toInt(), 0, 0, 0, maxY);
            invalidate();
            return true
        } else {
            Log.i(
                TAG, "xxx2"
            )
            return false
        }
//        return super.onNestedPreFling(target, velocityX, velocityY)
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        Log.i(
            TAG,
            "onNestedFling: scrollY=$scrollY, height=$height, velocityX=$velocityX, velocityY=${velocityY}, consumed=$consumed,  childHeight - height=${childHeight - height}"
        )

        if (scrollY != childHeight - height) {
            // 说明targetView位于顶部，完全暴露出来了
            if (velocityY < 0 && scrollY != childHeight - height) {
                val maxY = 0.coerceAtLeast(childHeight - height) // 计算最大滚动距离
                mScroller.fling(0, scrollY, 0, velocityY.toInt(), 0, 0, 0, maxY);
                invalidate();
                return true
            }
        } else {
            // 没有完全露出来
            return true
        }

        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }
}
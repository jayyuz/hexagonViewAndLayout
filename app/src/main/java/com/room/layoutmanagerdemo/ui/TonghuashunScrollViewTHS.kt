package com.room.layoutmanagerdemo.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.widget.THSNestedScrollView
import androidx.core.widget.THSNestedScrollView.OnScrollChangeListener
import com.room.layoutmanagerdemo.R

class TonghuashunScrollViewTHS : THSNestedScrollView {
    private var isDraggingTargetView: Boolean = false;
    private var startAtBottom: Boolean = false
    private var startAtTop: Boolean = false
    private var showAtTop: Boolean = false
    private var lastPressY: Float = 0f
    private var targetDownY: Float = 0f

    //    private var localY: Float = 0f
    private var targetView: View? = null
    private var innerOnScrollChangeListener: OnScrollChangeListener? = null
    private var onScrollChangeListener: OnScrollChangeListener? = null
    private var offsetToTopAnimator: ObjectAnimator? = null
    private var offsetToBottomAnimator: ObjectAnimator? = null
    private var bottomShowHeight = 0
    private var moved = false
    private var isFirstLayout = true
    private var moveToTopOffset: Int = 0
        set(value) {
            targetView?.apply {
                y = value.toFloat()
            }
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        setOnScrollChangeListener(OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            onScrollChangeListener!!.onScrollChange(v, scrollX, scrollY, oldScrollX, oldScrollY)

            if (targetView != null) {

                var local = IntArray(2)
                targetView!!.getLocationInWindow(local)
                val targetViewYInWindow = local[1]
                getLocationInWindow(local)
                val localYInWindow = local[1]
                Log.e(
                    TAG,
                    String.format("onScrollChange: scrollY=${scrollY},targetView!!.top=${targetView!!.top},showAtTop=$showAtTop,targetViewYInWindow=$targetViewYInWindow,localYInWindow=$localYInWindow")
                )
                if (targetViewYInWindow != localYInWindow) {
                    if (showAtTop) {
                        targetView!!.y = scrollY.toFloat();
                    } else {
                        if (scrollY < targetView!!.top - height + bottomShowHeight) {
                            // 固定在底部
                            targetView!!.y = (scrollY + height - bottomShowHeight).toFloat()
                        } else {
                            Log.e(
                                TAG,
                                String.format("onScrollChange: follow")
                            )
                            // 放置到正确的布局位置
                            targetView!!.y = targetView!!.top.toFloat()
                        }
                    }
                }
            }
        }.also { innerOnScrollChangeListener = it })
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        targetView = findViewById(R.id.LinearLayoutMore)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
//        getLocationInWindow()
        val v = findViewById<View>(android.R.id.content);
//        Log.e(TAG, "onLayout: contentView:,w = ${v.width}, h = ${v.height}" )
        if (isFirstLayout) {
            isFirstLayout = false
            bottomShowHeight = targetView!!.findViewById<View>(R.id.tv_content).height + paddingBottom
            targetView!!.y = (height - bottomShowHeight).toFloat()
        }
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?
    ): Boolean {
        Log.e(
            TonghuashunLinearLayout.TAG,
            "dispatchNestedScroll:, ${lastPressY.toInt()},${scrollY == targetView!!.y.toInt()}"
        )
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        Log.e(
            TonghuashunLinearLayout.TAG, "onNestedScroll1:, ${lastPressY.toInt()},${scrollY == targetView!!.y.toInt()}"
        )
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        val ret = super.onStartNestedScroll(child, target, axes, type);
        Log.e(
            TonghuashunLinearLayout.TAG, "onStartNestedScroll:, ${scrollY.toInt()},${targetView!!.y.toInt()},ret=$ret"
        )
        return ret;
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)
        Log.e(
            TonghuashunLinearLayout.TAG,
            "onNestedPreScroll:, dx:${dx},dy:${dy} " +
                    ""
        )
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        Log.e(
            TonghuashunLinearLayout.TAG,
            "onNestedScroll2:, scrollY:${scrollY.toInt()},targetView.y:${targetView!!.y.toInt()} " +
                    "dxConsumed:$dxConsumed,dyConsumed:$dyConsumed,dxUnconsumed:$dxUnconsumed,dyUnconsumed:$dyUnconsumed" +
                    ""
        )
        var local = IntArray(2)
        targetView!!.getLocationInWindow(local)
        val targetViewYInWindow = local[1]
        getLocationInWindow(local)
        val localYInWindow = local[1]
        Log.e(
            TonghuashunLinearLayout.TAG,
            "onNestedScroll21:, localYInWindow=${localYInWindow}, targetViewYInWindow=${targetViewYInWindow}"
        )
        if (targetViewYInWindow == localYInWindow) {
            // 在顶部
            Log.e(
                TonghuashunLinearLayout.TAG, "onNestedScroll23: consume"
            )
//            targetView!!.y -= dyUnconsumed
//            y -= dyUnconsumed;
            var scrollYOld = scrollY;
            targetView!!.y = (scrollY + dyUnconsumed).toFloat();
            val consumedY = scrollY - scrollYOld;
            if (consumed != null) {
                consumed[1] += dyUnconsumed
            }
//            Log.e(
//                TonghuashunLinearLayout.TAG, "onNestedScroll24: consume=$consumedY"
//            )
            super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
        } else {
            Log.e(
                TonghuashunLinearLayout.TAG, "onNestedScroll22: not consume,${scrollY - dyUnconsumed}"
            )
//            if (showAtTop) {
//                consumed[1] += dyUnconsumed
//            } else {
            targetView!!.y += (-dyUnconsumed).toFloat();
            if (consumed != null) {
                consumed[1] += dyUnconsumed
            }
            moved = true;
            super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
//            }
        }
    }

    override fun setOnScrollChangeListener(l: OnScrollChangeListener?) {
        if (l === innerOnScrollChangeListener || innerOnScrollChangeListener == null) {
            super.setOnScrollChangeListener(l)
            return
        }
        onScrollChangeListener = l
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.e(
            TonghuashunLinearLayout.TAG,
            "onInterceptTouchEvent: ${ev.action}"
        )

        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.e(
            TonghuashunLinearLayout.TAG,
            "dispatchTouchEvent: ${ev.action}"
        )
        if (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_CANCEL) {
            // 归位
            moveToRightPosition()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastPressY = event.y
                targetDownY = targetView!!.y
                val max = targetDownY + bottomShowHeight - scrollY;
                val min = targetDownY - scrollY;
                Log.e(
                    TonghuashunLinearLayout.TAG,
                    "onTouchEvent:ACTION_DOWN max=${max}, min=${min}, targetDownY=${targetDownY}, lastPressY=${lastPressY}, isInRange=${lastPressY > min && lastPressY < max}"
                )
                // 说明在点击位置在targetView的范围内,需要跟随
                if (lastPressY > min && lastPressY < max) {
                    isDraggingTargetView = true
                    var local = IntArray(2)
                    getLocationInWindow(local)
                    val localYTop = local[1]
                    targetView!!.getLocationInWindow(local)
                    val targetViewYTop = local[1]
                    if (localYTop == targetViewYTop) {
                        startAtTop = true
                    } else if (targetViewYTop - localYTop == height) {
                        startAtBottom = true
                    }
                } else {
                    isDraggingTargetView = false
                }
            }


            MotionEvent.ACTION_MOVE -> {
//                Log.e(
//                    TonghuashunLinearLayout.TAG,
//                    "onTouchEvent:ACTION_MOVE event.y=${event.y.toInt()}, lastPressY=${lastPressY.toInt()},scrollY=${scrollY},targetView!!.y=${targetView!!.y},${scrollY == targetView!!.y.toInt()}"
//                )
                if (isDraggingTargetView) {
                    val offset = event.y - lastPressY
                    Log.e(
                        TonghuashunLinearLayout.TAG,
                        "onTouchEvent:ACTION_MOVE event.y=${event.y.toInt()}, lastPressY=${lastPressY.toInt()}, offset=${offset}, scrollY=${scrollY}, offset + scrollY=${offset + scrollY}"
                    )
                    moved = true
                    if (startAtTop) {
                        // 点击下去的时候,view位于顶部
                        targetView!!.y = offset + scrollY
                    } else {
                        // 点击下去的时候,view位于底部
                        targetView!!.y = targetDownY + offset
                    }
                    Log.e(
                        TonghuashunLinearLayout.TAG, "onTouchEvent:ACTION_MOVE targetView!!.y=${targetView!!.y}"
                    )
                    return true
                } else {
                    return super.onTouchEvent(event)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                moveToRightPosition();
                Log.e(
                    TonghuashunLinearLayout.TAG, "onTouchEvent: is equal ${targetView!!.y},${scrollY.toInt()}"
                )
            }
        }
        return super.onTouchEvent(event)
    }

    private fun moveToRightPosition() {
        if (moved && targetView!!.y < height / 2 + scrollY) {
            offsetToTopAnimator = ObjectAnimator.ofInt(
                this@TonghuashunScrollViewTHS, "moveToTopOffset", targetView!!.y.toInt(), 0 + scrollY
            ).apply {
                duration = 600
                interpolator = AccelerateDecelerateInterpolator()
                start()
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) = Unit


                    override fun onAnimationEnd(animation: Animator) {
                        showAtTop = true
                    }


                    override fun onAnimationCancel(animation: Animator) {
                        showAtTop = false
                    }

                    override fun onAnimationRepeat(animation: Animator) = Unit
                })
            }
        } else if (moved && targetView!!.y > height / 2 + scrollY) {
            offsetToBottomAnimator = ObjectAnimator.ofInt(
                this@TonghuashunScrollViewTHS,
                "moveToTopOffset",
                targetView!!.y.toInt(),
                height - bottomShowHeight + scrollY
            ).apply {
                var x = (600 * (height - (targetView!!.y.toInt() - scrollY)) / height).toLong()
                if (x > 0) {
                    duration = x
                } else {
                    duration = 100
                }
                interpolator = AccelerateDecelerateInterpolator()
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) = Unit


                    override fun onAnimationEnd(animation: Animator) {
                        showAtTop = false
                    }


                    override fun onAnimationCancel(animation: Animator) {
                        showAtTop = false
                    }

                    override fun onAnimationRepeat(animation: Animator) = Unit
                })
                start()
            }
        }
        moved = false
        startAtTop = false
    }

    companion object {
        private const val TAG = "TonghuashunScrollView"
    }
}

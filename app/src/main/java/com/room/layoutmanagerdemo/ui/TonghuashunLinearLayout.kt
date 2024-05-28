package com.room.layoutmanagerdemo.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout

class TonghuashunLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var lastPressY: Float = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                lastPressY = event.y
//                return true
//            }
//
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                val offset = event.y - lastPressY
//                Log.e(TAG, "onTouchEvent: ${event.y}, $lastPressY")
//                return true
//            }
//        }
//        return super.onTouchEvent(event)
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e("THS", "onMeasureTHS: contentView:,w = ${MeasureSpec.getSize(widthMeasureSpec)}, h = ${MeasureSpec.getSize(heightMeasureSpec)}" )
    }
    companion object {
        val TAG = "Tonghuashun"
    }
}
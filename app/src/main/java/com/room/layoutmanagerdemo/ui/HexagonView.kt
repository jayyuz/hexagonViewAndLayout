package com.room.layoutmanagerdemo.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.room.layoutmanagerdemo.R

class HexagonView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    private val TAG = "CardItemView"
    private val Z3 = 1.732f
    private val H3 = 0.866f
    private val mSize: Int
    private val mRadius: Int
    private val mPaint: Paint
    private val mDrawPath: Path
    private val mRegion: Region

    constructor(context: Context) : this(context, null, 0) {
        Log.e(TAG, "constructor:")
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        Log.e(TAG, "CardItemView:2")
    }

    init {
        Log.e(TAG, "constructor:3")
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.FILL
        val ta = context.obtainStyledAttributes(attrs, R.styleable.HexagonView, defStyleAttr, 0)
        mSize = ta.getDimensionPixelSize(R.styleable.HexagonView_size, 10)
        mRadius = mSize / 2
        mPaint.color = ta.getColor(R.styleable.HexagonView_bgColor, 0)
        ta.recycle()
        mRegion = Region()
        mDrawPath = Path()
        val yOffset = (1 - H3) * mRadius
        mDrawPath.moveTo((mRadius / 2).toFloat(), yOffset)
        mDrawPath.lineTo(mRadius * 1.5f, yOffset)
        mDrawPath.lineTo(mRadius * 2.0f, mRadius.toFloat())
        mDrawPath.lineTo(mRadius * 1.5f, Z3 * mRadius + yOffset)
        mDrawPath.lineTo(mRadius * 0.5f, Z3 * mRadius + yOffset)
        mDrawPath.lineTo(0f, mRadius.toFloat())
        mDrawPath.close()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mSize, mSize)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (!isEventInPath(event)) {
                return false
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnimation()
    }

    private fun isEventInPath(event: MotionEvent): Boolean {
        val bounds = RectF()
        mDrawPath.computeBounds(bounds, true)
        mRegion.setPath(
            mDrawPath,
            Region(bounds.left.toInt(), bounds.top.toInt(), bounds.right.toInt(), bounds.bottom.toInt())
        )
        return mRegion.contains(event.x.toInt(), event.y.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawPath(mDrawPath, mPaint)
    }

    fun setCardColor(color: Int) {
        mPaint.color = color
        invalidate()
    }
}
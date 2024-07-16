package com.room.layoutmanagerdemo.ui.navigation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import kotlin.math.absoluteValue


class NavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val diff = -20F
        val diffL = diff.absoluteValue / 6
        val diffR = -diffL
        val c1 = Calculator.calculateCenterControlPoint(Point(100, 100), Point(300, 100), diff)
        path.reset()
        path.moveTo(40F, 130F)

        val c2 = Calculator.calculateEdgeControlPoint(Point(40, 130), Point(100, 100), diffL)
        val c3 = Calculator.calculateEdgeControlPoint(Point(300, 100), Point(360, 130), diffR)
        path.quadTo(c2.x.toFloat(), c2.y.toFloat(), 100F, 100F)
        path.quadTo(c1.x.toFloat(), c1.y.toFloat(), 300F, 100F)
        path.quadTo(c3.x.toFloat(), c3.y.toFloat(), 360F, 130F)
        path.lineTo(200F, 490F)
        path.close()
        canvas.drawPath(path, paint)
    }
}
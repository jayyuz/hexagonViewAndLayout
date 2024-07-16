package com.room.layoutmanagerdemo.ui.navigation


import android.graphics.Point
import android.util.Log
import androidx.core.graphics.minus
import androidx.core.graphics.plus

object Calculator {
    fun calculateEdgeControlPoint(p1: Point, p2: Point, fraction: Float): Point {
        var p3: Point = (p2 - p1) / 2 + p1
        val k1: Float = (p2.y.toFloat() - p1.y) / (p2.x - p1.x)
        var k2: Float = 1F
        Log.e("Calculator", "calculateEdgeControlPoint: $k1, ${p2.y}, ${p1.y}, ${p2.y - p1.y} ,${p2.x - p1.x}")
        k2 = -1 / k1
        val b2 = p3.y - p3.x * k2
        val x4 = p3.x + fraction
        val y4 = x4 * k2 + b2
        val p4 = Point(x4.toInt(), y4.toInt())
        return p4;
    }

    fun calculateCenterControlPoint(p1: Point, p2: Point, fraction: Float): Point {
        val center = p2 - p1
        return Point(center.x, (fraction + center.y).toInt())
    }
}


internal operator fun Point.div(i: Int): Point {
    return Point(x, y).apply {
        x /= i
        y /= i;
    }
}
package com.room.layoutmanagerdemo.ui.navigation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


class BetterNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var radius: Float = 100F
    private var sweepAngle: Float = 120f
    private val TAG = "BetterNavigationView"
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()

    private var totalLength = 300
    private var screenHeight = 0

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        path.reset()
        screenHeight = height
        paint.color = Color.RED
        totalLength = (300 + sweepAngle / 180 * 200).toInt()
        val screenWidth = width
        val screenHeight = height
        // 创建一个矩阵
        val matrix = Matrix()
        // 平移原点到左下角
        matrix.postTranslate(0f, screenHeight.toFloat())
        // 反转 Y 轴
        matrix.postScale(1f, -1f, 0f, screenHeight.toFloat())
        // 将矩阵应用到 Canvas
        canvas.setMatrix(matrix)

        // 建立笛卡尔坐标系，以View左侧底部为坐标原点，向上为y轴坐标正方向，向右为x轴坐标正方向
        val circle = Circle(250F, 150F, radius)
        val halfSweepAngle = (sweepAngle / 2).toDouble()

        // 圆所在的矩形
        val rectCircle = RectF(circle.h - circle.r, circle.k - circle.r, circle.h + circle.r, circle.k + circle.r)

        val quadLineXLength = (totalLength - circle.r * sin(Math.toRadians(halfSweepAngle)) * 2) / 2
        val quadLineYHeight = circle.r * 0.2F

        // 计算左侧相交点坐标 x0 (其实可以用对称轴的方式计算，更加节省算力)
        var leftConnectPoint = Point(
            (circle.h - sin(Math.toRadians(halfSweepAngle)) * circle.r).toInt(),
            (circle.k + cos(Math.toRadians(halfSweepAngle)) * circle.r).toInt()
        )
        // 计算左侧控制点 x2
        val leftHorizonPoint =
            Point((leftConnectPoint.x - quadLineXLength).toInt(), leftConnectPoint.y - quadLineYHeight.toInt())
        // 相交点在圆上的导数
        val dLeftConnectPoint = -(leftConnectPoint.x - circle.h) / (leftConnectPoint.y - circle.k)
        // 这里需要注意导数和右侧的不同
        var leftControlPoint = Point(x.toInt(), y.toInt())
        calculateControlPoint(-0.9F, dLeftConnectPoint, leftHorizonPoint, leftConnectPoint, leftControlPoint)
        Log.e(TAG, "onDraw: dLeftConnectPoint=$dLeftConnectPoint, leftConnectPoint=$leftConnectPoint")

        val txLeft =
            -(leftControlPoint.x - leftHorizonPoint.x).toFloat() / (leftConnectPoint.x - 2 * leftControlPoint.x + leftHorizonPoint.x)
        val tyLeft =
            -(leftControlPoint.y - leftHorizonPoint.y).toFloat() / (leftConnectPoint.y - 2 * leftControlPoint.y + leftHorizonPoint.y)

        // 计算右相交点坐标 x0
        var rightConnectPoint = Point(
            (circle.h + sin(Math.toRadians(halfSweepAngle)) * circle.r).toInt(),
            (circle.k + cos(Math.toRadians(halfSweepAngle)) * circle.r).toInt()
        )
        // 计算右侧控制点 x2
        val rightHorizonPoint =
            Point((rightConnectPoint.x + quadLineXLength).toInt(), rightConnectPoint.y - quadLineYHeight.toInt())
        // 相交点在圆上的导数
        val dRightConnectPoint = -(rightConnectPoint.x - circle.h) / (rightConnectPoint.y - circle.k)

        var rightControlPoint = Point(x.toInt(), y.toInt())
        calculateControlPoint(dRightConnectPoint, 0.9F, rightConnectPoint, rightHorizonPoint, rightControlPoint)

        Log.e(
            TAG,
            "onDraw: dRightConnectPoint=$dRightConnectPoint, \nrightConnectPoint=$rightConnectPoint, \nrightHorizonPoint=$rightHorizonPoint, \nrightControlPoint=$rightControlPoint"
        )
        // 计算B(t)= 0 时，t的值
        // t = 2*(P0 - P1)/(P0 - 2*P1 +P2)

        val txRight =
            -(rightControlPoint.x - rightConnectPoint.x).toFloat() / (rightHorizonPoint.x - 2 * rightControlPoint.x + rightConnectPoint.x)
        val tyRight =
            -(rightControlPoint.y - rightConnectPoint.y).toFloat() / (rightHorizonPoint.y - 2 * rightControlPoint.y + rightConnectPoint.y)
        Log.e(TAG, "onDraw: txRight=$txRight,tyRight=$tyRight")

        // 计算B(t) = 0 时，y的值
        // B(t) = (1 - t)^2 * P0 + 2 * (1 - t) * t * P1 + t^2 * P2
        var rightDYEqual0Y = (1 - tyRight).toDouble().pow((2).toDouble())
                .toFloat() * rightConnectPoint.y + 2 * (1 - tyRight) * tyRight * rightControlPoint.y + tyRight.toDouble()
                .pow(2) * rightHorizonPoint.y
//        rightDYEqual0Y -= 10

        val rightDYEqual0X = (1 - tyRight).toDouble().pow((2).toDouble())
                .toFloat() * rightConnectPoint.x + 2 * (1 - tyRight) * tyRight * rightControlPoint.x + tyRight.toDouble()
                .pow(2) * rightHorizonPoint.x

        var leftDYEqual0Y = (1F - tyLeft).toDouble()
                .pow((2).toDouble()) * leftHorizonPoint.y + 2 * (1F - tyLeft) * tyLeft * leftControlPoint.y + tyLeft.toDouble()
                .pow(2) * leftControlPoint.y
//        leftDYEqual0Y -= 10

        val leftDYEqual0X = (1 - tyLeft).toDouble().pow((2).toDouble())
                .toFloat() * leftHorizonPoint.x + 2 * (1 - tyLeft) * tyLeft * leftControlPoint.x + tyLeft.toDouble()
                .pow(2) * leftControlPoint.x

        Log.e(TAG, "onDraw: dYEqual0=$rightDYEqual0Y, leftDYEqual0Y=$leftDYEqual0Y, leftDYEqual0X=$leftDYEqual0X")

        path.reset()
//        path.moveTo(0F, 0F)
//        path.addArc(rectCircle, 90F - halfSweepAngle.toFloat(), sweepAngle)
        var lPath = Path()
        lPath.moveTo(leftHorizonPoint.x.toFloat(), leftHorizonPoint.y.toFloat())
        lPath.quadTo(
            leftControlPoint.x.toFloat(),
            leftControlPoint.y.toFloat(),
            leftConnectPoint.x.toFloat(),
            leftConnectPoint.y.toFloat()
        )
        lPath = truncatePath(lPath, tyLeft, 1F)
        val startPoint = getPointOnPath(lPath, 0F)
        Log.e(
            TAG,
            "onDraw: startPoint=$startPoint, \nleftConnectPoint=$leftConnectPoint, \nleftHorizonPoint=$leftHorizonPoint"
        )
        path.addPath(lPath)
//        path.lineTo(leftDYEqual0X.toFloat(), leftDYEqual0Y.toFloat() - 20)
        path.lineTo(circle.h, startPoint!!.y)
//        path.lineTo(rightConnectPoint.x.toFloat(), rightConnectPoint.y.toFloat())
//        path.lineTo(circle.h, rightDYEqual0Y.toFloat())
//        path.close()
//        canvas.drawPath(path, paint)

//        val nPath = Path()
//        nPath.moveTo(rightConnectPoint.x.toFloat(), rightConnectPoint.y.toFloat())
//        var rPath = Path()
//        rPath.quadTo(
//            rightControlPoint.x.toFloat(),
//            rightControlPoint.y.toFloat(),
//            rightHorizonPoint.x.toFloat(),
//            rightHorizonPoint.y.toFloat()
//        )
////        rPath = truncatePath(rPath, 0F, tyRight)
//        nPath.addPath(rPath)
//        nPath.lineTo(circle.h, circle.k)
////        path.lineTo(rightConnectPoint.x.toFloat(), rightConnectPoint.y.toFloat())
//        nPath.close()
//        canvas.drawPath(nPath, paint)

//        path.reset()
//        path.moveTo(rightConnectPoint.x.toFloat(), rightConnectPoint.y.toFloat())
//        path.lineTo(rightControlPoint.x.toFloat(), rightControlPoint.y.toFloat())
//        path.lineTo(rightHorizonPoint.x.toFloat(), rightHorizonPoint.y.toFloat())
//        path.close()
//        paint.color = Color.RED
//        canvas.drawPath(path, paint)

//        paint.color = Color.BLUE
//        path.reset()
        var rPath = Path()
        path.moveTo(rightConnectPoint.x.toFloat(), rightConnectPoint.y.toFloat())
        rPath.moveTo(rightConnectPoint.x.toFloat(), rightConnectPoint.y.toFloat())
        rPath.quadTo(
            rightControlPoint.x.toFloat(),
            rightControlPoint.y.toFloat(),
            rightHorizonPoint.x.toFloat(),
            rightHorizonPoint.y.toFloat()
        )
        rPath = truncatePath(rPath, 0F, tyRight)
        path.addPath(rPath)
        path.lineTo(circle.h, startPoint!!.y)
        path.lineTo(leftConnectPoint.x.toFloat(), leftConnectPoint.y.toFloat())
        path.lineTo(rightConnectPoint.x.toFloat(), rightConnectPoint.y.toFloat())
        path.close()
        canvas.drawPath(path, paint)

//        canvas.drawArc(rectCircle, 90F - halfSweepAngle.toFloat(), sweepAngle, true, paint)

//        paint.color = Color.CYAN
//        canvas.drawLine(
//            centerPoint.x.toFloat(),
//            centerPoint.y.toFloat(),
//            controlPoint.x.toFloat(),
//            controlPoint.y.toFloat(),
//            paint
//        )

//        paint.color = Color.BLACK
//        canvas.drawLine(
//            rightConnectPoint.x.toFloat(),
//            rightConnectPoint.y.toFloat(),
//            rightControlPoint.x.toFloat(),
//            rightControlPoint.y.toFloat(),
//            paint
//        )
//        paint.color = Color.BLUE
//        canvas.drawLine(
//            rightControlPoint.x.toFloat(),
//            rightControlPoint.y.toFloat(),
//            rightHorizonPoint.x.toFloat(),
//            rightHorizonPoint.y.toFloat(),
//            paint
//        )

//        paint.color = Color.BLACK
//        // 现在可以使用笛卡尔坐标系绘制
//        canvas.drawLine(
//            0f,
//            0f,
//            rightConnectPoint.x.toFloat(),
//            rightConnectPoint.y.toFloat(),
//            paint
//        ) // 画一条从 (0, 0) 到 (100, 100) 的线
    }

    fun setAngle(angle: Int) {
        this.sweepAngle = angle.toFloat()
        this.radius = 40 + (1 - angle.toFloat() / 180) * 120
        Log.e(TAG, "setAngle: $angle, radius=$radius")
        postInvalidate()
    }

    private fun calculateControlPoint(
        m0: Float,
        m2: Float,
        firstPoint: Point,
        thirdPoint: Point,
        controlPoint: Point
    ) {
        val x0 = firstPoint.x.toFloat()
        val y0 = firstPoint.y.toFloat()
        val x2 = thirdPoint.x.toFloat()
        val y2 = firstPoint.y.toFloat()
        val x1 = (y2 - y0 + m0 * x0 - m2 * x2) / (m0 - m2)
        val y1 = y0 + m0 * (x1 - x0)
        controlPoint.x = x1.toInt()
        controlPoint.y = y1.toInt()
//        Log.e(TAG, "onDraw: x1=$x1,y1=$y1, controlPoint=${controlPoint.y}, rightConnectPoint=${firstPoint.y}")
    }


    /**
     * 截断一个Path
     * @param originalPath 原始的Path
     * @param startD 截断的起始位置（0.0 - 1.0）
     * @param stopD 截断的结束位置（0.0 - 1.0）
     * @return 截断后的Path
     */
    fun truncatePath(originalPath: Path, startD: Float, stopD: Float): Path {
        val pathMeasure = PathMeasure(originalPath, false)
        val length = pathMeasure.length

        // 计算截断的实际位置
        val start = startD * length
        val stop = stopD * length
        val truncatedPath = Path()
        pathMeasure.getSegment(start, stop, truncatedPath, true)
        return truncatedPath
    }

    /**
     * 获取Path上某一个位置的坐标
     * @param path 原始的Path
     * @param distance 距离起点的距离（0.0 - path的总长度）
     * @return 该位置的坐标
     */
    fun getPointOnPath(path: Path?, distance: Float): PointF? {
        val pathMeasure = PathMeasure(path, false)
        val pos = FloatArray(2)

        // 获取路径上指定距离的坐标
        return if (pathMeasure.getPosTan(distance, pos, null)) {
            PointF(pos[0], pos[1])
        } else {
            null // 如果距离超出路径长度，返回null
        }
    }

    fun cartesianToScreen(cartesian: Point): Point {
        val screenX = cartesian.x
        val screenY = screenHeight - cartesian.y
        return Point(screenX, screenY)
    }

    fun cartesianYToScreen(y: Float): Float {
        return screenHeight - y
    }
}

data class Circle(var h: Float, var k: Float, var r: Float)


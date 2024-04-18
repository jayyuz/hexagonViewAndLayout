package com.room.layoutmanagerdemo.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep
import androidx.core.graphics.drawable.toBitmap
import com.room.layoutmanagerdemo.R
import kotlin.properties.Delegates

class MIUICleanBallView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var colorTransform: FloatArray = floatArrayOf()
    private var colorMatrix: ColorMatrix = ColorMatrix()
    private var bitmapPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rocketAnimator: ObjectAnimator? = null
    private lateinit var shadowRadialGradient: RadialGradient
    private lateinit var centerRadialGradient: RadialGradient
    private lateinit var outerRadialGradient: RadialGradient
    private var ballRadius: Int = 0
    private val ballFraction = 0.25f
    private val centerFraction = 124 / 152f
    private val outerFraction = 152 / 152f
    private val shadowFraction = 200 / 152f
    private val bitmapFraction = 0.85f
    private var centerColor: Int = Color.parseColor("#4263ef")//20
    private var outerColor: Int = Color.parseColor("#5363ef")//26
    private var shadowColor: Int = Color.parseColor("#5363ef")//30
    private var centerX by Delegates.notNull<Int>()
    private var centerY by Delegates.notNull<Int>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var shadowOffsetY = 0
    private var rocketBitmap: Bitmap? = null

    private var rocketOffsetY = 0f

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.shader = shadowRadialGradient
        canvas.drawCircle(
            centerX.toFloat(), centerY.toFloat() + shadowOffsetY, ballRadius.toFloat() * shadowFraction, paint
        )

        paint.shader = null
        var startColor = Color.parseColor("#55dddddd")
        paint.color = startColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.color = Color.WHITE
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), ballRadius.toFloat(), paint)

        paint.shader = outerRadialGradient
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), outerFraction * ballRadius, paint)

        paint.shader = centerRadialGradient
        paint.color = centerColor
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), centerFraction * ballRadius, paint)

        rocketBitmap?.let {
            canvas.drawBitmap(
                it,
                (centerX - it.width / 2).toFloat(),
                (centerY - it.height / 2 + rocketOffsetY * it.height * 0.25).toFloat(),
                bitmapPaint
            )
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ballRadius = (Math.min(w, h) * ballFraction).toInt()
        centerX = w / 2
        centerY = h / 2
        var startColor = Color.argb(0x20, Color.red(centerColor), Color.green(centerColor), Color.blue(centerColor))
        centerRadialGradient = RadialGradient(
            centerX.toFloat(),
            centerY.toFloat(),
            centerFraction * ballRadius,
            startColor,
            Color.TRANSPARENT,
            Shader.TileMode.REPEAT
        )
        startColor = Color.argb(0x40, Color.red(outerColor), Color.green(outerColor), Color.blue(outerColor))
        outerRadialGradient = RadialGradient(
            centerX.toFloat(),
            centerY.toFloat(),
            outerFraction * ballRadius,
            startColor,
            Color.TRANSPARENT,
            Shader.TileMode.REPEAT
        )

        shadowOffsetY = (ballRadius * .3).toInt()
        startColor = Color.argb(0x60, Color.red(shadowColor), Color.green(shadowColor), Color.blue(shadowColor))
        shadowRadialGradient = RadialGradient(
            centerX.toFloat(),
            centerY.toFloat() + shadowOffsetY,
            shadowFraction * ballRadius,
            startColor,
            Color.TRANSPARENT,
            Shader.TileMode.REPEAT
        )

        rocketBitmap?.recycle()
        val rocketDrawable = context.getDrawable(R.drawable.rocket)!!
        rocketDrawable.colorFilter = PorterDuffColorFilter(shadowColor, PorterDuff.Mode.DST)
        val height = bitmapFraction * ballRadius
        val fraction = height / rocketDrawable.intrinsicHeight
        rocketBitmap = rocketDrawable.toBitmap((rocketDrawable.intrinsicWidth * fraction).toInt(), height.toInt())

        colorTransform = floatArrayOf(
            0f, 0f, 0f, 0f, Color.red(startColor).toFloat(),  // Red
            0f, 0f, 0f, 0f, Color.green(startColor).toFloat(),  // Green
            0f, 0f, 0f, 0f, Color.blue(startColor).toFloat(),  // Blue
            0f, 0f, 0f, 1f, 0f // Alpha
        )
        colorMatrix.set(colorTransform)
        bitmapPaint.colorFilter = ColorMatrixColorFilter(colorTransform)
    }

    @Keep
    fun setRocketOffsetY(float: Float) {
        rocketOffsetY = float
        postInvalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        rocketAnimator = ObjectAnimator.ofFloat(this, "rocketOffsetY", 1.0f, -1f)
        rocketAnimator!!.duration = 1000
        rocketAnimator?.let {
            it.duration = 800
            it.repeatCount = INFINITE
            it.repeatMode = REVERSE
            it.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        rocketAnimator?.let {
            it.cancel()
        }
    }
}
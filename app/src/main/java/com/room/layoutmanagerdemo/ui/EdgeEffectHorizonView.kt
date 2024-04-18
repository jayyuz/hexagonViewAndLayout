package com.room.layoutmanagerdemo.ui

import android.content.Context
import android.graphics.Canvas
import android.media.effect.Effect
import android.os.Build
import android.util.AttributeSet
import android.widget.EdgeEffect
import android.widget.HorizontalScrollView
import java.util.Objects

class EdgeEffectHorizonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : HorizontalScrollView(context, attrs) {
    init {
        val clazz = HorizontalScrollView::class.java
        val parentField = HorizontalScrollView::class.members.find { it.name == "mEdgeGlowLeft" }
        try {
            val field = clazz.getField("mEdgeGlowLeft")
            field.isAccessible = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                field.set(this, CustomEdgeEffect(context))
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

    }

}
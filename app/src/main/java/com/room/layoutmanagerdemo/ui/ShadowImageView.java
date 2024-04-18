package com.room.layoutmanagerdemo.ui;

import static android.view.View.LAYER_TYPE_SOFTWARE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class ShadowImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Paint shadowPaint;
    private int shadowColor = Color.BLACK;
    private float shadowRadius = 10.0f;
    private float dx = 10.0f; // X轴偏移量
    private float dy = 10.0f; // Y轴偏移量

    public ShadowImageView(Context context) {
        super(context);
        initShadowPaint();
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initShadowPaint();
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initShadowPaint();
    }

    private void initShadowPaint() {
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        // 关闭硬件加速，因为setShadowLayer在硬件加速下可能不起作用
        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
    }

    public void setShadowLayer(float radius, float dx, float dy, int color) {
        shadowRadius = radius;
        this.dx = dx;
        this.dy = dy;
        shadowColor = color;
        initShadowPaint();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制阴影
        canvas.save();
        canvas.translate(dx, dy);
        canvas.drawBitmap(getBitmapFromDrawable(getDrawable()), 0, 0, shadowPaint);
        canvas.restore();

        // 绘制原始图片
        canvas.drawBitmap(getBitmapFromDrawable(getDrawable()), 0, 0, null);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        // 如果不是BitmapDrawable，需要手动创建Bitmap并绘制
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
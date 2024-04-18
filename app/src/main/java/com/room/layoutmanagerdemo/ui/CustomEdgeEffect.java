package com.room.layoutmanagerdemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.EdgeEffect;

public class CustomEdgeEffect extends EdgeEffect {

    private Paint mPaint;
    private final Rect mBounds = new Rect();
    private static final double ANGLE = Math.PI / 6;
    private static final float SIN = (float) Math.sin(ANGLE);
    private static final float COS = (float) Math.cos(ANGLE);
    private static final float RADIUS_FACTOR = 0.6f;

    public CustomEdgeEffect(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED); // 设置颜色
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        final float r = width * RADIUS_FACTOR / SIN;
        final float y = COS * r;
        final float h = r - y;
        final float or = height * RADIUS_FACTOR / SIN;
        final float oy = COS * or;
        final float oh = or - oy;

        mBounds.set(mBounds.left, mBounds.top, width, (int) Math.min(height, h));
    }

    @Override
    public boolean draw(Canvas canvas) {
        if (isFinished()) {
            return false;
        }

        // 在这里绘制自定义的边缘效果
        // 示例：绘制一个简单的矩形
        canvas.drawRect(mBounds, mPaint);

        // 如果需要，你可以在这里绘制自定义的图片
        return false;
    }
}
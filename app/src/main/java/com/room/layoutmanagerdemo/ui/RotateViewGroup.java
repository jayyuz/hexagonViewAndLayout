package com.room.layoutmanagerdemo.ui;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class RotateViewGroup extends FrameLayout {

    private Camera camera;
    private Matrix matrix;
    private float rotateAngle;
    private float lastX;

    public RotateViewGroup(Context context) {
        super(context);
        init();
    }

    public RotateViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        camera = new Camera();
        matrix = new Matrix();
        setWillNotDraw(false); // 如果ViewGroup不绘制任何内容，需要调用此方法以便能够调用dispatchDraw
    }

    public void setRotateAngle(float angle) {
        rotateAngle = angle;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int rotateCenterX = centerX; // 旋转中心X坐标
        int rotateCenterY = centerY; // 旋转中心Y坐标

        Log.e("RotateView", String.format("dispatchDraw: %f", rotateAngle));
        camera.save();
        int z = -90;
        camera.translate(0, 0, -z);
        camera.rotateY(rotateAngle);
        camera.translate(0, 0, z);
        camera.getMatrix(matrix);
        camera.restore();

        // 调整旋转中心
        matrix.preTranslate(-rotateCenterX, -rotateCenterY);
        matrix.postTranslate(rotateCenterX, rotateCenterY);

        // 应用变换
        canvas.save();
        canvas.concat(matrix);

        if (rotateAngle < 85 || rotateAngle > 265) {
            // 绘制子视图
            super.dispatchDraw(canvas);
        }

        // 恢复Canvas状态
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                // 计算手指移动的距离
                float currentX = event.getX();
                float deltaX = currentX - lastX;
                lastX = currentX;

                // 根据移动距离计算旋转角度
                rotateAngle += deltaX / 2; // 这里的2是旋转灵敏度，可以根据需要调整
                rotateAngle = rotateAngle % 360; // 保持角度在0-360度之间

                // 请求重新绘制
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                // 可以在这里添加惯性滚动或其他逻辑
                return true;
        }
        return super.onTouchEvent(event);
    }
}
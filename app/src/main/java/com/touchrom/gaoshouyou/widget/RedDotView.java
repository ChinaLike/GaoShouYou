package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lk on 2016/1/19.
 * 下载小圆点
 */
public class RedDotView extends View {
    private int mRadius;
    private int mColor;
    private Paint mPaint;
    int mWidth, mHeight;

    public RedDotView(Context context) {
        this(context, null);
    }

    public RedDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRedDot(canvas);
    }

    /**
     * 设置圆的半径
     */
    public void setRadius(int radius) {
        mRadius = radius;
        requestLayout();
    }

    /**
     * 设置圆点颜色
     */
    public void setDotColor(int color) {
        mColor = color;
        requestLayout();
    }

    /**
     * 画圆点
     *
     * @param canvas
     */
    private void drawRedDot(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(mWidth >> 1, mHeight >> 1, mRadius, mPaint);
    }

}

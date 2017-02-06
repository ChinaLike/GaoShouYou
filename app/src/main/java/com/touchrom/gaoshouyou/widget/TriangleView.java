package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.touchrom.gaoshouyou.R;

/**
 * Created by lyy on 2015/12/10.
 * 排行榜三角形
 */
public class TriangleView extends View {
    int mWidth, mHeight;
    int mTriangleColor = Color.BLUE; //三角颜色
    Paint mPaint;

    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TriangleView, defStyleAttr, 0);
        mTriangleColor = a.getColor(R.styleable.TriangleView_rt_triangle_color, Color.BLUE);
        a.recycle();
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }


    /**
     * 三角形颜色
     */
    public void setTriangleColor(int color) {
        mTriangleColor = color;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTriangle(canvas);
    }

    /**
     * 画三角
     *
     * @param canvas
     */
    private void drawTriangle(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTriangleColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path p = new Path();
        p.moveTo(mWidth, 0);
        p.lineTo(mWidth, mHeight);
        p.lineTo(0, mHeight);
        p.close();
        canvas.drawPath(p, mPaint);
    }

}

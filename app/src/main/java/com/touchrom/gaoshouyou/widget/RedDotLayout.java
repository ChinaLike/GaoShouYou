package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.arialyy.frame.util.DensityUtils;
import com.touchrom.gaoshouyou.R;

/**
 * Created by lyy on 2016/1/20.
 */
public class RedDotLayout extends FrameLayout {
    private RedDotView mRedDot;
    private TextView mTextView;

    public RedDotLayout(Context context) {
        this(context, null);
    }

    public RedDotLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedDotLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_red_dot, this, true);
        mRedDot = (RedDotView) findViewById(R.id.red_dot);
        mTextView = (TextView) findViewById(R.id.text);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RedDotLayout, defStyleAttr, 0);
        int mColor = a.getColor(R.styleable.RedDotLayout_rd_color, Color.parseColor("#F74C31"));
        int mRadius = (int) a.getDimension(R.styleable.RedDotLayout_rd_radius, DensityUtils.dp2px(16));
        CharSequence mText = a.getString(R.styleable.RedDotLayout_rd_text);
        a.recycle();
        mRedDot.setDotColor(mColor);
        mRedDot.setRadius(mRadius);
        mTextView.setText(mText);
    }

    public void setRadius(int radius) {
        if (mRedDot != null) {
            mRedDot.setRadius(radius);
        }
    }

    public void setDotColot(int color) {
        if (mRedDot != null) {
            mRedDot.setDotColor(color);
        }
    }

    public void setText(CharSequence text) {
        if (mTextView != null) {
            mTextView.setText(text);
        }
    }
}

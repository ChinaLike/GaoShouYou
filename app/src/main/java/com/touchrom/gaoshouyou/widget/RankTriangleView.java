package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.touchrom.gaoshouyou.R;

/**
 * Created by lk on 2015/12/10.
 */
public class RankTriangleView extends RelativeLayout {
    TriangleView mTriangle;
    TextView mText;

    public RankTriangleView(Context context) {
        this(context, null);
    }

    public RankTriangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankTriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int def) {
        LayoutInflater.from(context).inflate(R.layout.layout_rank_view, this, true);
        mTriangle = (TriangleView) findViewById(R.id.triangle);
        mText = (TextView) findViewById(R.id.text);

    }

    /**
     * 设置三角形颜色
     *
     * @param color
     */
    public void setTriangleColor(@ColorInt int color) {
        mTriangle.setTriangleColor(color);
        requestLayout();
    }

    /**
     * 设置排名
     */
    public void setRank(String rank) {
        mText.setText(rank);
    }

}

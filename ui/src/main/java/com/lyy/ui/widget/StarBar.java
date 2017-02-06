package com.lyy.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lyy.ui.R;
import com.lyy.ui.util.Util;

/**
 * Created by lyy on 2015/12/8.
 * 评分栏
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StarBar extends LinearLayout {

    private int mCount;
    private float mChildW;
    private float mChildH;
    private float mChildInterval;
    private int mScore;

    public StarBar(Context context) {
        this(context, null);
    }

    public StarBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StarBar, defStyleAttr, 0);
        mCount = a.getInt(R.styleable.StarBar_sb_count, 5);
        mChildW = a.getDimension(R.styleable.StarBar_sb_child_w, Util.dp2px(16));
        mChildH = a.getDimension(R.styleable.StarBar_sb_child_h, Util.dp2px(16));
        mChildInterval = a.getDimension(R.styleable.StarBar_sb_child_interval, Util.dp2px(2));
        mScore = a.getInt(R.styleable.StarBar_sb_score, 5);
        a.recycle();
        setOrientation(HORIZONTAL);
        int[] selectedImgs = new int[]{
                R.drawable.icon_star_bar_1, R.drawable.icon_star_bar_1, R.drawable.icon_star_bar_1,
                R.drawable.icon_star_bar_1, R.drawable.icon_star_bar_1
        };
        int[] defaultImgs = new int[]{
                R.drawable.icon_star_bar_b_1, R.drawable.icon_star_bar_b_1, R.drawable.icon_star_bar_b_1,
                R.drawable.icon_star_bar_b_1, R.drawable.icon_star_bar_b_1
        };
        setScoreBackground(selectedImgs, defaultImgs);
        setScore(mScore);
    }

    /**
     * @param selectedImgs
     * @param defaultImgs
     */
    public void setScoreBackground(int[] selectedImgs, int[] defaultImgs) {
        if (mCount != selectedImgs.length || mCount != defaultImgs.length) {
            throw new IllegalAccessError("被选中的背景图数量、默认背景图数量、分数count必须一致");
        }
        if (getChildCount() > 0) {
            removeAllViews();
        }
        for (int i = 0, count = selectedImgs.length; i < count; i++) {
            addView(createChild(selectedImgs[i], defaultImgs[i]));
        }
    }

    /**
     * 设置评分
     *
     * @param score
     */
    public void setScore(int score) {
        mScore = score;
        for (int i = 0; i < mCount; i++) {
            View view = getChildAt(i);
            view.setSelected(i < mScore);
        }
    }

    /**
     * 创建每一个子View
     *
     * @param selectedDrawable
     * @param defaultDrawable
     * @return
     */
    private ImageView createChild(int selectedDrawable, int defaultDrawable) {
        ImageView img = new ImageView(getContext());
        img.setImageDrawable(createDrawableSelector(selectedDrawable, defaultDrawable));
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) mChildW, (int) mChildH);
        img.setLayoutParams(params);
        img.setPadding((int) mChildInterval, 0, (int) mChildInterval, 0);
        return img;
    }


    /**
     * 图片是非代码构建的
     */
    private StateListDrawable createDrawableSelector(int selectedDrawable, int defaultDrawable) {
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_selected}, getDrawable(selectedDrawable));
        selector.addState(new int[]{}, getDrawable(defaultDrawable));
        return selector;
    }

    private Drawable getDrawable(int drawable) {
        return getContext().getResources().getDrawable(drawable);
    }


}

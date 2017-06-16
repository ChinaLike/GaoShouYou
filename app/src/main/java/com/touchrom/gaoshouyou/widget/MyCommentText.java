package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.arialyy.frame.util.DensityUtils;
import com.touchrom.gaoshouyou.R;

/**
 * Created by lk on 2016/3/15.
 * 我的评论 textView
 */
public class MyCommentText extends TextView {
    /**
     * 左边图标
     */
    private Drawable mLeftDrawable;
    /**
     * 右边图标
     */
    private Drawable mRightDrawable;

    public MyCommentText(Context context) {
        this(context, null);
    }

    public MyCommentText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCommentText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mLeftDrawable = getCompoundDrawables()[0];
        mRightDrawable = getCompoundDrawables()[2];
        if (mLeftDrawable == null) {
            mLeftDrawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
        }
        if (mRightDrawable == null) {
            mRightDrawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
        }
        setLeftDrawable(mLeftDrawable);
        setRightDrawable(mRightDrawable);
    }

    /**
     * 设置左边图标
     *
     * @param leftDrawable
     */
    public void setLeftDrawable(Drawable leftDrawable) {
        if (leftDrawable != null) {
            mLeftDrawable = leftDrawable;
            mLeftDrawable.setBounds(0, 0, DensityUtils.dp2px(16), DensityUtils.dp2px(16));
            setCompoundDrawables(leftDrawable, getCompoundDrawables()[1], getCompoundDrawables()[2], getCompoundDrawables()[3]);
        }
    }

    /**
     * 设置右边图标
     *
     * @param rightDrawable
     */
    public void setRightDrawable(Drawable rightDrawable) {
        if (rightDrawable != null) {
            mRightDrawable = rightDrawable;
            mRightDrawable.setBounds(0, 0, DensityUtils.dp2px(16), DensityUtils.dp2px(16));
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mRightDrawable, getCompoundDrawables()[3]);
        }
    }
}

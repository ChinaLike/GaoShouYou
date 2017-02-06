package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arialyy.frame.util.DensityUtils;
import com.touchrom.gaoshouyou.R;

/**
 * Created by lyy on 2015/12/2.
 * 简单的 ToolBar
 * 注意：右边图标不能和右边文字一起使用
 */
public class MyToolBar extends RelativeLayout {

    ImageView mRightIcon;
    TextView mBackView, mRightText;


    public MyToolBar(Context context) {
        this(context, null);
    }

    public MyToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_toolbar, this, true);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyToolBar, defStyleAttr, 0);
        Drawable leftImg, rightImg;
        CharSequence title, rightText;
        leftImg = a.getDrawable(R.styleable.MyToolBar_mt_left_icon);
        rightImg = a.getDrawable(R.styleable.MyToolBar_mt_right_icon);
        title = a.getString(R.styleable.MyToolBar_mt_title);
        rightText = a.getString(R.styleable.MyToolBar_mt_right_text);
        a.recycle();
        mRightIcon = (ImageView) findViewById(R.id.right_icon);
        mBackView = (TextView) findViewById(R.id.back);
        mRightText = (TextView) findViewById(R.id.right_text);

        if (rightImg != null) {
            mRightIcon.setImageDrawable(rightImg);
        }
        setBackIcon(leftImg);
        mBackView.setCompoundDrawablePadding(-DensityUtils.dp2px(5));
        mBackView.setText(title);
//        mBackView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.skin_selector_bar_widget));
//        mRightIcon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_bar_widget));
        mRightText.setText(rightText);
    }

    public ImageView getRightIcon() {
        return mRightIcon;
    }

    public TextView getTitle() {
        return mBackView;
    }

    public TextView getRightText() {
        return mRightText;
    }

    /**
     * 设置左边图标
     *
     * @param drawable
     */
    public void setBackIcon(Drawable drawable) {
        int size = DensityUtils.dp2px(40);
        if (drawable == null) {
            drawable = getContext().getResources().getDrawable(R.mipmap.icon_left_back);
        }
        assert drawable != null;
        drawable.setBounds(0, 0, size, size);
        mBackView.setCompoundDrawables(drawable, null, null, null);
    }

    public TextView getBackView() {
        return mBackView;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            mBackView.setText(title);
        }
    }

    /**
     * 设置右边图标
     */
    public void setRightIcon(Drawable drawable) {
        if (drawable != null) {
            mRightIcon.setImageDrawable(drawable);
        }
    }

    /**
     * 设置右边文字
     */
    public void setRightText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mRightText.setText(text);
        }
    }

}

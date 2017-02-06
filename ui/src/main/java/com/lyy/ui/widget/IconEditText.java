package com.lyy.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;

import com.lyy.ui.R;
import com.lyy.ui.util.Util;

/**
 * Created by lyy on 2015/8/4.
 * 带有icon的EditText
 * 图标点击监听接口OnIconTouchListener
 */
public class IconEditText extends EditText {
    private static final String TAG = "IconEditText";
    /**
     * 左边图标
     */
    private Drawable mLeftDrawable;
    /**
     * 右边图标
     */
    private Drawable mRightDrawable;
    private OnIconTouchListener mListener;
    private int mWidth = 32, mHeight = 32;

    public interface OnIconTouchListener {
        public void onLeftIcon(Object tag);

        public void onRightIcon(Object tag);
    }

    public IconEditText(Context context) {
        this(context, null);
    }

    public IconEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public IconEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconEditText, defStyleAttr, 0);
        mWidth = (int) a.getDimension(R.styleable.IconEditText_ic_icon_width, 32);
        mHeight = (int) a.getDimension(R.styleable.IconEditText_ic_icon_width, 32);
        a.recycle();
        mLeftDrawable = getCompoundDrawables()[0];
        mRightDrawable = getCompoundDrawables()[2];
        if (mLeftDrawable != null) {
            mLeftDrawable.setBounds(0, 0, mWidth, mHeight);
            setCompoundDrawables(mLeftDrawable, getCompoundDrawables()[1], getCompoundDrawables()[2], getCompoundDrawables()[3]);
        }
        if (mRightDrawable != null) {
            mRightDrawable.setBounds(0, 0, mWidth, mHeight);
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mRightDrawable, getCompoundDrawables()[3]);
        }
        setCompoundDrawables(mLeftDrawable, null, mRightDrawable, null);
        setTextColor(Color.BLACK);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int paddingRight = Util.dp2px(10);
        int paddingLeft = Util.dp2px(5);
        setPadding(paddingLeft, getPaddingTop(), paddingRight, getPaddingBottom());
//        int paddingTop = Util.dp2px(12);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        } else {
//            setPadding(paddingLeft, getPaddingTop(), paddingRight, getPaddingBottom());
//        }
    }

    /**
     * 设置右边图标的大小，单位dp
     */
    public void setRightIconSize(int width, int height) {
        if (mRightDrawable != null) {
            mWidth = width;
            mHeight = height;
            mRightDrawable.setBounds(0, 0, Util.dp2px(width), Util.dp2px(height));
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mRightDrawable, getCompoundDrawables()[3]);
        }
    }

    /**
     * 设置左边图标的大小，单位dp
     */
    public void setLeftIconSize(int width, int height) {
        if (mLeftDrawable != null) {
            mWidth = width;
            mHeight = height;
            mLeftDrawable.setBounds(0, 0, Util.dp2px(width), Util.dp2px(height));
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mRightDrawable, getCompoundDrawables()[3]);
        }
    }

    /**
     * 设置左边图标
     *
     * @param leftDrawable
     */
    public void setLeftDrawable(int leftDrawable) {
        setLeftDrawable(getContext().getResources().getDrawable(leftDrawable));
    }

    /**
     * 设置右边图标
     *
     * @param rightDrawable
     */
    public void setRightDrawable(int rightDrawable) {
        setRightDrawable(getContext().getResources().getDrawable(rightDrawable));
    }

    /**
     * 设置右边图标
     *
     * @param rightDrawable
     */
    public void setRightDrawable(Drawable rightDrawable) {
        if (rightDrawable != null) {
            mRightDrawable = rightDrawable;
            mRightDrawable.setBounds(0, 0, Util.dp2px(mWidth), Util.dp2px(mHeight));
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mRightDrawable, getCompoundDrawables()[3]);
        }
    }

    /**
     * 设置左边图标
     *
     * @param leftDrawable
     */
    public void setLeftDrawable(Drawable leftDrawable) {
        if (leftDrawable != null) {
            mLeftDrawable = leftDrawable;
            mLeftDrawable.setBounds(0, 0, Util.dp2px(mWidth), Util.dp2px(mHeight));
            setCompoundDrawables(leftDrawable, getCompoundDrawables()[1], getCompoundDrawables()[2], getCompoundDrawables()[3]);
        }
    }

    /**
     * 设置Icon监听
     *
     * @param listener
     */
    public void setOnIconTouchListener(OnIconTouchListener listener) {
        mListener = listener;
    }

    /**
     * 设置右边图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    public void setRightIconVisible(boolean visible) {
        Drawable right = visible ? mRightDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
        Drawable left = visible ? mLeftDrawable : null;
        setCompoundDrawables(left,
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event != null && event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {    // 右边图标

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable && mListener != null) {
                    mListener.onRightIcon(getTag());
                }
            }
            if (getCompoundDrawables()[0] != null) {    //左边图标
                boolean touchable = event.getX() > 0 && (event.getX() < mLeftDrawable.getBounds().width());
                if (touchable && mListener != null) {
                    mListener.onLeftIcon(getTag());
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        return false;
    }


}

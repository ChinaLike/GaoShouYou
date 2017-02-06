package com.lyy.ui.group;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyy.ui.R;
import com.lyy.ui.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by lyy on 2015/11/20.
 * 流式布局
 */
public class TagFlowLayout extends ViewGroup implements View.OnClickListener {
    private static final String TAG = "FlowLayout";
    /**
     * 标签
     */
    private CharSequence[] mTags;
    private int mPLeft, mPRight, mPTop, mPBottom;
    private float mTagTextSize;
    private float mIntervalX;
    private float mIntervalY;
    private int mTagColor;
    private OnChildClickListener mListener;
    private int mTagTextSelectorId;//字体颜色选择器
    private int mDrawableId;
    private int mViewWidth;

    /**
     * 子TAG监听
     */
    public interface OnChildClickListener {
        void onChildClick(TagFlowLayout parent, TextView child, int position);
    }

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TagFlowLayout, defStyleAttr, 0);
        mTags = a.getTextArray(R.styleable.TagFlowLayout_tfl_tags);
        mDrawableId = a.getResourceId(R.styleable.TagFlowLayout_tfl_drawable, -1);
        mTagTextSize = a.getDimensionPixelSize(R.styleable.TagFlowLayout_tfl_text_size, Util.sp2px(getContext(), 12));
        mIntervalX = a.getDimensionPixelSize(R.styleable.TagFlowLayout_tfl_interval_x, Util.dp2px(10));
        mIntervalY = a.getDimensionPixelSize(R.styleable.TagFlowLayout_tfl_interval_y, Util.dp2px(10));
        mTagColor = a.getColor(R.styleable.TagFlowLayout_tfl_text_color, Color.parseColor("#000000"));
        mTagTextSelectorId = a.getResourceId(R.styleable.TagFlowLayout_tfl_text_color_selector, -1);

        a.recycle();
        setTags(mTags);
        mPLeft = getPaddingLeft();
        mPRight = getPaddingRight();
        mPTop = getPaddingTop();
        mPBottom = getPaddingBottom();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int lineHeight = 0;
        int tempWidth = 0;
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int lineNum = 1;
        // 遍历所有的子View
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);
            // measure子View，并获取它的宽度和高度
            LayoutParams childLayoutParams = childView.getLayoutParams();
            childView.measure(
                    getChildMeasureSpec(widthMeasureSpec, mPLeft + mPRight, childLayoutParams.width),
                    getChildMeasureSpec(heightMeasureSpec, mPTop + mPBottom, childLayoutParams.height));
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            // 计算当前行的高度（当前行所有子View中最高的那个）
            lineHeight = Math.max(childHeight, lineHeight);
            tempWidth += childWidth + mIntervalX + getPaddingLeft();

            if (tempWidth >= viewWidth) {
                tempWidth = childWidth;
                lineNum++;
            }
        }
        mViewWidth = viewWidth;
        setMeasuredDimension(viewWidth, (int) (lineNum * lineHeight + (lineNum + 1) * mIntervalY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int tempWidth = 0, lineNum = 0;
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View child = getChildAt(i);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int left = (int) (tempWidth + mIntervalX);
            int top = (int) ((lineNum * (cHeight + mIntervalY)) + mIntervalY);
            int right = (int) (tempWidth + cWidth + mIntervalX);
            int bottom = top + cHeight;
            child.layout(left, top, right, bottom);
            tempWidth += cWidth + mIntervalX + getPaddingLeft();
            View nextView = getChildAt(i + 1);
            int nextWidth = nextView == null ? 0 : nextView.getMeasuredWidth();
            if (tempWidth + nextWidth >= mViewWidth) {
                lineNum++;
                tempWidth = 0;
            }
        }
    }

    /**
     * 设置背景
     *
     * @param drawable
     */
    public void setDrawable(@DrawableRes int drawable) {
        mDrawableId = drawable;
        notifyChild();
    }

    /**
     * 设置字体选择器
     */
    public void setTextSelector(@DrawableRes int selector) {
        mTagTextSelectorId = selector;
        notifyChild();
    }

    private void notifyChild() {
        for (int i = 0, count = getChildCount(); i < count; i++) {
            TextView tv = (TextView) getChildAt(i);
            if (mTagTextSelectorId != -1) {
                tv.setTextColor(getResources().getColorStateList(mTagTextSelectorId));
            } else {
                tv.setTextColor(mTagColor);
            }
            if (mDrawableId == -1) {
                tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tag));
            } else {
                tv.setBackgroundDrawable(getResources().getDrawable(mDrawableId));
            }
        }
        requestLayout();
    }

    /**
     * 单选
     *
     * @param position
     */
    public void setRadio(int position) {
        if (position > getChildCount()) {
            return;
        }
        for (int i = 0, count = getChildCount(); i < count; i++) {
            View child = getChildAt(i);
            child.setSelected(position == i);
        }
        requestLayout();
    }

    /**
     * 多选
     */
    public void setMultiselect(int position) {
        if (position > getChildCount()) {
            return;
        }
        View view = getChildAt(position);
        view.setSelected(true);
        requestLayout();
    }


    /**
     * 设置标签
     *
     * @param tags
     */
    public void setTags(CharSequence[] tags) {
        removeAllViews();
        mTags = tags;
        if (mTags != null && mTags.length > 0) {
            int i = 0;
            for (CharSequence tag : mTags) {
                addView(createChildView(tag, i));
                i++;
            }
        }
        requestLayout();
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onChildClick(this, (TextView) v, v.getId());
        }
    }

    /**
     * 子控件点击监听
     *
     * @param listener {@link OnChildClickListener}
     */
    public void setOnChildClickListener(OnChildClickListener listener) {
        mListener = listener;
    }

    /**
     * 创建标签
     *
     * @param tag
     * @return
     */
    private TextView createChildView(CharSequence tag, int id) {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TextView view = new TextView(getContext());
        view.setText(tag);
        try {
            Method method = Util.getMethod(TextView.class, "setRawTextSize", float.class);
            method.invoke(view, mTagTextSize);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//        view.setTextSize(mTagTextSize);
        if (mTagTextSelectorId != -1) {
            view.setTextColor(getResources().getColorStateList(mTagTextSelectorId));
        } else {
            view.setTextColor(mTagColor);
        }
        if (mDrawableId == -1) {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tag));
        } else {
            view.setBackgroundDrawable(getResources().getDrawable(mDrawableId));
        }
        view.setLayoutParams(lp);
        view.setId(id);
        view.setOnClickListener(this);
        int p = Util.dp2px(5);
        view.setPadding(p << 1, p, p << 1, p);
        return view;
    }
}

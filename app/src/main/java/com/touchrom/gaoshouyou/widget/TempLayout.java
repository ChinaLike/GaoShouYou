package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by lyy on 2016/1/11.
 */
public class TempLayout extends RelativeLayout {
    TempView mTemp;

    public TempLayout(Context context) {
        this(context, null);
    }

    public TempLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TempLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addView(createTempView());
    }

    private TempView createTempView() {
        mTemp = new TempView(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTemp.setLayoutParams(lp);
        return mTemp;
    }

    public void setTempViewVisibility(int visibility) {
        mTemp.setVisibility(visibility);
    }

    /**
     * 设置错误填充的View类型
     *
     * @param type {@link TempView#ERROR}
     */
    public void setTempType(int type) {
        mTemp.setType(type);
        requestLayout();
    }

    /**
     * 设置错误填充View的按钮监听
     */
    public void setOnTempBtListener(TempView.OnTempBtListener listener) {
        mTemp.setOnTempBtListener(listener);
    }

}

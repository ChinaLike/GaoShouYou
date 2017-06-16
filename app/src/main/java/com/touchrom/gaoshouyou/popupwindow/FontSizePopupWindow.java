package com.touchrom.gaoshouyou.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arialyy.frame.util.SharePreUtil;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BasePopupWindow;
import com.touchrom.gaoshouyou.config.Constance;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/15.
 * 字体大小悬浮框
 */
public class FontSizePopupWindow extends BasePopupWindow implements View.OnClickListener {
    @InjectView(R.id.mini)
    TextView mMini;
    @InjectView(R.id.medium)
    TextView mMedium;
    @InjectView(R.id.larger)
    TextView mLarger;
    @InjectView(R.id.ll)
    LinearLayout mLl;

    private OnFontSizeChangeListener mListener;

    public interface OnFontSizeChangeListener {
        public void onChange(View view, float fontSize);
    }

    public FontSizePopupWindow(Context context) {
        super(context, new ColorDrawable(Color.TRANSPARENT));
        setHeight((int) getContext().getResources().getDimension(R.dimen.tab_bar_height));
        init();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.popupwindow_font_size;
    }

    public void setOnFontSizeChangeListener(@NonNull OnFontSizeChangeListener listener) {
        mListener = listener;
    }

    protected void init() {
        super.init();
        int size = SharePreUtil.getInt(Constance.APP.NAME, getContext(), "fontSize");
        if (size == 12) {
            selectedView(mMini);
        } else if (size == 16) {
            selectedView(mLarger);
        } else {
            selectedView(mMedium);
        }
        mMini.setOnClickListener(this);
        mMedium.setOnClickListener(this);
        mLarger.setOnClickListener(this);
    }

    private void selectedView(View view) {
        for (int i = 0, count = mLl.getChildCount(); i < count; i++) {
            View view1 = mLl.getChildAt(i);
            view1.setSelected(view.getId() == view1.getId());
        }
    }

    @Override
    public void onClick(View v) {
        float size = 14;
        switch (v.getId()) {
            case R.id.mini:
                size = 12;
                break;
            case R.id.medium:
                size = 14;
                break;
            case R.id.larger:
                size = 16;
                break;
        }
        selectedView(v);
        if (mListener != null) {
            mListener.onChange(v, size);
        }
        SharePreUtil.putInt(Constance.APP.NAME, getContext(), "fontSize", (int) size);
    }
}

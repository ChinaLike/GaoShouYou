package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arialyy.frame.util.AndroidVersionUtil;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.help.RippleHelp;

/**
 * Created by lyy on 2015/11/30.
 * 设置界面中带有右向箭头的的Widget
 */
public class SettingNormalInfo extends RelativeLayout {
    private TextView mTitle, mValue;

    public SettingNormalInfo(Context context) {
        this(context, null);
    }

    public SettingNormalInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingNormalInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        String title, value;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SettingNormalInfo, defStyleAttr, 0);
        title = a.getString(R.styleable.SettingNormalInfo_sn_title);
        value = a.getString(R.styleable.SettingNormalInfo_sn_value);
        a.recycle();
        LayoutInflater.from(context).inflate(R.layout.layout_setting_normal_info, this, true);
        mTitle = (TextView) findViewById(R.id.title);
        mValue = (TextView) findViewById(R.id.value);
        setTitle(title);
        setValue(value);
        if (!AndroidVersionUtil.hasIcecreamsandwich()) {
            setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.selector_setting_widget));
        } else {
            /**
             * {@link RippleHelp}
             */
//            L.e("SettingNormalInfo", "请使用RippleHelp工具设置背景");
        }
    }

    public CharSequence getTitle() {
        return mTitle.getText();
    }

    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    public CharSequence getValue() {
        return mValue.getText();
    }

    public void setValue(CharSequence value) {
        mValue.setText(value);
    }
}

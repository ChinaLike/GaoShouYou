package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyy.ui.widget.MyToggleButton;
import com.touchrom.gaoshouyou.R;

/**
 * Created by lk on 2015/11/30.
 * 设置界面中带有选择控件的Widget
 */
public class SettingSwitchInfo extends RelativeLayout {
    private TextView mTitle, mValue;
    private MyToggleButton mToggleBt;
    private boolean isOn = false;
    private View mTemp;

    public SettingSwitchInfo(Context context) {
        this(context, null);
    }

    public SettingSwitchInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingSwitchInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        String title, value;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SettingSwitchInfo, defStyleAttr, 0);
        title = a.getString(R.styleable.SettingSwitchInfo_ss_title);
        value = a.getString(R.styleable.SettingSwitchInfo_ss_value);
        isOn = a.getBoolean(R.styleable.SettingSwitchInfo_ss_isOn, false);
        a.recycle();
        LayoutInflater.from(context).inflate(R.layout.layout_setting_switch_info, this, true);
        mTitle = (TextView) findViewById(R.id.title);
        mValue = (TextView) findViewById(R.id.value);
        mToggleBt = (MyToggleButton) findViewById(R.id.toggle);
        mTemp = findViewById(R.id.temp);
        setTitle(title);
        setValue(value);
        setSwitchState(isOn);
    }

    /**
     * 是否显示蒙版
     *
     * @param show
     */
    public void showTempView(boolean show) {
        if (mTemp != null) {
            mTemp.setVisibility(show ? VISIBLE : GONE);
        }
    }

    /**
     * 获取选择按钮
     *
     * @return
     */
    public MyToggleButton getToggleBt() {
        return mToggleBt;
    }

    /**
     * 设置开关却换事件
     */
    public void setOnToggleBtListener(MyToggleButton.OnToggleChanged toggleListener) {
        if (mToggleBt != null) {
            mToggleBt.setOnToggleChanged(toggleListener);
        }
    }

    /**
     * 设置开关状态
     *
     * @param isOn
     */
    public void setSwitchState(boolean isOn) {
        this.isOn = isOn;
        if (!isOn) {
            mToggleBt.setToggleOff();
        } else {
            mToggleBt.setToggleOn();
        }
    }

    /**
     * 获取开关状态
     *
     * @return
     */
    public boolean getSwitchState() {
        return mToggleBt.getCurrentState();
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

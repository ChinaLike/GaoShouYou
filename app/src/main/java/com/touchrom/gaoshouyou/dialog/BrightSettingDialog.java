package com.touchrom.gaoshouyou.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.arialyy.frame.util.ScreenUtil;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseDialog;
import com.touchrom.gaoshouyou.config.ResultCode;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/11/10.
 * 夜间模式对话框
 */
@SuppressLint("ValidFragment")
public class BrightSettingDialog extends BaseDialog implements View.OnClickListener {

    @InjectView(R.id.seek_bar)
    SeekBar mSeekBar;
    //    @InjectView(R.id.entity)
//    Button mEntity;
//    @InjectView(R.id.cancel)
//    Button mCancel;
    @InjectView(R.id.checkbox)
    CheckBox mCheckBox;
    private Activity mActivity;
    private int mCurrentBright = -1;
    private boolean isAuto = false;
    ScreenUtil mScreenUtil = ScreenUtil.getInstance();

    public BrightSettingDialog(Object obj) {
        super(obj);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_night_seting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mScreenUtil.setBrightness(mActivity, progress / 10f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mCurrentBright = seekBar.getProgress();
            }
        });
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAuto = isChecked;
            }
        });
//        mEntity.setOnClickListener(this);
//        mCancel.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enter) {
            if (isAuto) {
                getSimplerModule().onDialog(ResultCode.BRIGHT_SETTING, true);
            } else {
                getSimplerModule().onDialog(ResultCode.BRIGHT_SETTING, mCurrentBright / 10f);
            }
        } else if (v.getId() == R.id.cancel) {
            this.dismiss();
        }
    }
}

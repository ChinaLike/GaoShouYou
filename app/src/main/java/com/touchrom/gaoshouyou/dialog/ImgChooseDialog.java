package com.touchrom.gaoshouyou.dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseDialog;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.DialogImgChooseBinding;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/2/29.
 * 头像图片选择方式对话框
 */
@SuppressLint("ValidFragment")
public class ImgChooseDialog extends BaseDialog<DialogImgChooseBinding> implements View.OnClickListener {
    @InjectView(R.id.take_photo)
    Button mTakePhoto;
    @InjectView(R.id.local_photo)
    Button mLocalPhoto;
    @InjectView(R.id.cancel)
    Button mCancel;

    public ImgChooseDialog(Object obj) {
        super(obj);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_img_choose;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(p);
        getDialog().getWindow().setWindowAnimations(R.style.dialogAnim);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mTakePhoto.setOnClickListener(this);
        mLocalPhoto.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                getSimplerModule().onDialog(ResultCode.DIALOG.IMG_CHOOSE, 1);
                break;
            case R.id.local_photo:
                getSimplerModule().onDialog(ResultCode.DIALOG.IMG_CHOOSE, 2);
                break;
        }
        dismiss();
    }
}

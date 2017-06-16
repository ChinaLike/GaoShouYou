package com.touchrom.gaoshouyou.dialog;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseDialog;
import com.touchrom.gaoshouyou.databinding.DialogLoadingBinding;

/**
 * Created by lk on 2015/11/9.
 * 加载等待对话框
 */
@SuppressLint("ValidFragment")
public class LoadingDialog extends BaseDialog<DialogLoadingBinding> {
    //    @InjectView(R.id.img)
//    ImageView mLoading;
    public LoadingDialog(boolean canCancel) {
        setCancelable(canCancel);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
//        AnimationDrawable ad = ImgHelp.createLoadingAnim(getContext());
//        mLoading.setImageDrawable(ad);
//        ad.start();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_loading;
    }
}

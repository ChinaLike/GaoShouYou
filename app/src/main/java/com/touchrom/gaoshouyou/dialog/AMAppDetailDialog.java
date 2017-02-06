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
import com.touchrom.gaoshouyou.databinding.DialogAppManagerDetailBinding;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/12/25.
 * 应用管理Item点击dialog界面
 */
@SuppressLint("ValidFragment")
public class AMAppDetailDialog extends BaseDialog<DialogAppManagerDetailBinding> implements View.OnClickListener {
    /**
     * 删除
     */
    public static final int FUNCTION_DELETE = 0x01;
    /**
     * 忽略
     */
    public static final int FUNCTION_IGNORE = 0x02;
    /**
     * 打开
     */
    public static final int FUNCTION_OPEN = 0x03;
    private String mGameName;
    @InjectView(R.id.first_bt)
    Button mFirstBt;
    @InjectView(R.id.cancel)
    Button mCancel;
    private int mFunction = FUNCTION_DELETE;

    /**
     * @param gameName
     * @param obj
     * @param function {@link #FUNCTION_DELETE}
     *                 {@link #FUNCTION_IGNORE}
     *                 {@link #FUNCTION_OPEN}
     */
    public AMAppDetailDialog(String gameName, Object obj, int function) {
        super(obj);
        mGameName = gameName;
        mFunction = function;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_app_manager_detail;
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
        initWidget();
    }

    private void initWidget() {
        getBinding().setTitle(mGameName);
        String str = "";
        if (mFunction == FUNCTION_IGNORE) {
            str = "忽略";
        } else if (mFunction == FUNCTION_OPEN) {
            str = "启动";
        } else {
            str = "删除";
        }
        mFirstBt.setText(str);
        mFirstBt.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.first_bt) {
            getSimplerModule().onDialog(ResultCode.DIALOG.AM_ITEM_FUNCTION, mFunction);
        }
        dismiss();
    }
}

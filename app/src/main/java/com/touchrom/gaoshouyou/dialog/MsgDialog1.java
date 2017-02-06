package com.touchrom.gaoshouyou.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arialyy.frame.core.AbsDialog;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.help.RippleHelp;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/1/7.
 * 消息对话框
 */
public class MsgDialog1 extends AbsDialog implements View.OnClickListener {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.msg)
    TextView mMsg;
    @InjectView(R.id.cancel)
    Button mCancel;
    @InjectView(R.id.enter)
    Button mEnter;
    @InjectView(R.id.bar)
    LinearLayout mBar;
    @InjectView(R.id.root)
    LinearLayout mRootView;
    private int mRequestCode = -1;
    private CharSequence mTitleStr, mMsgStr;

    private MsgDialog1(Context context) {
        super(context);
    }

    public MsgDialog1(Context context, Object obj, int requestCode) {
        this(context, obj);
        mRequestCode = requestCode;
    }

    public MsgDialog1(Context context, CharSequence title, CharSequence msg, Object obj, int requestCode) {
        this(context, obj, requestCode);
        mTitleStr = title;
        mMsgStr = msg;
        mRequestCode = requestCode;
    }

    public MsgDialog1(Context context, Object obj) {
        super(context, obj);
        init();
    }

    private void init() {
        mTitle.setText(mTitleStr);
        mMsg.setText(mMsgStr);
        mCancel.setOnClickListener(this);
        mEnter.setOnClickListener(this);
        RippleHelp.createRipple(getContext(), mEnter);
        RippleHelp.createRipple(getContext(), mCancel);
        int color = getContext().getResources().getColor(R.color.skin_white_color);
        mTitle.setBackgroundColor(color);
        mMsg.setBackgroundColor(color);
        mBar.setBackgroundColor(color);
        mRootView.setBackgroundColor(color);
    }

    public void setRequestCode(int requestCode) {
        mRequestCode = requestCode;
    }

    public void setTitle(CharSequence title) {
        mTitleStr = title;
        if (mTitle != null) {
            mTitle.setText(mTitleStr);
        }
    }

    public void setMsg(CharSequence msg) {
        mMsgStr = msg;
        if (mMsg != null) {
            mMsg.setText(mMsgStr);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_msg_1;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enter) {
            getSimplerModule().onDialog(ResultCode.DIALOG.MSG_DIALOG_ENTER, mRequestCode);
        } else {
            getSimplerModule().onDialog(ResultCode.DIALOG.MSG_DIALOG_CANCEL, mRequestCode);
        }
        dismiss();
    }
}

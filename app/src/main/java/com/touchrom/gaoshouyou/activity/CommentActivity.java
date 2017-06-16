package com.touchrom.gaoshouyou.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.arialyy.frame.util.show.L;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityCommentBinding;
import com.touchrom.gaoshouyou.help.AnimHelp;
import com.touchrom.gaoshouyou.module.CommentModule;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/15.
 * 评论界面
 */
public class CommentActivity extends BaseActivity<ActivityCommentBinding> {
    @InjectView(R.id.et)
    EditText mEt;
    private int mAppId = -1;
    private int mTypeId = -1;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mAppId = getIntent().getIntExtra("appId", -1);
        mTypeId = getIntent().getIntExtra("typeId", -1);
        if (mAppId == -1 || mTypeId == -1) {
            L.e(TAG, "cmtId 或 typeId 不正确");
            finish();
            return;
        }
        mToolbar.getRightText().setVisibility(View.VISIBLE);
        mToolbar.getRightText().setText("发送");
        mToolbar.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }

    private void sendComment() {
        if (TextUtils.isEmpty(mEt.getText())) {
            AnimHelp.getInstance().nope(mToolbar.getRightText()).start();
            return;
        }
        getModule(CommentModule.class).sendComment(mAppId, mTypeId, mEt.getText().toString());
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.SEND_COMMENT) {
            if ((boolean) obj) {
                mEt.setText("");
                mEt.setHint("随便说点什么...");
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}

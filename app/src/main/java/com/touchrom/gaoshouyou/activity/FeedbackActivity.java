package com.touchrom.gaoshouyou.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.databinding.ActivityFeedbackBinding;
import com.touchrom.gaoshouyou.module.SettingModule;
import com.touchrom.gaoshouyou.util.S;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/12/2.
 * 反馈界面
 */
public class FeedbackActivity extends BaseActivity<ActivityFeedbackBinding> {
    @InjectView(R.id.feedback)
    EditText mFeedback;
    @InjectView(R.id.contact)
    EditText mContact;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mToolbar.getRightText().setBackgroundDrawable(getResources().getDrawable(R.drawable.skin_selector_bar_widget));
        mToolbar.getRightText().setVisibility(View.VISIBLE);
        mToolbar.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback, contact;
                feedback = mFeedback.getText().toString();
                contact = mContact.getText().toString();
                if (TextUtils.isEmpty(feedback)) {
                    S.i(mRootView, "请您输入反馈意见");
                    return;
                }
//                if (RegexHelp.isEmail(contact) || RegexHelp.isPhoneNumber(contact)) {
                getModule(SettingModule.class).feedback(feedback, contact);
//                    return;
//                }
                S.i(mRootView, "请您输入正确的联系方式，便于我们联系到您");
            }
        });
    }

    @Override
    protected void dataCallback(int result, Object data) {
        super.dataCallback(result, data);
        S.i(mRootView, (boolean) data ? "意见反馈成功，我们将认真对待你的每一条意见" : "反馈失败，请重试");
    }
}

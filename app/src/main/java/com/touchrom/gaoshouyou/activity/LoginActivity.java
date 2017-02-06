package com.touchrom.gaoshouyou.activity;

import android.os.Bundle;

import com.arialyy.frame.core.NotifyHelp;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.databinding.ActivityLoginBinding;
import com.touchrom.gaoshouyou.entity.UserEntity;
import com.touchrom.gaoshouyou.widget.LoginView;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/1.
 * 登录界面
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements LoginView.LoginCallback {
    @InjectView(R.id.login)
    LoginView mLogin;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("登录");
        mLogin.setLoginCallback(this);
    }

    @Override
    public void onLogin(boolean success, UserEntity entity) {
        if (success) {
            finish();
            NotifyHelp.getInstance().update(Constance.NOTIFY_KEY.USER_FRAGMENT);
        }
    }

    @Override
    public void onReg() {

    }
}

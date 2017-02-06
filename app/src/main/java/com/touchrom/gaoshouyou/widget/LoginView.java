package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.KeyBoardUtils;
import com.google.gson.Gson;
import com.lyy.ui.widget.MyToggleButton;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.AccountEntity;
import com.touchrom.gaoshouyou.entity.UserEntity;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.util.Encryption;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lyy on 2016/3/1.
 * 登录界面
 */
public class LoginView extends RelativeLayout implements View.OnClickListener, TextWatcher {
    private EditText mAccount, mPassword;
    private MyToggleButton mToggle;
    private Button mLogin;
    private TextView mReg, mForgetPw;
    private ServiceUtil mNetUtil;
    private LoginCallback mCallback;

    /**
     * 登录回调
     */
    public interface LoginCallback {
        public void onLogin(boolean success, UserEntity entity);

        public void onReg();
    }

    public LoginView(Context context) {
        this(context, null);
    }

    public LoginView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_login, this, true);
        mAccount = (EditText) findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);
        mToggle = (MyToggleButton) findViewById(R.id.toggle);
        mLogin = (Button) findViewById(R.id.login_bt);
        mReg = (TextView) findViewById(R.id.reg);
        mForgetPw = (TextView) findViewById(R.id.forget_pw);
        mLogin.setOnClickListener(this);
        mReg.setOnClickListener(this);
        mForgetPw.setOnClickListener(this);
        mToggle.setOnToggleChanged(new MyToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                mPassword.setInputType(on ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        mNetUtil = ServiceUtil.getInstance(getContext());
        mAccount.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
        mLogin.setEnabled(false);
        mLogin.setBackgroundResource(R.drawable.bg_login_un_click);
    }

    /**
     * 设置登录回调
     *
     * @param callback
     */
    public void setLoginCallback(@NonNull LoginCallback callback) {
        mCallback = callback;
    }

    private void login() {
        if (TextUtils.isEmpty(mAccount.getText())) {
            mAccount.setError("账号不能为空");
            return;
        } else if (TextUtils.isEmpty(mPassword.getText())) {
            mPassword.setError("登录密码不能为空");
            return;
        }
        KeyBoardUtils.closeKeybord(mPassword, getContext());
        KeyBoardUtils.closeKeybord(mAccount, getContext());
        Map<String, String> params = new HashMap<>();
        params.put("username", mAccount.getText().toString());
        params.put("password", Encryption.encodeSHA1ToString(mPassword.getText().toString()));
        mNetUtil.login(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                UserEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mNetUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), UserEntity.class);
                        AccountEntity account = new AccountEntity();
                        account.setAccount(mAccount.getText().toString());
                        account.setPassword(Encryption.encodeSHA1ToString(mPassword.getText().toString()));
                        AppManager.getInstance().saveLoginState(true);
                        AppManager.getInstance().saveUser(entity);
                        AppManager.getInstance().saveAccount(account);
                        if (entity.isSuccess()) {
                            if (mCallback != null) {
                                mCallback.onLogin(true, entity);
                            }
                        } else {
                            if (mCallback != null) {
                                mCallback.onLogin(false, entity);
                            }
                        }
                    } else {
                        if (mCallback != null) {
                            mCallback.onLogin(false, null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (mCallback != null) {
                        mCallback.onLogin(false, null);
                    }
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                if (mCallback != null) {
                    mCallback.onLogin(false, null);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt:
                login();
                break;
            case R.id.reg:
                if (mCallback != null) {
                    mCallback.onReg();
                }
                break;
            case R.id.forget_pw:
                WebEntity entity = new WebEntity();
                entity.setContentUrl(Constance.URL.FORGET_PASSWORD);
                entity.setTitle("忘记密码");
                TurnHelp.turnNormalWeb(getContext(), entity);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int drawable = R.drawable.bg_login_un_click;
        if (TextUtils.isEmpty(mAccount.getText()) || TextUtils.isEmpty(mPassword.getText())) {
            mLogin.setEnabled(false);
        } else {
            if (mPassword.getText().toString().length() < 6) {
                mLogin.setEnabled(false);
            } else {
                mLogin.setEnabled(true);
                drawable = R.drawable.selector_login_bt;
            }
        }
        mLogin.setBackgroundResource(drawable);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

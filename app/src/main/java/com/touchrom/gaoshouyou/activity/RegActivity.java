package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lyy.ui.widget.CountDownButton;
import com.lyy.ui.widget.MyToggleButton;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityRegBinding;
import com.touchrom.gaoshouyou.entity.AccountEntity;
import com.touchrom.gaoshouyou.entity.UserEntity;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.help.RegexHelp;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.UserModule;
import com.touchrom.gaoshouyou.util.Encryption;
import com.touchrom.gaoshouyou.util.S;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/1.
 * 注册界面
 */
public class RegActivity extends BaseActivity<ActivityRegBinding> implements TextWatcher, View.OnClickListener {
    @InjectView(R.id.phone_num)
    EditText mPhoneEt;
    @InjectView(R.id.code_bt)
    CountDownButton mCodeBt;
    @InjectView(R.id.code_et)
    EditText mCodeEt;
    @InjectView(R.id.password)
    EditText mPw;
    @InjectView(R.id.reg_bt)
    Button mRegBt;
    @InjectView(R.id.toggle)
    MyToggleButton mToggle;
    ColorStateList mTextCl;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_reg;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mToolbar.setTitle("注册账号");
        mCodeBt.setEnabled(false);
        mCodeBt.setBackgroundResource(R.drawable.bg_login_un_click);
        mCodeBt.setTextColor(getMyColor(R.color.skin_white_color));
        mRegBt.setEnabled(false);
        mRegBt.setBackgroundResource(R.drawable.bg_login_un_click);
        mPhoneEt.addTextChangedListener(this);
        mCodeEt.addTextChangedListener(this);
        mPw.addTextChangedListener(this);
        mToggle.setOnToggleChanged(new MyToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                mPw.setInputType(on ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        states[0] = new int[]{android.R.attr.state_pressed};
        colors[0] = getMyColor(R.color.skin_white_color);
        states[1] = new int[]{};
        colors[1] = getMyColor(R.color.skin_primary_color);
        mTextCl = new ColorStateList(states, colors);
        mCodeBt.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.code_bt:
                if (RegexHelp.isPhoneNumber(mPhoneEt.getText().toString())) {
                    getModule(UserModule.class).getCode(1, mPhoneEt.getText().toString());
//                    mCodeBt.resetTimer(10 * 1000, 1000);
                    mCodeBt.start();
                } else {
                    S.i(mRootView, "请输入正确的手机号码");
                }
                break;
            case R.id.reg_agreement:
                WebEntity entity = new WebEntity();
                entity.setContentUrl(Constance.URL.REG_AGREEMENT);
                entity.setTitle("高手游用户注册协议");
                TurnHelp.turnNormalWeb(this, entity);
                break;
            case R.id.reg_bt:
                showLoadingDialog();
                String pw = Encryption.encodeSHA1ToString(mPw.getText().toString());
                getModule(UserModule.class).regAccount(mPhoneEt.getText().toString(), mCodeEt.getText().toString(), pw);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int drawable = R.drawable.bg_login_un_click;
        if (mPhoneEt.hasFocus()) {
            if (!TextUtils.isEmpty(mPhoneEt.getText()) && !mCodeBt.isRunning()) {
                mCodeBt.setEnabled(true);
                mCodeBt.setBackgroundResource(R.drawable.skin_selector_reg_code_bg);
                mCodeBt.setTextColor(mTextCl);
            } else {
                mCodeBt.setEnabled(false);
                mCodeBt.setBackgroundResource(R.drawable.bg_login_un_click);
                mCodeBt.setTextColor(getMyColor(R.color.skin_white_color));
            }
        }

        if (TextUtils.isEmpty(mPhoneEt.getText()) || TextUtils.isEmpty(mCodeEt.getText()) || TextUtils.isEmpty(mPw.getText())) {
            mRegBt.setEnabled(false);
        } else {
            if (mPw.getText().length() < 6) {
                mRegBt.setEnabled(false);
            } else {
                mRegBt.setEnabled(true);
                drawable = R.drawable.selector_login_bt;
            }
        }
        mRegBt.setBackgroundResource(drawable);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.REG) {
            dismissLoadingDialog();
            UserEntity user = (UserEntity) obj;
            if (user.isSuccess()) {
                AppManager.getInstance().saveLoginState(true);
                AppManager.getInstance().saveUser((UserEntity) obj);
                AccountEntity entity = new AccountEntity();
                entity.setPassword(Encryption.encodeSHA1ToString(mPw.getText().toString()));
                entity.setAccount(mPhoneEt.getText().toString());
                AppManager.getInstance().saveAccount(entity);
                Intent intent = new Intent();
                intent.putExtra("user", user);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                S.i(mRootView, user.getRegMsg());
            }
        }
    }
}

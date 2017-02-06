package com.touchrom.gaoshouyou.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.DensityUtils;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.databinding.ActivityAboutBinding;
import com.touchrom.gaoshouyou.dialog.GuideDialog;
import com.touchrom.gaoshouyou.dialog.ShareDialog;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.help.RippleHelp;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.SettingModule;
import com.touchrom.gaoshouyou.util.S;
import com.touchrom.gaoshouyou.widget.SettingNormalInfo;

import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by lyy on 2015/12/2.
 * 关于界面
 */
public class AboutActivity extends BaseActivity<ActivityAboutBinding> {
    @InjectView(R.id.qq)
    SettingNormalInfo mQQ;
    @InjectView(R.id.weixin)
    SettingNormalInfo mWeixin;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        ShareSDK.initSDK(this);
        mToolbar.setTitle("关于");
        getBinding().setVersion(AndroidUtils.getVersionName(this));
        initWidget();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    private void initWidget() {
        View welcome = findViewById(R.id.welcome);
        View business = findViewById(R.id.business);
        View help = findViewById(R.id.help);
        View gsy = findViewById(R.id.gsy);
        View weibo = findViewById(R.id.weibo);
        View useServerAgreement = findViewById(R.id.use_server_agreement);
        View dutyRuleUrl = findViewById(R.id.duty_rule_url);
        if (AndroidVersionUtil.hasIcecreamsandwich()) {
            RippleHelp.createRipple(this, welcome);
            RippleHelp.createRipple(this, business);
            RippleHelp.createRipple(this, help);
            RippleHelp.createRipple(this, gsy);
            RippleHelp.createRipple(this, weibo);
            RippleHelp.createRipple(this, mQQ);
            RippleHelp.createRipple(this, mWeixin);
            RippleHelp.createRipple(this, useServerAgreement);
            RippleHelp.createRipple(this, dutyRuleUrl);
        } else {
            welcome.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_setting_widget));
            business.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_setting_widget));
            help.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_setting_widget));
            gsy.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_setting_widget));
            weibo.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_setting_widget));
            mQQ.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_setting_widget));
            mWeixin.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_setting_widget));
            useServerAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_setting_widget));
            dutyRuleUrl.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_setting_widget));
        }
        ImageView rightIcon = mToolbar.getRightIcon();
        rightIcon.setImageDrawable(getResources().getDrawable(R.mipmap.icon_share));
        int p = DensityUtils.dp2px(8);
        rightIcon.setPadding(p, 0, p, 0);
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog(SettingModule.SHARE_TYPE_GSY_APP, 0);
                shareDialog.show(AboutActivity.this.getSupportFragmentManager(), "shareDialog");
            }
        });
    }

    public void onClick(View view) {
        WebEntity webEntity = null;
        switch (view.getId()) {
            case R.id.welcome:
                GuideDialog guide = new GuideDialog(this);
                guide.show(getSupportFragmentManager(), "guide");
                break;
            case R.id.business:
                webEntity = new WebEntity(Constance.URL.BUSINESS_URL, "商务合作");
                break;
            case R.id.help:
                webEntity = new WebEntity(Constance.URL.HELP_URL, "帮助中心");
                break;
            case R.id.gsy:
                webEntity = new WebEntity(Constance.URL.GSY_URL, "高手游");
                break;
            case R.id.weibo:
                webEntity = new WebEntity(Constance.URL.WEIBO_URL, "高手游微博");
                break;
            case R.id.qq:
                copyText(mQQ.getTitle(), mQQ.getValue());
                S.i(mRootView, "已将QQ群号复制到剪切板了");
                break;
            case R.id.weixin:
                copyText(mWeixin.getTitle(), mWeixin.getValue());
                S.i(mRootView, "已将公众号信息复制到剪切板了");
                break;
            case R.id.use_server_agreement:
                webEntity = new WebEntity(Constance.URL.USE_SERVER_AGREEMENT_URL, "用户服务协议");
                break;
            case R.id.duty_rule_url:
                webEntity = new WebEntity(Constance.URL.DUTY_RULE_URL, "免责声明");
                break;
        }
        if (webEntity != null) {
            TurnHelp.turnNormalWeb(this, webEntity);
        }
    }

    /**
     * 复制文字到剪切板
     */
    private void copyText(CharSequence label, CharSequence text) {
        ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (AndroidVersionUtil.hasHoneycomb()) {
            cmb.setPrimaryClip(ClipData.newPlainText(label, text));
        } else {
            S.i(mRootView, "android 3.0 以下并不支持将文字复制到剪切板");
        }
    }

    @Override
    protected void dataCallback(int result, Object data) {
        super.dataCallback(result, data);
    }
}

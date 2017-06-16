package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.NetUtils;
import com.igexin.sdk.PushManager;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityLauncherBinding;
import com.touchrom.gaoshouyou.entity.LauncherEntity;
import com.touchrom.gaoshouyou.module.LauncherModule;

import butterknife.InjectView;

//import com.lyy.ui.group.TagFlowLayout2;

/**
 * Created by lk on 2015/11/12.
 * 启动界面
 */
public class LauncherActivity extends BaseActivity<ActivityLauncherBinding> {
    @InjectView(R.id.img)
    ImageView mImg;
    private static final long DELAY_TIME = 1000 * 2;
    private LauncherEntity mEntity;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // 设置状态栏可用
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (AndroidVersionUtil.hasLollipop()) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            Fade in = new Fade(Fade.IN);
            in.setDuration(2000);
            Fade out = new Fade(Fade.OUT);
            out.setDuration(2000);
            getWindow().setExitTransition(in);
            getWindow().setEnterTransition(out);
        }
        setUsDefaultAnim(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        launcherFlow();
    }

    private void launcherFlow() {
        getModule(LauncherModule.class).launcherFlows(mImg);
    }

    private void startApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetUtils.isConnected(LauncherActivity.this)) {
                    getModule(LauncherModule.class).startOtherFlow(LauncherActivity.this);

                }
                getModule(LauncherModule.class).startAppFlow(LauncherActivity.this, getSupportFragmentManager());
//                mApp.getAppManager().finishActivity(LauncherActivity.this);
            }
        }, DELAY_TIME);
    }

    @Override
    protected void dataCallback(int result, Object data) {
        if (result == ResultCode.LAUNCHER_FLOWS) {
            startApp();
        } else if (result == ResultCode.DIALOG.GUIDE) {
            startActivity(new Intent(this, MainActivity.class));
//            mApp.getAppManager().finishActivity(this);
        } else if (result == ResultCode.DIALOG.DOWNLOAD) {
            if (!mEntity.isForcedUpdate()) {
                startApp();
            }
        }
    }
}

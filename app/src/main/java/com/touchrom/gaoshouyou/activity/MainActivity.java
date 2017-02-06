package com.touchrom.gaoshouyou.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arialyy.frame.util.AndroidVersionUtil;
import com.lyy.ui.widget.IconEditText;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityMainBinding;
import com.touchrom.gaoshouyou.entity.LauncherEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.fragment.AMApkFragment;
import com.touchrom.gaoshouyou.fragment.find.FindContentFragment;
import com.touchrom.gaoshouyou.fragment.game.GameContentFragment;
import com.touchrom.gaoshouyou.fragment.news.NewsContentFragment;
import com.touchrom.gaoshouyou.fragment.user.UserFragment;
import com.touchrom.gaoshouyou.module.LauncherModule;
import com.touchrom.gaoshouyou.qr_code.QRScanActivity;
import com.touchrom.gaoshouyou.service.DownloadService;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements RadioGroup.OnCheckedChangeListener {
    @InjectView(R.id.navigation)
    RadioGroup mNavigationBar;
    @InjectView(R.id.download)
    ImageView mDownload;
    @InjectView(R.id.level)
    RelativeLayout mLeave;
    @InjectView(R.id.leave_num)
    TextView mLeaveNum;
    @InjectView(R.id.leave_text)
    TextView mLeaveText;
    @InjectView(R.id.search_et)
    IconEditText mSearchEt;
    @InjectView(R.id.scan_zxing)
    ImageView mLeftIcon;
    @InjectView(R.id.search)
    View mTemp;
    private int mNum = 0;

    private FragmentManager mFm;
    List<Fragment> mFragments = new ArrayList<>();

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshDot();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AndroidVersionUtil.hasLollipop()) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            Fade in = new Fade(Fade.IN);
            in.setDuration(1000);
            Fade out = new Fade(Fade.OUT);
            out.setDuration(1000);
            getWindow().setExitTransition(in);
            getWindow().setEnterTransition(out);
        }
        setUsDefaultAnim(false);
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AMApkFragment.ACTION_COMPLETE);
        filter.addAction(DownloadService.ACTION_STATE_CHANGE);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getModule(LauncherModule.class).checkNewVersion();
            }
        }, 1500);
        initFragment();
        initWidget();
    }

    private void initFragment() {
        mFragments.add(GameContentFragment.getInstance());
        mFragments.add(NewsContentFragment.getInstance());
        mFragments.add(FindContentFragment.getInstance());
        mFragments.add(UserFragment.getInstance());
        mFm = getSupportFragmentManager();
        FragmentTransaction ft = mFm.beginTransaction();
        ft.add(R.id.content, mFragments.get(0), "0");
        ft.add(R.id.content, mFragments.get(1), "1");
        ft.add(R.id.content, mFragments.get(2), "2");
        ft.add(R.id.content, mFragments.get(3), "3");
        ft.commit();
    }

    private void initWidget() {
        int size = (int) getResources().getDimension(R.dimen.navigation_top_icon_size);
        int[] icons = new int[]{
                R.drawable.selector_main_game_bg,
                R.drawable.selector_main_news_bg,
                R.drawable.selector_main_find_bg,
                R.drawable.selector_main_me_bg
        };
        for (int i = 0, count = mNavigationBar.getChildCount(); i < count; i++) {
            RadioButton rb = (RadioButton) mNavigationBar.getChildAt(i);
            Drawable topIcon = getResources().getDrawable(icons[i]);
            assert topIcon != null;
            topIcon.setBounds(0, 0, size, size);
            rb.setCompoundDrawables(null, topIcon, null, null);
            rb.setId(i);
        }
        mNavigationBar.setOnCheckedChangeListener(this);
        ((RadioButton) mNavigationBar.getChildAt(0)).setChecked(true);
        refreshDot();
    }

    /**
     * 设置等级显示状态
     *
     * @param visibility
     */
    public void setLeaveVisibility(int visibility) {
        mLeave.setVisibility(visibility);
    }

    /**
     * 设置等级数据
     *
     * @param leave
     * @param leaveTag
     */
    public void setLeave(String leave, String leaveTag) {
        mLeave.setVisibility(View.VISIBLE);
        mLeaveNum.setText("Lv" + leave);
        mLeaveText.setText(leaveTag);
    }

    /**
     * 刷新小红点
     */
    public void refreshDot() {
        List<DownloadEntity> entities = DownloadEntity.findAll(DownloadEntity.class);
        int count = 0;
        for (DownloadEntity entity : entities) {
            if (entity.getState() != DownloadEntity.STATE_COMPLETE) {
                count++;
            }
        }
        if (count == 0) {
            mDownload.setImageResource(R.mipmap.icon_download);
        } else {
            mDownload.setImageResource(R.mipmap.icon_has_download_task);
        }
    }

    @Override
    public void onBackPressed() {
        if (onDoubleClickExit()) {
            exitApp();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.scan_zxing:
                if (mNum == 3) {
                    intent = new Intent(this, SettingActivity.class);
                } else {
                    intent = new Intent(this, QRScanActivity.class);
                }
                break;
            case R.id.download:
                intent = new Intent(this, AppManagerActivity.class);
                break;
            case R.id.search:
                intent = new Intent(this, SearchActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void chooseFragment(String tag) {
        mNum = Integer.parseInt(tag);
        if (mFragments != null) {
            for (Fragment fragment : mFragments) {
                if (fragment.hashCode() == mFragments.get(mNum).hashCode()) {
                    if (fragment.isHidden()) {
                        mFm.beginTransaction().show(fragment).commit();
                    }
                } else {
                    mFm.beginTransaction().hide(fragment).commit();
                }
            }
        }
        if (mNum == 3) {
            mSearchEt.setVisibility(View.GONE);
            mTemp.setVisibility(View.GONE);
//            mLeave.setVisibility(AppManager.getInstance().isLogin() ? View.VISIBLE : View.GONE);
            mLeftIcon.setImageDrawable(getResources().getDrawable(R.mipmap.icon_setting_white));
        } else {
            mSearchEt.setVisibility(View.VISIBLE);
            mTemp.setVisibility(View.VISIBLE);
//            mLeave.setVisibility(View.GONE);
            mLeftIcon.setImageDrawable(getResources().getDrawable(R.mipmap.icon_scan));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton rb = (RadioButton) group.getChildAt(checkedId);
        if (rb.isChecked()) {
            chooseFragment(String.valueOf(rb.getTag()));
        }
    }

    @Override
    protected void dataCallback(int result, final Object data) {
        super.dataCallback(result, data);
        if (result == ResultCode.CHECK_NEW_VERSION) {
            dismissLoadingDialog();
            if (data == null) {
                return;
            }
            if (data instanceof LauncherEntity) {
                LauncherEntity entity = (LauncherEntity) data;
                if (entity.isHasNewVersion()) {
                    getModule(LauncherModule.class).startVersionDialogFlow(entity, MainActivity.this);
                }
            }
        }
    }
}

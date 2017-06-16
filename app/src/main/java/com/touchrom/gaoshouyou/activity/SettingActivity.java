package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.arialyy.frame.cache.CacheUtil;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.CalendarUtils;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.widget.MyToggleButton;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivitySettingBinding;
import com.touchrom.gaoshouyou.dialog.ChangeApkLocationDialog;
import com.touchrom.gaoshouyou.entity.LauncherEntity;
import com.touchrom.gaoshouyou.entity.MsgSettingEntity;
import com.touchrom.gaoshouyou.entity.SettingEntity;
import com.touchrom.gaoshouyou.help.RippleHelp;
import com.touchrom.gaoshouyou.module.LauncherModule;
import com.touchrom.gaoshouyou.module.SettingModule;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.util.ExternalStorage;
import com.touchrom.gaoshouyou.util.S;
import com.touchrom.gaoshouyou.widget.SettingNormalInfo;
import com.touchrom.gaoshouyou.widget.SettingSwitchInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import butterknife.InjectView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lk on 2015/11/30.
 * 设置界面
 */
public class SettingActivity extends BaseActivity<ActivitySettingBinding> {
    @InjectView(R.id.show_img)
    SettingSwitchInfo mShowImg;
    @InjectView(R.id.download_again)
    SettingSwitchInfo mDownloadAgain;
    @InjectView(R.id.auto_download_new_apk)
    SettingSwitchInfo mAutoDownloadNewApp;
    @InjectView(R.id.auto_install_apk)
    SettingSwitchInfo mAutoInstallApk;
    @InjectView(R.id.auto_delete_apk)
    SettingSwitchInfo mAutoDeleteApk;
    @InjectView(R.id.quickly_handle_apk)
    SettingSwitchInfo mQuicklyHandleApk;
    @InjectView(R.id.apk_location)
    SettingNormalInfo mApkDownloadLocation;
    @InjectView(R.id.msg)
    SettingNormalInfo mMsgSetting;
    @InjectView(R.id.other)
    SettingNormalInfo mVersionCheck;
    @InjectView(R.id.feedback)
    SettingNormalInfo mFeedback;
    @InjectView(R.id.about)
    SettingNormalInfo mAbout;
    @InjectView(R.id.clean_cache)
    SettingNormalInfo mClean;
    private SettingEntity mSettingEntity;
    private String mLocation;
    private String mLastCheckTime;
    private String mMsgValue;
    private MsgSettingEntity mSetMsgEntity;
    private int mLocationType = 0;
    private CacheUtil mCu;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mToolbar.setTitle("设置");
        mSettingEntity = getSettingEntity();
        mShowImg.setSwitchState(mSettingEntity.isShowImg());
        mDownloadAgain.setSwitchState(mSettingEntity.isDownloadAgain());
        mAutoDownloadNewApp.setSwitchState(mSettingEntity.isAutoDownloadNewApp());
        mAutoInstallApk.setSwitchState(mSettingEntity.isAutoInstallApk());
        mAutoDeleteApk.setSwitchState(mSettingEntity.isAutoDeleteApk());
        mApkDownloadLocation.setValue(mSettingEntity.getDownloadLocation());
        mSetMsgEntity = mSettingEntity.getSettingEntity();
        setMsgValue(mSetMsgEntity);
        mVersionCheck.setValue(mSettingEntity.getLastCheckTime());
        if (AndroidVersionUtil.hasIcecreamsandwich()) {
            RippleHelp.createRipple(this, mApkDownloadLocation);
            RippleHelp.createRipple(this, mMsgSetting);
            RippleHelp.createRipple(this, mVersionCheck);
            RippleHelp.createRipple(this, mFeedback);
            RippleHelp.createRipple(this, mAbout);
            RippleHelp.createRipple(this, mClean);
        }
        Map<String, File> externalLocations = ExternalStorage.getAllStorageLocations();
        if (externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD) == null) {
            mApkDownloadLocation.setEnabled(false);
            mApkDownloadLocation.setBackgroundColor(Color.parseColor("#11000000"));
        }
        mAbout.setValue(AndroidUtils.getVersionName(this));
        mQuicklyHandleApk.setSwitchState(mSettingEntity.isQuicklyHandleApk());
        if (!AndroidUtils.isRoot()) {
            mQuicklyHandleApk.getToggleBt().setEnabled(false);
            mQuicklyHandleApk.setEnabled(false);
//            mQuicklyHandleApk.setBackgroundColor(Color.parseColor("#11000000"));
            mQuicklyHandleApk.showTempView(true);
        }
        mQuicklyHandleApk.setOnToggleBtListener(new MyToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on && !AndroidUtils.getRootPermission(getPackageCodePath())) {
                    S.i(getRootView(), "获取Root权限失败，请重新获取");
                    mQuicklyHandleApk.setSwitchState(false);
                }
            }
        });
        mCu = new CacheUtil(this, false);
        mClean.setValue("缓存大小：" + mCu.getCacheSize());
    }

    /**
     * 设置msg提示信息
     *
     * @param setMsgEntity
     */
    private void setMsgValue(MsgSettingEntity setMsgEntity) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        Field[] fields = setMsgEntity.getClass().getDeclaredFields();
        sb1.append("[");
        try {
            for (int i = fields.length - 1; i > -1; i--) {
                Field field = fields[i];
                field.setAccessible(true);
                Object obj = field.get(setMsgEntity);
                if (obj instanceof MsgSettingEntity.Element) {
                    MsgSettingEntity.Element element = (MsgSettingEntity.Element) obj;
                    if (element.isOpen()) {
                        sb.append(element.getTitle()).append("、");
                    }
                    sb1.append("{").append("\"").append(field.getName()).append("\":")
                            .append(element.isOpen()).append("},");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String str = sb1.toString();
        str = str.substring(0, str.length() - 1) + "]";
        getModule(SettingModule.class).notifySetting(str);
        String msg = sb.toString();
        msg = TextUtils.isEmpty(msg) ? "已关闭所有通知" : msg.substring(0, msg.length() - 1);
        mMsgSetting.setValue(msg);
    }

    public void onClick(View view) {
        int requestCode = -1;
        Intent intent = null;
        switch (view.getId()) {
            case R.id.apk_location:
                ChangeApkLocationDialog calDialog = new ChangeApkLocationDialog(this,
                        mSettingEntity.getLocationType(), mSettingEntity.getDownloadLocation());
                calDialog.show(getSupportFragmentManager(), "calDialog");
                break;
            case R.id.msg:
                requestCode = 0;
                intent = new Intent(this, SettingMsgActivity.class);
                intent.putExtra(Constance.KEY.PARCELABLE_ENTITY, mSettingEntity.getSettingEntity());
                startActivityForResult(intent, requestCode);
                return;
            case R.id.other:
                showLoadingDialog();
                getModule(LauncherModule.class).checkNewVersion();
                break;
            case R.id.feedback:
                intent = new Intent(this, FeedbackActivity.class);
                break;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                break;
            case R.id.clean_cache:
                showLoadingDialog();
                Observable.just("").subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (mCu != null) {
                                    mCu.removeAll();
                                }
                                dismissLoadingDialog();
                            }
                        });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mClean.setValue("缓存大小：0.0Byte(s)");
                    }
                }, 500);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

    }

    @Override
    public void finish() {
        super.finish();
        saveSetting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCu != null) {
            mCu.close();
        }
    }

    private void saveSetting() {
        mSettingEntity.setShowImg(mShowImg.getSwitchState());
        mSettingEntity.setDownloadAgain(mDownloadAgain.getSwitchState());
        mSettingEntity.setAutoDownloadNewApp(mAutoDownloadNewApp.getSwitchState());
        mSettingEntity.setAutoInstallApk(mAutoInstallApk.getSwitchState());
        mSettingEntity.setAutoDeleteApk(mAutoDeleteApk.getSwitchState());
        mSettingEntity.setQuicklyHandleApk(mQuicklyHandleApk.getSwitchState());
        mSettingEntity.setDownloadLocation(TextUtils.isEmpty(mLocation) ? Constance.PATH.DEFAULT_DOWNLOAD_LOCATION : mLocation);
        mSettingEntity.setLocationType(mLocationType);
        mSettingEntity.setSettingEntity(mSetMsgEntity);
        mSettingEntity.setLastCheckTime(TextUtils.isEmpty(mLastCheckTime) ? mVersionCheck.getValue() + "" : mLastCheckTime);
        setSettingEntity(mSettingEntity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 110) {
            mSetMsgEntity = data.getParcelableExtra(Constance.KEY.PARCELABLE_ENTITY);
            setMsgValue(mSetMsgEntity);
            saveSetting();
        }
    }

    @Override
    protected void dataCallback(int result, final Object data) {
        if (data instanceof Integer && (int) data == ServiceUtil.ERROR) {
            dismissLoadingDialog();
            S.i(mRootView, "网络错误，请重试");
            return;
        }
        if (result == ResultCode.CHANGE_APK_LOCATION) {
            Object[] d = (Object[]) data;
            mLocationType = (int) d[0];
            mLocation = String.valueOf(d[1]);
            File file = new File(mLocation);
            if (!file.exists()) {
                file.mkdirs();
            }
            mApkDownloadLocation.setValue(mLocation);
            saveSetting();
        } else if (result == ResultCode.CHECK_NEW_VERSION) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissLoadingDialog();
                    mLastCheckTime = CalendarUtils.formatDateTimeWithTime(System.currentTimeMillis() + "");
                    mVersionCheck.setValue(mLastCheckTime);
                    mSettingEntity.setLastCheckTime(mLastCheckTime);
                    LauncherEntity entity = (LauncherEntity) data;
                    if (entity.isHasNewVersion()) {
                        getModule(LauncherModule.class).startVersionDialogFlow(entity, SettingActivity.this);
                    } else {
                        S.i(mRootView, "当前已是最新版本");
                    }
                }
            }, 1000);
        }
    }
}

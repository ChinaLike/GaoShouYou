package com.touchrom.gaoshouyou.base;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.arialyy.frame.util.SharePreUtil;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.AccountEntity;
import com.touchrom.gaoshouyou.entity.SettingEntity;
import com.touchrom.gaoshouyou.entity.UserEntity;

/**
 * Created by lk on 2015/11/9.
 * App管理器
 */
public class AppManager {
    private static final String TAG = "AppManager";
    private static volatile AppManager mManager = null;
    private Context mContext;
    private static final Object LOCK = new Object();
    private int mNetState = -1;

    private AppManager() {

    }

    private AppManager(Context context) {
        mContext = context;
    }

    protected static void init(Context context) {
        if (mManager == null) {
            synchronized (LOCK) {
                if (mManager == null) {
                    mManager = new AppManager(context);
                }
            }
        }
    }

    public static AppManager getInstance() {
        if (mManager == null) {
            throw new NullPointerException("请在Application里面初始化APPManager!!");
        }
        return mManager;
    }

    public void loginOut() {
        saveLoginState(false);
        SharePreUtil.removeKey(Constance.APP.NAME, mContext, Constance.KEY.USER);
        SharePreUtil.removeKey(Constance.APP.NAME, mContext, Constance.KEY.ACCOUNT);
    }

    public void saveAccount(@NonNull AccountEntity entity) {
        SharePreUtil.putObject(Constance.APP.NAME, mContext, Constance.KEY.ACCOUNT, AccountEntity.class, entity);
    }

    public AccountEntity getAccount() {
        return SharePreUtil.getObject(Constance.APP.NAME, mContext, Constance.KEY.ACCOUNT, AccountEntity.class);
    }

    public Long getFindApkLastTime() {
        return SharePreUtil.getObject(Constance.APP.NAME, mContext, Constance.KEY.FIND_APK_TIME, Long.class);
    }

    public void saveFindApkTime(Long findTime) {
        SharePreUtil.putObject(Constance.APP.NAME, mContext, Constance.KEY.FIND_APK_TIME, Long.class, findTime);
    }

    public SettingEntity getSetting() {
        return SharePreUtil.getObject(Constance.APP.NAME, mContext, Constance.KEY.SETTING, SettingEntity.class);
    }

    public void saveSetting(SettingEntity settingEntity) {
        SharePreUtil.putObject(Constance.APP.NAME, mContext, Constance.KEY.SETTING, SettingEntity.class, settingEntity);
    }

    public synchronized void setNetState(int state) {
        mNetState = state;
    }

    public synchronized int getNetState() {
        return mNetState;
    }

    public UserEntity getUser() {
        return SharePreUtil.getObject(Constance.APP.NAME, mContext, Constance.KEY.USER, UserEntity.class);
    }

    public void saveUser(UserEntity userEntity) {
        SharePreUtil.putObject(Constance.APP.NAME, mContext, Constance.KEY.USER, UserEntity.class, userEntity);
    }

    public void saveCurrentColor(@ColorInt int color) {
        SharePreUtil.putInt(Constance.APP.NAME, mContext, Constance.KEY.CURRENT_COLOR, color);
    }

    public int getCurrentColor() {
        return SharePreUtil.getInt(Constance.APP.NAME, mContext, Constance.KEY.CURRENT_COLOR);
    }

    public void saveStateBarColor(@ColorInt int color) {
        SharePreUtil.putInt(Constance.APP.NAME, mContext, Constance.KEY.STATE_BAR_COLOR, color);
    }

    public int getStateBarColor() {
        return SharePreUtil.getInt(Constance.APP.NAME, mContext, Constance.KEY.STATE_BAR_COLOR);
    }

    public void saveTheme(int theme) {
        SharePreUtil.putInt(Constance.APP.NAME, mContext, Constance.KEY.THEME, theme);
    }

    public int getTheme() {
        return SharePreUtil.getInt(Constance.APP.NAME, mContext, Constance.KEY.THEME);
    }

    public void saveLoginState(boolean state) {
        SharePreUtil.putBoolean(Constance.APP.NAME, mContext, Constance.KEY.LOGIN_STATE, state);
    }

    public boolean isLogin() {
        return SharePreUtil.getBoolean(Constance.APP.NAME, mContext, Constance.KEY.LOGIN_STATE);
    }


}

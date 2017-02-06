package com.touchrom.gaoshouyou.base;

import android.content.Context;

import com.arialyy.frame.core.AbsApplication;
import com.arialyy.frame.util.CalendarUtils;
import com.touchrom.gaoshouyou.entity.AccountEntity;
import com.touchrom.gaoshouyou.entity.SettingEntity;
import com.touchrom.gaoshouyou.entity.UserEntity;

import org.litepal.LitePalApplication;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by lyy on 2015/11/6.
 */
public class BaseApplication extends AbsApplication {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LitePalApplication.initialize(this);
        AppManager.init(getApplicationContext());
        ShareSDK.initSDK(this);
        setDefaultInfo();
    }

    /**
     * 设置默认信息
     */
    private void setDefaultInfo() {
        AppManager app = AppManager.getInstance();
        SettingEntity settingEntity = app.getSetting();
        if (settingEntity == null) {
            settingEntity = new SettingEntity();
            settingEntity.setLastCheckTime(CalendarUtils.formatDateTimeWithTime(System.currentTimeMillis() + ""));
            app.saveSetting(settingEntity);
        }
        Long findT = app.getFindApkLastTime();
        if (findT == null) {
            app.saveFindApkTime(0L);
        }
        UserEntity entity = app.getUser();
        if (entity == null) {
            app.saveLoginState(false);
        }
        AccountEntity account = app.getAccount();
        if (account == null) {
            app.saveLoginState(false);
        }
    }

    public static Context getAppContext() {
        return mContext;
    }
}

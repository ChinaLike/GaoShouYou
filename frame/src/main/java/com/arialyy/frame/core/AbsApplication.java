package com.arialyy.frame.core;

import org.litepal.LitePalApplication;

/**
 * Created by lyy on 2015/11/4.
 * 基础Application
 */
public class AbsApplication extends LitePalApplication {
    private ApplicationManager mAppManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppManager = ApplicationManager.getInstance(getApplicationContext());
    }

    public ApplicationManager getAppManager() {
        return mAppManager;
    }

    public void setAppManager(ApplicationManager appManager) {
        this.mAppManager = appManager;
    }

    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行,如果为true则为后台运行
     */
    public void exitApp(Boolean isBackground) {
        mAppManager.AppExit(this, isBackground);
    }
}

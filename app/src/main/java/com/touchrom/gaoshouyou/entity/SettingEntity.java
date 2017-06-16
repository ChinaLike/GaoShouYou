package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;
import com.touchrom.gaoshouyou.config.Constance;

/**
 * Created by lk on 2015/12/1.
 * 设置实体
 */
public class SettingEntity extends BaseEntity {
    private boolean showImg = true;
    private boolean downloadAgain = false;
    private boolean autoDownloadNewApp = false;

    private boolean autoInstallApk = false;
    private boolean autoDeleteApk = false;
    private boolean quicklyHandleApk = false;

    private String downloadLocation = Constance.PATH.DEFAULT_DOWNLOAD_LOCATION;
    /**
     * 0 --> 内置存储、1 --> 外置内存卡
     */
    private int locationType = 0;

    private MsgSettingEntity settingEntity = new MsgSettingEntity();

    private String lastCheckTime = "";

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public String getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(String lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public boolean isShowImg() {
        return showImg;
    }

    public void setShowImg(boolean showImg) {
        this.showImg = showImg;
    }

    public boolean isDownloadAgain() {
        return downloadAgain;
    }

    public void setDownloadAgain(boolean downloadAgain) {
        this.downloadAgain = downloadAgain;
    }

    public boolean isAutoDownloadNewApp() {
        return autoDownloadNewApp;
    }

    public void setAutoDownloadNewApp(boolean autoDownloadNewApp) {
        this.autoDownloadNewApp = autoDownloadNewApp;
    }

    public boolean isAutoInstallApk() {
        return autoInstallApk;
    }

    public void setAutoInstallApk(boolean autoInstallApk) {
        this.autoInstallApk = autoInstallApk;
    }

    public boolean isAutoDeleteApk() {
        return autoDeleteApk;
    }

    public void setAutoDeleteApk(boolean autoDeleteApk) {
        this.autoDeleteApk = autoDeleteApk;
    }

    public boolean isQuicklyHandleApk() {
        return quicklyHandleApk;
    }

    public void setQuicklyHandleApk(boolean quicklyHandleApk) {
        this.quicklyHandleApk = quicklyHandleApk;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }

    public void setDownloadLocation(String downloadLocation) {
        this.downloadLocation = downloadLocation;
    }

    public MsgSettingEntity getSettingEntity() {
        return settingEntity;
    }

    public void setSettingEntity(MsgSettingEntity settingEntity) {
        this.settingEntity = settingEntity;
    }
}

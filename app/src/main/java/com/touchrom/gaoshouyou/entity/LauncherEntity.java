package com.touchrom.gaoshouyou.entity;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2015/11/12.
 * 启动实体
 */
public class LauncherEntity extends BaseEntity {
    /**
     * 是否有升级
     */
    @SerializedName("update")
    private boolean hasNewVersion = false;
    /**
     * 启动图路径
     */
    private String imgUrl;
    /**
     * 版本升级标题
     */
    private String versionTitle;
    /**
     * 版本升级信息
     */
    private String versionMsg;
    /**
     * app下载路径
     */
    private String downloadUrl;
    /**
     * 强制升级
     */
    private boolean forcedUpdate = false;
    /**
     * 版本号
     */
    private String versionCode = "";
    /**
     * 版本名
     */
    private String versionName = "";

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public boolean isHasNewVersion() {
        return hasNewVersion;
    }

    public void setHasNewVersion(boolean hasNewVersion) {
        this.hasNewVersion = hasNewVersion;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVersionTitle() {
        return versionTitle;
    }

    public void setVersionTitle(String versionTitle) {
        this.versionTitle = versionTitle;
    }

    public String getVersionMsg() {
        return versionMsg;
    }

    public void setVersionMsg(String versionMsg) {
        this.versionMsg = versionMsg;
    }

    public boolean isForcedUpdate() {
        return forcedUpdate;
    }

    public void setForcedUpdate(boolean forcedUpdate) {
        this.forcedUpdate = forcedUpdate;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}

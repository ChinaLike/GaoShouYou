package com.touchrom.gaoshouyou.entity.sql;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseSqlEntity;

/**
 * Created by lyy on 2016/1/8.
 * app更新实体
 */
public class ApkUpdateInfoEntity extends BaseSqlEntity implements Parcelable {
    public static final int STATE_UN_UPDATE = 0; //未更新
    public static final int STATE_UPDATING = 1; //更新中
    public static final int STATE_UPDATE_COMPLETE = 2; //更新完成
    public static final int STATE_UPDATE_IGNORE = 3; //忽略


    @SerializedName("update")
    private boolean hasUpdate = false;
    //    private int update = 0;
    private int appId;
    @SerializedName("name")
    private String appName;
    private String imgUrl;
    private String downloadUrl;
    private String newVersionCode;
    private String newVersionName;
    private String packageName;
    private String size;
    private String oldVersionCode;  //自己获取的
    private String oldVersionName;  //自己获取的
    private int updateState = STATE_UN_UPDATE;    //更新状态

    public int getUpdateState() {
        return updateState;
    }

    public void setUpdateState(int updateState) {
        this.updateState = updateState;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getOldVersionName() {
        return oldVersionName;
    }

    public void setOldVersionName(String oldVersionName) {
        this.oldVersionName = oldVersionName;
    }

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getOldVersionCode() {
        return oldVersionCode;
    }

    public void setOldVersionCode(String oldVersionCode) {
        this.oldVersionCode = oldVersionCode;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getNewVersionCode() {
        return newVersionCode;
    }

    public void setNewVersionCode(String newVersionCode) {
        this.newVersionCode = newVersionCode;
    }

    public String getNewVersionName() {
        return newVersionName;
    }

    public void setNewVersionName(String newVersionName) {
        this.newVersionName = newVersionName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ApkUpdateInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(hasUpdate ? (byte) 1 : (byte) 0);
        dest.writeInt(this.appId);
        dest.writeString(this.appName);
        dest.writeString(this.imgUrl);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.newVersionCode);
        dest.writeString(this.newVersionName);
        dest.writeString(this.packageName);
        dest.writeString(this.size);
        dest.writeString(this.oldVersionCode);
        dest.writeString(this.oldVersionName);
        dest.writeInt(this.updateState);
    }

    protected ApkUpdateInfoEntity(Parcel in) {
        this.hasUpdate = in.readByte() != 0;
        this.appId = in.readInt();
        this.appName = in.readString();
        this.imgUrl = in.readString();
        this.downloadUrl = in.readString();
        this.newVersionCode = in.readString();
        this.newVersionName = in.readString();
        this.packageName = in.readString();
        this.size = in.readString();
        this.oldVersionCode = in.readString();
        this.oldVersionName = in.readString();
        this.updateState = in.readInt();
    }

    public static final Creator<ApkUpdateInfoEntity> CREATOR = new Creator<ApkUpdateInfoEntity>() {
        public ApkUpdateInfoEntity createFromParcel(Parcel source) {
            return new ApkUpdateInfoEntity(source);
        }

        public ApkUpdateInfoEntity[] newArray(int size) {
            return new ApkUpdateInfoEntity[size];
        }
    };

    @Override
    public String toString() {
        return "ApkUpdateInfoEntity{" +
                "hasUpdate=" + hasUpdate +
                ", appId=" + appId +
                ", appName='" + appName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", newVersionCode='" + newVersionCode + '\'' +
                ", newVersionName='" + newVersionName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", size='" + size + '\'' +
                ", oldVersionCode='" + oldVersionCode + '\'' +
                ", oldVersionName='" + oldVersionName + '\'' +
                ", updateState=" + updateState +
                '}';
    }
}

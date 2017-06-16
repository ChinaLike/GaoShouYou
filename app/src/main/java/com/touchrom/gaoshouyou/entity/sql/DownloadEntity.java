package com.touchrom.gaoshouyou.entity.sql;

import android.os.Parcel;
import android.os.Parcelable;

import com.touchrom.gaoshouyou.base.BaseSqlEntity;

/**
 * Created by lk on 2015/12/25.
 * 下载实体
 */
public class DownloadEntity extends BaseSqlEntity implements Parcelable, Cloneable {
    /**
     * 其它状态
     */
    public static final int STATE_OTHER = -1;
    /**
     * 失败状态
     */
    public static final int STATE_FAIL = 0;
    /**
     * 完成状态
     */
    public static final int STATE_COMPLETE = 1;
    /**
     * 停止状态
     */
    public static final int STATE_STOP = 2;
    /**
     * 未开始状态
     */
    public static final int STATE_WAIT = 3;
    /**
     * 未完成状态
     */
    public static final int STATE_UN_COMPLETE = 4;
    /**
     * 下载中
     */
    public static final int STATE_DOWNLOAD_ING = 5;

    private int gameId; //游戏id
    private String name;    //游戏名
    private String imgUrl;  //icon路径
    private String downloadUrl; //下载路径

    private String completeTime;  //完成时间
    private String size;        //大小
    private long maxLen = 1;
    private int apkVersionConde;    //apk版本号
    private boolean caAutoInstall = true;    //是否能自动安装

    /**
     * 下载状态：
     * -1 --> 其它
     * 0 -- > 失败
     * 1 --> 完成
     * 2 --> 停止
     */
    private int state = STATE_WAIT;

    private boolean isDownloadComplete = false;   //是否下载完成
    private boolean isInstalled = false;            //已安装
    private boolean autoStart = false;      //自动下载
    private long currentProgress = 0;    //当前下载进度
    private String packageName;

    public int getApkVersionConde() {
        return apkVersionConde;
    }

    public void setApkVersionConde(int apkVersionConde) {
        this.apkVersionConde = apkVersionConde;
    }

    public boolean isCaAutoInstall() {
        return caAutoInstall;
    }

    public void setCaAutoInstall(boolean caAutoInstall) {
        this.caAutoInstall = caAutoInstall;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public long getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(long currentProgress) {
        this.currentProgress = currentProgress;
    }

    public long getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(long maxLen) {
        this.maxLen = maxLen;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public boolean isDownloadComplete() {
        return isDownloadComplete;
    }

    public void setDownloadComplete(boolean downloadComplete) {
        isDownloadComplete = downloadComplete;
    }

    public DownloadEntity() {
    }

    @Override
    public DownloadEntity clone() throws CloneNotSupportedException {
        return (DownloadEntity) super.clone();
    }

    @Override
    public String toString() {
        return "DownloadEntity{" +
                "gameId=" + gameId +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", completeTime='" + completeTime + '\'' +
                ", size='" + size + '\'' +
                ", maxLen=" + maxLen +
                ", state=" + state +
                ", isDownloadComplete=" + isDownloadComplete +
                ", isInstalled=" + isInstalled +
                ", autoStart=" + autoStart +
                ", currentProgress=" + currentProgress +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.gameId);
        dest.writeString(this.name);
        dest.writeString(this.imgUrl);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.completeTime);
        dest.writeString(this.size);
        dest.writeLong(this.maxLen);
        dest.writeInt(this.apkVersionConde);
        dest.writeByte(caAutoInstall ? (byte) 1 : (byte) 0);
        dest.writeInt(this.state);
        dest.writeByte(isDownloadComplete ? (byte) 1 : (byte) 0);
        dest.writeByte(isInstalled ? (byte) 1 : (byte) 0);
        dest.writeByte(autoStart ? (byte) 1 : (byte) 0);
        dest.writeLong(this.currentProgress);
        dest.writeString(this.packageName);
    }

    protected DownloadEntity(Parcel in) {
        this.gameId = in.readInt();
        this.name = in.readString();
        this.imgUrl = in.readString();
        this.downloadUrl = in.readString();
        this.completeTime = in.readString();
        this.size = in.readString();
        this.maxLen = in.readLong();
        this.apkVersionConde = in.readInt();
        this.caAutoInstall = in.readByte() != 0;
        this.state = in.readInt();
        this.isDownloadComplete = in.readByte() != 0;
        this.isInstalled = in.readByte() != 0;
        this.autoStart = in.readByte() != 0;
        this.currentProgress = in.readLong();
        this.packageName = in.readString();
    }

    public static final Creator<DownloadEntity> CREATOR = new Creator<DownloadEntity>() {
        public DownloadEntity createFromParcel(Parcel source) {
            return new DownloadEntity(source);
        }

        public DownloadEntity[] newArray(int size) {
            return new DownloadEntity[size];
        }
    };
}

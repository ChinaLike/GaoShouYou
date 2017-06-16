package com.touchrom.gaoshouyou.entity.sql;

import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.os.Parcelable;

import com.touchrom.gaoshouyou.base.BaseSqlEntity;

/**
 * Created by lk on 2015/12/21.
 * APK信息
 */
public class ApkInfoEntity extends BaseSqlEntity implements Parcelable {
    private String apkPath;
    private String apkName;
    private String apkVersion;
    private String apkSize;
    private boolean apkInstallState;   //apk安装状态
    private String apkPackage;
    private PackageInfo packageInfo;

    private DownloadEntity downloadEntity;  //下载实体
    private ApkUpdateInfoEntity updateInfoEntity;    //app更新实体

    public ApkUpdateInfoEntity getUpdateInfoEntity() {
        return updateInfoEntity;
    }

    public void setUpdateInfoEntity(ApkUpdateInfoEntity updateInfoEntity) {
        this.updateInfoEntity = updateInfoEntity;
    }

    public DownloadEntity getDownloadEntity() {
        return downloadEntity;
    }

    public void setDownloadEntity(DownloadEntity downloadEntity) {
        this.downloadEntity = downloadEntity;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }


    public String getApkPackage() {
        return apkPackage;
    }

    public void setApkPackage(String apkPackage) {
        this.apkPackage = apkPackage;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getApkSize() {
        return apkSize;
    }

    public void setApkSize(String apkSize) {
        this.apkSize = apkSize;
    }

    public boolean isApkInstallState() {
        return apkInstallState;
    }

    public void setApkInstallState(boolean apkInstallState) {
        this.apkInstallState = apkInstallState;
    }

    public ApkInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.apkPath);
        dest.writeString(this.apkName);
        dest.writeString(this.apkVersion);
        dest.writeString(this.apkSize);
        dest.writeByte(apkInstallState ? (byte) 1 : (byte) 0);
        dest.writeString(this.apkPackage);
        dest.writeParcelable(this.packageInfo, 0);
        dest.writeParcelable(this.downloadEntity, 0);
        dest.writeParcelable(this.updateInfoEntity, 0);
    }

    protected ApkInfoEntity(Parcel in) {
        this.apkPath = in.readString();
        this.apkName = in.readString();
        this.apkVersion = in.readString();
        this.apkSize = in.readString();
        this.apkInstallState = in.readByte() != 0;
        this.apkPackage = in.readString();
        this.packageInfo = in.readParcelable(PackageInfo.class.getClassLoader());
        this.downloadEntity = in.readParcelable(DownloadEntity.class.getClassLoader());
        this.updateInfoEntity = in.readParcelable(ApkUpdateInfoEntity.class.getClassLoader());
    }

    public static final Creator<ApkInfoEntity> CREATOR = new Creator<ApkInfoEntity>() {
        public ApkInfoEntity createFromParcel(Parcel source) {
            return new ApkInfoEntity(source);
        }

        public ApkInfoEntity[] newArray(int size) {
            return new ApkInfoEntity[size];
        }
    };

    @Override
    public String toString() {
        return "ApkInfoEntity{" +
                "apkPath='" + apkPath + '\'' +
                ", apkName='" + apkName + '\'' +
                ", apkVersion='" + apkVersion + '\'' +
                ", apkSize='" + apkSize + '\'' +
                ", apkInstallState=" + apkInstallState +
                ", apkPackage='" + apkPackage + '\'' +
                ", packageInfo=" + packageInfo +
                ", downloadEntity=" + downloadEntity +
                ", updateInfoEntity=" + updateInfoEntity +
                '}';
    }
}

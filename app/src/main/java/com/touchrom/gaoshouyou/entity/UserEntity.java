package com.touchrom.gaoshouyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

import java.util.List;

/**
 * Created by lyy on 2015/11/6.
 * 用户实体
 */
public class UserEntity extends BaseEntity implements Parcelable {
    private int userId;
    /**
     * 用户类型
     */
    private int UserType;

    private String level;
    private String levelTag;
    private String nikeName;
    private String exp;
    private String integral;
    private String headImg;
    //注册的信息
    @SerializedName("result")
    private boolean isSuccess = false;
    @SerializedName("message")
    private String regMsg;
    private boolean hasNewMsg;
    //是否关注, flase ==> 没有关注
    @SerializedName("attention")
    private boolean isFollow = false;
    private String sign;    //签名
    private String sex;
    @SerializedName("location")
    private String location;    //地区
    @SerializedName("sex_o")
    private String sexDir;  //性取向
    @SerializedName("biaoqian")
    private String[] tags;  //标签

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLocation() {
        return location;
    }

    public String getSexDir() {
        return sexDir;
    }

    public String[] getTags() {
        return tags;
    }

    public String getSex() {
        return sex;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public String getSign() {
        return sign;
    }

    public boolean isHasNewMsg() {
        return hasNewMsg;
    }

    public void setHasNewMsg(boolean hasNewMsg) {
        this.hasNewMsg = hasNewMsg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getRegMsg() {
        return regMsg;
    }

    public String getHeadImg() {
        return headImg;
    }

    public int getUserId() {
        return userId;
    }

    public int getUserType() {
        return UserType;
    }

    public String getLevel() {
        return level;
    }

    public String getLevelTag() {
        return levelTag;
    }

    public String getNikeName() {
        return nikeName;
    }

    public String getExp() {
        return exp;
    }

    public String getIntegral() {
        return integral;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeInt(this.UserType);
        dest.writeString(this.level);
        dest.writeString(this.levelTag);
        dest.writeString(this.nikeName);
        dest.writeString(this.exp);
        dest.writeString(this.integral);
        dest.writeString(this.headImg);
        dest.writeByte(isSuccess ? (byte) 1 : (byte) 0);
        dest.writeString(this.regMsg);
    }

    public UserEntity() {
    }

    protected UserEntity(Parcel in) {
        this.userId = in.readInt();
        this.UserType = in.readInt();
        this.level = in.readString();
        this.levelTag = in.readString();
        this.nikeName = in.readString();
        this.exp = in.readString();
        this.integral = in.readString();
        this.headImg = in.readString();
        this.isSuccess = in.readByte() != 0;
        this.regMsg = in.readString();
    }

    public static final Parcelable.Creator<UserEntity> CREATOR = new Parcelable.Creator<UserEntity>() {
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };
}

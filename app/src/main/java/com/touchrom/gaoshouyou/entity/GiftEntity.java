package com.touchrom.gaoshouyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lk on 2016/3/8.
 * 礼包实体
 */
public class GiftEntity extends BaseEntity implements Parcelable {
    public static final int GRAB = 1;   //抢号
    public static final int AMOY = 2;   //淘号
    public static final int RESERVATIONS = 3;   //预定

    int giftId;
    int type;        //礼包类型
    int num;
    int totalNum;
    int linkId;
    int gameId;
    String giftName;
    String giftContent;
    String time;    //领取时间
    String condition;   //领取条件
    String useMethod;   //使用方法
    String imgUrl;
    String endTime;
    String gameName;
    @SerializedName("libaoma")
    String giftCode;    //礼包激活码
    @SerializedName("result")
    boolean isGrabed = false;  //抢号状态, ture ==> 未抢号
    List<TagEntity> linkList = new ArrayList<>();    //礼包关联列表

    public String getGiftCode() {
        return giftCode;
    }

    public boolean isGrabed() {
        return isGrabed;
    }

    public int getGameId() {
        return gameId;
    }

    public List<TagEntity> getLinkList() {
        return linkList;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getGameName() {
        return gameName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCondition() {
        return condition;
    }


    public String getUseMethod() {
        return useMethod;
    }


    public int getGiftId() {
        return giftId;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGiftName() {
        return giftName;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public String getGiftContent() {
        return giftContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.giftId);
        dest.writeInt(this.type);
        dest.writeString(this.giftName);
        dest.writeInt(this.num);
        dest.writeInt(this.totalNum);
        dest.writeString(this.giftContent);
        dest.writeString(this.time);
        dest.writeString(this.condition);
        dest.writeString(this.useMethod);
        dest.writeList(this.linkList);
        dest.writeInt(this.linkId);
        dest.writeString(this.imgUrl);
        dest.writeString(this.endTime);
        dest.writeString(this.gameName);
    }

    public GiftEntity() {
    }

    protected GiftEntity(Parcel in) {
        this.giftId = in.readInt();
        this.type = in.readInt();
        this.giftName = in.readString();
        this.num = in.readInt();
        this.totalNum = in.readInt();
        this.giftContent = in.readString();
        this.time = in.readString();
        this.condition = in.readString();
        this.useMethod = in.readString();
        this.linkList = new ArrayList<TagEntity>();
        in.readList(this.linkList, List.class.getClassLoader());
        this.linkId = in.readInt();
        this.imgUrl = in.readString();
        this.endTime = in.readString();
        this.gameName = in.readString();
    }

    public static final Parcelable.Creator<GiftEntity> CREATOR = new Parcelable.Creator<GiftEntity>() {
        public GiftEntity createFromParcel(Parcel source) {
            return new GiftEntity(source);
        }

        public GiftEntity[] newArray(int size) {
            return new GiftEntity[size];
        }
    };
}

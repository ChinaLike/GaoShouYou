package com.touchrom.gaoshouyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2015/12/7.
 * 推荐Banner的实体
 */
public class RecommendEntity extends BaseEntity implements Parcelable {
    /**
     * 类型
     * 1：系统浏览器；
     * 2：内页浏览器；
     * 3：内页游戏
     * 4：内页文章
     */
    int jump;
    String title;
    @SerializedName("name")
    String gameName;
    @SerializedName("description")
    String gameDetail;
    @SerializedName("imgUrl")
    String gameIconUrl;
    String url;
    int appId;

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDetail() {
        return gameDetail;
    }

    public void setGameDetail(String gameDetail) {
        this.gameDetail = gameDetail;
    }


    public String getGameIconUrl() {
        return gameIconUrl;
    }

    public void setGameIconUrl(String gameIconUrl) {
        this.gameIconUrl = gameIconUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RecommendEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.jump);
        dest.writeString(this.title);
        dest.writeString(this.gameName);
        dest.writeString(this.gameDetail);
        dest.writeString(this.gameIconUrl);
        dest.writeString(this.url);
        dest.writeInt(this.appId);
    }

    protected RecommendEntity(Parcel in) {
        this.jump = in.readInt();
        this.title = in.readString();
        this.gameName = in.readString();
        this.gameDetail = in.readString();
        this.gameIconUrl = in.readString();
        this.url = in.readString();
        this.appId = in.readInt();
    }

    public static final Creator<RecommendEntity> CREATOR = new Creator<RecommendEntity>() {
        public RecommendEntity createFromParcel(Parcel source) {
            return new RecommendEntity(source);
        }

        public RecommendEntity[] newArray(int size) {
            return new RecommendEntity[size];
        }
    };
}

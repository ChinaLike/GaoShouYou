package com.touchrom.gaoshouyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2015/12/8.
 * 游戏列表游戏信息实体
 */
public class GameInfoEntity extends BaseEntity implements Parcelable {
    public static final int GAME_INFO = 0;  //游戏信息详情
    public static final int GAME_IMG = 1;   //游戏横幅
    public static final int GAME_THEME_TITLE = 2;   //游戏列表标题文字
    public static final int GAME_RANK_INFO = 3;   //排行榜的
    public static final int GAME_COLLECT = 4;   //收藏的
    int appId;              //游戏Id
    @SerializedName("imgUrl")
    String iconUrl;
    String name;
    @SerializedName("rating")
    int score;              //分数
    String description;          //描述
    String size;            //游戏大小
    @SerializedName("lxName")
    String typeName;        //游戏类型
    String downNum;         //下载次数
    String introduction;    //简介
    String downloadUrl;
    @SerializedName("scId")
    int collectId;      //收藏Iduser/zhuangTai


    /**
     * 跳转链接
     */
    String url;
    /**
     * 跳转类型
     * 1：系统浏览器；
     * 2：内页浏览器；
     * 3：内页游戏
     * 4：内页文章
     */
    private int jump;
    /**
     * Adapter类型
     * {@link #GAME_INFO ...}
     */
    int type;
    @SerializedName("top")
    int rank;

    public int getCollectId() {
        return collectId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDownNum() {
        return downNum;
    }

    public void setDownNum(String downNum) {
        this.downNum = downNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GameInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.appId);
        dest.writeString(this.iconUrl);
        dest.writeString(this.name);
        dest.writeInt(this.score);
        dest.writeString(this.description);
        dest.writeString(this.size);
        dest.writeString(this.typeName);
        dest.writeString(this.downNum);
        dest.writeString(this.introduction);
        dest.writeString(this.downloadUrl);
        dest.writeInt(this.collectId);
        dest.writeString(this.url);
        dest.writeInt(this.jump);
        dest.writeInt(this.type);
        dest.writeInt(this.rank);
    }

    protected GameInfoEntity(Parcel in) {
        this.appId = in.readInt();
        this.iconUrl = in.readString();
        this.name = in.readString();
        this.score = in.readInt();
        this.description = in.readString();
        this.size = in.readString();
        this.typeName = in.readString();
        this.downNum = in.readString();
        this.introduction = in.readString();
        this.downloadUrl = in.readString();
        this.collectId = in.readInt();
        this.url = in.readString();
        this.jump = in.readInt();
        this.type = in.readInt();
        this.rank = in.readInt();
    }

    public static final Creator<GameInfoEntity> CREATOR = new Creator<GameInfoEntity>() {
        public GameInfoEntity createFromParcel(Parcel source) {
            return new GameInfoEntity(source);
        }

        public GameInfoEntity[] newArray(int size) {
            return new GameInfoEntity[size];
        }
    };
}

package com.touchrom.gaoshouyou.entity;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2016/1/11.
 * 游戏详情实体
 */
public class GameDetailEntity extends BaseEntity {
    int appId;
    @SerializedName("name")
    String gameName;
    String[] bgUrl;
    @SerializedName("imgUrl")
    String gameIconUrl;
    @SerializedName("lxName")
    String gameType;
    @SerializedName("size")
    String gameSize;
    @SerializedName("rating")
    int score;
    String tags;
    String downloadUrl;
    @SerializedName("imgType")
    boolean imgVertical = false;

    public boolean isImgVertical() {
        return imgVertical;
    }

    public void setImgVertical(boolean imgVertical) {
        this.imgVertical = imgVertical;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String[] getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String[] bgUrl) {
        this.bgUrl = bgUrl;
    }

    public String getGameIconUrl() {
        return gameIconUrl;
    }

    public void setGameIconUrl(String gameIconUrl) {
        this.gameIconUrl = gameIconUrl;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameSize() {
        return gameSize;
    }

    public void setGameSize(String gameSize) {
        this.gameSize = gameSize;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}

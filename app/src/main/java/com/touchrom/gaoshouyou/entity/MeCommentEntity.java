package com.touchrom.gaoshouyou.entity;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

public class MeCommentEntity extends BaseEntity {
    @SerializedName("name")
    String title;
    @SerializedName("comment")
    String content;
    String time;
    @SerializedName("agreeNum")
    int likeNum;
    int replyNum;
    int typeId;
    int appId;

    public int getTypeId() {
        return typeId;
    }

    public int getAppId() {
        return appId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public int getReplyNum() {
        return replyNum;
    }
}
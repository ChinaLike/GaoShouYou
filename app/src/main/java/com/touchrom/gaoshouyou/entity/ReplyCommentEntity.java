package com.touchrom.gaoshouyou.entity;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

public class ReplyCommentEntity extends BaseEntity {
    @SerializedName("nickName")
    String nikeName;
    String time;
    @SerializedName("myComment")
    String content;
    @SerializedName("reComment")
    String replyContent;
    @SerializedName("headImg")
    String imgUrl;
    @SerializedName("userId")
    int replyerId;
    int cmtId;

    public String getImgUrl() {
        return imgUrl;
    }

    public String getNikeName() {
        return nikeName;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public int getReplyerId() {
        return replyerId;
    }

    public int getCmtId() {
        return cmtId;
    }
}
package com.touchrom.gaoshouyou.entity;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2016/4/7.
 * 专题简介实体
 */
public class TopicInfoEntity extends BaseEntity {
    int topicId;
    String imgUrl;
    @SerializedName("contentTitle")
    String topicTitle;
    @SerializedName("articleId")
    int artId;

    public int getArtId() {
        return artId;
    }

    public int getTopicId() {
        return topicId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTopicTitle() {
        return topicTitle;
    }
}

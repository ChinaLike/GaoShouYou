package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lyy on 2016/3/16.
 * 粉丝实体
 */
public class FansEntity extends BaseEntity {
    String imgUrl;
    String nikeName;
    String sign;
    int fansId;         //粉丝Id
    boolean attention;  //关注状态 true已关注,false未关注

    public void setAttention(boolean attention) {
        this.attention = attention;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getNikeName() {
        return nikeName;
    }

    public String getSign() {
        return sign;
    }

    public int getFansId() {
        return fansId;
    }

    public boolean isAttention() {
        return attention;
    }
}

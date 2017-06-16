package com.touchrom.gaoshouyou.entity;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2016/3/8.
 * 攻略实体
 */
public class RaiderEntity extends BaseEntity {
    @SerializedName("artId")
    int raidersId;
    String title;
    @SerializedName("description")
    String content;

    /**
     * 1、攻略
     * 2、活动
     * 3、评测
     * 4、资讯
     * 5、新闻
     * 6、专题
     * 7、资料
     * 8、问答
     */
    int typeId;
    String artType;
    String artName;
    @SerializedName("scId")
    int collectId;      //收藏Iduser/zhuangTai

    public int getCollectId() {
        return collectId;
    }

    public String getArtName() {
        return artName;
    }

    public String getArtType() {
        return artType;
    }

    public void setArtType(String artType) {
        this.artType = artType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getRaidersId() {
        return raidersId;
    }

    public void setRaidersId(int raidersId) {
        this.raidersId = raidersId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

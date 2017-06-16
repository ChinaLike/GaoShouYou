package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

import java.util.List;

/**
 * Created by lk on 2016/3/3.
 * 游戏厂商实体
 */
public class GameFactoryEntity extends BaseEntity {
    String imgUrl;
    String name;
    String content;
    List<GameInfoEntity> games;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<GameInfoEntity> getGames() {
        return games;
    }

    public void setGames(List<GameInfoEntity> games) {
        this.games = games;
    }
}

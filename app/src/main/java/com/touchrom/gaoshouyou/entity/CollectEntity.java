package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lk on 2016/3/15.
 * 收藏实体
 */
public class CollectEntity extends BaseEntity {
    List<GameInfoEntity> game = new ArrayList<>();
    List<RaiderEntity> article = new ArrayList<>();

    public List<GameInfoEntity> getGame() {
        return game;
    }

    public List<RaiderEntity> getArticle() {
        return article;
    }
}

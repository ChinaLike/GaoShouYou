package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyy on 2016/4/7.
 */
public class TopicDetailEntity extends ArticleEntity {
    List<GameInfoEntity> games = new ArrayList<>();
    List<BarEntity> bars = new ArrayList<>();
    String description;

    public String getDescription() {
        return description;
    }

    public List<GameInfoEntity> getGames() {
        return games;
    }

    public List<BarEntity> getBars() {
        return bars;
    }

    public class BarEntity extends BaseEntity {
        String artType;
        String artTitle;
        int typeId;
        int artId;

        public String getArtType() {
            return artType;
        }

        public String getArtTitle() {
            return artTitle;
        }

        public int getTypeId() {
            return typeId;
        }

        public int getArtId() {
            return artId;
        }
    }
}

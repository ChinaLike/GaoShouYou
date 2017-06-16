package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lk on 2015/12/11.
 * 游戏分类筛选悬浮框实体
 */
public class GameClassifyScreenEntity extends BaseEntity {
    String title;
    List<ScreenTagChildEntity> childs = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ScreenTagChildEntity> getChilds() {
        return childs;
    }

    public void setChilds(List<ScreenTagChildEntity> childs) {
        this.childs = childs;
    }

    public static class ScreenTagChildEntity extends BaseEntity {
        int tagId;
        String tagName;

        public int getTagId() {
            return tagId;
        }

        public void setTagId(int tagId) {
            this.tagId = tagId;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }
    }
}

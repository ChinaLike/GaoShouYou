package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyy on 2016/3/22.
 * 高手称号实体
 */
public class HonorTagEntity extends BaseEntity {
    String currentTag;
    List<String> title = new ArrayList<>();
    List<String> grade = new ArrayList<>();

    public String getCurrentTag() {
        return currentTag;
    }

    public List<String> getTitle() {
        return title;
    }

    public List<String> getGrade() {
        return grade;
    }
}

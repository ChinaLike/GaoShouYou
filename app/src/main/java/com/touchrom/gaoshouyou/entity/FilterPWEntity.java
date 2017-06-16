package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2016/1/25.
 * 筛选悬浮框记录实体
 */
public class FilterPWEntity extends BaseEntity {
    private int classifyId;
    private int filterId1 = 0, filterId2 = 0, filterId3 = 0;
    private int position1 = 0, position2 = 0, position3 = 0;

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public int getFilterId1() {
        return filterId1;
    }

    public void setFilterId1(int filterId1) {
        this.filterId1 = filterId1;
    }

    public int getFilterId2() {
        return filterId2;
    }

    public void setFilterId2(int filterId2) {
        this.filterId2 = filterId2;
    }

    public int getFilterId3() {
        return filterId3;
    }

    public void setFilterId3(int filterId3) {
        this.filterId3 = filterId3;
    }

    public int getPosition1() {
        return position1;
    }

    public void setPosition1(int position1) {
        this.position1 = position1;
    }

    public int getPosition2() {
        return position2;
    }

    public void setPosition2(int position2) {
        this.position2 = position2;
    }

    public int getPosition3() {
        return position3;
    }

    public void setPosition3(int position3) {
        this.position3 = position3;
    }
}

package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyy on 2016/3/21.
 * 荣誉实体
 */
public class HonorEntity extends BaseEntity {
    int currentLev;     //当前等级
    int needExp;        //需要的经验
    int total;          //经验总计
    List<LevExpEntity> exps = new ArrayList<>();

    public int getTotal() {
        return total;
    }

    public int getCurrentLev() {
        return currentLev;
    }

    public int getNeedExp() {
        return needExp;
    }

    public List<LevExpEntity> getExps() {
        return exps;
    }

    public static class LevExpEntity extends BaseEntity {
        int exp;
        String time;
        String typeName;

        public String getTypeName() {
            return typeName;
        }

        public int getExp() {
            return exp;
        }

        public String getTime() {
            return time;
        }
    }


}

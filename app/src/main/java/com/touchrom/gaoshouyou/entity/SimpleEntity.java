package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2016/2/26.
 * 简单的实体
 */
public class SimpleEntity extends BaseEntity {
    String arg1, arg2;
    Object object;

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }
}

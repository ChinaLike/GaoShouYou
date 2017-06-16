package com.touchrom.gaoshouyou.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by lk on 2015/12/3.
 * 简单的Adapter实体
 */
public class SimpleAdapterEntity {
    String msg;
    String info;
    int arg;
    Drawable img;
    boolean b;

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getArg() {
        return arg;
    }

    public void setArg(int arg) {
        this.arg = arg;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

}

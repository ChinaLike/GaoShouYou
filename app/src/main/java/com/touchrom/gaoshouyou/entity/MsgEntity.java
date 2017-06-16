package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2016/3/21.
 * 消息实体
 */
public class MsgEntity extends BaseEntity {
    String title;
    String content;
    String time;
    int msgId;
    int appId;
    int msgType;
    int num;

    public String getTime() {
        return time;
    }

    public int getNum() {
        return num;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getMsgId() {
        return msgId;
    }

    public int getAppId() {
        return appId;
    }

    public int getMsgType() {
        return msgType;
    }
}

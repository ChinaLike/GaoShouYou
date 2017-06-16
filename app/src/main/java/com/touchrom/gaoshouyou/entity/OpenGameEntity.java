package com.touchrom.gaoshouyou.entity;


/**
 * Created by lk on 2016/3/22.
 * 开服游戏实体
 */
public class OpenGameEntity extends GameInfoEntity {
    public static final int OPEN_SERVER = 1;    //开服
    public static final int OPEN_TEST = 2;      //开测
    String gameName;
    String openType;        //开服类型
    String gameType;        //游戏类型
    String time;            //时间

    public String getGameType() {
        return gameType;
    }


    public String getGameName() {
        return gameName;
    }

    public String getOpenType() {
        return openType;
    }

    public String getTime() {
        return time;
    }
}

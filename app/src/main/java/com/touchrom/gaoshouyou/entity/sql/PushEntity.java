package com.touchrom.gaoshouyou.entity.sql;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseSqlEntity;

/**
 * Created by lk on 2016/1/29.
 * 推送实体
 */
public class PushEntity extends BaseSqlEntity implements Parcelable {
    public static final int GAME = 0;  //游戏
    public static final int BROWSER = 1;   //内置浏览器
    public static final int SYSTEM_BROWSER = 2;   //系统浏览器
    public static final int ARTICLE = 3;   //文章

    private String title;   //标题
    private String msg;     //内容
    private String url;     //跳转链接
    private String imgUrl;  //图片链接
    @SerializedName("msgType")
    private int type;    //跳转类型
    private int id;      //文章，游戏id
    private String msgId;   //消息id
    private int arcType;    //文章类型

    public int getArcType() {
        return arcType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PushEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.msg);
        dest.writeString(this.url);
        dest.writeString(this.imgUrl);
        dest.writeInt(this.type);
        dest.writeInt(this.id);
        dest.writeString(this.msgId);
    }

    protected PushEntity(Parcel in) {
        this.title = in.readString();
        this.msg = in.readString();
        this.url = in.readString();
        this.imgUrl = in.readString();
        this.type = in.readInt();
        this.id = in.readInt();
        this.msgId = in.readString();
    }

    public static final Creator<PushEntity> CREATOR = new Creator<PushEntity>() {
        public PushEntity createFromParcel(Parcel source) {
            return new PushEntity(source);
        }

        public PushEntity[] newArray(int size) {
            return new PushEntity[size];
        }
    };
}

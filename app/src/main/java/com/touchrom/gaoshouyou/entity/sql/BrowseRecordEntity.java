package com.touchrom.gaoshouyou.entity.sql;

import android.os.Parcel;
import android.os.Parcelable;

import com.touchrom.gaoshouyou.base.BaseSqlEntity;

/**
 * Created by lk on 2016/2/17.
 * 浏览实体
 */
public class BrowseRecordEntity extends BaseSqlEntity implements Parcelable {
    public static final int GAME = 0x01;    //游戏
    public static final int ARTICLE = 0x02; //文章
    private int type = 0;
    private int objId;
    private long time;
    private String title;
    private String msg;
    private String icon;
    private String url;

    public int getObjId() {
        return objId;
    }

    public void setObjId(int objId) {
        this.objId = objId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BrowseRecordEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.objId);
        dest.writeLong(this.time);
        dest.writeString(this.title);
        dest.writeString(this.msg);
        dest.writeString(this.icon);
        dest.writeString(this.url);
    }

    protected BrowseRecordEntity(Parcel in) {
        this.type = in.readInt();
        this.objId = in.readInt();
        this.time = in.readLong();
        this.title = in.readString();
        this.msg = in.readString();
        this.icon = in.readString();
        this.url = in.readString();
    }

    public static final Creator<BrowseRecordEntity> CREATOR = new Creator<BrowseRecordEntity>() {
        public BrowseRecordEntity createFromParcel(Parcel source) {
            return new BrowseRecordEntity(source);
        }

        public BrowseRecordEntity[] newArray(int size) {
            return new BrowseRecordEntity[size];
        }
    };
}

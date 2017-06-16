package com.touchrom.gaoshouyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2015/11/6.
 * Web实体
 */
public class WebEntity extends BaseEntity implements Parcelable {
    private int id;
    private String msg;
    private String title;
    private String contentUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }


    public WebEntity() {
    }

    public WebEntity(String contentUrl, String title) {
        this.contentUrl = contentUrl;
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.msg);
        dest.writeString(this.title);
        dest.writeString(this.contentUrl);
    }

    protected WebEntity(Parcel in) {
        this.id = in.readInt();
        this.msg = in.readString();
        this.title = in.readString();
        this.contentUrl = in.readString();
    }

    public static final Creator<WebEntity> CREATOR = new Creator<WebEntity>() {
        public WebEntity createFromParcel(Parcel source) {
            return new WebEntity(source);
        }

        public WebEntity[] newArray(int size) {
            return new WebEntity[size];
        }
    };
}

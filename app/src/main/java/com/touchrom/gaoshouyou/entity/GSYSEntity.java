package com.touchrom.gaoshouyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lyy on 2016/1/14.
 * 高手游服务实体
 */
public class GSYSEntity extends BaseEntity implements Parcelable {
    String data;
    /**
     * {@link com.touchrom.gaoshouyou.service.GaoShouYouService#ACTION_UNINSTALL}
     */
    String action;

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public GSYSEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.data);
        dest.writeString(this.action);
        dest.writeString(this.name);
    }

    protected GSYSEntity(Parcel in) {
        this.data = in.readString();
        this.action = in.readString();
        this.name = in.readString();
    }

    public static final Creator<GSYSEntity> CREATOR = new Creator<GSYSEntity>() {
        public GSYSEntity createFromParcel(Parcel source) {
            return new GSYSEntity(source);
        }

        public GSYSEntity[] newArray(int size) {
            return new GSYSEntity[size];
        }
    };
}

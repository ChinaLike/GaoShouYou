package com.touchrom.gaoshouyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MsgSettingEntity implements Parcelable {

    private Element update = new Element("更新", true);
    private Element system = new Element("系统", true);
    private Element notice = new Element("公告", true);
    private Element follow = new Element("关注", true);
    private Element reply = new Element("回复", true);
    private Element message = new Element("留言", true);
    private Element gift = new Element("礼包预定", true);

    public Element getUpdate() {
        return update;
    }

    public void setUpdate(Element update) {
        this.update = update;
    }

    public Element getSystem() {
        return system;
    }

    public void setSystem(Element system) {
        this.system = system;
    }

    public Element getNotice() {
        return notice;
    }

    public void setNotice(Element notice) {
        this.notice = notice;
    }

    public Element getFollow() {
        return follow;
    }

    public void setFollow(Element follow) {
        this.follow = follow;
    }

    public Element getReply() {
        return reply;
    }

    public void setReply(Element reply) {
        this.reply = reply;
    }

    public Element getMessage() {
        return message;
    }

    public void setMessage(Element message) {
        this.message = message;
    }

    public Element getGift() {
        return gift;
    }

    public void setGift(Element gift) {
        this.gift = gift;
    }

    public static class Element implements Parcelable {
        public Element(String title, boolean open) {
            this.title = title;
            this.open = open;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "name='" + title + '\'' +
                    ", open=" + open +
                    '}';
        }

        String title = "";
        boolean open = true;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeByte(open ? (byte) 1 : (byte) 0);
        }

        public Element(Parcel in) {
            this.title = in.readString();
            this.open = in.readByte() != 0;
        }

        public static final Parcelable.Creator<Element> CREATOR = new Parcelable.Creator<Element>() {
            public Element createFromParcel(Parcel source) {
                return new Element(source);
            }

            public Element[] newArray(int size) {
                return new Element[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.update, 0);
        dest.writeParcelable(this.system, 0);
        dest.writeParcelable(this.notice, 0);
        dest.writeParcelable(this.follow, 0);
        dest.writeParcelable(this.reply, 0);
        dest.writeParcelable(this.message, 0);
        dest.writeParcelable(this.gift, 0);
    }

    public MsgSettingEntity() {
    }

    protected MsgSettingEntity(Parcel in) {
        this.update = in.readParcelable(Element.class.getClassLoader());
        this.system = in.readParcelable(Element.class.getClassLoader());
        this.notice = in.readParcelable(Element.class.getClassLoader());
        this.follow = in.readParcelable(Element.class.getClassLoader());
        this.reply = in.readParcelable(Element.class.getClassLoader());
        this.message = in.readParcelable(Element.class.getClassLoader());
        this.gift = in.readParcelable(Element.class.getClassLoader());
    }

    public static final Parcelable.Creator<MsgSettingEntity> CREATOR = new Parcelable.Creator<MsgSettingEntity>() {
        public MsgSettingEntity createFromParcel(Parcel source) {
            return new MsgSettingEntity(source);
        }

        public MsgSettingEntity[] newArray(int size) {
            return new MsgSettingEntity[size];
        }
    };
}
package com.touchrom.gaoshouyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lk on 2015/11/6.
 * 分享实体
 */
public class ShareEntity extends BaseEntity implements Parcelable {
    ParamsEntity weixin;
    ParamsEntity qq;
    ParamsEntity weibo;
    ParamsEntity weixinMoment;
    ParamsEntity qZone;
    ParamsEntity system;

    public ParamsEntity getSystem() {
        return system;
    }

    public void setSystem(ParamsEntity system) {
        this.system = system;
    }

    public ParamsEntity getWeixinMoment() {
        return weixinMoment;
    }

    public void setWeixinMoment(ParamsEntity weixinMoment) {
        this.weixinMoment = weixinMoment;
    }

    public ParamsEntity getqZone() {
        return qZone;
    }

    public void setqZone(ParamsEntity qZone) {
        this.qZone = qZone;
    }

    public ParamsEntity getWeixin() {
        return weixin;
    }

    public void setWeixin(ParamsEntity weixin) {
        this.weixin = weixin;
    }

    public ParamsEntity getQq() {
        return qq;
    }

    public void setQq(ParamsEntity qq) {
        this.qq = qq;
    }

    public ParamsEntity getWeibo() {
        return weibo;
    }

    public void setWeibo(ParamsEntity weibo) {
        this.weibo = weibo;
    }

    /**
     * http://wiki.mob.com/%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E/
     */
    public static class ParamsEntity implements Parcelable {
        String title = "";       //标题，微信、QQ（新浪微博不需要标题）
        String titleUrl = "";    //标题链接，QQ和QQ空间的
        String text = "";        //分享文本
        String imgUrl;      //图片链接
        String site;        //发布分享的网站名称，QQ空间的
        String siteUrl;     //发布分享网站的地址，QQ空间的
        String imgPath;     //本地图片
        String url = "";         // url：仅在微信（包括好友和朋友圈）中使用，跳转链接

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleUrl() {
            return titleUrl;
        }

        public void setTitleUrl(String titleUrl) {
            this.titleUrl = titleUrl;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getSiteUrl() {
            return siteUrl;
        }

        public void setSiteUrl(String siteUrl) {
            this.siteUrl = siteUrl;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public ParamsEntity() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.titleUrl);
            dest.writeString(this.text);
            dest.writeString(this.imgUrl);
            dest.writeString(this.site);
            dest.writeString(this.siteUrl);
            dest.writeString(this.imgPath);
            dest.writeString(this.url);
        }

        protected ParamsEntity(Parcel in) {
            this.title = in.readString();
            this.titleUrl = in.readString();
            this.text = in.readString();
            this.imgUrl = in.readString();
            this.site = in.readString();
            this.siteUrl = in.readString();
            this.imgPath = in.readString();
            this.url = in.readString();
        }

        public static final Creator<ParamsEntity> CREATOR = new Creator<ParamsEntity>() {
            public ParamsEntity createFromParcel(Parcel source) {
                return new ParamsEntity(source);
            }

            public ParamsEntity[] newArray(int size) {
                return new ParamsEntity[size];
            }
        };
    }

    public ShareEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.weixin, 0);
        dest.writeParcelable(this.qq, 0);
        dest.writeParcelable(this.weibo, 0);
        dest.writeParcelable(this.weixinMoment, 0);
        dest.writeParcelable(this.qZone, 0);
        dest.writeParcelable(this.system, 0);
    }

    protected ShareEntity(Parcel in) {
        this.weixin = in.readParcelable(ParamsEntity.class.getClassLoader());
        this.qq = in.readParcelable(ParamsEntity.class.getClassLoader());
        this.weibo = in.readParcelable(ParamsEntity.class.getClassLoader());
        this.weixinMoment = in.readParcelable(ParamsEntity.class.getClassLoader());
        this.qZone = in.readParcelable(ParamsEntity.class.getClassLoader());
        this.system = in.readParcelable(ParamsEntity.class.getClassLoader());
    }

    public static final Creator<ShareEntity> CREATOR = new Creator<ShareEntity>() {
        public ShareEntity createFromParcel(Parcel source) {
            return new ShareEntity(source);
        }

        public ShareEntity[] newArray(int size) {
            return new ShareEntity[size];
        }
    };
}

package com.touchrom.gaoshouyou.util;

import android.content.Context;
import android.content.Intent;

import com.arialyy.frame.util.show.L;
import com.touchrom.gaoshouyou.entity.ShareEntity;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by lyy on 2015/12/3.
 * 分享工具
 */
public class ShareUtil {
    private static final String TAG = "ShareUtil";

    public static volatile ShareUtil mUtil;
    private static final Object LOCK = new Object();

    private ShareUtil() {
    }

    public static ShareUtil getInstance() {
        if (mUtil == null) {
            synchronized (LOCK) {
                mUtil = new ShareUtil();
            }
        }
        return mUtil;
    }

    /**
     * android程序间的分享
     */
    public void shareToSystem(Context context, String str) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "分享到："));
    }

    /**
     * 分享带新浪微博
     *
     * @param weibo
     */
    public void shareToWeibo(ShareEntity.ParamsEntity weibo, PlatformActionListener listener) {
        L.d(TAG, "shareWeibo");
        SinaWeibo.ShareParams shareParams = new SinaWeibo.ShareParams();
        shareParams.setText(weibo.getText() + weibo.getUrl());
//        shareParams.setImageUrl(weibo.getImgUrl()); //这玩意需要应用通过审核并且需要开通新浪的一个高级分享接口
        shareParams.setImagePath(weibo.getImgPath());
        Platform p = ShareSDK.getPlatform(SinaWeibo.NAME);
        p.SSOSetting(false);
        p.setPlatformActionListener(listener);
        p.share(shareParams);
    }

    /**
     * 分享到QQ空间
     *
     * @param qZone
     */
    public void shareToQzone(ShareEntity.ParamsEntity qZone, PlatformActionListener listener) {
        QZone.ShareParams shareParams = new QZone.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(qZone.getTitle());
        shareParams.setTitleUrl(qZone.getTitleUrl());
        shareParams.setText(qZone.getText());
//        shareParams.setImageUrl(qq.getImgUrl());  //貌似这玩意也要审核通过才行
        shareParams.setSite(qZone.getSite());
        shareParams.setImagePath(qZone.getImgPath());
        Platform p = ShareSDK.getPlatform(QZone.NAME);
        p.SSOSetting(false);
        p.setPlatformActionListener(listener);
        p.share(shareParams);
    }

    /**
     * 分享到QQ
     *
     * @param qq
     */
    public void shareToQQ(ShareEntity.ParamsEntity qq, PlatformActionListener listener) {
        QQ.ShareParams shareParams = new QQ.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(qq.getTitle());
        shareParams.setTitleUrl(qq.getTitleUrl());
        shareParams.setText(qq.getText());
//        shareParams.setImageUrl(qq.getImgUrl());  //貌似这玩意也要审核通过才行
        shareParams.setImagePath(qq.getImgPath());
        Platform p = ShareSDK.getPlatform(QQ.NAME);
        p.SSOSetting(false);
        p.setPlatformActionListener(listener);
        p.share(shareParams);
    }

    /**
     * 分享到微信
     *
     * @param weixin
     */
    public void shareToWeixin(ShareEntity.ParamsEntity weixin, PlatformActionListener listener) {
//        List md5 = AppSigning.getSingInfo(context, AndroidUtils.getPackageName(context), AppSigning.MD5);
        WechatMoments.ShareParams shareParams = new WechatMoments.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(weixin.getTitle());
        shareParams.setText(weixin.getText());
        shareParams.setUrl(weixin.getUrl());            //跳转链接
        shareParams.setImageUrl(weixin.getImgUrl());
        Platform p = ShareSDK.getPlatform(Wechat.NAME);
        p.SSOSetting(false);
        p.setPlatformActionListener(listener);
        p.share(shareParams);
    }

    /**
     * 分享到微信朋友圈
     *
     * @param weixinMonent
     */
    public void shareToWeixinMoment(ShareEntity.ParamsEntity weixinMonent, PlatformActionListener listener) {
//        List md5 = AppSigning.getSingInfo(context, AndroidUtils.getPackageName(context), AppSigning.MD5);
        WechatMoments.ShareParams shareParams = new WechatMoments.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(weixinMonent.getTitle());
        shareParams.setText(weixinMonent.getText());
        shareParams.setUrl(weixinMonent.getUrl());            //跳转链接
        shareParams.setImageUrl(weixinMonent.getImgUrl());
        Platform p = ShareSDK.getPlatform(WechatMoments.NAME);
        p.SSOSetting(false);
        p.setPlatformActionListener(listener);
        p.share(shareParams);
    }
}

package com.touchrom.gaoshouyou.util;

import android.content.Context;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by lk on 2016/2/1.
 * 第三方登录工具
 */
public class LoginUtil {
    private static final String TAG = "LoginUtil";

    public static volatile LoginUtil mUtil;
    private static final Object LOCK = new Object();

    private LoginUtil() {
    }

    public static LoginUtil getInstance() {
        if (mUtil == null) {
            synchronized (LOCK) {
                mUtil = new LoginUtil();
            }
        }
        return mUtil;
    }

    /**
     * 登录
     *
     * @param type QQ.NAME、Wechat.NAME、SinaWeibo.NAME
     */
    public void login(Context context, String type, PlatformActionListener listener) {
        Platform p = ShareSDK.getPlatform(context, type);
        p.setPlatformActionListener(listener);
        p.SSOSetting(false);
        p.showUser(null);
    }

    /**
     * 退出登录
     *
     * @param type QQ.NAME、Wechat.NAME、SinaWeibo.NAME
     */
    public void logout(Context context, String type) {
        Platform p = ShareSDK.getPlatform(context, type);
        if (p.isValid()) {
            p.removeAccount();
        }
    }

}

package com.touchrom.gaoshouyou.activity.web;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.touchrom.gaoshouyou.base.AppManager;

/**
 * JS统一回调接口
 */
public class WebAppInterface {
    private Context mContext;

    public WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public int getUserId(){
        return AppManager.getInstance().getUser().getUserId();
    }
}
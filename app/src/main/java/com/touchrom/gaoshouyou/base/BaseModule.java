package com.touchrom.gaoshouyou.base;

import android.content.Context;

import com.arialyy.frame.module.AbsModule;
import com.touchrom.gaoshouyou.net.ServiceUtil;

/**
 * Created by lyy on 2015/11/11.
 * 基本模型层
 */
public class BaseModule extends AbsModule {
    protected ServiceUtil mServiceUtil;


    public BaseModule(Context context) {
        super(context);
        mServiceUtil = ServiceUtil.getInstance(context);
    }
}

package com.touchrom.gaoshouyou.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.arialyy.frame.util.NetUtils;
import com.arialyy.frame.util.show.L;
import com.touchrom.gaoshouyou.base.AppManager;

/**
 * Created by lk on 2015/11/9.
 * 网络状态监听
 */
public class NetStateService extends IntentService {
    private static final String TAG = "NetStateService";
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetInfo;
    private AppManager mManager = AppManager.getInstance();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                L.d(TAG, "网络状态已经改变");
                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                mNetInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetInfo != null && mNetInfo.isAvailable()) {
                    if (!NetUtils.isWifi(context)) {

                    }

                    String name = mNetInfo.getTypeName();
                    L.d(TAG, "当前网络名称：" + name);
                    //doSomething()
                } else {
                    L.d(TAG, "没有可用网络");
                    //doSomething()
                }
            }
        }
    };

    public NetStateService() {
        super("NetStateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}

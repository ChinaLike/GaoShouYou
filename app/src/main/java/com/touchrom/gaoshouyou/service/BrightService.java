package com.touchrom.gaoshouyou.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;

/**
 * Created by lyy on 2015/11/11.
 * 亮度服务
 */
public class BrightService extends IntentService {

    private Context mContext;

    private ContentObserver mBrightnessObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            int status = getBrightnessMode(0);
            if (status == 0) {
                //set brightness by yourself
            } else {
                //automation set brightnessMode
            }
        }

        private int getBrightnessMode(int defaultValue) {
            int brightnessMode = defaultValue;
            try {
                brightnessMode = Settings.System.getInt(mContext.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return brightnessMode;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mContext.getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true,
                mBrightnessObserver);
    }

    public BrightService(Context context) {
        super("BrightService");
        mContext = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.getContentResolver().unregisterContentObserver(mBrightnessObserver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}

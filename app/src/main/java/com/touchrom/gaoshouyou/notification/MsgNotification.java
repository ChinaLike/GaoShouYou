package com.touchrom.gaoshouyou.notification;

import android.app.Activity;
import android.support.v4.app.NotificationCompat;

import com.touchrom.gaoshouyou.entity.NotificationEntity;

/**
 * Created by lyy on 2015/12/4.
 * 普通消息通知
 */
public class MsgNotification {
    public NotificationCompat.Builder create(Activity activity, NotificationEntity entity) {
        return new NotificationCompat.Builder(activity)
                .setSmallIcon(entity.getIcon())
                .setContentTitle(entity.getTitle())
                .setContentText(entity.getMsg())
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);
    }
}

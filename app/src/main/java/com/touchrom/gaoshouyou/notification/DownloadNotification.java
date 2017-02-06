package com.touchrom.gaoshouyou.notification;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.bumptech.glide.Glide;
import com.touchrom.gaoshouyou.activity.InstallTempActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.NotificationEntity;

/**
 * Created by lyy on 2015/11/13.
 * 下载通知栏
 */
public class DownloadNotification {

    public NotificationCompat.Builder create(Activity activity, NotificationEntity entity) {
        return new NotificationCompat.Builder(activity)
                .setSmallIcon(entity.getIcon())
                .setContentTitle(entity.getTitle())
                .setProgress(100, 0, false)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);
    }

    public static PendingIntent createIntent(Activity activity) {
        Intent intent = new Intent(activity, InstallTempActivity.class);
        intent.putExtra(Constance.KEY.STRING, Constance.PATH.NEW_APK_PATH);
        TaskStackBuilder stack = TaskStackBuilder.create(activity);
        stack.addNextIntent(intent);
        return stack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

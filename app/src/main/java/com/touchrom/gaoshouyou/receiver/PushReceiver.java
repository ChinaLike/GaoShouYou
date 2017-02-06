package com.touchrom.gaoshouyou.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.arialyy.frame.core.NotifyHelp;
import com.arialyy.frame.util.show.L;
import com.google.gson.Gson;
import com.igexin.sdk.PushConsts;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.AppManagerActivity;
import com.touchrom.gaoshouyou.activity.ArticleActivity;
import com.touchrom.gaoshouyou.activity.GameDetailActivity;
import com.touchrom.gaoshouyou.activity.user.FollowActivity;
import com.touchrom.gaoshouyou.activity.user.GiftManagerActivity;
import com.touchrom.gaoshouyou.activity.user.HonorActivity;
import com.touchrom.gaoshouyou.activity.user.UserCenterActivity;
import com.touchrom.gaoshouyou.activity.user.UserCommentActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.entity.sql.PushEntity;

/**
 * Created by lyy on 2016/1/29.
 * 接收推送的数据
 */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        switch (b.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:   //透传消息
                byte[] payload = b.getByteArray("payload");
                if (payload == null) {
                    return;
                }
                String msg = new String(payload);
                String msgId = b.getString("messageid");
                savePushEntity(context, msgId, msg);
                NotifyHelp.getInstance().update(Constance.NOTIFY_KEY.USER_FRAGMENT);
                break;
            case PushConsts.GET_CLIENTID:
                break;
        }

    }

    private void showNotification(Context context, PushEntity entity) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(entity.getTitle())
                .setContentText(entity.getMsg())
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);
        PendingIntent intent = createPendingIntent(context, entity);
        if (intent != null) {
            builder.setContentIntent(intent);
        }
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());
    }

    private PendingIntent createPendingIntent(Context context, PushEntity entity) {
        TaskStackBuilder stack = TaskStackBuilder.create(context);
        Intent intent = createIntent(context, entity);
        if (intent == null) {
            return null;
        }
        stack.addNextIntent(intent);
        return stack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent createIntent(Context context, PushEntity entity) {
        Intent intent = null;
        switch (entity.getType()) {
            case 13:    //游戏更新
                intent = new Intent(context, AppManagerActivity.class);
                intent.putExtra(Constance.KEY.TURN, 1);
                break;
            case 6:     //用户升级
                intent = new Intent(context, HonorActivity.class);
                intent.putExtra(Constance.KEY.TURN, 1);
                break;
            case 4:     //关注
                intent = new Intent(context, FollowActivity.class);
                intent.putExtra(Constance.KEY.TURN, 2);
                break;
            case 2:     //回复
                intent = new Intent(context, UserCommentActivity.class);
                intent.putExtra(Constance.KEY.TURN, 1);
                break;
            case 3:     //留言
                intent = new Intent(context, UserCenterActivity.class);
                intent.putExtra(Constance.KEY.TURN, 3);
                break;
            case 14:    //礼包预定
                intent = new Intent(context, GiftManagerActivity.class);
                intent.putExtra(Constance.KEY.TURN, 1);
                break;
            case 15:
                WebEntity web = new WebEntity();
                web.setContentUrl(entity.getUrl());
                web.setTitle(entity.getTitle());
                break;
            case 16:
                intent = new Intent(context, GameDetailActivity.class);
                intent.putExtra(Constance.KEY.APP_ID, entity.getId());
                break;
            case 17:
                intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("articleId", entity.getId());
                intent.putExtra("typeId", entity.getArcType());
                break;
        }
        return intent;
    }

    /**
     * 存储消息实体
     *
     * @param msgId
     * @param msg
     */
    private void savePushEntity(Context context, String msgId, String msg) {
        L.j(msg);
        PushEntity entity = new Gson().fromJson(msg, PushEntity.class);
        entity.setMsgId(msgId);
        showNotification(context, entity);
    }
}

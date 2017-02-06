package com.touchrom.gaoshouyou.help.turn;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.touchrom.gaoshouyou.activity.AppManagerActivity;
import com.touchrom.gaoshouyou.activity.user.FollowActivity;
import com.touchrom.gaoshouyou.activity.user.GiftManagerActivity;
import com.touchrom.gaoshouyou.activity.user.HonorActivity;
import com.touchrom.gaoshouyou.activity.user.UserCenterActivity;
import com.touchrom.gaoshouyou.activity.user.UserCommentActivity;
import com.touchrom.gaoshouyou.activity.web.NormalWebView;
import com.touchrom.gaoshouyou.base.BaseEntity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.MsgEntity;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.help.turn.inf.ITurn;

/**
 * Created by lyy on 2015/8/14.
 * 跳转到具体页面
 */
final class TurnMsg extends TurnHelp implements ITurn {
    private Context mContext;

    private static volatile TurnMsg TURN_MSG = null;
    private static final Object LOCK = new Object();

    public static TurnMsg getInStance(Context context) {
        if (TURN_MSG == null) {
            synchronized (LOCK) {
                TURN_MSG = new TurnMsg(context);
            }
        }
        return TURN_MSG;
    }

    private TurnMsg(Context context) {
        super(context);
    }

    @Override
    public void onTurn(Context context, int appId) {

    }

    @Override
    public void onTurn(Context context, BaseEntity entity) {
        MsgEntity msg = (MsgEntity) entity;
        Intent intent = null;
        switch (msg.getMsgType()) {
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
        }
        if (intent != null){
            context.startActivity(intent);
        }
    }

    @Override
    public void onTurn(Context context, BaseEntity entity, View itemView, View rootView) {

    }
}

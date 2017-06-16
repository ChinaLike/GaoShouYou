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
import com.touchrom.gaoshouyou.base.BaseEntity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.entity.MsgEntity;
import com.touchrom.gaoshouyou.help.turn.inf.ITurn;
import com.touchrom.gaoshouyou.widget.ExpansionTemp;

/**
 * Created by lk on 2015/8/14.
 * 跳转到具体页面
 */
final class TurnGift extends TurnHelp implements ITurn {
    private Context mContext;

    private static volatile TurnGift TURN_GIFT = null;
    private static final Object LOCK = new Object();

    public static TurnGift getInStance(Context context) {
        if (TURN_GIFT == null) {
            synchronized (LOCK) {
                TURN_GIFT = new TurnGift(context);
            }
        }
        return TURN_GIFT;
    }

    private TurnGift(Context context) {
        super(context);
    }

    @Override
    public void onTurn(Context context, int appId) {

    }

    @Override
    public void onTurn(Context context, BaseEntity entity) {
        GiftEntity entity1 = (GiftEntity) entity;
        turnGift(context, entity1.getGiftId());
    }

    @Override
    public void onTurn(Context context, BaseEntity entity, View itemView, View rootView) {
        GiftEntity entity1 = (GiftEntity) entity;
        turnGift(context, entity1.getGiftId());
        ExpansionTemp temp = new ExpansionTemp(context, rootView, itemView, mWm, entity);
        mWm.addView(temp, mWmParams);
        temp.show();
    }
}

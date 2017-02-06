package com.touchrom.gaoshouyou.help.turn;

import android.content.Context;
import android.view.View;

import com.touchrom.gaoshouyou.base.BaseEntity;
import com.touchrom.gaoshouyou.entity.NewsEntity;
import com.touchrom.gaoshouyou.help.turn.inf.ITurn;
import com.touchrom.gaoshouyou.widget.ExpansionTemp;

/**
 * Created by lyy on 2015/8/14.
 * 跳转到具体页面
 */
final class TurnNews extends TurnHelp implements ITurn {

    private static volatile TurnNews TURN_NEWS = null;
    private static final Object LOCK = new Object();

    public static TurnNews getInStance(Context context) {
        if (TURN_NEWS == null) {
            synchronized (LOCK) {
                TURN_NEWS = new TurnNews(context);
            }
        }
        return TURN_NEWS;
    }

    private TurnNews(Context context) {
        super(context);
    }

    @Override
    public void onTurn(Context context, int appId) {

    }

    @Override
    public void onTurn(Context context, BaseEntity entity) {
        NewsEntity newEntity = (NewsEntity) entity;
        int type = newEntity.getType();
        if (type == NewsEntity.ITEM_BANNER) {
            return;
        } else if (type == NewsEntity.ITEM_ARTICLE) {
            turnArticle(context, newEntity.getType(), newEntity.getArticle().getArticleId());
        } else if (type == NewsEntity.ITEM_REVIEW_ARTICLE) {
            turnArticle(context, newEntity.getType(), newEntity.getReviewArticle().getArticleId());
        }
    }

    @Override
    public void onTurn(Context context, BaseEntity entity, View itemView, View rootView) {
        NewsEntity newEntity = (NewsEntity) entity;
        int type = newEntity.getType();
        if (type != NewsEntity.ITEM_BANNER) {
            ExpansionTemp temp = new ExpansionTemp(context, rootView, itemView, mWm, entity);
            mWm.addView(temp, mWmParams);
            temp.show();
        }
    }
}

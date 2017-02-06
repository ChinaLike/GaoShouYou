package com.touchrom.gaoshouyou.help.turn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.touchrom.gaoshouyou.base.BaseEntity;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.entity.sql.BrowseRecordEntity;
import com.touchrom.gaoshouyou.help.turn.inf.ITurn;
import com.touchrom.gaoshouyou.widget.ExpansionTemp;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by lyy on 2015/8/14.
 * 跳转到具体页面
 */
final class TurnGameList extends TurnHelp implements ITurn {
    private static volatile TurnGameList TURN_GAME_LIST = null;
    private static final Object LOCK = new Object();

    public static TurnGameList getInStance(Context context) {
        if (TURN_GAME_LIST == null) {
            synchronized (LOCK) {
                TURN_GAME_LIST = new TurnGameList(context);
            }
        }
        return TURN_GAME_LIST;
    }

    private TurnGameList(Context context) {
        super(context);
    }


    @Override
    public void onTurn(Context context, int appId) {

    }

    @Override
    public void onTurn(Context context, BaseEntity entity) {
        GameInfoEntity gfEntity = (GameInfoEntity) entity;
        turnGameList(context, gfEntity);
        recodeGameBrowse(gfEntity);
    }

    @Override
    public void onTurn(Context context, BaseEntity entity, View itemView, View rootView) {
        GameInfoEntity gfEntity = (GameInfoEntity) entity;
        if (gfEntity.getType() == GameInfoEntity.GAME_THEME_TITLE) {
            return;
        }
        if (gfEntity.getJump() == 1 || gfEntity.getJump() == 2) {
            turnGameList(context, gfEntity);
            return;
        }
        ExpansionTemp temp = new ExpansionTemp(context, rootView, itemView, mWm, entity);
        mWm.addView(temp, mWmParams);
        temp.show();
    }

    /**
     * 游戏列表跳转
     */
    private void turnGameList(Context context, GameInfoEntity entity) {
        if (entity.getType() == GameInfoEntity.GAME_INFO || entity.getType() == GameInfoEntity.GAME_RANK_INFO) {
            turnGameDetail(context, entity.getAppId(), true);
        } else if (entity.getType() == GameInfoEntity.GAME_IMG) {
            switch (entity.getJump()) {
                case 1: //跳转系统浏览器
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entity.getUrl())));
                    break;
                case 2: //内置浏览器
                    WebEntity webEntity = new WebEntity();
                    webEntity.setTitle(entity.getName());
                    webEntity.setContentUrl(entity.getUrl());
                    turn(context, webEntity);
                    break;
                case 3: //游戏
                    turnGameDetail(context, entity.getAppId(), true);
                    break;
                case 4: //文章
                    break;
            }
        }
    }

    /**
     * 记录浏览记录
     */
    private void recodeGameBrowse(GameInfoEntity entity) {
        List<BrowseRecordEntity> list = DataSupport.where("objId = ? and type = ?", entity.getAppId() + "", BrowseRecordEntity.GAME + "")
                .find(BrowseRecordEntity.class);
        if (list == null || list.size() == 0) {
            BrowseRecordEntity brEntity = new BrowseRecordEntity();
            brEntity.setType(BrowseRecordEntity.GAME);
            brEntity.setIcon(entity.getIconUrl());
            brEntity.setTitle(entity.getName());
            brEntity.setObjId(entity.getAppId());
            brEntity.setMsg(entity.getDescription());
            brEntity.setTime(System.currentTimeMillis());
            brEntity.save();
        } else {
            BrowseRecordEntity brEntity = list.get(0);
            brEntity.setTime(System.currentTimeMillis());
            brEntity.save();
        }
    }

}

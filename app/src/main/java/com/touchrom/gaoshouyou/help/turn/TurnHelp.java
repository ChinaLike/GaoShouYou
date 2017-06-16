package com.touchrom.gaoshouyou.help.turn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.touchrom.gaoshouyou.activity.ArticleActivity;
import com.touchrom.gaoshouyou.activity.GameDetailActivity;
import com.touchrom.gaoshouyou.activity.GiftDetailActivity;
import com.touchrom.gaoshouyou.activity.ReplyActivity;
import com.touchrom.gaoshouyou.activity.user.UserCenterActivity;
import com.touchrom.gaoshouyou.activity.web.NormalWebView;
import com.touchrom.gaoshouyou.base.BaseEntity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.ArticleEntity;
import com.touchrom.gaoshouyou.entity.BannerEntity;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.entity.MsgEntity;
import com.touchrom.gaoshouyou.entity.NewsEntity;
import com.touchrom.gaoshouyou.entity.RecommendEntity;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.help.turn.inf.ITurn;

/**
 * Created by lk on 2015/8/14.
 * 跳转帮助类
 */
public class TurnHelp {
    protected WindowManager mWm;
    protected WindowManager.LayoutParams mWmParams;

    protected TurnHelp(Context context) {
        if (mWm == null) {
            mWm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            mWmParams = new WindowManager.LayoutParams();
            mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;     // 系统提示类型,重要
            mWmParams.format = 1;
            mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
            mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制
            mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL; // 排版不受限制
            mWmParams.alpha = 1.0f;
            mWmParams.gravity = Gravity.LEFT | Gravity.TOP;   //调整悬浮窗口至左上角
            //以屏幕左上角为原点，设置x、y初始值
            mWmParams.x = 0;
            mWmParams.y = 0;
            //设置悬浮窗口长宽数据
            mWmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mWmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        }
    }

    private TurnHelp() {
    }

    /**
     * 跳转到个人中心
     */
    public static void turnUserCenter(Context context, int userId) {
        Intent intent = new Intent(context, UserCenterActivity.class);
        intent.putExtra(Constance.KEY.USER_ID, userId);
        context.startActivity(intent);
    }

    /**
     * 跳转到评论
     *
     * @param context
     * @param cmtId
     * @param type    1、评论，2、回复
     */
    public static void turnComment(Context context, int type, int cmtId) {
        Intent intent = new Intent(context, ReplyActivity.class);
        intent.putExtra("cmtId", cmtId);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    /**
     * 跳转到礼包
     */
    public static void turnGift(Context context, int giftId) {
        Intent intent = new Intent(context, GiftDetailActivity.class);
        intent.putExtra("giftId", giftId);
        context.startActivity(intent);
    }

    /**
     * 跳转文章
     */
    public static void turnArticle(Context context, int typeId, int articleId) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("typeId", typeId);
        context.startActivity(intent);
    }

    /**
     * 跳转游戏详情
     */
    public static void turnGameDetail(Context context, int appId) {
        turnGameDetail(context, appId, true);
    }

    /**
     * 跳转
     */
    public static void turnGameDetail(Context context, int appId, boolean useLoadDialog) {
        Intent intent = new Intent(context, GameDetailActivity.class);
        intent.putExtra(Constance.KEY.APP_ID, appId);
        intent.putExtra("useLoadDialog", useLoadDialog);
        context.startActivity(intent);
    }

    /**
     * Banner跳转
     */
    private static void turnBanner(Context context, BannerEntity entity) {
        switch (entity.getJump()) {
            case 1: //跳转系统浏览器
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entity.getUrl())));
                break;
            case 2: //内置浏览器
                WebEntity webEntity = new WebEntity();
                webEntity.setTitle(entity.getTitle());
                webEntity.setContentUrl(entity.getUrl());
                turnNormalWeb(context, webEntity);
                break;
            case 3:
                turnGameDetail(context, entity.getAppId());
                break;
            case 4:
                turnArticle(context, entity.getArtType(), entity.getArticleId());
                break;
        }
    }

    /**
     * 推荐跳转
     */
    private static void turnRecommend(Context context, RecommendEntity entity) {
        switch (entity.getJump()) {
            case 1: //跳转系统浏览器
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entity.getUrl())));
                break;
            case 2: //内置浏览器
                WebEntity webEntity = new WebEntity();
                webEntity.setTitle(entity.getTitle());
                webEntity.setContentUrl(entity.getUrl());
                turnNormalWeb(context, webEntity);
                break;
            case 3:
                turnGameDetail(context, entity.getAppId());
                break;
            case 4:
                break;
        }
    }

    /**
     * 跳转到普通的Web页面
     */
    public static void turnNormalWeb(Context context, WebEntity entity) {
        Intent intent = new Intent(context, NormalWebView.class);
        intent.putExtra(Constance.KEY.PARCELABLE_ENTITY, entity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void turn(Context context, int appId) {

    }

    /**
     * 普通跳转
     */
    public static void turn(Context context, BaseEntity entity) {
        ITurn iTurn = null;
        if (entity instanceof BannerEntity) {
            turnBanner(context, (BannerEntity) entity);
        } else if (entity instanceof GameInfoEntity) {
            iTurn = TurnGameList.getInStance(context);
        } else if (entity instanceof RecommendEntity) {
            turnRecommend(context, (RecommendEntity) entity);
        } else if (entity instanceof NewsEntity) {
            iTurn = TurnNews.getInStance(context);
        } else if (entity instanceof WebEntity) {
            iTurn = TurnWeb.getInStance(context);
        } else if (entity instanceof MsgEntity) {
            iTurn = TurnMsg.getInStance(context);
        } else if (entity instanceof GiftEntity) {
            iTurn = TurnGift.getInStance(context);
        }
        if (iTurn != null) {
            iTurn.onTurn(context, entity);
        }
    }

    /**
     * 带动画的跳转，只支持游戏列表,文章列表,礼包
     */
    public static void turn(Context context, BaseEntity entity, View itemView, View rootView) {
        ITurn iTurn = null;
        if (entity instanceof GameInfoEntity) {
            iTurn = TurnGameList.getInStance(context);
        } else if (entity instanceof NewsEntity) {
            iTurn = TurnNews.getInStance(context);
        } else if (entity instanceof GiftEntity) {
            iTurn = TurnGift.getInStance(context);
        }
        if (iTurn != null) {
            iTurn.onTurn(context, entity, itemView, rootView);
        }
    }
}

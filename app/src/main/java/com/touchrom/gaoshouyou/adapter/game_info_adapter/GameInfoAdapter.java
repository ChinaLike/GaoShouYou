package com.touchrom.gaoshouyou.adapter.game_info_adapter;

import android.content.Context;

import com.touchrom.gaoshouyou.base.adapter.d_adapter.AbsDAdapter;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;

import java.util.List;

/**
 * Created by lyy on 2015/12/9.
 */
public class GameInfoAdapter extends AbsDAdapter<GameInfoEntity> {

    public GameInfoAdapter(Context context, List<GameInfoEntity> data) {
        super(context, data);
        mManager.addDelegate(new GameInfoDelegate(context, GameInfoEntity.GAME_INFO));
        mManager.addDelegate(new GameTitleDelegate(context, GameInfoEntity.GAME_THEME_TITLE));
        mManager.addDelegate(new GameImgDelegate(context, GameInfoEntity.GAME_IMG));
        mManager.addDelegate(new GameRankDelegate(context, GameInfoEntity.GAME_RANK_INFO));
        GameCollectDelegate collectDelegate = new GameCollectDelegate(context, GameInfoEntity.GAME_COLLECT);
        collectDelegate.setNotifyCallback(new GameCollectDelegate.OnNotifyCallback() {
            @Override
            public void onNotify(int position) {
                mData.remove(position);
                notifyDataSetChanged();
            }
        });
        mManager.addDelegate(collectDelegate);
    }

    @Override
    public void notifyDataSetChanged() {
        mManager.getDelegate(GameInfoEntity.GAME_INFO).onNotify();
        mManager.getDelegate(GameInfoEntity.GAME_IMG).onNotify();
        super.notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }
}

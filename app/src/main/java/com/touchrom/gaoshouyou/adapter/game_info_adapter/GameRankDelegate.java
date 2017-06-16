package com.touchrom.gaoshouyou.adapter.game_info_adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.widget.RankTriangleView;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/28.
 */
class GameRankDelegate extends GameInfoDelegate {


    public GameRankDelegate(Context context, int itemType) {
        super(context, itemType);
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_game_rank_info;
    }

    @Override
    public GameInfoHolder createHolder(View convertView) {
        return new RankHolder(convertView);
    }

    @Override
    public void bindData(int position, GameInfoHolder helper, GameInfoEntity entity) {
        super.bindData(position, helper, entity);
        String color = "#0DB8F6";
        int rank = entity.getRank();
        if (rank == 1) {
            color = "#FF8C5B";
        } else if (rank == 2) {
            color = "#FFC13A";
        } else if (rank == 3) {
            color = "#9AD555";
        }

        ((RankHolder)helper).rank.setTriangleColor(Color.parseColor(color));
        ((RankHolder)helper).rank.setRank(rank + "");
    }

    class RankHolder extends GameInfoDelegate.GameInfoHolder {
        @InjectView(R.id.rank_view)
        RankTriangleView rank;
        public RankHolder(View view) {
            super(view);
        }
    }
}

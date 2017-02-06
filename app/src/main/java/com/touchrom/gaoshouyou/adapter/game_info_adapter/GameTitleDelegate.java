package com.touchrom.gaoshouyou.adapter.game_info_adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.touchrom.gaoshouyou.base.adapter.d_adapter.AbsDHolder;
import com.touchrom.gaoshouyou.base.adapter.d_adapter.AbsDelegation;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/28.
 */
class GameTitleDelegate extends AbsDelegation<GameInfoEntity, GameTitleDelegate.GameTitleHolder> {

    protected GameTitleDelegate(Context context, int itemType) {
        super(context, itemType);
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_game_theme_title;
    }

    @Override
    public GameTitleHolder createHolder(View convertView) {
        return new GameTitleHolder(convertView);
    }

    @Override
    public void bindData(int position, GameTitleHolder helper, GameInfoEntity item) {
        helper.title.setText(item.getName());
        helper.divider.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
    }

    class GameTitleHolder extends AbsDHolder {
        @InjectView(R.id.game_theme_title)
        TextView title;
        @InjectView(R.id.gray_divider)
        View divider;

        public GameTitleHolder(View view) {
            super(view);
        }
    }
}

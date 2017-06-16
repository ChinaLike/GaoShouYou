package com.touchrom.gaoshouyou.adapter.game_info_adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.touchrom.gaoshouyou.base.adapter.d_adapter.AbsDHolder;
import com.touchrom.gaoshouyou.base.adapter.d_adapter.AbsDelegation;
import com.lyy.ui.widget.StarBar;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.entity.SettingEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.help.DownloadHelp;
import com.touchrom.gaoshouyou.help.ImgHelp;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/28.
 */
class GameInfoDelegate extends AbsDelegation<GameInfoEntity, GameInfoDelegate.GameInfoHolder> {

    protected SettingEntity mSettingEntity;

    public GameInfoDelegate(Context context, int itemType) {
        super(context, itemType);
        mSettingEntity = AppManager.getInstance().getSetting();
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_game_info;
    }

    @Override
    public GameInfoHolder createHolder(View convertView) {
        return new GameInfoHolder(convertView);
    }

    @Override
    public void bindData(int position, GameInfoHolder helper, GameInfoEntity entity) {
        ClickListener listener = (ClickListener) helper.download.getTag(helper.download.getId());
        if (listener == null) {
            listener = new ClickListener();
            helper.download.setTag(helper.download.getId(), listener);
        }
        listener.setAction(entity);
        helper.download.setOnClickListener(listener);
        if (mSettingEntity.isShowImg()) {
            ImgHelp.setImg(getContext(), entity.getIconUrl(), helper.icon);
        }
        helper.name.setText(entity.getName());
        helper.detail.setText(entity.getDescription());
        String introduction = entity.getTypeName() + "  " + entity.getDownNum() + "下载  " + entity.getSize();
        helper.introduction.setText(introduction);
        helper.starBar.setScore(entity.getScore());
        helper.divider.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
    }

    class ClickListener implements View.OnClickListener {
        GameInfoEntity entity;

        public void setAction(GameInfoEntity entity) {
            this.entity = entity;
        }

        @Override
        public void onClick(View v) {
            DownloadEntity dEntity = new DownloadEntity();
            dEntity.setGameId(entity.getAppId());
            dEntity.setDownloadUrl(entity.getDownloadUrl());
            dEntity.setImgUrl(entity.getIconUrl());
            dEntity.setName(entity.getName());
            DownloadHelp.newInstance().download(getContext(), dEntity);
        }
    }

    @Override
    public void onNotify() {
        super.onNotify();
        mSettingEntity = AppManager.getInstance().getSetting();
    }

    class GameInfoHolder extends AbsDHolder {
        @InjectView(R.id.game_icon)
        ImageView icon;
        @InjectView(R.id.game_name)
        TextView name;
        @InjectView(R.id.game_detail)
        TextView detail;
        @InjectView(R.id.game_introduction)
        TextView introduction;
        @InjectView(R.id.star_bar)
        StarBar starBar;
        @InjectView(R.id.download)
        Button download;
        @InjectView(R.id.gray_divider)
        View divider;

        public GameInfoHolder(View view) {
            super(view);
        }
    }
}

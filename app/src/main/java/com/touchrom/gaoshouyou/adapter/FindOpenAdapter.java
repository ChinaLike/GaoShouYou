package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.OpenGameEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.help.DownloadHelp;
import com.touchrom.gaoshouyou.help.ImgHelp;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/22.
 * 发现开服
 */
public class FindOpenAdapter extends AbsOrdinaryAdapter<OpenGameEntity, FindOpenAdapter.FindOpenHolder> {

    public FindOpenAdapter(Context context, List<OpenGameEntity> data) {
        super(context, data);
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_find_open;
    }

    @Override
    public void bindData(int position, FindOpenHolder helper, OpenGameEntity item) {
        ImgHelp.setImg(getContext(), item.getIconUrl(), helper.icon);
        helper.name.setText(item.getGameName());
        helper.detail.setText(item.getGameType() + " " + item.getDownNum() + "下载 " + item.getSize());
        helper.openType.setText(item.getOpenType());
        helper.time.setText(item.getTime());
        BtListener listener = (BtListener) helper.bt.getTag(R.id.bt);
        if (listener == null) {
            listener = new BtListener();
            helper.bt.setTag(R.id.bt, listener);
        }
        listener.setAction(position);
        helper.bt.setOnClickListener(listener);
    }

    private class BtListener implements View.OnClickListener {
        int position;

        public void setAction(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            OpenGameEntity gEntity = mData.get(position);
            DownloadEntity dEntity = new DownloadEntity();
            dEntity.setGameId(gEntity.getAppId());
            dEntity.setDownloadUrl(gEntity.getDownloadUrl());
            dEntity.setImgUrl(gEntity.getIconUrl());
            dEntity.setName(gEntity.getGameName());
            DownloadHelp.newInstance().download(getContext(), dEntity);
        }
    }

    @Override
    public FindOpenHolder getViewHolder(View convertView) {
        return new FindOpenHolder(convertView);
    }

    class FindOpenHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.game_icon)
        ImageView icon;
        @InjectView(R.id.game_name)
        TextView name;
        @InjectView(R.id.game_introduction)
        TextView detail;
        @InjectView(R.id.open_type)
        TextView openType;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.download)
        Button bt;

        public FindOpenHolder(View view) {
            super(view);
        }
    }
}

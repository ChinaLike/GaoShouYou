package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.GameFactoryListActivity;
import com.touchrom.gaoshouyou.activity.GameTopIcClassifyActivity;
import com.touchrom.gaoshouyou.entity.TopicEntity;
import com.touchrom.gaoshouyou.help.ImgHelp;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/2/26.
 * 游戏专题Adapter
 */
public class GameTopicAdapter extends AbsOrdinaryAdapter<TopicEntity, GameTopicAdapter.TopicHolder> {

    public GameTopicAdapter(Context context, List<TopicEntity> data) {
        super(context, data);
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_game_topic;
    }

    @Override
    public void bindData(int position, TopicHolder helper, TopicEntity item) {
        helper.topicTitle.setText(item.getTopicTitle());
        ImgHelp.setImg(getContext(), item.getImgUrl(), helper.img);
        MyButtonListener listener = (MyButtonListener) helper.more.getTag(helper.more.getId());
        if (listener == null) {
            listener = new MyButtonListener();
            helper.more.setTag(helper.more.getId(), listener);
        }
        helper.more.setOnClickListener(listener);
        listener.setEntity(item, position);
        helper.contentTile.setText(item.getContentTitle());
        helper.contentMsg.setText(item.getContentMsg());
    }

    @Override
    public TopicHolder getViewHolder(View convertView) {
        return new TopicHolder(convertView);
    }

    class MyButtonListener implements View.OnClickListener {
        TopicEntity entity;
        int position;

        public void setEntity(TopicEntity entity, int position) {
            this.entity = entity;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = null;
            if (position == 4) {
                intent = new Intent(getContext(), GameFactoryListActivity.class);
            } else {
                intent = new Intent(getContext(), GameTopIcClassifyActivity.class);
                intent.putExtra("topIcId", mData.get(position).getTopicId());
            }
            getContext().startActivity(intent);
        }
    }

    class TopicHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.topic_title)
        TextView topicTitle;
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.more)
        TextView more;
        @InjectView(R.id.content_title)
        TextView contentTile;
        @InjectView(R.id.content_msg)
        TextView contentMsg;

        public TopicHolder(View view) {
            super(view);
        }
    }
}

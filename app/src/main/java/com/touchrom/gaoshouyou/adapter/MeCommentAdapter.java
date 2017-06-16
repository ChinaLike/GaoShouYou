package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.MeCommentEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.widget.MyCommentText;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/15.
 * 我的评论适配器
 */
public class MeCommentAdapter extends AbsOrdinaryAdapter<MeCommentEntity, MeCommentAdapter.MyCommentHolder> {
    public MeCommentAdapter(Context context, List<MeCommentEntity> data) {
        super(context, data);
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_my_comment;
    }

    @Override
    public void bindData(int position, MyCommentHolder helper, MeCommentEntity item) {
        helper.title.setText(item.getTitle());
        TxListener cL = (TxListener) helper.title.getTag(R.id.title);
        if (cL == null) {
            cL = new TxListener();
            helper.title.setTag(R.id.title, cL);
        }
        cL.setReEntity(item);
        helper.title.setOnClickListener(cL);
        helper.replyContent.setText(item.getContent());

        helper.time.setText("发表于 " + item.getTime());
        helper.like.setText("赞 " + item.getLikeNum() + "   回复 " + item.getReplyNum());
    }

    @Override
    public MyCommentHolder getViewHolder(View convertView) {
        return new MyCommentHolder(convertView);
    }

    private class TxListener implements View.OnClickListener {
        MeCommentEntity entity;

        public void setReEntity(MeCommentEntity entity) {
            this.entity = entity;
        }

        @Override
        public void onClick(View v) {
            if (entity.getTypeId() == 0) {
                TurnHelp.turnGameDetail(getContext(), entity.getAppId());
            } else {
                TurnHelp.turnArticle(getContext(), entity.getTypeId(), entity.getAppId());
            }
        }
    }

    class MyCommentHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.reply_content)
        MyCommentText replyContent;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.like)
        TextView like;

        public MyCommentHolder(View view) {
            super(view);
        }
    }

}

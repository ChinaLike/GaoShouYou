package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.lyy.ui.widget.CircleImageView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.ReplyCommentEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.widget.MyCommentText;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/15.
 * 我的评论适配器
 */
public class ReplyCommentAdapter extends AbsOrdinaryAdapter<ReplyCommentEntity, ReplyCommentAdapter.ReplyCommentHolder> {


    public ReplyCommentAdapter(Context context, List<ReplyCommentEntity> data) {
        super(context, data);
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_re_my_comment;
    }

    @Override
    public void bindData(int position, ReplyCommentHolder holder, ReplyCommentEntity reEntity) {
        Glide.with(getContext()).load(reEntity.getImgUrl()).into(holder.img);
        holder.nikeName.setText(reEntity.getNikeName());
        TxListener1 nL = (TxListener1) holder.nikeName.getTag(R.id.nike_name);
        if (nL == null) {
            nL = new TxListener1();
            holder.nikeName.setTag(R.id.nike_name, nL);
        }
        nL.setReEntity(reEntity);
        holder.nikeName.setOnClickListener(nL);
        holder.time.setText(reEntity.getTime() + "  评论了");
        holder.content.setText(reEntity.getContent());
        TxListener1 cL = (TxListener1) holder.content.getTag(R.id.content);
        if (cL == null) {
            cL = new TxListener1();
            holder.content.setTag(R.id.content, cL);
        }
        cL.setReEntity(reEntity);
        holder.content.setOnClickListener(cL);
        holder.replyContent.setText(reEntity.getReplyContent());
    }

    @Override
    public ReplyCommentHolder getViewHolder(View convertView) {
        return new ReplyCommentHolder(convertView);
    }

    private class TxListener1 implements View.OnClickListener {
        ReplyCommentEntity entity;

        public void setReEntity(ReplyCommentEntity entity) {
            this.entity = entity;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nike_name:
                    int userId = entity.getReplyerId();
                    if (userId == 0){
                        return;
                    }
                    TurnHelp.turnUserCenter(getContext(), userId);
                    break;
                case R.id.content:
                    TurnHelp.turnComment(getContext(), 1, entity.getCmtId());
                    break;
            }
        }
    }

    class ReplyCommentHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.user_img)
        CircleImageView img;
        @InjectView(R.id.nike_name)
        TextView nikeName;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.content)
        TextView content;
        @InjectView(R.id.reply_content)
        MyCommentText replyContent;

        public ReplyCommentHolder(View view) {
            super(view);
        }
    }

}

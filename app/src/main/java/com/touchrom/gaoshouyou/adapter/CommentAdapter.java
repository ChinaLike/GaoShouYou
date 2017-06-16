package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.bumptech.glide.Glide;
import com.touchrom.gaoshouyou.activity.LoginActivity;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.lyy.ui.widget.CircleImageView;
import com.lyy.ui.widget.ReplyText;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.ReplyActivity;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.CommentEntity;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/14.
 * 评论适配器
 */
public class CommentAdapter extends AbsOrdinaryAdapter<CommentEntity, CommentAdapter.CommentHolder> {

    private int mType = 1;

    /**
     * @param context
     * @param data
     * @param type    1、评论，2、回复
     */
    public CommentAdapter(Context context, List<CommentEntity> data, int type) {
        super(context, data);
        mType = type;
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_comment;
    }

    @Override
    public void bindData(int position, CommentHolder helper, CommentEntity item) {
//        ImgHelp.setImg(getContext(), item.getImgUrl(), helper.img);
        Glide.with(getContext()).load(item.getImgUrl()).into(helper.img);
        helper.floor.setText(item.getFloor() + "楼");
        //nikeName
        TxListener nL = (TxListener) helper.nikeName.getTag(R.id.nike_name);
        if (nL == null) {
            nL = new TxListener();
            helper.nikeName.setTag(R.id.nike_name, nL);
        }
        nL.setPosition(position);
        helper.nikeName.setOnClickListener(nL);
        helper.nikeName.setText(item.getCmName());
        //like
        TxListener likeListener = (TxListener) helper.like.getTag(R.id.like);
        if (likeListener == null) {
            likeListener = new TxListener();
            helper.like.setTag(R.id.like, likeListener);
        }
        likeListener.setPosition(position);
        helper.like.setOnClickListener(likeListener);
        helper.like.setSelected(item.isAgreed());
        helper.like.setText(item.getAgreeNum() + "");

        helper.time.setText(item.getTime() + "  回复 " + item.getReply().size());
        helper.content.setText(item.getContent());
        TxListener listener = (TxListener) helper.allComment.getTag(R.id.all_comment);
        if (listener == null) {
            listener = new TxListener();
            helper.allComment.setTag(R.id.all_comment, listener);
        }
        if (item.getReply().size() == 0) {
            helper.allComment.setVisibility(View.GONE);
            helper.replyText.setVisibility(View.GONE);
            helper.line.setVisibility(View.GONE);
        } else {
            helper.allComment.setVisibility(View.VISIBLE);
            helper.replyText.setVisibility(View.VISIBLE);
            helper.line.setVisibility(View.VISIBLE);
            listener.setPosition(position);
            helper.allComment.setOnClickListener(listener);
        }
        List<ReplyText.ReplyEntity> replys = new ArrayList<>();
        for (CommentEntity.ReplyEntity reply : item.getReply()) {
            ReplyText.ReplyEntity entity = new ReplyText.ReplyEntity();
            entity.setReply(reply.getReName());
            entity.setReplyId(reply.getReId());
            entity.setByReply(reply.getByReName());
            entity.setByReplyId(reply.getByReId());
            entity.setReplyContent(reply.getReContent());
            entity.setStr(reply.getReTime());
            entity.setId(reply.getReCmtId());
            replys.add(entity);
        }
        helper.replyText.setReplyContent(replys);
        ReListener reListener = (ReListener) helper.replyText.getTag(R.id.reply);
        if (reListener == null) {
            reListener = new ReListener();
            helper.replyText.setTag(R.id.reply, reListener);
        }
        helper.replyText.setOnReplyClickListener(reListener);
        reListener.setCmtId(item.getCmtId());
    }

    @Override
    public CommentHolder getViewHolder(View convertView) {
        return new CommentHolder(convertView);
    }

    class ReListener implements ReplyText.OnReplyClickListener {
        int cmtId;

        public void setCmtId(int cmId) {
            this.cmtId = cmId;
        }

        @Override
        public void onReplyClick(View parent, int id) {
            TurnHelp.turnUserCenter(getContext(), id);
        }

        @Override
        public void onReplyContentClick(View parent, ReplyText.ReplyEntity entity) {
            Intent intent = new Intent(getContext(), ReplyActivity.class);
            intent.putExtra("cmtId", cmtId);
            intent.putExtra("reply", entity);
            intent.putExtra("type", mType);
            if (getContext() instanceof BaseActivity) {
                ((BaseActivity) getContext()).startActivityForResult(intent, ResultCode.ACTIVITY.GD_COMMENT);
            } else {
                getContext().startActivity(intent);
            }
        }
    }

    class TxListener implements View.OnClickListener {
        int position;
        ServiceUtil util;

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.all_comment:
                    intent = new Intent(getContext(), ReplyActivity.class);
                    intent.putExtra("cmtId", mData.get(position).getCmtId());
                    intent.putExtra("type", mType);
                    ((BaseActivity) getContext()).startActivityForResult(intent, ResultCode.ACTIVITY.GD_COMMENT);
                    break;
                case R.id.nike_name:
                    int userId = mData.get(position).getCmId();
                    if (userId == 0) {
                        return;
                    }
                    if (AppManager.getInstance().isLogin()) {
                        TurnHelp.turnUserCenter(getContext(), userId);
                    } else {
                        intent = new Intent(getContext(), LoginActivity.class);
                        getContext().startActivity(intent);
                    }
                    break;
                case R.id.like:
                    if (!mData.get(position).isAgreed()) {
                        like();
                    }
                    break;
            }
        }

        private void like() {
            util = ServiceUtil.getInstance(getContext());
            Map<String, String> params = new HashMap<>();
            params.put("cmtId", mData.get(position).getCmtId() + "");
            util.replyPraise(params, new HttpUtil.AbsResponse() {
                @Override
                public void onResponse(String data) {
                    super.onResponse(data);
                    try {
                        JSONObject obj = new JSONObject(data);
                        if (util.isRequestSuccess(obj)) {
                            JSONObject entity = obj.getJSONObject(ServiceUtil.DATA_KEY);
                            mData.get(position).setAgreed(entity.getBoolean("result"));
                            int n = mData.get(position).getAgreeNum() + 1;
                            mData.get(position).setAgreeNum(n);
                            T.showShort(getContext(), entity.getString("message"));
                            notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public class CommentHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.user_img)
        CircleImageView img;
        @InjectView(R.id.floor)
        TextView floor;
        @InjectView(R.id.nike_name)
        TextView nikeName;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.like)
        TextView like;
        @InjectView(R.id.content)
        TextView content;
        @InjectView(R.id.reply)
        ReplyText replyText;
        @InjectView(R.id.all_comment)
        TextView allComment;
        @InjectView(R.id.line)
        View line;

        public CommentHolder(View view) {
            super(view);
        }
    }
}

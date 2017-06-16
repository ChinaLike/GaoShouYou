package com.touchrom.gaoshouyou.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arialyy.frame.util.KeyBoardUtils;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.widget.CircleImageView;
import com.lyy.ui.widget.ReplyText;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityReplyBinding;
import com.touchrom.gaoshouyou.entity.CommentEntity;
import com.touchrom.gaoshouyou.help.AnimHelp;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.CommentModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/14.
 * 评论界面
 */
public class ReplyActivity extends BaseActivity<ActivityReplyBinding> implements View.OnClickListener {
    @InjectView(R.id.user_img)
    CircleImageView mUserImg;
    @InjectView(R.id.floor)
    TextView mFloor;
    @InjectView(R.id.nike_name)
    TextView mNikeName;
    @InjectView(R.id.time)
    TextView mTime;
    @InjectView(R.id.like)
    TextView mLike;
    @InjectView(R.id.content)
    TextView mContent;
    @InjectView(R.id.reply)
    ReplyText mReplyText;
    @InjectView(R.id.all_comment)
    TextView mAllComment;
    @InjectView(R.id.bar_title)
    TextView mBarTitle;
    @InjectView(R.id.send_et)
    EditText mSendEt;
    @InjectView(R.id.send_bt)
    Button mSendBt;
    private int mCmtId = -1;
    private CommentEntity mEntity;
    private ReplyText.ReplyEntity mCurrentReplyEntity;
    private int mType = 1;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        KeyBoardUtils.closeKeybord(mSendEt, this);
        mCmtId = getIntent().getIntExtra("cmtId", -1);
        mCurrentReplyEntity = getIntent().getParcelableExtra("reply");
        mType = getIntent().getIntExtra("type", 1);
        if (mCmtId == -1) {
            L.e(TAG, "请输入正确的评论Id");
            finish();
            return;
        }
        mToolbar.setTitle("评论");
        getModule(CommentModule.class).getCommentDetail(mType, mCmtId);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_reply;
    }

    private void setUpWidget(CommentEntity entity) {
        mEntity = entity;
        ImgHelp.setImg(this, entity.getImgUrl(), mUserImg);
        mBarTitle.setText(entity.getFloor() + "楼");
//        mFloor.setText(entity.getFloor() + "楼");
        mFloor.setVisibility(View.INVISIBLE);
        mNikeName.setText(entity.getCmName());
        mNikeName.setOnClickListener(this);
        mTime.setText(entity.getTime() + "  回复 " + entity.getReply().size());
        mLike.setText(entity.getAgreeNum() + "");
        mLike.setSelected(entity.isAgreed());
        mLike.setOnClickListener(this);
        mContent.setText(entity.getContent());
        mContent.setOnClickListener(this);
        mAllComment.setVisibility(View.GONE);
        if (mCurrentReplyEntity != null) {
            KeyBoardUtils.openKeybord(mSendEt, this);
            mSendEt.setHint("回复" + mCurrentReplyEntity.getReply() + "：");
        }
        List<ReplyText.ReplyEntity> replys = new ArrayList<>();
        for (CommentEntity.ReplyEntity reply : entity.getReply()) {
            ReplyText.ReplyEntity replyEntity = new ReplyText.ReplyEntity();
            replyEntity.setReply(reply.getReName());
            replyEntity.setReplyId(reply.getReId());
            replyEntity.setByReply(reply.getByReName());
            replyEntity.setByReplyId(reply.getByReId());
            replyEntity.setReplyContent(reply.getReContent());
            replyEntity.setStr(reply.getReTime());
            replyEntity.setId(reply.getReCmtId());
            replys.add(replyEntity);
        }
        mReplyText.setReplyContent(replys);
        mReplyText.setOnReplyClickListener(new ReplyText.OnReplyClickListener() {
            @Override
            public void onReplyClick(View parent, int id) {
                TurnHelp.turnUserCenter(ReplyActivity.this, id);
            }

            @Override
            public void onReplyContentClick(View parent, ReplyText.ReplyEntity entity) {
                KeyBoardUtils.openKeybord(mSendEt, ReplyActivity.this);
                mCurrentReplyEntity = entity;
                mSendEt.setHint("回复" + entity.getReply() + "：");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_bt:
                sendReply();
                break;
            case R.id.content:
                KeyBoardUtils.openKeybord(mSendEt, this);
                mSendEt.setText("");
                mSendEt.setHint("我来说一句...");
                mCurrentReplyEntity = null;
                break;
            case R.id.nike_name:
                if (mEntity.getCmId() == 0) {
                    return;
                }
                TurnHelp.turnUserCenter(this, mEntity.getCmId());
                break;
            case R.id.like:
                if (!mEntity.isAgreed()) {
                    getModule(CommentModule.class).like(mCmtId);
                }
                break;
        }
    }

    private void sendReply() {
        if (TextUtils.isEmpty(mSendEt.getText())) {
            AnimHelp.getInstance().nope(mSendBt).start();
            return;
        }
        if (mCurrentReplyEntity != null) {
            getModule(CommentModule.class).sendReply(mType, mCurrentReplyEntity.getId(), mSendEt.getText().toString());
        } else {
            getModule(CommentModule.class).sendReply(mType, mCmtId, mSendEt.getText().toString());
        }
        KeyBoardUtils.closeKeybord(mSendEt, this);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_COMMENT_DETAIL) {
            setUpWidget((CommentEntity) obj);
        } else if (result == ResultCode.SERVER.SEND_REPLY) {
            if ((boolean) obj) {
                mSendEt.setText("");
                mSendEt.setHint("我来说一句...");
                mCurrentReplyEntity = null;
                getModule(CommentModule.class).getCommentDetail(mType, mCmtId);
            }
        } else if (result == ResultCode.SERVER.COMMENT_LIKE) {
            mEntity.setAgreed((Boolean) obj);
            mLike.setText(mEntity.getAgreeNum() + 1 + "");
            mLike.setSelected(mEntity.isAgreed());
        }
    }
}

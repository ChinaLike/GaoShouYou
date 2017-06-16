package com.touchrom.gaoshouyou.fragment.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.arialyy.frame.util.KeyBoardUtils;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.ReplyActivity;
import com.touchrom.gaoshouyou.adapter.CommentAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentUserLeaveMsgBinding;
import com.touchrom.gaoshouyou.entity.CommentEntity;
import com.touchrom.gaoshouyou.help.AnimHelp;
import com.touchrom.gaoshouyou.module.UserModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/14.
 * 用户留言
 */
@SuppressLint("ValidFragment")
public class UserLeaveMsgFragment extends BaseFragment<FragmentUserLeaveMsgBinding> implements View.OnClickListener {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    @InjectView(R.id.send_et)
    EditText mSendEt;
    @InjectView(R.id.send_bt)
    Button mSendBt;
    private int mUserId;
    private int mPage = 1;
    private CommentAdapter mAdapter;
    private List<CommentEntity> mData = new ArrayList<>();
    private boolean isFirst = true;

    public UserLeaveMsgFragment(int userId) {
        mUserId = userId;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mAdapter = new CommentAdapter(getContext(), mData, 2);
        mList.setAdapter(mAdapter);
        mList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isFirst = false;
                mPage++;
                getModule(UserModule.class).getLeaveMsgList(mUserId, mPage);
            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ReplyActivity.class);
                intent.putExtra("cmtId", mData.get(position - 1).getCmtId());
                intent.putExtra("type", 2);
                startActivityForResult(intent, ResultCode.ACTIVITY.GD_COMMENT);
            }
        });
        getModule(UserModule.class).getLeaveMsgList(mUserId, mPage);
        mSendBt.setOnClickListener(this);
    }

    public void notifyData() {
        mData.clear();
        mPage = 1;
        getModule(UserModule.class).getLeaveMsgList(mUserId, mPage);
    }

    private void setUpList(List<CommentEntity> data) {
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultCode.ACTIVITY.GD_COMMENT) {
            notifyData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_bt:
                sendReply();
                break;
        }
    }

    private void sendReply() {
        if (TextUtils.isEmpty(mSendEt.getText())) {
            AnimHelp.getInstance().nope(mSendBt).start();
            return;
        }
        getModule(UserModule.class).sendLeaveMsg(mUserId, mSendEt.getText().toString());
        KeyBoardUtils.closeKeybord(mSendEt, getContext());
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_user_leave_msg;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_USER_LEAVE_MSG) {
            List<CommentEntity> list = (List<CommentEntity>) obj;
            setUpList(list);
            if (!isFirst && list.size() == 0) {
                hintTempView();
            }
        } else if (result == ResultCode.SERVER.SEND_LEAVE_MSG) {
            mSendEt.setText("");
            KeyBoardUtils.closeKeybord(mSendEt, getContext());
            notifyData();
        }
    }
}

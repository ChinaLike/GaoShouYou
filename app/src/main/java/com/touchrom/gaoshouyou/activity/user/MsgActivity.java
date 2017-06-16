package com.touchrom.gaoshouyou.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arialyy.frame.util.StringUtil;
import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityMsgBinding;
import com.touchrom.gaoshouyou.entity.MsgEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.UserModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/21.
 * 消息界面
 */
public class MsgActivity extends BaseActivity<ActivityMsgBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private List<MsgEntity> mData = new ArrayList<>();
    private MsgAdapter mAdapter;
    private boolean isRefresh = true;
    private int mPage = 1;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_msg;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mToolbar.setTitle("消息");
        mList.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                isRefresh = true;
                getModule(UserModule.class).getMsgList(mPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isRefresh = false;
                getModule(UserModule.class).getMsgList(mPage);
            }
        });
        mAdapter = new MsgAdapter(this, mData, R.layout.item_message);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TurnHelp.turn(MsgActivity.this, mData.get(position - 1));
            }
        });
        getModule(UserModule.class).getMsgList(mPage);
    }

    private void setUpList(List<MsgEntity> data) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_MSG_LIST) {
            setUpList((List<MsgEntity>) obj);
        }
    }

    class MsgAdapter extends SimpleAdapter<MsgEntity> {

        public MsgAdapter(Context context, List<MsgEntity> mData, int itemLayoutId) {
            super(context, mData, itemLayoutId);
        }

        @Override
        public void convert(SimpleViewHolder helper, MsgEntity item) {
            helper.setText(R.id.time, item.getTime());
            helper.setText(R.id.title, item.getTitle());
            if (item.getMsgType() == 0) {       //如果通知是游戏更新通知
                String str = "您安装的" + item.getNum() + "个游戏有了新版本，快来看看吧。";
                helper.setText(R.id.content, StringUtil.highLightStr(str, item.getNum() + "", getResources().getColor(R.color.orange)));
            } else {
                helper.setText(R.id.content, item.getContent());
            }
        }
    }
}

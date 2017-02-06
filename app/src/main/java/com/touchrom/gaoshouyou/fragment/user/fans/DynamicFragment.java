package com.touchrom.gaoshouyou.fragment.user.fans;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arialyy.frame.core.NotifyHelp;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.ReplyCommentAdapter;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentCollectBinding;
import com.touchrom.gaoshouyou.entity.ReplyCommentEntity;
import com.touchrom.gaoshouyou.module.UserModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/9.
 * 动态
 */
@SuppressLint("ValidFragment")
public class DynamicFragment extends BaseFragment<FragmentCollectBinding> implements NotifyHelp.OnNotifyCallback {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private List<ReplyCommentEntity> mData = new ArrayList<>();
    private ReplyCommentAdapter mAdapter;
    private boolean isRefresh = true;
    private int mPage = 1;
    int mUserId;

    public DynamicFragment() {
        this(AppManager.getInstance().getUser().getUserId());
    }

    public DynamicFragment(int userId) {
        mUserId = userId;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        showTempView(TempView.LOADING);
        mList.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                isRefresh = true;
                dataNotify();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isRefresh = false;
                dataNotify();
            }
        });
        mAdapter = new ReplyCommentAdapter(getContext(), mData);
        mList.setAdapter(mAdapter);
        dataNotify();
        NotifyHelp.getInstance().addObj(Constance.NOTIFY_KEY.FANS, this);
    }

    private void setUpList(List<ReplyCommentEntity> data) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    public void dataNotify() {
        getModule(UserModule.class).getDynamicList(mUserId, mPage);
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        dataNotify();
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        mActivity.finish();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_DYNAMIC_LIST) {
            List<ReplyCommentEntity> list = (List<ReplyCommentEntity>) obj;
            if (!isRefresh && list.size() == 0) {
                hintTempView();
            }
            setUpList(list);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_collect;
    }


    @Override
    public void onNotify(int action, Object obj) {
        dataNotify();
    }
}

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
import com.touchrom.gaoshouyou.adapter.FansAdapter;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentCollectBinding;
import com.touchrom.gaoshouyou.entity.FansEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.UserModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/9.
 * 关注和粉丝
 */
@SuppressLint("ValidFragment")
public class FansFragment extends BaseFragment<FragmentCollectBinding> implements NotifyHelp.OnNotifyCallback {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private List<FansEntity> mData = new ArrayList<>();
    private FansAdapter mAdapter;
    private boolean isRefresh = true;
    private int mPage = 1;
    private int mType = 2; //2 ==> 关注，3 ==> 粉丝
    int mUserId;

    public FansFragment(int type) {
        this(AppManager.getInstance().getUser().getUserId(), type);
    }


    public FansFragment(int userId, int type) {
        mType = type;
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
        mAdapter = new FansAdapter(getContext(), mData, mType, mUserId == AppManager.getInstance().getUser().getUserId());
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TurnHelp.turnUserCenter(getContext(), mData.get(position - 1).getFansId());
            }
        });

        dataNotify();
        NotifyHelp.getInstance().addObj(Constance.NOTIFY_KEY.FANS,this);
    }

    private void setUpList(List<FansEntity> data) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    public void dataNotify() {
        getModule(UserModule.class).getFansList(mUserId, mType, mPage);
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
        if (result == ResultCode.SERVER.GET_FANS_LIST) {
            List<FansEntity> list = (List<FansEntity>) obj;
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

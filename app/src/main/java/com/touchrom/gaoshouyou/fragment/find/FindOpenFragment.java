package com.touchrom.gaoshouyou.fragment.find;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.lyy.ui.widget.NoScrollGridView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.FindOpenAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentFindGiftBinding;
import com.touchrom.gaoshouyou.entity.OpenGameEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.FindModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/22.
 * 发现--礼包
 */
@SuppressLint("ValidFragment")
public class FindOpenFragment extends BaseFragment<FragmentFindGiftBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    @InjectView(R.id.grid)
    NoScrollGridView mGrid;
    FindOpenAdapter mAdapter;
    List<OpenGameEntity> mData = new ArrayList<>();
    private boolean isRefresh = true;
    private int mPage = 1;
    private int mType;

    public FindOpenFragment(int type) {
        mType = type;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_find_gift;
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        showTempView(TempView.LOADING);
        mAdapter = new FindOpenAdapter(getContext(), mData);
        mList.setAdapter(mAdapter);
        showTempView(TempView.LOADING);
        mGrid.setVisibility(View.GONE);
        mList.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                isRefresh = true;
                getModule(FindModule.class).getOpenServerList(mType, mPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isRefresh = false;
                getModule(FindModule.class).getOpenServerList(mType, mPage);
            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TurnHelp.turn(getContext(), mData.get(position - 1), view, mRootView);
            }
        });
        getModule(FindModule.class).getOpenServerList(mType, mPage);
    }

    private void setUpList(List<OpenGameEntity> data) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        getModule(FindModule.class).getOpenServerList(mType, mPage);
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        getModule(FindModule.class).getOpenServerList(mType, mPage);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_FIND_GIFT_LIST) {
            List<OpenGameEntity> list = (List<OpenGameEntity>) obj;
            if (!isRefresh && list.size() == 0) {
                hintTempView();
            }
            setUpList(list);
        }
    }

}

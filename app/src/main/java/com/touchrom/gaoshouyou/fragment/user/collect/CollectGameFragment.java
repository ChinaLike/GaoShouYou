package com.touchrom.gaoshouyou.fragment.user.collect;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.game_info_adapter.GameInfoAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentCollectBinding;
import com.touchrom.gaoshouyou.entity.CollectEntity;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.CollectModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/9.
 * 游戏收藏Fragment
 */
@SuppressLint("ValidFragment")
public class CollectGameFragment extends BaseFragment<FragmentCollectBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private List<GameInfoEntity> mData = new ArrayList<>();
    private GameInfoAdapter mAdapter;
    private boolean isRefresh = true;
    private int mPage = 1;

    public CollectGameFragment() {

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
                getModule(CollectModule.class).getCollectList(3, mPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isRefresh = false;
                getModule(CollectModule.class).getCollectList(3, mPage);
            }
        });
        mAdapter = new GameInfoAdapter(getContext(), mData);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TurnHelp.turnGameDetail(getContext(), mData.get(position - 1).getAppId());
            }
        });

        getModule(CollectModule.class).getCollectList(3, mPage);
    }

    private void setUpList(List<GameInfoEntity> data) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        mActivity.finish();
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        getModule(CollectModule.class).getCollectList(3, mPage);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.COLLECT) {
            CollectEntity entity = (CollectEntity) obj;
            setUpList(entity.getGame());
            if (!isRefresh && entity.getGame().size() == 0) {
                hintTempView();
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_collect;
    }
}

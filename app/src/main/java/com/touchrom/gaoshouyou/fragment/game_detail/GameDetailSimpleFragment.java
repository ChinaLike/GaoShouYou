package com.touchrom.gaoshouyou.fragment.game_detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.GameDetailActivity;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameDetailListBinding;
import com.touchrom.gaoshouyou.entity.RaiderEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.GameDetailModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/4.
 * 游戏详情界面 攻略
 */
@SuppressLint("ValidFragment")
public class GameDetailSimpleFragment extends BaseFragment<FragmentGameDetailListBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private int mGameId;
    private int mPage = 1;
    private List<RaiderEntity> mData = new ArrayList<>();
    private SimpleAdapter<RaiderEntity> mAdapter;
    private int mType;
    private boolean isFirst = true;

    public static GameDetailSimpleFragment newInstance(int gameId, int type) {
        return new GameDetailSimpleFragment(gameId, type);
    }

    private GameDetailSimpleFragment(int gameId, int type) {
        mGameId = gameId;
        mType = type;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        showTempView(TempView.LOADING);
        mList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isFirst = false;
                getModule(GameDetailModule.class).getGameDetailRaider(mGameId, mType, mPage);
            }
        });
        mAdapter = new SimpleAdapter<RaiderEntity>(getContext(), mData, R.layout.item_game_detail_raider) {
            @Override
            public void convert(SimpleViewHolder helper, RaiderEntity item) {
                helper.setText(R.id.title, item.getTitle());
                helper.setText(R.id.content, item.getContent());
            }
        };
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mData.size()) {
                    return;
                }
                RaiderEntity entity = mData.get(position + 1);
                TurnHelp.turnArticle(getContext(), entity.getTypeId(), entity.getRaidersId());
            }
        });
        mList.setAdapter(mAdapter);
        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mList.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        ((GameDetailActivity) mActivity).setTopState(mList, true);
                    } else {
                        ((GameDetailActivity) mActivity).setTopState(mList, false);
                    }
                } else {
                    ((GameDetailActivity) mActivity).setTopState(mList, false);
                }
            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == mData.size() - 1) {
//                    return;
//                }
                RaiderEntity entity = mData.get(position - 1);
                TurnHelp.turnArticle(getContext(), entity.getTypeId(), entity.getRaidersId());
            }
        });
        getModule(GameDetailModule.class).getGameDetailRaider(mGameId, mType, mPage);
    }

    private void setUpList(List<RaiderEntity> list) {
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_detail_list;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_GAME_DETAIL_RAIDER) {
            List<RaiderEntity> list = (List<RaiderEntity>) obj;
            setUpList(list);
            if (!isFirst && list.size() == 0) {
                hintTempView();
            }
        }
    }
}

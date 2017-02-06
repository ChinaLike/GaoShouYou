package com.touchrom.gaoshouyou.fragment.game_detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.GameDetailActivity;
import com.touchrom.gaoshouyou.adapter.GameDetailGiftAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameDetailListBinding;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.GameDetailModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/4.
 * 游戏详情界面 礼包
 */
@SuppressLint("ValidFragment")
public class GameDetailGiftFragment extends BaseFragment<FragmentGameDetailListBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private GameDetailGiftFragment mFragments;
    private int mGameId;
    private int mPage = 1;
    private List<GiftEntity> mData = new ArrayList<>();
    private GameDetailGiftAdapter mAdapter;
    private boolean isFirst = true;

    public static GameDetailGiftFragment newInstance(int gameId) {
        return new GameDetailGiftFragment(gameId);
    }

    private GameDetailGiftFragment(int gameId) {
        mGameId = gameId;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        mList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isFirst = false;
                getModule(GameDetailModule.class).getGameDetailGift(mGameId, mPage);
            }
        });
        mAdapter = new GameDetailGiftAdapter(getContext(), mData);
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
//                if (position - 1 == mData.size()) {
//                    return;
//                }
                TurnHelp.turnGift(getContext(), mData.get(position - 1).getGiftId());
            }
        });
        getModule(GameDetailModule.class).getGameDetailGift(mGameId, mPage);
    }

    private void setUpList(List<GiftEntity> list) {
        mData.addAll(list);
        mList.onRefreshComplete();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_detail_list;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_GAME_DETAIL_GIFT) {
            List<GiftEntity> list = (List<GiftEntity>) obj;
            if (!isFirst && list.size() == 0) {
                hintTempView();
            }
            setUpList(list);
        }
    }
}

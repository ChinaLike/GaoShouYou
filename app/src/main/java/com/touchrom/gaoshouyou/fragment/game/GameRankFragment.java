package com.touchrom.gaoshouyou.fragment.game;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.lyy.ui.pulltorefresh.x.XListView;
import com.lyy.ui.widget.NoScrollGridView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.RankClassifyAdapter;
import com.touchrom.gaoshouyou.adapter.game_info_adapter.GameInfoAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameRankBinding;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.entity.TagEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.GameModule;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/9.
 * 游戏排行类别
 */
@SuppressLint("ValidFragment")
public class GameRankFragment extends BaseFragment<FragmentGameRankBinding> implements XListView.IXListViewListener {

    private static volatile GameRankFragment mFragment = null;
    private static final Object LOCK = new Object();
    @InjectView(R.id.rank_list)
    XListView mList;
    @InjectView(R.id.grid)
    NoScrollGridView mGrid;
    private List<GameInfoEntity> mRankData = new ArrayList<>();
    private GameInfoAdapter mAdapter;
    private int mCurrentClassifyId = 4;

    public static GameRankFragment getInstance() {
        if (mFragment == null) {
            synchronized (LOCK) {
                mFragment = new GameRankFragment();
            }
        }
        return mFragment;
    }

    private GameRankFragment() {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_rank;
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        mAdapter = new GameInfoAdapter(getContext(), mRankData);
        mList.setAdapter(mAdapter);
        mList.setXListViewListener(this);
        mList.setPullLoadEnable(false);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 == mRankData.size()) {
                    return;
                }
                TurnHelp.turn(getContext(), mRankData.get(position - 1), view, mActivity.getRootView());
            }
        });
        getModule(GameModule.class).getGameRankClassify();
        showTempView(TempView.LOADING);
    }

    /**
     * 设置游戏分类信息
     */
    private void setupGameClassify(final List<TagEntity> rankClassifyData) {
        final RankClassifyAdapter adapter = new RankClassifyAdapter(getContext(), rankClassifyData);
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectItem(position);
                updateData(rankClassifyData.get(position).getClassifyId());
            }
        });
        getModule(GameModule.class).getGameRankData(mCurrentClassifyId);
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        getModule(GameModule.class).getGameRankClassify();
    }

    /**
     * 设置列表数据
     */
    private void setupList(List<GameInfoEntity> data) {
        if (data.size() != 0) {
            mRankData.clear();
            mRankData.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.stopRefresh();
            }
        }, 1000);
    }

    /**
     * 重新更新数据
     *
     * @param rankId 排行榜Id
     */
    private void updateData(final int rankId) {
        mCurrentClassifyId = rankId;
        mList.smoothScrollToPosition(0);
        mList.stopRefresh();
        mList.autoRefresh();
        getModule(GameModule.class).getGameRankData(rankId);
    }

    @Override
    public void onRefresh() {
        getModule(GameModule.class).getGameRankData(mCurrentClassifyId);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (obj == null || ((obj instanceof Integer) && (int) obj == ServiceUtil.ERROR)) {
            return;
        }
        if (result == ResultCode.SERVER.GET_GAME_RANK_CLASSIFY) {
            setupGameClassify((List<TagEntity>) obj);
        } else if (result == ResultCode.SERVER.GET_GAME_RANK_DATA) {
            setupList((List<GameInfoEntity>) obj);
        }
    }
}

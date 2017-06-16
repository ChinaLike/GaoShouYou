package com.touchrom.gaoshouyou.fragment.game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.game_info_adapter.GameInfoAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameTheNewBinding;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.GameModule;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.util.S;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/9.
 * 最新
 */
@SuppressLint("ValidFragment")
public class GameTheNewFragment extends BaseFragment<FragmentGameTheNewBinding> implements PullToRefreshListView.OnRefreshListener2 {
    @InjectView(R.id.the_new_list)
    PullToRefreshListView mList;
    private static volatile GameTheNewFragment mFragment = null;
    private static final Object LOCK = new Object();
    private List<GameInfoEntity> mData = new ArrayList<>();
    private GameInfoAdapter mAdapter;
    private int mPage = 1;
    private boolean isRefresh = true;

    public static GameTheNewFragment getInstance() {
        if (mFragment == null) {
            synchronized (LOCK) {
                mFragment = new GameTheNewFragment();
            }
        }
        return mFragment;
    }

    private GameTheNewFragment() {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_the_new;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        initWidget();
//        getModule(GameModule.class).getSelectedData(2, mPage);
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        initWidget();
        getModule(GameModule.class).getSelectedData(2, mPage);
        showTempView(TempView.LOADING);
    }

    private void initWidget() {
        mAdapter = new GameInfoAdapter(getContext(), mData);
        mList.setAdapter(mAdapter);
        mList.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setOnRefreshListener(this);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 == mData.size()) {
                    return;
                }
                TurnHelp.turn(getContext(), mData.get(position - 1), view, mActivity.getRootView());
            }
        });
    }

    private void setupList(List<GameInfoEntity> data) {
        mList.onRefreshComplete();
        if (data.size() != 0) {
            if (isRefresh) {
                mData.clear();
            }
            mData.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        isRefresh = true;
        mPage = 1;
        getModule(GameModule.class).getSelectedData(2, mPage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        isRefresh = false;
        mPage++;
        getModule(GameModule.class).getSelectedData(2, mPage);
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        mList.demo();
        isRefresh = true;
        mPage = 1;
        getModule(GameModule.class).getSelectedData(2, mPage);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (obj == null || (obj instanceof Integer) && (int) obj == ServiceUtil.ERROR) {
            return;
        }
        if (!isRefresh && (obj instanceof List) && ((List) obj).size() == 0) {
            hintTempView();
            S.i(mRootView, "没有更多数据了");
        }
        if (result == ResultCode.SERVER.GET_MAIN_GAME_DATA) {
            setupList((List<GameInfoEntity>) obj);
        }
    }


}

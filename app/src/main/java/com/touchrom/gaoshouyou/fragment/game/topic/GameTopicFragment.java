package com.touchrom.gaoshouyou.fragment.game.topic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.GameFactoryActivity;
import com.touchrom.gaoshouyou.adapter.GameTopicAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameTopicBinding;
import com.touchrom.gaoshouyou.entity.TopicEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.GameModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/2/26.
 * 游戏专题
 */
@SuppressLint("ValidFragment")
public class GameTopicFragment extends BaseFragment<FragmentGameTopicBinding> implements PullToRefreshListView.OnRefreshListener {
    private static volatile GameTopicFragment mFragment = null;
    private static final Object LOCK = new Object();

    @InjectView(R.id.list)
    PullToRefreshListView mList;

    private GameTopicAdapter mAdapter;
    private List<TopicEntity> mData = new ArrayList<>();
    private boolean isRefresh = false;

    public static GameTopicFragment getInstance() {
        if (mFragment == null) {
            synchronized (LOCK) {
                mFragment = new GameTopicFragment();
            }
        }
        return mFragment;
    }

    private GameTopicFragment() {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        mAdapter = new GameTopicAdapter(getContext(), mData);
        mList.setAdapter(mAdapter);
        mList.setOnRefreshListener(this);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (position == 5) {
                    intent = new Intent(getContext(), GameFactoryActivity.class);
                    intent.putExtra("factoryId", mData.get(position - 1).getArticleId());
                    startActivity(intent);
                } else {
                    TurnHelp.turnArticle(getContext(), 6, mData.get(position - 1).getArticleId());
                }

            }
        });
        getModule(GameModule.class).getGameTopicData();
        showTempView(TempView.LOADING);
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        mList.demo();
        isRefresh = true;
        getModule(GameModule.class).getGameTopicData();
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        isRefresh = true;
        getModule(GameModule.class).getGameTopicData();
    }

    private void setupList(List<TopicEntity> list) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_topic;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_GAME_TOPIC) {
            setupList((List<TopicEntity>) obj);
        }
    }
}

package com.touchrom.gaoshouyou.fragment.game.topic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.arialyy.frame.util.show.L;
import com.touchrom.gaoshouyou.activity.TopicDetailActivity;
import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshGridView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameTopicClassifyDetailBinding;
import com.touchrom.gaoshouyou.entity.SimpleEntity;
import com.touchrom.gaoshouyou.entity.TopicInfoEntity;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.module.GameModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/2/26.
 * 分类详情
 */
@SuppressLint("ValidFragment")
public class GameTopicClassifyDetailFragment extends BaseFragment<FragmentGameTopicClassifyDetailBinding> implements
        PullToRefreshGridView.OnRefreshListener2 {
    @InjectView(R.id.grid)
    PullToRefreshGridView mGrid;
    private int mTopicClassifyId = -1;
    private List<TopicInfoEntity> mData = new ArrayList<>();
    private boolean isRefresh = false;
    private int mPage = 1;
    private SimpleAdapter<TopicInfoEntity> mAdapter;

    public static GameTopicClassifyDetailFragment newInstance(int topicClassifyId) {
        return new GameTopicClassifyDetailFragment(topicClassifyId);
    }

    private GameTopicClassifyDetailFragment(int topicClassifyId) {
        if (topicClassifyId == -1) {
            L.e(TAG, "请传入正确的专题分类Id");
            return;
        }
        mTopicClassifyId = topicClassifyId;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        showTempView(TempView.LOADING);
        mGrid.setMode(PullToRefreshBase.Mode.BOTH);
        mAdapter = new SimpleAdapter<TopicInfoEntity>(getContext(), mData, R.layout.item_game_topic_classify_detail) {
            @Override
            public void convert(SimpleViewHolder helper, TopicInfoEntity item) {
                ImageView img = helper.getView(R.id.img);
                ImgHelp.setImg(getContext(), item.getImgUrl(), img);
                helper.setText(R.id.text, item.getTopicTitle());
            }
        };
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopicInfoEntity entity = mData.get(position);
                Intent intent = new Intent(mActivity, TopicDetailActivity.class);
                intent.putExtra("topicId", entity.getArtId());
                mActivity.startActivity(intent);
            }
        });
        mGrid.setAdapter(mAdapter);
        getModule(GameModule.class).getGameTopicClassifyDetailData(mTopicClassifyId, mPage);
    }

    private void setUpGrid(List<TopicInfoEntity> data) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mGrid.onRefreshComplete();
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        mPage = 1;
        isRefresh = true;
        getModule(GameModule.class).getGameTopicClassifyDetailData(mTopicClassifyId, mPage);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_topic_classify_detail;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mPage = 1;
        isRefresh = true;
        getModule(GameModule.class).getGameTopicClassifyDetailData(mTopicClassifyId, mPage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mPage++;
        isRefresh = false;
        getModule(GameModule.class).getGameTopicClassifyDetailData(mTopicClassifyId, mPage);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_GAME_TOPIC_CLASSIFY_DETAIL) {
            setUpGrid((List<TopicInfoEntity>) obj);
        }
    }
}

package com.touchrom.gaoshouyou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arialyy.frame.util.show.L;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.GameTopicArtAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityTopicAllArtBinding;
import com.touchrom.gaoshouyou.entity.ArticleEntity;
import com.touchrom.gaoshouyou.entity.NewsEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.NewsModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/4/7.
 * 专题所有文章
 */
public class TopicAllArtActivity extends BaseActivity<ActivityTopicAllArtBinding> {
    private int mArtId;
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private List<NewsEntity> mData = new ArrayList<>();
    private GameTopicArtAdapter mAdapter;
    private boolean isRefresh = true;
    private int mPage = 1;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_topic_all_art;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mArtId = getIntent().getIntExtra("artId", -1);
        if (mArtId == -1) {
            L.e(TAG, "请传入正确的专题Id");
            finish();
            return;
        }
        mToolbar.setTitle("所有文章");
        showTempView(TempView.LOADING);
        mList.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                isRefresh = true;
                getModule(NewsModule.class).getTopicArtList(mArtId, mPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isRefresh = false;
                getModule(NewsModule.class).getTopicArtList(mArtId, mPage);
            }
        });
        mAdapter = new GameTopicArtAdapter(this, mData);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TurnHelp.turn(TopicAllArtActivity.this, mData.get(position), view, mRootView);
            }
        });
        getModule(NewsModule.class).getTopicArtList(mArtId, mPage);
    }

    private void setUpList(List<NewsEntity> data) {
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
        getModule(NewsModule.class).getTopicArtList(mArtId, mPage);
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        finish();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_TOPIC_ALL_ART) {
            List<NewsEntity> list = (List<NewsEntity>) obj;
            if (!isRefresh && list.size() == 0) {
                hintTempView();
            }
            setUpList(list);
        }
    }
}

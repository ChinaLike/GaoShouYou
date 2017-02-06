package com.touchrom.gaoshouyou.fragment.news;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.touchrom.gaoshouyou.base.adapter.AbsRVHolder;
import com.lyy.ui.pulltorefresh.xrecyclerview.ProgressStyle;
import com.lyy.ui.pulltorefresh.xrecyclerview.XRecyclerView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.NewsSelectedAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentNewsSelectedBinding;
import com.touchrom.gaoshouyou.entity.NewsEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.NewsModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/1.
 * 资讯精选
 */
@SuppressLint("ValidFragment")
public class NewsSelectedFragment extends BaseFragment<FragmentNewsSelectedBinding> {
    //    PullToRefreshListView mList;
    @InjectView(R.id.list)
    XRecyclerView mList;
    private static volatile NewsSelectedFragment mFragment = null;
    private static final Object LOCK = new Object();

    private NewsSelectedAdapter mAdapter;
    private List<NewsEntity> mData = new ArrayList<>();
    private boolean isRefresh = false;
    private int mPage = 1;

    public static NewsSelectedFragment getInstance() {
        if (mFragment == null) {
            synchronized (LOCK) {
                mFragment = new NewsSelectedFragment();
            }
        }
        return mFragment;
    }

    private NewsSelectedFragment() {
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_news_selected;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        showTempView(TempView.LOADING);
        mAdapter = new NewsSelectedAdapter(getContext(), mData, getChildFragmentManager());
        mList.setAdapter(mAdapter);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setLoadingMoreEnabled(true);
        mList.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                mPage = 1;
                getModule(NewsModule.class).getNewsArticleList(0, 0, mPage);
                mList.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                isRefresh = false;
                mPage++;
                getModule(NewsModule.class).getNewsArticleList(0, 0, mPage);
                mList.loadMoreComplete();
            }
        });
        mAdapter.setItemClickListener(new AbsRVHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View parent, int position) {
                if (position - 1 == mData.size()) {
                    return;
                }
                TurnHelp.turn(getContext(), mData.get(position - 1), parent, mRootView);
            }
        });
        getModule(NewsModule.class).getNewsArticleList(0, 0, mPage);
    }

    private void setUpData(List<NewsEntity> data) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_NEWS_ARTICLE_LIST) {
            List<NewsEntity> list = (List<NewsEntity>) obj;
            setUpData(list);
            if (!isRefresh && list.size() == 0) {
                hintTempView();
            }
        }
    }
}

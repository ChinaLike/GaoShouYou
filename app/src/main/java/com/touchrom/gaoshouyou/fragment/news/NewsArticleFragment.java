package com.touchrom.gaoshouyou.fragment.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.touchrom.gaoshouyou.base.adapter.AbsRVAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsRVHolder;
import com.lyy.ui.pulltorefresh.xrecyclerview.XRecyclerView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.NewsSelectedAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentNewsRaidersBinding;
import com.touchrom.gaoshouyou.entity.NewsEntity;
import com.touchrom.gaoshouyou.entity.TagEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.NewsModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/4.
 * 资讯文章列表
 */
@SuppressLint("ValidFragment")
public class NewsArticleFragment extends BaseFragment<FragmentNewsRaidersBinding> {
    @InjectView(R.id.tag_list)
    RecyclerView mTags;
    @InjectView(R.id.list)
    XRecyclerView mList;
    private List<NewsEntity> mData = new ArrayList<>();
    private int mClassifyId;
    private int mPage = 1;
    private boolean isRefresh = false;
    private boolean isChange = false;
    private NewsSelectedAdapter mContentAdapter;
    private int mTagId = 0;

    public static NewsArticleFragment newInstance(int classifyId) {
        NewsArticleFragment fragment = new NewsArticleFragment(classifyId);
        return fragment;
    }

    private NewsArticleFragment() {

    }

    private NewsArticleFragment(int classifyId) {
        mClassifyId = classifyId;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_news_raiders;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        showTempView(TempView.LOADING);
        getModule(NewsModule.class).getRaidersTags(mClassifyId);
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        mTagId = 0;
        isChange = true;
        mList.getLayoutManager().scrollToPosition(0);
        ((TagAdapter) mTags.getAdapter()).selectItem(0);
        refresh();
        hintTempView();
    }

    private void setUpContentList(List<NewsEntity> data) {
        if (isRefresh && !isChange) {
            mData.clear();
        }
        mData.addAll(data);
        mContentAdapter.notifyDataSetChanged();
        dismissLoadingDialog();
    }

    private void setUpTagList(final List<TagEntity> data) {
        final TagAdapter adapter = new TagAdapter(getContext(), data);
        final LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mTags.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mTags.setAdapter(adapter);
        mTagId = data.get(0).getClassifyId();
        adapter.setItemClickListener(new AbsRVHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View parent, int position) {
                adapter.selectItem(position);
                mTagId = data.get(position).getClassifyId();
                isChange = true;
                showLoadingDialog();
                llm.scrollToPosition(0);
                refresh();
            }
        });
        mContentAdapter = new NewsSelectedAdapter(getContext(), mData, getChildFragmentManager());
        mList.setLayoutManager(llm);
        mList.setAdapter(mContentAdapter);
        mList.setLoadingMoreEnabled(true);
        mList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isChange = false;
                refresh();
                mList.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                isRefresh = false;
                isChange = false;
                getModule(NewsModule.class).getNewsArticleList(mClassifyId, mTagId, mPage);
                mList.loadMoreComplete();
            }
        });
        mContentAdapter.setItemClickListener(new AbsRVHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View parent, int position) {
                if (position - 1 == mData.size()) {
                    return;
                }
                TurnHelp.turn(getContext(), mData.get(position - 1), parent, mRootView);
            }
        });
        refresh();
    }

    private void refresh() {
        mPage = 1;
        isRefresh = true;
        getModule(NewsModule.class).getNewsArticleList(mClassifyId, mTagId, mPage);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_RAIDERS_TAG) {
            setUpTagList((List<TagEntity>) obj);
        } else if (result == ResultCode.SERVER.GET_NEWS_ARTICLE_LIST) {
            List<NewsEntity> list = (List<NewsEntity>) obj;
            setUpContentList(list);
            if (!isRefresh && list.size() == 0) {
                hintTempView();
            }
        }
    }

    class TagAdapter extends AbsRVAdapter<TagEntity, TagAdapter.TagHolder> {
        private SparseBooleanArray mSelectState = new SparseBooleanArray();

        public TagAdapter(Context context, List<TagEntity> data) {
            super(context, data);
            mSelectState.append(0, true);
            int size = mData.size();
            for (int i = 1; i < size; i++) {
                mSelectState.append(i, false);
            }
        }

        /**
         * 设置Item选择状态
         *
         * @param position 被选中项
         */
        public void selectItem(int position) {
            int size = mData.size();
            for (int i = 0; i < size; i++) {
                mSelectState.put(i, position == i);
            }
            notifyDataSetChanged();
        }

        @Override
        protected TagHolder getViewHolder(View convertView, int viewType) {
            return new TagHolder(convertView);
        }

        @Override
        protected int setLayoutId(int type) {
            return R.layout.item_rank_classify;
        }

        @Override
        protected void bindData(TagHolder holder, int position, TagEntity item) {
            holder.text.setText(item.getClassifyName());
            holder.text.setSelected(mSelectState.get(position));
        }

        class TagHolder extends AbsRVHolder {
            @InjectView(R.id.text)
            TextView text;

            public TagHolder(View itemView) {
                super(itemView);
            }
        }
    }
}

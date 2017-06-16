package com.touchrom.gaoshouyou.fragment.game_detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.GameDetailActivity;
import com.touchrom.gaoshouyou.activity.ReplyActivity;
import com.touchrom.gaoshouyou.adapter.CommentAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameDetailListBinding;
import com.touchrom.gaoshouyou.entity.CommentEntity;
import com.touchrom.gaoshouyou.module.CommentModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/14.
 * 评论fragment
 */
@SuppressLint("ValidFragment")
public class GameDetailCommentFragment extends BaseFragment<FragmentGameDetailListBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private int mGameId;
    private int mPage = 1;
    private CommentAdapter mAdapter;
    private List<CommentEntity> mData = new ArrayList<>();
    private boolean isFirst = true;

    public static GameDetailCommentFragment newInstance(int gameId) {
        return new GameDetailCommentFragment(gameId);
    }

    private GameDetailCommentFragment(int gameId) {
        mGameId = gameId;
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        showTempView(TempView.LOADING);
        mAdapter = new CommentAdapter(getContext(), mData, 1);
        mList.setAdapter(mAdapter);
        mList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isFirst = false;
                mPage++;
                getModule(CommentModule.class).getComment(mGameId, 0, mPage);
            }
        });
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
                Intent intent = new Intent(getContext(), ReplyActivity.class);
                intent.putExtra("cmtId", mData.get(position - 1).getCmtId());
                mActivity.startActivityForResult(intent, ResultCode.ACTIVITY.GD_COMMENT);
            }
        });
        getModule(CommentModule.class).getComment(mGameId, 0, mPage);
    }

    public void notifyData() {
        mData.clear();
        mPage = 1;
        getModule(CommentModule.class).getComment(mGameId, 0, mPage);
    }

    private void setUpList(List<CommentEntity> data) {
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_detail_comment;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_COMMENT) {
            List<CommentEntity> list = (List<CommentEntity>) obj;
            setUpList(list);
            if (!isFirst && list.size() == 0) {
                hintTempView();
            }
        }
    }
}

package com.touchrom.gaoshouyou.fragment.user.comment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.ReplyCommentAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentCollectBinding;
import com.touchrom.gaoshouyou.entity.ReplyCommentEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.CommentModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/9.
 * 我的评论
 */
@SuppressLint("ValidFragment")
public class ReplyMyCommentFragment extends BaseFragment<FragmentCollectBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private List<ReplyCommentEntity> mData = new ArrayList<>();
    private ReplyCommentAdapter mAdapter;
    private boolean isRefresh = true;
    private int mPage = 1;

    public ReplyMyCommentFragment() {

    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        showTempView(TempView.LOADING);
        mList.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                isRefresh = true;
                getModule(CommentModule.class).reComment(mPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isRefresh = false;
                getModule(CommentModule.class).reComment(mPage);
            }
        });
        mAdapter = new ReplyCommentAdapter(getContext(), mData);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TurnHelp.turn(getContext(), mData.get(position), view, mRootView);
            }
        });

        getModule(CommentModule.class).reComment(mPage);
    }

    private void setUpList(List<ReplyCommentEntity> data) {
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
        getModule(CommentModule.class).reComment(mPage);
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        mActivity.finish();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.RE_COMMENT) {
            List<ReplyCommentEntity> list = (List<ReplyCommentEntity>) obj;
            if (!isRefresh && list.size() == 0) {
                hintTempView();
            }
            setUpList(list);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_collect;
    }
}

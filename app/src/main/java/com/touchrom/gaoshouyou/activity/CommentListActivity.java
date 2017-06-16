package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arialyy.frame.util.show.L;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.CommentAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityCommentListBinding;
import com.touchrom.gaoshouyou.entity.CommentEntity;
import com.touchrom.gaoshouyou.module.CommentModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/15.
 * 评论列表
 */
public class CommentListActivity extends BaseActivity<ActivityCommentListBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private int mPage = 1;
    private CommentAdapter mAdapter;
    private List<CommentEntity> mData = new ArrayList<>();
    private int mAppId = -1, mTypeId = -1;
    private boolean isFirst = true;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_comment_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mAppId = getIntent().getIntExtra("appId", -1);
        mTypeId = getIntent().getIntExtra("typeId", -1);
        if (mAppId == -1 || mTypeId == -1) {
            L.e(TAG, "appId或typeId 错误");
            finish();
            return;
        }

        mToolbar.setTitle("所有评论");
        showTempView(TempView.LOADING);
        mAdapter = new CommentAdapter(this, mData, 1);
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
                getModule(CommentModule.class).getComment(mAppId, mTypeId, mPage);
            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CommentListActivity.this, ReplyActivity.class);
                intent.putExtra("cmtId", mData.get(position - 1).getCmtId());
                startActivityForResult(intent, ResultCode.ACTIVITY.GD_COMMENT);
            }
        });
        getModule(CommentModule.class).getComment(mAppId, mTypeId, mPage);
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        finish();
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        mData.clear();
        mPage = 1;
        getModule(CommentModule.class).getComment(mAppId, mTypeId, mPage);
    }

    private void setUpList(List<CommentEntity> data) {
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultCode.ACTIVITY.GD_COMMENT) {
            mData.clear();
            mPage = 1;
            getModule(CommentModule.class).getComment(mAppId, mTypeId, mPage);
        }
    }
}

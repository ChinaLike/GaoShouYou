package com.touchrom.gaoshouyou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.arialyy.frame.util.KeyBoardUtils;
import com.lyy.ui.group.TagFlowLayout;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.lyy.ui.widget.IconEditText;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.game_info_adapter.GameInfoAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivitySearchBinding;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.SearchModule;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.util.S;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/1/18.
 * 搜索界面
 */
public class SearchActivity extends BaseActivity<ActivitySearchBinding> {
    @InjectView(R.id.search_et)
    IconEditText mEt;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    @InjectView(R.id.hot_key)
    TagFlowLayout mTag;
    @InjectView(R.id.temp)
    FrameLayout mTemp;
    private List<GameInfoEntity> mData = new ArrayList<>();
    private GameInfoAdapter mAdapter;
    private boolean isLoadMore = false;
    private int mPage = 1;


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        showTempView(TempView.LOADING);
        initWidget();
        getModule(SearchModule.class).getSearchHotWord();
    }

    private void initWidget() {
        mAdapter = new GameInfoAdapter(this, mData);
        mList.setAdapter(mAdapter);
        mList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = true;
                mPage++;
                showLoadingDialog();
                search(mEt.getText().toString());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mList.onRefreshComplete();

                    }
                }, 1000);
            }
        });
        mTag.setOnChildClickListener(new TagFlowLayout.OnChildClickListener() {
            @Override
            public void onChildClick(TagFlowLayout parent, TextView child, int position) {
                showLoadingDialog();
                search(child.getText().toString());
                dismissLoadingDialog();
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 == mData.size()) {
                    return;
                }
                TurnHelp.turn(SearchActivity.this, mData.get(position - 1), view, getRootView());
            }
        });

        mEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    search(mEt.getText().toString());
                    return true;
                }
                return false;
            }
        });

        int drawable = R.drawable.skin_selector_search_tag_bg;
        int textDrawable = R.drawable.skin_selector_search_tag_text_bg;
//        if (SkinManager.getInstance().getPrefUtil().getSuffix().equals("dark")) {
//            drawable = R.drawable.skin_selector_search_tag_bg_dark;
//            textDrawable = R.drawable.skin_selector_search_tag_text_bg_dark;
//        }
        mTag.setDrawable(drawable);
        mTag.setTextSelector(textDrawable);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
//                finish();
                onBackPressed();
                break;
            case R.id.search:
                search(mEt.getText().toString());
                break;
        }
    }

    /**
     * 执行搜索
     */
    private void search(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        showTempView(TempView.LOADING);
        KeyBoardUtils.closeKeybord(mEt, this);
        getModule(SearchModule.class).getSearchData(key, mPage);
    }

    private void setupList(List<GameInfoEntity> data) {
        dismissLoadingDialog();
        if (data.size() == 0) {
            if (!isLoadMore) {
                mList.setVisibility(View.GONE);
                mList.getRefreshableView().setVisibility(View.GONE);
                mTitle.setVisibility(View.GONE);
                mTag.setVisibility(View.GONE);
                mTemp.setVisibility(View.VISIBLE);
            } else {
                S.i(mRootView, "没有更多数据了");
            }
        } else {
            mList.setVisibility(View.VISIBLE);
            mList.getRefreshableView().setVisibility(View.VISIBLE);
            mTag.setVisibility(View.GONE);
            mTitle.setVisibility(View.VISIBLE);
            mTemp.setVisibility(View.GONE);
            if (!isLoadMore) {
                mData.clear();
            }
            mData.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
        isLoadMore = false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void dataCallback(int result, Object data) {
//        super.dataCallback(result, data);
        if (data instanceof Integer && (int) data == ServiceUtil.ERROR) {
            showTempView(TempView.ERROR);
            return;
        }
        if (result == ResultCode.SERVER.GET_SEARCH_HOT_WORD) {
            mTag.setTags((String[]) data);
        } else if (result == ResultCode.SERVER.GET_SEARCH_DATA) {
            setupList((List<GameInfoEntity>) data);
        } else if (result == 0xaaa1) {
            mTitle.setText("共搜索到" + data + "条结果");
        }
    }
}

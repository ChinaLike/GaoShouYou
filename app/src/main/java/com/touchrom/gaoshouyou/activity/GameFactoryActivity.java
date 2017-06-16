package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arialyy.frame.util.show.L;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.game_info_adapter.GameInfoAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityGameFactoryBinding;
import com.touchrom.gaoshouyou.entity.GameFactoryEntity;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.GameModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/3.
 * 游戏厂商
 */
public class GameFactoryActivity extends BaseActivity<ActivityGameFactoryBinding> implements View.OnClickListener {
    @InjectView(R.id.factory_icon)
    ImageView mIcon;
    @InjectView(R.id.name)
    TextView mName;
    @InjectView(R.id.content)
    TextView mContent;
    @InjectView(R.id.content_1)
    TextView mContent1;
    @InjectView(R.id.list)
    PullToRefreshListView mList;

    private int mPage = 1;
    private int mFactoryId;
    private boolean isLoadMore = false;
    private List<GameInfoEntity> mGames = new ArrayList<>();
    private GameInfoAdapter mAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_game_factory;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mFactoryId = getIntent().getIntExtra("factoryId", -1);
        if (mFactoryId == -1) {
            L.d(TAG, "请输入正确的游戏厂商Id");
            finish();
            return;
        }
        showTempView(TempView.LOADING);

        mToolbar.setTitle("游戏厂商");
        mToolbar.setRightIcon(getResources().getDrawable(R.mipmap.icon_download));
        mToolbar.getRightIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameFactoryActivity.this, AppManagerActivity.class));
            }
        });
        mList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isLoadMore = true;
                getModule(GameModule.class).getGameFactory(mFactoryId, mPage);
            }
        });
        mAdapter = new GameInfoAdapter(this, mGames);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 == mGames.size()) {
                    return;
                }
                TurnHelp.turn(GameFactoryActivity.this, mGames.get(position - 1), view, mRootView);
            }
        });
        mContent.setOnClickListener(this);
        mContent1.setOnClickListener(this);
        getModule(GameModule.class).getGameFactory(mFactoryId, mPage);
    }

    private void setUpData(final GameFactoryEntity entity) {
        if (!isLoadMore) {
            ImgHelp.setImg(this, entity.getImgUrl(), mIcon);
            mName.setText(entity.getName());
            mContent.setText(entity.getContent());
            mContent1.setText(entity.getContent());
            mGames.addAll(entity.getGames());
        } else {
            mGames.addAll(entity.getGames());
        }
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content:
                if (mContent1.getVisibility() == View.GONE) {
                    mContent.setVisibility(View.GONE);
                    mContent1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.content_1:
                if (mContent.getVisibility() == View.GONE) {
                    mContent.setVisibility(View.VISIBLE);
                    mContent1.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_GAME_FACTORY) {
            setUpData((GameFactoryEntity) obj);
        }
    }
}

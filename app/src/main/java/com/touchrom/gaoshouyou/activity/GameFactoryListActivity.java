package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshGridView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityGameFactoryListBinding;
import com.touchrom.gaoshouyou.entity.GameClassifyEntity;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.module.GameModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/3.
 * 游戏厂商列表
 */
public class GameFactoryListActivity extends BaseActivity<ActivityGameFactoryListBinding> {
    @InjectView(R.id.grid)
    PullToRefreshGridView mGrid;

    private int mPage = 1;
    private List<GameClassifyEntity> mData = new ArrayList<>();
    private boolean isRefresh = false;
    private SimpleAdapter<GameClassifyEntity> mAdapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mToolbar.setTitle("游戏厂商");
        mToolbar.setRightIcon(getResources().getDrawable(R.mipmap.icon_download));
        mToolbar.getRightIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameFactoryListActivity.this, AppManagerActivity.class));
            }
        });
        mAdapter = new SimpleAdapter<GameClassifyEntity>(this, mData, R.layout.item_game_factory) {
            @Override
            public void convert(SimpleViewHolder helper, GameClassifyEntity item) {
                ImageView img = helper.getView(R.id.img);
                ImgHelp.setImg(helper.getContext(), item.getImgUrl(), img);
                helper.setText(R.id.title, item.getName());
                helper.setText(R.id.num, item.getNum() + "款游戏");
            }
        };
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GameFactoryListActivity.this, GameFactoryActivity.class);
                intent.putExtra("factoryId", mData.get(position).getId());
                startActivity(intent);
            }
        });
        mGrid.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                isRefresh = true;
                mPage = 1;
                getModule(GameModule.class).getGameFactoryList(mPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                isRefresh = false;
                mPage++;
                getModule(GameModule.class).getGameFactoryList(mPage);
            }
        });
        getModule(GameModule.class).getGameFactoryList(mPage);
    }

    private void setUpList(List<GameClassifyEntity> data) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mGrid.onRefreshComplete();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_game_factory_list;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_GAME_FACTORY_LIST) {
            final List<GameClassifyEntity> list = (List<GameClassifyEntity>) obj;
            setUpList(list);
        }
    }
}

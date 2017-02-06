package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.databinding.ActivityGameTopicClassifyBinding;
import com.touchrom.gaoshouyou.fragment.game.topic.GameTopicClassifyDetailFragment;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/2/26.
 * 游戏专题分类
 */
public class GameTopIcClassifyActivity extends BaseActivity<ActivityGameTopicClassifyBinding> {
    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mToolbar.setTitle("游戏专题");
        mToolbar.getRightIcon().setVisibility(View.VISIBLE);
        mToolbar.setRightIcon(getResources().getDrawable(R.mipmap.icon_download));
        mToolbar.getRightIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameTopIcClassifyActivity.this, AppManagerActivity.class));
            }
        });
        setupContentViewPager();
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager() {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(GameTopicClassifyDetailFragment.newInstance(4), "高手盘点");
        adapter.addFrag(GameTopicClassifyDetailFragment.newInstance(3), "精彩合辑");
        adapter.addFrag(GameTopicClassifyDetailFragment.newInstance(1), "特别观察");
        adapter.addFrag(GameTopicClassifyDetailFragment.newInstance(2), "独家整理");
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);    //设置Viewpager保存fragment个数
        //如果真实的栏目小于4个，则不让其滚动
        //mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(mViewPager);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_game_topic_classify;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
    }
}

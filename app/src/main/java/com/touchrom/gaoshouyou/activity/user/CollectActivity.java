package com.touchrom.gaoshouyou.activity.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.databinding.ActivityCollectBinding;
import com.touchrom.gaoshouyou.fragment.user.collect.CollectArticleFragment;
import com.touchrom.gaoshouyou.fragment.user.collect.CollectGameFragment;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/9.
 * 收藏界面
 */
public class CollectActivity extends BaseActivity<ActivityCollectBinding> {
    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mToolbar.setTitle("收藏");
        setupContentViewPager(mViewPager);
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager(ViewPager viewPager) {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CollectGameFragment(), "游戏");
        adapter.addFrag(new CollectArticleFragment(), "文章");
        viewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mTab.setupWithViewPager(viewPager);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_collect;
    }
}

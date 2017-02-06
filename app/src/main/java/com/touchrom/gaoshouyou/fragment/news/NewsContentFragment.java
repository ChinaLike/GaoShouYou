package com.touchrom.gaoshouyou.fragment.news;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.databinding.FragmentSimpleContentBinding;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/12/4.
 * 资讯界面
 */
@SuppressLint("ValidFragment")
public class NewsContentFragment extends BaseFragment<FragmentSimpleContentBinding> {
    private static volatile NewsContentFragment mFragment = null;
    private static final Object LOCK = new Object();

    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    public static NewsContentFragment getInstance() {
        if (mFragment == null) {
            synchronized (LOCK) {
                mFragment = new NewsContentFragment();
            }
        }
        return mFragment;
    }

    private NewsContentFragment() {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setupContentViewPager(mViewPager);
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager(ViewPager viewPager) {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(NewsSelectedFragment.getInstance(), "精选");
        adapter.addFrag(NewsArticleFragment.newInstance(1), "攻略");
        adapter.addFrag(NewsArticleFragment.newInstance(3), "评测");
        adapter.addFrag(NewsArticleFragment.newInstance(2), "活动");
        adapter.addFrag(NewsArticleFragment.newInstance(4), "更多");
        viewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);    //设置Viewpager保存fragment个数
        //如果真实的栏目小于4个，则不让其滚动
        //mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(viewPager);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_simple_content;
    }
}

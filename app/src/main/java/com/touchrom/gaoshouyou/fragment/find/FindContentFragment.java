package com.touchrom.gaoshouyou.fragment.find;

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
 * 游戏bar界面
 */
@SuppressLint("ValidFragment")
public class FindContentFragment extends BaseFragment<FragmentSimpleContentBinding> {

    private static final Object LOCK = new Object();
    private static volatile FindContentFragment mFragment = null;
    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    private FindContentFragment() {
    }

    public static FindContentFragment getInstance() {
        if (mFragment == null) {
            synchronized (LOCK) {
                mFragment = new FindContentFragment();
            }
        }
        return mFragment;
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
        adapter.addFrag(new FindGiftFragment(), "礼包");
        adapter.addFrag(new FindOpenFragment(1), "开服");
        adapter.addFrag(new FindOpenFragment(2), "开测");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);    //设置Viewpager保存fragment个数
        //如果真实的栏目小于4个，则不让其滚动
        //mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(viewPager);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_simple_content;
    }


}

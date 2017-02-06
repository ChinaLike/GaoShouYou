package com.touchrom.gaoshouyou.fragment.game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.databinding.FragmentSimpleContentBinding;
import com.touchrom.gaoshouyou.fragment.game.topic.GameTopicFragment;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/12/4.
 * 游戏bar界面
 */
@SuppressLint("ValidFragment")
public class GameContentFragment extends BaseFragment<FragmentSimpleContentBinding> {
    private static volatile GameContentFragment mFragment = null;
    private static final Object LOCK = new Object();

    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;


    public static GameContentFragment getInstance() {
        if (mFragment == null) {
            synchronized (LOCK) {
                mFragment = new GameContentFragment();
            }
        }
        return mFragment;
    }

    private GameContentFragment() {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        showLoadingDialog();
        initWidget();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dismissLoadingDialog();
//            }
//        }, 2000);
    }

    private void initWidget() {
        setupContentViewPager(mViewPager);
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager(ViewPager viewPager) {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(GameSelectedFragment.getInstance(), "精选");
        adapter.addFrag(GameTheNewFragment.getInstance(), "最新");
        adapter.addFrag(GameRankFragment.getInstance(), "排行");
        adapter.addFrag(GameClassifyFragment.getInstance(), "分类");
        adapter.addFrag(GameTopicFragment.getInstance(), "专题");
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

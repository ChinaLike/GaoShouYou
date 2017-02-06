package com.touchrom.gaoshouyou.activity.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.arialyy.frame.core.NotifyHelp;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.databinding.ActivityCollectBinding;
import com.touchrom.gaoshouyou.fragment.user.fans.DynamicFragment;
import com.touchrom.gaoshouyou.fragment.user.fans.FansFragment;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/9.
 * 关注
 */
public class FollowActivity extends BaseActivity<ActivityCollectBinding> {
    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    int mTurnPage = -1;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTurnPage = getIntent().getIntExtra(Constance.KEY.TURN, -1);
        mToolbar.setTitle("关注");
        setupContentViewPager(mViewPager);
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager(ViewPager viewPager) {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DynamicFragment(), "动态");
        adapter.addFrag(new FansFragment(2), "关注");
        adapter.addFrag(new FansFragment(3), "粉丝");
        viewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mTab.setupWithViewPager(viewPager);
        if (mTurnPage != -1) {
            viewPager.setCurrentItem(mTurnPage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotifyHelp.getInstance().removeObj(Constance.NOTIFY_KEY.FANS);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_collect;
    }
}

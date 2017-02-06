package com.touchrom.gaoshouyou.activity.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.databinding.ActivityCollectBinding;
import com.touchrom.gaoshouyou.fragment.user.comment.MyCommentFragment;
import com.touchrom.gaoshouyou.fragment.user.comment.ReplyMyCommentFragment;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/9.
 * 收藏界面
 */
public class UserCommentActivity extends BaseActivity<ActivityCollectBinding> {
    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    int mTurnPage = -1;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTurnPage = getIntent().getIntExtra(Constance.KEY.TURN, -1);
        mToolbar.setTitle("评论");
        setupContentViewPager(mViewPager);
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager(ViewPager viewPager) {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MyCommentFragment(), "我的评论");
        adapter.addFrag(new ReplyMyCommentFragment(), "回复我的");
        viewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mTab.setupWithViewPager(viewPager);
        if (mTurnPage != -1) {
            viewPager.setCurrentItem(mTurnPage);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_collect;
    }
}

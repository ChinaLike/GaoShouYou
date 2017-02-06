package com.touchrom.gaoshouyou.activity.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.lyy.ui.widget.CircleImageView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityUserCenterBinding;
import com.touchrom.gaoshouyou.entity.UserEntity;
import com.touchrom.gaoshouyou.fragment.user.UserDataFragment;
import com.touchrom.gaoshouyou.fragment.user.UserLeaveMsgFragment;
import com.touchrom.gaoshouyou.fragment.user.fans.DynamicFragment;
import com.touchrom.gaoshouyou.fragment.user.fans.FansFragment;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.module.UserModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/24.
 * 用户中心实体
 */
public class UserCenterActivity extends BaseActivity<ActivityUserCenterBinding> {
    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.head_img)
    CircleImageView mImg;
    @InjectView(R.id.vp)
    ViewPager mVp;
    @InjectView(R.id.bt)
    Button mBt;
    int mUserId;
    int mTurnPage = -1;
    UserEntity mEntity;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mUserId = getIntent().getIntExtra(Constance.KEY.USER_ID, AppManager.getInstance().getUser().getUserId());
        mTurnPage = getIntent().getIntExtra(Constance.KEY.TURN, -1);
        getModule(UserModule.class).getUserCenterData(mUserId);
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager(UserEntity entity, ViewPager viewPager) {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DynamicFragment(entity.getUserId()), "动态");
        adapter.addFrag(new FansFragment(entity.getUserId(), 2), "关注");
        adapter.addFrag(new FansFragment(entity.getUserId(), 3), "粉丝");
        adapter.addFrag(new UserLeaveMsgFragment(entity.getUserId()), "留言");
        adapter.addFrag(new UserDataFragment(entity), "资料");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        mTab.setupWithViewPager(viewPager);
        if (mTurnPage != -1) {
            viewPager.setCurrentItem(mTurnPage);
        }
    }

    public void onClick(View view) {
        if (mEntity.isFollow()) {
            getModule(UserModule.class).cancelFollow(mEntity.getUserId());
        } else {
            getModule(UserModule.class).follow(mEntity.getUserId());
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_user_center;
    }

    public static String catNameAndSex(String sex, String nikeName) {
        return nikeName + " " + sex;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_USER_CENTER_DATA) {
            mEntity = (UserEntity) obj;
            mEntity.setLevel("LV" + mEntity.getLevel());
            if (mEntity.getUserId() == AppManager.getInstance().getUser().getUserId()) {
                mBt.setVisibility(View.GONE);
            }
            mToolbar.setTitle(mEntity.getNikeName() + "的个人主页");
            Glide.with(this).load(mEntity.getHeadImg()).into(mImg);
            getBinding().setEntity(mEntity);
            setupContentViewPager(mEntity, mVp);
        } else if (result == ResultCode.SERVER.FOLLOW) {
            if ((boolean) obj) {
                mEntity.setFollow(true);
                getBinding().setEntity(mEntity);
            }
        } else if (result == ResultCode.SERVER.CANCEL_FOLLOW) {
            if ((boolean) obj) {
                mEntity.setFollow(false);
                getBinding().setEntity(mEntity);
            }
        }
    }
}

package com.touchrom.gaoshouyou.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.arialyy.frame.util.DensityUtils;
import com.arialyy.frame.util.SharePreUtil;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityGameClassifyBinding;
import com.touchrom.gaoshouyou.entity.FilterPWEntity;
import com.touchrom.gaoshouyou.entity.GameClassifyScreenEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.fragment.AMApkFragment;
import com.touchrom.gaoshouyou.fragment.game.GameClassifyDetailFragment;
import com.touchrom.gaoshouyou.module.GameClassifyModule;
import com.touchrom.gaoshouyou.popupwindow.GameClassifyFilterPopupWindow;
import com.touchrom.gaoshouyou.popupwindow.GameClassifyTypePopupWindow;
import com.touchrom.gaoshouyou.service.DownloadService;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/10.
 * 游戏分类详情
 */
public class GameClassifyActivity extends BaseActivity<ActivityGameClassifyBinding> implements View.OnClickListener {

    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.type)
    TextView mType;
    @InjectView(R.id.screen)
    TextView mScreen;
    @InjectView(R.id.view_pager)
    ViewPager mPager;
    private int mClassifyId = -1;   //分类id
    private int mTypeId = 0;   //类型分类Id
    private int mScreenId1 = 0; //筛选分类Id
    private int mScreenId2 = 0; //筛选分类Id
    private int mScreenId3 = 0; //筛选分类Id
    private int mSelectedPosition = 0;  //默认选择的选项
    private Drawable mUpIcon, mDownIcon;
    private List<GameClassifyScreenEntity> mTagData = new ArrayList<>();    //悬浮框标签数据
    private List<GameClassifyDetailFragment> mFragments = new ArrayList<>();

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshDot();
        }
    };

    /**
     * 刷新小红点
     */
    public void refreshDot() {
        List<DownloadEntity> entities = DownloadEntity.findAll(DownloadEntity.class);
        int count = 0;
        for (DownloadEntity entity : entities) {
            if (entity.getState() != DownloadEntity.STATE_COMPLETE) {
                count++;
            }
        }
        if (count == 0) {
            mToolbar.getRightIcon().setImageResource(R.mipmap.icon_download);
        } else {
            mToolbar.getRightIcon().setImageResource(R.mipmap.icon_has_download_task);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AMApkFragment.ACTION_COMPLETE);
        filter.addAction(DownloadService.ACTION_STATE_CHANGE);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_game_classify;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mClassifyId = getIntent().getIntExtra(Constance.KEY.INT, -1);
        if (mClassifyId == -1) {
            throw new IllegalAccessError("请传入游戏的分类Id");
        }
        mToolbar.setTitle(getIntent().getStringExtra(Constance.KEY.SETTING));
        showLoadingDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initWidget();
                dismissLoadingDialog();
            }
        }, 1000);
    }

    private void initWidget() {
        getModule(GameClassifyModule.class).getGameClassifyTag(mClassifyId);
        setupContentViewPager(mPager);
        mType.setOnClickListener(this);
        mScreen.setOnClickListener(this);
        mUpIcon = getResources().getDrawable(R.mipmap.icon_screen_up);
        mDownIcon = getResources().getDrawable(R.mipmap.icon_screen_down);
        int iconSize = DensityUtils.dp2px(14);
        mUpIcon.setBounds(0, 0, iconSize, iconSize);
        mDownIcon.setBounds(0, 0, iconSize, iconSize);
        mType.setCompoundDrawables(null, null, mDownIcon, null);
        mScreen.setCompoundDrawables(null, null, mDownIcon, null);
//        mToolbar.setRightIcon(getResources().getDrawable(R.mipmap.icon_download));
        mToolbar.getRightIcon().setOnClickListener(this);
        refreshDot();
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager(ViewPager viewPager) {
        mFragments.add(GameClassifyDetailFragment.newInstance(mClassifyId, 1));
        mFragments.add(GameClassifyDetailFragment.newInstance(mClassifyId, 2));
        mFragments.add(GameClassifyDetailFragment.newInstance(mClassifyId, 3));
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        //id 和 标题是需要从服务器获取的
        adapter.addFrag(mFragments.get(0), "推荐");
        adapter.addFrag(mFragments.get(1), "最新");
        adapter.addFrag(mFragments.get(2), "人气");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);    //设置Viewpager保存fragment个数
        //如果真实的栏目小于4个，则不让其滚动
        //mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(viewPager);
    }

    @Override
    public void finish() {
        super.finish();
        FilterPWEntity record = SharePreUtil.getObject(Constance.APP.NAME, this, "FILTER_RECORD", FilterPWEntity.class);
        if (record != null) {
            record.setFilterId1(0);
            record.setFilterId2(0);
            record.setFilterId3(0);
            record.setPosition1(0);
            record.setPosition2(0);
            record.setPosition3(0);
            SharePreUtil.putObject(Constance.APP.NAME, this, "FILTER_RECORD", FilterPWEntity.class, record);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type:
                screenType(true);
                break;
            case R.id.screen:
                screenType(false);
                break;
            case R.id.right_icon:   //下载管理
                startActivity(new Intent(this, AppManagerActivity.class));
                break;
        }
    }

    /**
     * 筛选类型
     *
     * @param isUseType true --> 点击类型，false --> 点击筛选
     */
    private void screenType(boolean isUseType) {
        if (isUseType && !mType.isSelected()) { //展开类型对话框
            mType.setSelected(true);
            mType.setCompoundDrawables(null, null, mUpIcon, null);
            GameClassifyTypePopupWindow type = new GameClassifyTypePopupWindow(this, this, mSelectedPosition);
            type.showAsDropDown(findViewById(R.id.ll));
            type.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mType.setSelected(false);
                    mType.setCompoundDrawables(null, null, mDownIcon, null);
                }
            });
        }

        if (!isUseType && !mScreen.isSelected()) {   //展开筛选对话框
            mScreen.setSelected(true);
            mScreen.setCompoundDrawables(null, null, mUpIcon, null);
            GameClassifyFilterPopupWindow screen = new GameClassifyFilterPopupWindow(this, this, mClassifyId, mTagData);
            screen.showAsDropDown(findViewById(R.id.ll));
            screen.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mScreen.setSelected(false);
                    mScreen.setCompoundDrawables(null, null, mDownIcon, null);
                }
            });
        }
    }

    /**
     * 更新数据
     *
     * @param typeId
     * @param screenId1
     * @param screenId2
     * @param screenId3
     */
    private void update(int typeId, int screenId1, int screenId2, int screenId3) {
        showLoadingDialog();
        for (GameClassifyDetailFragment fragment : mFragments) {
            fragment.upDateData(mClassifyId, typeId, screenId1, screenId2, screenId3);
        }
        dismissLoadingDialog();
    }

    @Override
    protected void dataCallback(int result, Object data) {
        if (result == ResultCode.GAME_CLASSIFY_TYPE) {      //类型
            Bundle b = (Bundle) data;
            mTypeId = b.getInt("id");
            mSelectedPosition = b.getInt("position");
            update(mTypeId, mScreenId1, mScreenId2, mScreenId3);
        } else if (result == ResultCode.GAME_CLASSIFY_SCREEN) {   //筛选
            Bundle b = (Bundle) data;
            mScreenId1 = b.getInt("screenId1");
            mScreenId2 = b.getInt("screenId2");
            mScreenId3 = b.getInt("screenId3");
            update(mTypeId, mScreenId1, mScreenId2, mScreenId3);
        } else if (result == ResultCode.SERVER.GET_GAME_CLASSIFY_TAG) { //标签数据
            if (data != null) {
                mTagData.clear();
                mTagData.addAll((List<GameClassifyScreenEntity>) data);
            }
        }
    }
}

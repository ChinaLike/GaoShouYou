package com.touchrom.gaoshouyou.fragment.game;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arialyy.frame.util.DensityUtils;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshScrollView;
import com.lyy.ui.widget.CircleIndicator;
import com.lyy.ui.widget.NoScrollListView;
import com.lyy.ui.widget.VerticalViewPager;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.RecommendAdapter;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.adapter.game_info_adapter.GameInfoAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameSelectedBinding;
import com.touchrom.gaoshouyou.entity.BannerEntity;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.entity.RecommendEntity;
import com.touchrom.gaoshouyou.fragment.BannerFragment;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.GameModule;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/7.
 * 精选Fragment
 */
@SuppressLint("ValidFragment")
public class GameSelectedFragment extends BaseFragment<FragmentGameSelectedBinding> implements View.OnClickListener,
        PullToRefreshScrollView.OnRefreshListener<ScrollView> {

    private static volatile GameSelectedFragment mFragment = null;
    private static final Object LOCK = new Object();
    @InjectView(R.id.banner_View_pager)
    ViewPager mBannerViewPager;
    @InjectView(R.id.indicator)
    CircleIndicator mIndicator;
    @InjectView(R.id.gift_code)
    TextView mGiftCode;
    @InjectView(R.id.game_raiders)
    TextView mRaiders;
    @InjectView(R.id.game_evaluation)
    TextView mEvaluation;
    @InjectView(R.id.game_open_server)
    TextView mOpenServer;
    @InjectView(R.id.vertical_banner)
    VerticalViewPager mRecommended;
    @InjectView(R.id.selected_scrollview)
    PullToRefreshScrollView mScrollView;
    @InjectView(R.id.list)
    NoScrollListView mList;
    @InjectView(R.id.bottom_msg)
    TextView mBottomMsg;
    @InjectView(R.id.return_top)
    TextView mReTop;

    private List<GameInfoEntity> mGameData = new ArrayList<>();
    private List<Fragment> mBannerFrag = new ArrayList<>();
    private GameInfoAdapter mGameAdapter;
    private SimpleViewPagerAdapter mBannerAdapter;

    public static GameSelectedFragment getInstance() {
        if (mFragment == null) {
            synchronized (LOCK) {
                mFragment = new GameSelectedFragment();
            }
        }
        return mFragment;
    }

    public GameSelectedFragment() {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_selected;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setupBanner(mBannerViewPager);
        initWidget();
        getData();
    }
//
//    @Override
//    protected void onDelayLoad() {
//        super.onDelayLoad();
//        setupBanner(mBannerViewPager);
//        initWidget();
//        getData();
//        showTempView(TempView.LOADING);
//    }

    private void initWidget() {
//        RippleHelp.createRipple(getContext(), mGiftCode);
//        RippleHelp.createRipple(getContext(), mRaiders);
//        RippleHelp.createRipple(getContext(), mEvaluation);
//        RippleHelp.createRipple(getContext(), mOpenServer);
        mGiftCode.setOnClickListener(this);
        mRaiders.setOnClickListener(this);
        mEvaluation.setOnClickListener(this);
        mOpenServer.setOnClickListener(this);
        mScrollView.setOnRefreshListener(this);

        mGameAdapter = new GameInfoAdapter(getContext(), mGameData);
        mList.setAdapter(mGameAdapter);


        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameInfoEntity entity = mGameData.get(position);
                TurnHelp.turn(getContext(), entity, view, mActivity.getRootView());
//                TurnHelp.turnGameList(getContext(), entity);
            }
        });
        Drawable icon = getResources().getDrawable(R.mipmap.icon_refresh_center);
        int bound = DensityUtils.dp2px(20);
        icon.setBounds(0, 0, bound, bound);
        mBottomMsg.setCompoundDrawables(icon, null, null, null);
        mReTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollView.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
            }
        });
        mRecommended.setDuration(1000);
    }

    /**
     * 初始化Banner
     *
     * @param viewPager
     */
    private void setupBanner(ViewPager viewPager) {
        mBannerAdapter = new SimpleViewPagerAdapter(getActivity().getSupportFragmentManager());
        mBannerAdapter.update(mBannerFrag);
        viewPager.setAdapter(mBannerAdapter);
    }

    /**
     * 更新游戏数据
     *
     * @param gameData
     */
    private void setupList(List<GameInfoEntity> gameData) {
        if (gameData.size() != 0) {
            mGameData.clear();
            mGameData.addAll(gameData);
        }
        mGameAdapter.notifyDataSetChanged();
    }

    /**
     * 更新Banner
     */
    private void updateBanner(List<BannerEntity> bannerData) {
        if (bannerData.size() != 0) {
            mBannerFrag.clear();
            for (BannerEntity entity : bannerData) {
                BannerFragment bf = BannerFragment.newInstance(entity);
                bf.setCanClick(true);
                mBannerFrag.add(bf);
                mBannerAdapter.update(mBannerFrag);
                mIndicator.setViewPager(mBannerViewPager);
                mIndicator.onPageSelected(0);
            }
        }
    }

    /**
     * 更新推荐
     *
     * @param recommendData
     */
    private void updateRecommend(List<RecommendEntity> recommendData) {
        if (recommendData.size() != 0) {
            RecommendAdapter mRecommendAdapter = new RecommendAdapter(getChildFragmentManager(), recommendData);
            mRecommended.setAdapter(mRecommendAdapter);
        }
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        getData();
    }

    protected void getData() {
        getModule(GameModule.class).getSelectedData(1);
        getModule(GameModule.class).getGameBanner();
        getModule(GameModule.class).getRecommend();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gift_code:    //礼包代码
                break;
            case R.id.game_raiders: //攻略
                break;
            case R.id.game_evaluation:  //评测
                break;
            case R.id.game_open_server: //网游开服
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        getData();
        refreshView.onRefreshComplete();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (obj == null || ((obj instanceof Integer) && (int) obj == ServiceUtil.ERROR)) {
            return;
        }
        if (result == ResultCode.SERVER.GET_MAIN_GAME_DATA) {
            setupList((List<GameInfoEntity>) obj);
        } else if (result == ResultCode.SERVER.GET_MAIN_GAME_BANNER) {
            updateBanner((List<BannerEntity>) obj);
        } else if (result == ResultCode.SERVER.GET_MAIN_GAME_RECOMMEND) {
            updateRecommend((List<RecommendEntity>) obj);
        }
    }

}

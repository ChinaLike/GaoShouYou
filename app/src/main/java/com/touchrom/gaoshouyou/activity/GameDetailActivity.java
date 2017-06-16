package com.touchrom.gaoshouyou.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.DensityUtils;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.widget.CircleIndicator;
import com.lyy.ui.widget.StarBar;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityGameDetailBinding;
import com.touchrom.gaoshouyou.dialog.ShareDialog;
import com.touchrom.gaoshouyou.entity.BannerEntity;
import com.touchrom.gaoshouyou.entity.GameDetailEntity;
import com.touchrom.gaoshouyou.entity.TagEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.fragment.ScreenshotFragment;
import com.touchrom.gaoshouyou.fragment.game_detail.GameDetailCommentFragment;
import com.touchrom.gaoshouyou.fragment.game_detail.GameDetailGiftFragment;
import com.touchrom.gaoshouyou.fragment.game_detail.GameDetailInfoFragment;
import com.touchrom.gaoshouyou.fragment.game_detail.GameDetailSimpleFragment;
import com.touchrom.gaoshouyou.help.DownloadHelp;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.module.CollectModule;
import com.touchrom.gaoshouyou.module.GameDetailModule;
import com.touchrom.gaoshouyou.module.SettingModule;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.widget.GameDetailCommentBar;
import com.touchrom.gaoshouyou.widget.GameDetailInfoBottomBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2015/11/13.
 * 游戏详情Activity
 * 注意，为了不被状态栏挤压，需要AppBarLayout里面的控件都设置android:fitsSystemWindows="true"属性，toolbar除外
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GameDetailActivity extends BaseActivity<ActivityGameDetailBinding> {
    @InjectView(R.id.content)
    View mContent;
    @InjectView(R.id.content_vp)
    ViewPager mContentVP;
    @InjectView(R.id.img_vp)
    ViewPager mImgVP;
    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.bar_bg)
    View mBarBg;
    @InjectView(R.id.back)
    TextView mBack;
    @InjectView(R.id.game_detail_bar)
    RelativeLayout mBar;
    @InjectView(R.id.status_bar_temp)
    View mStatusBarTemp;
    @InjectView(R.id.icon)
    ImageView mIcon;
    @InjectView(R.id.name)
    TextView mName;
    @InjectView(R.id.star_bar)
    StarBar mStar;
    @InjectView(R.id.detail)
    TextView mDetail;
    @InjectView(R.id.feature)
    TextView mFeature;
    @InjectView(R.id.indicator)
    CircleIndicator mIndicator;
    @InjectView(R.id.bottom_bar)
    FrameLayout mBottomBar;
    float mOldX = 0;
    private GestureDetectorCompat mDetector;
    private int mHeadH, mBarH, mRawY, mTime = 500, mStateBarH, mNavigationBarH;
    private float a;
    private boolean isScrollTop = false;
    private float mOldY = 0;
    private int STATE_TOP = 0x01, STATE_CENTER = 0x02, STATE_BOTTOM = 0x03, STATE_OTHER = 0x04;
    private int mCurrentState = STATE_TOP;
    private int mTopL, mCenterL, mBottomL;  //顶部，中部，底部滑动的基准位置
    private int mRawTopL, mRawCenterL, mRawBottomL;  //顶部，中部，底部滑动的基准位置
    private int mBottomBarL;
    private int mBarColor;
    private int mGameId = -1;
    private GameDetailEntity mGDEntity;
    private boolean bottomBarIsShow = true;
    private int mTemp;
    private boolean rotated = false;
    private View mScrollView;
    private int mShotVpPosition = 0;
    private GameDetailInfoBottomBar mGDIBar;
    private GameDetailCommentBar mGDCBar;
    private int mScreenH;
    private int mBarId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // 设置状态栏可用
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setBarCanSetColor(false);
        super.init(savedInstanceState);
        mGameId = getIntent().getIntExtra(Constance.KEY.APP_ID, -1);
        if (mGameId == -1) {
            L.e(TAG, "请传入正确的gameId");
            finish();
            return;
        }
        mBarId = getIntent().getIntExtra("tabId", 0);
        if (getIntent().getBooleanExtra("useLoadDialog", true)) {
            showLoadingDialog();
        }
        mDetector = new GestureDetectorCompat(this, new SimpleGestureAction());
        getModule(GameDetailModule.class).getGameDetail(mGameId);
        initWidget();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initWidget() {
        mBack.setCompoundDrawablePadding(-DensityUtils.dp2px(5));
        mBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bar_transparent));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        int size = DensityUtils.dp2px(40);
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_left_back);
        assert drawable != null;
        drawable.setBounds(0, 0, size, size);
        mBack.setCompoundDrawables(drawable, null, null, null);
        initParam();
        mBarBg.setAlpha(a);
        mStatusBarTemp.setAlpha(a);
        setStateBarColor((int) (mBarColor * a));
        mContent.setTranslationY(mRawY);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mRootView.post(new Runnable() {
                    @Override
                    public void run() {
                        int sh = AndroidVersionUtil.hasKitKat() ? mStateBarH : 0;
                        mRootView.getLayoutParams().height = mRootView.getHeight() + (mHeadH - mBarH) + sh + mBarH;
                    }
                });
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mBottomBar.post(new Runnable() {
                    @Override
                    public void run() {
                        mBottomBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.tool_bar_height);
                    }
                });
            }
        });
        initBottomBar();
    }

    private void initBottomBar() {
        mGDIBar = new GameDetailInfoBottomBar(this);
        mGDIBar.setOnGDIBarListener(new GameDetailInfoBottomBar.OnGDIBarClickListener() {
            @Override
            public void onGDIBarClick(View view) {
                switch (view.getId()) {
                    case R.id.download:
                        DownloadEntity dEntity = new DownloadEntity();
                        dEntity.setImgUrl(mGDEntity.getGameIconUrl());
                        dEntity.setName(mGDEntity.getGameName());
                        dEntity.setDownloadUrl(mGDEntity.getDownloadUrl());
                        dEntity.setGameId(mGDEntity.getAppId());
                        DownloadHelp.newInstance().download(GameDetailActivity.this, dEntity);
                        break;
                    case R.id.share:
                        ShareDialog shareDialog = new ShareDialog(SettingModule.SHARE_TYPE_GAME, mGDEntity.getAppId());
                        shareDialog.show(GameDetailActivity.this.getSupportFragmentManager(), "shareDialog");
                        break;
                    case R.id.like:
                        if (!AppManager.getInstance().isLogin()) {
                            startActivity(new Intent(GameDetailActivity.this, LoginActivity.class));
                        } else {
                            if (mGDIBar.isCollect()) {
                                getModule(CollectModule.class).cancelCollect(mGDIBar.getCollectId());
                            } else {
                                getModule(CollectModule.class).addCollect(0, mGDEntity.getAppId());
                            }
                        }
                        break;
                }
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mGDIBar.setLayoutParams(lp);

        mGDCBar = new GameDetailCommentBar(this, mGameId);
        mGDCBar.setLayoutParams(lp);

        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) mBottomBar.getLayoutParams();
        int sh = AndroidVersionUtil.hasKitKat() ? mStateBarH : 0;
        lp1.setMargins(0, 0, 0, (mHeadH - mBarH) + sh);
        mBottomBar.setLayoutParams(lp1);
        mBottomBar.setTranslationY(0);

    }

    /**
     * 初始化一些常亮参数
     */
    private void initParam() {
        a = 0.0f;
        mScreenH = AndroidUtils.getScreenParams(this)[1];
        mHeadH = (int) getResources().getDimension(R.dimen.game_detail_head_height);
        mBarH = (int) getResources().getDimension(R.dimen.tool_bar_height);
        mBarColor = getMyColor(R.color.skin_primary_color);
        mTemp = (int) getResources().getDimension(R.dimen.game_detail_head_indicator_height);
        mStateBarH = AndroidUtils.getStatusBarHeight(this);
        mNavigationBarH = AndroidUtils.getNavigationBarHeight(this);
        if (AndroidVersionUtil.hasKitKat()) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBar.getLayoutParams();
            lp.setMargins(0, mStateBarH, 0, 0);
            mBar.setLayoutParams(lp);

            mStatusBarTemp.getLayoutParams().height = mStateBarH;
            mStatusBarTemp.setBackgroundColor(mBarColor);
            mStatusBarTemp.setVisibility(View.VISIBLE);

            mTopL = -mHeadH - mStateBarH;
            mCenterL = (int) (mBarH + mStateBarH + getResources().getDimension(R.dimen.game_detail_center_height) + mTemp) - mStateBarH;
            mBottomL = mScreenH - mNavigationBarH - mHeadH - mTemp - mStateBarH + DensityUtils.dp2px(3);
        } else {
            mStatusBarTemp.setVisibility(View.GONE);
            mTopL = -mHeadH;
            mCenterL = (int) (mBarH + mStateBarH + getResources().getDimension(R.dimen.game_detail_center_height) + mTemp);
            mBottomL = mScreenH - mNavigationBarH - mHeadH - mTemp + DensityUtils.dp2px(3);
        }

        mRawTopL = mBarH + mStateBarH + mTemp;
        mRawCenterL = mCenterL + mBarH + mTemp;
        mRawBottomL = mBottomL + mBarH + mTemp;
        mBottomBarL = mScreenH - mNavigationBarH;
        mCurrentState = STATE_CENTER;
        mRawY = mCenterL;
    }

    /**
     * 初始化头部
     */
    private void setupWidget(GameDetailEntity gdEntity) {
        mGDEntity = gdEntity;
        if (getSettingEntity().isShowImg()) {
            ImgHelp.setImg(this, gdEntity.getGameIconUrl(), mIcon);
        }
        mName.setText(gdEntity.getGameName());
        mStar.setScore(gdEntity.getScore());
        mDetail.setText(gdEntity.getGameType() + "  " + gdEntity.getGameSize());
        mFeature.setText(gdEntity.getTags());
        mBack.setText(gdEntity.getGameName());
        setupGameShotViewPager(mImgVP);
        mImgVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mShotVpPosition = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getModule(CollectModule.class).getCollectState(0, mGDEntity.getAppId());
        getModule(GameDetailModule.class).getGameDetailTab(mGDEntity.getAppId());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mTempView.getVisibility() == View.VISIBLE) {
            return super.dispatchTouchEvent(ev);
        }
        if (mRawY < mBarH) {
            mCurrentState = STATE_TOP;
        } else if (mBarH < mRawY && mRawY <= mRawCenterL + (mBarH >> 1)) {
            mCurrentState = STATE_CENTER;
        } else if (mRawCenterL + (mBarH >> 1) <= mRawY && mRawY < mBottomL + mBarH) {
            mCurrentState = STATE_BOTTOM;
        } else {
            mCurrentState = STATE_OTHER;
        }
        boolean isTop = mRawY == mTopL;

        if (Math.abs(ev.getX() - mOldX) >= 0 && Math.abs(ev.getY() - mOldY) < 200 && isTop) {
            mOldX = ev.getX();
            return super.dispatchTouchEvent(ev);
        }

        float t = Math.abs(ev.getY() - mOldY);
        if (isTop && (ev.getY() < mRawTopL || (ev.getY() > mBottomBarL && bottomBarIsShow) || t < 10)) {
            return super.dispatchTouchEvent(ev);
        }

        if (mCurrentState == STATE_CENTER && (ev.getY() < mRawCenterL || (ev.getY() > mBottomBarL && bottomBarIsShow && mRawY >= mCenterL))) {
            return super.dispatchTouchEvent(ev);
        }

        if (mCurrentState == STATE_BOTTOM && ev.getY() < mRawBottomL && mRawY >= mBottomL) {
            return super.dispatchTouchEvent(ev);
        }

        boolean isUp = ev.getY() - mOldY < 0;

        if (isTop && mCurrentState == STATE_TOP) {
            mOldY = (int) ev.getY();
            if (isScrollTop && !isUp) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    return super.dispatchTouchEvent(ev);
                }
                return onTouchEvent(ev);
            } else {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    return super.dispatchTouchEvent(ev);
                }
                return super.dispatchTouchEvent(ev);
            }
        }

        return onTouchEvent(ev);
    }


    public void setTopState(View view, boolean top) {
        mScrollView = view;
        isScrollTop = top;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mRawY <= -mStateBarH) {
                    toTop();
                } else if ((-mStateBarH < mRawY && mRawY <= mCenterL + (mBarH >> 1)) || mCurrentState == STATE_CENTER) {
                    toCenter();
                } else if (mCenterL + (mBarH >> 1) <= mRawY || mCurrentState == STATE_BOTTOM) {
                    toBottom();
                }
                return true;
            default:
                if (0 <= a && a <= 1.0f) {
                    mBarBg.setAlpha(a);
                    mStatusBarTemp.setAlpha(a);
                }
                mDetector.onTouchEvent(event);
                return super.onTouchEvent(event);
        }
    }


    /**
     * 回到顶部
     */
    private void toTop() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mContent, "translationY", mRawY, mTopL);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mBarBg, "alpha", a, 1.0f);
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(mStatusBarTemp, "alpha", a, 1.0f);
        set.setDuration(mTime);
        set.play(animator).with(alpha).with(alpha1);
        set.start();
        mRawY = mTopL;
        mCurrentState = STATE_TOP;
        a = 1.0f;
        mBarBg.setAlpha(a);
        mStatusBarTemp.setAlpha(a);
        showBottomBar(true);
    }

    /**
     * 回到中间
     */
    private void toCenter() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mContent, "translationY", mRawY, mCenterL);
        animator.setDuration(mTime);
        animator.start();
        mRawY = mCenterL;
        a = 0.0f;
        mBarBg.setAlpha(a);
        mStatusBarTemp.setAlpha(a);
        mCurrentState = STATE_CENTER;
        rotationBanner(false);
        showBottomBar(true);
    }

    /**
     * 到底部
     */
    private void toBottom() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mContent, "translationY", mRawY, mBottomL);
        animator.setDuration(mTime);
        animator.start();
        mRawY = mBottomL;
        a = 0.0f;
        mBarBg.setAlpha(a);
        mStatusBarTemp.setAlpha(a);
        mCurrentState = STATE_BOTTOM;
        rotationBanner(true);
        showBottomBar(false);
    }

    /**
     * 显示底部栏
     */
    public void showBottomBar(boolean isShow) {
        bottomBarIsShow = isShow;
        if (isShow) {
            mBottomBar.requestFocus();
            mBottomBar.setTranslationY(-mBarH);
        } else {
            mBottomBar.clearFocus();
            mBottomBar.setTranslationY(0);
        }
    }

    private void rotationBanner(boolean toBottom) {
        if (mGDEntity != null && mGDEntity.isImgVertical()) {
            return;
        }
        if (toBottom && !rotated) {
            rotated = true;
        } else if (!toBottom && rotated) {
            rotated = false;
        } else {
            return;
        }
        SimpleViewPagerAdapter adapter = (SimpleViewPagerAdapter) mImgVP.getAdapter();
        if (adapter != null) {
            for (int i = 0, count = adapter.getCount(); i < count; i++) {
                ScreenshotFragment fragment = (ScreenshotFragment) adapter.getItem(i);
                if (fragment != null) {
                    fragment.setRotation(rotated, mShotVpPosition == i);
                }
            }
        }
        System.gc();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_manager: //下载管理
                Intent intent = new Intent(this, AppManagerActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void setupContentViewPager(ViewPager viewPager, final List<TagEntity> tabs) {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        int i = 0, position = 0;
        for (TagEntity tag : tabs) {
            int tagId = tag.getTagId();
            BaseFragment fragment = null;
            if (mBarId == tagId) {
                position = i;
            }
            switch (tagId) {
                case 0:
                    fragment = GameDetailInfoFragment.newInstance(mGDEntity.getAppId());
                    break;
                case 1:
                case 2:
                case 5:
                case 6:
                case 7:
                    fragment = GameDetailSimpleFragment.newInstance(mGDEntity.getAppId(), tag.getTagId());
                    break;
                case 8:
                    fragment = GameDetailGiftFragment.newInstance(mGDEntity.getAppId());
                    break;
                case 9:
                    fragment = GameDetailCommentFragment.newInstance(mGDEntity.getAppId());
                    break;
            }
            if (fragment != null) {
                adapter.addFrag(fragment, tag.getTagName());
            }
            i++;
        }
        viewPager.setAdapter(adapter);
        //如果真实的栏目小于6个，则不让其滚动
        mTab.setTabMode(tabs.size() < 5 ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);
        mTab.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(tabs.size());
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                isScrollTop = true;
                int tagId = tabs.get(position).getTagId();
                if (mBottomBar.getChildCount() > 0) {
                    mBottomBar.removeAllViews();
                }
                if (tagId == 0) {
                    showBottomBar(true);
                    mBottomBar.addView(mGDIBar);
                } else if (tagId == 9) {
                    mBottomBar.addView(mGDCBar);
                    showBottomBar(true);
                } else {
                    showBottomBar(false);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化游戏截图ViewPager
     *
     * @return
     */
    private void setupGameShotViewPager(final ViewPager viewPager) {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        List<BannerEntity> data = getBannerData();
        for (BannerEntity entity : data) {
            ScreenshotFragment bf = null;
            if (getSettingEntity().isShowImg()) {
                bf = ScreenshotFragment.newInstance(entity);
            }
            adapter.addFrag(bf, "");
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(data.size());
        mIndicator.setViewPager(viewPager);
        mIndicator.onPageSelected(0);

        //设置Banner图片高度
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                viewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        SimpleViewPagerAdapter adapter = (SimpleViewPagerAdapter) mImgVP.getAdapter();
                        int h = (int) getResources().getDimension(R.dimen.game_detail_head_img_vp_height);
                        if (!getSettingEntity().isShowImg() || mGDEntity.isImgVertical()) {
                            h = AndroidUtils.getScreenParams(GameDetailActivity.this)[1];
                            mImgVP.getLayoutParams().height = h;
                        }
                        for (int i = 0, count = adapter.getCount(); i < count; i++) {
                            ScreenshotFragment fragment = (ScreenshotFragment) adapter.getItem(i);
                            if (fragment != null) {
                                fragment.setBannerHeight(h);
                                if (!getSettingEntity().isShowImg()) {
                                    fragment.setDrawable(R.mipmap.default_full);
                                }
                            }
                        }
                    }
                });
            }
        });

        dismissLoadingDialog();
    }

    private List<BannerEntity> getBannerData() {
        List<BannerEntity> list = new ArrayList<>();
        for (String img : mGDEntity.getBgUrl()) {
            BannerEntity entity = new BannerEntity();
            entity.setImgUrl(img);
            list.add(entity);
        }
        return list;
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        getModule(GameDetailModule.class).getGameDetail(mGameId);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_game_detail;
    }

    @Override
    protected void dataCallback(int result, Object data) {
        if (result == ResultCode.ACTIVITY.GD_COMMENT) {
            return;
        }
        super.dataCallback(result, data);
        if (data == null || ((data instanceof Integer) && (int) data == ServiceUtil.ERROR)) {
            return;
        }
        if (result == ResultCode.SERVER.GET_GAME_DETAIL_DATA) {
            setupWidget((GameDetailEntity) data);
        } else if (result == ResultCode.SERVER.GET_GAME_DETAIL_Tab) {
            setupContentViewPager(mContentVP, (List<TagEntity>) data);
        } else if (result == ResultCode.SERVER.GET_COLLECT_STATE) {
            Bundle b = (Bundle) data;
            mGDIBar.setCollectState(b.getBoolean("state"), b.getInt("collectId"));
        } else if (result == ResultCode.SERVER.ADD_COLLECT || result == ResultCode.SERVER.CANCEL_COLLECT) {
            getModule(CollectModule.class).getCollectState(0, mGDEntity.getAppId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultCode.ACTIVITY.GD_COMMENT) {
            SimpleViewPagerAdapter adapter = (SimpleViewPagerAdapter) mContentVP.getAdapter();
            for (int i = 0, count = adapter.getCount(); i < count; i++) {
                Fragment fragment = adapter.getItem(i);
                if (fragment instanceof GameDetailCommentFragment) {
                    ((GameDetailCommentFragment) fragment).notifyData();
                }
            }

        }
    }

    /**
     * 滑动手势
     */
    private class SimpleGestureAction extends GestureDetector.SimpleOnGestureListener {

        int t = DensityUtils.dp2px(30);

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            L.d(TAG, "fling");
            if (velocityY > mTemp) {
                showBottomBar(false);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e1 == null) {
                L.d(TAG, "e1 null");
                return false;
            }

//            if (Math.abs(distanceY) > t) {      //控制滑动速度
//                return true;
//            }

            if (mRawY == mTopL && distanceY > 0) {
                mRawY = mTopL;
                return true;
            }

            if (mRawY == mBottomL && distanceY < 0) {
                mRawY = mBottomL;
                return true;
            }

            mRawY -= distanceY;
            if (mRawY < mCenterL) {
                a += distanceY < 0 ? -0.03 : 0.03;
                if (a < 0.0f) {
                    a = 0.0f;
                } else if (a > 1.0f) {
                    a = 1.0f;
                }
            } else {
                a = 0.0f;
            }
            if (mRawY <= mTopL) {
                mRawY = mTopL;
                a = 1.0f;
                mBarBg.setAlpha(a);
                mStatusBarTemp.setAlpha(a);
            }

            if (mRawY >= mRawCenterL + mBarH) {
                rotationBanner(true);
            }
            mContent.setTranslationY(mRawY);
            showBottomBar(mRawY <= mCenterL);
            return true;
        }
    }
}

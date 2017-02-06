package com.touchrom.gaoshouyou.base;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.graphics.Palette;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.ScreenUtil;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.dialog.LoadingDialog;
import com.touchrom.gaoshouyou.dialog.MsgDialog;
import com.touchrom.gaoshouyou.entity.SettingEntity;
import com.touchrom.gaoshouyou.entity.UserEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.widget.MyToolBar;
import com.touchrom.gaoshouyou.widget.StatusBarCompat;
import com.touchrom.gaoshouyou.widget.TempView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lyy on 2015/11/6.
 * activity基类
 */
public abstract class BaseActivity<VB extends ViewDataBinding> extends AbsActivity<VB> {
    protected int mCurrentColor = -1;
    protected int mStateBarColor = -1;
    protected UserEntity mUser;
    protected MyToolBar mToolbar;
    protected AppManager mManager = AppManager.getInstance();
    private boolean autoSetColor = false;
    private SettingEntity mSettingEntity;
    private MsgDialog mMsgDialog;
    private LoadingDialog mLoadingDialog;
    protected TempView mTempView;
    private boolean usDefaultAnim = true;
    /**
     * 是否设置ActionBar颜色，主要是为了防止和CollapsingToolbarLayout联动时没反应
     * 和CollapsingToolbarLayout联动时需要设置false
     */
    private boolean isSetBarColor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AndroidVersionUtil.hasLollipop()) {
            if (usDefaultAnim) {
                getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
                getWindow().setExitTransition(new Slide(Gravity.LEFT));
                getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
//        super.startActivity(intent, options);
        if (AndroidVersionUtil.hasLollipop()) {
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
            if (options == null) {
            }
            super.startActivity(intent, options);
        } else {
            super.startActivity(intent, options);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
//        super.startActivityForResult(intent, requestCode, options);
        if (AndroidVersionUtil.hasLollipop()) {
            if (options == null) {
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
            }
            super.startActivityForResult(intent, requestCode, options);
        } else {
            super.startActivityForResult(intent, requestCode, options);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }
    }

    protected void setUsDefaultAnim(boolean useDefaultAnim) {
        this.usDefaultAnim = useDefaultAnim;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        init();
    }

    private void init() {
        mToolbar = (MyToolBar) findViewById(R.id.bar);
        if (mToolbar != null) {
            mToolbar.getBackView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        mTempView = (TempView) findViewById(R.id.temp_view);
        if (mTempView != null) {
            mTempView.setOnTempBtListener(new TempView.OnTempBtListener() {
                @Override
                public void onTempBt(int type) {
                    if (type == TempView.ERROR) {
                        onNetError();
                    } else if (type == TempView.NULL) {
                        onNetDataNull();
                    }
                }
            });
        }
        int color = mManager.getCurrentColor();
        int stateBarColor = mManager.getStateBarColor();
        mCurrentColor = color == -1 ? getMyColor(R.color.skin_primary_color) : color;
        mStateBarColor = stateBarColor == -1 ? getMyColor(R.color.skin_primary_color) : stateBarColor;
        themeColorSetting(mCurrentColor); //尼玛，设置了状态栏颜色时联动是会出现问题
        mSettingEntity = mManager.getSetting();
        if (mSettingEntity == null) {
            mSettingEntity = new SettingEntity();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                // 进行资源释放操作
                System.gc();
                break;
        }
    }

    /**
     * 显示填充对话框
     *
     * @param type {@link TempView#ERROR}
     *             {@link TempView#NULL}
     *             {@link TempView#LOADING}
     */
    protected void showTempView(int type) {
        if (mTempView == null) {
            return;
        }
        mTempView.setVisibility(View.VISIBLE);
        mTempView.setType(type);
    }

    /**
     * 关闭错误填充对话框
     */
    protected void hintTempView() {
        hintTempView(0);
    }

    /**
     * 延时关闭填充对话框
     */
    protected void hintTempView(int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTempView == null) {
                    return;
                }
                mTempView.closeLoading();
                mTempView.clearFocus();
                mTempView.setVisibility(View.GONE);
            }
        }, delay);
    }


    /**
     * 网络数据返回为空
     */
    protected void onNetDataNull() {

    }

    /**
     * 网络错误
     */
    protected void onNetError() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen");
        MobclickAgent.onPause(this);
    }

    /**
     * 获取设置实体
     */
    public SettingEntity getSettingEntity() {
        return mSettingEntity;
    }

    /**
     * 保存设置实体
     */
    public void setSettingEntity(SettingEntity settingEntity) {
        this.mSettingEntity = settingEntity;
        mManager.saveSetting(mSettingEntity);
    }

    /**
     * {@link #isSetBarColor}
     *
     * @param set
     */
    protected void setBarCanSetColor(boolean set) {
        isSetBarColor = set;
    }

    /**
     * 在onCreate里面设置
     *
     * @param auto
     */
    protected void setAutoSetColor(boolean auto) {
        autoSetColor = auto;
    }

    /**
     * 自动设置主题颜色
     * {@link #setAutoSetColor(boolean)}
     */
    private void autoSetThemeColor() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        final Bitmap bmp = view.getDrawingCache();
        if (bmp == null) {
            return;
        }
        final Bitmap bitmap = Bitmap.createBitmap(bmp);
        view.setDrawingCacheEnabled(false);
        if (bitmap != null) {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette p) {
                    mCurrentColor = p.getDarkVibrantColor(getMyColor(R.color.skin_primary_color));
                    mStateBarColor = p.getVibrantColor(getMyColor(R.color.skin_primary_color_dark));
                    themeColorSetting(mCurrentColor);
                    bmp.recycle();
                    bitmap.recycle();
                }
            });
        }
    }

    /**
     * 显示消息对话框
     *
     * @param title
     * @param msg
     */
    protected void showMsgDialog(final String title, final String msg, final int requestCode) {
        Observable.just("").subscribeOn(Schedulers.io())
                .map(new Func1<String, MsgDialog>() {

                    @Override
                    public MsgDialog call(String s) {
                        return new MsgDialog(title, msg, BaseActivity.this, requestCode);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MsgDialog>() {
                    @Override
                    public void call(MsgDialog msgDialog) {
                        mMsgDialog = msgDialog;
                        msgDialog.show(BaseActivity.this.getSupportFragmentManager(), "msgDialog");
                    }
                });
    }

    /**
     * 显示消息对话框
     *
     * @param title
     * @param msg
     */
    protected void showMsgDialog(final String title, final String msg) {
        showMsgDialog(title, msg, -1);
    }

    /**
     * 关闭消息对话框
     */
    protected void dismissMsgDialog() {
        Observable.just("").delay(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (mMsgDialog != null) {
                            mMsgDialog.dismiss();
                        }
                    }
                });
    }

    /**
     * 显示加载等待对话框
     */
    protected void showLoadingDialog() {
        showLoadingDialog(true);
    }

    /**
     * 显示加载等待对话框
     *
     * @param canCancel 能被取消？
     */
    protected void showLoadingDialog(final boolean canCancel) {
        Observable.just("").subscribeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, LoadingDialog>() {
                    @Override
                    public LoadingDialog call(String s) {
                        return new LoadingDialog(canCancel);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoadingDialog>() {
                    @Override
                    public void call(LoadingDialog loadingDialog) {
                        mLoadingDialog = loadingDialog;
                        loadingDialog.show(BaseActivity.this.getSupportFragmentManager(), "loadingDialog");
                    }
                });
    }

    /**
     * 关闭加载等待对话框，保证对话框至少显示1秒
     */
    protected void dismissLoadingDialog() {
        Observable.just("").delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        if (obj == null) {
            showTempView(TempView.NULL);
        } else if (obj instanceof Integer) {
            int i = (int) obj;
            if (i == ServiceUtil.ERROR) {
                showTempView(TempView.ERROR);
            }
        } else if (obj instanceof List) {
            List l = (List) obj;
            if (l.size() == 0) {
                showTempView(TempView.NULL);
            } else {
                hintTempView();
            }
        } else {
            hintTempView();
        }
    }

    /**
     * 主题颜色设置
     *
     * @param color
     */
    protected void themeColorSetting(int color) {
        if (color == -1 || !isSetBarColor) {
            return;
        }
        //设置4.4的沉浸效果
        StatusBarCompat.compat(BaseActivity.this, mCurrentColor);
        if (AndroidVersionUtil.hasLollipop()) {
            getWindow().setNavigationBarColor(getMyColor(R.color.black));
            if (mStateBarColor == -1) {
                return;
            }
            getWindow().setStatusBarColor(mStateBarColor);
        }
        if (mToolbar != null) {
            mToolbar.setBackgroundColor(mCurrentColor);
        }
    }

//    /**
//     * 设置状态栏透明度
//     *
//     * @param alpha
//     */
//    protected void setStateBarAlpha(@FloatRange(from = 0.0f, to = 1.0f) float alpha) {
//        if (AndroidVersionUtil.hasLollipop()){
//        }
//    }

    /**
     * 设置StartBar颜色
     */
    protected void setStateBarColor(@ColorInt int color) {
        if (AndroidVersionUtil.hasLollipop()) {
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 夜间模式
     *
     * @param night true --> 设置灰度
     */
    protected void setNightStyle(boolean night) {
        ScreenUtil.getInstance().setGreyScale(getWindow().getDecorView(), night);
    }

    /**
     * 获取用户
     *
     * @return
     */
    protected UserEntity getUser() {
        mUser = AppManager.getInstance().getUser();
        return mUser;
    }

    protected int getMyColor(int color) {
        return getResources().getColor(color);
    }


    @Override
    public void finish() {
        super.finish();
        if (!AndroidVersionUtil.hasLollipop()) {
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
    }


}

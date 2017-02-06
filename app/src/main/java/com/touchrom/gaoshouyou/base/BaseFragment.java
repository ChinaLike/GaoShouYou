package com.touchrom.gaoshouyou.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arialyy.frame.core.AbsFragment;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.dialog.LoadingDialog;
import com.touchrom.gaoshouyou.dialog.MsgDialog;
import com.touchrom.gaoshouyou.entity.SettingEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;
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
 * fragment基类
 */
public abstract class BaseFragment<VB extends ViewDataBinding> extends AbsFragment<VB> {

    private LoadingDialog mLoadingDialog;
    private MsgDialog mMsgDialog;
    protected int mToolBarAndTabBarH;   //工具栏和tab栏的总高度
    protected int mToolBarH;            //工具栏高度
    protected SettingEntity mSettingEntity;
    protected TempView mTempView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mToolBarH = (int) getResources().getDimension(R.dimen.tool_bar_height);
        mToolBarAndTabBarH = (int) (mToolBarH + getResources().getDimension(R.dimen.tab_bar_height));
        mSettingEntity = AppManager.getInstance().getSetting();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void onDelayLoad() {

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
                        return new MsgDialog(title, msg, BaseFragment.this, requestCode);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MsgDialog>() {
                    @Override
                    public void call(MsgDialog msgDialog) {
                        mMsgDialog = msgDialog;
                        msgDialog.show(mActivity.getSupportFragmentManager(), "msgDialog");
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
        Observable.just("").subscribeOn(Schedulers.newThread())
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
                        loadingDialog.show(mActivity.getSupportFragmentManager(), "loadingDialog");
                    }
                });
    }

    /**
     * 关闭加载等待对话框，保证对话框至少显示1秒
     */
    protected void dismissLoadingDialog() {
        Observable.just("").delay(1000, TimeUnit.MILLISECONDS, Schedulers.newThread())
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
    protected void init(Bundle savedInstanceState) {
        mTempView = (TempView) mRootView.findViewById(R.id.temp_view);
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }
}

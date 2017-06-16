package com.arialyy.frame.core;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.arialyy.frame.module.AbsModule;
import com.arialyy.frame.module.IOCProxy;
import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.T;

import butterknife.ButterKnife;

/**
 * Created by lyy on 2015/11/3.
 * 所有的 Activity都应该继承这个类
 */
public abstract class AbsActivity<VB extends ViewDataBinding> extends AppCompatActivity {
    protected String TAG = "";
    private VB mBind;
    private IOCProxy mProxy;
    /**
     * 第一次点击返回的系统时间
     */
    private long mFirstClickTime = 0;
    protected AbsApplication mApp;
    protected View mRootView;
    private ModuleFactory mModuleF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (AbsApplication) getApplication();
        mApp.getAppManager().addActivity(this);
        mBind = DataBindingUtil.setContentView(this, setLayoutId());
        mProxy = IOCProxy.newInstance(this);
        TAG = StringUtil.getClassName(this);
        mModuleF = ModuleFactory.newInstance();
        ButterKnife.inject(this);
        mRootView = findViewById(android.R.id.content);
        init(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void finish() {
        super.finish();
        mApp.getAppManager().removeActivity(this);
        System.gc();
    }

    public View getRootView() {
        return mRootView;
    }

    /**
     * 设置资源布局
     *
     * @return
     */
    protected abstract int setLayoutId();

    /**
     * 获取binding对象
     */
    protected VB getBinding() {
        return mBind;
    }

    /**
     * 获取Module
     *
     * @param clazz {@link AbsModule}
     */
    protected <M extends AbsModule> M getModule(Class<M> clazz) {
        M module = mModuleF.getModule(this, clazz);
        mProxy.changeModule(module);
        return module;
    }

    /**
     * 获取应用管理器
     */
    public AbsApplication getApp() {
        return mApp;
    }

    /**
     * 数据回调
     *
     * @param result
     * @param data
     */
    protected abstract void dataCallback(int result, Object data);

    /**
     * 双击退出
     */
    private boolean onDoubleClickExit(long timeSpace) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mFirstClickTime > timeSpace) {
            T.showShort(this, "再按一次退出");
            mFirstClickTime = currentTimeMillis;
            return false;
        } else {
            return true;
        }
    }

    /**
     * 双击退出，间隔时间为2000ms
     *
     * @return
     */
    public boolean onDoubleClickExit() {
        return onDoubleClickExit(2000);
    }

    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行,如果为true则为后台运行
     */
    public void exitApp(Boolean isBackground) {
        mApp.exitApp(isBackground);
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        mApp.exitApp(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelp.getInstance().handlePermissionCallback(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       PermissionHelp.getInstance().handleSpecialPermissionCallback(this, requestCode, resultCode, data);
    }
}

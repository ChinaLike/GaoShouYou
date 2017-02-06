package com.arialyy.frame.core;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arialyy.frame.module.AbsModule;
import com.arialyy.frame.module.IOCProxy;
import com.arialyy.frame.permission.PermissionManager;
import com.arialyy.frame.util.StringUtil;

import butterknife.ButterKnife;

/**
 * Created by lyy on 2015/11/4.
 * 基础Fragment
 */
public abstract class AbsFragment<VB extends ViewDataBinding> extends Fragment {
    protected String TAG = "";
    private VB mBind;
    private IOCProxy mProxy;
    protected View mRootView;
    protected AbsActivity mActivity;
    private ModuleFactory mModuleF;
    protected boolean isInit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, setLayoutId(), container, false);
        initFragment();
        mRootView = mBind.getRoot();
        return mRootView;
    }

    private void initFragment() {
        TAG = StringUtil.getClassName(this);
        mProxy = IOCProxy.newInstance(this);
        mModuleF = ModuleFactory.newInstance();
        ButterKnife.inject(this, mBind.getRoot());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AbsActivity) {
            mActivity = (AbsActivity) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
        isInit = true;
        if (getUserVisibleHint()) {
            onDelayLoad();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isInit) {
            isInit = false;
            onDelayLoad();
        }
    }

    protected abstract void init(Bundle savedInstanceState);

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
        M module = mModuleF.getModule(getContext(), clazz);
        mProxy.changeModule(module);
        return module;
    }

    /**
     * 延时加载
     */
    protected abstract void onDelayLoad();

    /**
     * 设置资源布局
     *
     * @return
     */
    protected abstract int setLayoutId();

    /**
     * 数据回调
     */
    protected abstract void dataCallback(int result, Object obj);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelp.getInstance().handlePermissionCallback(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionHelp.getInstance().handleSpecialPermissionCallback(getContext(), requestCode, resultCode, data);
    }
}

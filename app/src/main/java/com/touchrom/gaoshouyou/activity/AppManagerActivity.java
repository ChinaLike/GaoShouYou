package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.show.L;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityAppManagerBinding;
import com.touchrom.gaoshouyou.entity.GSYSEntity;
import com.touchrom.gaoshouyou.entity.sql.ApkInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.ApkUpdateInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.fragment.AMApkFragment;
import com.touchrom.gaoshouyou.help.DownloadHelp;
import com.touchrom.gaoshouyou.help.GSYServiceHelp;
import com.touchrom.gaoshouyou.inf.OnAMAdapterListener;
import com.touchrom.gaoshouyou.inf.OnAMFragmentListener;
import com.touchrom.gaoshouyou.module.AppManagerModule;
import com.touchrom.gaoshouyou.service.GaoShouYouService;
import com.touchrom.gaoshouyou.util.S;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/21.
 * 应用管理界面
 */
public class AppManagerActivity extends BaseActivity<ActivityAppManagerBinding> implements OnAMFragmentListener,
        View.OnClickListener {
    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.content_vp)
    ViewPager mContentVp;
    private int mType;  //fragment类型
    private Set<ApkInfoEntity> mCheckData;
    private Map<Integer, AMApkFragment> mFragments = new HashMap<>();
    private int mCheckNum = 0;
    private Drawable mCheckIcon;
    private ApkInfoEntity mCurrentApkInfo;
    private List<ApkInfoEntity> mBack = new ArrayList<>();
    private int mTurnPage = -1;   //跳转游戏更新界面

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_app_manager;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mToolbar.setTitle("应用管理");
        mToolbar.getRightIcon().setImageDrawable(getResources().getDrawable(R.mipmap.icon_delete));
        mToolbar.getRightIcon().setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bar_transparent));
        mToolbar.getRightIcon().setVisibility(View.GONE);
        mToolbar.getRightIcon().setOnClickListener(this);
        mToolbar.getBackView().setOnClickListener(this);
        mCheckIcon = getResources().getDrawable(R.mipmap.icon_checked);
        mTurnPage = getIntent().getIntExtra(Constance.KEY.TURN, -1);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        showLoadingDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpContentVp(mContentVp);
                dismissLoadingDialog();
            }
        }, 2000);
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        AMApkFragment allApk = AMApkFragment.newInstance(AMApkFragment.ALL_APK);
        allApk.setOnAMFragmentListener(this);
        AMApkFragment installed = AMApkFragment.newInstance(AMApkFragment.INSTALLED_APK);
        installed.setOnAMFragmentListener(this);
        AMApkFragment download = AMApkFragment.newInstance(AMApkFragment.DOWNLOAD_APK);
        download.setOnAMFragmentListener(this);
        AMApkFragment update = AMApkFragment.newInstance(AMApkFragment.UPDATE_APK);
        update.setOnAMFragmentListener(this);

        mFragments.put(AMApkFragment.ALL_APK, allApk);
        mFragments.put(AMApkFragment.INSTALLED_APK, installed);
        mFragments.put(AMApkFragment.DOWNLOAD_APK, download);
        mFragments.put(AMApkFragment.UPDATE_APK, update);
    }

    /**
     * 设置内容VP
     */
    private void setUpContentVp(ViewPager vp) {
        initFragment();
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(mFragments.get(AMApkFragment.DOWNLOAD_APK), "下载(0)");
        adapter.addFrag(mFragments.get(AMApkFragment.UPDATE_APK), "可更新(0)");
        adapter.addFrag(mFragments.get(AMApkFragment.INSTALLED_APK), "已安装(0)");
        adapter.addFrag(mFragments.get(AMApkFragment.ALL_APK), "安装包(0)");
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(4);
        mTab.setupWithViewPager(vp);
        if (mTurnPage != -1) {
            vp.setCurrentItem(mTurnPage);
        }
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                clearState();
            }

            @Override
            public void onPageSelected(int position) {
                clearState();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right_icon) {
            if (mType == AMApkFragment.ALL_APK) {
                showMsgDialog("操作提示", "确认删除选中软件包？", mType);
            } else if (mType == AMApkFragment.INSTALLED_APK) {
                showMsgDialog("操作提示", "确认卸载选中软件包？", mType);
            } else if (mType == AMApkFragment.DOWNLOAD_APK) {
                showMsgDialog("操作提示", "确认删除选中的下载任务或已下载的软件包？", mType);
            } else if (mType == AMApkFragment.UPDATE_APK) {
                showMsgDialog("操作提示", "确认更新选中的软件包？", mType);
            }
        } else if (v.getId() == R.id.back) {
            if (mCheckNum == 0) {
                onBackPressed();
            } else { //取消所有选择
                clearState();
            }
        }
    }

    /**
     * 清空所有状态
     */
    public void clearState() {
        if (mCheckNum != 0) {
            mCheckNum = 0;
            mToolbar.getRightIcon().setVisibility(View.GONE);
            mToolbar.setTitle("应用管理");
            mToolbar.setBackIcon(null);
            mFragments.get(mType).clearCheckState();
        }
    }

    @Override
    public void onNum(BaseFragment fragment, int type, int num) {
        int index = 0;
        String text = "下载()";
        if (type == AMApkFragment.ALL_APK) {
            index = 3;
            text = "安装包(" + num + ")";
        } else if (type == AMApkFragment.INSTALLED_APK) {
            index = 2;
            text = "已安装(" + num + ")";
        } else if (type == AMApkFragment.UPDATE_APK) {
            index = 1;
            text = "可更新(" + num + ")";
        } else if (type == AMApkFragment.DOWNLOAD_APK) {
            index = 0;
            text = "下载(" + num + ")";
        }
        mTab.getTabAt(index).setText(text);
//        mTab.getTabAt(0).setText("安装包(" + num + ")");
    }

    @Override
    public void onCheckNum(BaseFragment fragment, int type, int checkNum, Set<ApkInfoEntity> checkData) {
        mCheckNum = checkNum;
        if (checkNum == 0) {
            mToolbar.getRightIcon().setVisibility(View.GONE);
            mToolbar.setTitle("应用管理");
            mToolbar.setBackIcon(null);
            return;
        }
        mToolbar.setBackIcon(mCheckIcon);
        mType = type;
        mToolbar.getRightIcon().setVisibility(View.VISIBLE);
        mToolbar.setTitle("已选" + checkNum + "个");

        if (type == AMApkFragment.ALL_APK) {
            mToolbar.setRightIcon(getResources().getDrawable(R.mipmap.icon_delete));
        } else if (type == AMApkFragment.INSTALLED_APK) {
            mToolbar.setRightIcon(getResources().getDrawable(R.mipmap.icon_delete));
        } else if (type == AMApkFragment.UPDATE_APK) {
            mToolbar.setRightIcon(getResources().getDrawable(R.mipmap.icon_update));
        }
        mCheckData = checkData;
    }

    @Override
    public void onButtonHandle(BaseFragment fragment, int type, int handleType, ApkInfoEntity entity) {
        mType = type;
        mCurrentApkInfo = entity;
        if (mType != AMApkFragment.DOWNLOAD_APK) {
            if (handleType == OnAMAdapterListener.HANDLER_INSTALL) {
                String path;
                if (mType == AMApkFragment.UPDATE_APK) {
                    path = getSettingEntity().getDownloadLocation() + entity.getUpdateInfoEntity().getAppName() + ".apk";
                } else {
                    path = entity.getApkPath();
                }
                if (TextUtils.isEmpty(path)) {
                    L.w(TAG, "安装路径不能为空");
                    return;
                }
                install(path);
            } else if (handleType == OnAMAdapterListener.HANDLER_UN_INSTALL) {
                uninstall(entity);
            } else if (handleType == OnAMAdapterListener.HANDLER_UPDATE) {
                mBack.clear();
                updateApk(entity.getUpdateInfoEntity());
                mFragments.get(mType).updateAllData();
                mFragments.get(AMApkFragment.DOWNLOAD_APK).addItemData(mBack.get(0));
            }
        } else {
            if (handleType == OnAMAdapterListener.HANDLER_INSTALL) {
                install(getSettingEntity().getDownloadLocation() + entity.getDownloadEntity().getName() + ".apk");
            } else if (handleType == OnAMAdapterListener.HANDLER_OPEN) {
                AndroidUtils.startOtherApp(this, entity.getDownloadEntity().getPackageName());
            }
        }
    }

    /**
     * 执行安装
     *
     * @param apkPath
     */
    private void install(String apkPath) {
        GSYSEntity entity = new GSYSEntity();
        File file = new File(apkPath);
        entity.setData(apkPath);
        entity.setName(file.getName());
        entity.setAction(GaoShouYouService.ACTION_INSTALL);
        GSYServiceHelp.newInstance().run(this, entity);
    }

    /**
     * APP版本升级
     */
    private void updateApk(ApkUpdateInfoEntity entity) {
        DownloadEntity dEntity = new DownloadEntity();
        dEntity.setImgUrl(entity.getImgUrl());
        dEntity.setName(entity.getAppName());
        dEntity.setGameId(entity.getAppId());
        dEntity.setDownloadUrl(entity.getDownloadUrl());
        DownloadHelp.newInstance().download(this, dEntity);
        entity.setUpdateState(ApkUpdateInfoEntity.STATE_UPDATING);
        getModule(AppManagerModule.class).saveUpdateEntity(entity);
        ApkInfoEntity apkEntity = new ApkInfoEntity();
        apkEntity.setDownloadEntity(dEntity);
        mBack.add(apkEntity);
    }

    /**
     * 删除软件
     */
    private void uninstall(ApkInfoEntity apkEntity) {
        GSYSEntity entity = new GSYSEntity();
        entity.setName(apkEntity.getApkName());
        entity.setData(apkEntity.getApkPackage());
        entity.setAction(GaoShouYouService.ACTION_UNINSTALL);
        GSYServiceHelp.newInstance().run(this, entity);
    }

    @Override
    protected void dataCallback(int result, Object data) {
        super.dataCallback(result, data);
        if (result == ResultCode.DIALOG.MSG_DIALOG_ENTER) { //操作确认
            int type = (int) data;
            mType = type;
            if (type == AMApkFragment.ALL_APK) {    //删除软件包
                showLoadingDialog(false);
                for (ApkInfoEntity entity : mCheckData) {
                    File file = new File(entity.getApkPath());
                    file.delete();
                }
                mFragments.get(mType).removeAllCheckData();
                S.i(mRootView, "选中软件包删除完成");
            } else if (type == AMApkFragment.INSTALLED_APK) { //卸载APP
//                showLoadingDialog(false);
                for (ApkInfoEntity entity : mCheckData) {
                    AndroidUtils.uninstall(this, entity.getApkPackage());
                    mCurrentApkInfo = entity;
                }
//                S.i(mRootView, "选中App卸载完成");
            } else if (type == AMApkFragment.DOWNLOAD_APK) {  //删除下载任务
                mFragments.get(mType).removeAllCheckData();
            } else if (type == AMApkFragment.UPDATE_APK) {   //批量更新操作
                mBack.clear();
                for (ApkInfoEntity entity : mCheckData) {
                    updateApk(entity.getUpdateInfoEntity());
                }
                mFragments.get(mType).updateAllData();
                mFragments.get(AMApkFragment.DOWNLOAD_APK).addItemData(mBack);
            }

            mToolbar.setTitle("应用管理");
            dismissLoadingDialog();
            clearState();
        }
    }

}

package com.touchrom.gaoshouyou.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.show.L;
import com.touchrom.gaoshouyou.base.adapter.AbsRVHolder;
import com.lyy.ui.help.DividerItemDecoration;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.AppManagerActivity;
import com.touchrom.gaoshouyou.activity.MainActivity;
import com.touchrom.gaoshouyou.adapter.AppManagerAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentAppManagerTabContentBinding;
import com.touchrom.gaoshouyou.dialog.AMAppDetailDialog;
import com.touchrom.gaoshouyou.entity.sql.ApkInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.ApkUpdateInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.inf.OnAMAdapterListener;
import com.touchrom.gaoshouyou.inf.OnAMFragmentListener;
import com.touchrom.gaoshouyou.module.AppManagerModule;
import com.touchrom.gaoshouyou.service.DownloadService;
import com.touchrom.gaoshouyou.service.GaoShouYouService;
import com.touchrom.gaoshouyou.util.S;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/21.
 * 应用管理 安装包、已安装、可更新 的fragment
 */
@SuppressLint("ValidFragment")
public class AMApkFragment extends BaseFragment<FragmentAppManagerTabContentBinding> {
    public static final String ACTION_PRE = "com.lyy.download_pre"; //下载前
    public static final String ACTION_DOWNLOAD_ING = "com.lyy.download_ing"; //下载中
    public static final String ACTION_RESUME = "com.lyy.download_resume"; //恢复下载
    public static final String ACTION_PAUSE = "com.lyy.download_pause"; //暂停
    public static final String ACTION_COMPLETE = "com.lyy.download_complete"; //完成
    public static final String ACTION_STOP = "com.lyy.download_stop"; //停止
    public static final String ACTION_FAIL = "com.lyy.download_fail";       //失败
    public static final String ACTION_CANCEL = "com.lyy.download_cancel";       //取消任务
    public static final String ACTION_IGNORE = "com.lyy.download_ignore";       //忽略任务
    public static final String ACTION_SERVICE_START = "com.lyy.service_start";  //服务启动
    public static final String ACTION_SERVICE_STOP = "com.lyy.service_stop";    //服务停止
    public static final String ACTION_CLOSE_UPDATE_TIME = "com.lyy.close_update_time";  //关闭定时器
    public static final String ACTION_UPDATE_DATA = "com.lyy.update_data";  //重新更新数据
    public static final int ALL_APK = 0x01; //所有安装包
    public static final int INSTALLED_APK = 0x02;   //已安装的安装包
    public static final int UPDATE_APK = 0x03;  //可更新的安装包
    public static final int DOWNLOAD_APK = 0x04;  //下载界面
    private static final long UPDATE_INTERVAL = 100; //更新间隔
    @InjectView(R.id.list)
    RecyclerView mList;
    private List<ApkInfoEntity> mData = new ArrayList<>();
    private int mType = ALL_APK;
    private AppManagerAdapter mAdapter;
    /**
     * 接收下载完成的广播
     */
    BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            L.d(TAG, "update fragment = " + action);
            switch (action) {
                case ACTION_COMPLETE: {
                    DownloadEntity entity = intent.getParcelableExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY);
                    for (ApkInfoEntity apkEntity : mData) {
                        ApkUpdateInfoEntity updateEntity = apkEntity.getUpdateInfoEntity();
                        if (updateEntity.getDownloadUrl().equals(entity.getDownloadUrl())) {
                            updateEntity.setUpdateState(ApkUpdateInfoEntity.STATE_UPDATE_COMPLETE);
                            getModule(AppManagerModule.class).saveUpdateEntity(updateEntity);
                        }
                    }
                    updateAllData();
                    break;
                }
                case ACTION_CANCEL: {
                    DownloadEntity entity = intent.getParcelableExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY);
                    for (ApkInfoEntity apkEntity : mData) {
                        ApkUpdateInfoEntity updateEntity = apkEntity.getUpdateInfoEntity();
                        if (updateEntity.getDownloadUrl().equals(entity.getDownloadUrl())) {
                            updateEntity.setUpdateState(ApkUpdateInfoEntity.STATE_UN_UPDATE);
                            getModule(AppManagerModule.class).saveUpdateEntity(updateEntity);
                        }
                    }
                    updateAllData();
                    break;
                }
                case GaoShouYouService.ACTION_INSTALL_COMPLETE:
                case GaoShouYouService.ACTION_UNINSTALL_COMPLETE:
                    handleID(intent);
                    break;
            }
        }
    };
    /**
     * 已安装和安装包
     */
    BroadcastReceiver mIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_UPDATE_DATA)) {
                if (mType == ALL_APK) {
                    getModule(AppManagerModule.class).getAllApk(true, Constance.PATH.ROOT_PATH);
                } else if (mType == INSTALLED_APK) {
                    getModule(AppManagerModule.class).getInstalledApkInfo();
                }
            } else {
                handleID(intent);
            }
        }
    };
    private ApkInfoEntity mCurrentItem = null;
    private OnAMFragmentListener mFragmentListener;
    private int mListScrollState;
    private UpdateTask mUpdateTask;
    /**
     * 接收下载的广播
     */
    BroadcastReceiver mDownloadReceiver = new BroadcastReceiver() {
        long lastTime;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_DOWNLOAD_ING:
                    if (mAdapter != null && System.currentTimeMillis() - lastTime >= UPDATE_INTERVAL) {
                        mAdapter.setProgress(intent.getStringExtra("key"), intent.getLongExtra("progress", 0));
                        lastTime = System.currentTimeMillis();
                    }
                    break;
                case ACTION_CLOSE_UPDATE_TIME:  //关闭更新定时器
                    if (mUpdateTask != null) {
                        mUpdateTask.stopTask();
                    }
                    break;
                case ACTION_SERVICE_START:    //下载服务启动
                    L.v(TAG, "service start broadcast");
                    if (mUpdateTask == null) {
                        mUpdateTask = new UpdateTask();
                    }
                    mUpdateTask.startTask();
                    break;
                case ACTION_SERVICE_STOP:     //下载服务停止
                    L.v(TAG, "service stop broadcast");
                    if (mUpdateTask != null) {
                        mUpdateTask.stopTask();
                    }
                    break;
                case ACTION_IGNORE:
                    if (mAdapter != null) {
                        mAdapter.ignoreTask(intent.getStringExtra(Constance.KEY.STRING));
                    }
                    break;
                case GaoShouYouService.ACTION_INSTALL_COMPLETE:
                case GaoShouYouService.ACTION_UNINSTALL_COMPLETE:
                    handleID(intent);
                    break;
                default:
                    L.d(TAG, "download fragment = " + intent.getAction());
                    if (action.equals(ACTION_PRE)) {
                        lastTime = System.currentTimeMillis();
                        if (mUpdateTask == null) {
                            mUpdateTask = new UpdateTask();
                        }
                        mUpdateTask.startTask();
                    }
                    if (action.equals(ACTION_COMPLETE)) {
                        Intent intent1 = new Intent();
                        intent1.setAction(AMApkFragment.ACTION_UPDATE_DATA);
                        getContext().sendBroadcast(intent1);
                    }
                    DownloadEntity entity = intent.getParcelableExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY);
                    if (mAdapter != null) {
                        mAdapter.updateDownloadState(entity);
                    }
                    break;
            }
        }
    };

    private AMApkFragment() {
    }

    private AMApkFragment(int type) {
        mType = type;
    }

    /**
     * @param type {@link #ALL_APK}
     *             {@link #INSTALLED_APK}
     *             {@link #UPDATE_APK}
     * @return
     */
    public static AMApkFragment newInstance(int type) {
        AMApkFragment mFragment = new AMApkFragment(type);
        return mFragment;
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        Stack<AbsActivity> stacks = mActivity.getApp().getAppManager().getActivityStack();
        for (int i = 0, count = stacks.size(); i < count; i++) {
            AbsActivity activity = stacks.pop();
            if (!(activity instanceof MainActivity)) {
                activity.finish();
            }
        }
    }

    /**
     * 安装和卸载完成操作
     */
    private void handleID(Intent intent) {
        if (mType == ALL_APK) {
            getModule(AppManagerModule.class).getAllApk(true, Constance.PATH.ROOT_PATH);
            String packageName = intent.getStringExtra("packageName");
            for (ApkInfoEntity entity : mData) {
                if (entity.getApkPackage().equals(packageName)) {
                    entity.setApkInstallState(intent.getAction().equals(GaoShouYouService.ACTION_INSTALL_COMPLETE));
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else if (mType == INSTALLED_APK) {
            getModule(AppManagerModule.class).getInstalledApkInfo();
        } else if (mType == UPDATE_APK) {
            String packageName = intent.getStringExtra("packageName");
            for (ApkInfoEntity entity : mData) {
                ApkUpdateInfoEntity aEntity = entity.getUpdateInfoEntity();
                if (aEntity.getPackageName().equals(packageName) && aEntity.getUpdateState() == ApkUpdateInfoEntity.STATE_UPDATE_COMPLETE) {
                    mAdapter.removeItem(entity);
                }
            }
//            getModule(AppManagerModule.class).getUpdateInfo();
        } else if (mType == DOWNLOAD_APK) {
            getModule(AppManagerModule.class).getDownloadInfo();
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initWidget();
        if (mType == ALL_APK) {
            getModule(AppManagerModule.class).getAllApk(false, Constance.PATH.ROOT_PATH);
        } else if (mType == INSTALLED_APK) {
            getModule(AppManagerModule.class).getInstalledApkInfo();
        } else if (mType == UPDATE_APK) {
            getModule(AppManagerModule.class).getUpdateInfo();
        } else if (mType == DOWNLOAD_APK) {
            getModule(AppManagerModule.class).getDownloadInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(GaoShouYouService.ACTION_INSTALL_COMPLETE);
        filter.addAction(GaoShouYouService.ACTION_UNINSTALL_COMPLETE);
        if (mType == DOWNLOAD_APK) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    filter.addAction(ACTION_PRE);
                    filter.addAction(ACTION_DOWNLOAD_ING);
                    filter.addAction(ACTION_RESUME);
                    filter.addAction(ACTION_PAUSE);
                    filter.addAction(ACTION_COMPLETE);
                    filter.addAction(ACTION_STOP);
                    filter.addAction(ACTION_FAIL);
                    filter.addAction(ACTION_CANCEL);
                    filter.addAction(ACTION_IGNORE);
                    filter.addAction(ACTION_SERVICE_START);
                    filter.addAction(ACTION_SERVICE_STOP);
                    filter.addAction(ACTION_CLOSE_UPDATE_TIME);
                    mActivity.registerReceiver(mDownloadReceiver, filter);
                }
            }, 1000);
        } else if (mType == UPDATE_APK) {
            filter.addAction(ACTION_COMPLETE);
            filter.addAction(ACTION_CANCEL);
            mActivity.registerReceiver(mUpdateReceiver, filter);
        } else {
            filter.addAction(ACTION_UPDATE_DATA);
            mActivity.registerReceiver(mIDReceiver, filter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mType == DOWNLOAD_APK && mDownloadReceiver != null) {
            mActivity.unregisterReceiver(mDownloadReceiver);
        }
        if (mType == UPDATE_APK && mUpdateReceiver != null) {
            mActivity.unregisterReceiver(mUpdateReceiver);
        }
        if (mType == ALL_APK || mType == INSTALLED_APK) {
            if (mIDReceiver != null) {
                mActivity.unregisterReceiver(mIDReceiver);
            }
        }
    }

    /**
     * 添加数据
     */
    public void addItemData(ApkInfoEntity entity) {
        mAdapter.addItem(entity);
        hintTempView();
    }

    /**
     * 添加一组数据
     */
    public void addItemData(List<ApkInfoEntity> entities) {
        mAdapter.addItem(entities);
        hintTempView();
    }

    /**
     * 更新数据
     */
    public void updateAllData() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 清空选中状态
     */
    public void clearCheckState() {
        mAdapter.clearCheckState();
    }

    /**
     * 删除所有选中数据
     */
    public void removeAllCheckData() {
        mAdapter.removeAllCheckedItem();
    }

    /**
     * 设置Fragment监听
     *
     * @param listener
     */
    public void setOnAMFragmentListener(OnAMFragmentListener listener) {
        mFragmentListener = listener;
    }

    private void initWidget() {
        mAdapter = new AppManagerAdapter(getContext(), mData, mType);
        mList.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        mList.setLayoutManager(lm);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext());
        decoration.setDividerColor(getResources().getColor(R.color.skin_line_color));
        decoration.setDivierSpace(1);
        mList.addItemDecoration(decoration);
        mList.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new AbsRVHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View parent, int position) {
                int function = AMAppDetailDialog.FUNCTION_DELETE;
                if (mType == ALL_APK || mType == DOWNLOAD_APK) {
                    function = AMAppDetailDialog.FUNCTION_DELETE;
                } else if (mType == UPDATE_APK) {
                    function = AMAppDetailDialog.FUNCTION_IGNORE;
                } else if (mType == INSTALLED_APK) {
                    function = AMAppDetailDialog.FUNCTION_OPEN;
                }

                mCurrentItem = mData.get(position);
                String title;
                if (mType == UPDATE_APK) {
                    title = mCurrentItem.getUpdateInfoEntity().getAppName();
                } else {
                    title = mType == DOWNLOAD_APK ? mCurrentItem.getDownloadEntity().getName() : mCurrentItem.getApkName();
                }
                AMAppDetailDialog dialog = new AMAppDetailDialog(title, AMApkFragment.this, function);
                dialog.show(getChildFragmentManager(), "detail");
            }
        });
        mAdapter.setOnAMAdapterListener(new OnAMAdapterListener() {
            @Override
            public void onCheck(int type, int num, Set<ApkInfoEntity> checkData) {
                if (mFragmentListener != null) {
                    mFragmentListener.onCheckNum(AMApkFragment.this, type, num, checkData);
                }
            }

            @Override
            public void onItemNumChange(int type, int num) {
                if (mFragmentListener != null) {
                    mFragmentListener.onNum(AMApkFragment.this, type, num);
                }
            }

            @Override
            public void onButtonHandle(int type, int handleType, ApkInfoEntity entity) {
                if (mFragmentListener != null) {
                    mFragmentListener.onButtonHandle(AMApkFragment.this, type, handleType, entity);
                }
            }
        });

        if (mType == DOWNLOAD_APK) {
            if (AndroidUtils.isServiceRun(getContext(), DownloadService.class)) {
                if (mUpdateTask == null) {
                    mUpdateTask = new UpdateTask();
                }
                mUpdateTask.startTask();
            }
            mList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    mListScrollState = newState;
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 获取list可见item
     *
     * @return
     */
    private int[] getListVisiblePosition() {
        LinearLayoutManager lm = (LinearLayoutManager) mList.getLayoutManager();
        return new int[]{lm.findFirstVisibleItemPosition(), lm.findLastVisibleItemPosition()};
    }

    /**
     * 初始化列表
     *
     * @param data
     */
    private void setUpList(List<ApkInfoEntity> data) {
        if (data != null && data.size() > 0) {
            mData.clear();
            mData.addAll(data);
        }
        if (mType == DOWNLOAD_APK) {
            mAdapter.pre();
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_app_manager_tab_content;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.GET_ALL_APK_PATH) {
            List<ApkInfoEntity> data = (List<ApkInfoEntity>) obj;
            setUpList(data);
            if (mFragmentListener != null) {
                mFragmentListener.onNum(this, mType, mData.size());
            }
        } else if (result == ResultCode.DIALOG.AM_ITEM_FUNCTION) {  //弹出框操作
            int function = (int) obj;
            if (function == AMAppDetailDialog.FUNCTION_DELETE) {    //删除操作
                if (mType == AMApkFragment.ALL_APK) {
                    if (mFragmentListener != null) {
                        File file = new File(mCurrentItem.getApkPath());
                        if (file.exists()) {
                            file.delete();
                            mAdapter.removeItem(mCurrentItem);
                            S.i(mRootView, mCurrentItem.getApkName() + ".apk删除成功");
                        }
                    }
                } else if (mType == AMApkFragment.DOWNLOAD_APK) {
                    mAdapter.removeItem(mCurrentItem);
                    mActivity.sendBroadcast(new Intent(DownloadService.ACTION_STATE_CHANGE));
                }
                ((AppManagerActivity) mActivity).clearState();
            } else if (function == AMAppDetailDialog.FUNCTION_IGNORE) { //忽略更新
                mAdapter.removeItem(mCurrentItem);
                Intent intent = new Intent(ACTION_IGNORE);
                intent.putExtra(Constance.KEY.STRING, mCurrentItem.getUpdateInfoEntity().getDownloadUrl());
                mActivity.sendBroadcast(intent);
            } else if (function == AMAppDetailDialog.FUNCTION_OPEN) {
                //打开
                AndroidUtils.startOtherApp(getContext(), mCurrentItem.getApkPackage());
            }
        }
    }

    /**
     * 更新线程
     */
    class UpdateTask extends AsyncTask<Void, Void, Void> {
        private CountDownTimer timer;

        public void startTask() {
            doInBackground();
        }

        public void stopTask() {
            if (timer != null) {
                L.d(TAG, "stop update task");
                timer.cancel();
                timer = null;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (timer == null) {
                timer = new CountDownTimer(Integer.MAX_VALUE, UPDATE_INTERVAL) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (mListScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                            onProgressUpdate();
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                };
                timer.start();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (mAdapter != null) {
//                mAdapter.updateProgress(getListVisiblePosition());
                LinearLayoutManager lm = (LinearLayoutManager) mList.getLayoutManager();
                mAdapter.updateProgress(lm.findFirstVisibleItemPosition(), lm.findLastVisibleItemPosition());
            }
        }
    }
}

package com.touchrom.gaoshouyou.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.show.L;
import com.arialyy.frame.util.show.T;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.GSYSEntity;
import com.touchrom.gaoshouyou.entity.sql.ApkInfoEntity;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by lk on 2016/1/14.
 * 高手游系统服务,该服务为APP服务，负责操作APP的全局事件
 */
public class GaoShouYouService extends Service {
    public static final String TAG = "GaoShouYouService";
    public static final String ACTION_INSTALL = "com.lyy.gsy.install_app";  //安装APK
    public static final String ACTION_UNINSTALL = "com.lyy.gsy.uninstall_app";    //卸载APK
    public static final String ACTION_INSTALL_COMPLETE = "com.lyy.gsy.install_app_complete";    //apk安装完成
    public static final String ACTION_UNINSTALL_COMPLETE = "com.lyy.gsy.delete_app_complete";    //apk卸载完成
    private static final int INSTALL_APP = 0x01;
    private static final int UNINSTALL_APP = 0x02;
    private static final int INSTALL_COMPLETE = 0x03;
    private static final int REMOVE_COMPLETE = 0x04;

    private ServiceHandler mHandler;
    HandlerThread mHt;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            L.d(TAG, "action = " + action);
            GSYSEntity entity = intent.getParcelableExtra(Constance.SERVICE.GSY_SERVICE_ENTITY);
            if (entity == null) {
                entity = new GSYSEntity();
            }
            Task task = new Task(context, entity);
            switch (action) {

                case Intent.ACTION_PACKAGE_ADDED:   //接受系统安装apk广播
                    task.entity.setData(intent.getDataString());
                    mHandler.obtainMessage(INSTALL_COMPLETE, task).sendToTarget();
                    break;
                case Intent.ACTION_PACKAGE_REMOVED:
                    task.entity.setData(intent.getDataString());
                    mHandler.obtainMessage(REMOVE_COMPLETE, task).sendToTarget();
                    break;
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHt = new HandlerThread("gsyHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
        mHt.start();
        mHandler = new ServiceHandler(mHt.getLooper());
        L.i(TAG, "gsy service onCreate");
        IntentFilter filter = new IntentFilter();
//        filter.addAction(ACTION_INSTALL);
//        filter.addAction(ACTION_UNINSTALL);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
//        filter.addDataScheme("lyy");
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleCommand(Intent intent) {
        if (intent == null) {
            return;
        }
        String cmd = intent.getStringExtra(Constance.SERVICE.DOWNLOAD_CMD);
        if (TextUtils.isEmpty(cmd)) {
            return;
        }
        GSYSEntity entity = intent.getParcelableExtra(Constance.SERVICE.GSY_SERVICE_ENTITY);
        Task task = new Task(getApplicationContext(), entity);
        switch (cmd) {
            case ACTION_INSTALL:
                mHandler.obtainMessage(INSTALL_APP, task).sendToTarget();
                break;
            case ACTION_UNINSTALL:
                mHandler.obtainMessage(UNINSTALL_APP, task).sendToTarget();
                break;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        if (mHt != null) {
            mHt.quit();
        }
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Task task = (Task) msg.obj;
            switch (msg.what) {
                case INSTALL_APP:
                    installApk(task);
                    break;
                case UNINSTALL_APP:
                    uninstall(task);
                    break;
                case INSTALL_COMPLETE:
                    installComplete(task);
                    break;
                case REMOVE_COMPLETE:
                    removeComplete(task);
                    break;
            }
        }

        /**
         * 卸载完成操作
         */
        private void removeComplete(Task task) {
            Intent intent = new Intent();
            String str = task.entity.getData();
            String packageName = str.substring(8, str.length());
            intent.putExtra("packageName", packageName);
            intent.setAction(ACTION_UNINSTALL_COMPLETE);
            sendBroadcast(intent);
        }

        /**
         * 安装完成
         */
        private void installComplete(Task task) {
            Intent intent = new Intent();
            String str = task.entity.getData();
            String packageName = str.substring(8, str.length());
            intent.putExtra("packageName", packageName);
            intent.setAction(ACTION_INSTALL_COMPLETE);
            sendBroadcast(intent);
            removeApk(packageName);
        }

        /**
         * 删除Apk
         */
        private void removeApk(String packageName) {
            if (!AppManager.getInstance().getSetting().isAutoDeleteApk()) {
                return;
            }
            List<ApkInfoEntity> list = DataSupport.where("apkPackage=?", packageName).find(ApkInfoEntity.class);
            if (list != null && list.size() > 0) {
                String apkPath = list.get(0).getApkPath();
                L.d(TAG, "path = " + apkPath);
                File file = new File(apkPath);
                if (file.exists()) {
                    file.delete();
                }
            }
        }

        /**
         * 安装apk
         */
        private void installApk(final Task task) {
            T.showShort(getApplicationContext(), "开始安装 " + task.entity.getName());
            if (AppManager.getInstance().getSetting().isQuicklyHandleApk()) {
                Observable.just("").map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return AndroidUtils.installInBackground(task.entity.getData());
                    }
                }).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        T.showShort(getApplicationContext(), task.entity.getName() + " 安装" + (aBoolean ? "成功" : "失败"));
                        if (aBoolean) {
                            Intent intent = new Intent();
                            intent.setAction(ACTION_INSTALL_COMPLETE);
                            intent.putExtra("packageName", AndroidUtils.getApkPackageName(getApplicationContext(), task.entity.getData()));
                            sendBroadcast(intent);
                            if (AppManager.getInstance().getSetting().isAutoDeleteApk()) {
                                File apk = new File(task.entity.getData());
                                if (apk.exists()) {
                                    apk.delete();
                                }
                            }
                        }
                    }
                });
            } else {
                AndroidUtils.install(task.context, new File(task.entity.getData()));
//                InstallHelp.installApk(task.context, task.entity.getData());
            }
        }

        /**
         * 删除软件
         */
        private void uninstall(final Task task) {
            T.showShort(getApplicationContext(), "开始卸载" + task.entity.getName());
            if (AppManager.getInstance().getSetting().isQuicklyHandleApk()) {
                Observable.just("").map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return AndroidUtils.uninstallInBackground(task.entity.getData());
                    }
                }).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        T.showShort(getApplicationContext(), task.entity.getName() + " 卸载" + (aBoolean ? "成功" : "失败"));
                        if (aBoolean) {
                            Intent intent = new Intent();
                            intent.setAction(ACTION_UNINSTALL_COMPLETE);
                            intent.putExtra("packageName", task.entity.getData());
                            sendBroadcast(intent);
                        }
                    }
                });
            } else {
                AndroidUtils.uninstall(task.context, task.entity.getData());
            }
        }
    }

    private class Task {
        Context context;
        GSYSEntity entity;

        public Task(Context context, GSYSEntity entity) {
            this.context = context;
            this.entity = entity;
        }
    }
}

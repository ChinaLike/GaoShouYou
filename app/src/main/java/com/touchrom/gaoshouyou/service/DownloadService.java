package com.touchrom.gaoshouyou.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;

import com.arialyy.downloadutil.DownLoadUtil;
import com.arialyy.downloadutil.DownloadListener;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.CalendarUtils;
import com.arialyy.frame.util.FileUtil;
import com.arialyy.frame.util.NetUtils;
import com.arialyy.frame.util.show.L;
import com.arialyy.frame.util.show.T;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.dialog.MsgDialog1;
import com.touchrom.gaoshouyou.entity.GSYSEntity;
import com.touchrom.gaoshouyou.entity.SettingEntity;
import com.touchrom.gaoshouyou.entity.sql.ApkInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.ApkUpdateInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.fragment.AMApkFragment;
import com.touchrom.gaoshouyou.help.DownloadHelp;
import com.touchrom.gaoshouyou.help.GSYServiceHelp;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lk on 2015/12/30.
 * 下载服务
 */
public class DownloadService extends Service {
    public static final String TAG = "DownloadService";
    public static final String ACTION_START = "com.lyy.action.start";
    public static final String ACTION_STOP = "com.lyy.action.stop";
    public static final String ACTION_REMOVE_TASK = "com.lyy.action.remove_task";
    public static final String ACTION_STATE_CHANGE = "com.lyy.action.state_change";
    public static final int D_START = 0, D_STOP = 1, D_CANCEL = 2, D_COMPLETE = 3, D_FAIL = 4;
    private static final int DOWNLOAD_TASK_NUM = 2;  //同时下载数量
    private static final int DOWNLOAD_MAX_NUM = Integer.MAX_VALUE;  //最大下载任务数
    private static final int DOWNLOAD_AGAIN_NUM = 5;  //重试次数
    private static final int DOWNLOAD_FAIL_INTERVAL = 1000 * 5;  //失败重试间隔
    private Map<String, Task> mTask = new HashMap<>();
    private ArrayBlockingQueue<Task> mCurrentTask = new ArrayBlockingQueue<>(DOWNLOAD_TASK_NUM);
    private LinkedBlockingQueue<Task> mWaitQueue = new LinkedBlockingQueue<>(DOWNLOAD_MAX_NUM);
    private MsgDialog1 mMsgDialog;
    private Set<Task> mTaskBack = new HashSet<>();
    private boolean isForceDownload = false;
    private boolean isNetError = false;
    private SettingEntity mSetting;
    /**
     * 下载任务调度器
     */
    private Handler mSchedulers;
    DownloadEntity currentEntity;
    int mFlag;
    HandlerThread mHt;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            L.d(TAG, "action = " + action);
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                checkNet();
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
        L.i(TAG, "download service start");
        mHt = new HandlerThread("DownloadThread", Process.THREAD_PRIORITY_BACKGROUND);
        mHt.start();
        mSchedulers = new ServiceHandler(mHt.getLooper());
        addDownloadTask();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);
        mSetting = AppManager.getInstance().getSetting();
        sendBroadcast(new Intent(AMApkFragment.ACTION_SERVICE_START));
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
        DownloadEntity entity = intent.getParcelableExtra(Constance.SERVICE.DOWNLOAD_ENTITY);
        mFlag = intent.getIntExtra("flag", -1);
        if (cmd.equals(ACTION_START)) {
            startCmdFlow(cmd, entity);
        } else {
            normalCmdFlow(cmd, entity);
        }
    }

    /**
     * 开始命令流程
     */
    private void startCmdFlow(String action, DownloadEntity entity) {
        if (taskIsExist(entity) && mFlag == DownloadHelp.DOWNLOAD_FLAG) {
            currentEntity = entity;
            showHintDialog(entity);
        } else {
            normalCmdFlow(action, entity);
        }
    }

    /**
     * 非开始命令流程
     */
    private void normalCmdFlow(String action, DownloadEntity entity) {
        if (entity == null) {
            judgeStopService();
        }
        Task task = mTask.get(entity.getDownloadUrl());
        if (task == null) {
            task = new Task(entity, new DownLoadUtil());
            task.flag = mFlag;
            if (entity.getState() == DownloadEntity.STATE_WAIT) {
                mWaitQueue.offer(task);
            }
            saveDownloadEntity2Db(entity);
            mTask.put(entity.getDownloadUrl(), task);
        } else {
            task.canStart = true;
        }

        switch (action) {
            case ACTION_START:
                mSchedulers.obtainMessage(D_START, task).sendToTarget();
                break;
            case ACTION_STOP:
                mSchedulers.obtainMessage(D_STOP, task).sendToTarget();
                break;
            case ACTION_REMOVE_TASK:
                mSchedulers.obtainMessage(D_CANCEL, task).sendToTarget();
                break;
        }
        Intent changeIntent = new Intent();
        changeIntent.setAction(ACTION_STATE_CHANGE);
        sendBroadcast(changeIntent);
    }

    /**
     * 检查下载数据库是否有该下载任务
     */
    private boolean taskIsExist(DownloadEntity dEntity) {
        List<DownloadEntity> dEntitys = DownloadEntity.findAll(DownloadEntity.class);
        for (DownloadEntity entity : dEntitys) {
            if (entity.getDownloadUrl().equals(dEntity.getDownloadUrl())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 显示提示对话框
     */
    private void showHintDialog(DownloadEntity dEntity) {
        MsgDialog1 msgDialog1 = new MsgDialog1(getApplicationContext(), this, 2);
        msgDialog1.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        msgDialog1.setTitle("注意！！");
        msgDialog1.setMsg("下载任务：" + dEntity.getName() + "已经存在，是否重新下载？");
        msgDialog1.show();
    }

    /**
     * 检查网络
     *
     * @return true --> 处于wifi状态，可进行下载
     */
    private synchronized boolean checkNet() {
        if (NetUtils.isConnected(getApplicationContext())) {
            if (!NetUtils.isWifi(getApplicationContext())) { //没有Wifi
                stopAllTask();
                if (mMsgDialog == null) {
                    mMsgDialog = new MsgDialog1(getApplicationContext(), this);
                    if (AndroidVersionUtil.hasKitKat()) {
                        mMsgDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                    } else {
                        mMsgDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    }
                }
                String msg = "当前网络是移动网络，是否继续下载？";
                if (mMsgDialog != null && !mMsgDialog.isShowing()) {
                    mMsgDialog.setTitle("注意");
                    mMsgDialog.setMsg(msg);
                    mMsgDialog.setRequestCode(1);
                    mMsgDialog.show();
                }
                return false;
            } else {
                isForceDownload = false;
                return true;
            }
        } else { //没有网络
//            wifi 却换到其它网络有个延时过程，需要延时处理
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    String msg = "当没有前网络连接，是否前往网络设置打开Wifi？";
//                    if (mMsgDialog != null && !mMsgDialog.isShowing()) {
//                        mMsgDialog.setTitle("注意");
//                        mMsgDialog.setMsg(msg);
//                        mMsgDialog.setRequestCode(0);
//                        mMsgDialog.show();
//                    }
//                }
//            }, 1000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mMsgDialog == null || !mMsgDialog.isShowing()) {
//                        String msg = "网络不给力，正在停止下载...";
//                        T.showShort(getApplicationContext(), msg);
                        showMsg("网络不给力，正在停止下载...");
                        judgeStopService();
                    }
                }
            }, 2000);
            return false;
        }
    }

    private void showMsg(final String msg) {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Looper.myLooper();
                T.showShort(getApplicationContext(), msg);
                Looper.loop();
            }
        }.start();
    }

    /**
     * 暂停所有任务
     */
    private void stopAllTask() {
        isNetError = true;
        if (mTask != null && mTask.size() > 0) {
            mTaskBack.clear();
            Set set = mTask.entrySet();
            for (Object aSet : set) {
                Map.Entry entry = (Map.Entry) aSet;
                Task task = (Task) entry.getValue();
                DownLoadUtil util = task.dUtil;
                if (util != null && util.isDownloading()) {
                    mTaskBack.add(task);
                    util.stopDownload();
                    L.d(TAG, "pause download ==> " + task.dEntity.getName());
                }
            }
        }
    }

    /**
     * 恢复因网络导致暂停或失败的任务
     */
    private void resumeAllFailTask() {
        isNetError = false;
        if (!mCurrentTask.isEmpty()) {
            ArrayBlockingQueue<Task> back = new ArrayBlockingQueue<>(DOWNLOAD_TASK_NUM);
            while (!mCurrentTask.isEmpty()) {
                back.offer(mCurrentTask.poll());
            }
            if (!back.isEmpty()) {
                mCurrentTask.clear();
                for (Task task : back) {
                    mSchedulers.obtainMessage(D_START, task).sendToTarget();
                }
            }
        } else if (!mTaskBack.isEmpty()) {
            for (Task task : mTaskBack) {
                mSchedulers.obtainMessage(D_START, task).sendToTarget();
            }
        } else {
            Set set = mTask.entrySet();
            int i = 0;
            for (Object aSet : set) {
                if (i >= DOWNLOAD_TASK_NUM) {
                    return;
                }
                Map.Entry entry = (Map.Entry) aSet;
                Task task = (Task) entry.getValue();
                mSchedulers.obtainMessage(D_START, task).sendToTarget();
                i++;
            }
        }
    }

    /**
     * 下载
     */
    private void download(Task task) {
        DownloadEntity entity = task.dEntity;
        DownLoadUtil util = task.dUtil;
        if (util != null) {
            util.download(getApplicationContext(), entity.getDownloadUrl(),
                    mSetting.getDownloadLocation() + entity.getName() + ".apk",
                    new MDownloadListener(task));
        }
    }

    /**
     * 下载结束操作
     * 下载结束状态有：完成，失败，停止，取消下载
     */
    private void handleDownloadEnd(DownloadEntity entity) {
        L.d(TAG, "handle download end");
        mTask.remove(entity.getDownloadUrl());
        judgeStopService();
    }

    /**
     * 判断是否停止服务
     */
    private void judgeStopService() {
        if (mCurrentTask.size() == 0) {
            if (mMsgDialog != null && mMsgDialog.isShowing()) {
                return;
            }
            stopSelf();
        }
    }

    /**
     * 将下载实体存储到数据库
     *
     * @param downloadEntity 下载实体
     */
    private void saveDownloadEntity2Db(DownloadEntity downloadEntity) {
        Task t = mTask.get(downloadEntity.getDownloadUrl());
        if (t != null) {
            t.dEntity = downloadEntity;
        }
        List<DownloadEntity> list = DataSupport.where("downloadUrl=?", downloadEntity.getDownloadUrl()).find(DownloadEntity.class);
        if (list == null || list.size() == 0) {
            downloadEntity.save();
        } else {
            downloadEntity.updateAll("downloadUrl=?", downloadEntity.getDownloadUrl());
        }
    }


    /**
     * 添加下载任务
     */
    private void addDownloadTask() {
        List<DownloadEntity> list = DownloadEntity.findAll(DownloadEntity.class);
        if (list != null && list.size() > 0) {
            for (DownloadEntity entity : list) {
                int state = entity.getState();
                if (state == DownloadEntity.STATE_COMPLETE) {
                    continue;
                }
                Task task = new Task(entity, new DownLoadUtil());
                mTask.put(entity.getDownloadUrl(), task);
                if (state == DownloadEntity.STATE_STOP || state == DownloadEntity.STATE_FAIL || state == DownloadEntity.STATE_DOWNLOAD_ING) {
                    continue;
                }
                mWaitQueue.offer(task);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMsgDialog != null) {
            mMsgDialog.dismiss();
        }
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }

        if (mTask != null && mTask.size() > 0) {
            Set set = mTask.entrySet();
            for (Object aSet : set) {
                Map.Entry entry = (Map.Entry) aSet;
                Task task = (Task) entry.getValue();
                DownLoadUtil util = task.dUtil;
                if (util != null) {
                    util.stopDownload();
                    L.d(TAG, "stop download ==> " + task.dEntity.getName());
                }
            }
        }
        sendBroadcast(new Intent(AMApkFragment.ACTION_SERVICE_STOP));
        if (mHt != null) {
            if (mHt.getLooper() == Looper.myLooper()) {
                mHt.quit();
            }
        }
        L.e(TAG, "download service stop");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    /**
     * 对话框数据回调
     *
     * @param result
     * @param obj
     */
    protected void dataCallback(int result, Object obj) {
        Integer requestCode = (Integer) obj;
        if (requestCode == 0) {  //没有网络
            if (result == ResultCode.DIALOG.MSG_DIALOG_ENTER) {
                if (AndroidVersionUtil.hasHoneycomb()) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent("/");
                    ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(cm);
                    intent.setAction("android.intent.action.VIEW");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else {
                L.e(TAG, "没有网络 ==> server stop");
                stopSelf();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 2000);
            }
        } else if (requestCode == 1) {  //wifi断开
            if (result == ResultCode.DIALOG.MSG_DIALOG_CANCEL) {
                L.e(TAG, "没有移动网络 ==> server stop");
                stopSelf();
            } else {
                isForceDownload = true;
                resumeAllFailTask();
            }
        } else {
            if (result == ResultCode.DIALOG.MSG_DIALOG_ENTER) {
                Task task = mTask.get(currentEntity.getDownloadUrl());
                if (task == null) {
                    task = new Task(currentEntity, new DownLoadUtil());
                    task.flag = mFlag;
                    mTask.put(currentEntity.getDownloadUrl(), task);
                }
                mSchedulers.obtainMessage(D_START, task).sendToTarget();
            } else {
//                normalCmdFlow(currentAction, currentEntity);
            }
        }
    }

    private class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Task task = (Task) msg.obj;
            if (task == null) {
                L.w(TAG, "task is null");
                return;
            }
            if (!isForceDownload && !checkNet()) {
                L.w(TAG, "没有网络或者不是wifi连接状态");
//                T.showShort(getApplicationContext(), "网络不给力，正在停止下载...");
                removeQueueElement(task);
                return;
            }

            if (!task.canStart) {
                L.d(TAG, "不进行下载");
                return;
            }
            switch (msg.what) {
                case D_START: //开始
                    startDownload(task);
                    break;
                case D_STOP: //停止
                    stopDownload(task);
                    break;
                case D_CANCEL: //删除任务
                    cancelDownload(task);
                    break;
                case D_COMPLETE: //完成
                    completeDownload(task);
                    break;
                case D_FAIL: //失败
                    failDownload(task);
                    break;
            }
        }

        /**
         * 下载失败
         */
        private void failDownload(final Task task) {
            L.d(TAG, "handle fail download");
            if (AppManager.getInstance().getSetting().isDownloadAgain() && task.failNum < DOWNLOAD_AGAIN_NUM) {
                showMsg(task.dEntity.getName() + " 下载失败，正在重新下载...");
                removeQueueElement(task);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startDownload(task);
                    }
                }, DOWNLOAD_FAIL_INTERVAL);
            } else {
                showMsg(task.dEntity.getName() + " 下载失败，正在停止下载...");
                stopDownload(task);
            }
        }

        /**
         * 删除任务
         */
        private void cancelDownload(Task task) {
            L.d(TAG, "handle cancel download");
            DownloadEntity entity = task.dEntity;
            DataSupport.deleteAll(DownloadEntity.class, "downloadUrl=?", entity.getDownloadUrl());
            DownLoadUtil util = task.dUtil;
            if (util != null && util.isDownloading()) {
                util.cancelDownload();
                L.i(TAG, "cancel(触发条件：正常状态)");
            } else {
                L.d(TAG, util == null ? "util == null" : "isDownloading ==> false");
                L.d(TAG, "取消下载任务(该触发条件为：任务没在下载)");
            }
            File configF = new File(getApplicationContext().getFilesDir().getPath() + "/temp/" + task.dEntity.getName() + ".apk" + ".properties");
            if (configF.exists()) {
                configF.delete();
            }
            Intent cancelIntent = new Intent(AMApkFragment.ACTION_CANCEL);
            cancelIntent.putExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY, entity);
            sendBroadcast(cancelIntent);
            mTask.remove(task.dEntity.getDownloadUrl());
            updateQueue(task);
            Task nextT = getWaitTask(task);
            if (nextT != null) {
                startDownload(nextT);
            }
            judgeStopService();
        }

        /**
         * 完成
         */
        private void completeDownload(Task task) {
            L.d(TAG, "handle complete download");
            if (task.dEntity.isCaAutoInstall() && AppManager.getInstance().getSetting().isAutoInstallApk()) {
                GSYSEntity entity = new GSYSEntity();
                entity.setAction(GaoShouYouService.ACTION_INSTALL);
                entity.setData(mSetting.getDownloadLocation() + task.dEntity.getName() + ".apk");
                entity.setName(task.dEntity.getName());
                GSYServiceHelp.newInstance().run(getApplicationContext(), entity);
            }
            updateQueue(task);
            Task nextT = getWaitTask(task);
            if (nextT != null) {
                startDownload(nextT);
            }
            handleDownloadEnd(task.dEntity);
        }

        /**
         * 更新下载队列
         */
        private void updateQueue(Task task) {
            removeQueueElement(task);
            mWaitQueue.clear();
            Set set = mTask.entrySet();
            for (Object aSet : set) {
                Map.Entry entry = (Map.Entry) aSet;
                Task bTask = (Task) entry.getValue();
                int state = bTask.dEntity.getState();
                if (state == DownloadEntity.STATE_STOP || state == DownloadEntity.STATE_FAIL || state == DownloadEntity.STATE_DOWNLOAD_ING) {
                    continue;
                }
                mWaitQueue.offer(bTask);
            }
        }

        /**
         * 停止
         */
        private void stopDownload(Task task) {
            L.d(TAG, "handle stop download");
            if (task != null) {
                DownLoadUtil util = task.dUtil;
                if (util != null && util.isDownloading()) {
                    util.stopDownload();
                    L.d(TAG, "停止下载(触发条件：正常状态)");
                } else {
                    L.d(TAG, "停止下载任务(该触发条件为：任务没在下载)");
                }
                Intent stopIntent = new Intent();
                task.dEntity.setState(DownloadEntity.STATE_STOP);
                saveDownloadEntity2Db(task.dEntity);
                stopIntent.setAction(AMApkFragment.ACTION_STOP);
                stopIntent.putExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY, task.dEntity);
                sendBroadcast(stopIntent);
                updateQueue(task);
                Task nextT = getWaitTask(task);
                if (nextT != null) {
                    startDownload(nextT);
                }
                judgeStopService();
            }
        }

        /**
         * 启动下载
         */
        private synchronized void startDownload(Task task) {
            if (task.dUtil.isDownloading()) {
                L.d(TAG, task.dEntity.getName() + " ==> 正在下载...");
                return;
            }
            if (hasTask(task)) {
                L.d(TAG, "queue has task ==> " + task.dEntity.getName());
                return;
            }
            //过滤新下载的，任务队列满时，新下载的应处于等待状态
            if (mCurrentTask.size() >= DOWNLOAD_TASK_NUM && task.flag == DownloadHelp.DOWNLOAD_FLAG) {
                L.d(TAG, "taskName ==> " + task.dEntity.getName());
                L.d(TAG, "taskQueue size ==> " + mCurrentTask.size());
                return;
            }
            DownloadEntity dEntity = task.dEntity;
            saveDownloadEntity2Db(dEntity);
            if (!mCurrentTask.offer(task)) { //队列已经满了
                L.d(TAG, "task " + dEntity.getName() + " poll");
                Task firstT = mCurrentTask.poll();
                firstT.dUtil.stopDownload();
                mCurrentTask.offer(task);
            }
            download(task);
        }

        /**
         * 下载队列里面是否有指定的下载任务
         *
         * @param task
         * @return
         */
        private boolean hasTask(Task task) {
            for (Task t : mCurrentTask) {
                if (t.dEntity.getDownloadUrl().equals(task.dEntity.getDownloadUrl())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 删除队列元素，不能直接使用队列自带的remove,那方法没效果
         */
        private void removeQueueElement(Task task) {
            Set<Task> temp = new HashSet<>();
            while (!mCurrentTask.isEmpty()) {
                Task t = mCurrentTask.poll();
                temp.add(t);
            }
            for (Task t1 : temp) {
                if (!t1.dEntity.getDownloadUrl().equals(task.dEntity.getDownloadUrl())) {
                    mCurrentTask.offer(t1);
                }
            }
        }

        /**
         * 获取等待中的任务
         *
         * @param task 已完成的任务
         */
        private synchronized Task getWaitTask(Task task) {
            Task nextT = mWaitQueue.poll();
            if (nextT == null) {
                return null;
            }
            if (task.dEntity.getDownloadUrl().equals(nextT.dEntity.getDownloadUrl())) {
                return getWaitTask(nextT);
            }
            L.d(TAG, "nextT = " + nextT.dEntity.getName());
            return nextT;
        }
    }

    private class MDownloadListener extends DownloadListener {
        private DownloadEntity entity;
        Intent sendIntent = new Intent();
        long INTERVAL = 1024 * 10;   //10k大小的间隔
        long lastLen = 0;   //上一次发送长度
        private Task task;

        MDownloadListener(Task task) {
            this.task = task;
            this.entity = task.dEntity;
        }

        @Override
        public void onPreDownload(HttpURLConnection connection) {
            super.onPreDownload(connection);
            L.d(TAG, "download pre ==> " + entity.getName());
            long maxLen = connection.getContentLength();
            Intent preIntent = new Intent();
            preIntent.setAction(AMApkFragment.ACTION_PRE);
            entity.setMaxLen(maxLen);
            entity.setState(DownloadEntity.STATE_DOWNLOAD_ING);
            entity.setSize(FileUtil.formatFileSize(maxLen));
            saveDownloadEntity2Db(entity);
            preIntent.putExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY, entity);
            sendBroadcast(preIntent);
            //初始化下载中的intent
            sendIntent.setAction(AMApkFragment.ACTION_DOWNLOAD_ING);
            sendIntent.putExtra("key", entity.getDownloadUrl());
        }

        @Override
        public void onProgress(long currentLocation) {
            super.onProgress(currentLocation);
            if (currentLocation - lastLen > INTERVAL) { //不要太过于频繁发送广播
                sendIntent.putExtra("progress", currentLocation);
                sendBroadcast(sendIntent);
                lastLen = currentLocation;
            }
        }

        @Override
        public void onResume(long resumeLocation) {
            super.onResume(resumeLocation);
            L.i(TAG, "download resume ==> " + entity.getName());
            entity.setState(DownloadEntity.STATE_DOWNLOAD_ING);
            saveDownloadEntity2Db(entity);
            Intent resumeIntent = new Intent();
            resumeIntent.setAction(AMApkFragment.ACTION_RESUME);
            resumeIntent.putExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY, entity);
            resumeIntent.putExtra("resumeLocation", resumeLocation);
            sendBroadcast(resumeIntent);
        }

        @Override
        public void onCancel() {
            super.onCancel();
            L.i(TAG, "download cancel ==> " + entity.getName());
            Intent cancelIntent = new Intent(AMApkFragment.ACTION_CANCEL);
            cancelIntent.putExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY, entity);
            sendBroadcast(cancelIntent);
        }

        @Override
        public void onStop(long stopLocation) {
            super.onStop(stopLocation);
            L.d(TAG, "download stop ==> " + entity.getName());
            entity.setState(isNetError ? DownloadEntity.STATE_WAIT : DownloadEntity.STATE_STOP);
            entity.setCurrentProgress(stopLocation);
            saveDownloadEntity2Db(entity);
            Intent stopIntent = new Intent();
            stopIntent.setAction(AMApkFragment.ACTION_STOP);
            stopIntent.putExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY, entity);
            stopIntent.putExtra("stopLocation", stopLocation);
            sendBroadcast(stopIntent);
            handleDownloadEnd(entity);
        }

        @Override
        public void onFail() {
            super.onFail();
            L.i(TAG, "download fail ==> " + entity.getName());
            entity.setState(DownloadEntity.STATE_FAIL);
            entity.setCurrentProgress(0);
            task.failNum++;
            saveDownloadEntity2Db(entity);
            Intent failIntent = new Intent();
            failIntent.setAction(AMApkFragment.ACTION_FAIL);
            failIntent.putExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY, entity);
            sendBroadcast(failIntent);

            Task task = mTask.get(entity.getDownloadUrl());
            mSchedulers.obtainMessage(D_FAIL, task).sendToTarget();
        }

        @Override
        public void onComplete() {
            super.onComplete();
            L.i(TAG, "download complete ==> " + entity.getName());
            entity.setState(DownloadEntity.STATE_COMPLETE);
            entity.setCompleteTime(CalendarUtils.getData());
            entity.setDownloadComplete(true);
            entity.setCurrentProgress(entity.getMaxLen());
            String path = mSetting.getDownloadLocation() + entity.getName() + ".apk";
            String packageName = AndroidUtils.getApkPackageName(getApplicationContext(), path);
            PackageManager pm = getApplicationContext().getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                // 需要有下面两句才能准确获取到应用名！！
                appInfo.sourceDir = path;
                appInfo.publicSourceDir = path;
                entity.setApkVersionConde(info.versionCode);
            }
            entity.setPackageName(packageName);
            saveDownloadEntity2Db(entity);
            Observable.just("").delay(1000, TimeUnit.MILLISECONDS).subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            //更新更新数据库
                            List<ApkUpdateInfoEntity> apkUpdateEntity = DataSupport
                                    .where("downloadUrl=?", entity.getDownloadUrl()).find(ApkUpdateInfoEntity.class);
                            if (apkUpdateEntity != null && apkUpdateEntity.size() > 0) {
                                ApkUpdateInfoEntity apkEntity = apkUpdateEntity.get(0);
                                apkEntity.setUpdateState(ApkUpdateInfoEntity.STATE_UPDATE_COMPLETE);
                                apkEntity.updateAll("downloadUrl=?", entity.getDownloadUrl());
                            }
                            //更新安装包数据库
                            saveInfoToDb(mSetting.getDownloadLocation() + entity.getName() + ".apk");
                            Intent completeIntent = new Intent();
                            completeIntent.setAction(AMApkFragment.ACTION_COMPLETE);
                            completeIntent.putExtra(Constance.SERVICE.DOWNLOAD_SEND_ENTITY, entity);
                            sendBroadcast(completeIntent);

                            Task task = mTask.get(entity.getDownloadUrl());
                            mSchedulers.obtainMessage(D_COMPLETE, task).sendToTarget();
                        }
                    });
        }

        /**
         * 将信息存储到数据库
         */

        private void saveInfoToDb(String apkPath) {
            PackageManager pm = getApplicationContext().getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                // 需要有下面两句才能准确获取到应用名！！
                appInfo.sourceDir = apkPath;
                appInfo.publicSourceDir = apkPath;
                ApkInfoEntity entity = new ApkInfoEntity();
                entity.setApkPath(apkPath);
                entity.setApkName(appInfo.loadLabel(pm).toString());
                entity.setApkPackage(appInfo.packageName);
                entity.setApkVersion(info.versionName);
                entity.setApkSize(FileUtil.formatFileSize(new File(apkPath).length()));
                entity.setApkInstallState(isInstalled(pm, appInfo.packageName));
                List<ApkInfoEntity> list = DataSupport.where("apkPackage=?", entity.getApkPackage()).find(ApkInfoEntity.class);
                if (list == null || list.size() == 0) {
                    entity.save();
                } else {
                    list.get(0).updateAll("apkPackage=?", entity.getApkPackage());
                }
            }
        }

        /**
         * 判断是否安装
         */
        private boolean isInstalled(PackageManager pm, String packageName) {
            try {
                pm.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                return true;
            } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
                return false;
            }
        }

    }

    private class Task implements Cloneable {
        DownloadEntity dEntity;
        DownLoadUtil dUtil;
        boolean canStart = true;
        int flag = -1;
        int failNum = 0;

        public Task(DownloadEntity dEntity, DownLoadUtil dUtil) {
            this.dEntity = dEntity;
            this.dUtil = dUtil;
        }

        @Override
        protected Task clone() throws CloneNotSupportedException {
            Task newTask = (Task) super.clone();
            newTask.dEntity = this.dEntity.clone();
            return newTask;
        }
    }
}

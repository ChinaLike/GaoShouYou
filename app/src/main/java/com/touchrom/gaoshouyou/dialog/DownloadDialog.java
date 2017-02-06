package com.touchrom.gaoshouyou.dialog;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.arialyy.downloadutil.DownLoadUtil;
import com.arialyy.downloadutil.DownloadListener;
import com.lyy.ui.widget.HorizontalProgressBarWithNumber;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseDialog;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.DialogDownloadBinding;
import com.touchrom.gaoshouyou.entity.LauncherEntity;
import com.touchrom.gaoshouyou.entity.NotificationEntity;
import com.touchrom.gaoshouyou.help.InstallHelp;
import com.touchrom.gaoshouyou.notification.DownloadNotification;
import com.touchrom.gaoshouyou.util.S;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import butterknife.InjectView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lyy on 2015/11/12.
 * APK下载对话框
 */
@SuppressLint("ValidFragment")
public class DownloadDialog extends BaseDialog<DialogDownloadBinding> {
    @InjectView(R.id.progress)
    HorizontalProgressBarWithNumber mProgress;
    LauncherEntity mEntity;
    int mFileLen;
    NotificationManager mNm;
    NotificationCompat.Builder mBuilder;
    Notification notify;

    public DownloadDialog(LauncherEntity entity, Object obj) {
        super(obj);
        mEntity = entity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (mEntity.isForcedUpdate()) {
            setCancelable(false);
        }
        NotificationEntity entity = new NotificationEntity();
        entity.setTitle("gaoShouYou.apk");
        entity.setIcon(R.mipmap.ic_launcher);
        mBuilder = new DownloadNotification().create(mActivity, entity);
        notify = mBuilder.build();
        mNm = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNm.notify(Constance.NOTIFICATION.VERSION_APK_ID, notify);
        startDownload();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_download;
    }

    @Override
    public void onDestroy() {
        if (!mEntity.isForcedUpdate()) {
            S.i(mActivity.getRootView(), "正在进入后台下载");
        }
        super.onDestroy();
    }

    private void startDownload() {
        new DownLoadUtil().download(mActivity, mEntity.getDownloadUrl(), Constance.PATH.NEW_APK_PATH, new DownloadListener() {
            @Override
            public void onPreDownload(HttpURLConnection connection) {
                super.onPreDownload(connection);
                mFileLen = connection.getContentLength();
                mProgress.setMax(100);
            }

            @Override
            public void onProgress(long currentLocation) {
                super.onProgress(currentLocation);
                final int progress = (int) (currentLocation * 100 / mFileLen);
                mProgress.setProgress(progress);
                mBuilder.setProgress(100, progress, false);
                mNm.notify(Constance.NOTIFICATION.VERSION_APK_ID, mBuilder.build());
            }

            @Override
            public void onFail() {
                super.onFail();
                dismiss();
                getSimplerModule().onDialog(ResultCode.DIALOG.DOWNLOAD, null);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                Observable.just(new File(Constance.PATH.NEW_APK_PATH))
                        .delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                        .subscribe(new Action1<File>() {
                            @Override
                            public void call(File file) {
                                mBuilder.setContentText("下载完成,点击安装")
                                        .setProgress(0, 0, false);
                                mBuilder.setContentIntent(DownloadNotification.createIntent(mActivity));
                                mBuilder.setContentInfo("下载完成");
                                mNm.notify(Constance.NOTIFICATION.VERSION_APK_ID, mBuilder.build());
                                DownloadDialog.this.dismiss();
                                InstallHelp.installApk(mActivity);
                                getSimplerModule().onDialog(ResultCode.DIALOG.DOWNLOAD, null);
                            }
                        });
            }
        });
    }

}

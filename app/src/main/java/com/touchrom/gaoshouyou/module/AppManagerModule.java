package com.touchrom.gaoshouyou.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.FileUtil;
import com.arialyy.frame.util.show.L;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.sql.ApkInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.ApkUpdateInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.help.RegexHelp;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.service.DownloadService;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lyy on 2015/12/21.
 * 应用管理模型
 */
public class AppManagerModule extends BaseModule {
    private int mApkNum = 0;
    private List<ApkInfoEntity> mAllApkEntity = new ArrayList<>();  //APk信息实体集合
    private static final long INTERVAL_TIME = 1000 * 60 * 5;

    public AppManagerModule(Context context) {
        super(context);
    }

    /**
     * 获取安装包更新信息
     */
    public void getUpdateInfo() {
        List<PackageInfo> infos = AndroidUtils.getAllNoSystemApps(getContext());
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (PackageInfo info : infos) {
            String str = "{\"versionCode\":" + info.versionCode + ",\"packageName\":" + "\"" + info.packageName + "\"" + "},";
            sb.append(str);
        }
        String str = sb.toString();
        str = str.substring(0, str.length() - 1) + "]";
        Map<String, String> params = new HashMap<>();
        params.put("versions", str);
        mServiceUtil.getGameUpdateInfo(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<ApkUpdateInfoEntity> entities = new ArrayList<ApkUpdateInfoEntity>();
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entities = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString(),
                                new TypeToken<List<ApkUpdateInfoEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.GET_ALL_APK_PATH, assenbleUpdateInfo(compareData(entities)));
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.GET_ALL_APK_PATH, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 组合更新实体真实数据
     */
    public List<ApkInfoEntity> assenbleUpdateInfo(List<ApkUpdateInfoEntity> infoEntity) {
        List<ApkInfoEntity> list = new ArrayList<>();
        for (ApkUpdateInfoEntity entity : infoEntity) {
            ApkInfoEntity apkEntity = new ApkInfoEntity();
            PackageInfo info = AndroidUtils.getAppInfo(getContext(), entity.getPackageName());
            if (info != null) {
                entity.setOldVersionName(info.versionName);
                apkEntity.setUpdateInfoEntity(entity);
                list.add(apkEntity);
            }
        }
        return list;
    }

    /**
     * 比较数据
     */
    public List<ApkUpdateInfoEntity> compareData(List<ApkUpdateInfoEntity> netData) {
        List<ApkUpdateInfoEntity> dbData = ApkUpdateInfoEntity.findAll(ApkUpdateInfoEntity.class);
        List<ApkUpdateInfoEntity> compareData = new ArrayList<>();
        for (ApkUpdateInfoEntity netEntity : netData) {
            if (netEntity.isHasUpdate()) {
                if (dbData.size() == 0) {
                    netEntity.save();
                    continue;
                }
                for (ApkUpdateInfoEntity dbEntity : dbData) {
                    if (dbEntity.getAppId() == netEntity.getAppId()) {
                        if (Integer.parseInt(netEntity.getNewVersionCode()) <= Integer.parseInt(dbEntity.getNewVersionCode())) {
                            netEntity.setUpdateState(dbEntity.getUpdateState());
                        }
                        saveUpdateEntity(netEntity);
                        break;
                    }
                }
                if (netEntity.getUpdateState() == ApkUpdateInfoEntity.STATE_UPDATE_IGNORE) {
                    continue;
                }
                compareData.add(netEntity);
            }
        }
        return compareData;
    }

    /**
     * 保存更新实体
     *
     * @param entity
     */
    public void saveUpdateEntity(ApkUpdateInfoEntity entity) {
        List<ApkUpdateInfoEntity> list = DataSupport.where("downloadUrl=?", entity.getDownloadUrl()).find(ApkUpdateInfoEntity.class);
        if (list == null || list.size() == 0) {
            entity.save();
        } else {
            ApkUpdateInfoEntity entity1 = list.get(0);
            entity1.setDownloadUrl(entity.getDownloadUrl());
            entity1.save();
//            list.get(0).updateAll("downloadUrl=?", entity.getDownloadUrl());
        }
    }

    /**
     * 获取已安装的APK信息
     */
    public void getInstalledApkInfo() {
        PackageManager pm = getContext().getPackageManager();
        List<PackageInfo> list = AndroidUtils.getAllNoSystemApps(getContext());
        ArrayList<ApkInfoEntity> allApkEntity = new ArrayList<>();
        for (PackageInfo info : list) {
            ApplicationInfo appInfo = info.applicationInfo;
            ApkInfoEntity entity = new ApkInfoEntity();
            entity.setApkName(appInfo.loadLabel(pm).toString());
            entity.setApkPackage(appInfo.packageName);
            entity.setApkVersion(info.versionName);
            entity.setApkSize(FileUtil.formatFileSize(new File(appInfo.publicSourceDir).length()));
            entity.setPackageInfo(info);
            allApkEntity.add(entity);
        }
        mSendHandler.obtainMessage(2, allApkEntity).sendToTarget();
    }

    /**
     * 获取文件系统中Apk数量
     *
     * @param path       搜索的初始目录
     * @param ignoreTime 是否忽略间隔时间
     * @return
     */
    public void getAllApk(boolean ignoreTime, final String path) {
        mAllApkEntity.clear();
        //5分钟之内再次进入使用的是数据库数据
        if (!ignoreTime && System.currentTimeMillis() - AppManager.getInstance().getFindApkLastTime() < INTERVAL_TIME) {
            mAllApkEntity = ApkInfoEntity.findAll(ApkInfoEntity.class);
            mSendHandler.obtainMessage(0).sendToTarget();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ApkInfoEntity.deleteAll(ApkInfoEntity.class);
                    AppManager.getInstance().saveFindApkTime(System.currentTimeMillis());
                    iterateApk(path);
                    mSendHandler.obtainMessage(0).sendToTarget();
                }
            }).start();
        }
    }

    private Handler mSendHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: //查找文件系统文件
                    callback(ResultCode.GET_ALL_APK_PATH, mAllApkEntity);
                    break;
                case 1: //下载的
                    callback(ResultCode.GET_ALL_APK_PATH, msg.obj);
                    break;
                case 2: //已安装的
                    callback(ResultCode.GET_ALL_APK_PATH, msg.obj);
                    break;
            }
        }
    };

    /**
     * 递归整个外置内存
     *
     * @param path
     * @return
     */
    private String iterateApk(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            File[] files = file.listFiles();
            if (files != null) {
                for (File childF : files) {
                    if (!childF.isDirectory()) {
                        addApkPath(childF.getPath());
                        continue;
                    }
                    iterateApk(childF.getPath());
                }
            }
        }
        return "";
    }

    /**
     * 添加apk路径
     */
    private void addApkPath(String filePath) {
        try {
            File file = new File(filePath);
            String fileName = FileUtil.getFileExtensionName(file.getName());
            if (RegexHelp.isApk(fileName)) {
                mApkNum++;
                saveInfoToDb(filePath);
            }
        } catch (Throwable e) {
            L.e(TAG, "文件有不合法的文件编码:" + filePath);
        }
    }

    /**
     * 保存APK信息实体
     *
     * @param entity
     */
    public void saveApkInfoEntity(ApkInfoEntity entity) {
        entity.save();
//        List<ApkInfoEntity> list = DataSupport.where("apkPackage=?", entity.getApkPackage()).find(ApkInfoEntity.class);
//        if (list == null || list.size() == 0) {
//            entity.save();
//        } else {
//            list.get(0).updateAll("apkPackage=?", entity.getApkPackage());
//        }
    }

    /**
     * 将信息存储到数据库
     */
    private void saveInfoToDb(String apkPath) {
        PackageManager pm = getContext().getPackageManager();
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
            saveApkInfoEntity(entity);
            mAllApkEntity.add(entity);
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

    /**
     * 下载管理数据
     */
    public void getDownloadInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ApkInfoEntity> list = new ArrayList<>();
                //数据库里面下载数据
                List<DownloadEntity> dbData = DownloadEntity.findAll(DownloadEntity.class);
                if (dbData != null) {
                    for (DownloadEntity entity : dbData) {
                        ApkInfoEntity apkEntity = new ApkInfoEntity();
                        boolean isInstall = false;
                        if (entity.getState() == DownloadEntity.STATE_COMPLETE) {
                            if (AndroidUtils.isInstall(getContext(), entity.getPackageName())) {
                                PackageInfo installedAppInfo = AndroidUtils.getAppInfo(getContext(), entity.getPackageName());
                                isInstall = installedAppInfo != null && installedAppInfo.versionCode == entity.getApkVersionConde();
                                if (entity.getApkVersionConde() == 0) {
                                    isInstall = true;
                                }
                            }
                        }
                        String path = AppManager.getInstance().getSetting().getDownloadLocation() + entity.getName() + ".apk";
                        File apk = new File(path);
                        if (!isInstall && !apk.exists()) {
//                            continue;
                            entity.setState(DownloadEntity.STATE_WAIT);
                            entity.setDownloadComplete(false);
                            entity.setCurrentProgress(0);
                        } else if (!AndroidUtils.isServiceRun(getContext(), DownloadService.class)) {
                            if (entity.getState() == DownloadEntity.STATE_DOWNLOAD_ING) {
                                entity.setState(DownloadEntity.STATE_STOP);
                            }
                        }
                        entity.setInstalled(isInstall);
                        apkEntity.setDownloadEntity(entity);
                        list.add(apkEntity);
                    }
                }
                mSendHandler.obtainMessage(1, list).sendToTarget();
            }
        }).start();
    }

}

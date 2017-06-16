package com.touchrom.gaoshouyou.module;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.arialyy.downloadutil.DownLoadUtil;
import com.arialyy.downloadutil.DownloadListener;
import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.FileUtil;
import com.arialyy.frame.util.SharePreUtil;
import com.arialyy.frame.util.show.L;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushManager;
import com.touchrom.gaoshouyou.activity.MainActivity;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.dialog.GuideDialog;
import com.touchrom.gaoshouyou.dialog.VersionUpgradeDialog;
import com.touchrom.gaoshouyou.entity.LauncherEntity;
import com.touchrom.gaoshouyou.entity.sql.ApkUpdateInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.help.DownloadHelp;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.service.GaoShouYouService;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lk on 2015/11/12.
 * 启动页面数据模型
 */
public class LauncherModule extends BaseModule {
    public LauncherModule(Context context) {
        super(context);
    }

    /**
     * 检查新版本
     */
    public void checkNewVersion() {
        mServiceUtil.getNewVersionInfo(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                LauncherEntity launcherEntity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject launcherObj = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        launcherEntity = new Gson().fromJson(launcherObj.toString(), LauncherEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.CHECK_NEW_VERSION, launcherEntity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.CHECK_NEW_VERSION, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 启动流程
     */
    public void launcherFlows(final ImageView imageView) {
        mServiceUtil.getLauncherParams(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                LauncherEntity launcherEntity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject launcherObj = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        launcherEntity = new Gson().fromJson(launcherObj.toString(), LauncherEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    startLauncherImgFlow(launcherEntity, imageView);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                startLauncherImgFlow(null, imageView);
            }
        });
    }

    /**
     * 开始启动图流程
     *
     * @param entity
     * @param imageView
     */
    public void startLauncherImgFlow(final LauncherEntity entity, final ImageView imageView) {
        L.v(TAG, "开始启动图流程");
        if (entity == null) {
            File file = new File(Constance.PATH.LAUNCHER_IMG_PATH);
            if (file.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(Constance.PATH.LAUNCHER_IMG_PATH);
                imageView.setImageBitmap(bm);
            }
            callback(ResultCode.LAUNCHER_FLOWS, null);
            return;
        }

        Observable.just(entity)
                .map(new Func1<LauncherEntity, String>() {
                    @Override
                    public String call(LauncherEntity launcherEntity) {
                        LauncherEntity oldEntity = SharePreUtil.getObject(Constance.APP.NAME, getContext(), Constance.KEY.LAUNCHER_ENTITY, LauncherEntity.class);
                        if (oldEntity == null) {
                            SharePreUtil.putObject(Constance.APP.NAME, getContext(), Constance.KEY.LAUNCHER_ENTITY, LauncherEntity.class, entity);
                        } else {
                            File file = new File(Constance.PATH.LAUNCHER_IMG_PATH);
                            if (!oldEntity.getImgUrl().equals(entity.getImgUrl())) {   //两个链接不相同-->更新启动图
                                return entity.getImgUrl();
                            } else if (!file.exists()) {  //链接相同，文件不存在-->更新启动图
                                return entity.getImgUrl();
                            } else {    //其他情况从磁盘读取图片
                                return null;
                            }
                        }
                        return null;
                    }
                })
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        if (TextUtils.isEmpty(s)) {
                            return BitmapFactory.decodeFile(Constance.PATH.LAUNCHER_IMG_PATH);
                        } else {
                            downloadImg(s);
                            SharePreUtil.putObject(Constance.APP.NAME, getContext(), Constance.KEY.LAUNCHER_ENTITY, LauncherEntity.class, entity);
                        }
                        return null;
                    }
                })
                .filter(new Func1<Bitmap, Boolean>() {
                    @Override
                    public Boolean call(Bitmap bitmap) {
                        return bitmap != null;
                    }
                })
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onCompleted() {
//                        startVersionUpdateFlow(entity);
                        callback(ResultCode.LAUNCHER_FLOWS, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback(ResultCode.LAUNCHER_FLOWS, null);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }

    /**
     * 开启版本升级流程
     *
     * @param entity
     */
    public void startVersionUpdateFlow(LauncherEntity entity) {
        L.v(TAG, "版本升级流程启动");
        if (entity.isHasNewVersion()) {
            //有新版本
            callback(ResultCode.LAUNCHER_FLOWS, entity);
        } else {
            callback(ResultCode.LAUNCHER_FLOWS, null);
        }
    }

    /**
     * 开启版本对话框流程
     */
    public void startVersionDialogFlow(LauncherEntity entity, final BaseActivity activity) {
        Observable.just(entity).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LauncherEntity>() {
                    @Override
                    public void call(LauncherEntity entity) {
                        VersionUpgradeDialog dialog = new VersionUpgradeDialog(entity, activity);
                        dialog.show(activity.getSupportFragmentManager(), "versionDialog");
                    }
                });
    }

    /**
     * 打开推送，定位等配置流程
     */
    public void startOtherFlow(Context context) {
        //启动全局服务
        context.startService(new Intent(context, GaoShouYouService.class));
        //预下载APP
        if (AppManager.getInstance().getSetting().isAutoDownloadNewApp()) {
            getUpdateInfo();
        }
        //通知系统扫描整个目录
        context.sendBroadcast(new Intent(AndroidVersionUtil.hasKitKat() ? Intent.ACTION_MEDIA_SCANNER_SCAN_FILE : Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        //打开推送
        PushManager pm = PushManager.getInstance();
        pm.initialize(getContext().getApplicationContext());
        pushBind(pm.getClientid(getContext()));
    }

    private void pushBind(String cid) {
        Map<String, String> map = new WeakHashMap<>();
        map.put("clientId", cid);
        mServiceUtil.pushBind(map, new HttpUtil.AbsResponse());
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
                    if (entities != null && entities.size() > 0) {
                        updateInBackground(entities);
                    }
                }
            }

        });
    }

    /**
     * 后台下载应用
     *
     * @param list
     */
    private void updateInBackground(List<ApkUpdateInfoEntity> list) {
        for (ApkUpdateInfoEntity entity : list) {
            if (entity.isHasUpdate()) {
                saveUpdateEntity(entity);
                DownloadEntity dEntity = new DownloadEntity();
                dEntity.setGameId(entity.getAppId());
                dEntity.setImgUrl(entity.getImgUrl());
                dEntity.setDownloadUrl(entity.getDownloadUrl());
                dEntity.setName(entity.getAppName());
                dEntity.setPackageName(entity.getPackageName());
                dEntity.setCaAutoInstall(false);
                DownloadHelp.newInstance().download(getContext(), dEntity);
            }
        }
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
            entity.updateAll("downloadUrl=?", entity.getDownloadUrl());
        }
    }

    /**
     * 进入主界面流程，通过判断文件的差异来判断是否需要进入引导界面
     */
    public void startAppFlow(final BaseActivity activity, final FragmentManager fm) {
        File temp = new File(activity.getFilesDir() + Constance.PATH.GUIDE_SETTING_TEMP_PATH);
        if (temp.exists()) {
            Observable.just(temp)
                    .map(new Func1<File, String>() {
                        @Override
                        public String call(File file) {
                            StringBuilder sb = new StringBuilder();
                            try {
                                FileInputStream fis = new FileInputStream(file);
                                byte[] buf = new byte[1024];
                                int len = 0;
                                while ((len = fis.read(buf)) > 0) {
                                    byte[] b = new byte[len];
                                    System.arraycopy(buf, 0, b, 0, len);
                                    sb.append(new String(b));
                                }
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return sb.toString();
                        }
                    })
                    .map(new Func1<String, Boolean>() {
                        @Override
                        public Boolean call(String s) {
                            String code = String.valueOf(AndroidUtils.getVersionCode(activity));
                            return code.equalsIgnoreCase(s);
                        }
                    })
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {  //如果当前版本号和配置文件的版本号相同
                                activity.startActivity(new Intent(activity, MainActivity.class));
//                                activity.getApp().getAppManager().finishActivity(LauncherActivity.class);
                            } else { //启动引导界面
                                startGuide(activity, fm);
                            }
                        }
                    });
        } else {
            startGuide(activity, fm);
        }
    }

    /**
     * 启动引导界面
     */
    private void startGuide(final BaseActivity activity, final FragmentManager fm) {
        final String path = activity.getFilesDir() + Constance.PATH.GUIDE_SETTING_TEMP_PATH;
        File temp = new File(path);
        FileUtil.createFile(path);
        Observable.just(temp)
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.delete();
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        try {
                            L.v(TAG, "启动引导界面");
                            FileUtil.createFile(path);
                            FileOutputStream fos = new FileOutputStream(path);
                            fos.write(String.valueOf(AndroidUtils.getVersionCode(activity)).getBytes());
                            fos.flush();
                            fos.close();
//                            activity.startActivity(new Intent(activity, GuideActivity.class));
                            GuideDialog guide = new GuideDialog(getContext());
                            guide.show(fm, "guide");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 后台下载图片
     */
    public void downloadImg(String imgUrl) {
        Observable.just(imgUrl).subscribeOn(Schedulers.newThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                L.v(TAG, "后台下载启动图");
                new DownLoadUtil().download(getContext(), s, Constance.PATH.LAUNCHER_IMG_PATH, new DownloadListener());
            }
        });
    }

}

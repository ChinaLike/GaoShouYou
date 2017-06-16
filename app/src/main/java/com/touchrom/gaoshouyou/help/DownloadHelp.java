package com.touchrom.gaoshouyou.help;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.service.DownloadService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lk on 2016/1/8.
 * 下载帮助类
 */
public class DownloadHelp {
    public static final int DOWNLOAD_FLAG = 0xdffa1;
    private Context mContext;
    private int mFlag = -1;

    private DownloadHelp() {

    }

    public static DownloadHelp newInstance() {
        return new DownloadHelp();
    }

    /**
     * 下载计数
     *
     * @param appId
     */
    private void updateDownloadNum(int appId) {
        ServiceUtil util = ServiceUtil.getInstance(mContext);
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId + "");
        util.downloadCount(params, new HttpUtil.AbsResponse());
    }

    /**
     * 下载操作
     *
     * @param cmd {@link DownloadService#ACTION_START}...
     */
    public void downloadCmd(@NonNull Context context, @NonNull DownloadEntity entity, @NonNull String cmd) {
        mContext = context;
        Intent intent = new Intent(context, DownloadService.class);
        if (mFlag != -1 && mFlag == DOWNLOAD_FLAG) {
            intent.putExtra("flag", mFlag);
            updateDownloadNum(entity.getGameId());
        }
        intent.putExtra(Constance.SERVICE.DOWNLOAD_CMD, cmd);
        intent.putExtra(Constance.SERVICE.DOWNLOAD_ENTITY, entity);
        context.startService(intent);
    }

    /**
     * 下载
     *
     * @param context
     * @param entity  下载实体
     */
    public void download(@NonNull Context context, @NonNull final DownloadEntity entity) {
        T.showShort(context, "开始下载：" + entity.getName());
        mFlag = DOWNLOAD_FLAG;
        downloadCmd(context, entity, DownloadService.ACTION_START);
    }

    /**
     * 二维码扫描游戏下载
     *
     * @param context
     * @param appId
     */
    public void qrGamenDownload(@NonNull final Context context, final int appId) {
        ServiceUtil util = ServiceUtil.getInstance(context);
        Map<String, String> params = new WeakHashMap<>();
        params.put("appId", appId + "");
        util.getGameDownloadInfo(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (obj.getInt("code") == 200) {
                        DownloadEntity entity = new DownloadEntity();
                        JSONObject data1 = new JSONObject(obj.getString(ServiceUtil.DATA_KEY));
                        String dUrl = data1.getString("downloadUrl");
                        if (TextUtils.isEmpty(dUrl)) {
                            return;
                        }
                        entity.setGameId(appId);
                        entity.setDownloadUrl(dUrl);
                        entity.setImgUrl(data1.getString("imgUrl"));
                        entity.setName(data1.getString("gameName"));
                        download(context, entity);
                    } else {
                        T.showShort(context, "该二维码没有对应的下载地址");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
            }
        });
    }
}

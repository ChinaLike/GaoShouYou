package com.touchrom.gaoshouyou.module;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.google.gson.Gson;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.ShareEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lk on 2016/1/18.
 * 一些通用的module,如分享，收藏。。
 */
public class SettingModule extends BaseModule {
    public static final int SHARE_TYPE_GSY_APP = 0; //高手游APP分享
    public static final int SHARE_TYPE_GAME = 1; //游戏分享
    public static final int SHARE_TYPE_ARTICLE = 2; //文章分享
    public static final int SHARE_TYPE_GIFT = 3; //礼包分享

    public SettingModule(Context context) {
        super(context);
    }

    /**
     * 通知设置
     */
    public void notifySetting(String setJson) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("set", setJson);
        mServiceUtil.notifySetting(params, new HttpUtil.AbsResponse());
    }

    /**
     * 意见反馈
     */
    public void feedback(String text, String contact) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AppManager.getInstance().getUser().getUserId() + "");
        params.put("text", text);
        params.put("contact", contact);
        mServiceUtil.feedback(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                boolean success = false;
                try {
                    JSONObject object = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(object)) {
                        success = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.FEEDBACK, success);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.FEEDBACK, false);
            }
        });
    }

    /**
     * 获取文章分享数据
     *
     * @param typeId 只有分享文章时才有
     */
    public void getShareData(int type, int typeId, int id) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type + "");
        params.put("id", id + "");
        params.put("typeId", typeId + "");
        mServiceUtil.getShareData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                ShareEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), ShareEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_SHARE_DATA, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_SHARE_DATA, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取分享数据
     *
     * @param type {@link #SHARE_TYPE_GAME}
     * @param id   游戏，文章，礼包等Id
     */
    public void getShareData(int type, int id) {
        getShareData(type, 0, id);
    }
}

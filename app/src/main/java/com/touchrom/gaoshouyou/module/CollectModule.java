package com.touchrom.gaoshouyou.module;

import android.content.Context;
import android.os.Bundle;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.google.gson.Gson;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.CollectEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lk on 2016/3/16.
 * 收藏模型
 */
public class CollectModule extends BaseModule {
    public CollectModule(Context context) {
        super(context);
    }

    /**
     * 取消收藏
     */
    public void cancelCollect(int collectId) {
        Map<String, String> params = new HashMap<>();
        params.put("scId", collectId + "");
        mServiceUtil.cancelCollect(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                boolean b = false;
                try {
                    JSONObject object = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(object)) {
                        JSONObject obj = object.getJSONObject(ServiceUtil.DATA_KEY);
                        b = obj.getBoolean("result");
                        T.showShort(getContext(), "取消收藏" + (b ? "成功" : "失败"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.CANCEL_COLLECT, b);
                }
            }
        });
    }

    /**
     * 添加收藏
     */
    public void addCollect(int typeId, int appId) {
        Map<String, String> params = new HashMap<>();
        params.put("typeId", typeId + "");
        params.put("artId", appId + "");
        mServiceUtil.addCollect(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                boolean b = false;
                try {
                    JSONObject object = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(object)) {
                        JSONObject obj = object.getJSONObject(ServiceUtil.DATA_KEY);
                        b = obj.getBoolean("result");
                        T.showShort(getContext(), obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.ADD_COLLECT, b);
                }
            }
        });
    }

    /**
     * 获取收藏状态
     */
    public void getCollectState(int typeId, int appId) {
        Map<String, String> params = new HashMap<>();
        params.put("typeId", typeId + "");
        params.put("artId", appId + "");
        mServiceUtil.getCollectState(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                Bundle b = null;
                try {
                    JSONObject object = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(object)) {
                        b = new Bundle();
                        JSONObject obj = object.getJSONObject(ServiceUtil.DATA_KEY);
                        b.putBoolean("state", obj.getBoolean("result"));
                        b.putInt("collectId", obj.getInt("scId"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_COLLECT_STATE, b);
                }
            }
        });
    }

    /**
     * 收藏列表
     *
     * @param type 2、文章
     *             3、游戏
     */
    public void getCollectList(int type, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type + "");
        params.put("num", "10");
        params.put("page", page + "");
        mServiceUtil.getCollectList(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                CollectEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), CollectEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.COLLECT, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.COLLECT, ServiceUtil.ERROR);
            }
        });
    }
}

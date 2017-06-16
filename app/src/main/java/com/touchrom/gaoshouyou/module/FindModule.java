package com.touchrom.gaoshouyou.module;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.entity.OpenGameEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lk on 2016/3/28.
 * 发现模型
 */
public class FindModule extends BaseModule {
    public FindModule(Context context) {
        super(context);
    }

    /**
     * 获取礼包列表
     *
     * @param page
     */
    public void getGiftList(int type, int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("num", "10");
        params.put("page", page + "");
        params.put("type", type + "");
        mServiceUtil.getFindGiftList(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<GiftEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<GiftEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_FIND_GIFT_LIST, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_FIND_GIFT_LIST, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取礼包列表
     *
     * @param page
     */
    public void getOpenServerList(int type, int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("num", "10");
        params.put("page", page + "");
        params.put("type", type + "");
        mServiceUtil.getFindOpenServerList(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<OpenGameEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<OpenGameEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_FIND_GIFT_LIST, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_FIND_GIFT_LIST, ServiceUtil.ERROR);
            }
        });
    }
}

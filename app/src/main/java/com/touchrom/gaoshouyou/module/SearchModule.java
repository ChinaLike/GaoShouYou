package com.touchrom.gaoshouyou.module;

import android.content.Context;
import android.support.annotation.NonNull;

import com.arialyy.frame.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lk on 2016/1/18.
 * 搜索模型
 */
public class SearchModule extends BaseModule {
    public SearchModule(Context context) {
        super(context);
    }

    /**
     * 获取搜索热词
     */
    public void getSearchHotWord() {
        mServiceUtil.getSearchHotWord(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                String[] keys = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONArray array = obj.getJSONArray(ServiceUtil.DATA_KEY);
                        keys = new String[array.length()];
                        for (int i = 0, count = array.length(); i < count; i++) {
                            JSONObject word = array.getJSONObject(i);
                            keys[i] = word.getString("name");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_SEARCH_HOT_WORD, keys);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_SEARCH_HOT_WORD, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取搜索数据
     */
    public void getSearchData(@NonNull String searchName, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("searchName", searchName);
        params.put("page", page + "");
        mServiceUtil.getSearchData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<GameInfoEntity> entities = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        callback(0xaaa1, obj.getInt("count"));
                        entities = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<GameInfoEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_SEARCH_DATA, entities);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_SEARCH_DATA, ServiceUtil.ERROR);
            }
        });
    }


}

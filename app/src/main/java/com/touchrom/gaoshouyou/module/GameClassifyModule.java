package com.touchrom.gaoshouyou.module;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.GameClassifyEntity;
import com.touchrom.gaoshouyou.entity.GameClassifyScreenEntity;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lk on 2015/12/23.
 * 游戏分类模型
 */
public class GameClassifyModule extends BaseModule {
    public GameClassifyModule(Context context) {
        super(context);
    }

    /**
     * 筛选分类获取 标签悬浮框数据
     */
    public void getGameClassifyTag(int classifyId) {
        Map<String, String> params = new HashMap<>();
        params.put("classifyId", classifyId + "");
        mServiceUtil.getGameClassifyScreen(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<GameClassifyScreenEntity> entities = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entities = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString(),
                                new TypeToken<List<GameClassifyScreenEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_CLASSIFY_TAG, entities);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_CLASSIFY_TAG, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取分类游戏
     */
    public void getGameClassifyInfo(int classifyId, int type) {
        getGameClassifyInfo(classifyId, type, 0, 0, 0, 0);
    }

    /**
     * 分类筛选
     */
    public void getGameClassifyInfo(int classifyId, int type, int typeId, int firstId,
                                    int secondId, int thirdId) {
        getGameClassifyInfo(classifyId, type, typeId, firstId, secondId, thirdId, 1);
    }

    /**
     * 加载更多
     *
     * @param classifyId 分类Id
     * @param type       分类几个栏目的type类型
     * @param typeId     通过类型Id筛选
     * @param firstId    第一个筛选Id
     * @param secondId   第二个筛选Id
     * @param thirdId    第三个筛选Id
     * @param page       翻页页数
     */
    public void getGameClassifyInfo(int classifyId, int type, int typeId, int firstId,
                                    int secondId, int thirdId, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("classifyId", classifyId + "");
        params.put("type", type + "");
        params.put("num", 10 + "");
        params.put("page", page + "");
        params.put("typeId", typeId + "");
        params.put("runId", firstId + "");
        params.put("spendId", secondId + "");
        params.put("tagId", thirdId + "");
        mServiceUtil.getGameClassifyInfo(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<GameInfoEntity> entities = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entities = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString(),
                                new TypeToken<List<GameInfoEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_CLASSIFY_INFO, entities);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_CLASSIFY_INFO, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取游戏分类
     */
    public void getGameClassify() {
        mServiceUtil.getGameClassify(new HashMap<String, String>(), new HttpUtil.AbsResponse() {
                    @Override
                    public void onResponse(String data) {
                        super.onResponse(data);
                        List<GameClassifyEntity> entities = null;
                        try {
                            JSONObject obj = new JSONObject(data);
                            if (mServiceUtil.isRequestSuccess(obj)) {
                                entities = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                        , new TypeToken<List<GameClassifyEntity>>() {
                                        }.getType());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            callback(ResultCode.SERVER.GET_GAME_CLASSIFY, entities);
//                            callback(ResultCode.SERVER.GET_GAME_CLASSIFY, ServiceUtil.ERROR);
                        }
                    }

                    @Override
                    public void onError(Object error) {
                        super.onError(error);
                        callback(ResultCode.SERVER.GET_GAME_CLASSIFY, ServiceUtil.ERROR);
                    }
                }
        );
    }


}

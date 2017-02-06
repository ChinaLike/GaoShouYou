package com.touchrom.gaoshouyou.module;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.BannerEntity;
import com.touchrom.gaoshouyou.entity.GameClassifyEntity;
import com.touchrom.gaoshouyou.entity.GameFactoryEntity;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.entity.RecommendEntity;
import com.touchrom.gaoshouyou.entity.SimpleEntity;
import com.touchrom.gaoshouyou.entity.TagEntity;
import com.touchrom.gaoshouyou.entity.TopicEntity;
import com.touchrom.gaoshouyou.entity.TopicInfoEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lyy on 2016/1/11.
 * 首页游戏界面Module
 */
public class GameModule extends BaseModule {
    public GameModule(Context context) {
        super(context);
    }


    /**
     * 获取游戏厂商数据
     */
    public void getGameFactory(int gameFactoryId, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("articleId", gameFactoryId + "");
        params.put("num", 10 + "");
        params.put("page", page + "");
        mServiceUtil.getGameFactoryData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                GameFactoryEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), GameFactoryEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_FACTORY, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_FACTORY, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取游戏厂商列表数据
     */
    public void getGameFactoryList(int page) {
        Map<String, String> params = new HashMap<>();
        params.put("num", 10 + "");
        params.put("page", page + "");
        mServiceUtil.getGameFactoryListData(params, new HttpUtil.AbsResponse() {
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
                    callback(ResultCode.SERVER.GET_GAME_FACTORY_LIST, entities);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_FACTORY_LIST, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取专题分类详情
     */
    public void getGameTopicClassifyDetailData(int topicClassifyId, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("topicId", topicClassifyId + "");
        params.put("page", page + "");
        params.put("num", "10");
        mServiceUtil.getGameTopicClassifyDetailData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<TopicInfoEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<TopicInfoEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_TOPIC_CLASSIFY_DETAIL, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_TOPIC_CLASSIFY_DETAIL, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取专题分类
     */
    public void getGameTopicClassifyData() {
        mServiceUtil.getGameTopicClassifyData(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<SimpleEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new ArrayList<>();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_TOPIC_CLASSIFY, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_TOPIC_CLASSIFY, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取专题数据
     */
    public void getGameTopicData() {
        mServiceUtil.getGameTopicData(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<TopicEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<TopicEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_TOPIC, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_TOPIC, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取游戏排行数据
     */
    public void getGameRankData(int classifyId) {
        Map<String, String> params = new HashMap<>();
        params.put("topId", classifyId + "");
        mServiceUtil.getGameRankData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<GameInfoEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<GameInfoEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_RANK_DATA, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_RANK_DATA, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取游戏排行分类
     */
    public void getGameRankClassify() {
        mServiceUtil.getGameRankClassify(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<TagEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<TagEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_RANK_CLASSIFY, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_RANK_CLASSIFY, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取推荐数据
     */
    public void getRecommend() {
        mServiceUtil.getGameRecommendData(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<RecommendEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<RecommendEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MAIN_GAME_RECOMMEND, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MAIN_GAME_RECOMMEND, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取游戏Banner
     */
    public void getGameBanner() {
        mServiceUtil.getGameBannerData(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<BannerEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<BannerEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MAIN_GAME_BANNER, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MAIN_GAME_BANNER, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取精选，最新界面数据
     *
     * @param type 1.精选, 2.最新；
     */
    public void getSelectedData(int type) {
        getSelectedData(type, 1);
    }

    /**
     * 获取更多数据
     *
     * @param type 1.精选, 2.最新；
     * @param page 页数
     */
    public void getSelectedData(int type, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type + "");
        params.put("num", 10 + "");
        params.put("page", page + "");
        mServiceUtil.getGameSelectedData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<GameInfoEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<GameInfoEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MAIN_GAME_DATA, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MAIN_GAME_DATA, ServiceUtil.ERROR);
            }
        });
    }

}

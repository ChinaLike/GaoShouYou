package com.touchrom.gaoshouyou.module;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.ArticleEntity;
import com.touchrom.gaoshouyou.entity.NewsEntity;
import com.touchrom.gaoshouyou.entity.TagEntity;
import com.touchrom.gaoshouyou.entity.TopicDetailEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lyy on 2016/3/4.
 * 资讯Module
 */
public class NewsModule extends BaseModule {
    public NewsModule(Context context) {
        super(context);
    }

    /**
     * 获取专题文章列表
     */
    public void getTopicArtList(int artId, int page){
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        params.put("num", "10");
        params.put("page", page + "");
        mServiceUtil.getTopicArtList(params, new HttpUtil.AbsResponse(){
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<NewsEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<NewsEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_TOPIC_ALL_ART, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_TOPIC_ALL_ART, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取专题内容
     */
    public void getTopicDetailContent(int topicId) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", topicId + "");
        mServiceUtil.getTopicDetailContent(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                TopicDetailEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), TopicDetailEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_TOPIC_DETAIL_CONTENT, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_TOPIC_DETAIL_CONTENT, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取文章内容
     *
     * @param articleId 文章Id
     * @param typeId    1、攻略
     *                  2、活动
     *                  3、评测
     *                  4、资讯
     *                  5、新闻
     *                  6、专题
     *                  7、资料
     *                  8、问答
     */
    public void getArticleContent(int typeId, int articleId) {
        Map<String, String> params = new HashMap<>();
        params.put("artId", articleId + "");
        params.put("typeId", typeId + "");
        mServiceUtil.getArticleContent(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                ArticleEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), ArticleEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_ARTICLE_CONTENT, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_ARTICLE_CONTENT, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取资讯文章列表
     */
    public void getNewsArticleList(int classifyId, int tagId, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("type", classifyId + "");
        params.put("typeId", tagId + "");
        params.put("num", 10 + "");
        params.put("page", page + "");
        mServiceUtil.getNewsArticleList(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<NewsEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<NewsEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_NEWS_ARTICLE_LIST, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_NEWS_ARTICLE_LIST, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取分类标签
     */
    public void getRaidersTags(int classifyId) {
        Map<String, String> params = new HashMap<>();
        params.put("type", classifyId + "");
        mServiceUtil.getRaidersTag(params, new HttpUtil.AbsResponse() {
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
                    callback(ResultCode.SERVER.GET_RAIDERS_TAG, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_RAIDERS_TAG, ServiceUtil.ERROR);
            }
        });
    }

}

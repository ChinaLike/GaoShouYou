package com.touchrom.gaoshouyou.module;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.CommentEntity;
import com.touchrom.gaoshouyou.entity.MeCommentEntity;
import com.touchrom.gaoshouyou.entity.ReplyCommentEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lyy on 2016/3/14.
 * 评论模型
 */
public class CommentModule extends BaseModule {
    public CommentModule(Context context) {
        super(context);
    }

    /**
     * 评论点赞
     */
    public void like(int cmtId) {
        Map<String, String> params = new HashMap<>();
        params.put("cmtId", cmtId + "");
        mServiceUtil.replyPraise(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                boolean b = false;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject entity = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        b = entity.getBoolean("result");
                        T.showShort(getContext(), entity.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.COMMENT_LIKE, b);
                }
            }
        });
    }

    /**
     * 我的评论
     */
    public void meComment(int page) {
        Map<String, String> params = new HashMap<>();
        params.put("num", "10");
        params.put("page", page + "");
        mServiceUtil.meComment(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<MeCommentEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<MeCommentEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.ME_COMMENT, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.ME_COMMENT, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 回复我的
     */
    public void reComment(int page) {
        Map<String, String> params = new HashMap<>();
        params.put("num", "10");
        params.put("page", page + "");
        mServiceUtil.reComment(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<ReplyCommentEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<ReplyCommentEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.RE_COMMENT, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.RE_COMMENT, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 发表回复
     */
    public void sendReply(int type, int cmtId, String reply) {
        Map<String, String> params = new HashMap<>();
        params.put("cmtId", cmtId + "");
        params.put("text", reply);
        params.put("type", type + "");
        mServiceUtil.sendReply(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                boolean b = false;
                try {
                    String str = "评论失败，请重试";
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject obj1 = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        str = obj1.getString("message");
                        b = obj1.getBoolean("result");
                    }
                    T.showLong(getContext(), str);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.SEND_REPLY, b);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                T.showLong(getContext(), "网络错误");
            }
        });
    }

    /**
     * 发表评论
     */
    public void sendComment(int appId, int typeId, String comment) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId + "");
        params.put("typeId", typeId + "");
        params.put("text", comment);
        mServiceUtil.sendComment(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                boolean b = false;
                try {
                    String str = "评论失败，请重试";
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject obj1 = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        str = obj1.getString("message");
                        b = obj1.getBoolean("result");
                    }
                    T.showLong(getContext(), str);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.SEND_COMMENT, b);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                T.showLong(getContext(), "网络错误");
            }
        });
    }

    /**
     * 获取评论详情
     */
    public void getCommentDetail(int type, int cmtId) {
        Map<String, String> params = new HashMap<>();
        params.put("cmtId", cmtId + "");
        params.put("type", type + "");
        mServiceUtil.getCommentDetail(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                CommentEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), CommentEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_COMMENT_DETAIL, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_COMMENT_DETAIL, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取游戏评论
     *
     * @param typeId 0游戏
     *               1、攻略
     *               2、活动
     *               3、评测
     *               4、资讯
     *               5、新闻
     *               6、专题
     *               7、资料
     *               8、问答
     */
    public void getComment(int appId, int typeId, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId + "");
        params.put("typeId", typeId + "");
        params.put("num", 10 + "");
        params.put("page", page + "");
        mServiceUtil.getComment(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<CommentEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<CommentEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_COMMENT, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_COMMENT, ServiceUtil.ERROR);
            }
        });
    }
}

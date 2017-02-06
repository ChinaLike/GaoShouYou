package com.touchrom.gaoshouyou.module;

import android.content.Context;
import android.support.annotation.NonNull;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.L;
import com.arialyy.frame.util.show.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.CommentEntity;
import com.touchrom.gaoshouyou.entity.FansEntity;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.entity.HonorEntity;
import com.touchrom.gaoshouyou.entity.HonorTagEntity;
import com.touchrom.gaoshouyou.entity.MsgEntity;
import com.touchrom.gaoshouyou.entity.ReplyCommentEntity;
import com.touchrom.gaoshouyou.entity.UserEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lyy on 2016/3/1.
 * 用户相关Module
 */
public class UserModule extends BaseModule {
    public UserModule(Context context) {
        super(context);
    }

    /**
     * 上传头像
     */
    public void uploadHeadImg(String filePath){
        mServiceUtil.uploadHeadImg("upload_head_img", filePath, new HttpUtil.AbsResponse(){
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
//                L.d(TAG, data);
            }
        });
    }

    /**
     * 发表留言
     */
    public void sendLeaveMsg(int fansId, String msg) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("userId", fansId + "");
        params.put("text", msg);
        mServiceUtil.sendLeaveMsg(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject result = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        callback(ResultCode.SERVER.SEND_LEAVE_MSG, result.getBoolean("result"));
                        T.showShort(getContext(), result.getString("message"));
                    } else {
                        T.showShort(getContext(), ServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 签到
     */
    public void signIn() {
        mServiceUtil.signIn(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject result = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        callback(ResultCode.SERVER.SIGN_IN, result.getBoolean("result"));
                        T.showShort(getContext(), result.getString("message"));
                    } else {
                        T.showShort(getContext(), ServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取留言数据
     */
    public void getLeaveMsgList(int userId, int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("userId", userId + "");
        params.put("num", "10");
        params.put("page", page + "");
        mServiceUtil.getLeaveMsgList(params, new HttpUtil.AbsResponse() {
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
                    callback(ResultCode.SERVER.GET_USER_LEAVE_MSG, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_USER_LEAVE_MSG, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 取消关注、
     *
     * @param fansId
     */
    public void cancelFollow(int fansId) {
        Map<String, String> m = new HashMap<>();
        m.put("fansId", fansId + "");
        mServiceUtil.cancelFollow(m, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject object = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        T.showShort(getContext(), object.getString("message"));
                        callback(ResultCode.SERVER.CANCEL_FOLLOW, object.getBoolean("result"));
                    } else {
                        T.showShort(getContext(), ServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 关注
     *
     * @param fansId
     */
    public void follow(int fansId) {
        Map<String, String> m = new HashMap<>();
        m.put("fansId", fansId + "");
        mServiceUtil.follow(m, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject object = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        T.showShort(getContext(), object.getString("message"));
                        callback(ResultCode.SERVER.FOLLOW, object.getBoolean("result"));
                    } else {
                        T.showShort(getContext(), ServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取个人中心消息
     */
    public void getUserCenterData(int userId) {
        Map<String, String> map = new WeakHashMap<>();
        map.put("fansId", userId + "");
        mServiceUtil.getUserCenterData(map, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                UserEntity entity = null;
                try {
                    JSONObject object = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(object)) {
                        entity = new Gson().fromJson(object.getJSONObject(ServiceUtil.DATA_KEY).toString(), UserEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_USER_CENTER_DATA, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_USER_CENTER_DATA, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 消息读取状态
     */
    public void msgReadState() {
        mServiceUtil.msgReadState(null, new HttpUtil.AbsResponse());
    }

    /**
     * 获取通知消息
     */
    public void getMsgList(int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("num", "10");
        params.put("page", page + "");
        mServiceUtil.getMsgList(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<MsgEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<MsgEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MSG_LIST, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MSG_LIST, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 高手称号
     */
    public void getHonorDesign() {
        mServiceUtil.honorTag(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                HonorTagEntity entity = null;
                try {
                    JSONObject object = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(object)) {
                        entity = new Gson().fromJson(object.getJSONObject(ServiceUtil.DATA_KEY).toString(), HonorTagEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MY_HONOR_DESIGN, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MY_HONOR_DESIGN, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 高手等级
     */
    public void getHonorLev() {
        mServiceUtil.honorLev(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                HonorEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), HonorEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MY_HONOR_LEV, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MY_HONOR_LEV, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 我的礼包
     *
     * @param typeId 1，已领取；2，已预定
     */
    public void getMyGift(int typeId, int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("num", "10");
        params.put("page", page + "");
        params.put("typeId", typeId + "");
        mServiceUtil.getGiftList(params, new HttpUtil.AbsResponse() {
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
                    callback(ResultCode.SERVER.GET_MY_GIFT_LIST, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MY_GIFT_LIST, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 动态
     */
    public void getDynamicList(int userId, int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("num", "10");
        params.put("page", page + "");
        params.put("userId", userId + "");
        mServiceUtil.getDynamicList(params, new HttpUtil.AbsResponse() {
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
                    callback(ResultCode.SERVER.GET_DYNAMIC_LIST, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_DYNAMIC_LIST, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取粉丝，关注列表
     *
     * @param type 2、关注
     *             3、粉丝
     */
    public void getFansList(int userId, int type, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("typeId", type + "");
        params.put("num", "10");
        params.put("page", page + "");
        params.put("userId", userId + "");
        mServiceUtil.getFansList(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<FansEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<FansEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_FANS_LIST, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_FANS_LIST, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param type 验证码类型 {1注册、2身份验证、3信息变更}
     */
    public void getCode(int type, @NonNull String phoneNum) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type + "");
        params.put("phone", phoneNum);
        mServiceUtil.getCode(params, new HttpUtil.AbsResponse());
    }

    /**
     * 注册
     */
    public void regAccount(@NonNull String phoneNum, @NonNull String code, @NonNull String pw) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNum + "");
        params.put("code", code);
        params.put("password", pw);
        mServiceUtil.reg(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                UserEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), UserEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.REG, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.REG, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取用户数据
     */
    public void getUserInfo(@NonNull String account, @NonNull String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", account);
        params.put("password", password);
        mServiceUtil.login(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                UserEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), UserEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.LOGIN, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.LOGIN, ServiceUtil.ERROR);
            }
        });
    }
}

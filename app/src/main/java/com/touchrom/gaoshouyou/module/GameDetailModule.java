package com.touchrom.gaoshouyou.module;

import android.content.Context;
import android.text.TextUtils;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.gaoshouyou.base.BaseModule;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.dialog.GiftGrabSuccessDialog;
import com.touchrom.gaoshouyou.entity.GameDetailEntity;
import com.touchrom.gaoshouyou.entity.GameDetailInfoEntity;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.entity.RaiderEntity;
import com.touchrom.gaoshouyou.entity.TagEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lyy on 2016/1/12.
 * 游戏详情模型
 */
public class GameDetailModule extends BaseModule {
    public GameDetailModule(Context context) {
        super(context);
    }

    /**
     * 获取礼包码
     */
    public void getGiftCode(int giftId, int giftType) {
        Map<String, String> params = new HashMap<>();
        params.put("libaoId", giftId + "");
        params.put("typeId", giftType + "");
        mServiceUtil.getGiftCode(params, new GiftResponse(getContext(), mServiceUtil, giftId, giftType));
    }

    /**
     * 礼包详情
     */
    public void getGiftDetail(int giftId) {
        Map<String, String> params = new HashMap<>();
        params.put("giftId", giftId + "");
        mServiceUtil.getGiftDetail(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                GiftEntity gift = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        gift = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), GiftEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GIFT_DETAIL, gift);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GIFT_DETAIL, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * y游戏详情栏目
     */
    public void getGameDetailTab(int gameId) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", gameId + "");
        mServiceUtil.getGameDetailTab(params, new HttpUtil.AbsResponse() {
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
                    callback(ResultCode.SERVER.GET_GAME_DETAIL_Tab, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_DETAIL_Tab, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取游戏礼包
     */
    public void getGameDetailGift(int gameId, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", gameId + "");
        params.put("num", 10 + "");
        params.put("page", page + "");
        mServiceUtil.getGameDetailGift(params, new HttpUtil.AbsResponse() {
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
                    callback(ResultCode.SERVER.GET_GAME_DETAIL_GIFT, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_DETAIL_GIFT, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取游戏攻略
     */
    public void getGameDetailRaider(int gameId, int type, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", gameId + "");
        params.put("num", 10 + "");
        params.put("page", page + "");
        params.put("tagId", type + "");
        mServiceUtil.getGameDetailRaider(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<RaiderEntity> list = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        list = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<RaiderEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_DETAIL_RAIDER, list);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_DETAIL_RAIDER, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取游戏详情--详情
     */
    public void getGameDetailInfo(int appId) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId + "");
        mServiceUtil.getGameDetailInfo(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                GameDetailInfoEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), GameDetailInfoEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_DETAIL_INFO, entity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_DETAIL_INFO, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取游戏详情数据
     */
    public void getGameDetail(int appId) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId + "");
        mServiceUtil.getGameDetailData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                GameDetailEntity detailEntity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        detailEntity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), GameDetailEntity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_GAME_DETAIL_DATA, detailEntity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_GAME_DETAIL_DATA, ServiceUtil.ERROR);
            }
        });
    }

    public static class GiftResponse extends HttpUtil.AbsResponse {
        ServiceUtil util;
        Context context;
        int giftId, giftType;

        public GiftResponse(Context context, ServiceUtil util, int giftId, int giftType) {
            this.util = util;
            this.giftId = giftId;
            this.giftType = giftType;
            this.context = context;
        }

        @Override
        public void onResponse(String data) {
            super.onResponse(data);
            try {
                JSONObject obj = new JSONObject(data);
                if (util.isRequestSuccess(obj)) {
                    JSONObject object = obj.getJSONObject(ServiceUtil.DATA_KEY);
                    T.showShort(context, object.getString("message"));
                    String key = "";
                    switch (giftType) {
                        case 1:
                            key = "lq";
                            break;
                        case 2:
                            key = "th";
                            break;
                        case 3:
                            key = "yd";
                            break;
                    }
                    JSONArray array = object.getJSONArray(key);
                    GiftGrabSuccessDialog dialog = new GiftGrabSuccessDialog(context, getDialogData(array),
                            giftType, giftId);
                    dialog.show();
                } else {
                    T.showShort(context, util.getMsg(obj) + "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private List<String> getDialogData(JSONArray array) throws JSONException {
            List<String> data = new ArrayList<>();
            if (array.length() > 0) {
                for (int i = 0, count = array.length(); i < count; i++) {
                    data.add(array.getString(i));
                }
            }
            return data;
        }
    }
}

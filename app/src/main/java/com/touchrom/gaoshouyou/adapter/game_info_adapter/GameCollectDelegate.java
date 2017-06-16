package com.touchrom.gaoshouyou.adapter.game_info_adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lk on 2016/3/29.
 */
class GameCollectDelegate extends GameInfoDelegate {
    private OnNotifyCallback mCallback;

    interface OnNotifyCallback {
        public void onNotify(int position);
    }

    public GameCollectDelegate(Context context, int itemType) {
        super(context, itemType);
    }

    @Override
    public void bindData(int position, GameInfoHolder helper, GameInfoEntity entity) {
        if (mSettingEntity.isShowImg()) {
            ImgHelp.setImg(getContext(), entity.getIconUrl(), helper.icon);
        }
        helper.name.setText(entity.getName());
        helper.detail.setText(entity.getDescription());
        String introduction = entity.getTypeName() + "  " + entity.getDownNum() + "下载  " + entity.getSize();
        helper.introduction.setText(introduction);
        helper.starBar.setScore(entity.getScore());
        ClickListener listener = (ClickListener) helper.download.getTag(helper.download.getId());
        if (listener == null) {
            listener = new ClickListener();
            helper.download.setTag(helper.download.getId(), listener);
        }
        listener.setAction(position, entity);
        helper.download.setOnClickListener(listener);
        helper.download.setText("移除");
    }

    public void setNotifyCallback(@NonNull OnNotifyCallback callback){
        mCallback = callback;
    }

    /**
     * 取消收藏
     */
    private void cancelCollect(final int position, int collectId) {
        final ServiceUtil util = ServiceUtil.getInstance(getContext());
        Map<String, String> params = new HashMap<>();
        params.put("scId", collectId + "");
        util.cancelCollect(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                boolean b = false;
                try {
                    JSONObject object = new JSONObject(data);
                    if (util.isRequestSuccess(object)) {
                        JSONObject obj = object.getJSONObject(ServiceUtil.DATA_KEY);
                        b = obj.getBoolean("result");
                        T.showLong(getContext(), "取消收藏" + (b ? "成功" : "失败"));
                        if (b && mCallback != null) {
                            mCallback.onNotify(position);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class ClickListener implements View.OnClickListener {
        int position;
        GameInfoEntity entity;

        public void setAction(int position, GameInfoEntity entity) {
            this.position = position;
            this.entity = entity;
        }

        @Override
        public void onClick(View v) {
            cancelCollect(position, entity.getCollectId());
        }
    }

}

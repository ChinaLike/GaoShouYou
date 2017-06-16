package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arialyy.frame.core.NotifyHelp;
import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.bumptech.glide.Glide;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.lyy.ui.widget.CircleImageView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.FansEntity;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/16.
 * 粉丝Adapter
 */
public class FansAdapter extends AbsOrdinaryAdapter<FansEntity, FansAdapter.FansHolder> {

    private int mType;
    private boolean isMeFans = true;

    /**
     * @param context
     * @param data
     * @param type    2 ==> 关注， 3 ==> 粉丝
     */
    public FansAdapter(Context context, List<FansEntity> data, int type, boolean isMeFans) {
        super(context, data);
        mType = type;
        this.isMeFans = isMeFans;
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_fans;
    }

    @Override
    public void bindData(int position, FansHolder helper, FansEntity item) {
        Glide.with(getContext()).load(item.getImgUrl()).into(helper.mImg);
        helper.nikeName.setText(item.getNikeName());
        helper.sign.setText(item.getSign());
        if (item.getFansId() == AppManager.getInstance().getUser().getUserId()) {
            helper.bt.setVisibility(View.GONE);
            helper.cancelFollow.setVisibility(View.GONE);
            return;
        }
        if (mType == 2) {
            if (isMeFans) {
                helper.bt.setVisibility(View.GONE);
                helper.cancelFollow.setVisibility(View.VISIBLE);
                setBtListener(helper.cancelFollow, item);
            } else {
                if (item.isAttention()) {
                    helper.bt.setVisibility(View.GONE);
                    helper.cancelFollow.setVisibility(View.VISIBLE);
                    setBtListener(helper.cancelFollow, item);
                } else {
                    helper.bt.setVisibility(View.VISIBLE);
                    helper.cancelFollow.setVisibility(View.GONE);
                    helper.bt.setText("+ 关注");
                    helper.bt.setBackgroundResource(R.drawable.selector_blue_bt);
                    setBtListener(helper.bt, item);
                }
            }
        } else if (mType == 3) {
            if (item.isAttention()) {
                helper.bt.setVisibility(View.GONE);
                helper.cancelFollow.setVisibility(View.VISIBLE);
                setBtListener(helper.cancelFollow, item);
            } else {
                helper.bt.setVisibility(View.VISIBLE);
                helper.cancelFollow.setVisibility(View.GONE);
                helper.bt.setText("+ 关注");
                helper.bt.setBackgroundResource(R.drawable.selector_blue_bt);
                setBtListener(helper.bt, item);
            }
        }
    }

    private void setBtListener(View view, FansEntity item) {
        BtListener cL = (BtListener) view.getTag(view.getId());
        if (cL == null) {
            cL = new BtListener();
            view.setTag(view.getId(), cL);
        }
        cL.setEntity(item);
        view.setOnClickListener(cL);
    }

    @Override
    public FansHolder getViewHolder(View convertView) {
        return new FansHolder(convertView);
    }

    private class BtListener implements View.OnClickListener {
        FansEntity entity;
        ServiceUtil util;

        public void setEntity(FansEntity entity) {
            this.entity = entity;
            util = ServiceUtil.getInstance(getContext());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt:
                    follow(entity.getFansId());
                    break;
                case R.id.cancel_follow:
                    cancelFollow(entity.getFansId());
                    break;
            }
        }

        private void cancelFollow(int fansId) {
            Map<String, String> m = new HashMap<>();
            m.put("fansId", fansId + "");
            util.cancelFollow(m, new HttpUtil.AbsResponse() {
                @Override
                public void onResponse(String data) {
                    super.onResponse(data);
                    try {
                        JSONObject obj = new JSONObject(data);
                        if (util.isRequestSuccess(obj)) {
                            JSONObject object = obj.getJSONObject(ServiceUtil.DATA_KEY);
                            T.showShort(getContext(), object.getString("message"));
                            if (object.getBoolean("result")) {
                                entity.setAttention(false);
                                if (isMeFans && mType == 2) {
                                    mData.remove(entity);
                                }
                                notifyDataSetChanged();
                                NotifyHelp.getInstance().update(Constance.NOTIFY_KEY.FANS);
                            }
                        } else {
                            T.showShort(getContext(), util.getMsg(obj));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void follow(int fansId) {
            Map<String, String> m = new HashMap<>();
            m.put("fansId", fansId + "");
            util.follow(m, new HttpUtil.AbsResponse() {
                @Override
                public void onResponse(String data) {
                    super.onResponse(data);
                    try {
                        JSONObject obj = new JSONObject(data);
                        if (util.isRequestSuccess(obj)) {
                            JSONObject object = obj.getJSONObject(ServiceUtil.DATA_KEY);
                            T.showShort(getContext(), object.getString("message"));
                            if (object.getBoolean("result")) {
                                entity.setAttention(true);
                                notifyDataSetChanged();
                                NotifyHelp.getInstance().update(Constance.NOTIFY_KEY.FANS);
                            }
                        } else {
                            T.showShort(getContext(), util.getMsg(obj));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    class FansHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.user_img)
        CircleImageView mImg;
        @InjectView(R.id.nike_name)
        TextView nikeName;
        @InjectView(R.id.sign)
        TextView sign;
        @InjectView(R.id.bt)
        Button bt;
        @InjectView(R.id.cancel_follow)
        TextView cancelFollow;

        public FansHolder(View view) {
            super(view);
        }
    }
}

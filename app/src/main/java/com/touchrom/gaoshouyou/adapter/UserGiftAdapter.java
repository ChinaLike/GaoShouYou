package com.touchrom.gaoshouyou.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.T;
import com.touchrom.gaoshouyou.activity.LoginActivity;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.module.GameDetailModule;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/17.
 * 礼包适配器
 */
public class UserGiftAdapter extends AbsOrdinaryAdapter<GiftEntity, UserGiftAdapter.GiftHolder> {
    private int mType = 1;

    /**
     * @param context
     * @param data
     * @param type    1 ==> 已领取, 2 ==> 已预定, 3 ==> 发现-礼包
     */
    public UserGiftAdapter(Context context, List<GiftEntity> data, int type) {
        super(context, data);
        mType = type;
    }

    @Override
    protected int setLayoutId(int type) {
        if (mType == 1) {
            return R.layout.item_gift_have;
        } else if (mType == 3) {
            return R.layout.item_finf_gift_rv;
        }
        return R.layout.item_gift_rv;
    }

    @Override
    public void bindData(int position, GiftHolder helper, GiftEntity item) {
        ImgHelp.setImg(getContext(), item.getImgUrl(), helper.gameIcon);
        helper.title.setText(item.getGameName() + item.getGiftName());
        helper.content.setText(mType == 1 ? "有效期至：" + item.getEndTime() : item.getGiftContent());
        BtListener btListener = (BtListener) helper.bt.getTag(R.id.bt);
        if (btListener == null) {
            btListener = new BtListener();
            helper.bt.setTag(R.id.bt, btListener);
        }
        int bg = R.drawable.selector_apk_orange_bt;
        if (mType == 2) {
            int type = item.getType();
            if (type == GiftEntity.AMOY) {
                bg = R.drawable.selector_green_bt;
            } else if (type == GiftEntity.GRAB) {
                if (!item.isGrabed()) {
                    bg = R.drawable.selector_apk_un_grey_bt;
                    helper.bt.setEnabled(false);
                }
            } else if (type == GiftEntity.RESERVATIONS) {
                bg = R.drawable.selector_apk_un_grey_bt;
                helper.bt.setEnabled(false);
                helper.bt.setText(item.isGrabed() ? "已过期" : "已预定");
            }
        } else if (mType == 1) {
            bg = R.drawable.selector_green_bt;
        } else if (mType == 3) {
            String str = "";
            switch (item.getType()) {
                case GiftEntity.AMOY:
                    bg = R.drawable.selector_green_bt;
                    str = "淘号";
                    break;
                case GiftEntity.GRAB:
                    bg = R.drawable.selector_apk_orange_bt;
                    str = "抢号";
                    break;
                case GiftEntity.RESERVATIONS:
                    bg = R.drawable.selector_blue_bt;
                    str = "预定";
                    break;
            }
            helper.bt.setText(str);
            if (helper.divider != null) {
                helper.divider.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            }
        }
        helper.bt.setBackgroundResource(bg);
        btListener.setAction(position, item);
        helper.bt.setOnClickListener(btListener);
        if (mType == 1) {
            RVGiftHolder rvGiftHolder = (RVGiftHolder) helper;
            String str = "激活码：" + item.getGiftCode();
            if (!TextUtils.isEmpty(item.getGiftCode())) {
                rvGiftHolder.giftCode.setText(StringUtil.highLightStr(str, item.getGiftCode(), Color.parseColor("#FF7537")));
            }
        }
    }

    @Override
    public GiftHolder getViewHolder(View convertView) {
        if (mType == 1) {
            return new RVGiftHolder(convertView);
        }
        return new GiftHolder(convertView);
    }

    class BtListener implements View.OnClickListener {
        int position;
        GiftEntity entity;
        ServiceUtil util;

        public void setAction(int position, GiftEntity entity) {
            this.position = position;
            this.entity = entity;
        }

        @Override
        public void onClick(View v) {
            if (!AppManager.getInstance().isLogin()) {
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            if (mType == 1) {
                copyText("礼包激活码", entity.getGiftCode());
                T.showShort(getContext(), "礼包激活码已复制到剪切板");
            } else if (mType == 2) {
                if (!entity.isGrabed()) {
                    getGiftCode();
                }
            } else if (mType == 3) {
                getGiftCode();
            }
        }

        private void getGiftCode() {
            Map<String, String> params = new HashMap<>();
            params.put("libaoId", entity.getGiftId() + "");
            params.put("typeId", entity.getType() + "");
            util = ServiceUtil.getInstance(getContext());
            util.getGiftCode(params, new GameDetailModule.GiftResponse(getContext(), util, entity.getGiftId(), entity.getType()));
        }

        /**
         * 复制文字到剪切板
         */
        private void copyText(CharSequence label, CharSequence text) {
            ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(label, text));
        }
    }

    class GiftHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.game_icon)
        ImageView gameIcon;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.content)
        TextView content;
        @InjectView(R.id.bt)
        Button bt;
        View divider;

        public GiftHolder(View view) {
            super(view);
            divider = view.findViewById(R.id.gray_divider);
        }
    }

    class RVGiftHolder extends GiftHolder {
        @InjectView(R.id.gift_code)
        TextView giftCode;

        public RVGiftHolder(View view) {
            super(view);
        }
    }
}

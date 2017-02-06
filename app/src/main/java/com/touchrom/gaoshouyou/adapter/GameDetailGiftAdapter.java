package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arialyy.frame.util.StringUtil;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.module.GameDetailModule;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/8.
 * 游戏详情礼包适配器
 */
public class GameDetailGiftAdapter extends AbsOrdinaryAdapter<GiftEntity, GameDetailGiftAdapter.GiftHolder> {

    public GameDetailGiftAdapter(Context context, List<GiftEntity> data) {
        super(context, data);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_game_detail_gift;
    }

    @Override
    public void bindData(int position, GiftHolder helper, GiftEntity item) {
        int type = getItemViewType(position);
        helper.title.setText(item.getGiftName());
        helper.content.setText(item.getGiftContent());
        if (type == GiftEntity.GRAB) {
            helper.pb.setVisibility(View.VISIBLE);
            helper.pb.setMax(100);
            int pr = 100 * item.getNum() / item.getTotalNum();
            helper.pb.setProgress(pr);
            helper.num.setText(StringUtil.highLightStr("剩余" + pr + "%", pr + "%", Color.parseColor("#0cc6c6")));
            helper.bt.setBackgroundResource(R.drawable.selector_apk_orange_bt);
            helper.bt.setText("抢号");
        } else if (type == GiftEntity.AMOY) {
            helper.pb.setVisibility(View.GONE);
            helper.bt.setBackgroundResource(R.drawable.selector_green_bt);
            helper.num.setText(StringUtil.highLightStr("淘号" + item.getNum() + "次", item.getNum() + "", Color.parseColor("#0cc6c6")));
            helper.bt.setText("淘号");
        } else if (type == GiftEntity.RESERVATIONS) {
            helper.pb.setVisibility(View.GONE);
            helper.bt.setBackgroundResource(R.drawable.selector_blue_bt);
            helper.num.setText(StringUtil.highLightStr("预定" + item.getNum() + "次", item.getNum() + "", Color.parseColor("#0cc6c6")));
            helper.bt.setText("预定");
        }
        BtListener listener = (BtListener) helper.bt.getTag(helper.bt.getId());
        if (listener == null) {
            listener = new BtListener();
            helper.bt.setTag(helper.bt.getId(), listener);
        }
        listener.setAction(position, item);
        helper.bt.setOnClickListener(listener);
    }

    @Override
    public GiftHolder getViewHolder(View convertView) {
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
            getGiftCode();
        }

        private void getGiftCode() {
            Map<String, String> params = new HashMap<>();
            params.put("libaoId", entity.getGiftId() + "");
            params.put("typeId", entity.getType() + "");
            util = ServiceUtil.getInstance(getContext());
            util.getGiftCode(params, new GameDetailModule.GiftResponse(getContext(), util, entity.getGiftId(), entity.getType()));
        }
    }

    class GiftHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.pb)
        ProgressBar pb;
        @InjectView(R.id.num)
        TextView num;
        @InjectView(R.id.content)
        TextView content;
        @InjectView(R.id.bt)
        Button bt;

        public GiftHolder(View view) {
            super(view);
        }
    }
}

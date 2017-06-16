package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.L;
import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.lyy.ui.widget.NoScrollListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityGiftDetailBinding;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.entity.TagEntity;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.GameDetailModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/11.
 * 礼包详情
 */
public class GiftDetailActivity extends BaseActivity<ActivityGiftDetailBinding> implements View.OnClickListener {
    @InjectView(R.id.game_icon)
    ImageView mImg;
    @InjectView(R.id.bt)
    Button mBt;
    @InjectView(R.id.list)
    NoScrollListView mList;
    @InjectView(R.id.pb)
    ProgressBar mPb;
    @InjectView(R.id.scroll_view)
    ScrollView mSv;

    private GiftEntity mGift;
    private int mGiftId = -1;
    private List<TagEntity> mLinkGifts = new ArrayList<>();
    private SimpleAdapter<TagEntity> mAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_gift_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mGiftId = getIntent().getIntExtra("giftId", -1);
        if (mGiftId == -1) {
            L.e(TAG, "请传入正确的礼包Id");
            finish();
            return;
        }
        getModule(GameDetailModule.class).getGiftDetail(mGiftId);
        mAdapter = new SimpleAdapter<TagEntity>(this, mLinkGifts, R.layout.item_more_gift) {
            @Override
            public void convert(SimpleViewHolder helper, TagEntity item) {
                helper.setText(R.id.gift_name, item.getTagName());
            }
        };
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GiftDetailActivity.this, GiftDetailActivity.class);
                intent.putExtra("giftId", mLinkGifts.get(position).getTagId());
                startActivity(intent);
            }
        });
        mList.setAdapter(mAdapter);
        findViewById(R.id.more).setOnClickListener(this);
        mToolbar.setTitle("礼包详情");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt:
                if (mGift.getCondition().equals("登录") && !AppManager.getInstance().isLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    getModule(GameDetailModule.class).getGiftCode(mGift.getGiftId(), mGift.getType());
                }
                break;
            case R.id.more:
                Intent intent = new Intent(this, GameDetailActivity.class);
                intent.putExtra(Constance.KEY.APP_ID, mGift.getGameId());
                intent.putExtra("tabId", 8);
                startActivity(intent);
                break;
            case R.id.game_detail:
                TurnHelp.turnGameDetail(this, mGift.getGameId());
                break;
        }
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        getModule(GameDetailModule.class).getGiftDetail(mGiftId);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_GIFT_DETAIL) {
            mGift = (GiftEntity) obj;
            mLinkGifts.clear();
            mLinkGifts.addAll(mGift.getLinkList());
            int type = mGift.getType();
            CharSequence str = "";
            if (type == GiftEntity.GRAB) {
                int pr = 100 * mGift.getNum() / mGift.getTotalNum();
                str = StringUtil.highLightStr("剩余" + pr + "%", pr + "%", Color.parseColor("#0cc6c6"));
                mBt.setText("抢号");
                mBt.setBackgroundResource(R.drawable.selector_apk_orange_bt);
                mPb.setVisibility(View.VISIBLE);
                mPb.setMax(100);
                mPb.setProgress(pr);
                getBinding().setShowGrab(true);
            } else if (type == GiftEntity.AMOY) {
                str = StringUtil.highLightStr("淘号" + mGift.getNum() + "次", mGift.getNum() + "", Color.parseColor("#0cc6c6"));
                mBt.setText("淘号");
                mBt.setBackgroundResource(R.drawable.selector_green_bt);
                mPb.setVisibility(View.INVISIBLE);
                getBinding().setShowGrab(false);
            } else if (type == GiftEntity.RESERVATIONS) {
                str = StringUtil.highLightStr("预定" + mGift.getNum() + "次", mGift.getNum() + "", Color.parseColor("#0cc6c6"));
                mBt.setText("预定");
                mBt.setBackgroundResource(R.drawable.selector_blue_bt);
                mPb.setVisibility(View.INVISIBLE);
                getBinding().setShowGrab(false);
            }
            ImgHelp.setImg(this, mGift.getImgUrl(), mImg);
            getBinding().setNum(str);
            getBinding().setGift(mGift);
            getBinding().setTitle(mGift.getGameName() + " " + mGift.getGiftName());
            getBinding().setEndTime("*礼包兑换截止时间：" + mGift.getEndTime());
            getBinding().setGameName("*该礼包仅用于高手游版《" + mGift.getGameName() + "》，请先下载游戏");
            mAdapter.notifyDataSetChanged();
        }
    }
}

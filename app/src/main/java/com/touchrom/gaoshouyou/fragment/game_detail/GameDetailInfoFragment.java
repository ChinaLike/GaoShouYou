package com.touchrom.gaoshouyou.fragment.game_detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arialyy.frame.util.StringUtil;
import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.lyy.ui.widget.NoScrollGridView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.GameDetailActivity;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameDetailInfoBinding;
import com.touchrom.gaoshouyou.entity.GameDetailInfoEntity;
import com.touchrom.gaoshouyou.module.GameDetailModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/17.
 * 游戏展示详情页面
 */
@SuppressLint("ValidFragment")
public class GameDetailInfoFragment extends BaseFragment<FragmentGameDetailInfoBinding> implements View.OnClickListener {

    private int mGameId;
    private GameDetailInfoEntity mEntity;
    @InjectView(R.id.review)
    TextView mReview;
    @InjectView(R.id.grid)
    NoScrollGridView mGrid;
    @InjectView(R.id.detail_1)
    TextView mDetail1;
    @InjectView(R.id.detail_2)
    TextView mDetail2;
    @InjectView(R.id.more)
    ImageView mMore;
    @InjectView(R.id.scroll_view)
    ScrollView mScrollView;

    public static GameDetailInfoFragment newInstance(int gameId) {
        GameDetailInfoFragment fragment = new GameDetailInfoFragment();
        Bundle b = new Bundle();
        b.putInt(Constance.KEY.INT, gameId);
        fragment.setArguments(b);
        return fragment;
    }

    private GameDetailInfoFragment() {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mGameId = getArguments().getInt(Constance.KEY.INT, -1);
        if (mGameId == -1) {
            throw new NullPointerException("请传入正确的游戏id");
        }
        getModule(GameDetailModule.class).getGameDetailInfo(mGameId);
    }

    private void setupWidget(GameDetailInfoEntity entity) {
        mEntity = entity;
        mReview.setText(mEntity.getReview());
        SimpleAdapter<CharSequence> adapter = new SimpleAdapter<CharSequence>(getContext(), formatInfo(), R.layout.item_game_detail_info) {
            @Override
            public void convert(SimpleViewHolder helper, CharSequence item) {
                helper.setText(R.id.text, item);
            }
        };
        mGrid.setAdapter(adapter);
        mDetail1.setText(mEntity.getDetail());
        mDetail2.setText(mEntity.getDetail());
        mMore.setOnClickListener(this);

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y = mScrollView.getScrollY();
                ((GameDetailActivity) mActivity).setTopState(mScrollView, -0.999 <= y && y <= 0.001);
                return false;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
            }
        }, 100);
    }

    private List<CharSequence> formatInfo() {
        List<CharSequence> list = new ArrayList<>();
        String like = "喜欢：" + mEntity.getLikeNum();
        String state = "状态：" + mEntity.getState();
        String version = "版本：" + mEntity.getVersion();
        String time = "发布：" + mEntity.getTime();
        String price = "资费：" + mEntity.getPrice();
        String language = "语言：" + mEntity.getLanguage();
        String download = "下载：" + mEntity.getDownNum();
        String factory = "厂商：" + mEntity.getFactory();
        int color = getResources().getColor(R.color.skin_text_black_color);
        list.add(StringUtil.highLightStr(like, mEntity.getLikeNum() + "", color));
        list.add(StringUtil.highLightStr(state, mEntity.getState(), color));
        list.add(StringUtil.highLightStr(version, mEntity.getVersion(), color));
        list.add(StringUtil.highLightStr(time, mEntity.getTime(), color));
        list.add(StringUtil.highLightStr(price, mEntity.getPrice(), color));
        list.add(StringUtil.highLightStr(language, mEntity.getLanguage(), color));
        list.add(StringUtil.highLightStr(download, mEntity.getDownNum() + "", color));
        list.add(StringUtil.highLightStr(factory, mEntity.getFactory(), color));
        return list;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_detail_info;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                if (mDetail1.getVisibility() == View.VISIBLE) {
                    mDetail1.setVisibility(View.GONE);
                    mDetail2.setVisibility(View.VISIBLE);
                    mMore.setImageResource(R.mipmap.icon_screen_up);
                } else {
                    mDetail1.setVisibility(View.VISIBLE);
                    mDetail2.setVisibility(View.GONE);
                    mMore.setImageResource(R.mipmap.icon_screen_down);
                }
                break;
        }
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_GAME_DETAIL_INFO) {
            setupWidget((GameDetailInfoEntity) obj);
        }
    }
}

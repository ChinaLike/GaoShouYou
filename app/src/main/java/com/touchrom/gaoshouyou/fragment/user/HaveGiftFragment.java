package com.touchrom.gaoshouyou.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.UserGiftAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentCollectBinding;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.UserModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/9.
 * 已领取的礼包
 */
@SuppressLint("ValidFragment")
public class HaveGiftFragment extends BaseFragment<FragmentCollectBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private List<GiftEntity> mData = new ArrayList<>();
    private UserGiftAdapter mAdapter;
    private boolean isRefresh = true;
    private int mPage = 1;
    private int mType = 1;

    /**
     * @param type 1==> 已领取， 2 ==> 已预定
     */
    public HaveGiftFragment(int type) {
        mType = type;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        showTempView(TempView.LOADING);
        mList.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                isRefresh = true;
                getModule(UserModule.class).getMyGift(mType, mPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isRefresh = false;
                getModule(UserModule.class).getMyGift(mType, mPage);
            }
        });
        mAdapter = new UserGiftAdapter(getContext(), mData, mType);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TurnHelp.turnGift(getContext(), mData.get(position - 1).getGiftId());
            }
        });

        getModule(UserModule.class).getMyGift(mType, mPage);
    }

    private void setUpList(List<GiftEntity> data) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        getModule(UserModule.class).getMyGift(mType, mPage);
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        mActivity.finish();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_MY_GIFT_LIST) {
            List<GiftEntity> list = (List<GiftEntity>) obj;
            if (!isRefresh && list.size() == 0) {
                hintTempView();
            }
            setUpList(list);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_collect;
    }
}

package com.touchrom.gaoshouyou.fragment.find;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.lyy.ui.widget.NoScrollGridView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.RankClassifyAdapter;
import com.touchrom.gaoshouyou.adapter.UserGiftAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentFindGiftBinding;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.entity.TagEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.FindModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/22.
 * 发现--礼包
 */
@SuppressLint("ValidFragment")
public class FindGiftFragment extends BaseFragment<FragmentFindGiftBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    @InjectView(R.id.grid)
    NoScrollGridView mGrid;
    UserGiftAdapter mAdapter;
    List<GiftEntity> mData = new ArrayList<>();
    private boolean isRefresh = true;
    private int mPage = 1;
    int mUserId;
    int mType;
    RankClassifyAdapter mAdapter1;

    public FindGiftFragment() {
//        this(AppManager.getInstance().getUser().getUserId());
    }

    public FindGiftFragment(int userId) {
        mUserId = userId;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_find_gift;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        showTempView(TempView.LOADING);
        mAdapter = new UserGiftAdapter(getContext(), mData, 3);
        mList.setAdapter(mAdapter);
        mList.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                isRefresh = true;
                notifyData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isRefresh = false;
                notifyData();
            }
        });
        mAdapter1 = new RankClassifyAdapter(getContext(), getData());
        mGrid.setAdapter(mAdapter1);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter1.selectItem(position);
                mType = position;
                isRefresh = true;
                mPage = 1;
                notifyData();
            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TurnHelp.turn(getContext(), mData.get(position - 1));
            }
        });
        notifyData();
    }

    private void notifyData() {
        getModule(FindModule.class).getGiftList(mType, mPage);
    }

    private List<TagEntity> getData() {
        List<TagEntity> list = new ArrayList<>();
        String[] tags = getResources().getStringArray(R.array.find_gift_tag);
        String[] id = getResources().getStringArray(R.array.find_gift_id);
        for (int i = 0; i < 4; i++) {
            TagEntity entity = new TagEntity();
            entity.setClassifyName(tags[i]);
            entity.setTagId(Integer.parseInt(id[i]));
            list.add(entity);
        }
        return list;
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
        notifyData();
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        hintTempView();
        isRefresh = true;
        mType = 0;
        mPage = 1;
        mAdapter1.selectItem(0);
        getModule(FindModule.class).getGiftList(mType, mPage);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_FIND_GIFT_LIST) {
            List<GiftEntity> list = (List<GiftEntity>) obj;
            if (!isRefresh && list.size() == 0) {
                hintTempView();
            }
            setUpList(list);
        }
    }

}

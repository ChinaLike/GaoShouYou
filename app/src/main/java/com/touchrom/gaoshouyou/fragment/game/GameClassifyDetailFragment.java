package com.touchrom.gaoshouyou.fragment.game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.game_info_adapter.GameInfoAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameClassifyDetailBinding;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.GameClassifyModule;
import com.touchrom.gaoshouyou.net.ServiceUtil;
import com.touchrom.gaoshouyou.util.S;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/10.
 * 游戏分类详情
 */
@SuppressLint("ValidFragment")
public class GameClassifyDetailFragment extends BaseFragment<FragmentGameClassifyDetailBinding>
        implements PullToRefreshListView.OnRefreshListener2 {

    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private int mTypeId = -1;   //动态根据tapId从后台获取数据
    private int mClassifyId = -1;   //分类Id
    private List<GameInfoEntity> mData = new ArrayList<>();
    private GameInfoAdapter mAdapter;
    private int mScreenTypeId = 0;  //筛选类型Id
    private int mScreen1Id = 0;
    private int mScreen2Id = 0;
    private int mScreen3Id = 0;
    private int mPage = 1;  //当前页数
    private boolean isRefresh = true;   //是否刷新操作

    private GameClassifyDetailFragment() {

    }

    /**
     * 分类栏目Id
     *
     * @param typeId
     * @return
     */
    public static GameClassifyDetailFragment newInstance(int classifyId, int typeId) {
        GameClassifyDetailFragment fragment = new GameClassifyDetailFragment();
        Bundle b = new Bundle();
        b.putInt("typeId", typeId);
        b.putInt("classifyId", classifyId);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTypeId = getArguments().getInt("typeId", -1);
        mClassifyId = getArguments().getInt("classifyId", -1);
        if (mTypeId == -1 || mClassifyId == -1) {
            throw new IllegalAccessError("请传入正确的分类id和栏目id");
        }
        initWidget();
        upDateData(mClassifyId, mScreenTypeId, mScreen1Id, mScreen2Id, mScreen3Id);
//        setUpList(getGameData());
    }

    private void initWidget() {
        mAdapter = new GameInfoAdapter(getContext(), mData);
        mList.setAdapter(mAdapter);
        mList.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setOnRefreshListener(this);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 == mData.size()) {
                    return;
                }
                TurnHelp.turn(getContext(), mData.get(position - 1), view, mActivity.getRootView());
            }
        });
    }

    /**
     * 更新数据
     */
    public void upDateData(int classifyId, int screenTypeId, int screen1Id, int screen2Id,
                           int screen3Id) {
        loadMore(classifyId, mTypeId, screenTypeId, screen1Id, screen2Id, screen3Id, 1);
    }

    /**
     * 更新数据
     */
    private void loadMore(int classifyId, int type, int screenTypeId, int screen1Id, int screen2Id,
                          int screen3Id, int page) {
        mScreenTypeId = screenTypeId;
        mScreen1Id = screen1Id;
        mScreen2Id = screen2Id;
        mScreen3Id = screen3Id;
        mPage = page;
        getModule(GameClassifyModule.class).getGameClassifyInfo(classifyId, type, screenTypeId,
                screen1Id, screen2Id, screen3Id, page);
    }

    /**
     * 设置列表数据
     */
    private void setUpList(List<GameInfoEntity> data) {
        mList.onRefreshComplete();
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
//        hintTempView();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_classify_detail;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        isRefresh = true;
        mPage = 1;
        upDateData(mClassifyId, mScreenTypeId, mScreen1Id, mScreen2Id, mScreen3Id);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        isRefresh = false;
        mPage++;
        loadMore(mClassifyId, mTypeId, mScreenTypeId, mScreen1Id, mScreen2Id, mScreen3Id, mPage);
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        mList.demo();
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        showLoadingDialog();
        isRefresh = true;
        mPage = 1;
        upDateData(mClassifyId, 0, 0, 0, 0);
        dismissLoadingDialog();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (obj == null || (obj instanceof Integer) && (int) obj == ServiceUtil.ERROR) {
            return;
        }
        if (!isRefresh && (obj instanceof List) && ((List) obj).size() == 0) {
            hintTempView();
            S.i(mRootView, "没有更多数据了");
        }
        if (result == ResultCode.SERVER.GET_GAME_CLASSIFY_INFO) {
            setUpList((List<GameInfoEntity>) obj);
        }
    }
}

package com.touchrom.gaoshouyou.fragment.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.GameClassifyActivity;
import com.touchrom.gaoshouyou.adapter.GameClassifyAdapter;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentGameClassifyBinding;
import com.touchrom.gaoshouyou.entity.GameClassifyEntity;
import com.touchrom.gaoshouyou.module.GameClassifyModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/12/10.
 * 游戏分类
 */
@SuppressLint("ValidFragment")
public class GameClassifyFragment extends BaseFragment<FragmentGameClassifyBinding> {
    private static volatile GameClassifyFragment mFragment = null;
    private static final Object LOCK = new Object();
    @InjectView(R.id.grid)
    GridView mGrid;
    private List<GameClassifyEntity> mData = new ArrayList<>();
    private GameClassifyAdapter mAdapter;

    public static GameClassifyFragment getInstance() {
        if (mFragment == null) {
            synchronized (LOCK) {
                mFragment = new GameClassifyFragment();
            }
        }
        return mFragment;
    }

    private GameClassifyFragment() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_classify;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        initWidget();
//        getModule(GameClassifyModule.class).getGameClassify();
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        initWidget();
        getModule(GameClassifyModule.class).getGameClassify();
        showTempView(TempView.LOADING);
    }

    private void initWidget() {
        mAdapter = new GameClassifyAdapter(getContext(), mData);
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, GameClassifyActivity.class);
                GameClassifyEntity entity = mData.get(position);
                intent.putExtra(Constance.KEY.INT, entity.getId());
                intent.putExtra(Constance.KEY.SETTING, entity.getName());
                mActivity.startActivity(intent);
            }
        });
    }

    private void setUpList(List<GameClassifyEntity> data) {
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_GAME_CLASSIFY) {   //获取分类数据
            setUpList((List<GameClassifyEntity>) obj);
        }
    }

}

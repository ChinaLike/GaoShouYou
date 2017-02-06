package com.touchrom.gaoshouyou.popupwindow;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.GameClassifyTypeAdapter;
import com.touchrom.gaoshouyou.base.BasePopupWindow;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.SimpleAdapterEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/12/10.
 * 游戏分类 类型筛选悬浮框
 */
public class GameClassifyTypePopupWindow extends BasePopupWindow {
    @InjectView(R.id.list)
    ListView mList;
    List<SimpleAdapterEntity> mData;
    int mSelectPosition = 0;    //默认被选中的位置

    public GameClassifyTypePopupWindow(Context context) {
        this(context, null, 0);
    }

    public GameClassifyTypePopupWindow(Context context, Object obj, int selectPosition) {
        super(context, null, obj);
        mSelectPosition = selectPosition;
        init();
    }

    @Override
    protected void init() {
        mData = getData();
        final GameClassifyTypeAdapter adapter = new GameClassifyTypeAdapter(getContext(), mData, mSelectPosition);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                adapter.select(position);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle b = new Bundle();
                        b.putInt("position", position);
                        b.putInt("id", mData.get(position).getArg());
                        getSimplerModule().onDialog(ResultCode.GAME_CLASSIFY_TYPE, b);
                        dismiss();
                    }
                }, 200);
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.popupwindow_game_classify_type;
    }

    /*====================================以下是测试数据==========================================*/
    private List<SimpleAdapterEntity> getData() {
        List<SimpleAdapterEntity> list = new ArrayList<>();
        String[] itemStr = new String[]{"所有游戏", "网络游戏", "单机游戏"};
        int[] id = new int[]{0, 1, 2};
        int i = 0;
        for (String str : itemStr) {
            SimpleAdapterEntity entity = new SimpleAdapterEntity();
            entity.setMsg(str); //设置名
            entity.setArg(id[i]);   //分类Id
            list.add(entity);
            i++;
        }
        return list;
    }
}

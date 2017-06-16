package com.touchrom.gaoshouyou.fragment.user.collect;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.PullToRefreshListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentCollectBinding;
import com.touchrom.gaoshouyou.entity.CollectEntity;
import com.touchrom.gaoshouyou.entity.RaiderEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.CollectModule;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/9.
 * 文章收藏Fragment
 */
@SuppressLint("ValidFragment")
public class CollectArticleFragment extends BaseFragment<FragmentCollectBinding> {
    @InjectView(R.id.list)
    PullToRefreshListView mList;
    private List<RaiderEntity> mData = new ArrayList<>();
    private SimpleAdapter<RaiderEntity> mAdapter;
    private boolean isRefresh = true;
    private int mPage = 1;
    private RaiderEntity mCurrentRaider;

    public CollectArticleFragment() {

    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        showTempView(TempView.LOADING);
        mList.setMode(PullToRefreshBase.Mode.BOTH);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                isRefresh = true;
                getModule(CollectModule.class).getCollectList(2, mPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                isRefresh = false;
                getModule(CollectModule.class).getCollectList(2, mPage);
            }
        });
        mAdapter = new SimpleAdapter<RaiderEntity>(getContext(), mData, R.layout.item_article_collect) {
            @Override
            public void convert(SimpleViewHolder helper, RaiderEntity item) {
                Button bt = helper.getView(R.id.bt);
                BtListener listener = (BtListener) bt.getTag(R.id.bt);
                if (listener == null) {
                    listener = new BtListener();
                    bt.setTag(R.id.bt, listener);
                }
                listener.setData(item);
                bt.setOnClickListener(listener);
                helper.setText(R.id.title, item.getArtName());
                helper.setText(R.id.content, item.getArtType());
            }

            class BtListener implements View.OnClickListener {
                RaiderEntity entity;

                public void setData(RaiderEntity entity) {
                    this.entity = entity;
                }

                @Override
                public void onClick(View v) {
                    mCurrentRaider = entity;
                    getModule(CollectModule.class).cancelCollect(entity.getCollectId());
                }
            }

        };

        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RaiderEntity entity = mData.get(position - 1);
                TurnHelp.turnArticle(getContext(), entity.getTypeId(), entity.getRaidersId());
            }
        });
        getModule(CollectModule.class).getCollectList(2, mPage);
    }

    private void setUpList(List<RaiderEntity> data) {
        if (isRefresh) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mList.onRefreshComplete();
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        mActivity.finish();
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        getModule(CollectModule.class).getCollectList(2, mPage);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.COLLECT) {
            CollectEntity entity = (CollectEntity) obj;
            setUpList(entity.getArticle());
            if (!isRefresh && entity.getArticle().size() == 0) {
                hintTempView();
            }
        } else if (result == ResultCode.SERVER.CANCEL_COLLECT) {
            mData.remove(mCurrentRaider);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_collect;
    }
}

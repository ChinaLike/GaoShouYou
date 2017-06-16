package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.TagEntity;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/1/12.
 * 排行榜分类适配器
 */
public class RankClassifyAdapter extends AbsOrdinaryAdapter<TagEntity, RankClassifyAdapter.RankClassifyHolder> {
    private SparseBooleanArray mSelectState = new SparseBooleanArray();

    public RankClassifyAdapter(Context context, List<TagEntity> data) {
        super(context, data);
        mSelectState.append(0, true);
        int size = mData.size();
        for (int i = 1; i < size; i++) {
            mSelectState.append(i, false);
        }
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_rank_classify;
    }

    @Override
    public void bindData(int position, RankClassifyHolder helper, TagEntity item) {
        helper.text.setText(item.getClassifyName());
        helper.text.setSelected(mSelectState.get(position));
    }

    /**
     * 设置Item选择状态
     *
     * @param position 被选中项
     */
    public void selectItem(int position) {
        int size = mData.size();
        for (int i = 0; i < size; i++) {
            mSelectState.put(i, position == i);
        }
        notifyDataSetChanged();
    }

    @Override
    public RankClassifyHolder getViewHolder(View convertView) {
        return new RankClassifyHolder(convertView);
    }

    class RankClassifyHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.text)
        TextView text;

        public RankClassifyHolder(View view) {
            super(view);
        }
    }
}

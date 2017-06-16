package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.SimpleAdapterEntity;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/10.
 * 游戏根据类型分类悬浮框的Adapter
 */
public class GameClassifyTypeAdapter extends AbsOrdinaryAdapter<SimpleAdapterEntity, GameClassifyTypeAdapter.GameClassifyTypeHolder> {

    SparseBooleanArray mSelect = new SparseBooleanArray();

    public GameClassifyTypeAdapter(Context context, List<SimpleAdapterEntity> data) {
        super(context, data);
        mSelect.append(0, true);
        for (int i = 1, count = data.size(); i < count; i++) {
            mSelect.append(i, false);
        }
    }

    /**
     * @param context
     * @param data
     * @param selectPosition 设置哪个被选中
     */
    public GameClassifyTypeAdapter(Context context, List<SimpleAdapterEntity> data, int selectPosition) {
        super(context, data);
        for (int i = 0, count = data.size(); i < count; i++) {
            mSelect.append(i, i == selectPosition);
        }
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_game_classify_type;
    }

    @Override
    public void bindData(int position, GameClassifyTypeHolder helper, SimpleAdapterEntity item) {
        helper.mText.setText(item.getMsg());
        helper.mCheck.setVisibility(mSelect.get(position) ? View.VISIBLE : View.GONE);
        helper.mText.setSelected(mSelect.get(position));
    }

    /**
     * 进行选择
     *
     * @param position
     */
    public void select(int position) {
        mSelect.clear();
        for (int i = 0, count = mData.size(); i < count; i++) {
            mSelect.append(i, i == position);
        }
        notifyDataSetChanged();
    }

    @Override
    public GameClassifyTypeHolder getViewHolder(View convertView) {
        return new GameClassifyTypeHolder(convertView);
    }

    class GameClassifyTypeHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.text)
        TextView mText;
        @InjectView(R.id.check)
        View mCheck;

        public GameClassifyTypeHolder(View view) {
            super(view);
        }
    }
}

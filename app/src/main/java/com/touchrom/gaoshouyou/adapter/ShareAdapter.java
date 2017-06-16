package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.touchrom.gaoshouyou.base.adapter.AbsRVAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsRVHolder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.SimpleAdapterEntity;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/3.
 * 分享Adapter
 */
public class ShareAdapter extends AbsRVAdapter<SimpleAdapterEntity, ShareAdapter.ShareHolder> {


    public ShareAdapter(Context context, List<SimpleAdapterEntity> data) {
        super(context, data);
    }

    @Override
    protected ShareHolder getViewHolder(View convertView, int viewType) {
        return new ShareHolder(convertView);
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_share;
    }

    @Override
    protected void bindData(ShareHolder holder, int position, SimpleAdapterEntity item) {

    }

    class ShareHolder extends AbsRVHolder {
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.text)
        TextView text;

        public ShareHolder(View itemView) {
            super(itemView);
        }
    }
}

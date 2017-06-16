package com.touchrom.gaoshouyou.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by lk on 2015/12/3.
 * RecyclerView 通用Holder
 */
public class AbsRVHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private OnItemClickListener mClick;
    private OnItemLongClickListener mLongClick;

    public interface OnItemClickListener {
        public void onItemClick(View parent, int position);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(View parent, int position);
    }

    @Override
    public void onClick(View v) {
        if (mClick != null) {
            mClick.onItemClick(v, getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mLongClick != null) {
            mLongClick.onItemLongClick(v, getLayoutPosition());
        }
        return false;
    }

    public void setItemClickListener(OnItemClickListener mClick) {
        this.mClick = mClick;
    }

    public void setItemLongClickListener(OnItemLongClickListener mLongClick) {
        this.mLongClick = mLongClick;
    }

    public AbsRVHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

}

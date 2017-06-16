package com.touchrom.gaoshouyou.base.adapter.d_adapter;

import android.content.Context;

/**
 * Created by lk on 2016/3/28.
 * Delegation基类
 */
public abstract class AbsDelegation<T, H extends AbsDHolder> implements IDelegation<T, H> {
    private int mItemType;
    private Context mContext;

    protected AbsDelegation(Context context, int itemType) {
        mItemType = itemType;
        mContext = context;
    }

    @Override
    public void onNotify() {

    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public int getItemViewType() {
        return mItemType;
    }
}

package com.touchrom.gaoshouyou.base.adapter.d_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by lk on 2016/3/28.
 * 处理含有多个ViewType的Adapter使用
 */
public abstract class AbsDAdapter<T> extends BaseAdapter {
    private LayoutInflater mInflater;
    protected List<T> mData;
    private Context mContext;
    protected DManager mManager = new DManager();

    public AbsDAdapter(Context context, List<T> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AbsDHolder holder;
        int type = getItemViewType(position);
        IDelegation delegation = mManager.getDelegate(type);
        if (convertView == null) {
            convertView = mInflater.inflate(delegation.setLayoutId(), null);
            holder = delegation.createHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AbsDHolder) convertView.getTag();
        }
        delegation.bindData(position, holder, mData.get(position));
        return convertView;
    }
}

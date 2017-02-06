package com.touchrom.gaoshouyou.base.adapter.d_adapter;

import android.view.View;

/**
 * Created by lyy on 2016/3/28.
 */
public interface IDelegation<T, H extends AbsDHolder> {
    /**
     * 更新
     */
    public void onNotify();

    /**
     * 设置布局
     *
     * @return
     */
    public int setLayoutId();

    public int getItemViewType();

    /**
     * 创建Holder
     *
     * @param convertView
     * @return
     */
    public H createHolder(View convertView);

    /**
     * 绑定数据
     *
     * @param position
     * @param helper
     * @param item
     */
    public void bindData(int position, H helper, T item);
}

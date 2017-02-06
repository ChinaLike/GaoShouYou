package com.touchrom.gaoshouyou.base.adapter.inf;

import android.view.View;

/**
 * Adapter监听
 *
 * @author Administrator
 */
public interface OnMyAdapterListener {
    /**
     * @param requestCode 请求码
     * @param sultCode    返回码
     * @param position    第几个item
     * @param data        数据
     */
    public <T> void onMyAdapter(int requestCode, int sultCode, int position, T data);

    public <T> void onMyAdapter(int position, T data);

    /**
     * item点击接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    /**
     * item长按接口
     */
    public interface OnItemLongClickListener {
        public void onItemLongClick(View v, int position);
    }
}
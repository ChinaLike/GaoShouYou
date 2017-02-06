package com.touchrom.gaoshouyou.base.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Adapter通用的holder
 *
 * @author Administrator
 */
public class SimpleViewHolder {
    /**
     * 转载一个item里面所有控件的数组
     */
    private SparseArray<View> mViews;
    /**
     * item所加载的view
     */
    private View mConvertView;
    private int mPosition;
    private Context mContext;

    private SimpleViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        mContext = context;
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static SimpleViewHolder getViewHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new SimpleViewHolder(context, parent, layoutId, position);
        }
        return (SimpleViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public SimpleViewHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public SimpleViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public SimpleViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 设置RadioButton
     *
     * @param viewId
     * @param isCheck
     * @return
     */
    public SimpleViewHolder setRadioButton(int viewId, Boolean checked) {
        RadioButton radioBt = getView(viewId);
        radioBt.setChecked(checked);
        return this;
    }

    /**
     * 是否显示radioBt
     *
     * @param viewId
     * @param visible
     * @return
     */
    public SimpleViewHolder setRadioBtVisiable(int viewId, boolean visible) {
        RadioButton radioBt = getView(viewId);
        radioBt.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    // /**
    // * 为ImageView设置图片
    // *
    // * @param viewId
    // * @param drawableId
    // * @return
    // */
    // public ViewHolder setImageByUrl(int viewId, String url)
    // {
    // ImageLoader.getInstance(3, Type.LIFO).loadImage(url,
    // (ImageView) getView(viewId));
    // return this;
    // }
    public int getPosition() {
        return mPosition;
    }
}
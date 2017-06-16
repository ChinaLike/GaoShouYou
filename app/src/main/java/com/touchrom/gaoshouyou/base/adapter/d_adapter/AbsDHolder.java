package com.touchrom.gaoshouyou.base.adapter.d_adapter;

import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by lk on 2016/3/28.
 */
public class AbsDHolder {
    View mView;
    private SparseArray<View> mViews = new SparseArray<>();

    public AbsDHolder(View view) {
        ButterKnife.inject(this, view);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }
}

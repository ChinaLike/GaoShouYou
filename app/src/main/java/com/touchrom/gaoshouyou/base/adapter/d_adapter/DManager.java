package com.touchrom.gaoshouyou.base.adapter.d_adapter;

import android.util.Log;
import android.util.SparseArray;

/**
 * Created by lk on 2016/3/28.
 */
public class DManager {
    private String TAG = "DManager";
    private SparseArray<IDelegation> mDelegates = new SparseArray<>();

    public DManager() {

    }

    public void addDelegate(IDelegation delegation) {
        mDelegates.put(delegation.getItemViewType(), delegation);
    }

    public IDelegation getDelegate(int itemType) {
        try {
            return mDelegates.get(itemType);
        } catch (NullPointerException e) {
            Log.e(TAG, "delegate 为空");
            return null;
        }
    }
}

package com.touchrom.gaoshouyou.base.adapter;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Lyy on 2015/3/12.
 */
public abstract class AbsOrdinaryViewHolder {
    public AbsOrdinaryViewHolder(View view) {
        ButterKnife.inject(this, view);
    }
}

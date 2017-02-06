package com.touchrom.gaoshouyou.help.turn.inf;

import android.content.Context;
import android.view.View;

import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lyy on 2016/3/3.
 */
public interface ITurn {
    public void onTurn(Context context, int appId);

    public void onTurn(Context context, BaseEntity entity);

    public void onTurn(Context context, BaseEntity entity, View itemView, View rootView);
}

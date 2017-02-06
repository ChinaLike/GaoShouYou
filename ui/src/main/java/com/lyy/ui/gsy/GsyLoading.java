package com.lyy.ui.gsy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import com.lyy.ui.R;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.internal.LoadingLayout;
import com.lyy.ui.util.Util;

/**
 * Created by lyy on 2015/12/9.
 * 高手游下拉刷新样式
 */
public class GsyLoading extends LoadingLayout {
    public GsyLoading(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, PullToRefreshBase.Orientation.VERTICAL, attrs);
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.gsy_load_anim;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        int b = Util.dp2px(40);
        if (null != imageDrawable) {
            ViewGroup.LayoutParams lp = mHeaderImage.getLayoutParams();
            lp.width = lp.height = Math.max(b, b);
            mHeaderImage.requestLayout();
        }
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {

    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {

    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {

    }
}

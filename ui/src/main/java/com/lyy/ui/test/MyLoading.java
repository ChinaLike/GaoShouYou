package com.lyy.ui.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.lyy.ui.R;
import com.lyy.ui.pulltorefresh.PullToRefreshBase;
import com.lyy.ui.pulltorefresh.internal.LoadingLayout;

/**
 * Created by lyy on 2015/11/19.
 */
public class MyLoading extends LoadingLayout {
    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;

    public MyLoading(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
    }

    /**
     * 下拉提示，例：“下拉以刷新”
     *
     * @param pullLabel
     */
    public final void setPullLabel(CharSequence pullLabel) {
        mPullLabel = pullLabel;
    }

    /**
     * 正在刷新提示，例：“正在刷新”
     *
     * @param refreshingLabel
     */
    public final void setRefreshingLabel(CharSequence refreshingLabel) {
        mRefreshingLabel = refreshingLabel;
    }

    /**
     * 刷新之前提示，例：“放开以刷新”
     *
     * @param releaseLabel
     */
    public final void setReleaseLabel(CharSequence releaseLabel) {
        mReleaseLabel = releaseLabel;
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.loading_anim;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPreRefresh() {
        super.onPreRefresh();
        super.setPullLabel(mPullLabel);
        super.setRefreshingLabel(mRefreshingLabel);
        super.setReleaseLabel(mReleaseLabel);
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

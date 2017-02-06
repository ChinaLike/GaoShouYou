package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.lyy.ui.pulltorefresh.x.XListView;

/**
 * Created by lyy on 2016/1/25.
 */
public class CustomListView extends XListView {
    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void refresh() {
        super.refresh();
    }

    @Override
    protected void loadMore() {
        getFooter().getHintText().setVisibility(VISIBLE);
        super.loadMore();
    }

    @Override
    public void stopLoadMore() {
        getFooter().getHintText().setVisibility(INVISIBLE);
        super.stopLoadMore();
    }
}

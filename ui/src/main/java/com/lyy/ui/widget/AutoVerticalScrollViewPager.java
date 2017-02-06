package com.lyy.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.lyy.ui.R;

/**
 * Created by lyy on 2015/11/18.
 * 带有竖直滚动功能的ViewPager
 */
public class AutoVerticalScrollViewPager extends VerticalViewPager {
    private static final String TAG = "AutoScrollViewPager";
    private static final int INTERVAL = 1000 * 5;
    private CountDownTimer mTimer;
    private int mTime;
    /**
     * 是否能滑动
     */
    private boolean isCanScroll = true;

    public AutoVerticalScrollViewPager(Context context) {
        this(context, null);
    }

    public AutoVerticalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AutoVerticalScrollViewPager, 0, 0);
        mTime = a.getInteger(R.styleable.AutoVerticalScrollViewPager_avs_interval, INTERVAL);
        isCanScroll = a.getBoolean(R.styleable.AutoScrollViewPager_av_can_scroll, true);
        a.recycle();
        setAutoScroll(true);
    }

    /**
     * 设置能否滚动
     * 如果设置为false，viewpager不能用手指移动但是能点击
     */
    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    /**
     * 设置是否自动滚动
     *
     * @param autoScroll
     */
    public void setAutoScroll(boolean autoScroll) {
        setAutoScroll(autoScroll, mTime);
    }

    /**
     * 设置是否自动滚动
     *
     * @param autoScroll
     * @param interval   时间间隔
     */
    public void setAutoScroll(boolean autoScroll, final int interval) {
        if (autoScroll) {
            mTime = interval;
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }

            mTimer = new CountDownTimer(Integer.MAX_VALUE, interval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int currentItem = getCurrentItem();
                    PagerAdapter adapter = getAdapter();
                    if (adapter == null) {
                        Log.d(TAG, "sss");
                        return;
                    }
                    int count = adapter.getCount();
                    setCurrentItem(currentItem < count - 1 ? currentItem + 1 : 0);
                }

                @Override
                public void onFinish() {

                }
            };
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTimer.start();
                }
            }, interval);
        } else {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }
}

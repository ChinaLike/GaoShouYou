package com.lyy.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.lyy.ui.R;

/**
 * Created by lyy on 2015/11/18.
 * 带有自动滚动功能的ViewPager
 */
public class AutoScrollViewPager extends ViewPager {
    private static final String TAG = "AutoScrollViewPager";
    private static final int INTERVAL = 1000 * 5;
    private CountDownTimer mTimer;
    private int mTime;

    public AutoScrollViewPager(Context context) {
        this(context, null);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AutoScrollViewPager, 0, 0);
        mTime = a.getInteger(R.styleable.AutoScrollViewPager_av_interval, INTERVAL);
        a.recycle();
        setAutoScroll(true);
    }


    /**
     * 设置是否自动滚动
     *
     * @param autoScroll
     */
    public void setAutoScroll(boolean autoScroll) {
        setAutoScroll(autoScroll, INTERVAL);
    }

    /**
     * 设置是否自动滚动
     *
     * @param autoScroll
     * @param interval   时间间隔
     */
    public void setAutoScroll(boolean autoScroll, final int interval) {
        if (autoScroll) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }

            mTimer = new CountDownTimer(Integer.MAX_VALUE, interval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int currentItem = getCurrentItem();
                    int count = getAdapter().getCount();
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

}

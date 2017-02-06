package com.lyy.ui.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by lyy on 2015/9/21.
 * 带有倒计时的Button
 */
public class CountDownButton extends Button {
    private CountDownTimer mCountTimer;
    /**
     * 记时时间
     */
    private int mCountTime = 60 * 1000;
    /**
     * 时间间隔
     */
    private int mInterval = 1000;
    private CharSequence mTextBak;
    private boolean isRunning = false;

    public CountDownButton(Context context) {
        this(context, null);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.button);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 设置记时参数
     *
     * @param countTime 总时间
     * @param interval  间隔
     */
    public void resetTimer(int countTime, int interval) {
        if (mCountTimer != null) {
            mCountTimer.cancel();
        }
        mCountTime = countTime;
        mInterval = interval;
        initTimer();
    }

    private void initTimer() {
        mCountTimer = new CountDownTimer(mCountTime, mInterval) {
            private int num = mCountTime / 1000;

            @Override
            public void onTick(long millisUntilFinished) {
                num--;
                setText(num + "秒后重发");
                isRunning = true;
                setEnabled(false);
            }

            @Override
            public void onFinish() {
                setText(mTextBak);
                setEnabled(true);
                isRunning = false;
            }
        };
    }

    public void stop() {
        if (mCountTimer != null) {
            mCountTimer.cancel();
        }
    }

    private void init() {
        mTextBak = getText();
    }

    public void start() {
        setEnabled(false);
        mTextBak = getText();
        if (mCountTimer != null) {
            mCountTimer.cancel();
        }
        initTimer();
        mCountTimer.start();
    }

    public void start(int countTime, int interval) {
        setEnabled(false);
        mTextBak = getText();
        if (mCountTimer != null) {
            mCountTimer.cancel();
        }
        mCountTime = countTime;
        mInterval = interval;
        initTimer();
        mCountTimer.start();
    }
}

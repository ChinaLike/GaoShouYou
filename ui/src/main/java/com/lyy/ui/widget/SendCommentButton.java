package com.lyy.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

import com.lyy.ui.R;

/**
 * Created by lyy on 2015/11/10.
 * 评论button
 * <href="http://jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0209/2451.html">效果</>
 */
public class SendCommentButton extends ViewAnimator {
    private static final String TAG = "SendCommentButton";

    public static final int STATE_SEND = 0;
    public static final int STATE_DONE = 1;

    private static final long RESET_STATE_DELAY_MILLIS = 1000;

    private int mCurrentState;
    private int mClickColor;
    private int mDefaultColor;

    private Runnable mRevertStateRunnable = new Runnable() {
        @Override
        public void run() {
            setCurrentState(STATE_SEND);
        }
    };

    public SendCommentButton(Context context) {
        super(context);
    }

    public SendCommentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_comment_button, this, true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialRippleLayout);

        mClickColor = a.getColor(R.styleable.SendCommentButton_send_bt_clickColor, Color.parseColor("#006633"));
        mDefaultColor = a.getColor(R.styleable.SendCommentButton_send_bt_defaultColor, Color.parseColor("#02c754"));
//        if (VersionUtil.hasLollipop()) {
//            setBackgroundColor(mDefaultColor);
//        } else {
//            setBackground(SelectorHelp.createSendBtBg(mClickColor, mDefaultColor));
//        }
        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mCurrentState = STATE_SEND;
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(mRevertStateRunnable);
        super.onDetachedFromWindow();
    }

    public void setCurrentState(int state) {
        if (state == mCurrentState) {
            return;
        }

        mCurrentState = state;
        if (state == STATE_DONE) {
            setEnabled(false);
            postDelayed(mRevertStateRunnable, RESET_STATE_DELAY_MILLIS);
            setInAnimation(getContext(), R.anim.slide_in_done);
            setOutAnimation(getContext(), R.anim.slide_out_send);
        } else if (state == STATE_SEND) {
            setEnabled(true);
            setInAnimation(getContext(), R.anim.slide_in_send);
            setOutAnimation(getContext(), R.anim.slide_out_done);
        }
        showNext();
    }


    /**
     * 错误抖动动画
     */
    public void startErroeAnim() {
        startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_error));
    }

}

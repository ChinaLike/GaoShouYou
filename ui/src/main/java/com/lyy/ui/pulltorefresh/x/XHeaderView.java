package com.lyy.ui.pulltorefresh.x;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyy.ui.R;


/**
 * The header view for {@link XListView} and
 * {@link XScrollView}
 *
 * @author markmjw
 * @date 2013-10-08
 */
public class XHeaderView extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    private LinearLayout mContainer;

    private ImageView mLoadImg;

    private TextView mHintTextView;

    private int mState = STATE_NORMAL;

    private boolean mIsFirst;
    /**
     * 正在准备刷新的文字 --> 下拉以刷新
     */
    private CharSequence mReadRefreshText;

    /**
     * 正在刷新的文字 --> 正在刷新
     */
    private CharSequence mRefreshingText;

    public XHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public XHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // Initial set header view height 0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.vw_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mHintTextView = (TextView) findViewById(R.id.header_hint_text);
        mLoadImg = (ImageView) findViewById(R.id.load_img);
        mLoadImg.setImageDrawable(getResources().getDrawable(R.drawable.gsy_load_anim));
        mReadRefreshText = getResources().getString(R.string.header_hint_refresh_ready);
        mRefreshingText = getResources().getString(R.string.header_hint_refresh_loading);
    }

    public void setState(int state) {
        if (state == mState && mIsFirst) {
            mIsFirst = true;
            return;
        }

        if (state == STATE_REFRESHING) {
            ((AnimationDrawable) mLoadImg.getDrawable()).start();
        } else {
            ((AnimationDrawable) mLoadImg.getDrawable()).stop();
        }

        switch (state) {
            case STATE_NORMAL:
                mHintTextView.setText(R.string.header_hint_refresh_normal);
                break;

            case STATE_READY:
                if (mState != STATE_READY) {
                    mHintTextView.setText(mReadRefreshText);
                }
                break;

            case STATE_REFRESHING:
                mHintTextView.setText(mRefreshingText);
                break;

            default:
                break;
        }

        mState = state;
    }

    /**
     * {@link #mReadRefreshText}
     */
    public void setReadText(CharSequence readText) {
        mReadRefreshText = readText;
        mHintTextView.setText(mReadRefreshText);
    }

    /**
     * {@link #mRefreshingText}
     */
    public void setRefreshingText(CharSequence refreshingText) {
        mRefreshingText = refreshingText;
        mHintTextView.setText(mRefreshingText);
    }

    /**
     * Set the header view visible height.
     *
     * @param height
     */
    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    /**
     * Get the header view visible height.
     *
     * @return
     */
    public int getVisibleHeight() {
        return mContainer.getHeight();
    }

}

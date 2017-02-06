package com.touchrom.gaoshouyou.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arialyy.frame.util.DensityUtils;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.widget.ChromeFloatingCirclesDrawable;
import com.lyy.ui.widget.ProgressColor;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.help.ImgHelp;

/**
 * Created by lyy on 2016/1/11.
 * 错误填充View
 */
public class TempView extends LinearLayout {
    public static final int ERROR = 0xaff1;
    public static final int NULL = 0xaff2;
    public static final int LOADING = 0xaff3;
    private static final String TAG = "TempView";
    private ImageView mImg;
    private Button mBt;
    private TextView mText;
    private int mType = ERROR;

    private int mErrorDrawable;
    private int mTempDrawable;

    private CharSequence mErrorStr = "重新加载";
    private CharSequence mEmptyStr = "别处看看";

    private CharSequence mErrorHintStr = "网络错误";
    private CharSequence mEmptyHintStr = "什么都没找到";
    private OnTempBtListener mBtListener;
    private LinearLayout mTemp;
    private LinearLayout mErrorTemp;
    private ImageView mLoadingTemp;
//    private View mLoadingTemp;

    public interface OnTempBtListener {
        /**
         * @param type {@link #ERROR}, {@link #NULL}, {@link #LOADING}
         */
        public void onTempBt(int type);
    }

    public TempView(Context context) {
        this(context, null);
    }

    public TempView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TempView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_error_temp, this);
        mErrorDrawable = R.mipmap.icon_error;
        mTempDrawable = R.mipmap.icon_empty;
        mImg = (ImageView) findViewById(R.id.img);
        mBt = (Button) findViewById(R.id.bt);
        mText = (TextView) findViewById(R.id.text);
        mTemp = (LinearLayout) findViewById(R.id.temp);
        mErrorTemp = (LinearLayout) findViewById(R.id.error_temp);
        mLoadingTemp = (ImageView) findViewById(R.id.loading);
//        mLoadingTemp = findViewById(R.id.loading);
        mBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtListener != null) {
                    mBtListener.onTempBt(mType);
                }
            }
        });
        setType(mType);
    }

    /**
     * 设置距离顶部的高度
     */
    public void setMarginTop(int dp) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, DensityUtils.dp2px(dp), 0, 0);
        mTemp.setLayoutParams(lp);
        requestLayout();
    }

    public int getType() {
        return mType;
    }

    /**
     * 设置TempBt监听
     *
     * @param listener
     */
    public void setOnTempBtListener(OnTempBtListener listener) {
        mBtListener = listener;
    }

    /**
     * 关闭加载填充
     */
    public void closeLoading() {
        if (mLoadingTemp != null) {
            mLoadingTemp.setVisibility(GONE);
        }
    }

    /**
     * 设置填充类型
     *
     * @param type {@link #ERROR}, {@link #NULL}, {@link #LOADING}
     */
    public void setType(int type) {
        mType = type;
        if (type == LOADING) {
            AnimationDrawable ad = ImgHelp.createLoadingAnim(getContext());
            mLoadingTemp.setImageDrawable(ad);
            ad.start();
            mLoadingTemp.setVisibility(VISIBLE);
            mErrorTemp.setVisibility(GONE);
            Drawable drawable = new ChromeFloatingCirclesDrawable.Builder(getContext())
                    .colors(ProgressColor.COLOR)
                    .build();
//            mLoadingTemp.setBackgroundDrawable(drawable);

//            if (AndroidVersionUtil.hasLollipop()) {
//                mLoadingTemp.setIndeterminateDrawable(new ColorDrawable(Color.BLUE));
//                mLoadingTemp.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
//                mLoadingTemp.postInvalidateDelayed(1000);
//            } else {
//                mLoadingTemp.setIndeterminateDrawable(drawable);
//            }
            L.d(TAG, "loading");
            requestLayout();
            return;
        }
        mLoadingTemp.setVisibility(GONE);
        mErrorTemp.setVisibility(VISIBLE);
        if (type == ERROR) {
            mImg.setImageResource(mErrorDrawable);
            mText.setText(mErrorHintStr);
            mBt.setText(mErrorStr);
        } else if (type == NULL) {
            mImg.setImageResource(mTempDrawable);
            mText.setText(mEmptyHintStr);
            mBt.setText(mEmptyStr);
        } else {
            L.e(TAG, "类型错误");
        }
        requestLayout();
    }

}

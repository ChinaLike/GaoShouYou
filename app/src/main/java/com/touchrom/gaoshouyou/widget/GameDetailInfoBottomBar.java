package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.touchrom.gaoshouyou.R;

/**
 * Created by lyy on 2015/12/17.
 * 游戏展示页面底部栏
 */
public class GameDetailInfoBottomBar extends RelativeLayout implements View.OnClickListener {
    private String TAG = "GameDetailInfoBottomBar";
    private ImageView mLike;
    private ImageView mShare;
    private Button mDownload;
    private boolean isCollect = false;
    private int mCollectId;

    private OnGDIBarClickListener mListener;

    public interface OnGDIBarClickListener {
        public void onGDIBarClick(View view);
    }

    public GameDetailInfoBottomBar(Context context) {
        this(context, null);
    }

    public GameDetailInfoBottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameDetailInfoBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_game_detail_info_bottom_bar, this, true);
        mLike = (ImageView) findViewById(R.id.like);
        mShare = (ImageView) findViewById(R.id.share);
        mDownload = (Button) findViewById(R.id.download);
        mLike.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mDownload.setOnClickListener(this);
    }

    /**
     * @param collect   收藏状态
     * @param collectId 收藏id
     */
    public void setCollectState(boolean collect, int collectId) {
        this.isCollect = collect;
        mCollectId = collectId;
//        mLike.setImageResource(img);
    }

    public boolean isCollect() {
        return isCollect;
    }

    public int getCollectId() {
        return mCollectId;
    }

    public void setOnGDIBarListener(OnGDIBarClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onGDIBarClick(v);
        }
    }
}

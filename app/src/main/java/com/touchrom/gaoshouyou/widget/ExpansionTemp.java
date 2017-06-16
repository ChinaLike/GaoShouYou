package com.touchrom.gaoshouyou.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.util.AndroidUtils;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseEntity;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.entity.NewsEntity;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;

import java.util.List;

/**
 * Created by lk on 2016/2/19.
 */
@TargetApi(11)
public class ExpansionTemp extends LinearLayout {
    private static final String TAG = "ExpansionTemp";
    private View mHandleView;
    private WindowManager mWm;
    private ImageView mImg;
    private View mTemp;
    //    private ProgressBar mPb;
    private ImageView mPb;
    private int[] mLocation = new int[2];
    private BaseEntity mEntity;
    private View mRootView;

    public ExpansionTemp(Context context, View rootView, View handleView, WindowManager wm, BaseEntity entity) {
        super(context, null);
        mHandleView = handleView;
        mRootView = rootView;
        mWm = wm;
        mEntity = entity;
        init();
    }

    public ExpansionTemp(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ExpansionTemp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_expansion, this);
        mImg = (ImageView) findViewById(R.id.ep_img);
        mTemp = findViewById(R.id.ep_line);
//        mPb = (ProgressBar) findViewById(R.id.progress);
        mPb = (ImageView) findViewById(R.id.ep_progress);
        mHandleView.getLocationInWindow(mLocation);
        int sbh = AndroidUtils.getStatusBarHeight(getContext());
        mImg.setTranslationY(mLocation[1] - sbh);
        mTemp.setTranslationY(mLocation[1] + mImg.getMeasuredHeight() / 2 + sbh);
        mHandleView.findViewById(R.id.gray_divider).setBackgroundColor(Color.TRANSPARENT);
        mPb.setVisibility(GONE);
        Bitmap bm = getViewImg(mHandleView);
        if (bm != null) {
            mImg.setImageBitmap(getViewImg(mHandleView));
        }
        AnimationDrawable ad = ImgHelp.createLoadingAnim(getContext());
        mPb.setImageDrawable(ad);
        ad.setOneShot(false);
        ad.start();
    }

    public void show() {
        handleRootView();
        setBackgroundColor(Color.parseColor("#7f000000"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTemp.setVisibility(View.VISIBLE);
                expansion();
            }
        }, 500);
    }

    private void handleRootView() {
        ObjectAnimator setScaleY = ObjectAnimator.ofFloat(mRootView, "scaleY", 1f, 0.95f);
        ObjectAnimator setScaleX = ObjectAnimator.ofFloat(mRootView, "scaleX", 1f, 0.95f);
        AnimatorSet set = new AnimatorSet();
        set.play(setScaleX).with(setScaleY);
        set.setDuration(500);
        set.start();
    }

    public Bitmap getViewImg(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        int width = AndroidUtils.getScreenParams(getContext())[0];
        Bitmap bmp = view.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        Bitmap bp;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, bmp.getHeight());
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 扩展到整个屏幕
     */
    private void expansion() {
        int wh = AndroidUtils.getScreenParams(getContext())[1];
        int sbh = AndroidUtils.getStatusBarHeight(getContext());
        int h = Math.max(mLocation[1], Math.abs(mLocation[1] - wh));
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTemp, "scaleY", 1f, h + sbh);
        animator.setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mImg.setVisibility(View.GONE);
                int color = getContext().getResources().getColor(R.color.skin_background_color);
                mHandleView.findViewById(R.id.gray_divider).setBackgroundColor(color);
                if (mEntity instanceof GameInfoEntity) {
                    TurnHelp.turnGameDetail(getContext(), ((GameInfoEntity) mEntity).getAppId(), false);
                } else if (mEntity instanceof NewsEntity) {
                    NewsEntity entity = ((NewsEntity) mEntity);
                    int type = entity.getType();
                    if (type == NewsEntity.ITEM_BANNER) {
                        return;
                    } else if (type == NewsEntity.ITEM_ARTICLE) {
                        TurnHelp.turnArticle(getContext(), entity.getArticle().getTypeId(), entity.getArticle().getArticleId());
                    } else if (type == NewsEntity.ITEM_REVIEW_ARTICLE) {
                        TurnHelp.turnArticle(getContext(), entity.getReviewArticle().getTypeId(), entity.getReviewArticle().getArticleId());
                    }
                } else if (mEntity instanceof GiftEntity) {
                    TurnHelp.turnGift(getContext(), ((GiftEntity) mEntity).getGiftId());
                }
                mRootView.setScaleY(1f);
                mRootView.setScaleX(1f);
                mPb.setVisibility(VISIBLE);
                setBackgroundColor(Color.TRANSPARENT);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shrink();
                    }
                }, 1000);
            }
        });
        animator.start();
    }

    /**
     * 收缩
     */
    private void shrink() {
        mPb.setVisibility(GONE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTemp, "alpha", 1f, 0f);
        animator.setDuration(800);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTemp.setVisibility(GONE);
//                mTemp.setScaleY(1f);
                mPb.setVisibility(GONE);
                mWm.removeView(ExpansionTemp.this);
                recycle();
            }
        });
        animator.start();
    }

    private void recycle() {
        Drawable drawable = mImg.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            ((BitmapDrawable) drawable).getBitmap().recycle();
        }
//        List<SkinView> list = SkinManager.getInstance().getSkinViews((AbsActivity) getContext());
//        if (list != null) {
//            for (int i = 0; i < list.size(); i++) {
//                SkinView sv = list.get(i);
//                switch (sv.getView().getId()) {
//                    case R.id.ep_img:
//                    case R.id.ep_line:
//                    case R.id.ep_progress:
//                        list.remove(sv);
//                        break;
//                }
//            }
//        }
    }
}

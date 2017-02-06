package com.touchrom.gaoshouyou.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.databinding.FragmentBannerBinding;
import com.touchrom.gaoshouyou.entity.BannerEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/11/18.
 * Banner
 */
@SuppressLint("ValidFragment")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BannerFragment extends BaseFragment<FragmentBannerBinding> {
    @InjectView(R.id.img_banner)
    ImageView mBannerImg;
    private BannerEntity mEntity;
    private boolean isCanClick = false;

    private BannerFragment() {

    }

    public static BannerFragment newInstance(BannerEntity entity) {
        BannerFragment fragment = new BannerFragment();
        Bundle b = new Bundle();
        b.putParcelable(Constance.KEY.PARCELABLE_ENTITY, entity);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mEntity = getArguments().getParcelable(Constance.KEY.PARCELABLE_ENTITY);
//        mBannerImg.setScaleType(mScaleType);
        setUpData(mEntity);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCanClick) {
                    return;
                }
                TurnHelp.turn(getContext(), mEntity);

            }
        });
    }

    /**
     * 获取ImageView
     */
    public ImageView getBannerImg() {
        return mBannerImg;
    }

    /**
     * 设置能否点击
     */
    public void setCanClick(boolean isCanClick) {
        this.isCanClick = isCanClick;
    }

    /**
     * 设置数据
     *
     * @param entity
     */
    private void setUpData(BannerEntity entity) {
        if (entity != null && mSettingEntity.isShowImg()) {
            //databind会自动给控件设置tag的
            mBannerImg.setTag(null);
            mBannerImg.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getContext()).load(entity.getImgUrl())
                    .error(R.mipmap.default_banner)
                    .placeholder(R.mipmap.default_banner)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mBannerImg);
        }
    }

    /**
     * 设置图片
     */
    public void setDrawable(@DrawableRes int drawable) {
        if (mBannerImg != null) {
            mBannerImg.setImageResource(drawable);
        }
    }

    /**
     * 更新数据
     */
    public void update(BannerEntity entity) {
        mEntity = entity;
        setUpData(entity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_banner;
    }
}

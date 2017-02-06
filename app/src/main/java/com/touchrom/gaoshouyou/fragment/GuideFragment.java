package com.touchrom.gaoshouyou.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.databinding.FragmentGuideBinding;

import butterknife.InjectView;

@SuppressLint("ValidFragment")
public class GuideFragment extends BaseFragment<FragmentGuideBinding> {
    @InjectView(R.id.img)
    ImageView mImg;
    private int mDrawable = -1;

    private GuideFragment(@DrawableRes int drawable) {
        mDrawable = drawable;
    }

    public static GuideFragment newInstance(@DrawableRes int drawable) {
        return new GuideFragment(drawable);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (mDrawable != -1) {
            Glide.with(getContext()).load(mDrawable).into(mImg);
        }
    }
}
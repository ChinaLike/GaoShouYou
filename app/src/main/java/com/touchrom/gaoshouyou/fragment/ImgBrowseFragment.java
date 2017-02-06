package com.touchrom.gaoshouyou.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.databinding.FragmentGuideBinding;

import butterknife.InjectView;

@SuppressLint("ValidFragment")
public class ImgBrowseFragment extends BaseFragment<FragmentGuideBinding> {
    @InjectView(R.id.img)
    ImageView mImg;
    private String mUrl;

    private ImgBrowseFragment(String url) {
        mUrl = url;
    }

    public static ImgBrowseFragment newInstance(String url) {
        return new ImgBrowseFragment(url);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_img_browse;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (!TextUtils.isEmpty(mUrl)) {
            Glide.with(getContext()).load(mUrl).into(mImg);
        }
    }
}
package com.touchrom.gaoshouyou.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.BaseDialog;
import com.touchrom.gaoshouyou.databinding.DialogImgBrowseBinding;
import com.touchrom.gaoshouyou.fragment.ImgBrowseFragment;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/2/2.
 * 图片浏览对话框
 */
@TargetApi(11)
@SuppressLint("ValidFragment")
public class ImgBrowseDialog extends BaseDialog<DialogImgBrowseBinding> implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;
    @InjectView(R.id.view_pager)
    ViewPager mVp;
    @InjectView(R.id.count)
    TextView mCount;
    private List<String> mImgs;
    private int mPosition = 0;

    public ImgBrowseDialog(List<String> imgs) {
        mImgs = imgs;
    }

    public ImgBrowseDialog(List<String> imgs, int position) {
        mImgs = imgs;
        mPosition = position;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_img_browse;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mVp.setPageTransformer(true, this);
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getChildFragmentManager());
        int i = 0;
        for (String img : mImgs) {
            adapter.addFrag(ImgBrowseFragment.newInstance(img), "img" + i);
            i++;
        }
        mVp.setAdapter(adapter);
        mVp.setCurrentItem(mPosition);
        getBinding().setCount((mPosition + 1) + "/" + mImgs.size());
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getBinding().setCount((position + 1) + "/" + mImgs.size());
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        if (position < -1) {
            view.setAlpha(0);

        } else if (position <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else {
            view.setAlpha(0);
        }
    }
}

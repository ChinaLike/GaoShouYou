package com.touchrom.gaoshouyou.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.lyy.ui.widget.CircleIndicator;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.SimpleViewPagerAdapter;
import com.touchrom.gaoshouyou.base.BaseDialog;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.DialogGuideBinding;
import com.touchrom.gaoshouyou.fragment.GuideFragment;
import com.touchrom.gaoshouyou.help.RippleHelp;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/1/11.
 * 欢迎页
 */
@SuppressLint("ValidFragment")
public class GuideDialog extends BaseDialog<DialogGuideBinding> implements View.OnClickListener {
    @InjectView(R.id.view_pager)
    ViewPager mVp;
    @InjectView(R.id.indicator)
    CircleIndicator mIndicator;
    @InjectView(R.id.start_app)
    Button mBt;


    public GuideDialog(Object obj) {
        super(obj);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_guide;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int[] drawables = new int[]{
                R.mipmap.bg_welcome_1,
                R.mipmap.bg_welcome_2,
                R.mipmap.bg_welcome_3,
        };
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getChildFragmentManager());
        for (int drawable : drawables) {
            adapter.addFrag(GuideFragment.newInstance(drawable), "guide");
        }
        mVp.setAdapter(adapter);
        mVp.setOffscreenPageLimit(drawables.length);
        mIndicator.setViewPager(mVp);
        RippleHelp.createRipple(getContext(), mBt);
        mBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getSimplerModule().onDialog(ResultCode.DIALOG.GUIDE, null);
        dismiss();
    }
}

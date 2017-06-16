package com.touchrom.gaoshouyou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.touchrom.gaoshouyou.base.adapter.CycleFragmentPagerAdapter;
import com.touchrom.gaoshouyou.entity.BannerEntity;
import com.touchrom.gaoshouyou.fragment.ScreenshotFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lk on 2015/11/18.
 * Banner 循环滚动适配器
 */
public class BannerCycleAdapter extends CycleFragmentPagerAdapter {
    private List<BannerEntity> mEntitys = new ArrayList<>();

    public BannerCycleAdapter(FragmentManager fm, List<BannerEntity> entities) {
        super(fm, entities);
        mEntitys.clear();
        mEntitys.addAll(entities);
    }

    @Override
    public Fragment getItem(int position) {

        return ScreenshotFragment.newInstance(mEntitys.get(position));
    }
}

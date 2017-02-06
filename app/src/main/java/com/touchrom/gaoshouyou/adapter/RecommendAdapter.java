package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.adapter.CycleFragmentPagerAdapter;
import com.touchrom.gaoshouyou.base.adapter.RecyclingPagerAdapter;
import com.touchrom.gaoshouyou.entity.RecommendEntity;
import com.touchrom.gaoshouyou.entity.SettingEntity;
import com.touchrom.gaoshouyou.fragment.RecommendFragment;
import com.touchrom.gaoshouyou.help.ImgHelp;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/11/18.
 * Banner 循环滚动适配器
 */
public class RecommendAdapter extends CycleFragmentPagerAdapter {
//public class RecommendAdapter extends RecyclingPagerAdapter {
    private List<RecommendEntity> mEntitys = new ArrayList<>();
//    private final LayoutInflater inflater;
//    private Context mContext;
//    protected SettingEntity mSettingEntity;
//
//    public RecommendAdapter(Context context, List<RecommendEntity> data) {
//        inflater = LayoutInflater.from(context);
//        mContext = context;
//        mSettingEntity = AppManager.getInstance().getSetting();
//        mEntitys = data;
//    }


    public RecommendAdapter(FragmentManager fm, List<RecommendEntity> entities) {
        super(fm, entities);
        mEntitys.clear();
        mEntitys.addAll(entities);
    }

    public void update(List<RecommendEntity> entity) {
//        mEntitys.clear();
        mEntitys.addAll(entity);
        mEntitys.addAll(entity);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {

        return RecommendFragment.newInstance(mEntitys.get(position));
    }

//    @Override
//    public View getView(int position, View view, ViewGroup container) {
//        ViewHolder holder;
//        if (view != null) {
//            holder = (ViewHolder) view.getTag();
//        } else {
//            view = inflater.inflate(R.layout.fragment_game_recommended, container, false);
//            holder = new ViewHolder(view);
//            view.setTag(holder);
//        }
//        RecommendEntity entity = mEntitys.get(position);
//        holder.gameIcon.setTag(null);
//        if (!TextUtils.isEmpty(entity.getGameIconUrl()) && mSettingEntity.isShowImg()) {
//            ImgHelp.setImg(mContext, entity.getGameIconUrl(), holder.gameIcon);
//        }
//        holder.title.setText(entity.getTitle());
//        holder.gameDetail.setText(entity.getGameDetail());
//        holder.gameName.setText(entity.getGameName());
//        return view;
//    }
//
//    @Override
//    public int getCount() {
//        return mEntitys.size();
//    }
//
//    private static class ViewHolder {
//        ImageView gameIcon;
//        ImageView titleIcon;
//        TextView title;
//        TextView gameName;
//        TextView gameDetail;
//
//        public ViewHolder(View view) {
//            gameIcon = (ImageView) view.findViewById(R.id.game_icon);
//            titleIcon = (ImageView) view.findViewById(R.id.title_icon);
//            title = (TextView) view.findViewById(R.id.title);
//            gameName = (TextView) view.findViewById(R.id.game_name);
//            gameDetail = (TextView) view.findViewById(R.id.game_detail);
//        }
//    }

}

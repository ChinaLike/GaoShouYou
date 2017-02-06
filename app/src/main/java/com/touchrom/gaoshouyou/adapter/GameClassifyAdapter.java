package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.lyy.ui.widget.CircleImageView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.entity.GameClassifyEntity;
import com.touchrom.gaoshouyou.entity.SettingEntity;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/12/10.
 */
public class GameClassifyAdapter extends AbsOrdinaryAdapter<GameClassifyEntity, GameClassifyAdapter.ClassifyHolder> {
    private SettingEntity mSettingEntity;

    public GameClassifyAdapter(Context context, List<GameClassifyEntity> data) {
        super(context, data);
        mSettingEntity = AppManager.getInstance().getSetting();
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_game_classify;
    }

    @Override
    public void bindData(int position, ClassifyHolder holder, GameClassifyEntity item) {
        if (mSettingEntity.isShowImg()) {
//            ImgHelp.setImg(getContext(), item.getImgUrl(), holder.img);
            Glide.with(getContext()).load(item.getImgUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .error(R.mipmap.default_icon)
                    .into(holder.img);
        }
        holder.name.setText(item.getName());
        holder.num.setText("(" + item.getNum() + ")");
        holder.rightLine.setVisibility(position % 2 == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void notifyDataSetChanged() {
        mSettingEntity = AppManager.getInstance().getSetting();
        super.notifyDataSetChanged();
    }

    @Override
    public ClassifyHolder getViewHolder(View convertView) {
        return new ClassifyHolder(convertView);
    }

    class ClassifyHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.img)
        CircleImageView img;
        @InjectView(R.id.name)
        TextView name;
        @InjectView(R.id.num)
        TextView num;
        @InjectView(R.id.right_line)
        View rightLine;

        public ClassifyHolder(View view) {
            super(view);
        }
    }
}

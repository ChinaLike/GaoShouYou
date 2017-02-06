package com.touchrom.gaoshouyou.adapter.game_info_adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.touchrom.gaoshouyou.base.adapter.d_adapter.AbsDHolder;
import com.touchrom.gaoshouyou.base.adapter.d_adapter.AbsDelegation;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.entity.SettingEntity;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/28.
 */
class GameImgDelegate extends AbsDelegation<GameInfoEntity, GameImgDelegate.GameImgHolder> {
    private SettingEntity mSettingEntity;

    protected GameImgDelegate(Context context, int itemType) {
        super(context, itemType);
        mSettingEntity = AppManager.getInstance().getSetting();
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_game_img;
    }

    @Override
    public GameImgHolder createHolder(View convertView) {
        return new GameImgHolder(convertView);
    }

    @Override
    public void bindData(int position, GameImgHolder helper, GameInfoEntity item) {
        if (mSettingEntity.isShowImg()) {
//            ImgHelp.setImg(mContext, entity.getIconUrl(), R.mipmap.default_ad, imgHolder.adImg);
            Glide.with(getContext()).load(item.getIconUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .centerCrop()
                    .fitCenter()
                    .into(helper.adImg);
        }
        helper.divider.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onNotify() {
        super.onNotify();
        mSettingEntity = AppManager.getInstance().getSetting();
    }

    class GameImgHolder extends AbsDHolder {
        @InjectView(R.id.ad_img)
        ImageView adImg;
        @InjectView(R.id.gray_divider)
        View divider;

        public GameImgHolder(View view) {
            super(view);
        }
    }
}

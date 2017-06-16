package com.touchrom.gaoshouyou.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.databinding.FragmentGameRecommendedBinding;
import com.touchrom.gaoshouyou.entity.RecommendEntity;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/7.
 * Game界面推荐Banner
 */
@SuppressLint("ValidFragment")
public class RecommendFragment extends BaseFragment<FragmentGameRecommendedBinding> {
    @InjectView(R.id.game_icon)
    ImageView mGameIcon;
    @InjectView(R.id.title_icon)
    ImageView mTitleIcon;

    private RecommendEntity mEntity;


    private RecommendFragment() {

    }

    public static RecommendFragment newInstance(RecommendEntity entity) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle b = new Bundle();
        b.putParcelable(Constance.KEY.PARCELABLE_ENTITY, entity);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_game_recommended;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mEntity = getArguments().getParcelable(Constance.KEY.PARCELABLE_ENTITY);
        initWidget();
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnHelp.turn(getContext(), mEntity);
            }
        });
    }

    private void initWidget() {
        mGameIcon.setTag(null);
        mTitleIcon.setTag(null);
        if (!TextUtils.isEmpty(mEntity.getGameIconUrl()) && mSettingEntity.isShowImg()) {
            ImgHelp.setImg(mActivity, mEntity.getGameIconUrl(), mGameIcon);
        }
        getBinding().setTitle(mEntity.getTitle());
        getBinding().setGameName(mEntity.getGameName());
        getBinding().setGameDetail(mEntity.getGameDetail());
    }
}

package com.touchrom.gaoshouyou.fragment.user.honor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.arialyy.frame.util.StringUtil;
import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentHonorLevBinding;
import com.touchrom.gaoshouyou.entity.HonorEntity;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.UserModule;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/21.
 * 高手等级
 */
public class HonorLevFragment extends BaseFragment<FragmentHonorLevBinding> {
    @InjectView(R.id.bt)
    Button mBt;
    @InjectView(R.id.list)
    ListView mList;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_honor_lev;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getModule(UserModule.class).getHonorLev();
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebEntity entity = new WebEntity();
                entity.setTitle("等级规则");
                entity.setContentUrl(Constance.URL.LEV_RULE);
                TurnHelp.turn(getContext(), entity);
            }
        });
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_MY_HONOR_LEV) {
            HonorEntity entity = (HonorEntity) obj;
            String str = "当前等级：" + entity.getCurrentLev();
            final int color = getResources().getColor(R.color.orange);
            getBinding().setLev(StringUtil.highLightStr(str, entity.getCurrentLev() + "", color));
            getBinding().setExp("升级还需" + entity.getNeedExp() + "经验");
            getBinding().setTotal("经验明细(总计：" + entity.getTotal() + ")");
            SimpleAdapter<HonorEntity.LevExpEntity> adapter = new SimpleAdapter<HonorEntity.LevExpEntity>
                    (getContext(), entity.getExps(), R.layout.item_honor_lev) {
                @Override
                public void convert(SimpleViewHolder helper, HonorEntity.LevExpEntity item) {
                    String str = item.getTypeName() + " +" + item.getExp();
                    helper.setText(R.id.num, StringUtil.highLightStr(str, "+" + item.getExp(), color));
                    helper.setText(R.id.time, item.getTime());
                }
            };
            mList.setAdapter(adapter);
        }
    }
}

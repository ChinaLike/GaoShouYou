package com.touchrom.gaoshouyou.fragment.user.honor;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentHonorDesignBinding;
import com.touchrom.gaoshouyou.entity.HonorTagEntity;
import com.touchrom.gaoshouyou.module.UserModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/21.
 * 高手称号
 */
public class HonorDesignFragment extends BaseFragment<FragmentHonorDesignBinding> {
    @InjectView(R.id.grid)
    GridView mGrid;
    int mCurrentP = 1;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_honor_design;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getModule(UserModule.class).getHonorDesign();
    }


    private List<String> getData(HonorTagEntity tagEntity) {
        List<String> list = new ArrayList<>();
        for (int i = 0, count = tagEntity.getTitle().size(); i < count; i++) {
            list.add(tagEntity.getGrade().get(i));
            String title = tagEntity.getTitle().get(i);
            list.add(title);
            if (tagEntity.getCurrentTag().equals(title)) {
                mCurrentP = i;
            }
        }
        return list;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_MY_HONOR_DESIGN) {
            HonorTagEntity tagEntity = (HonorTagEntity) obj;
            DesignAdapter adapter = new DesignAdapter(getContext(), getData(tagEntity), R.layout.item_honor_design);
            mGrid.setAdapter(adapter);
        }
    }

    class DesignAdapter extends SimpleAdapter<String> {
        SparseBooleanArray selected;

        @Override
        protected void pre() {
            super.pre();
            selected = new SparseBooleanArray();
            for (int i = 0, count = mData.size() / 2; i < count; i++) {
                selected.put(i, false);
            }
            selected.put(mCurrentP, true);
        }

        @Override
        public void convert(SimpleViewHolder helper, String item) {
            TextView text = helper.getView(R.id.text);
            View line = helper.getView(R.id.line);
            if (mCurrentPosition % 2 == 0) {
                line.setBackgroundColor(getResources().getColor(R.color.skin_line_color));
                text.setTextColor(getResources().getColor(R.color.skin_text_gray_color));
            } else {
                text.setTextColor(getResources().getColor(R.color.skin_white_color));
                text.setBackgroundResource(R.drawable.selector_honor_design_bg);
                line.setBackgroundColor(getResources().getColor(R.color.skin_white_color));
                text.setSelected(selected.get(mCurrentPosition / 2));
            }
            text.setText(item);
        }

        public DesignAdapter(Context context, List<String> mData, int itemLayoutId) {
            super(context, mData, itemLayoutId);
        }
    }
}

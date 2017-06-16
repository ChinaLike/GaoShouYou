package com.touchrom.gaoshouyou.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;

import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.lyy.ui.widget.NoScrollListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.databinding.FragmentUserDataBinding;
import com.touchrom.gaoshouyou.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/24.
 * 用户资料
 */
@SuppressLint("ValidFragment")
public class UserDataFragment extends BaseFragment<FragmentUserDataBinding>{
    @InjectView(R.id.list)
    NoScrollListView mList;
    SimpleAdapter<Tag> mAdapter;
    UserEntity mEntity;

    public UserDataFragment(UserEntity entity){
        mEntity = entity;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_user_data;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mAdapter = new SimpleAdapter<Tag>(getContext(), getData(), R.layout.item_user_data) {
            @Override
            public void convert(SimpleViewHolder helper, Tag item) {
                helper.setText(R.id.hint, item.hint);
                helper.setText(R.id.content, item.content);
            }
        };
        mList.setAdapter(mAdapter);
    }

    private List<Tag> getData(){
        List<Tag> list = new ArrayList<>();
        list.add(new Tag("昵称：", mEntity.getNikeName()));
        list.add(new Tag("性别：", mEntity.getSex()));
        list.add(new Tag("取向：", mEntity.getSexDir()));
        list.add(new Tag("地区：", mEntity.getLocation()));
        list.add(new Tag("签名：", mEntity.getSign()));
        String[] tas = mEntity.getTags();
        if (tas == null){
            list.add(new Tag("标签：", ""));
            return list;
        }
        String tag = "";
        for (String s : tas){
            tag += "、" + s;
        }
        if (!TextUtils.isEmpty(tag)){
            tag = tag.substring(1, tag.length() - 1);
        }
        list.add(new Tag("标签：", tag));
        return list;
    }

    private class Tag{
        public Tag(String hint, String content){
            this.hint = hint;
            this.content = content;
        }
        String hint, content;
    }
}

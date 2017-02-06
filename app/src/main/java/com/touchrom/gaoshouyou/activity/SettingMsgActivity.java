package com.touchrom.gaoshouyou.activity;

import android.os.Bundle;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.databinding.ActivitySettingMsgBinding;
import com.touchrom.gaoshouyou.entity.MsgSettingEntity;
import com.touchrom.gaoshouyou.widget.SettingSwitchInfo;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/12/1.
 * 推送消息设置界面
 */
public class SettingMsgActivity extends BaseActivity<ActivitySettingMsgBinding> {
    @InjectView(R.id.update)
    SettingSwitchInfo mUpdate;
    @InjectView(R.id.system)
    SettingSwitchInfo mSystem;
    @InjectView(R.id.notice)
    SettingSwitchInfo mNotice;
    @InjectView(R.id.follow)
    SettingSwitchInfo mFollow;
    @InjectView(R.id.reply)
    SettingSwitchInfo mReply;
    @InjectView(R.id.message)
    SettingSwitchInfo mMessage;
    @InjectView(R.id.gift)
    SettingSwitchInfo mGift;
    private MsgSettingEntity mEntity;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting_msg;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mEntity = getIntent().getParcelableExtra(Constance.KEY.PARCELABLE_ENTITY);
        mToolbar.setTitle("消息设置");
        initWidget();
    }

    private void initWidget() {
        mUpdate.setSwitchState(mEntity.getUpdate().isOpen());
        mSystem.setSwitchState(mEntity.getSystem().isOpen());
        mNotice.setSwitchState(mEntity.getNotice().isOpen());
        mFollow.setSwitchState(mEntity.getFollow().isOpen());
        mReply.setSwitchState(mEntity.getReply().isOpen());
        mMessage.setSwitchState(mEntity.getMessage().isOpen());
        mGift.setSwitchState(mEntity.getGift().isOpen());
    }

    @Override
    public void finish() {
        mEntity.getUpdate().setOpen(mUpdate.getSwitchState());
        mEntity.getSystem().setOpen(mSystem.getSwitchState());
        mEntity.getNotice().setOpen(mNotice.getSwitchState());
        mEntity.getFollow().setOpen(mFollow.getSwitchState());
        mEntity.getReply().setOpen(mReply.getSwitchState());
        mEntity.getMessage().setOpen(mMessage.getSwitchState());
        mEntity.getGift().setOpen(mGift.getSwitchState());
        getIntent().putExtra(Constance.KEY.PARCELABLE_ENTITY, mEntity);
        setResult(110, getIntent());
        super.finish();
    }
}

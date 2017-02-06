package com.touchrom.gaoshouyou.popupwindow;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arialyy.frame.util.SharePreUtil;
import com.lyy.ui.group.TagFlowLayout;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BasePopupWindow;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.entity.FilterPWEntity;
import com.touchrom.gaoshouyou.entity.GameClassifyScreenEntity;
import com.touchrom.gaoshouyou.help.RippleHelp;

import java.util.List;

import butterknife.InjectView;

//import com.lyy.ui.group.TagFlowLayout2;

/**
 * Created by lyy on 2015/12/11.
 * 游戏分类中筛选悬浮框
 */
public class GameClassifyFilterPopupWindow extends BasePopupWindow implements View.OnClickListener
        , TagFlowLayout.OnChildClickListener {
    @InjectView(R.id.tag_1)
    TextView mTag1;
    @InjectView(R.id.tag_flow_1)
    TagFlowLayout mTagFlow1;
    @InjectView(R.id.tag_2)
    TextView mTag2;
    @InjectView(R.id.tag_flow_2)
    TagFlowLayout mTagFlow2;
    @InjectView(R.id.tag_3)
    TextView mTag3;
    @InjectView(R.id.tag_flow_3)
    TagFlowLayout mTagFlow3;
    @InjectView(R.id.reset)
    Button mReset;
    @InjectView(R.id.complete)
    Button mComplete;
    private List<GameClassifyScreenEntity> mData;
    private int mFilterId1 = 0, mFilterId2 = 0, mFilterId3 = 0;
    private int mPosition1 = 0, mPosition2 = 0, mPosition3 = 0;
    private int mClassifyId;

    public GameClassifyFilterPopupWindow(Context context, Object object, int classsifyId, List<GameClassifyScreenEntity> data) {
        super(context, null, object);
        mData = data;
        mClassifyId = classsifyId;
        initWidget();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.popupwindow_game_classify_filter;
    }

    private void initWidget() {
        RippleHelp.createRipple(getContext(), mReset);
        RippleHelp.createRipple(getContext(), mComplete);
        mReset.setOnClickListener(this);
        mComplete.setOnClickListener(this);

        mTagFlow1.setOnChildClickListener(this);
        mTagFlow2.setOnChildClickListener(this);
        mTagFlow3.setOnChildClickListener(this);
        if (mData.size() == 0) {
            return;
        }

        FilterPWEntity record = loadRecordEntity();

        mTag1.setText(mData.get(0).getTitle());
        mTagFlow1.setTags(getTags(mData.get(0).getChilds()));
        mFilterId1 = record.getFilterId1();
        mTagFlow1.setRadio(record.getPosition1());

        mTag2.setText(mData.get(1).getTitle());
        mTagFlow2.setTags(getTags(mData.get(1).getChilds()));
        mFilterId2 = record.getFilterId2();
        mTagFlow2.setRadio(record.getPosition2());

        mTag3.setText(mData.get(2).getTitle());
        mTagFlow3.setTags(getTags(mData.get(2).getChilds()));
        mFilterId3 = record.getFilterId3();
        mTagFlow3.setRadio(record.getPosition3());

        int drawable = R.drawable.skin_selector_filter_tag_bg;
        int textDrawable = R.drawable.skin_selector_filter_tag_text_bg;
        mTagFlow1.setDrawable(drawable);
        mTagFlow2.setDrawable(drawable);
        mTagFlow3.setDrawable(drawable);
        mTagFlow1.setTextSelector(textDrawable);
        mTagFlow2.setTextSelector(textDrawable);
        mTagFlow3.setTextSelector(textDrawable);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
    }

    /**
     * 提取tag
     *
     * @param entitys
     * @return
     */
    private String[] getTags(List<GameClassifyScreenEntity.ScreenTagChildEntity> entitys) {
        String[] tags = new String[entitys.size()];
        for (int i = 0, count = tags.length; i < count; i++) {
            tags[i] = entitys.get(i).getTagName();
        }
        return tags;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reset) {
            mTagFlow1.setRadio(0);
            mTagFlow2.setRadio(0);
            mTagFlow3.setRadio(0);
            mFilterId1 = 0;
            mFilterId2 = 0;
            mFilterId3 = 0;
            mPosition1 = 0;
            mPosition2 = 0;
            mPosition3 = 0;
        } else if (v.getId() == R.id.complete) {
            Bundle b = new Bundle();
            b.putInt("screenId1", mFilterId1);
            b.putInt("screenId2", mFilterId2);
            b.putInt("screenId3", mFilterId3);
            saveRecordEntity();
            getSimplerModule().onDialog(ResultCode.GAME_CLASSIFY_SCREEN, b);
            dismiss();
        }
    }

    /**
     * 保存筛选记录实体
     *
     * @return
     */
    private void saveRecordEntity() {
        FilterPWEntity record = new FilterPWEntity();
        record.setClassifyId(mClassifyId);
        record.setFilterId1(mFilterId1);
        record.setFilterId2(mFilterId2);
        record.setFilterId3(mFilterId3);
        record.setPosition1(mPosition1);
        record.setPosition2(mPosition2);
        record.setPosition3(mPosition3);
        SharePreUtil.putObject(Constance.APP.NAME, getContext(), "FILTER_RECORD", FilterPWEntity.class, record);
    }

    /**
     * 读取筛选记录实体
     */
    private FilterPWEntity loadRecordEntity() {
        FilterPWEntity record = SharePreUtil.getObject(Constance.APP.NAME, getContext(), "FILTER_RECORD", FilterPWEntity.class);
        return record == null ? new FilterPWEntity() : record;
    }


    @Override
    public void onChildClick(TagFlowLayout parent, TextView child, int position) {
        parent.setRadio(position);
        int parentId = parent.getId();
        if (parentId == R.id.tag_flow_1) {
            mPosition1 = position;
            mFilterId1 = mData.get(0).getChilds().get(position).getTagId();
        } else if (parentId == R.id.tag_flow_2) {
            mPosition2 = position;
            mFilterId2 = mData.get(1).getChilds().get(position).getTagId();
        } else if (parentId == R.id.tag_flow_3) {
            mPosition3 = position;
            mFilterId3 = mData.get(2).getChilds().get(position).getTagId();
        }
    }
}

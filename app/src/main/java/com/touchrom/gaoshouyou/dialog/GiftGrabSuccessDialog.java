package com.touchrom.gaoshouyou.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arialyy.frame.core.AbsDialog;
import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.T;
import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.lyy.ui.widget.NoScrollListView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.user.GiftManagerActivity;
import com.touchrom.gaoshouyou.entity.GiftEntity;
import com.touchrom.gaoshouyou.help.RippleHelp;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/17.
 * 抢号成功
 */
public class GiftGrabSuccessDialog extends AbsDialog implements View.OnClickListener {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.cancel)
    Button mCancel;
    @InjectView(R.id.enter)
    Button mEnter;
    @InjectView(R.id.list)
    NoScrollListView mList;
    List<String> mCodes = new ArrayList<>();
    int mType;
    int mGiftId;
    ServiceUtil mUtil;
    SimpleAdapter<String> mAdapter;

    /**
     * @param context
     * @param codes
     * @param type    {@link com.touchrom.gaoshouyou.entity.GiftEntity#AMOY}
     */
    public GiftGrabSuccessDialog(Context context, List<String> codes, int type, int giftId) {
        super(context);
        mCodes = codes;
        mType = type;
        mGiftId = giftId;
        init();
    }

    private void init() {
        String title = "";
        String btStr = "";
        switch (mType) {
            case GiftEntity.GRAB:
                title = "哎哟不错呦，抢号成功！";
                btStr = "礼包管理";
                break;
            case GiftEntity.AMOY:
                title = "运气不错，淘到啦！";
                btStr = "换一批";
                break;
            case GiftEntity.RESERVATIONS:
                title = "先到先得，预定成功！";
                btStr = "礼包管理";
                break;
        }
        mTitle.setText(title);
        mEnter.setText(btStr);
        mEnter.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        RippleHelp.createRipple(getContext(), mEnter);
        RippleHelp.createRipple(getContext(), mCancel);
        initList();
    }

    private void initList() {
        mAdapter = new SimpleAdapter<String>(getContext(), mCodes, R.layout.item_gift_grab_success) {
            @Override
            public void convert(SimpleViewHolder helper, String item) {
                if (mType == GiftEntity.RESERVATIONS) {
                    String str = "领号时间：" + item;
                    helper.setText(R.id.code, StringUtil.highLightStr(str, item, Color.parseColor("#FF7537")));
                    helper.setText(R.id.hint, "预定成功后，将拥有提前30分钟抢号的特权");
                    helper.getView(R.id.bt).setVisibility(View.GONE);
                } else {
                    String str = "激活码：" + item;
                    Button bt = helper.getView(R.id.bt);
                    helper.setText(R.id.code, StringUtil.highLightStr(str, item, Color.parseColor("#FF7537")));
                    helper.setText(R.id.hint, mType == GiftEntity.GRAB ? "淘到的号码不一定能使用，建议多试几次" :
                            "已存入“我的-礼包”中，可随时查看");
                    bt.setVisibility(View.VISIBLE);
                    BtListener listener = (BtListener) bt.getTag(R.id.bt);
                    if (listener == null) {
                        listener = new BtListener(item);
                        bt.setTag(R.id.bt, listener);
                    }
                    bt.setOnClickListener(listener);
                }
            }

            class BtListener implements View.OnClickListener {
                String code;

                public BtListener(String code) {
                    this.code = code;
                }

                @Override
                public void onClick(View v) {
                    copyText("礼包激活码", code);
                }

                /**
                 * 复制文字到剪切板
                 */
                private void copyText(CharSequence label, CharSequence text) {
                    ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setPrimaryClip(ClipData.newPlainText(label, text));
                    T.showShort(getContext(), "礼包激活码已复制到剪切板");
                }
            }

        };
        mList.setAdapter(mAdapter);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_grab_success;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
        } else {
            if (mType == GiftEntity.AMOY) {
                updateCodes();
            } else {
                getContext().startActivity(new Intent(getContext(), GiftManagerActivity.class));
                dismiss();
            }
        }
    }

    public void updateCodes() {
        mUtil = ServiceUtil.getInstance(getContext());
        Map<String, String> params = new HashMap<>();
        params.put("libaoId", mGiftId + "");
        params.put("typeId", 2 + "");
        mUtil.getGiftCode(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mUtil.isRequestSuccess(obj)) {
                        JSONObject entity = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        T.showShort(getContext(), entity.getString("message"));
                        JSONArray array = entity.getJSONArray("th");
                        if (array.length() > 0) {
                            mCodes.clear();
                            for (int i = 0, count = array.length(); i < count; i++) {
                                mCodes.add(array.getString(i));
                            }
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {
                        T.showShort(getContext(), mUtil.getMsg(obj) + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

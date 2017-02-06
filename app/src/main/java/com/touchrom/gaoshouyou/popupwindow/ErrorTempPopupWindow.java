package com.touchrom.gaoshouyou.popupwindow;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BasePopupWindow;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/12/23.
 * 错误填充窗口
 */
public class ErrorTempPopupWindow extends BasePopupWindow {
    @InjectView(R.id.img)
    ImageView mImg;
    @InjectView(R.id.text)
    TextView mText;
    @InjectView(R.id.bt)
    Button mBt;
    private int mType;

    /**
     * @param type {@link com.touchrom.gaoshouyou.net.ServiceUtil#ERROR}
     *             {@link com.touchrom.gaoshouyou.net.ServiceUtil#NULL}
     */
    public ErrorTempPopupWindow(Context context, Object obj, int type) {
        super(context, null, obj);
        initWidget(type);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_error_temp;
    }


    private void initWidget(int type) {
        mType = type;
        setOutsideTouchable(false);
        int drawable;
        String text;
        String btText;
        if (mType == ServiceUtil.ERROR) {
            drawable = R.mipmap.icon_error;
            text = "网络错误";
            btText = "重新加载";
        } else {
            drawable = R.mipmap.icon_empty;
            text = "什么都没有";
            btText = "看看别的";
        }
        mImg.setImageDrawable(getContext().getResources().getDrawable(drawable));
        mText.setText(text);
        mBt.setText(btText);
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSimplerModule().onDialog(ResultCode.DIALOG.ERROR_TEMP_DIALOG, mType);
            }
        });
    }

}

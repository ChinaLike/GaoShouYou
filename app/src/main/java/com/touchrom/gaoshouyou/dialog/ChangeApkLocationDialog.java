package com.touchrom.gaoshouyou.dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StatFs;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.FileUtil;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseDialog;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.DialogChangeApkLocationBinding;
import com.touchrom.gaoshouyou.help.RippleHelp;
import com.touchrom.gaoshouyou.util.ExternalStorage;

import java.io.File;
import java.util.Map;

import butterknife.InjectView;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lyy on 2015/12/1.
 * 下载位置选择对话框
 */
@SuppressLint("ValidFragment")
public class ChangeApkLocationDialog extends BaseDialog<DialogChangeApkLocationBinding> implements View.OnClickListener {
    @InjectView(R.id.enter)
    Button mEnter;
    @InjectView(R.id.cancel)
    Button mCancel;
    @InjectView(R.id.bi)
    RelativeLayout mBi;
    @InjectView(R.id.bo)
    RelativeLayout mBo;
    @InjectView(R.id.bi_rb)
    RadioButton mBiRb;
    @InjectView(R.id.bo_rb)
    RadioButton mBoRb;
    private String mLocation;
    private int mType = 0;

    public ChangeApkLocationDialog(Object obj, int type, String location) {
        super(obj);
        mType = type;
        mLocation = TextUtils.isEmpty(location) ? Constance.PATH.DEFAULT_DOWNLOAD_LOCATION : location;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_change_apk_location;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initWidget();
        handleRb(mType == 0);
        getBinding().setBiPath(Constance.PATH.DEFAULT_DOWNLOAD_LOCATION);   //内置存储路径
        Map<String, File> externalLocations = ExternalStorage.getAllStorageLocations();
        File sdCard = externalLocations.get(ExternalStorage.SD_CARD);   //内置SD卡，就是内置存储
        File externalSdCard = externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD);  //外置Sdcard
        if (externalSdCard != null) {
            String boPath = externalSdCard.getPath() + "/gaoShouYou/download/";
            getBinding().setBoPath(boPath);
        }
        getBinding().setBiSpace("剩余空间：正在计算..");
        getBinding().setBoSpace("剩余空间：正在计算..");

        initSpace(0, sdCard);
        initSpace(1, externalSdCard);
    }

    /**
     * @param type 0 --> 内置存储， 1 --> 外置内存卡
     * @param file
     */
    private void initSpace(final int type, File file) {
        if (file == null) {
            return;
        }
        Observable.just(file)
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<File, String>() {
                    @Override
                    public String call(File file) {
                        StatFs stat = new StatFs(file.getPath());
                        long blockSize = stat.getBlockSize();
                        long availableBlocks = stat.getAvailableBlocks();
                        return FileUtil.formatFileSize(blockSize * availableBlocks);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        String str = "剩余空间：" + s;
                        if (type == 0) {
                            getBinding().setBiSpace(str);
                        } else if (type == 1) {
                            getBinding().setBoSpace(str);
                        }
                    }
                });
    }

    private void initWidget() {
        if (AndroidVersionUtil.hasIcecreamsandwich()) {
            mEnter.setBackgroundColor(Color.TRANSPARENT);
            mCancel.setBackgroundColor(Color.TRANSPARENT);
            mBi.setBackgroundColor(Color.TRANSPARENT);
            mBo.setBackgroundColor(Color.TRANSPARENT);
            RippleHelp.createRipple(getContext(), mEnter);
            RippleHelp.createRipple(getContext(), mCancel);
            RippleHelp.createRipple(getContext(), mBi);
            RippleHelp.createRipple(getContext(), mBo);
        } else {
            mEnter.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.selector_setting_widget));
            mCancel.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.selector_setting_widget));
            mBi.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.selector_setting_widget));
            mBo.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.selector_setting_widget));
        }
        mEnter.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mBiRb.setChecked(true);
        mBi.setOnClickListener(this);
        mBo.setOnClickListener(this);
    }

    private void handleRb(boolean check) {
        mBiRb.setChecked(check);
        mBoRb.setChecked(!check);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter:
                Object[] data = new Object[]{
                        mType,
                        mLocation
                };
                getSimplerModule().onDialog(ResultCode.CHANGE_APK_LOCATION, data);
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.bi:
                handleRb(true);
                mLocation = getBinding().getBiPath();
                mType = 0;
                break;
            case R.id.bo:
                handleRb(false);
                mLocation = getBinding().getBoPath();
                mType = 1;
                break;
        }
    }
}

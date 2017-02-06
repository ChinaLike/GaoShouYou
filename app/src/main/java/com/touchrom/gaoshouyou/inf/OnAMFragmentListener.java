package com.touchrom.gaoshouyou.inf;

import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.entity.sql.ApkInfoEntity;
import com.touchrom.gaoshouyou.fragment.AMApkFragment;

import java.util.Set;

/**
 * Created by lyy on 2015/12/22.
 * 应用管理Fragment接口
 */
public interface OnAMFragmentListener {
    /**
     * item数量
     *
     * @param type     {@link AMApkFragment#ALL_APK}
     *                 {@link AMApkFragment#INSTALLED_APK}
     *                 {@link AMApkFragment#UPDATE_APK}
     * @param fragment
     * @param num      每一个Fragment的每个item的数量，用于更新tab的信息
     */
    public void onNum(BaseFragment fragment, int type, int num);

    /**
     * 获取选择的item数据
     *
     * @param type      {@link AMApkFragment#ALL_APK}
     *                  {@link AMApkFragment#INSTALLED_APK}
     *                  {@link AMApkFragment#UPDATE_APK}
     * @param fragment
     * @param checkNum  选中的数量
     * @param checkData 选中的数据
     */
    public void onCheckNum(BaseFragment fragment, int type, int checkNum, Set<ApkInfoEntity> checkData);

    /**
     * 按钮操作
     *
     * @param type   {@link OnAMAdapterListener#HANDLER_INSTALL}
     *               {@link OnAMAdapterListener#HANDLER_UN_INSTALL}
     *               {@link OnAMAdapterListener#HANDLER_UPDATE}
     *               {@link OnAMAdapterListener#HANDLER_OPEN}
     * @param entity 操作实体
     */
    public void onButtonHandle(BaseFragment fragment, int type, int handleType, ApkInfoEntity entity);


}

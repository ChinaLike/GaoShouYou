package com.touchrom.gaoshouyou.inf;

import com.touchrom.gaoshouyou.entity.sql.ApkInfoEntity;
import com.touchrom.gaoshouyou.fragment.AMApkFragment;

import java.util.Set;

/**
 * Created by lk on 2015/12/22.
 * 应用管理适配器被选中监听
 */
public interface OnAMAdapterListener {
    public static final int HANDLER_INSTALL = 0x01; //安装操作
    public static final int HANDLER_UN_INSTALL = 0x02;  //卸载操作
    public static final int HANDLER_UPDATE = 0x03;  //更新操作
    public static final int HANDLER_OPEN = 0x04;    //打开操作

    /**
     * @param type      {@link AMApkFragment#ALL_APK}
     *                  {@link AMApkFragment#INSTALLED_APK}
     *                  {@link AMApkFragment#UPDATE_APK}
     * @param num       选中的数量
     * @param checkData 选中的数据
     */
    public void onCheck(int type, int num, Set<ApkInfoEntity> checkData);

    /**
     * adapter 的item数量发生改变时，进行通知
     *
     * @param type
     */
    public void onItemNumChange(int type, int num);

    /**
     * 按钮操作
     *
     * @param type   {@link #HANDLER_INSTALL}
     *               {@link #HANDLER_UN_INSTALL}
     *               {@link #HANDLER_UPDATE}
     *               {@link #HANDLER_OPEN}
     * @param entity 操作实体
     */
    public void onButtonHandle(int type, int handleType, ApkInfoEntity entity);

}

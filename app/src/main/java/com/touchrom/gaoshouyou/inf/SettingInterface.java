package com.touchrom.gaoshouyou.inf;

import com.touchrom.gaoshouyou.entity.UserEntity;

/**
 * Created by lyy on 2015/11/6.
 */
public interface SettingInterface {
    int WIFI_NET = 0x01;
    int MOBILE_NET = 0x02;
    int LIGHT_STYLE = 0x01;
    int NIGHT_STYLE = 0x02;

    /**
     * 网络设置
     *
     * @return
     */
    void netSetting(int net);

    /**
     * 主题色彩设置
     *
     * @return
     */
    void themeColorSetting(int color);

    /**
     * 主题设置
     *
     * @return
     */
    void themeStyleSetting(int style);

    /**
     * 获取用户
     *
     * @return
     */
    UserEntity getUser();
}

package com.touchrom.gaoshouyou.config;

import android.os.Environment;

/**
 * Created by lyy on 2015/11/9.
 * 配置文件
 */
public class Constance {

    public static class APP {
        public static final String NAME = "GAO_SHOU_YOU";
    }

    public static class NOTIFY_KEY{
        public static final int USER_FRAGMENT = 0xaa0;
        public static final int FANS = 0xaa1;
    }

    public static class KEY {
        public static final String USER = "USER";
        public static final String CURRENT_COLOR = "CURRENT_COLOR";
        public static final String STATE_BAR_COLOR = "STATE_BAR_COLOR";
        public static final String LOGIN_STATE = "LOGIN_STATE";
        public static final String THEME = "THEME";
        public static final String LAUNCHER_ENTITY = "LAUNCHER_ENTITY";
        public static final String PARCELABLE_ENTITY = "PARCELABLE_ENTITY";
        public static final String SETTING = "SETTING";
        public static final String FIND_APK_TIME = "FIND_APK_TIME";
        public static final String ACCOUNT = "ACCOUNT";
        public static final String INT = "INT";
        public static final String STRING = "STRING";
        public static final String LIST = "LIST";
        public static final String APP_ID = "APP_ID";
        public static final String TURN = "TURN";
        public static final String USER_ID = "USER_ID";
    }

    public static class PATH {
        public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
        public static final String BASE_DIR = Environment.getExternalStorageDirectory().getPath() + "/gaoShouYou/";
        public static final String LAUNCHER_IMG_PATH = BASE_DIR + "launcher/launcher.dat";  //启动图
        public static final String NEW_APK_PATH = BASE_DIR + "update/gaoShouYou.apk";       //版本升级apk
        public static final String GUIDE_SETTING_TEMP_PATH = "/guideTemp.dat";  //引导版本的差异文件
        public static final String DEFAULT_DOWNLOAD_LOCATION = BASE_DIR + "download/";  //apk下载位置
        public static final String TEMP_HEAD_IMG_PATH = BASE_DIR + "temp/t_hi.jpg";  //头像上传缓存文件
    }

    public static class NOTIFICATION {
        public static final int VERSION_APK_ID = 0x01; //版本升级APK
        public static final int VERSION_NORMAL_MSG_ID = 0x02; //版本升级APK
    }

    public static class URL {
        public static final String GSY_URL = "http://m.gaoshouyou.com";       //官网地址
        public static final String HELP_URL = "http://app.gaoshouyou.com/webforapp/help.html";       //帮助
        public static final String WEIBO_URL = "http://weibo.com/gaoshouyou";     //微博链接
        public static final String USE_SERVER_AGREEMENT_URL = "http://app.gaoshouyou.com/webforapp/agreement.html";  //用户协议
        public static final String DUTY_RULE_URL = "http://app.gaoshouyou.com/webforapp/disclaimer.html";     //免责声明
        public static final String BUSINESS_URL = "http://app.gaoshouyou.com/webforapp/bussiness.html";     //商务合作
        public static final String FORGET_PASSWORD = "http://app.gaoshouyou.com/webforapp/bussiness.html";     //忘记密码
        public static final String REG_AGREEMENT = "http://app.gaoshouyou.com/webforapp/bussiness.html";     //注册协议
        public static final String LEV_RULE = "http://app.gaoshouyou.com/webforapp/bussiness.html";     //等级规则
        public static final String USER_SETTING = "http://app.gaoshouyou.com/webforapp/bussiness.html";     //用户设置
        public static final String SIGN_RULE = "http://app.gaoshouyou.com/webforapp/bussiness.html";     //签到规则
    }

    public static class SERVICE {
        public static final String DOWNLOAD_ENTITY = "download_data";       //下载数据
        public static final String GSY_SERVICE_ENTITY = "gsy_service_entity";       //下载数据
        public static final String DOWNLOAD_CMD = "download_cmd";       //下载命令
        public static final String DOWNLOAD_SEND_ENTITY = "download_entity";       //下载服务发送实体
    }

}

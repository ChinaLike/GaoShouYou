package com.touchrom.gaoshouyou.config;

/**
 * Created by lk on 2015/11/9.
 * 方法返回码
 */
public class ResultCode {

    public static final int BRIGHT_SETTING = 0x01;   //亮度调节
    public static final int LAUNCHER_FLOWS = 0x02;   //启动页启动流程
    public static final int CHANGE_APK_LOCATION = 0x03;   //APK下载位置
    public static final int CHECK_NEW_VERSION = 0x04;   //检查新版本
    public static final int GAME_CLASSIFY_TYPE = 0x05;   //游戏根据类型id分类
    public static final int GAME_CLASSIFY_SCREEN = 0x06;   //游戏根据筛选id分类
    public static final int GET_ALL_APK_PATH = 0x07;   //文件系统所有的安装包路径

    //ACTIVITY请求码
    public class ACTIVITY {
        public static final int GD_COMMENT = 0xcc0; //游戏详情bar请求码
    }

    //对话框相关
    public class DIALOG {
        public static final int MSG_DIALOG_ENTER = 0xaa1;         //提示对话框确认
        public static final int MSG_DIALOG_CANCEL = 0xaa2;         //提示对话框确认
        public static final int AM_ITEM_FUNCTION = 0xaa3;         //应该管理点击弹出对话框
        public static final int UPGRADE = 0xaa4;         //升级对话框的
        public static final int GUIDE = 0xaa5;         //欢迎页面
        public static final int DOWNLOAD = 0xaa6;         //下载对话框
        public static final int IMG_CHOOSE = 0xaa7;         //图片旋转方式对话框
        public static final int ERROR_TEMP_DIALOG = 0xaa8;         //错误填充对话框
    }


    //后台接口相关
    public class SERVER {
        public static final int GET_GAME_CLASSIFY = 0xbb1;         //获取游戏分类
        public static final int GET_GAME_CLASSIFY_INFO = 0xbb2;         //获取分类游戏
        public static final int GET_GAME_CLASSIFY_TAG = 0xbb3;         //分类悬浮框tag标签
        public static final int GET_MAIN_GAME_BANNER = 0xbb4;         //首页游戏Banner
        public static final int GET_MAIN_GAME_RECOMMEND = 0xbb5;         //首页推荐数据
        public static final int GET_GAME_DETAIL_DATA = 0xbb6;         //获取游戏详情页数据
        public static final int GET_GAME_RANK_CLASSIFY = 0xbb7;         //获取游戏排行分类
        public static final int GET_GAME_RANK_DATA = 0xbb8;         //获取游戏排行数据
        public static final int GET_GAME_DETAIL_INFO = 0xbb9;         //游戏详情--详情
        public static final int GET_SHARE_DATA = 0xbba;         //获取分享数据
        public static final int GET_SEARCH_HOT_WORD = 0xbbb;         //获取搜索热词
        public static final int GET_SEARCH_DATA = 0xbbc;         //获取搜索数据
        public static final int FEEDBACK = 0xbbd;         //意见反馈
        public static final int GET_GAME_TOPIC = 0xbbe;         //游戏专题
        public static final int GET_GAME_TOPIC_CLASSIFY = 0xbbf;         //游戏专题
        public static final int GET_GAME_TOPIC_CLASSIFY_DETAIL = 0xbc0;         //游戏专题详情
        public static final int LOGIN = 0xbc1;         //登录
        public static final int REG = 0xbc2;         //注册
        public static final int GET_GAME_FACTORY_LIST = 0xbc3;         //游戏厂商列表
        public static final int GET_GAME_FACTORY = 0xbc4;         //游戏厂商数据
        public static final int GET_RAIDERS_TAG = 0xbc5;         //资讯-攻略 分类标签
        public static final int GET_NEWS_ARTICLE_LIST = 0xbc6;         //资讯文章列表
        public static final int GET_ARTICLE_CONTENT = 0xbc7;         //文章内容
        public static final int GET_GAME_DETAIL_RAIDER = 0xbc8;         //游戏详情攻略
        public static final int GET_GAME_DETAIL_GIFT = 0xbc9;         //游戏详情礼包
        public static final int GET_GAME_DETAIL_Tab = 0xbca;         //游戏详情栏目
        public static final int GET_GIFT_DETAIL = 0xbcb;         //游戏礼包详情
        public static final int GET_COMMENT = 0xbcc;         //获取评论
        public static final int GET_COMMENT_DETAIL = 0xbcd;         //获取评论详情
        public static final int SEND_COMMENT = 0xbce;         //发送评论
        public static final int SEND_REPLY = 0xbcf;         //发送回复
        public static final int COLLECT = 0xbd0;         //收藏
        public static final int ME_COMMENT = 0xbd1;         //我的回复
        public static final int RE_COMMENT = 0xbd2;         //回复我的
        public static final int GET_COLLECT_STATE = 0xbd3;         //获取收藏状态
        public static final int ADD_COLLECT = 0xbd4;         //添加收藏
        public static final int CANCEL_COLLECT = 0xbd5;         //取消收藏
        public static final int COMMENT_LIKE = 0xbd6;         //评论点赞
        public static final int GET_FANS_LIST = 0xbd7;         //评论点赞
        public static final int GET_DYNAMIC_LIST = 0xbd8;         //获取动态列表
        public static final int GET_MY_GIFT_LIST = 0xbd9;         //我的礼包
        public static final int GET_MY_HONOR_LEV = 0xbda;         //高手等级
        public static final int GET_MY_HONOR_DESIGN = 0xbdb;         //高手称号
        public static final int GET_MSG_LIST = 0xbdc;         //消息
        public static final int GET_MAIN_GAME_DATA = 0xbdd;         //首页游戏数据
        public static final int GET_USER_CENTER_DATA = 0xbde;         //首页游戏数据
        public static final int GET_USER_LEAVE_MSG = 0xbdf;         //用户留言
        public static final int FOLLOW = 0xbe0;         //关注
        public static final int CANCEL_FOLLOW = 0xbe1;         //取消关注
        public static final int SEND_LEAVE_MSG = 0xbe2;         //发送留言
        public static final int SIGN_IN = 0xbe3;         //签到
        public static final int GET_FIND_GIFT_LIST = 0xbe4;         //发现礼包列表
        public static final int GET_TOPIC_DETAIL_CONTENT = 0xbe5;   //专题详情
        public static final int GET_TOPIC_ALL_ART = 0xbe5;   //专题所有文章
    }

}

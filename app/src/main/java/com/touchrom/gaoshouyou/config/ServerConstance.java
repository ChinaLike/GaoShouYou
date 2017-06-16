package com.touchrom.gaoshouyou.config;

/**
 * Created by lk on 2015/11/12.
 * 网络接口
 */
public class ServerConstance {

    public static final String BASE_URL = "http://www.gaoshouyou.com/api_v1/";      //接入接口
    public static final String TEST_BASE_URL = "http://www.9sw.wang/index.php/api_v2/"; //测试接口

    /**
     * 获取下载信息
     */
    public static final String GET_GAME_DOWNLOAD_INFO = "game/down_detail";

    /**
     * 专题文章列表
     */
    public static final String GET_TOPIC_ART_LISTS = "topic/lm_list";

    /**
     * 获取专题内容
     */
    public static final String GET_TOPIC_DETAIL_CONTENT = "topic/zt_info";

    /**
     * 上传头像
     */
    public static final String UPLOAD_HEAD_IMG = "user/file_upload";

    /**
     * 推送绑定
     */
    public static final String PUSH_BIND = "ajax/bind";

    /**
     * 开服开测
     */
    public static final String OPEN_SERVER_LIST = "find/kaiFu";

    /**
     * 发现-礼包
     */
    public static final String FIND_GIFT_LIST = "find/lb";

    /**
     * 签到
     */
    public static final String SIGN_IN = "user/sign";

    /**
     * 发表留言
     */
    public static final String SEND_LEAVE_MSG = "user/publish_ly";

    /**
     * 留言
     */
    public static final String GET_LEAVE_MSG_LIST = "user/message";

    /**
     * 用户中心
     */
    public static final String GET_USER_CENTER_DATA = "user/homepage";

    /**
     * 消息中心
     */
    public static final String GET_MSG_LIST = "user/min_xiaoxi";

    /**
     * 高手等级
     */
    public static final String HONOR_LEV = "user/honor_grade";

    /**
     * 高手称号
     */
    public static final String HONOR_TAG = "user/honor_title";

    /**
     * 通知设置
     */
    public static final String NOTIFY_SETTING = "user/xiaoxi_setup";

    /**
     * 消息读取状态
     */
    public static final String MSG_READ_STATE = "user/duXiaoxi";

    /**
     * 礼包
     */
    public static final String GET_GIFT_LIST = "user/min_liBao";

    /**
     * 动态
     */
    public static final String GET_DYNAMIC_LIST = "user/dongTai";

    /**
     * 取消关注
     */
    public static final String CANCEL_FOLLOW = "user/deleteFollow";

    /**
     * 关注
     */
    public static final String FOLLOW = "user/addFollow";

    /**
     * 获取粉丝、关注列表
     */
    public static final String GET_FANS_LIST = "user/followFans";

    /**
     * 抢号
     */
    public static final String GIFT_CODE = "game/min_lb";

    /**
     * 取消收藏
     */
    public static final String CANCEL_COLLECT = "user/delete";

    /**
     * 添加收藏
     */
    public static final String ADD_COLLECT = "user/addCollection";

    /**
     * 收藏状态
     */
    public static final String GET_COLLECT_STATE = "user/zhuangTai";

    /**
     * 我的评论
     */
    public static final String ME_COMMENT = "user/comment";

    /**
     * 回复我的
     */
    public static final String RE_COMMENT = "user/replyme";

    /**
     * 收藏
     */
    public static final String COLLECT_LIST = "user/shouCang";

    /**
     * 评论点赞
     */
    public static final String REPLY_PRAISE = "comment/praise";

    /**
     * 发表回复
     */
    public static final String SEND_REPLY = "comment/reply";

    /**
     * 发表评论
     */
    public static final String SEND_COMMENT = "comment/form";

    /**
     * 评论详情
     */
    public static final String GET_COMMENT_DETAIL = "comment/detail";

    /**
     * 评论总数
     */
    public static final String GET_COMMENT_TOTAL = "comment/total";

    /**
     * 游戏评论
     */
    public static final String GET_COMMENT = "comment/index";

    /**
     * 礼包详情
     */
    public static final String GET_GIFT_DETAIL = "game/liBaoInfo";

    /**
     * 游戏详情-栏目
     */
    public static final String GET_GAME_DETAIL_TAB = "game/fenLei";

    /**
     * 游戏详情-礼包
     */
    public static final String GET_GAME_DETAIL_GIFT = "game/liBao";

    /**
     * 游戏详情-攻略
     */
    public static final String GET_GAME_DETAIL_RAIDERS = "game/gameArt";

    /**
     * 文章内容
     */
    public static final String GET_ARTICLE_CONTENT = "article/info";

    /**
     * 资讯文章列表
     */
    public static final String GET_NEWS_ARTICLE_LIST = "article/index";

    /**
     * 获取攻略分类标签
     */
    public static final String GET_RAIDERS_TAGS = "article/type";

    /**
     * 获取游戏厂商数据
     */
    public static final String GET_GAME_FACTORY = "topic/gameLists";

    /**
     * 获取游戏厂商列表数据
     */
    public static final String GET_GAME_FACTORY_LIST = "topic/gameMakers";

    /**
     * 获取验证码
     */
    public static final String GET_CODE = "ajax/sms";

    /**
     * 注册
     */
    public static final String REG = "user/register";

    /**
     * 登录
     */
    public static final String LOGIN = "user/login";

    /**
     * 专题分类详情
     */
    public static final String GAME_TOPIC_CLASSIFY_DETAIL = "topic/lists";

    /**
     * 专题分类
     */
    public static final String GAME_TOPIC_CLASSIFY = "topic/classify";

    /**
     * 专题
     */
    public static final String GAME_TOPIC = "topic/index";

    /**
     * 下载计数
     */
    public static final String DOWNLOAD_COUNT = "game/down";

    /**
     * 意见反馈
     */
    public static final String FEEDBACK = "welcome/feedback";

    /**
     * 搜索--搜索数据
     */
    public static final String SEARCH_DATA = "search/game";

    /**
     * 搜索--搜索热词
     */
    public static final String SEARCH_HOT_WORD = "search/word";

    /**
     * 分享数据
     */
    public static final String SHARE_DATA = "welcome/share";

    /**
     * 游戏详情--详情
     */
    public static final String GAME_DETAIL_INFO = "game/info";

    /**
     * 游戏排行
     */
    public static final String GAME_RANK = "game/top";

    /**
     * 游戏排行分类
     */
    public static final String GAME_RANK_CLASSIFY = "game/top/style";

    /**
     * 新版本检测
     */
    public static final String CHECK_NEW_VERSION = "welcome/checkUpdate";

    /**
     * 游戏详情
     */
    public static final String GAME_DETAIL = "game/detail";

    /**
     * 游戏推荐
     */
    public static final String GAME_RECOMMEND = "game/today";

    /**
     * 游戏banner
     */
    public static final String GAME_BANNER = "game/ads";

    /**
     * 游戏精选
     */
    public static final String GAME_SELECTED = "game/home";

    /**
     * 启动图
     */
    public static final String LAUNCHER_PARAMS = "welcome/index";

    /**
     * 获取游戏更新信息
     */
    public static final String GAME_UPDATE = "game/update";

    /**
     * 游戏分类
     */
    public static final String CLASSIFY = "game/classify/index";

    /**
     * 分类游戏
     */
    public static final String CLASSIFY_GAME = "game/classify/item";

    /**
     * 分类下的游戏筛选模式 ：类型
     */
    public static final String CLASSIFY_TYPE = "game/classify/type";

    /**
     * 分类下的游戏筛选模式 ：筛选
     */
    public static final String CLASSIFY_SCREEN = "game/classify/screen";

}

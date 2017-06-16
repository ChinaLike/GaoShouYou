package com.touchrom.gaoshouyou.net;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.AndroidUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.config.ServerConstance;
import com.touchrom.gaoshouyou.entity.UserEntity;
import com.touchrom.gaoshouyou.util.Encryption;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lk on 2015/11/11.
 * 网络接口请求类
 */
public class ServiceUtil {
    private Context mContext;
    private HttpUtil mHttpUtil;
    private volatile static ServiceUtil INSTANCE = null;
    private static final Object LOCK = new Object();
    public static final String DATA_KEY = "content";
    /**
     * 网络错误
     */
    public static final int ERROR = 0xdff1;
    /**
     * 数据为空
     */
    public static final int NULL = 0xdff2;

    private ServiceUtil() {
    }

    private ServiceUtil(Context context) {
        mContext = context;
        mHttpUtil = HttpUtil.getInstance(context);
    }

    public static ServiceUtil getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceUtil(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取下载信息
     */
    public void getGameDownloadInfo(Map<String, String> params, HttpUtil.AbsResponse response){
        requestData(ServerConstance.GET_GAME_DOWNLOAD_INFO, params, response, false);
    }

    /**
     * 获取专题文章列表
     */
    public void getTopicArtList(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_TOPIC_ART_LISTS, params, response, true);
    }

    /**
     * 获取专题详情内容
     */
    public void getTopicDetailContent(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_TOPIC_DETAIL_CONTENT, params, response, true);
    }

    /**
     * 上传头像
     */
    public void uploadHeadImg(String key, String filePath, HttpUtil.AbsResponse response) {
        upload(ServerConstance.UPLOAD_HEAD_IMG, key, filePath, response);
    }

    /**
     * 推送绑定
     */
    public void pushBind(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.PUSH_BIND, params, response, true);
    }

    /**
     * 开服开测列表
     */
    public void getFindOpenServerList(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.OPEN_SERVER_LIST, params, response, true);
    }

    /**
     * 发现-礼包
     */
    public void getFindGiftList(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.FIND_GIFT_LIST, params, response, true);
    }

    /**
     * 签到
     */
    public void signIn(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.SIGN_IN, params, response, false);
    }

    /**
     * 发表留言
     */
    public void sendLeaveMsg(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.SEND_LEAVE_MSG, params, response, false);
    }

    /**
     * 获取留言列表
     */
    public void getLeaveMsgList(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_LEAVE_MSG_LIST, params, response, true);
    }

    /**
     * 获取个人中心数据
     */
    public void getUserCenterData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_USER_CENTER_DATA, params, response, false);
    }

    /**
     * 获取消息列表
     */
    public void getMsgList(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_MSG_LIST, params, response, true);
    }

    /**
     * 高手称号
     */
    public void honorTag(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.HONOR_TAG, params, response, true);
    }

    /**
     * 高手等级
     */
    public void honorLev(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.HONOR_LEV, params, response, true);
    }

    /**
     * 通知设置
     */
    public void notifySetting(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.NOTIFY_SETTING, params, response, false);
    }

    /**
     * 消息读取状态
     */
    public void msgReadState(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.MSG_READ_STATE, params, response, false);
    }

    /**
     * 我的礼包
     */
    public void getGiftList(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_GIFT_LIST, params, response, true);
    }

    /**
     * 动态
     */
    public void getDynamicList(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_DYNAMIC_LIST, params, response, true);
    }

    /**
     * 取消关注
     */
    public void cancelFollow(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CANCEL_FOLLOW, params, response, false);
    }

    /**
     * 关注
     */
    public void follow(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.FOLLOW, params, response, true);
    }

    /**
     * 获取粉丝，关注列表
     */
    public void getFansList(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_FANS_LIST, params, response, true);
    }

    /**
     * 抢号
     */
    public void getGiftCode(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GIFT_CODE, params, response, false);
    }

    /**
     * 取消收藏
     */
    public void cancelCollect(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CANCEL_COLLECT, params, response, false);
    }

    /**
     * 添加收藏
     */
    public void addCollect(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.ADD_COLLECT, params, response, false);
    }

    /**
     * 获取收藏状态
     */
    public void getCollectState(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_COLLECT_STATE, params, response, false);
    }

    /**
     * 回复我的
     */
    public void reComment(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.RE_COMMENT, params, response, false);
    }

    /**
     * 我的评论
     */
    public void meComment(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.ME_COMMENT, params, response, false);
    }

    /**
     * 收藏
     */
    public void getCollectList(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.COLLECT_LIST, params, response, false);
    }

    /**
     * 评论点赞
     */
    public void replyPraise(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.REPLY_PRAISE, params, response, false);
    }

    /**
     * 发表回复
     */
    public void sendReply(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.SEND_REPLY, params, response, false);
    }

    /**
     * 发表评论
     */
    public void sendComment(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.SEND_COMMENT, params, response, false);
    }

    /**
     * 评论详情
     */
    public void getCommentDetail(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_COMMENT_DETAIL, params, response, true);
    }

    /**
     * 获取评论总数
     */
    public void getCommentTotal(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_COMMENT_TOTAL, params, response, false);
    }

    /**
     * 获取评论
     */
    public void getComment(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_COMMENT, params, response, true);
    }

    /**
     * 礼包详情
     */
    public void getGiftDetail(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_GIFT_DETAIL, params, response, true);
    }

    /**
     * 游戏详情-栏目
     */
    public void getGameDetailTab(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_GAME_DETAIL_TAB, params, response, true);
    }

    /**
     * 游戏详情-礼包
     */
    public void getGameDetailGift(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_GAME_DETAIL_GIFT, params, response, true);
    }

    /**
     * 游戏详情-攻略
     */
    public void getGameDetailRaider(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_GAME_DETAIL_RAIDERS, params, response, true);
    }

    /**
     * 获取文字内容
     */
    public void getArticleContent(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_ARTICLE_CONTENT, params, response, true);
    }

    /**
     * 获取资讯文章列表
     */
    public void getNewsArticleList(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_NEWS_ARTICLE_LIST, params, response, true);
    }

    /**
     * 获取攻略分类标签
     */
    public void getRaidersTag(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_RAIDERS_TAGS, params, response, true);
    }

    /**
     * 获取游戏厂商数据
     */
    public void getGameFactoryData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_GAME_FACTORY, params, response, true);
    }

    /**
     * 获取游戏厂商列表数据
     */
    public void getGameFactoryListData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_GAME_FACTORY_LIST, params, response, true);
    }

    /**
     * 获取验证码
     */
    public void getCode(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_CODE, params, response, false);
    }

    /**
     * 注册
     */
    public void reg(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.REG, params, response, false);
    }

    /**
     * 登录
     */
    public void login(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.LOGIN, params, response, false);
    }

    /**
     * 专题分类详情
     */
    public void getGameTopicClassifyDetailData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_TOPIC_CLASSIFY_DETAIL, params, response, true);
    }


    /**
     * 专题分类数据
     */
    public void getGameTopicClassifyData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_TOPIC_CLASSIFY, params, response, true);
    }

    /**
     * 专题数据
     */
    public void getGameTopicData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_TOPIC, params, response, true);
    }


    /**
     * 下载计数
     */
    public void downloadCount(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.DOWNLOAD_COUNT, params, response, false);
    }

    /**
     * 意见反馈
     */
    public void feedback(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.FEEDBACK, params, response, false);
    }

    /**
     * 搜索--搜索数据
     */
    public void getSearchData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.SEARCH_DATA, params, response, true);
    }

    /**
     * 搜索--搜索热词
     */
    public void getSearchHotWord(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.SEARCH_HOT_WORD, params, response, true);
    }

    /**
     * 获取分享数据
     */
    public void getShareData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.SHARE_DATA, params, response, true);
    }

    /**
     * 游戏详情--详情
     */
    public void getGameDetailInfo(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_DETAIL_INFO, params, response, false);
    }

    /**
     * 获取游戏排行数据
     */
    public void getGameRankData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_RANK, params, response, true);
    }

    /**
     * 获取游戏排行分类
     */
    public void getGameRankClassify(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_RANK_CLASSIFY, params, response, true);
    }

    /**
     * 游戏详情
     */
    public void getGameDetailData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_DETAIL, params, response, true);
    }

    /**
     * 获取游戏推荐数据
     */
    public void getGameRecommendData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_RECOMMEND, params, response, true);
    }

    /**
     * 获取游戏Banner数据
     */
    public void getGameBannerData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_BANNER, params, response, true);
    }

    /**
     * 获取游戏精选信息
     */
    public void getGameSelectedData(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_SELECTED, params, response, true);
    }

    /**
     * 获取游戏更新
     */
    public void getGameUpdateInfo(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GAME_UPDATE, params, response, true);
    }

    /**
     * 游戏分类筛选下通过tag进行筛选
     */
    public void getGameClassifyScreen(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CLASSIFY_SCREEN, params, response, true);
    }

    /**
     * 游戏分类筛选下通过type进行筛选
     */
    public void getGameClassifyType(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CLASSIFY_TYPE, params, response, true);
    }

    /**
     * 获取分类游戏
     */
    public void getGameClassifyInfo(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CLASSIFY_GAME, params, response, true);
    }

    /**
     * 获取游戏分类
     */
    public void getGameClassify(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CLASSIFY, params, response, true);
    }

    /**
     * 获取启动参数
     */
    public void getLauncherParams(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.LAUNCHER_PARAMS, params, response);
    }


    /**
     * 新版本检测
     */
    public void getNewVersionInfo(Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CHECK_NEW_VERSION, params, response);
    }

    /**
     * 判断是否请求成功
     *
     * @param jsonObject
     * @return
     */
    public boolean isRequestSuccess(JSONObject jsonObject) {
        try {
            return jsonObject.getInt("code") == 200;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param obj   JsonObj
     * @param clazz
     * @param <T>   各种实体
     * @return
     * @throws JSONException
     */
    public <T> List<T> jsonArray2List(JSONObject obj, Class<T> clazz) throws JSONException {
        return new Gson().fromJson(obj.getJSONArray("content").toString(),
                new TypeToken<List<T>>() {
                }.getType());
    }

    /**
     * 获取msg
     *
     * @param jsonObject
     * @return
     */
    public static String getMsg(JSONObject jsonObject) {
        try {
            return jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

//    private static Map<String, String> mixParams(Map<String, String> map) {
//        Map<String, String> params = new HashMap<>();
//        Set set = map.entrySet();
//        for (Object aSet : set) {
//            Map.Entry entry = (Map.Entry) aSet;
//            params.put(entry.getKey() + "", entry.getValue() + "");
//        }
//        //添加通用参数
//        return params;
//    }

    /**
     * 文件上传
     *
     * @param url
     * @param key      和服务器匹配的key
     * @param filePath
     * @param response
     */
    private void upload(String url, String key, String filePath, HttpUtil.AbsResponse response) {
//        mHttpUtil.uploadFile(ServerConstance.BASE_URL + url, filePath, key, HttpUtil.CONTENT_TYPE_IMG, createHeadr(), response);
        mHttpUtil.uploadFile(ServerConstance.TEST_BASE_URL + url, filePath, key, HttpUtil.CONTENT_TYPE_IMG, createHeadr(), response);
    }

    /**
     * 不使用缓存的网络请求
     */
    private void requestData(String url, Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(url, params, response, false);
    }

    /**
     * @param url      请求链接
     * @param params   请求参数
     * @param response 网络回调接口
     * @param useCache 是否使用缓存
     */
    private void requestData(String url, Map<String, String> params, HttpUtil.AbsResponse response, boolean useCache) {
//        mHttpUtil.post(ServerConstance.BASE_URL + url, params, headers, response, useCache);
        mHttpUtil.post(ServerConstance.TEST_BASE_URL + url, params, createHeadr(), response, useCache);
    }

    private Map<String, String> createHeadr() {
        String randomCode = Encryption.getRandomCode();
        String timestamp = System.currentTimeMillis() + "";
        Map<String, String> headers = new WeakHashMap<>();
        headers.put("nonce", randomCode);
        headers.put("sign", Encryption.getSingCode(randomCode, timestamp));
        headers.put("timestamp", timestamp);
        headers.put("Charset", "UTF-8");
        headers.put("version", AndroidUtils.getVersionName(mContext));
        headers.put("versionCode", AndroidUtils.getVersionCode(mContext) + "");
        headers.put("device", 1 + "");
        UserEntity user = AppManager.getInstance().getUser();
        if (user != null) {
            headers.put("userId", user.getUserId() + "");
        }
        return headers;
    }
}

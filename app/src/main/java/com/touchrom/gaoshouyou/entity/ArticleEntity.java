package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lyy on 2016/3/2.
 * 文章实体
 */
public class ArticleEntity extends BaseEntity {
    String imgUrl;
    String title;   //文章标题
    String content; //文章内容
    String time;
    int num;     //浏览次数
    int articleId;   //文章Id
    String commentNum;  //评论数
    String compiler;    //编辑昵称
    /**
     * 1、攻略
     * 2、活动
     * 3、评测
     * 4、资讯
     * 5、新闻
     * 6、专题
     * 7、资料
     * 8、问答
     */
    int typeId;
    String artType; //文章类型

    public String getArtType() {
        return artType;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public String getCompiler() {
        return compiler;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getArticleId() {
        return articleId;
    }
}

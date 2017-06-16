package com.touchrom.gaoshouyou.entity;

import com.touchrom.gaoshouyou.base.BaseEntity;

import java.util.List;

/**
 * Created by lk on 2016/3/2.
 * 资讯实体
 */
public class NewsEntity extends BaseEntity {
    public static final int ITEM_BANNER = 1; //banner
    public static final int ITEM_ARTICLE = 2;    //文章
    public static final int ITEM_REVIEW_ARTICLE = 3; //评测文章

    List<BannerEntity> banners;
    ArticleEntity article;
    ReviewArticleEntity reviewArticle;
    int type;   //item类型
    int jump;   //跳转类型

    public List<BannerEntity> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerEntity> banners) {
        this.banners = banners;
    }

    public ArticleEntity getArticle() {
        return article;
    }

    public void setArticle(ArticleEntity article) {
        this.article = article;
    }

    public ReviewArticleEntity getReviewArticle() {
        return reviewArticle;
    }

    public void setReviewArticle(ReviewArticleEntity reviewArticle) {
        this.reviewArticle = reviewArticle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    /**
     * 文章评测实体
     */
    public static class ReviewArticleEntity extends ArticleEntity {
        String muNum;   //多媒体分数
        String ieNum;   //交互体验分数
        String playNum; //玩法体验分数
        String complexNum;  //综合分数

        public String getMuNum() {
            return muNum;
        }

        public void setMuNum(String muNum) {
            this.muNum = muNum;
        }

        public String getIeNum() {
            return ieNum;
        }

        public void setIeNum(String ieNum) {
            this.ieNum = ieNum;
        }

        public String getPlayNum() {
            return playNum;
        }

        public void setPlayNum(String playNum) {
            this.playNum = playNum;
        }

        public String getComplexNum() {
            return complexNum;
        }

        public void setComplexNum(String complexNum) {
            this.complexNum = complexNum;
        }
    }

}

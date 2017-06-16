package com.touchrom.gaoshouyou.entity;

import com.google.gson.annotations.SerializedName;
import com.touchrom.gaoshouyou.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lk on 2016/3/14.
 * 评论实体
 */
public class CommentEntity extends BaseEntity {
    int cmtId;  //评论Id
    /**
     * 0游戏
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
    @SerializedName("headImg")
    String imgUrl;
    @SerializedName("nickName")
    String cmName;   //评论者昵称
    @SerializedName("userId")
    int cmId;   //评论者Id
    String time;
    @SerializedName("replyNum")
    int commentNum; //回复数
    int agreeNum;   //点赞数
    int floor;  //楼层
    @SerializedName("comment")
    String content;
    @SerializedName("status")
    boolean agreed;
    List<ReplyEntity> reply = new ArrayList<>();

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }

    public void setAgreeNum(int agreeNum) {
        this.agreeNum = agreeNum;
    }

    public int getCmtId() {
        return cmtId;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCmName() {
        return cmName;
    }

    public int getCmId() {
        return cmId;
    }

    public String getTime() {
        return time;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public int getAgreeNum() {
        return agreeNum;
    }

    public int getFloor() {
        return floor;
    }

    public String getContent() {
        return content;
    }

    public List<ReplyEntity> getReply() {
        return reply;
    }

    public class ReplyEntity extends BaseEntity {
        @SerializedName("reCmtid")
        int reCmtId;
        @SerializedName("reNickname")
        String reName;
        @SerializedName("reUserid")
        int reId;
        @SerializedName("byNickname")
        String byReName;
        @SerializedName("byUserid")
        int byReId;
        @SerializedName("reComment")
        String reContent;
        String reTime;

        public int getByReId() {
            return byReId;
        }

        public int getReCmtId() {
            return reCmtId;
        }

        public String getReName() {
            return reName;
        }

        public int getReId() {
            return reId;
        }

        public String getByReName() {
            return byReName;
        }

        public String getReContent() {
            return reContent;
        }

        public String getReTime() {
            return reTime;
        }
    }
}

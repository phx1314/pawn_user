package com.glavesoft.pawnuser.mod;

import java.util.List;

/**
 * Created by Sinyu on 2018/12/19.
 */

public class SendCallGoodsComment {

    /**
     * commentReplyExList : [{"commentId":0,"content":"是实事求是","createTime":null,
     * "fromNickname":"","fromThumbImg":"","fromUid":0,"id":12,"isAuthor":0,"isShow":0,
     * "replyId":0,"replyType":0,"toNickname":"","toUid":0}]
     * content : 后还治其人之身全心全意行going 公司破坏和你明明跟明明毫米汞柱going 狗狗狗狗狗狗狗狗狗狗狗狗狗狗你狗狗你狗狗泼皮民情哦
     * createTime : 2018-12-17 19:52:36
     * headImg : 12313
     * id : 12
     * isAuthor : 0
     * isHot : 0
     * isReply : 0
     * isShow : 0
     * isTop : 0
     * likeNum : 0
     * nickName : 228
     * replyNum : 0
     * status : 1
     * topicId : 1104
     * topicUserId : 228
     * type : 1
     * userId : 228
     */

    private String content;
    private String createTime;
    private String headImg;
    private int id;
    private int isAuthor;
    private int isHot;
    private int isReply;
    private int isShow;
    private int isTop;
    private int likeNum;
    private String nickName;
    private int replyNum;
    private int status;
    private int topicId;
    private int topicUserId;
    private int type;
    private int userId;
    private List<CommentReplyExListBean> commentReplyExList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsAuthor() {
        return isAuthor;
    }

    public void setIsAuthor(int isAuthor) {
        this.isAuthor = isAuthor;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getIsReply() {
        return isReply;
    }

    public void setIsReply(int isReply) {
        this.isReply = isReply;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getTopicUserId() {
        return topicUserId;
    }

    public void setTopicUserId(int topicUserId) {
        this.topicUserId = topicUserId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<CommentReplyExListBean> getCommentReplyExList() {
        return commentReplyExList;
    }

    public void setCommentReplyExList(List<CommentReplyExListBean> commentReplyExList) {
        this.commentReplyExList = commentReplyExList;
    }

    public static class CommentReplyExListBean {
        /**
         * commentId : 0
         * content : 是实事求是
         * createTime : null
         * fromNickname :
         * fromThumbImg :
         * fromUid : 0
         * id : 12
         * isAuthor : 0
         * isShow : 0
         * replyId : 0
         * replyType : 0
         * toNickname :
         * toUid : 0
         */

        private int commentId;
        private String content;
        private String createTime;
        private String fromNickname;
        private String fromThumbImg;
        private int fromUid;
        private int id;
        private int isAuthor;
        private int isShow;
        private int replyId;
        private int replyType;
        private String toNickname;
        private int toUid;

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getFromNickname() {
            return fromNickname;
        }

        public void setFromNickname(String fromNickname) {
            this.fromNickname = fromNickname;
        }

        public String getFromThumbImg() {
            return fromThumbImg;
        }

        public void setFromThumbImg(String fromThumbImg) {
            this.fromThumbImg = fromThumbImg;
        }

        public int getFromUid() {
            return fromUid;
        }

        public void setFromUid(int fromUid) {
            this.fromUid = fromUid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsAuthor() {
            return isAuthor;
        }

        public void setIsAuthor(int isAuthor) {
            this.isAuthor = isAuthor;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public int getReplyId() {
            return replyId;
        }

        public void setReplyId(int replyId) {
            this.replyId = replyId;
        }

        public int getReplyType() {
            return replyType;
        }

        public void setReplyType(int replyType) {
            this.replyType = replyType;
        }

        public String getToNickname() {
            return toNickname;
        }

        public void setToNickname(String toNickname) {
            this.toNickname = toNickname;
        }

        public int getToUid() {
            return toUid;
        }

        public void setToUid(int toUid) {
            this.toUid = toUid;
        }
    }
}

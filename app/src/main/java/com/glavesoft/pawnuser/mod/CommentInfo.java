package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/28.
 */

public class CommentInfo implements Serializable {

    private String content="";//内容
    private int id;
    private int isPraise;//	1已赞
    private int praiseCnt;//赞数量
    private int replyCnt;//评论数量
    private String time;//时间
    private String replyContent="";//回复的内容
    private UserInfo userInfo;//用户信息
    private UserInfo replyUser;//回复的用户

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public UserInfo getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserInfo replyUser) {
        this.replyUser = replyUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public int getPraiseCnt() {
        return praiseCnt;
    }

    public void setPraiseCnt(int praiseCnt) {
        this.praiseCnt = praiseCnt;
    }

    public int getReplyCnt() {
        return replyCnt;
    }

    public void setReplyCnt(int replyCnt) {
        this.replyCnt = replyCnt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}

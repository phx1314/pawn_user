package com.glavesoft.pawnuser.mod;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 严光
 * @date: 2017/8/29
 * @company:常州宝丰
 */
public class MyNewsListInfo implements Serializable{
    private String praiseCnt;
    private String commentCnt;
    private String id;
    private ArrayList<String> imgs;
    private String isHot;
    private String isTop;
    private String time;
    private String title;
    private String source;
    private int type;
    private String isCollect;
    private UserInfo userInfo;
    private Boolean isCheck=false;
    private ArrayList<SendIndo> content1;
    private String content;

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }

    public String getPraiseCnt() {
        return praiseCnt;
    }

    public void setPraiseCnt(String praiseCnt) {
        this.praiseCnt = praiseCnt;
    }

    public String getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(String commentCnt) {
        this.commentCnt = commentCnt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ArrayList<SendIndo> getContent1() {
        return content1;
    }

    public void setContent1(ArrayList<SendIndo> content1) {
        this.content1 = content1;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

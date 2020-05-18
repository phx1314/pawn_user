package com.glavesoft.pawnuser.mod;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.glavesoft.pawnuser.adapter.CommentsAdapter;

/**
 * Created by Sinyu on 2018/8/3.
 */

public class GoodsCommentsInfo  implements MultiItemEntity {

    /**
     * createTime : 2018-08-03 15:08:09
     * goodsId : 725
     * goodsName : 金玉满堂琥珀挂件
     * id : 2
     * img : 078ad9b58fd24f5c84ff28eb0f3976e2
     * info : 好评
     * modifyTime : null
     * orderId : 315
     * orgId : 0
     * replayInfo :
     * replayTime : null
     * replayUserId : 0
     * score : 5
     * showName : 0
     * status : 1
     * userId : 167
     * userName : 杨俊鑫
     */
    private String userHeadImg;
    private String createTime;
    private int goodsId;
    private String goodsName;
    private int id;
    private String img;
    private String info;
    private Object modifyTime;
    private int orderId;
    private int orgId;
    private String replayInfo;
    private Object replayTime;
    private int replayUserId;
    private int score;
    private int showName;
    private int status;
    private int userId;
    private String userName;

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Object modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getReplayInfo() {
        return replayInfo;
    }

    public void setReplayInfo(String replayInfo) {
        this.replayInfo = replayInfo;
    }

    public Object getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(Object replayTime) {
        this.replayTime = replayTime;
    }

    public int getReplayUserId() {
        return replayUserId;
    }

    public void setReplayUserId(int replayUserId) {
        this.replayUserId = replayUserId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getShowName() {
        return showName;
    }

    public void setShowName(int showName) {
        this.showName = showName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int getItemType() {
        return CommentsAdapter.TYPE1;
    }
}

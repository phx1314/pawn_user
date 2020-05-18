package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/11/6
 * @company:常州宝丰
 */
public class NoPawnInfo implements Serializable{

    private String addressCode;//地址Code
    private String authPrice;//鉴定价格
    private String id;//当品id
    private String image;//当品图片
    private String state;//邮寄状态(0:未邮寄 1:已邮寄)
    private String title;//当品图片
    private String goSellType;//是否卖给平台
    private String authCnt;//是否流拍 0未流拍
    private String pawnId;//拍卖id
    private String isRedeem;//1就是已经赎回

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoSellType() {
        return goSellType;
    }

    public void setGoSellType(String goSellType) {
        this.goSellType = goSellType;
    }

    public String getAuthCnt() {
        return authCnt;
    }

    public void setAuthCnt(String authCnt) {
        this.authCnt = authCnt;
    }

    public String getPawnId() {
        return pawnId;
    }

    public void setPawnId(String pawnId) {
        this.pawnId = pawnId;
    }

    public String getIsRedeem() {
        return isRedeem;
    }

    public void setIsRedeem(String isRedeem) {
        this.isRedeem = isRedeem;
    }
}

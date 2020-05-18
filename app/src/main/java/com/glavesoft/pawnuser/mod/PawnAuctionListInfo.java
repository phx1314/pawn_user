package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/11/8
 * @company:常州宝丰
 */
public class PawnAuctionListInfo implements Serializable{

    private String auctionOrgname;
    private String createTime;
    private String id;
    private String modifyTime;
    private String money;
    private String moneyRate;
    private String num;
    private String orgId;
    private String orgUserId;
    private String pawnCode;
    private String pawnId;
    private String rate;
    private String times;

    public String getAuctionOrgname() {
        return auctionOrgname;
    }

    public void setAuctionOrgname(String auctionOrgname) {
        this.auctionOrgname = auctionOrgname;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoneyRate() {
        return moneyRate;
    }

    public void setMoneyRate(String moneyRate) {
        this.moneyRate = moneyRate;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgUserId() {
        return orgUserId;
    }

    public void setOrgUserId(String orgUserId) {
        this.orgUserId = orgUserId;
    }

    public String getPawnCode() {
        return pawnCode;
    }

    public void setPawnCode(String pawnCode) {
        this.pawnCode = pawnCode;
    }

    public String getPawnId() {
        return pawnId;
    }

    public void setPawnId(String pawnId) {
        this.pawnId = pawnId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}

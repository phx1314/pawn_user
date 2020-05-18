package com.glavesoft.pawnuser.mod;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 严光
 * @date: 2017/11/14
 * @company:常州宝丰
 */
public class PawnDetailInfo implements Serializable {
    private String authPrice;//鉴定价格
    private String count;//出价次数
    private String id;//当品id
    private String image;//当品图片
    private String loansPrice;//我的需求贷款额度
    private String loansRate;//我的需求贷款利率
    private String money;//已贷款额度
    private String pawnTime;//贷款时间
    private String state;//状态(0:进行中 1:已结束)
    private String time;//到期时间
    private String title;//当品名称
    private String userId;//用户id
    private String type;//0未逾期 1逾期
    private String isVerify;//0无1已到账
    private String payeeTicket;//机构打款凭证
    private ArrayList<PawnAuctionListInfo> pawnAuctionList;//贷款机构列表

    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

    public String getLoansPrice() {
        return loansPrice;
    }

    public void setLoansPrice(String loansPrice) {
        this.loansPrice = loansPrice;
    }

    public String getLoansRate() {
        return loansRate;
    }

    public void setLoansRate(String loansRate) {
        this.loansRate = loansRate;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPawnTime() {
        return pawnTime;
    }

    public void setPawnTime(String pawnTime) {
        this.pawnTime = pawnTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(String isVerify) {
        this.isVerify = isVerify;
    }

    public String getPayeeTicket() {
        return payeeTicket;
    }

    public void setPayeeTicket(String payeeTicket) {
        this.payeeTicket = payeeTicket;
    }

    public ArrayList<PawnAuctionListInfo> getPawnAuctionList() {
        return pawnAuctionList;
    }

    public void setPawnAuctionList(ArrayList<PawnAuctionListInfo> pawnAuctionList) {
        this.pawnAuctionList = pawnAuctionList;
    }
}

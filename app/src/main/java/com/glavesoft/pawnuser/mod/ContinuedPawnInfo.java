package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/11/21
 * @company:常州宝丰
 */
public class ContinuedPawnInfo implements Serializable{

    private String authPrice;//鉴定价	string
    private String beginTime;//借款日期	string
    private String comMoney;//预缴综合费	string
    private String coninueTime;//续当至日期	string
    private String endTime;//当前应还款日期	string
    private String goodsId;//当品当号	string
    private String id;//当品id	integer/int32
    private String images;//图片	string
    private String loansPrice;//已贷款额度	string
    private String lxMoney;//利息	string
    private String money;//到期应还款额度	string
    private String moneyRate;//利息利率	string
    private String orgName;//典当机构名称	string
    private String overdueRate;//逾期滞纳利率	string
    private String payeeBankCardCode;//收款账户--银行卡号	string
    private String payeeBankName;//收款账户--银行名称	string
    private String payeeName;//收款账户--户名	string
    private String rate;//综合利率	string
    private String redeemOverdue;//逾期滞纳金	string
    private String title;//当品名称	string
    private String totalMoney;
    private String zhlxMoney;//综合利息

    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getComMoney() {
        return comMoney;
    }

    public void setComMoney(String comMoney) {
        this.comMoney = comMoney;
    }

    public String getConinueTime() {
        return coninueTime;
    }

    public void setConinueTime(String coninueTime) {
        this.coninueTime = coninueTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getLoansPrice() {
        return loansPrice;
    }

    public void setLoansPrice(String loansPrice) {
        this.loansPrice = loansPrice;
    }

    public String getLxMoney() {
        return lxMoney;
    }

    public void setLxMoney(String lxMoney) {
        this.lxMoney = lxMoney;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(String overdueRate) {
        this.overdueRate = overdueRate;
    }

    public String getPayeeBankCardCode() {
        return payeeBankCardCode;
    }

    public void setPayeeBankCardCode(String payeeBankCardCode) {
        this.payeeBankCardCode = payeeBankCardCode;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRedeemOverdue() {
        return redeemOverdue;
    }

    public void setRedeemOverdue(String redeemOverdue) {
        this.redeemOverdue = redeemOverdue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getZhlxMoney() {
        return zhlxMoney;
    }

    public void setZhlxMoney(String zhlxMoney) {
        this.zhlxMoney = zhlxMoney;
    }
}

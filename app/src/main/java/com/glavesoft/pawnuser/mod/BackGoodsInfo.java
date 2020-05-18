package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/11/20
 * @company:常州宝丰
 */
public class BackGoodsInfo implements Serializable{
    private String allMoney;//总计金额	string
    private String authPrice;//鉴定价	string
    private String beginDate;//贷款日期	string
    private String beginMoney;//还款明细--本金	string
    private String endDate;//应还款日期	string
    private String id;//id	integer/int32
    private String images;//图片	string
    private String monetRate;//滞纳金利率	string
    private String money;//已发放当金	string
    private String name;//name	string
    private String outTime;//逾期天数	integer/int32
    private String pawnTime;//上期典当期限	integer/int32
    private String pawnticketCode;//当号	string
    private String rate;//综合利率	string
    private String redeemOverdue;//逾期滞纳金	string
    private String redeemRate;//利息利率	string
    private String totalBackMoney;//应还款额	string
    private String totalMoney;//综合利息

    private String payeeBankCardCode;//收款账户--银行卡号	string
    private String payeeBankName;//收款账户--银行卡名字	string
    private String payeeName;//收款账户--户名	string



    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
    }

    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getBeginMoney() {
        return beginMoney;
    }

    public void setBeginMoney(String beginMoney) {
        this.beginMoney = beginMoney;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getMonetRate() {
        return monetRate;
    }

    public void setMonetRate(String monetRate) {
        this.monetRate = monetRate;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getPawnTime() {
        return pawnTime;
    }

    public void setPawnTime(String pawnTime) {
        this.pawnTime = pawnTime;
    }

    public String getPawnticketCode() {
        return pawnticketCode;
    }

    public void setPawnticketCode(String pawnticketCode) {
        this.pawnticketCode = pawnticketCode;
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

    public String getRedeemRate() {
        return redeemRate;
    }

    public void setRedeemRate(String redeemRate) {
        this.redeemRate = redeemRate;
    }

    public String getTotalBackMoney() {
        return totalBackMoney;
    }

    public void setTotalBackMoney(String totalBackMoney) {
        this.totalBackMoney = totalBackMoney;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
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
}

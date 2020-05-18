package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/11/20
 * @company:常州宝丰
 */
public class PlatGetDetailInfo implements Serializable{

    private String bxOrgId;
    private String authPrice;//鉴定价格
    private String bxMoney;//平台提供当金
    private String bxMoneyRate;//平台利息利率
    private String bxRate;//平台综合利率
    private String id;//当品id
    private String image;//当品图片
    private String loansPrice;//期望当金
    private String loansRate;//期望利率
    private String pawnTime;//贷款时间(1:15天 2:30天)
    private String title;//当品名称
    private String comName;//公司名称
    private String comPhone;//联系电话
    private String comaddress;//公司地址
    private String manager;//法人代表
    private String regMoney;//公司注册资金


    public String getBxOrgId() {
        return bxOrgId;
    }

    public void setBxOrgId(String bxOrgId) {
        this.bxOrgId = bxOrgId;
    }

    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

    public String getBxMoney() {
        return bxMoney;
    }

    public void setBxMoney(String bxMoney) {
        this.bxMoney = bxMoney;
    }

    public String getBxMoneyRate() {
        return bxMoneyRate;
    }

    public void setBxMoneyRate(String bxMoneyRate) {
        this.bxMoneyRate = bxMoneyRate;
    }

    public String getBxRate() {
        return bxRate;
    }

    public void setBxRate(String bxRate) {
        this.bxRate = bxRate;
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

    public String getPawnTime() {
        return pawnTime;
    }

    public void setPawnTime(String pawnTime) {
        this.pawnTime = pawnTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getComPhone() {
        return comPhone;
    }

    public void setComPhone(String comPhone) {
        this.comPhone = comPhone;
    }

    public String getComaddress() {
        return comaddress;
    }

    public void setComaddress(String comaddress) {
        this.comaddress = comaddress;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getRegMoney() {
        return regMoney;
    }

    public void setRegMoney(String regMoney) {
        this.regMoney = regMoney;
    }
}

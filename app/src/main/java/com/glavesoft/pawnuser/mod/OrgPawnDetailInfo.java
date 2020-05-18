package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/11/30
 * @company:常州宝丰
 */
public class OrgPawnDetailInfo {
    private String address;//注册地址	string
    private String dealAmount;//平台成交数量	string
    private String introduction;//公司简介	string
    private String lagalPerson;//法人代表	string
    private String orgId;//机构id	string
    private String orgImages;//公司环境	string
    private String orgName;//机构名称	string
    private String registeredCapital;//注册资金

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(String dealAmount) {
        this.dealAmount = dealAmount;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getLagalPerson() {
        return lagalPerson;
    }

    public void setLagalPerson(String lagalPerson) {
        this.lagalPerson = lagalPerson;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgImages() {
        return orgImages;
    }

    public void setOrgImages(String orgImages) {
        this.orgImages = orgImages;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }
}

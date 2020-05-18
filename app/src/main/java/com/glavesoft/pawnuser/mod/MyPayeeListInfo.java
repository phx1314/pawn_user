package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/11/17
 * @company:常州宝丰
 */
public class MyPayeeListInfo {
    private String bankCode;//银行卡号
    private String bankName;//银行名称
    private String id;
    private String money;//金额
    private String orgName;//相关机构名称
    private String pawnCode;//当号
    private String phone;//电话
    private String state;//1典当 2续当 3赎当
    private String ticket;//凭证
    private String time	;//时间

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPawnCode() {
        return pawnCode;
    }

    public void setPawnCode(String pawnCode) {
        this.pawnCode = pawnCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

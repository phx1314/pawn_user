package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

public class UserInfo implements Serializable
{
    private boolean isRememberPwd = false;

    private String id = "";
    private String token="" ;
    private String  Version;//客户端版本
    private String code="";
    private String deviceType="";
    private String deviceid="";
    private String userid="";
    private String account="";//账号
    private String headImg="";//头像
    private String nickName="";//昵称
    private String  sex="";
    private String count="";
    private String balance;//余额

    private String address;//地址
    private String idCardImg;//身份证正面
    private String idCardReverse;//身份证反面
    private String isBind;//身份证是否绑定0:未绑定 1绑定


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isRememberPwd() {
        return isRememberPwd;
    }

    public void setRememberPwd(boolean rememberPwd) {
        isRememberPwd = rememberPwd;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdCardImg() {
        return idCardImg;
    }

    public void setIdCardImg(String idCardImg) {
        this.idCardImg = idCardImg;
    }

    public String getIdCardReverse() {
        return idCardReverse;
    }

    public void setIdCardReverse(String idCardReverse) {
        this.idCardReverse = idCardReverse;
    }

    public String getIsBind() {
        return isBind;
    }

    public void setIsBind(String isBind) {
        this.isBind = isBind;
    }
}

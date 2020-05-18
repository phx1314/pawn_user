package com.glavesoft.alipay.util;

/**
 * @author 严光
 * @date: 2017/9/5
 * @company:常州宝丰
 */
public class PayInfo {
    /**
     * backUrl :
     * id :
     * money :
     * platform : 0
     * wxJsPay : {"appid":"","nonceStr":"","packageInfo":"","prepayId":"","sign":"","signature":"","timestamp":0}
     */

    private String backUrl;
    private String id;
    private String money;
    private int platform;


    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
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

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }
}

package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/10/30
 * @company:常州宝丰
 */
public class MyAppraisalInfo implements Serializable{
    private String id;//宝贝id
    private String image;//图片
    private String postState;//1未邮寄2邮寄中3平台确认
    private String price;//鉴定价格
    private String priceTest;//预估价
    private String result;//鉴定结果(-1:无 0:未鉴定 1:鉴定中 2:无法鉴定 3:赝品 4:真品)
    private String title;//名称
    private String platformIsVerify;//用户把宝贝卖给平台，平台是否确认 0：未确认 1：确认
    private String platformPayTicket;//用户把宝贝卖给平台，平台上传的打款凭证
    private String appraisalDsc;//鉴定说明

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

    public String getPostState() {
        return postState;
    }

    public void setPostState(String postState) {
        this.postState = postState;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceTest() {
        return priceTest;
    }

    public void setPriceTest(String priceTest) {
        this.priceTest = priceTest;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatformIsVerify() {
        return platformIsVerify;
    }

    public void setPlatformIsVerify(String platformIsVerify) {
        this.platformIsVerify = platformIsVerify;
    }

    public String getPlatformPayTicket() {
        return platformPayTicket;
    }

    public void setPlatformPayTicket(String platformPayTicket) {
        this.platformPayTicket = platformPayTicket;
    }

    public String getAppraisalDsc() {
        return appraisalDsc;
    }

    public void setAppraisalDsc(String appraisalDsc) {
        this.appraisalDsc = appraisalDsc;
    }
}

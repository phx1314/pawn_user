package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/11/22
 * @company:常州宝丰
 */
public class StoreGoodsInfo implements Serializable{

    private String addressCode;//收货地址code	integer/int32
    private String authPrice;//鉴定价格	string
    private String couponPrice;//优惠券减免金额	string
    private String declare;//购买声明	string
    private String description;//市场预估文本	string
    private String goodsDescription;//商品描述	string
    private String id;//商品id	integer/int32
    private String images;//商品图片	string
    private String payType;//支付方式(1:支付宝 2;微信)	integer/int32
    private String price;//当前售价	string
    private String property;//商品名称--绝当品	string
    private String title;//商品名称
    private String count;//竞拍次数
    private String height;//商品图片长度
    private String img;//商品封面
    private String type;//是否是竞拍商品 0不是 1是
    private String width;//商品图片宽度
    private String name;
    private String bannerVideo;
    private String bannerVideoFace;

    private String source="";
    private String orgLogo="";
    private String orgId="";
    private String orgName="";
    private String orgIntroduction="";

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getDeclare() {
        return declare;
    }

    public void setDeclare(String declare) {
        this.declare = declare;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOrgLogo() {
        return orgLogo;
    }

    public void setOrgLogo(String orgLogo) {
        this.orgLogo = orgLogo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgIntroduction() {
        return orgIntroduction;
    }

    public void setOrgIntroduction(String orgIntroduction) {
        this.orgIntroduction = orgIntroduction;
    }

    public String getBannerVideo() {
        return bannerVideo;
    }

    public void setBannerVideo(String bannerVideo) {
        this.bannerVideo = bannerVideo;
    }

    public String getBannerVideoFace() {
        return bannerVideoFace;
    }

    public void setBannerVideoFace(String bannerVideoFace) {
        this.bannerVideoFace = bannerVideoFace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

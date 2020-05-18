package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/12/4
 * @company:常州宝丰
 */
public class JdStoreGoodsAucInfo {
    private String authPrice;//鉴定价	string
    private String goodsImg;//商品封面	string
    private String id;//商品id	integer/int32
    private String maxPirce;//当前价	string
    private String myPrice;//我的出价	string
    private String price;//起价	string
    private String state;//状态 2已中标 3未中标	integer/int32
    private int time;//时间	string
    private String title;//商品封面

    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaxPirce() {
        return maxPirce;
    }

    public void setMaxPirce(String maxPirce) {
        this.maxPirce = maxPirce;
    }

    public String getMyPrice() {
        return myPrice;
    }

    public void setMyPrice(String myPrice) {
        this.myPrice = myPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

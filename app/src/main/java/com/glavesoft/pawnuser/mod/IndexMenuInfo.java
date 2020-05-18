package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/12/11
 * @company:常州宝丰
 */
public class IndexMenuInfo {
    private String aucCount;//竞拍次数
    private String endTime;//结束竞拍时间
    private String endTime2;
    private String id;
    private String img;
    private String price;
    private String state;//1.认证商场 2.绝当新品 3.热门竞拍
    private String title;
    private long time;
    private String maxPrice;//当前最高价
    private String myPrice;//我的出价
    private String goodsType;
    private String orgName="";
    private String orgId="";
    private String source="";


    public String getAucCount() {
        return aucCount;
    }

    public void setAucCount(String aucCount) {
        this.aucCount = aucCount;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEndTime2() {
        return endTime2;
    }

    public void setEndTime2(String endTime2) {
        this.endTime2 = endTime2;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMyPrice() {
        return myPrice;
    }

    public void setMyPrice(String myPrice) {
        this.myPrice = myPrice;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

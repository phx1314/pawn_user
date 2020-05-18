package com.glavesoft.pawnuser.mod;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 严光
 * @date: 2017/12/4
 * @company:常州宝丰
 */
public class JdGoodsInfo implements Serializable{

    private String authPrice;//鉴定价--起价
    private String content;//物品描述
    private String goodsName;//商品名字
    private String id;//商品id
    private String images;//商品图片
    private String price;//当前价
    private String time;//倒计时
    private ArrayList<AuctionListInfo> goodsAuctionList;//竞拍记录

    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<AuctionListInfo> getGoodsAuctionList() {
        return goodsAuctionList;
    }

    public void setGoodsAuctionList(ArrayList<AuctionListInfo> goodsAuctionList) {
        this.goodsAuctionList = goodsAuctionList;
    }

    public class AuctionListInfo implements Serializable{
        private String createTime;
        private String goodsId;
        private String id;
        private String price;
        private String userId;
        private String userName;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}

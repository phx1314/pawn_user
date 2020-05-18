package com.glavesoft.pawnuser.mod;

import java.util.ArrayList;

public class TradeRecordInfo {

    public String authPrice;//鉴定价
    public String createTime;//录入时间
    public String goodsId;//宝贝id
    public String id;//id
    public String images;//图片
    public String orgName;//当前典当机构名称
    public String pawnTicketCode;//当号
    public String title;//宝贝名称
    public String type;//类型 1典当2续当3赎当4绝当5交易

    public ArrayList<ListInfo> list;//流通记录

    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPawnTicketCode() {
        return pawnTicketCode;
    }

    public void setPawnTicketCode(String pawnTicketCode) {
        this.pawnTicketCode = pawnTicketCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ListInfo> getList() {
        return list;
    }

    public void setList(ArrayList<ListInfo> list) {
        this.list = list;
    }

    public class ListInfo{

        public String beginTime;//时间
        public String id;
        public String orgName;//典当行名称
        public String price;//价格
        public String state;//类型 0鉴定真品1典当2续当3赎当4绝当5交易6卖给平台

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
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
    }
}

package com.glavesoft.pawnuser.model;

import java.io.Serializable;
import java.util.List;

public class ModelProduct implements Serializable {


    /**
     * total : 1
     * footer : []
     * rows : [{"mainMaterial":"牛肉","img":"000890ccb8e04719939c31f6c09f3ed9","goodsId":0,"createYear":"2020","reasonOfDismounting":"","isOnline":1,"source":6,"type":1,"orgId":0,"spec":"肉干","otherMaterial":"香料","isVerfiy":2,"total":12,"modifyTime":"","price":"0.01","isSuggest":0,"theme":"牛肉干","bannerVideoFace":"","id":1517,"state":0,"maxAutionId":0,"brand":"吉吉国王","ccHeight":"2","height":"30","info":"","platformMoney":0,"imgs":"000890ccb8e04719939c31f6c09f3ed9","cost":"0.01","goodsOwner":616,"weight":"10kg","soldOut":-2,"userId":200,"cateCode":1,"materialName":"美味","material":"肉","ccWidth":"5","createTime":"2020-06-09 20:40:58","ccLength":"10","platformState":0,"refuseInfo":"","platformRate":0,"sortOrder":1,"name":"牛肉干","width":"20","maxAuction":0,"style":"美味","bannerVideo":"","newPercent":"新"}]
     */

    public int total;
    public List<?> footer;
    public List<RowsBean> rows;

    public static class RowsBean implements Serializable {
        /**
         * mainMaterial : 牛肉
         * img : 000890ccb8e04719939c31f6c09f3ed9
         * goodsId : 0
         * createYear : 2020
         * reasonOfDismounting :
         * isOnline : 1
         * source : 6
         * type : 1
         * orgId : 0
         * spec : 肉干
         * otherMaterial : 香料
         * isVerfiy : 2
         * total : 12
         * modifyTime :
         * price : 0.01
         * isSuggest : 0
         * theme : 牛肉干
         * bannerVideoFace :
         * id : 1517
         * state : 0
         * maxAutionId : 0
         * brand : 吉吉国王
         * ccHeight : 2
         * height : 30
         * info :
         * platformMoney : 0
         * imgs : 000890ccb8e04719939c31f6c09f3ed9
         * cost : 0.01
         * goodsOwner : 616
         * weight : 10kg
         * soldOut : -2
         * userId : 200
         * cateCode : 1
         * materialName : 美味
         * material : 肉
         * ccWidth : 5
         * createTime : 2020-06-09 20:40:58
         * ccLength : 10
         * platformState : 0
         * refuseInfo :
         * platformRate : 0
         * sortOrder : 1
         * name : 牛肉干
         * width : 20
         * maxAuction : 0
         * style : 美味
         * bannerVideo :
         * newPercent : 新
         */

        public String mainMaterial;
        public String img;
        public int goodsId;
        public String createYear;
        public String reasonOfDismounting;
        public int isOnline;
        public int source;
        public int type;
        public int orgId;
        public String spec;
        public String otherMaterial;
        public int isVerfiy;
        public int total;
        public String modifyTime;
        public String price;
        public int isSuggest;
        public String theme;
        public String bannerVideoFace;
        public int id;
        public int state;
        public int maxAutionId;
        public String brand;
        public String ccHeight;
        public String height;
        public String info;
        public int platformMoney;
        public String imgs;
        public String cost;
        public int goodsOwner;
        public String weight;
        public int soldOut;
        public int userId;
        public int cateCode;
        public String materialName;
        public String material;
        public String ccWidth;
        public String createTime;
        public String ccLength;
        public int platformState;
        public String refuseInfo;
        public int platformRate;
        public int sortOrder;
        public String name;
        public String width;
        public int maxAuction;
        public String style;
        public String bannerVideo;
        public String newPercent;
    }
}

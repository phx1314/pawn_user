package com.glavesoft.pawnuser.model;

import java.io.Serializable;
import java.util.List;

public class ModelDingdan implements Serializable {


    /**
     * total : 1
     * footer : []
     * rows : [{"goodsImg":"9e51930ee3714b318fbd8e65c8554916","backState":0,"payTime":"2020-03-25 09:28:15","shipUser":"肚子里","couponId":0,"modifyTime":"2020-03-25 09:28:03","payType":1,"price":"0.10","backCode":"","id":10000236,"state":2,"goodsName":"牛肉干","userGoodsId":0,"orgName":"","tradNo":"","couponInfo":"","estate":"","payLogId":"4200000501202003259887201461","expressState":0,"commentState":0,"isSell":0,"goodsPrice":"0.10","backUser":"","refundNotVerifyReason":"","shipFirm":"","expressId":0,"code":"15850996835401513","goodsId":1517,"backAddress":"","eIsBalance":"","goodsOwnerAccount":"13915079457","goodsOwnerName":"","video":"","goodsSource":6,"orgId":0,"refState":0,"shipPhone":"15151963763","goodsCost":"0.10","expressData":"","goodsOwnerNick":"匿名用户","refundReason":"","isBalance":0,"balanceTime":"","userName":"","userId":616,"couponValue":0,"shipAddress":"北京北京市东城区阿尔","shipCode":"","createTime":"2020-03-25 09:28:03","backPhone":"","isDel":0}]
     */

    public int total;
    public List<?> footer;
    public List<RowsBean> rows;

    public static class RowsBean {
        /**
         * goodsImg : 9e51930ee3714b318fbd8e65c8554916
         * backState : 0
         * payTime : 2020-03-25 09:28:15
         * shipUser : 肚子里
         * couponId : 0
         * modifyTime : 2020-03-25 09:28:03
         * payType : 1
         * price : 0.10
         * backCode :
         * id : 10000236
         * state : 2
         * goodsName : 牛肉干
         * userGoodsId : 0
         * orgName :
         * tradNo :
         * couponInfo :
         * estate :
         * payLogId : 4200000501202003259887201461
         * expressState : 0
         * commentState : 0
         * isSell : 0
         * goodsPrice : 0.10
         * backUser :
         * refundNotVerifyReason :
         * shipFirm :
         * expressId : 0
         * code : 15850996835401513
         * goodsId : 1517
         * backAddress :
         * eIsBalance :
         * goodsOwnerAccount : 13915079457
         * goodsOwnerName :
         * video :
         * goodsSource : 6
         * orgId : 0
         * refState : 0
         * shipPhone : 15151963763
         * goodsCost : 0.10
         * expressData :
         * goodsOwnerNick : 匿名用户
         * refundReason :
         * isBalance : 0
         * balanceTime :
         * userName :
         * userId : 616
         * couponValue : 0
         * shipAddress : 北京北京市东城区阿尔
         * shipCode :
         * createTime : 2020-03-25 09:28:03
         * backPhone :
         * isDel : 0
         */

        public String goodsImg;
        public int backState;
        public String payTime;
        public String shipUser;
        public int couponId;
        public String modifyTime;
        public int payType;
        public String price;
        public String backCode;
        public int id;
        public int state;
        public String goodsName;
        public int userGoodsId;
        public String orgName;
        public String tradNo;
        public String couponInfo;
        public String estate;
        public String payLogId;
        public int expressState;
        public int commentState;
        public int isSell;
        public String goodsPrice;
        public String backUser;
        public String refundNotVerifyReason;
        public String shipFirm;
        public int expressId;
        public String code;
        public int goodsId;
        public String backAddress;
        public String eIsBalance;
        public String goodsOwnerAccount;
        public String goodsOwnerName;
        public String video;
        public int goodsSource;
        public int orgId;
        public int refState;
        public String shipPhone;
        public String goodsCost;
        public String expressData;
        public String goodsOwnerNick;
        public String refundReason;
        public int isBalance;
        public String balanceTime;
        public String userName;
        public int userId;
        public int couponValue;
        public String shipAddress;
        public String shipCode;
        public String createTime;
        public String backPhone;
        public int isDel;
    }
}

package com.glavesoft.pawnuser.model;

import java.io.Serializable;
import java.util.List;

public class ModelGrzl implements Serializable {


    /**
     * headShake : null
     * idCard : null
     * qqOpenid : null
     * type : 0
     * orgId : null
     * returnAddress : [{"address":"科技街","area":"江苏省常州市钟楼区","createTime":"2018-06-01 16:31:09","id":40,"isDefault":1,"modifyTime":"2019-07-18 15:16:35","phone":"13861138609","userId":616,"userName":"王杰"},{"address":"南甸苑20栋1305","area":"江苏省常州市武进区","createTime":"2018-06-14 11:15:09","id":42,"isDefault":1,"modifyTime":"","phone":"15052528161","userId":616,"userName":"刘健"},{"address":"科技街A座","area":"江苏省常州市钟楼区","createTime":"2018-06-20 16:21:09","id":43,"isDefault":1,"modifyTime":"","phone":"13861138609","userId":616,"userName":"王天真"},{"address":"南大街智慧园","area":"江苏省常州市钟楼区","createTime":"2018-06-12 13:00:27","id":41,"isDefault":0,"modifyTime":"","phone":"15295063176","userId":616,"userName":"杨先生"},{"address":"朝阳三村","area":"江苏省常州市天宁区","createTime":"2018-06-20 16:58:03","id":44,"isDefault":0,"modifyTime":"","phone":"13861138609","userId":616,"userName":"李木子"}]
     * idCardReverse : null
     * password : 96e79218965eb72c92a549dd5a330112
     * loginTime : 2020-05-18 09:28:14
     * modifyTime : 2020-06-10 14:18:34
     * balance : 0.00
     * headEye : null
     * lastSobotTokenTime : null
     * id : 616
     * state : 1
     * credit : 0
     * authType : 个人用户
     * headImg : 40ddab16477743d6bc4105b47e85fd77
     * headNod : null
     * isBind : 0
     * nickName : 匿名用户
     * wxOpenid : null
     * sex : 1
     * idCardHand : null
     * store : df
     * createTime : 2020-05-18 09:28:14
     * phone : 13915079457
     * imToken : null
     * name : null
     * sobotToken : null
     * idCardImg : null
     * account : 13915079457
     * isComplete : 0
     */

    public Object headShake;
    public Object idCard;
    public Object qqOpenid;
    public int type;
    public Object orgId;
    public Object idCardReverse;
    public String password;
    public String loginTime;
    public String modifyTime;
    public String balance;
    public Object headEye;
    public Object lastSobotTokenTime;
    public int id;
    public int state;
    public int credit;
    public String authType;
    public String headImg;
    public Object headNod;
    public int isBind;
    public String nickName;
    public Object wxOpenid;
    public int sex;
    public Object idCardHand;
    public String store;
    public String createTime;
    public String phone;
    public Object imToken;
    public String name;
    public Object sobotToken;
    public Object idCardImg;
    public String account;
    public int isComplete;
    public List<ReturnAddressBean> returnAddress;

    public static class ReturnAddressBean implements Serializable {
        /**
         * address : 科技街
         * area : 江苏省常州市钟楼区
         * createTime : 2018-06-01 16:31:09
         * id : 40
         * isDefault : 1
         * modifyTime : 2019-07-18 15:16:35
         * phone : 13861138609
         * userId : 616
         * userName : 王杰
         */

        public String address;
        public String area;
        public String createTime;
        public int id;
        public int isDefault;
        public String modifyTime;
        public String phone;
        public int userId;
        public String userName;
    }
}

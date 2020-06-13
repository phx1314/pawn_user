package com.glavesoft.pawnuser.model;

import java.io.Serializable;

public class ModelCw implements Serializable {


    /**
     * total : 0.00
     * toBeDelivered : 0.10
     * toBeHarvested : 0.20
     * toBePai : 0
     * remark : toBePai:待付款,toBeDelivered:待发货,toBeHarvested:待收货,total:总收入
     */

    public String total;
    public String toBeDelivered;
    public String toBeHarvested;
    public String toBePai;
    public String remark;
}

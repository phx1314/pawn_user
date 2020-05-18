package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/12/28
 * @company:常州宝丰
 */
public class PawnVideoInfo implements Serializable{
    private String authPrice;//鉴定价
    private String code;//当号
    private String goVideo;//打包视频
    private String goodName;//宝贝名字
    private String id;
    private String img;//图片
    private String openVideo;//拆箱视频
    private String orgName;//机构名称
    private String platGoodsAuthVideo;//鉴定视频
    private String time;//时间

    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGoVideo() {
        return goVideo;
    }

    public void setGoVideo(String goVideo) {
        this.goVideo = goVideo;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
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

    public String getOpenVideo() {
        return openVideo;
    }

    public void setOpenVideo(String openVideo) {
        this.openVideo = openVideo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPlatGoodsAuthVideo() {
        return platGoodsAuthVideo;
    }

    public void setPlatGoodsAuthVideo(String platGoodsAuthVideo) {
        this.platGoodsAuthVideo = platGoodsAuthVideo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

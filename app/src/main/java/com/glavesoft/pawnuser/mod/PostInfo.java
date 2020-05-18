package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/11/10
 * @company:常州宝丰
 */
public class PostInfo {

    private String backState;//物流状态(0默认1申请取回2取回中3确认收货---平台取回
    private String id;//id
    private String images;//图片
    private String name;//宝贝名称
    private String postExpressCode;//快递单号
    private String postState;//物流状态(1未邮寄2邮寄中3平台确认)---寄往平台

    public String getBackState() {
        return backState;
    }

    public void setBackState(String backState) {
        this.backState = backState;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostExpressCode() {
        return postExpressCode;
    }

    public void setPostExpressCode(String postExpressCode) {
        this.postExpressCode = postExpressCode;
    }

    public String getPostState() {
        return postState;
    }

    public void setPostState(String postState) {
        this.postState = postState;
    }

}

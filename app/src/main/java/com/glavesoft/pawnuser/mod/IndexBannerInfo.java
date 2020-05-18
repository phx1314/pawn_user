package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/12/11
 * @company:常州宝丰
 */
public class IndexBannerInfo {
    private String id;
    private String index_images;//首页轮播图片
    private String pawnMsg;//首页拍档头条
    private String state;//类型1 首页轮播图 2拍档头条
    private String content;//内容 null、跳转url、富文本内容、商品id、视频id
    private String type;//类型 0不跳转；1网址；2富文本；3认证商城商品详情页；4绝当商城商品详情页；5视频详情页
    private String cstate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndex_images() {
        return index_images;
    }

    public void setIndex_images(String index_images) {
        this.index_images = index_images;
    }

    public String getPawnMsg() {
        return pawnMsg;
    }

    public void setPawnMsg(String pawnMsg) {
        this.pawnMsg = pawnMsg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCstate() {
        return cstate;
    }

    public void setCstate(String cstate) {
        this.cstate = cstate;
    }
}

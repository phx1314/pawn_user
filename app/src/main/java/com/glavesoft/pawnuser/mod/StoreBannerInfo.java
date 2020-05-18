package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/11/22
 * @company:常州宝丰
 */
public class StoreBannerInfo {

    private String content;//内容 null、跳转url、富文本内容、商品id、视频id	string
    private String id;//图片id	integer/int32
    private String type;//类型 0不跳转；1网址；2富文本；3认证商城商品详情页；4绝当商城商品详情页；5视频详情页	integer/int32
    private String url;//图片连接
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

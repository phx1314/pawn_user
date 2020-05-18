package com.glavesoft.pawnuser.mod;

public class LoadingImgInfo {
    private String img;//图片	string
    private String seconds;//持续时间，单位:s	int32
    private String url;//跳转

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

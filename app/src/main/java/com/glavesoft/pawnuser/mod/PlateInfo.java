package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/8/29
 * @company:常州宝丰
 */
public class PlateInfo {
    private String contentType;//可以发布的类型0全部1图文2图片3视频
    private int id;//
    private String name;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

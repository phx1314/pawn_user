package com.glavesoft.pawnuser.mod;

import java.util.ArrayList;

/**
 * @author 严光
 * @date: 2017/8/29
 * @company:常州宝丰
 */
public class SendNewsInfo {

    private int num;
    private String content;
    private ArrayList<String> imgs;
    private ArrayList<String> imgpaths;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public ArrayList<String> getImgpaths() {
        return imgpaths;
    }

    public void setImgpaths(ArrayList<String> imgpaths) {
        this.imgpaths = imgpaths;
    }
}

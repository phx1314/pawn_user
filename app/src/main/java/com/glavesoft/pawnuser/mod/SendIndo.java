package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/8/30
 * @company:常州宝丰
 */
public class SendIndo implements Serializable{
    private String content;
    private String img;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgs() {
        return img;
    }

    public void setImgs(String imgs) {
        this.img = imgs;
    }
}

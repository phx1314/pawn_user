package com.glavesoft.pawnuser.mod;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Sinyu on 2018/7/31.
 */


public class CommentsInfo implements MultiItemEntity {

    private int type;
    private String head;
    private String user;
    private String desc;
    private String time;
    private String choose;
    private String[] imgs;
    public CommentsInfo() {
    }

    public CommentsInfo(int type, String head, String user, String desc, String time, String choose, String[] imgs) {
        this.type = type;
        this.head = head;
        this.user = user;
        this.desc = desc;
        this.time = time;
        this.choose = choose;
        this.imgs = imgs;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }
}

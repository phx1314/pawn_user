package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/8/3
 */
public class MessageInfo implements Serializable{

    private String content;//内容
    private String id;
    private String isRead;//是否已读 0:未读 1:已读
    private String nid;//群发id
    private String redirectType;//0:不跳转 1:典当成功通知 2续当到期通知 3赎当通知 4赎当邮寄通知 5邮寄通知 6到款通知
    private String title;//标题
    private String type;//类型(1系统通知)
    private String user_id;//用户id
    private String createTime;//时间


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

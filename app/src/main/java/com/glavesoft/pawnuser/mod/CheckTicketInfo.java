package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/12/5
 * @company:常州宝丰
 */
public class CheckTicketInfo {
    private String id;
    private String state;//类型 1机构打款凭证 2续当打款凭证 3赎当打款凭证
    private String ticket;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}

package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/10/23
 * @company:常州宝丰
 */
public class AuctionInfo {

    private String name;
    private String url;
    public long duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

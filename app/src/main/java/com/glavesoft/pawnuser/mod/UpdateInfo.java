package com.glavesoft.pawnuser.mod;

/**
 * 更新文件数据结构
 *
 * @author gloria
 * @since 2013-1-21
 */
public class UpdateInfo {

    private String info;
    public int isFlag;    //1强制更新0不强制
    private String url;    //下载地址	string
    public String apkFile;    //下载地址	string
    public int version;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

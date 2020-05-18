package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/5.
 */

public class ImageInfo implements Serializable
{
    private String file = "";
    private int size;
    private String url = "";
    private String path="";
    private String id="";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile()
    {
        return file;
    }

    public void setFile(String file)
    {
        this.file = file;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}

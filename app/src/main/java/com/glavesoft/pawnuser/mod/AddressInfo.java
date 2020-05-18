package com.glavesoft.pawnuser.mod;

import java.io.Serializable;

/**
 * @author 严光
 * @date: 2017/11/1
 * @company:常州宝丰
 */
public class AddressInfo implements Serializable{

    private String address;//详细地址
    private String area;//地区
    private String id;
    private String is_default;//是否默认1默认0否
    private String name;//姓名
    private String phone;//联系电话

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

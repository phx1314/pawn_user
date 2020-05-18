package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2018/1/30
 * @company:常州宝丰
 */
public class ContractInfo {
    private String code;//当号
    private String id;
    private String img;//封面
    private String pawnMoney;//当款
    private String title;//名字
    private String verifyTime;//签订时间

    private String type;//类型(1典当合同 2续当合同)

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPawnMoney() {
        return pawnMoney;
    }

    public void setPawnMoney(String pawnMoney) {
        this.pawnMoney = pawnMoney;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

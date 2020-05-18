package com.glavesoft.pawnuser.mod;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sinyu on 2018/7/26.
 */

public class ShopCar implements Serializable {

    /**
     * goods : [{"createTime":"2018-07-26 18:09:21","goodsId":819,
     * "goodsImg":"fd8f580a903b416aa3a78136293efe54","goodsInfo":"绝当品宝祥编号：Z027B<br
     * />\n尺寸长（内径）：56.97mm<br />\n质量：47.76g<br />\n<img
     * src=\"/paidangAdmin/download?id=d037127f304d45a7862f8dc07f758b00\" alt=\"\" /><img
     * src=\"/paidangAdmin/download?id=810a1290884844069b1043e8ab6d2b38\" alt=\"\" />",
     * "goodsName":"天然翡翠手镯","goodsPrice":1000,"goodsTotal":1,"id":7,"isOnline":1,
     * "modifyTime":null,"num":1,"orgId":0,"orgLogo":"","orgName":"","userId":0},
     * {"createTime":"2018-07-26 18:24:34","goodsId":849,"goodsImg":"",
     * "goodsInfo":"<p>\n\t超低价\n<\/p>","goodsName":"zhu_testing1","goodsPrice":0.01,
     * "goodsTotal":1,"id":8,"isOnline":1,"modifyTime":null,"num":1,"orgId":0,"orgLogo":"",
     * "orgName":"","userId":0},{"createTime":"2018-07-26 18:24:42","goodsId":846,
     * "goodsImg":"76c3fbf0b05243959fbb131126d7baa2","goodsInfo":"","goodsName":"测试用",
     * "goodsPrice":100,"goodsTotal":1,"id":9,"isOnline":1,"modifyTime":null,"num":1,"orgId":0,
     * "orgLogo":"","orgName":"","userId":0}]
     * orgId : 0
     * orgLogo :
     * orgName :
     */

    private int orgId;
    private String orgLogo;
    private String orgName;
    private List<GoodsBean> goods;
    /** 是否处于编辑状态 */
    private boolean isEditing;
    /** 组是否被选中 */
    private boolean isGroupSelected;

    /** 是否失效列表 */
    private boolean isInvalidList;

    private boolean isAllGoodsInvalid;
    public boolean isAllGoodsInvalid() {
        return isAllGoodsInvalid;
    }

    public void setIsAllGoodsInvalid(boolean isAllGoodsInvalid) {
        this.isAllGoodsInvalid = isAllGoodsInvalid;
    }

    public boolean isInvalidList() {
        return isInvalidList;
    }

    public void setIsInvalidList(boolean isInvalidList) {
        this.isInvalidList = isInvalidList;
    }
    public boolean isEditing() {
        return isEditing;
    }

    public boolean isGroupSelected() {
        return isGroupSelected;
    }

    public void setIsGroupSelected(boolean isGroupSelected) {
        this.isGroupSelected = isGroupSelected;
    }
    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgLogo() {
        return orgLogo;
    }

    public void setOrgLogo(String orgLogo) {
        this.orgLogo = orgLogo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean implements Serializable{
        /**
         * createTime : 2018-07-26 18:09:21
         * goodsId : 819
         * goodsImg : fd8f580a903b416aa3a78136293efe54
         * goodsInfo : 绝当品宝祥编号：Z027B<br />
         尺寸长（内径）：56.97mm<br />
         质量：47.76g<br />
         <img src="/paidangAdmin/download?id=d037127f304d45a7862f8dc07f758b00" alt="" /><img
         src="/paidangAdmin/download?id=810a1290884844069b1043e8ab6d2b38" alt="" />
         * goodsName : 天然翡翠手镯
         * goodsPrice : 1000.0
         * goodsTotal : 1
         * id : 7
         * isOnline : 1
         * modifyTime : null
         * num : 1
         * orgId : 0
         * orgLogo :
         * orgName :
         * userId : 0
         */
        private boolean isChildSelected;
        private String createTime;
        private int goodsId;
        private String goodsImg;
        private String goodsInfo;
        private String goodsName;
        private double goodsPrice;
        private int goodsTotal;
        private int id;
        private int isOnline;
        private Object modifyTime;
        private int num;
        private int orgId;
        private String orgLogo;
        private String orgName;
        private int userId;
        /** 是否是编辑状态 */
        private boolean isEditing;
        public boolean isEditing() {
            return isEditing;
        }

        public void setIsEditing(boolean isEditing) {
            this.isEditing = isEditing;
        }
        public boolean isChildSelected() {
            return isChildSelected;
        }

        public void setIsChildSelected(boolean isChildSelected) {
            this.isChildSelected = isChildSelected;
        }
        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }

        public String getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(String goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public double getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(double goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public int getGoodsTotal() {
            return goodsTotal;
        }

        public void setGoodsTotal(int goodsTotal) {
            this.goodsTotal = goodsTotal;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(int isOnline) {
            this.isOnline = isOnline;
        }

        public Object getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(Object modifyTime) {
            this.modifyTime = modifyTime;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getOrgId() {
            return orgId;
        }

        public void setOrgId(int orgId) {
            this.orgId = orgId;
        }

        public String getOrgLogo() {
            return orgLogo;
        }

        public void setOrgLogo(String orgLogo) {
            this.orgLogo = orgLogo;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}

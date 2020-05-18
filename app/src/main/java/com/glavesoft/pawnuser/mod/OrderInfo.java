package com.glavesoft.pawnuser.mod;

/**
 * @author 严光
 * @date: 2017/12/6
 * @company:常州宝丰
 */
public class OrderInfo {

    private String commentState="";//评价状态
    private String authPrice;//商品鉴定价	string
    private String couponPrice;//优惠券减免金额	string
    private String declare;//购买声明	string
    private String description;//市场预估文本	string
    private String expressId;
    private String expressState;//物流状态0：在途1：揽件2：疑难3：签收4：退签或异常签收5：派件6：退回	integer/int32
    private String goodsDescription;//商品描述	string
    private String goodsName;//商品名称	string
    private String goodsId;
    private String id;
    private String images;//商品封面	string
    private String isBuy;//是否已经购买0未购买 1已购买	integer/int32
    private String orderCode;//商品订单号	string
    private String postAddress;//快递公司名称	string
    private String postCode;//快递公司单号	string
    private String price;//商品售价	string
    private String state;//订单状态-1已取消1待付款2已付款3已发货4确认收货5退款
    private String goodsType;//商品类型 1认证商品 2绝当商品
    private String refState;//退款状态 0未退款 1申请退款 2同意退款 3已退款 4拒绝退款
    private String refundNotVerifyReason;//拒绝原因
    private String isSell="";
    private String userGoodsId="";

    public String getUserGoodsId() {
        return userGoodsId;
    }

    public void setUserGoodsId(String userGoodsId) {
        this.userGoodsId = userGoodsId;
    }

    public String getIsSell() {
        return isSell;
    }

    public void setIsSell(String isSell) {
        this.isSell = isSell;
    }

    public String getCommentState() {
        return commentState;
    }

    public void setCommentState(String commentState) {
        this.commentState = commentState;
    }
    public String getAuthPrice() {
        return authPrice;
    }

    public void setAuthPrice(String authPrice) {
        this.authPrice = authPrice;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getDeclare() {
        return declare;
    }

    public void setDeclare(String declare) {
        this.declare = declare;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getExpressState() {
        return expressState;
    }

    public void setExpressState(String expressState) {
        this.expressState = expressState;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getRefState() {
        return refState;
    }

    public void setRefState(String refState) {
        this.refState = refState;
    }

    public String getRefundNotVerifyReason() {
        return refundNotVerifyReason;
    }

    public void setRefundNotVerifyReason(String refundNotVerifyReason) {
        this.refundNotVerifyReason = refundNotVerifyReason;
    }
}

package com.glavesoft.pawnuser.mod;

/**
 * Created by Sinyu on 2019/1/9.
 */

public class SimplePageInfo {

    /**
     * code : about
     * content :
     * 千呼万唤始出来，支付宝2018
     * 年度账单今天终于出炉了。面对账单，朋友圈里是这样的，“我哪来这么多钱”、“我啥时候花了这么多钱”、“失忆式消费”、“该还花呗了”，...，于是乎催生了今天的热门词汇「账单式小康」，也登上了微博热搜榜单。<br />
     而除了2018年度账单之外，今天和「支付宝」有关的还有一件大事。<br />
     新浪财经报道，据启信宝信息显示，支付宝（中国）信息技术有限公司于2018年12月18日更名为瀚宝（上海）信息技术有限公司。此前2018年8月29
     日，该公司法人代表由马云变更为叶郁青。<br />
     网友表示，“我花呗是欠的马云的，凭啥要还给叶郁青！”<br />
     后来，支付宝官方回应了：<img src="/paidang-admin/download?id=adcf0112272e4d3096d4e49c422a79b6"
     width="300" height="300" alt="" />
     * isShow : 1
     * remark : 关于我们
     * sortOrder : 10
     */

    private String code;
    private String content;
    private int isShow;
    private String remark;
    private int sortOrder;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}

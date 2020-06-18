//
//  FrgDingdanDetailShz
//
//  Created by 86139 on 2020-06-03 11:06:01
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;
import android.text.Html

import com.glavesoft.pawnuser.R;

import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout
import com.glavesoft.F
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.pawnuser.model.ModelDingdan
import com.glavesoft.pawnuser.model.ModelDingdanDetail
import com.glavesoft.util.GlideLoader
import kotlinx.android.synthetic.main.frg_dingdan_detail_shz.*
import kotlinx.android.synthetic.main.item_dingdan.view.*


class FrgDingdanDetailShz : BaseFrg() {
    lateinit var code: String
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_dingdan_detail_shz)
        code = activity!!.intent.getStringExtra("code")
    }

    override fun initView() {
    }

    override fun loaddata() {
        load(
            F.gB().info(
                code
            ), "info"
        )
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "info") {
            var item = F.data2Model(data, ModelDingdanDetail::class.java)
            mTextView_price.text =
                Html.fromHtml("退款金额：  <font color='#B42929'>" + item.price + "</font>")
            mTextView_name.text = item.backUser
            mTextView_phone.text = item.backPhone
            mTextView_adress.text = item.backAddress
            mTextView_name2.text = item.shipUser
            mTextView_phone2.text = item.shipPhone
            mTextView_adress2.text = item.shipAddress
            GlideLoader.loadImage(BaseConstant.Image_URL + item.goodsImg, mImageView, R.drawable.defalut_logo)
            mTextView_pname.text = item.goodsName
            mTextView_pprice.text = item.goodsPrice
            mTextView_ddh.text = "订单号： " + item.code
//            退款状态 0未退款 1申请退款 2同意退款 3提交单号 4已退款 5拒绝退款
            var state: String = ""
            when (item.refState) {
                2 -> {
                    state = "同意退款"
                }
                3 -> {
                    state = "提交单号"
                }
                4 -> {
                    state = "已退款"
                }
                5 -> {
                    state = "拒绝退款"
                }
            }

            mTextView_ddstate.text =
                Html.fromHtml("订单状态：  <font color='#B42929'>$state</font>")
            mTextView_ddtime.text = "订单时间： " + item.createTime
            mTextView_wldh.text = "物流单号：  " + item.shipCode
            mTextView_wlgs.text = "物流公司：  " + item.shipFirm
        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("订单详情", "O R D E R   D E T A I L S");
    }
}
//
//  FrgDingdanDetail
//
//  Created by 86139 on 2020-06-11 15:22:19
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.app.Dialog
import android.os.Bundle;
import android.text.Html
import android.text.TextUtils
import android.view.View

import com.glavesoft.pawnuser.R;

import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ImageButton;
import com.glavesoft.F
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.pawnuser.item.DialogPub
import com.glavesoft.pawnuser.model.ModelDingdanDetail
import com.glavesoft.util.GlideLoader
import com.mdx.framework.view.CallBackOnly
import kotlinx.android.synthetic.main.frg_dingdan_detail.*
import kotlinx.android.synthetic.main.frg_dingdan_detail.mImageView
import kotlinx.android.synthetic.main.frg_dingdan_detail.mTextView_adress
import kotlinx.android.synthetic.main.frg_dingdan_detail.mTextView_ddh
import kotlinx.android.synthetic.main.frg_dingdan_detail.mTextView_ddstate
import kotlinx.android.synthetic.main.frg_dingdan_detail.mTextView_ddtime
import kotlinx.android.synthetic.main.frg_dingdan_detail.mTextView_name
import kotlinx.android.synthetic.main.frg_dingdan_detail.mTextView_phone
import kotlinx.android.synthetic.main.frg_dingdan_detail.mTextView_pname
import kotlinx.android.synthetic.main.frg_dingdan_detail.mTextView_price


class FrgDingdanDetail : BaseFrg() {
    lateinit var code: String
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_dingdan_detail)
        code = activity!!.intent.getStringExtra("code")
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        when (type) {
            0 -> {
                finish()
            }
        }

    }

    override fun initView() {
        mImageButton_sub.setOnClickListener {
            if (TextUtils.isEmpty(mTextView_wldh.text.toString())) {
                F.toast("请输入物流单号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mTextView_wlgs.text.toString())) {
                F.toast("请输入物流公司")
                return@setOnClickListener
            }
            load(
                F.gB().experter(
                    mTextView_wlgs.text.toString(), mTextView_wldh.text.toString(),
                    code
                ), "experter"
            )
        }
        mButton_ty.setOnClickListener {
            load(
                F.gB().orderReturn("2", "", code),
                "orderReturn"
            )
        }
        mButton_bh.setOnClickListener {
            var mDialogPub = DialogPub(context)
            com.mdx.framework.F.showCenterDialog(context, mDialogPub, object : CallBackOnly() {
                override fun goReturnDo(mDialog: Dialog) {
                    mDialogPub.set(mDialog, code)
                }
            })
        }
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
//            0已取消1待付款2已付款3已发货4确认收货5已评价
            var state: String = ""
            when (item.state) {
                0 -> {
                    state = "已取消"
                }
                1 -> {
                    state = "待付款"
                }
                2 -> {
                    state = "已付款"
                    mLinearLayout_wl.visibility = View.VISIBLE
                    mLinearLayout_bottom.visibility = View.VISIBLE
                    mTextView_wldh.isEnabled = true
                    mTextView_wlgs.isEnabled = true
                }
                3 -> {
                    state = "已发货"
                    mLinearLayout_wl.visibility = View.VISIBLE
                }
                4 -> {
                    state = "确认收货"
                }
                5 -> {
                    state = "已评价"
                }
            }

            when (item.refState) {
                1 -> {
                    state = "售后中"
                    mLinearLayout_wl.visibility = View.VISIBLE
                    mLinearLayout_bottom.visibility = View.VISIBLE
                    mButton_ty.visibility = View.VISIBLE
                    mButton_bh.visibility = View.VISIBLE
                    mImageButton_sub.visibility = View.GONE
                }

            }


            mTextView_name.text = item.shipUser
            mTextView_phone.text = item.shipPhone
            mTextView_adress.text = item.shipAddress
            GlideLoader.loadImage(BaseConstant.Image_URL + item.goodsImg, mImageView, R.drawable.defalut_logo)
            mTextView_pname.text = item.goodsName
            mTextView_price.text = item.goodsPrice
            mTextView_ddh.text = "订单号： " + item.code
            mTextView_ddstate.text =
                Html.fromHtml("订单状态：  <font color='#B42929'>$state</font>")
            mTextView_ddtime.text = "订单时间： " + item.createTime
            mTextView_wldh.setText(item.shipCode)
            mTextView_wlgs.setText(item.shipFirm)
        } else if (method == "experter") {
            F.toast("提交成功")
        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("订单详情", "O R D E R   D E T A I L S");
    }
}
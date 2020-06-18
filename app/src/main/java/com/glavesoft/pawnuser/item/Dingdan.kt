//
//  Dingdan
//
//  Created by 86139 on 2020-05-30 14:39:49
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.item;

import com.glavesoft.pawnuser.R;

import android.annotation.SuppressLint;
import android.app.Dialog
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide
import com.glavesoft.F
import com.glavesoft.pawnuser.constant.BaseConstant.Image_URL
import com.glavesoft.pawnuser.model.ModelDingdan
import com.glavesoft.util.GlideLoader
import com.mdx.framework.Frame
import com.mdx.framework.view.CallBackOnly
import kotlinx.android.synthetic.main.item_dingdan.view.*


class Dingdan(context: Context?) : BaseItem(context) {
    lateinit var item: ModelDingdan.RowsBean

    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_dingdan, this)


        mTextView_green.setOnClickListener {
            load(F.gB().orderReturn("2", "", item.code), "orderReturn")
        }
        mTextView_red.setOnClickListener {
            if (mTextView_red.text.toString() == "驳回") {
                var mDialogPub = DialogPub(context)
                com.mdx.framework.F.showCenterDialog(context, mDialogPub, object : CallBackOnly() {
                    override fun goReturnDo(mDialog: Dialog) {
                        mDialogPub.set(mDialog, item.code)
                    }
                })
            } else if (mTextView_red.text.toString() == "填写物流") {
                var mDialogTxwul = DialogTxwul(context)
                com.mdx.framework.F.showCenterDialog(
                    context,
                    mDialogTxwul,
                    object : CallBackOnly() {
                        override fun goReturnDo(mDialog: Dialog) {
                            mDialogTxwul.set(mDialog, item.code)
                        }
                    })
            }
        }
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "orderReturn") {
            Frame.HANDLES.sentAll("4", 0, "")
        }
    }

    fun set(item: ModelDingdan.RowsBean, state: String) {
        this.item = item
        GlideLoader.loadImage(Image_URL + item.goodsImg, mImageView, R.drawable.defalut_logo)
        mTextView_name.text = item.goodsName
        mTextView_order.text = "订单号:" + item.code
        mTextView_price.text = item.goodsPrice
        when (state) {
            "1" -> {
                mTextView_state.text = "待付款"
                mTextView_green.visibility = View.GONE
                mTextView_red.visibility = View.GONE
            }
            "2" -> {
                mTextView_state.text = "待发货"
                mTextView_green.visibility = View.GONE
                mTextView_red.visibility = View.VISIBLE
                mTextView_red.text = "填写物流"
            }
            "3" -> {
                mTextView_state.text = "待收货"
                mTextView_green.visibility = View.GONE
                mTextView_red.visibility = View.VISIBLE
                mTextView_red.text = "用户已确认"
            }
            "4" -> {
                mTextView_state.text = "售后"
                if (item.refState == 1) {
                    mTextView_green.visibility = View.VISIBLE
                    mTextView_red.visibility = View.VISIBLE
                    mTextView_red.text = "驳回"
                } else {
                    mTextView_green.visibility = View.GONE
                    mTextView_red.visibility = View.GONE
                }
            }

        }

    }

}
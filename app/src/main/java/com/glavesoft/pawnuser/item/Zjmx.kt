//
//  Zjmx
//
//  Created by 86139 on 2020-06-03 15:17:58
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.item;

import com.glavesoft.pawnuser.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.widget.TextView;
import com.glavesoft.pawnuser.model.ModelZjmx
import kotlinx.android.synthetic.main.item_zjmx.view.*


class Zjmx(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_zjmx, this)
    }

    fun set(item: ModelZjmx) {
//        0 余额1支付宝2微信10线下银行卡
        if (item.type.toInt() == 1) {//增加
            mTextView_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ruz, 0, 0, 0)
        } else if (item.type.toInt() == 2) {
            mTextView_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tx2, 0, 0, 0)
        }
        mTextView_1.text = "提现金额：￥" + item.amount
        var type = ""
        when (item.tradeType.toInt()) {
            0 -> type = "余额"
            1 -> type = "支付宝"
            2 -> type = "微信"
            10 -> type = "线下银行卡"
        }
        mTextView_2.text = "提现方式：$type"
        mTextView_3.text = "提现流水号：" + item.tradeNo
        mTextView_4.text = item.createTime
    }

}
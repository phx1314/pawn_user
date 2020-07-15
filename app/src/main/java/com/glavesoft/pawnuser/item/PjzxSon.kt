//
//  PjzxSon
//
//  Created by 86139 on 2020-06-18 11:08:18
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.item;

import com.glavesoft.pawnuser.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;
import com.glavesoft.F
import com.glavesoft.pawnuser.frg.FrgHk
import com.glavesoft.pawnuser.frg.FrgPtDetail
import com.glavesoft.pawnuser.model.ModelDp
import com.glavesoft.pawnuser.model.ModelPjDetail
import com.glavesoft.pawnuser.model.ModelUpload
import com.mdx.framework.Frame
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.AbDateUtil
import com.mdx.framework.utility.AbDateUtil.dateFormatYMD
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.item_pjzx_son.view.*


class PjzxSon(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_pjzx_son, this)

    }

    fun set(item: ModelDp) {
        mLinearLayout.setOnClickListener {
            if (!TextUtils.isEmpty(item.contractId)) {
                load(F.gB().showContract(item.id.toString()), "showContract")
            }
        }
        mTextView_title.text = "典当行名称：" + item.orgName
        mTextView_name.text = item.goodsName
        mTextView_price.text = "￥" + item.loanMoneyCN
        mTextView_state.text = if (item.type == "1") "典当" else "续当"
        mTextView_yfv.text = item.rate + "%"
        mTextView_ylv.text = item.moneyRate + "%"
        mTextView_zdrq.text = if (item.type == "1") item.pawnBeginTime else item.repawnBeginTime
        mTextView_qsrq.text = AbDateUtil.formatDateStr2Desc(item.signTime ?: "", dateFormatYMD)
        mTextView_qsrq.setTextColor(Color.parseColor("#000000"))
        mLinearLayout_qsrq.visibility = View.VISIBLE
        mTextView_qs.visibility = View.GONE
        if (!TextUtils.isEmpty(item.status)) {
            if (item.status == "0") {//未生成合同
                mTextView_qs.visibility = View.VISIBLE
                mLinearLayout_qsrq.visibility = View.GONE
            } else if (item.status == "1") {//待签署
                mLinearLayout_qsrq.visibility = View.GONE
            } else if (item.status == "2") {//2已签署
            } else if (item.status == "3") {//拒签
                mTextView_qsrq.text = "拒签"
                mTextView_qsrq.setTextColor(Color.parseColor("#AB1414"))
            } else if (item.status == "4") {//作废
                mTextView_qsrq.text = "作废"
                mTextView_qsrq.setTextColor(Color.parseColor("#AB1414"))
            }
        }
        mTextView_qs.setOnClickListener {
            load(F.gB().signPawnTicket(item.id.toString()), "signPawnTicket")
        }
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "signPawnTicket") {
            Frame.HANDLES.sentAll("FrgPjzxSon", 0, "")
        } else if (method == "showContract") {
            var mModelPjDetail = F.data2Model(data, ModelPjDetail::class.java)
            Helper.startActivity(
                context,
                FrgPtDetail::class.java,
                TitleAct::class.java,
                "url",
                mModelPjDetail.pageUrl ?: ""
            )
        }
    }
}
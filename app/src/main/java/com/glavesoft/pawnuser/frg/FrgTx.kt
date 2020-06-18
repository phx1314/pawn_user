//
//  FrgTx
//
//  Created by 86139 on 2020-06-03 15:30:52
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle
import android.text.TextUtils
import android.widget.LinearLayout
import com.alipay.api.AlipayClient
import com.alipay.api.DefaultAlipayClient
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse
import com.glavesoft.F
import com.glavesoft.alipay.AliPay.*
import com.glavesoft.pawnuser.R
import com.glavesoft.pawnuser.model.ModelCw
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_tx.*
import okhttp3.internal.http2.Http2Reader.Companion.logger


class FrgTx : BaseFrg() {
    lateinit var item: ModelCw
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_tx)
    }

    override fun initView() {
        mImageButton.setOnClickListener {
            if (TextUtils.isEmpty(mEditText.text.toString())) {
                Helper.toast("请输入提现金额")
            }
            if (mEditText.text.toString().toDouble() > item.total.toDouble()) {
                Helper.toast("提现金额超出余额")
            }
        }
    }

    fun zfbTx() {
        val alipayClient: AlipayClient = DefaultAlipayClient(
            "https://openapi.alipay.com/gateway.do",
            APPID,
            RSA2_PRIVATE,
            "json",
            "GBK",
            RSA2_PUBLIC,
            "RSA2"
        )
        val request = AlipayFundTransToaccountTransferRequest()
        val map: MutableMap<String, String> =
            LinkedHashMap()
        map["out_biz_no"] = "3142321423432"
        map["payee_type"] = "ALIPAY_LOGONID"
        map["payee_account"] = "1163096519@qq.com"
        map["amount"] = "0.01"
        map["payer_show_name"] = "蚌蚌拍档平台用户提现"
        map["payee_real_name"] = "戴飞"
        map["remark"] = "您的提现已转出请查收。"
        request.bizContent = map.toString()
        val response: AlipayFundTransToaccountTransferResponse = alipayClient.execute(request)
        if ("10000" == response.getCode()) { //提现成功,本地业务逻辑略
        } else { // 支付宝提现失败，本地业务逻辑略
            logger.info("调用支付宝提现接口成功,但提现失败")
        }
    }

    override fun loaddata() {
        load(
            F.gB().financeinfo(),
            "financeinfo"
        )
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "financeinfo") {
            item = F.data2Model(data, ModelCw::class.java)
            mTextView_ye.text = "余额 ¥" + item.total
        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("提现", "C A S H   O U T");
    }

}
//
//  FrgCwzx
//
//  Created by 86139 on 2020-06-03 14:27:18
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;

import com.glavesoft.pawnuser.R;

import android.widget.Button;
import android.widget.LinearLayout
import android.widget.TextView;
import com.glavesoft.F
import com.glavesoft.pawnuser.model.ModelCw
import com.glavesoft.pawnuser.model.ModelDingdanDetail
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_cwzx.*


class FrgCwzx : BaseFrg() {

    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_cwzx)
    }

    override fun initView() {
        mButton_mx.setOnClickListener {
            Helper.startActivity(context, FrgZjmx::class.java, TitleAct::class.java)
        }
        mTextView_hk.setOnClickListener {
            Helper.startActivity(context, FrgHk::class.java, TitleAct::class.java)
        }
        mButton_tx.setOnClickListener {
            Helper.startActivity(context, FrgTx::class.java, TitleAct::class.java)
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
            var item = F.data2Model(data, ModelCw::class.java)
            mTextView_price.text = "￥" + item.total
        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("财务中心", "F I N A N C I A L   C E N T E R");
    }
}
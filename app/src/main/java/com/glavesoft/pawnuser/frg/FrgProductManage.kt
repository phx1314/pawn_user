//
//  FrgProductManage
//
//  Created by 86139 on 2020-06-04 15:23:23
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;

import com.glavesoft.pawnuser.R;

import android.widget.LinearLayout;
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_product_manage.*


class FrgProductManage : BaseFrg() {

    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_product_manage)
    }

    override fun initView() {
        mLinearLayout_1.setOnClickListener {
            Helper.startActivity(context, FrgProductList::class.java, TitleAct::class.java)
        }
        mLinearLayout_2.setOnClickListener {
            Helper.startActivity(context, FrgAddProductList::class.java, TitleAct::class.java)
        }
    }

    override fun loaddata() {
    }

    override fun onSuccess(data: String?, method: String) {
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("商品管理", "C O M M O D I T Y   M A N A G E M E N T");
    }
}
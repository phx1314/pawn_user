//
//  FrgShangjiaduan
//
//  Created by 86139 on 2020-06-02 19:21:58
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;

import com.glavesoft.pawnuser.R;

import android.widget.ImageView;
import android.widget.LinearLayout
import cn.sharesdk.framework.TitleLayout
import com.glavesoft.F
import com.guoxiaoxing.phoenix.core.common.PhoenixConstant
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.DelayClickListener
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_shangjiaduan.*


class FrgShangjiaduan : BaseFrg() {

    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_shangjiaduan)
    }

    override fun initView() {
        mImageView_spgl.setOnClickListener(DelayClickListener {
//            Helper.startActivity(context, FrgAddProduct::class.java, TitleAct::class.java)
            Helper.startActivity(context, FrgProductManage::class.java, TitleAct::class.java)
        })
        mImageView_ddgl.setOnClickListener(DelayClickListener {
            Helper.startActivity(context, FrgDingdanManage::class.java, TitleAct::class.java)
        })
        mImageView_cwgl.setOnClickListener(DelayClickListener {
            Helper.startActivity(context, FrgCwzx::class.java, TitleAct::class.java)
        })
        mImageView_person.setOnClickListener(DelayClickListener {
            Helper.startActivity(context, FrgGerenziliao::class.java, TitleAct::class.java)
        })
    }

    override fun loaddata() {
    }

    override fun onSuccess(data: String?, method: String) {
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("商家端", "B U S I N E S S   E N D");
    }
}
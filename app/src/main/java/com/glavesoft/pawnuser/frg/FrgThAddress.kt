//
//  FrgThAddress
//
//  Created by 86139 on 2020-06-03 09:36:31
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;
import android.text.TextUtils

import com.glavesoft.pawnuser.R;

import com.glavesoft.util.PListView;
import android.widget.ImageView;
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.glavesoft.F
import com.glavesoft.pawnuser.ada.AdaThAddress
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.pawnuser.model.ModelGrzl
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_geren_renzheng.*
import kotlinx.android.synthetic.main.frg_geren_renzheng.mImageView
import kotlinx.android.synthetic.main.frg_gerenziliao.*
import kotlinx.android.synthetic.main.frg_th_address.*


class FrgThAddress : BaseFrg() {
    lateinit var mModelGrzl: ModelGrzl
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_th_address)

    }

    override fun initView() {
        mImageView_add.setOnClickListener {
            Helper.startActivity(
                context,
                FrgEditThAddress::class.java,
                TitleAct::class.java, "item", ModelGrzl.ReturnAddressBean()
            )
        }
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        when (type) {
            0 -> {
                load(F.gB().userInfo(), "userInfo")
            }
            1 -> {
                load(F.gB().deleteReturnAddress(obj.toString()), "deleteReturnAddress")
            }
        }

    }

    override fun loaddata() {
        load(F.gB().userInfo(), "userInfo")
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "userInfo") {
            mModelGrzl = F.data2Model(data, ModelGrzl::class.java)
            mAbPullListView.adapter = AdaThAddress(context!!, mModelGrzl.returnAddress)
            for (it in mModelGrzl.returnAddress!!) {
                if (it.isDefault == 1) {
                    mTextView_th_address.text = it.area + it.address
                    break
                }
            }
        } else if (method == "deleteReturnAddress") {
            F.toast("删除成功")
            load(F.gB().userInfo(), "userInfo")
        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("退货地址", "R E T U R N   A D D R E S S");
    }
}
//
//  FrgGerenziliao
//
//  Created by 86139 on 2020-06-03 09:29:12
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;

import com.glavesoft.pawnuser.R;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.glavesoft.F
import com.glavesoft.F.data2Model
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.pawnuser.constant.BaseConstant.Image_URL
import com.glavesoft.pawnuser.model.ModelGrzl
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_geren_renzheng.*
import kotlinx.android.synthetic.main.frg_geren_renzheng.mImageView
import kotlinx.android.synthetic.main.frg_gerenziliao.*


class FrgGerenziliao : BaseFrg() {
    var mModelGrzl: ModelGrzl? = null
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_gerenziliao)
    }

    override fun initView() {
        mLinearLayout_address.setOnClickListener {
            Helper.startActivity(
                context,
                FrgThAddress::class.java,
                TitleAct::class.java
            )
        }
    }

    override fun loaddata() {
        load(F.gB().userInfo(), "userInfo")
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "userInfo") {
            mModelGrzl = data2Model(data, ModelGrzl::class.java)
            Glide.with(this)
                .load(Image_URL + mModelGrzl?.headImg)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(mImageView)
            mTextView_name.text = mModelGrzl?.store
//            mTextView_sign.text=mModelGrzl?.store
            mTextView_lxr.text = mModelGrzl?.name
            mTextView_phone.text = mModelGrzl?.phone
            for (it in mModelGrzl?.returnAddress!!) {
                if (it.isDefault == 1) {
                    mTextView_th_address.text = it.area + it.address
                    break
                }
            }

        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("个人资料", "P E R S O N A L   D A T A");
    }
}
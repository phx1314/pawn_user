//
//  FrgAddProduct
//
//  Created by 86139 on 2020-06-04 19:48:42
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle
import android.text.TextUtils
import android.widget.LinearLayout
import com.glavesoft.F
import com.glavesoft.pawnuser.R
import com.guoxiaoxing.phoenix.core.common.PhoenixConstant
import kotlinx.android.synthetic.main.frg_add_product.*


class FrgAddProduct : BaseFrg() {
    var video: String? = null
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_add_product)
    }

    override fun initView() {
        mImageView_addvideo.setOnClickListener {
            F.takePhoto(activity!!, 20, fileType = PhoenixConstant.TYPE_VIDEO)
        }

        mImageButton.setOnClickListener {
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText2.text.toString())) {
                F.toast("请输入分类")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText3.text.toString())) {
                F.toast("请输入子分类")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText4.text.toString())) {
                F.toast("请输入材质")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText5.text.toString())) {
                F.toast("请输入质量")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText6.text.toString())) {
                F.toast("请输入主材")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText7.text.toString())) {
                F.toast("请输入辅材")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText8.text.toString())) {
                F.toast("请输入品牌")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText9.text.toString())) {
                F.toast("请输入年代")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText10.text.toString())) {
                F.toast("请输入题材")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText11.text.toString())) {
                F.toast("请输入新旧程度")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText12.text.toString())) {
                F.toast("请输入样式")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText13.text.toString())) {
                F.toast("请输入种地")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText14.text.toString())) {
                F.toast("请输入尺寸")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText15.text.toString())) {
                F.toast("请输入售价")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText16.text.toString())) {
                F.toast("请输入描述")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }


        }
    }

    override fun loaddata() {
    }

    override fun onSuccess(data: String?, method: String) {
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("新增商品", "N E W   P R O D U C T S");
    }
}
//
//  ProductListSon
//
//  Created by 86139 on 2020-06-04 18:33:17
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.item;

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.glavesoft.F
import com.glavesoft.pawnuser.R
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.pawnuser.model.ModelData
import com.glavesoft.pawnuser.model.ModelProduct
import com.glavesoft.util.GlideLoader
import com.mdx.framework.Frame
import kotlinx.android.synthetic.main.frg_dingdan_detail_shz.*
import kotlinx.android.synthetic.main.item_product_list_son.view.*


class ProductListSon(context: Context?) : BaseItem(context) {
    lateinit var item: ModelData<ModelProduct.RowsBean>
    lateinit var from: String

    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_product_list_son, this)


        mImageButton_edit.setOnClickListener {


        }
        mImageButton_del.setOnClickListener {


        }
        mImageButton_edit2.setOnClickListener {


        }
        mImageButton_del2.setOnClickListener {


        }
        mTextView_state.setOnClickListener {
            if (mTextView_state.text == "下架") {
                load(
                    F.gB().dismount(item.mList[0].id.toString(), ""),
                    "dismount"
                )
            } else {
                load(
                    F.gB().online(item.mList[0].id.toString()),
                    "online"
                )
            }
        }
        mTextView_state2.setOnClickListener {
            if (mTextView_state2.text == "下架") {
                load(
                    F.gB().dismount(item.mList[1].id.toString(), ""),
                    "dismount"
                )
            } else {
                load(
                    F.gB().online(item.mList[1].id.toString()),
                    "online"
                )
            }
        }
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "dismount" || method == "online") {
            Frame.HANDLES.sentAll("FrgProductListSon1,FrgProductListSon0", 0, "")
        }
    }

    fun set(item: ModelData<ModelProduct.RowsBean>, from: String) {
        this.item = item
        this.from = from
        setShow(item.mList.size)
        for (i in item.mList.indices) {
            val it: ModelProduct.RowsBean = item.mList[i] as ModelProduct.RowsBean
            if (i == 0) {
                mTextView_info.visibility = View.GONE
                mLinearLayout_cz.visibility = View.GONE
                GlideLoader.loadImage(BaseConstant.Image_URL + it.img, mImageView, R.drawable.tu)
                mTextView_name.text = it.name
                mTextView_price.text = it.price
                if (it.state == 1) {//0下架1上架2新增待上架
                    mTextView_state.text = "下架"
                    if (!TextUtils.isEmpty(it.reasonOfDismounting)) {
                        mTextView_info.visibility = View.VISIBLE
                        mTextView_info.text = it.reasonOfDismounting
                    }
                } else {
                    mTextView_state.text = "上架"
                    mLinearLayout_cz.visibility = View.VISIBLE
                }
            } else if (i == 1) {
                mTextView_info2.visibility = View.GONE
                mLinearLayout_cz2.visibility = View.GONE
                GlideLoader.loadImage(BaseConstant.Image_URL + it.img, mImageView2, R.drawable.tu)
                mTextView_name2.text = it.name
                mTextView_price2.text = it.price
                if (it.state == 1) {//0下架1上架2新增待上架
                    mTextView_state2.text = "下架"
                    if (!TextUtils.isEmpty(it.reasonOfDismounting)) {
                        mTextView_info2.visibility = View.VISIBLE
                        mTextView_info2.text = it.reasonOfDismounting
                    }
                } else {
                    mTextView_state2.text = "上架"
                    mLinearLayout_cz2.visibility = View.VISIBLE
                }
            }
        }
    }

    fun setShow(count: Int) {
        mLinearLayout_1.setVisibility(if (count > 0) View.VISIBLE else View.INVISIBLE)
        mLinearLayout_2.setVisibility(if (count > 1) View.VISIBLE else View.INVISIBLE)
    }

}
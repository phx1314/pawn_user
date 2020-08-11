//
//  ProductListSon
//
//  Created by 86139 on 2020-06-04 18:33:17
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.item;

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.glavesoft.F
import com.glavesoft.pawnuser.R
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.pawnuser.frg.FrgAddProduct
import com.glavesoft.pawnuser.frg.FrgProductDetail
import com.glavesoft.pawnuser.model.ModelData
import com.glavesoft.pawnuser.model.ModelProduct
import com.glavesoft.util.GlideLoader
import com.mdx.framework.Frame
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import com.mdx.framework.view.CallBackOnly
import kotlinx.android.synthetic.main.item_product_list_son.view.*


class ProductListSon(context: Context?) : BaseItem(context) {
    lateinit var item: ModelData<ModelProduct.RowsBean>
    lateinit var from: String

    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_product_list_son, this)
//        mTextView_state.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//        mTextView_state.getPaint().setAntiAlias(true);//抗锯齿
//        mTextView_state2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//        mTextView_state2.getPaint().setAntiAlias(true);//抗锯齿
        mLinearLayout_1.setOnClickListener {
            Helper.startActivity(
                context,
                FrgProductDetail::class.java,
                TitleAct::class.java,
                "id",
                item.mList[0].id.toString(), "type", "rz", "isShow", false
            )
        }
        mLinearLayout_2.setOnClickListener {
            Helper.startActivity(
                context,
                FrgProductDetail::class.java,
                TitleAct::class.java,
                "id",
                item.mList[1].id.toString(), "type", "rz", "isShow", false
            )
        }
        mImageButton_edit.setOnClickListener {
            Helper.startActivity(
                context,
                FrgAddProduct::class.java,
                TitleAct::class.java,
                "id",
                item.mList[0].id.toString()
            )

        }
        mImageButton_del.setOnClickListener {
            com.mdx.framework.F.yShoure(
                context, "删除", "确认删除"
            ) { dialog, which ->
                kotlin.run {

                    load(
                        F.gB().goodsDelete(item.mList[0].id.toString()),
                        "goodsDelete"
                    )
                }
            }

        }
        mImageButton_edit2.setOnClickListener {

            Helper.startActivity(
                context,
                FrgAddProduct::class.java,
                TitleAct::class.java,
                "id",
                item.mList[1].id.toString()
            )
        }
        mImageButton_del2.setOnClickListener {
            com.mdx.framework.F.yShoure(
                context, "删除", "确认删除"
            ) { dialog, which ->
                kotlin.run {

                    load(
                        F.gB().goodsDelete(item.mList[1].id.toString()),
                        "goodsDelete"
                    )
                }
            }

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
        if (method == "dismount" || method == "online" || method == "goodsDelete") {
            Frame.HANDLES.sentAll("FrgProductListSon1,FrgProductListSon0,FrgAddProductList", 0, "")
        }
    }

    fun set(item: ModelData<ModelProduct.RowsBean>, from: String, type: Int) {
        this.item = item
        this.from = from
        setShow(item.mList.size)
        for (i in item.mList.indices) {
            val it: ModelProduct.RowsBean = item.mList[i] as ModelProduct.RowsBean
            if (i == 0) {
                mTextView_info.visibility = View.GONE
                mLinearLayout_cz.visibility = View.GONE
                GlideLoader.loadImage(
                    BaseConstant.Image_URL + it.img,
                    mImageView,
                    R.drawable.defalut_logo
                )
                mTextView_name.text = it.name
                mTextView_price.text = it.price
                if (type == 1) {//0下架1上架2新增待上架
                    mTextView_state.text = "下架"
                    mTextView_state.setTextColor(Color.parseColor("#DA8B54"))
                } else {
                    mTextView_state.text = "上架"
                    mTextView_state.setTextColor(resources.getColor(R.color.bg_title))
                    mLinearLayout_cz.visibility = View.VISIBLE
                    if (!TextUtils.isEmpty(it.reasonOfDismounting)) {
                        mTextView_info.visibility = View.VISIBLE
                        mTextView_info.text = "平台下架：" + it.reasonOfDismounting
                    }
                }
            } else if (i == 1) {
                mTextView_info2.visibility = View.GONE
                mLinearLayout_cz2.visibility = View.GONE
                GlideLoader.loadImage(
                    BaseConstant.Image_URL + it.img,
                    mImageView2,
                    R.drawable.defalut_logo
                )
                mTextView_name2.text = it.name
                mTextView_price2.text = it.price
                if (type == 1) {//0下架1上架2新增待上架
                    mTextView_state2.text = "下架"
                    mTextView_state2.setTextColor(Color.parseColor("#DA8B54"))
                } else {
                    mTextView_state2.text = "上架"
                    mTextView_state2.setTextColor(resources.getColor(R.color.bg_title))
                    mLinearLayout_cz2.visibility = View.VISIBLE
                    if (!TextUtils.isEmpty(it.reasonOfDismounting)) {
                        mTextView_info2.visibility = View.VISIBLE
                        mTextView_info2.text = "平台下架：" + it.reasonOfDismounting
                    }
                }
            }
        }
    }

    fun setShow(count: Int) {
        mLinearLayout_1.setVisibility(if (count > 0) View.VISIBLE else View.INVISIBLE)
        mLinearLayout_2.setVisibility(if (count > 1) View.VISIBLE else View.INVISIBLE)
    }

}
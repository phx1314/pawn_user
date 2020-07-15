//
//  AddProduct
//
//  Created by 86139 on 2020-06-04 19:54:10
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.item;

import com.glavesoft.pawnuser.R;

import android.content.Context;
import android.text.TextUtils
import android.view.LayoutInflater;

import android.view.View;
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.util.GlideLoader
import com.mdx.framework.Frame
import kotlinx.android.synthetic.main.item_add_product.view.*


class AddProduct(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_add_product, this)


    }

    fun set(item: String) {
        if (TextUtils.isEmpty(item)) {
            mImageView_del.visibility = View.GONE
        } else {
            mImageView_del.visibility = View.VISIBLE
        }
        GlideLoader.loadImage(
            BaseConstant.Image_URL + item,
            mImageView,
            R.drawable.add2
        )

        mImageView_del.setOnClickListener {
            Frame.HANDLES.sentAll("FrgAddProduct", 1, item)
        }
        mImageView.setOnClickListener {
            if (TextUtils.isEmpty(item))
                Frame.HANDLES.sentAll("FrgAddProduct", 0, item)
        }
    }

}
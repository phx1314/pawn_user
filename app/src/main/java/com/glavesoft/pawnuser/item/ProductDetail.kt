//
//  ProductDetail
//
//  Created by 86139 on 2020-06-05 10:01:06
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.item;

import com.glavesoft.pawnuser.R;

import android.annotation.SuppressLint;
import android.app.Activity
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.glavesoft.F
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.util.GlideLoader
import com.guoxiaoxing.phoenix.core.model.MediaEntity
import com.mdx.framework.Frame
import kotlinx.android.synthetic.main.item_product_detail.view.*


class ProductDetail(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_product_detail, this)
    }

    fun set(item: String, hasVideo: Boolean, position: Int) {
        if (position == 0 && hasVideo) {
            mImageView_bf.visibility = ViewGroup.VISIBLE
        } else {
            mImageView_bf.visibility = ViewGroup.GONE
        }

        GlideLoader.loadImage(
            BaseConstant.Image_URL + item,
            mImageView,
            R.drawable.defalut_logo
        )
        mImageView.setOnClickListener {
            if (mImageView_bf.visibility == ViewGroup.VISIBLE) {
                Frame.HANDLES.sentAll("FrgProductDetail", 0, "")
            } else {
                F.takePhoto(
                    context as Activity,
                    data = arrayOf(MediaEntity().apply {
                        this.localPath = BaseConstant.Image_URL + item
                    }).toMutableList()
                )
            }


        }
    }

}
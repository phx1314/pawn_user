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
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


class  ProductDetail(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_product_detail, this)
    }

    fun set(item: Any?) {

    }

}
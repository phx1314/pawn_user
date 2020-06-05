//
//  AdaProductDetail
//
//  Created by 86139 on 2020-06-05 10:01:06
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import com.glavesoft.pawnuser.item.ProductDetail;

class AdaProductDetail (context: Context, list: List<String>) : MAdapter<String>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = ProductDetail(context)
        }
        try {
            (convertView as ProductDetail).set(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}


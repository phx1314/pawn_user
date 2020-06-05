//
//  AdaProductListSon
//
//  Created by 86139 on 2020-06-04 18:33:17
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import com.glavesoft.pawnuser.item.ProductListSon;

class AdaProductListSon (context: Context, list: List<String>) : MAdapter<String>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = ProductListSon(context)
        }
        try {
            (convertView as ProductListSon).set(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}


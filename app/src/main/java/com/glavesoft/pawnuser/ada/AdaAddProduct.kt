//
//  AdaAddProduct
//
//  Created by 86139 on 2020-06-04 19:54:10
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import com.glavesoft.pawnuser.item.AddProduct;
import com.glavesoft.pawnuser.model.ModelGridImgData

class AdaAddProduct (context: Context, list: List<ModelGridImgData>) : MAdapter<ModelGridImgData>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = AddProduct(context)
        }
        try {
            (convertView as AddProduct).set(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}


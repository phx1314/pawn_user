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
import com.glavesoft.pawnuser.frg.FrgProductDetail
import com.glavesoft.pawnuser.frg.FrgProductList

import com.glavesoft.pawnuser.item.ProductListSon;
import com.glavesoft.pawnuser.model.ModelData
import com.glavesoft.pawnuser.model.ModelProduct
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper

class AdaProductListSon(
    context: Context,
    list: List<ModelData<ModelProduct.RowsBean>>,
    var from: String, var type: Int
) : MAdapter<ModelData<ModelProduct.RowsBean>>(context, list) {

    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = ProductListSon(context)
        }
        try {
            (convertView as ProductListSon).set(item, from, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return convertView
    }
}


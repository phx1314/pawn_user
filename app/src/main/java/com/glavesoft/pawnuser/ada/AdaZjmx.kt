//
//  AdaZjmx
//
//  Created by 86139 on 2020-06-03 15:17:58
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import com.glavesoft.pawnuser.item.Zjmx;
import com.glavesoft.pawnuser.model.ModelZjmx

class AdaZjmx (context: Context, list: List<ModelZjmx>) : MAdapter<ModelZjmx>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = Zjmx(context)
        }
        try {
            (convertView as Zjmx).set(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}


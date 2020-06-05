//
//  AdaThAddress
//
//  Created by 86139 on 2020-06-03 09:57:45
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import com.glavesoft.pawnuser.item.ThAddress;

class AdaThAddress (context: Context, list: List<String>) : MAdapter<String>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = ThAddress(context)
        }
        try {
            (convertView as ThAddress).set(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}


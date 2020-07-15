//
//  AdaDialogXj
//
//  Created by 86139 on 2020-07-10 13:56:24
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import com.glavesoft.pawnuser.item.DialogXj;

class AdaDialogXj (context: Context, list: List<String>) : MAdapter<String>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = DialogXj(context)
        }
        try {
//            (convertView as DialogXj).set(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}


//
//  AdaDialogPub
//
//  Created by 86139 on 2020-06-03 11:15:42
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import com.glavesoft.pawnuser.item.DialogPub;

class AdaDialogPub (context: Context, list: List<String>) : MAdapter<String>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = DialogPub(context)
        }
        try {
//            (convertView as DialogPub).set(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}


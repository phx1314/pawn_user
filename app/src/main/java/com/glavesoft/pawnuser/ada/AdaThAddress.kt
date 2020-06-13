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
import com.glavesoft.pawnuser.model.ModelGrzl
import com.mdx.framework.Frame

class AdaThAddress(context: Context, list: List<ModelGrzl.ReturnAddressBean>) :
    MAdapter<ModelGrzl.ReturnAddressBean>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = ThAddress(context)
        }
        try {
            (convertView as ThAddress).set(item, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        convertView.setOnLongClickListener(View.OnLongClickListener {
           Frame.HANDLES.sentAll("FrgThAddress",1,item.id.toString())
            true
        })
        return convertView
    }
}


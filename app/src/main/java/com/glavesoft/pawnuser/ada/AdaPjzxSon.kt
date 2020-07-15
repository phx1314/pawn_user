//
//  AdaPjzxSon
//
//  Created by 86139 on 2020-06-18 11:08:18
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.ada;

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.glavesoft.pawnuser.item.PjzxSon
import com.glavesoft.pawnuser.model.ModelDp
import com.mdx.framework.Frame
import com.mdx.framework.adapter.MAdapter
import com.mdx.framework.frg.FrgPtDetail
import com.mdx.framework.utility.Helper

class AdaPjzxSon (context: Context, list: List<ModelDp>) : MAdapter<ModelDp>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = PjzxSon(context)
        }
        try {
            (convertView as PjzxSon).set(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}


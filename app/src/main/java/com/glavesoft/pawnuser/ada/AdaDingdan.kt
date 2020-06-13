//
//  AdaDingdan
//
//  Created by 86139 on 2020-05-30 14:39:49
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.text.TextUtils
import android.view.ViewGroup;
import android.view.View;
import com.glavesoft.pawnuser.frg.FrgDingdanDetail
import com.glavesoft.pawnuser.frg.FrgDingdanDetailShz

import com.glavesoft.pawnuser.item.Dingdan;
import com.glavesoft.pawnuser.model.ModelDingdan
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper

class AdaDingdan(context: Context, list: List<ModelDingdan.RowsBean>) :
    MAdapter<ModelDingdan.RowsBean>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = Dingdan(context)
        }
        try {
            (convertView as Dingdan).set(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        convertView.setOnClickListener {
//            退款状态 0未退款 1申请退款 2同意退款 3提交单号 4已退款 5拒绝退款
            if (item.refState > 1) {
                Helper.startActivity(
                    context,
                    FrgDingdanDetailShz::class.java,
                    TitleAct::class.java,
                    "code",
                    item.code
                )
            } else {
                Helper.startActivity(
                    context,
                    FrgDingdanDetail::class.java,
                    TitleAct::class.java,
                    "code",
                    item.code
                )
            }

        }
        return convertView
    }
}


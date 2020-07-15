//
//  DialogXj
//
//  Created by 86139 on 2020-07-10 13:56:24
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.item;

import com.glavesoft.pawnuser.R;

import android.annotation.SuppressLint;
import android.app.Dialog
import android.content.Context;
import android.text.TextUtils
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import com.glavesoft.F
import com.mdx.framework.Frame
import kotlinx.android.synthetic.main.item_dialog_xj.view.*


class DialogXj(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_dialog_xj, this)
    }

    fun set(item: Dialog, id: String) {
        mTextView_sure.setOnClickListener {
            if (TextUtils.isEmpty(mEditText.text.toString())) {
                F.toast("请输入原因")
                return@setOnClickListener
            }
            item.dismiss()
            load(
                F.gB().dismount(id, mEditText.text.toString()),
                "dismount"
            )
        }
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "dismount") {
            Frame.HANDLES.sentAll("FrgProductListSon1,FrgProductListSon0,FrgAddProductList", 0, "")
        }
    }
}
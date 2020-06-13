//
//  ThAddress
//
//  Created by 86139 on 2020-06-03 09:57:45
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.item;

import com.glavesoft.pawnuser.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.CompoundButton
import com.glavesoft.F
import com.glavesoft.pawnuser.ada.AdaThAddress
import com.glavesoft.pawnuser.frg.FrgEditThAddress
import com.glavesoft.pawnuser.model.ModelGrzl
import com.mdx.framework.Frame
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_gerenziliao.*
import kotlinx.android.synthetic.main.frg_th_address.*
import kotlinx.android.synthetic.main.item_th_address.view.*


class ThAddress(context: Context?) : BaseItem(context) {
    lateinit var mAdaThAddress: AdaThAddress
    lateinit var item: ModelGrzl.ReturnAddressBean

    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_th_address, this)

        mCheckBox.setOnClickListener {
            if (item.isDefault == 0) {
                load(F.gB().default(item.id.toString()), "default")
            }

        }
        mButton_delete.setOnClickListener {
            load(F.gB().deleteReturnAddress(item.id.toString()), "deleteReturnAddress")
        }
        mButton_edit.setOnClickListener {
            Helper.startActivity(
                context,
                FrgEditThAddress::class.java,
                TitleAct::class.java, "item", item
            )
        }
    }

    fun set(item: ModelGrzl.ReturnAddressBean, mAdaThAddress: AdaThAddress) {
        this.mAdaThAddress = mAdaThAddress
        this.item = item
        mCheckBox.setCompoundDrawablesWithIntrinsicBounds(
            if (item.isDefault == 1) R.drawable.mr else R.drawable.mr_,
            0,
            0,
            0
        )
        mTextView_name.text = item.userName
        mTextView_phone.text = item.phone
        mTextView_adress.text = item.area + item.address
    }


    override fun onSuccess(data: String?, method: String) {
        if (method == "default" || method == "deleteReturnAddress") {
            Frame.HANDLES.sentAll("FrgThAddress", 0, "")
        }
    }
}
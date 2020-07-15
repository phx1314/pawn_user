//
//  DialogTxwul
//
//  Created by 86139 on 2020-06-13 08:47:06
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
import kotlinx.android.synthetic.main.item_dialog_txwul.view.*


class DialogTxwul(context: Context?) : BaseItem(context) {
    lateinit var mDialog: Dialog

    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_dialog_txwul, this)
    }

    fun set(mDialog: Dialog, code: String) {
        this.mDialog = mDialog;
        mTextView_sure.setOnClickListener {
            if (TextUtils.isEmpty(mEditText_1.text.toString())) {
                F.toast("请输入物流单号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText_2.text.toString())) {
                F.toast("请输入物流公司")
                return@setOnClickListener
            }
            load(
                F.gB().experter(
                    mEditText_1.text.toString(), mEditText_2.text.toString(),
                    code
                ), "experter"
            )
        }
    }

    override fun onSuccess(data: String?, method: String) {
        mDialog.dismiss()
        F.toast("提交成功")
//        Frame.HANDLES.sentAll("4,FrgDingdanDetail", 0, "")
    }
}
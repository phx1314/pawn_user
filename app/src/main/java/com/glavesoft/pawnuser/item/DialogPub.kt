//
//  DialogPub
//
//  Created by 86139 on 2020-06-03 11:15:42
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
import com.glavesoft.pawnuser.model.ModelDingdan
import com.mdx.framework.Frame
import kotlinx.android.synthetic.main.item_dialog_pub.view.*


class DialogPub(context: Context?) : BaseItem(context) {
    lateinit var mDialog: Dialog

    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_dialog_pub, this)


    }

    fun set(mDialog: Dialog, code: String) {
        this.mDialog = mDialog;
        mTextView_sure.setOnClickListener {
            if (TextUtils.isEmpty(mEditText.text.toString())) {
                F.toast("请输入原因")
                return@setOnClickListener
            }
            load(F.gB().orderReturn("5", mEditText.text.toString(), code), "orderReturn")
        }
    }

    override fun onSuccess(data: String?, method: String) {
        mDialog.dismiss()
        F.toast("提交成功")
        Frame.HANDLES.sentAll("4,FrgDingdanDetail", 0, "")
    }
}
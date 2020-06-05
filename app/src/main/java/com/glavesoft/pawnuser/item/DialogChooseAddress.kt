//
//  DialogChooseAddress
//
//  Created by 86139 on 2020-06-03 11:23:49
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


class  DialogChooseAddress(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_dialog_choose_address, this)
    }

    fun set(item: Any?) {

    }

}
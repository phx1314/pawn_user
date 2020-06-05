//
//  BaseItem
//
//  Created by 86139 on 2020-05-30 14:39:49
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.item;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout


open class BaseItem(context: Context?) : LinearLayout(context), View.OnClickListener{
   override fun onClick(v: View) {
   }
}
//
//  FrgHome
//
//  Created by 86139 on 2020-05-18 14:32:47
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.frg;
import android.os.Bundle;

import com.glavesoft.pawnuser.R;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;


class FrgHome : BaseFrg() {

  override fun create(savedInstanceState: Bundle?) {
           setContentView(R.layout.frg_home)
  }

  override fun initView() {
  }

  override fun loaddata() {
  }

  override fun onSuccess(data: String?, method: String) {
  }
 
}
//
//  FrgAddProduct
//
//  Created by 86139 on 2020-06-04 19:48:42
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.frg;
import android.os.Bundle;

import com.glavesoft.pawnuser.R;

import android.widget.EditText;
import android.widget.GridView;



class FrgAddProduct : BaseFrg() {

  override fun create(savedInstanceState: Bundle?) {
           setContentView(R.layout.frg_add_product)
  }

  override fun initView() {
  }

  override fun loaddata() {
  }

  override fun onSuccess(data: String?, method: String) {
  }
 
}
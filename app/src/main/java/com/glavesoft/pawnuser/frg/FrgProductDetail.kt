//
//  FrgProductDetail
//
//  Created by 86139 on 2020-06-05 09:51:33
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.frg;
import android.os.Bundle;

import com.glavesoft.pawnuser.R;

import android.widget.ImageView;
import android.widget.TextView;
import com.mdx.framework.view.MGridView;



class FrgProductDetail : BaseFrg() {

  override fun create(savedInstanceState: Bundle?) {
           setContentView(R.layout.frg_product_detail)
  }

  override fun initView() {
  }

  override fun loaddata() {
  }

  override fun onSuccess(data: String?, method: String) {
  }
 
}
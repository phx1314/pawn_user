//
//  FrgTx
//
//  Created by 86139 on 2020-06-03 15:30:52
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.glavesoft.pawnuser.frg;
import android.os.Bundle;

import com.glavesoft.pawnuser.R;

import android.widget.TextView;
import android.widget.EditText;



class FrgTx : BaseFrg() {

  override fun create(savedInstanceState: Bundle?) {
           setContentView(R.layout.frg_tx)
  }

  override fun initView() {
  }

  override fun loaddata() {
  }

  override fun onSuccess(data: String?, method: String) {
  }
 
}
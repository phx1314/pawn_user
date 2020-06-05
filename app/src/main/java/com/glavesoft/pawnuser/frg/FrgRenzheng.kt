//
//  FrgRenzheng
//
//  Created by 86139 on 2020-05-30 14:16:37
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;
import android.util.Log
import android.widget.LinearLayout
import com.glavesoft.F.gB

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity


class FrgRenzheng : BaseFrg() {

    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_renzheng)
    }

    override fun initView() {
    }

    override fun loaddata() {
        load(gB().loginByPwd("13915079457", BaseActivity.md5("111111")), "login")
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "loginByPwd") {
            Log.i("loginByPwd", data)
        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("认证资料", "C E R T I F I C A T I O N   D A T A");

    }
}
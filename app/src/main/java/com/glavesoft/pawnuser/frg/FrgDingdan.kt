//
//  FrgDingdan
//
//  Created by 86139 on 2020-05-30 14:22:50
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;
import android.text.TextUtils
import com.glavesoft.F

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.ada.AdaDingdan
import com.glavesoft.pawnuser.mod.LocalData
import com.glavesoft.pawnuser.model.ModelDingdan

import com.glavesoft.util.PListView;
import kotlinx.android.synthetic.main.frg_dingdan.*


class FrgDingdan : BaseFrg() {
    lateinit var state: String
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_dingdan)
        state = arguments!!.getString("state").toString()
        setId(state)
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        when (type) {
            0 -> {
                mAbPullListView.pullLoad()
            }
        }
    }

    override fun initView() {
        mAbPullListView.setApiLoadParams(
            this,
            "orderList",
            if (state == "4") "" else state,
            if (state == "4") "1" else "0",
            "",
            LocalData.getInstance().getUserInfo().getToken()
        )
        mAbPullListView.setAbOnListViewListener { _, content ->
            run {
                var mModelDingdan = F.data2Model(content, ModelDingdan::class.java)
                AdaDingdan(context!!, mModelDingdan.rows)
            }

        }
    }

    override fun loaddata() {
    }

    override fun onSuccess(data: String?, method: String) {
    }

}
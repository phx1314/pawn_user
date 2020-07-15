//
//  FrgPjzxSon
//
//  Created by 86139 on 2020-06-18 10:22:04
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;
import com.glavesoft.F

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.ada.AdaPjzxSon
import com.glavesoft.pawnuser.ada.AdaProductListSon
import com.glavesoft.pawnuser.mod.LocalData
import com.glavesoft.pawnuser.model.ModelData
import com.glavesoft.pawnuser.model.ModelDp
import com.glavesoft.pawnuser.model.ModelGrRz
import com.glavesoft.pawnuser.model.ModelProduct

import com.glavesoft.util.PListView;
import kotlinx.android.synthetic.main.frg_add_product_list.*
import kotlinx.android.synthetic.main.frg_dingdan.*
import kotlinx.android.synthetic.main.frg_dingdan.mAbPullListView
import java.util.ArrayList


class FrgPjzxSon : BaseFrg() {
    lateinit var status: String
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_pjzx_son)
        status = arguments!!.getString("status").toString()
    }

    override fun initView() {
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        when (type) {
            0 -> {
                mAbPullListView.pullLoad()
            }

        }

    }

    override fun loaddata() {
        mAbPullListView.setApiLoadParams(
            this,
            "pawnTicketList", status,
            LocalData.getInstance().getUserInfo().getToken()
        )
        mAbPullListView.setAbOnListViewListener { _, content ->
            run {
                var data: Array<ModelDp> = F.data2Model(content, Array<ModelDp>::class.java)


                AdaPjzxSon(context!!, data.toMutableList())
            }

        }
    }

    override fun onSuccess(data: String?, method: String) {
    }

}
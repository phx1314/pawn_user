//
//  FrgZjmx
//
//  Created by 86139 on 2020-06-03 15:10:39
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;
import android.widget.LinearLayout
import com.glavesoft.F

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.ada.AdaZjmx
import com.glavesoft.pawnuser.model.ModelCw
import com.glavesoft.pawnuser.model.ModelGrRz
import com.glavesoft.pawnuser.model.ModelZjmx

import com.glavesoft.util.PListView;
import kotlinx.android.synthetic.main.frg_cwzx.*
import kotlinx.android.synthetic.main.frg_zjmx.*


class FrgZjmx : BaseFrg() {

    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_zjmx)
    }

    override fun initView() {
    }

    override fun loaddata() {
        load(
            F.gB().financeetails(),
            "financeetails"
        )
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "financeetails") {
            var data: Array<ModelZjmx> = F.data2Model(data, Array<ModelZjmx>::class.java)
            mAbPullListView.adapter = AdaZjmx(context!!, data.toMutableList())
        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("资金明细", "F I N A N C I A L   D E T A I L S");
    }
}
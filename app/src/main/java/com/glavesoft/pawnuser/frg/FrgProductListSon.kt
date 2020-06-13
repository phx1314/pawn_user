//
//  FrgProductListSon
//
//  Created by 86139 on 2020-06-11 18:59:26
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle
import com.amap.api.maps.model.LatLng
import com.glavesoft.F
import com.glavesoft.pawnuser.R
import com.glavesoft.pawnuser.ada.AdaDingdan
import com.glavesoft.pawnuser.ada.AdaProductListSon
import com.glavesoft.pawnuser.mod.LocalData
import com.glavesoft.pawnuser.model.ModelData
import com.glavesoft.pawnuser.model.ModelDingdan
import com.glavesoft.pawnuser.model.ModelProduct
import kotlinx.android.synthetic.main.frg_dingdan.*
import java.util.ArrayList


class FrgProductListSon : BaseFrg() {
    lateinit var state: String
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_product_list_son)
        state = arguments!!.getString("state").toString()
        setId("FrgProductListSon$state")
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
            "getGoods",
            state,
            "",
            LocalData.getInstance().getUserInfo().getToken()
        )
        mAbPullListView.setAbOnListViewListener { _, content ->
            run {
                var mModelProduct = F.data2Model(content, ModelProduct::class.java)


                var data = ArrayList<ModelData<ModelProduct.RowsBean>>()
                for (i in 0 until mModelProduct.rows.size) {
                    if (i % 2 == 0) {
                        val mModelData: ModelData<ModelProduct.RowsBean> =
                            ModelData<ModelProduct.RowsBean>()
                        for (j in i until Math.min(mModelProduct.rows.size, i + 2)) {
                            mModelData.mList.add(mModelProduct.rows[j])
                        }
                        data.add(mModelData)
                    }
                }
                AdaProductListSon(context!!, data, "FrgProductListSon$state")
            }

        }
    }

    override fun onSuccess(data: String?, method: String) {
    }

}
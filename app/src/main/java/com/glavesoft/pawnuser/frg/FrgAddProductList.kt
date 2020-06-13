//
//  FrgAddProductList
//
//  Created by 86139 on 2020-06-12 17:42:03
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;

import com.glavesoft.pawnuser.R;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout
import com.glavesoft.F
import com.glavesoft.pawnuser.ada.AdaProductListSon
import com.glavesoft.pawnuser.mod.LocalData
import com.glavesoft.pawnuser.model.ModelData
import com.glavesoft.pawnuser.model.ModelProduct
import com.glavesoft.util.PListView;
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_add_product_list.*
import kotlinx.android.synthetic.main.frg_dingdan.*
import kotlinx.android.synthetic.main.frg_dingdan.mAbPullListView
import kotlinx.android.synthetic.main.item_head.view.*
import java.util.ArrayList


class FrgAddProductList : BaseFrg() {

    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_add_product_list)
    }

    override fun initView() {
        mImageButton_search.setOnClickListener {
            mAbPullListView.setApiLoadParams(
                this,
                "getGoods",
                "2",
                mEditText.text.toString(),
                LocalData.getInstance().getUserInfo().getToken()
            )
        }
    }

    override fun loaddata() {

        mAbPullListView.setApiLoadParams(
            this,
            "getGoods",
            "2",
            mEditText.text.toString(),
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
                AdaProductListSon(context!!, data, "FrgAddProductList")
            }

        }
    }

    override fun onSuccess(data: String?, method: String) {
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("新增商品", "N E W   P R O D U C T S");
        mHead.setRightRes(R.drawable.add)
        mHead.mImageButton_right.setOnClickListener {
            Helper.startActivity(context, FrgAddProduct::class.java, TitleAct::class.java)
        }
    }
}
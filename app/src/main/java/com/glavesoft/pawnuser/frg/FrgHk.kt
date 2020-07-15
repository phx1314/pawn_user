//
//  FrgHk
//
//  Created by 86139 on 2020-06-04 14:59:52
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle
import android.widget.LinearLayout
import com.glavesoft.F
import com.glavesoft.pawnuser.R
import com.glavesoft.pawnuser.item.MFragmentAdapter
import com.glavesoft.pawnuser.model.ModelCw
import kotlinx.android.synthetic.main.frg_hk.*


class FrgHk : BaseFrg() {
    var fragments: ArrayList<BaseFrg> = ArrayList()
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_hk)
    }

    override fun initView() {
        mViewPager.setNoScroll(false)
        fragments.add(FrgDingdan().apply {
            val bundle = Bundle()
            bundle.putString("state", "1")
            this.arguments = bundle
        })
        fragments.add(FrgDingdan().apply {
            val bundle = Bundle()
            bundle.putString("state", "2")
            this.arguments = bundle
        })
        fragments.add(FrgDingdan().apply {
            val bundle = Bundle()
            bundle.putString("state", "3")
            this.arguments = bundle
        })
        mViewPager.adapter = MFragmentAdapter(childFragmentManager, fragments)
        //将tabLayout与viewpager连起来
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.offscreenPageLimit = fragments.size
        mTabLayout.getTabAt(0)!!.text = "待付款"
        mTabLayout.getTabAt(1)!!.text = "待发货"
        mTabLayout.getTabAt(2)!!.text = "待收货"
        mTabLayout.getTabAt(1)!!.select()
        mTabLayout.getTabAt(0)!!.select()
    }

    override fun loaddata() {
        load(
            F.gB().financeinfo(),
            "financeinfo"
        )
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "financeinfo") {
            var item = F.data2Model(data, ModelCw::class.java)
            mTextView_price.text = "￥" + item.total
            mTextView_dfk.text = "￥" + item.toBePai
            mTextView_dfh.text = "￥" + item.toBeDelivered
            mTextView_dsh.text = "￥" + item.toBeHarvested
        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("货款", "P A Y M E N T   F O R   G O O D S");
    }
}
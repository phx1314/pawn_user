//
//  FrgProductList
//
//  Created by 86139 on 2020-06-11 18:53:18
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle;
import android.widget.LinearLayout

import com.glavesoft.pawnuser.R;

import com.androidkun.xtablayout.XTabLayout;
import com.glavesoft.pawnuser.item.MFragmentAdapter
import com.mdx.framework.newMenu.DfMViewPager;
import kotlinx.android.synthetic.main.frg_renzheng.*


class FrgProductList : BaseFrg() {
    var fragments: ArrayList<BaseFrg> = ArrayList()
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_product_list)
    }

    override fun initView() {
        mViewPager.setNoScroll(false)
        fragments.add(FrgProductListSon().apply {
            val bundle = Bundle()
            bundle.putString("state", "1")
            this.arguments = bundle
        })
        fragments.add(FrgProductListSon().apply {
            val bundle = Bundle()
            bundle.putString("state", "0")
            this.arguments = bundle
        })
        mViewPager.adapter = MFragmentAdapter(childFragmentManager, fragments)
        //将tabLayout与viewpager连起来
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.offscreenPageLimit = fragments.size
        mTabLayout.getTabAt(0)!!.text = "上架商品"
        mTabLayout.getTabAt(1)!!.text = "仓库商品"
        mTabLayout.getTabAt(1)!!.select()
        mTabLayout.getTabAt(0)!!.select()
    }

    override fun loaddata() {
    }

    override fun onSuccess(data: String?, method: String) {
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("商品列表", "P R O D U C T   L I S T");
    }
}
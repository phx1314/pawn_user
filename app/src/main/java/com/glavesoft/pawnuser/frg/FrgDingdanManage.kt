//
//  FrgDingdanManage
//
//  Created by 86139 on 2020-05-30 14:22:53
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


class FrgDingdanManage : BaseFrg() {
    var fragments: ArrayList<BaseFrg> = ArrayList()
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_dingdan_manage)
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
        fragments.add(FrgDingdan().apply {
            val bundle = Bundle()
            bundle.putString("state", "4")
            this.arguments = bundle
        })
        mViewPager.adapter = MFragmentAdapter(childFragmentManager, fragments)
        //将tabLayout与viewpager连起来
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.offscreenPageLimit = fragments.size
        mTabLayout.getTabAt(0)!!.text = "待付款"
        mTabLayout.getTabAt(1)!!.text = "待发货"
        mTabLayout.getTabAt(2)!!.text = "待收货"
        mTabLayout.getTabAt(3)!!.text = "售后"
        mTabLayout.getTabAt(1)!!.select()
        mTabLayout.getTabAt(0)!!.select()
    }

    override fun loaddata() {
    }

    override fun onSuccess(data: String?, method: String) {
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("订单管理", "O R D E R   M A N A G E M E N T");
    }
}
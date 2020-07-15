//
//  FrgPjzx
//
//  Created by 86139 on 2020-06-18 10:21:24
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


class FrgPjzx : BaseFrg() {
    var fragments: ArrayList<BaseFrg> = ArrayList()
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_pjzx)
    }

    override fun initView() {
    }

    override fun loaddata() {
        mViewPager.setNoScroll(false)
        fragments.add(FrgPjzxSon().apply {
            val bundle = Bundle()
            bundle.putString("status", "1")
            this.arguments = bundle
        })
        fragments.add(FrgPjzxSon().apply {
            val bundle = Bundle()
            bundle.putString("status", "2")
            this.arguments = bundle
        })
        mViewPager.adapter = MFragmentAdapter(childFragmentManager, fragments)
        //将tabLayout与viewpager连起来
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.offscreenPageLimit = fragments.size
        mTabLayout.getTabAt(0)!!.text = "待签"
        mTabLayout.getTabAt(1)!!.text = "已签"
        mTabLayout.getTabAt(1)!!.select()
        mTabLayout.getTabAt(0)!!.select()
    }

    override fun onSuccess(data: String?, method: String) {
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("票据中心", "B I L L   C E N T E R");

    }
}
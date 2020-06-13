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
import com.glavesoft.pawnuser.item.MFragmentAdapter
import com.mdx.framework.newMenu.SlidingFragment
import kotlinx.android.synthetic.main.frg_renzheng.*


class FrgRenzheng : BaseFrg() {
    var fragments: ArrayList<BaseFrg> = ArrayList()
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_renzheng)
    }

    override fun initView() {
    }

    override fun loaddata() {
        mViewPager.setNoScroll(false)
        fragments.add(FrgGerenRenzheng())
        fragments.add(FrgQiyeRenzheng())
        mViewPager.adapter = MFragmentAdapter(childFragmentManager, fragments)
        //将tabLayout与viewpager连起来
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.offscreenPageLimit = fragments.size
        mTabLayout.getTabAt(0)!!.text = "个人认证"
        mTabLayout.getTabAt(1)!!.text = "企业认证"
        mTabLayout.getTabAt(1)!!.select()
        mTabLayout.getTabAt(0)!!.select()
    }

    override fun onSuccess(data: String?, method: String) {

    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("认证资料", "C E R T I F I C A T I O N   D A T A");

    }
}
//
//  Head
//
//  Created by 86139 on 2019-11-22 08:25:55
//  Copyright (c) 86139 All rights reserved.


/**
 *
 */

package com.glavesoft.pawnuser.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.glavesoft.pawnuser.R
import com.mdx.framework.activity.BaseActivity
import kotlinx.android.synthetic.main.item_head.view.*


class Head(context: Context?) : LinearLayout(context) {

    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_head, this)

    }


    fun canGoBack(b: Boolean = true) {
        if (b) mImageButton_back.visibility = View.VISIBLE else mImageButton_back.visibility =
            View.GONE
        mImageButton_back.setOnClickListener {
            (context as BaseActivity).finish()
        }
    }


    fun setTitle(s: String, s_en: String) {
        mTextView_title.visibility = View.VISIBLE
        mTextView_title.text = s
        mLinearLayout_bottom.visibility = View.VISIBLE
        mTextView_en.text = s_en
    }


}
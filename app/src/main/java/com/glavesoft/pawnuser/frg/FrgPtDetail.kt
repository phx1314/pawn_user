//
//  FrgPtDetail
//
//  Created by 86139 on 2020-07-02 11:16:33
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.glavesoft.pawnuser.R
import kotlinx.android.synthetic.main.frg_pt_detail.*


class FrgPtDetail : BaseFrg() {
    var url = ""
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_pt_detail)
        url = activity!!.intent.getStringExtra("url")
    }

    override fun initView() {
    }

    override fun loaddata() {
        mWebView.loadUrl(url)
        mWebView.getSettings().setJavaScriptEnabled(true)
        mWebView.getSettings().setDomStorageEnabled(true)
        mWebView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url)
                return true
            }
        })

        mWebView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                    && mWebView.canGoBack()
                ) { // 表示按返回键
                    mWebView.goBack() // 后退
                    return@OnKeyListener true // 已处理
                }
            }
            false
        })
    }

    override fun onSuccess(data: String?, method: String) {
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("票据详情", "B I L L   D E T A I L");

    }
}
//
//  BaseFrg
//
//  Created by 86139 on 2020-03-18 14:02:52
//  Copyright (c) 86139 All rights reserved.


/**

 */
package com.glavesoft.pawnuser.frg;

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.glavesoft.F.toast
import com.glavesoft.pawnuser.item.Head
import com.glavesoft.pawnuser.mod.DataResult
import com.glavesoft.pawnuser.mod.DataResult.RESULT_OK_ZERO
import com.google.gson.GsonBuilder
import com.mdx.framework.Frame
import com.mdx.framework.activity.MFragment
import com.mdx.framework.service.subscriber.HttpResultSubscriberListener
import com.mdx.framework.service.subscriber.S
import com.mdx.framework.utility.AbAppUtil
import com.mdx.framework.utility.Helper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

open class BaseFrg : MFragment(), View.OnClickListener, HttpResultSubscriberListener {
    lateinit var mHead: Head

    @JvmField
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    final override fun initV(view: View) {
        initView()
        loaddata()
    }

    override fun create(var1: Bundle?) {
    }

    open fun initView() {}
    open fun loaddata() {}
    override fun onClick(v: View) {
    }


    override fun onSuccess(data: String?, method: String) {
    }


    override fun onError(code: String?, msg: String?, data: String?, mehotd: String) {

    }

    override fun onNext(httpResult: Any?, method: String) {
        try {
            val mHttpResult = (httpResult as DataResult<*>)
            if (mHttpResult.errorCode == RESULT_OK_ZERO) {
                onSuccess(GsonBuilder().serializeNulls().create().toJson(mHttpResult.data), method)
            } else {
                toast(mHttpResult.errorMsg)
                onError(
                    mHttpResult.errorCode.toString(),
                    mHttpResult.errorMsg,
                    GsonBuilder().serializeNulls().create()
                        .toJson(mHttpResult.data),//serializeNulls()属性之后，就会导出值为null的属性了
                    method
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> load(
        o: Observable<DataResult<T>>,
        m: String,
        isShow: Boolean = true,
        showMessage: String = "加载中"
    ) {
        var s = S(this, ProgressDialog(context).apply { this.setMessage(showMessage) }, m, isShow)
        compositeDisposable.add(s)
        if (!AbAppUtil.isNetworkAvailable(Frame.CONTEXT)) {
            Helper.toast("网络连接错误")
        }
        o.subscribeOn(Schedulers.newThread()).unsubscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { if (s.isShow) s.mProgressDialog.show() }
            .doFinally { if (s.mProgressDialog.isShowing) s.mProgressDialog.dismiss() }.subscribe(s)
    }

    override fun setActionBar(actionBar: LinearLayout?) {
        mHead = Head(context)
        mHead.canGoBack(true)
        actionBar?.addView(
            mHead,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }


}

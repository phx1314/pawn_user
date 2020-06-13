//
//  BaseItem
//
//  Created by 86139 on 2020-05-30 14:39:49
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.item;

import android.app.ProgressDialog
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout
import com.glavesoft.pawnuser.frg.BaseFrg
import com.glavesoft.pawnuser.mod.DataResult
import com.mdx.framework.Frame
import com.mdx.framework.activity.BaseActivity
import com.mdx.framework.service.subscriber.HttpResult
import com.mdx.framework.service.subscriber.HttpResultSubscriberListener
import com.mdx.framework.service.subscriber.S
import com.mdx.framework.utility.AbAppUtil
import com.mdx.framework.utility.Helper
import com.mdx.framework.utility.handle.MHandler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


open class BaseItem(context: Context?) : LinearLayout(context), View.OnClickListener,
    HttpResultSubscriberListener {
    override fun onClick(v: View) {
    }

    var handler = MHandler()
    val className = this.javaClass.simpleName

    init {
        handler.setId(className)
        handler.setMsglisnener { msg ->
            when (msg.what) {
                201 -> this@BaseItem.disposeMsg(msg.arg1, msg.obj)
            }
        }
        if (Frame.HANDLES.get(className).size > 0) {
            Frame.HANDLES.get(className).forEach {
                Frame.HANDLES.remove(it)
            }
        }
        Frame.HANDLES.add(handler)
    }

    fun setHandlerId(id: String) {
        handler.setId(id)
    }


    open fun disposeMsg(type: Int, obj: Any) {}

    override fun onError(code: String?, msg: String?, data: String?, method: String) {

    }

    override fun onNext(httpResult: Any?, method: String) {

    }


    override fun onSuccess(data: String?, method: String) {
    }

    fun <T> load(
        o: Observable<DataResult<T>>,
        m: String,
        isShow: Boolean = true,
        showMessage: String = "加载中"
    ) {
        var s = S(this, ProgressDialog(context).apply { this.setMessage(showMessage) }, m, isShow)
        ((context as BaseActivity).mFragment as BaseFrg).compositeDisposable.add(s)
        if (!AbAppUtil.isNetworkAvailable(Frame.CONTEXT)) {
            Helper.toast("网络连接错误")
        }
        o.subscribeOn(Schedulers.newThread()).unsubscribeOn(Schedulers.newThread()).observeOn(
            AndroidSchedulers.mainThread()
        ).doOnSubscribe {
            try {
                if (s.isShow) s.mProgressDialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.doFinally {
            try {
                if (s.mProgressDialog.isShowing) s.mProgressDialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.subscribe(s)
    }

    override fun onDetachedFromWindow() {
        Frame.HANDLES.remove(this.handler)
        super.onDetachedFromWindow()
    }
}
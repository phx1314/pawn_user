package com.glavesoft

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.view.inputmethod.InputMethodManager
import com.glavesoft.pawnuser.service.ApiService
import com.google.gson.Gson
import com.mdx.framework.Frame


object F {
    var s: Int = -1
    var checked = "-1"
    val baseUrl = "http://baidu.paidangwang.net/paidangUserApi/api/"
    fun gB(TIME: Long = 30) = com.mdx.framework.service.gB(
        ApiService::class.java,
        baseUrl, "", TIME
    )

    fun init() {


    }

    fun <T> data2Model(data: String?, mclass: Class<T>): T? {
        return Gson().fromJson(data, mclass)
    }

    fun getJson(key: String): String? {
        val sp = PreferenceManager.getDefaultSharedPreferences(Frame.CONTEXT)
        return sp.getString(key, "")
    }

    fun saveJson(key: String, json: String?) {
        val sp = PreferenceManager.getDefaultSharedPreferences(Frame.CONTEXT)
        sp.edit().putString(key, json).apply()

    }

    fun logOut(context: Context?, isShow: Boolean = true, isFromTask: Boolean = false) {


    }


    // kfc 1
    // / 关闭软件盘
    fun closeSoftKey(act: Activity) {
        val localInputMethodManager =
            act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val localIBinder = act.getWindow().getDecorView().getWindowToken()
        localInputMethodManager.hideSoftInputFromWindow(localIBinder, 2)
        // InputMethodManager imm = (InputMethodManager) getActivity()
        // .getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}













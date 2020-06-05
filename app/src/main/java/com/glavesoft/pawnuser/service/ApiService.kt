package com.glavesoft.pawnuser.service

import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.DeviceUtils
import com.glavesoft.pawnuser.mod.DataResult
import com.glavesoft.pawnuser.model.PwHttpResult
import com.mdx.framework.Frame
import com.mdx.framework.service.subscriber.HttpResult
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiService {

    @POST("account/loginByPwd")
    @FormUrlEncoded
    fun loginByPwd(
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("deviceType") deviceType: String = "1",
        @Field("deviceid") deviceid: String = DeviceUtils.getAndroidID(),
        @Field("cid") cid: String = JPushInterface.getRegistrationID(Frame.CONTEXT)
    ): Observable<PwHttpResult<Any>>


}
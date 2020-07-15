package com.glavesoft.pawnuser.service

import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.DeviceUtils
import com.glavesoft.pawnuser.mod.DataResult
import com.glavesoft.pawnuser.mod.LocalData
import com.mdx.framework.Frame
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {


    //刪除退货地址
    @POST("auth/delete/returnAddress")
    @FormUrlEncoded
    fun deleteReturnAddress(
        @Field("addressId") addressId: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //查看个人资金记录
    @POST("auth/finance/details")
    @FormUrlEncoded
    fun financeetails(
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //查看个人财务情况
    @POST("auth/finance/info")
    @FormUrlEncoded
    fun financeinfo(
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //转账到支付宝
    @POST("auth/finance/pay")
    @FormUrlEncoded
    fun financepay(
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //获取个人信息
    @GET("auth/get/userInfo")
    fun userInfo(
        @Query("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //商家端商品下架
    @POST("auth/goods/dismount")
    @FormUrlEncoded
    fun dismount(
        @Field("goodsId") goodsId: String,
        @Field("reasonOfDismounting") reasonOfDismounting: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //生成合同开始签署*
    @POST("pawnTicketCenter/signPawnTicket")
    @FormUrlEncoded
    fun signPawnTicket(
        @Field("pawnTicketId") pawnTicketId: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //查看合同*
    @POST("pawnTicketCenter/showContract")
    @FormUrlEncoded
    fun showContract(
        @Field("pawnTicketId") pawnTicketId: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //根据(状态)获取发布商品
    @GET("auth/goods/getGoods")
    fun getGoods(
        @Query("state") state: String,
        @Query("goodsName") goodsName: String,
        @Query("token") token: String = LocalData.getInstance().getUserInfo().getToken(),
        @Query("page") page: String,
        @Query("rows") rows: String
    ): Observable<DataResult<Any>>

    //查看提交的认证信息
    @GET("auth/view/authInfo")
    fun authInfo(
        @Query("type") type: String,
        @Query("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //根据(状态)获取订单列表
    @GET("auth/order/orderList")
    fun orderList(
        @Query("state") state: String,
        @Query("goodsName") goodsName: String,
        @Query("token") token: String = LocalData.getInstance().getUserInfo().getToken(),
        @Query("page") page: String,
        @Query("rows") rows: String
    ): Observable<DataResult<Any>>

    //票据列表
    @POST("pawnTicketCenter/pawnTicketList")
    @FormUrlEncoded
    fun pawnTicketList(
        @Field("status") status: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken(),
        @Field("page") page: String,
        @Field("limit") rows: String
    ): Observable<DataResult<Any>>

    //删除发布的商品
    @GET("auth/goods/delete")
    fun goodsDelete(
        @Query("goodsId") goodsId: String,
        @Query("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //获取售后订单列表
    @GET("auth/order/afterSales")
    fun afterSales(
        @Query("token") token: String = LocalData.getInstance().getUserInfo().getToken(),
        @Query("page") page: String,
        @Query("rows") rows: String
    ): Observable<DataResult<Any>>

    //商家端商品上架
    @POST("auth/goods/online")
    @FormUrlEncoded
    fun online(
        @Field("goodsId") goodsId: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //填写物流信息
    @POST("auth/order/experter")
    @FormUrlEncoded
    fun experter(
        @Field("shipFirm") shipFirm: String,
        @Field("shipCode") shipCode: String,
        @Field("orderCode") orderCode: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    @POST("common/appVerion")
    @FormUrlEncoded
    fun appVerion(
        @Field("deviceType") deviceType: String = "1"
    ): Observable<DataResult<Any>>

    //订单详情
    @POST("auth/order/info")
    @FormUrlEncoded
    fun info(
        @Field("orderCode") orderCode: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //同意/不同意退货
    @POST("auth/order/return")
    @FormUrlEncoded
    fun orderReturn(
        @Field("refState") refState: String,
        @Field("refundNotVerifyReason") refundNotVerifyReason: String,
        @Field("orderCode") orderCode: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    @POST("userShopCart/updateCart")
    @FormUrlEncoded
    fun updateCart(
        @Field("goodsId") goodsId: String,
        @Field("num") num: String = "1",
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    @POST("userGoods/getShareText")
    @FormUrlEncoded
    fun getShareText(
        @Field("id") goodsId: String,
        @Field("type") type: String
    ): Observable<DataResult<Any>>

    @POST("storeGoods/storeGoodsDetail")
    @FormUrlEncoded
    fun storeGoodsDetail(
        @Field("id") id: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //支付密码验证
    @POST("pay/password/verification")
    @FormUrlEncoded
    fun verification(
        @Field("password") password: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //重置支付密码
    @POST("pay/reset/password")
    @FormUrlEncoded
    fun password(
        @Field("password") password: String,
        @Field("newPassword") newPassword: String,
        @Field("reNewPassword") reNewPassword: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //设置支付密码
    @POST("auth/pay/set/password")
    @FormUrlEncoded
    fun setPassword(
        @Field("password") password: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //设置默认地址
    @POST("auth/set/default")
    @FormUrlEncoded
    fun default(
        @Field("addressId") addressId: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //保存退货地址
    @POST("auth/saveOrUpdate/returnAddress")
    @FormUrlEncoded
    fun saveReturnAddress(
        @Field("id") id: String,
        @Field("userId") userId: String,
        @Field("userName") userName: String,
        @Field("area") area: String,
        @Field("address") address: String,
        @Field("isDefault") isDefault: String,
        @Field("phone") phone: String,
        @Field("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //商家端商品发布
    @POST("auth/goods/save")
    @FormUrlEncoded
    fun save(
        @FieldMap map: Map<String, String?>
    ): Observable<DataResult<Any>>

    //提交企业认证
    @POST("auth/save/enterprise")
    @FormUrlEncoded
    fun enterprise(
        @FieldMap map: Map<String, String?>
    ): Observable<DataResult<Any>>

    //提交个人认证
    @POST("auth/save/personal")
    @FormUrlEncoded
    fun personal(
        @FieldMap map: Map<String, String?>
    ): Observable<DataResult<Any>>

    //查询是否通过验证
    @GET("auth/passOrNot")
    fun passOrNot(
        @Query("token") token: String = LocalData.getInstance().getUserInfo().getToken()
    ): Observable<DataResult<Any>>

    //单文件上传
    @Multipart
    @POST("common/upload")
    fun upload(
        @Part file: MultipartBody.Part
    ): Observable<DataResult<Any>>

    //上传多文件
    @Multipart
    @POST("common/upload")
    fun uploadFiles(
        @Part files: List<MultipartBody.Part>
    ): Observable<DataResult<Any>>

    //上传多文件
    @POST("common/upload")
    fun uploadFilesBody(
        @Body files: MultipartBody
    ): Observable<DataResult<Any>>


}
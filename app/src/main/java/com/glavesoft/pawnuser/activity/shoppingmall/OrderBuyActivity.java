package com.glavesoft.pawnuser.activity.shoppingmall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.alipay.AliPay;
import com.glavesoft.alipay.util.PayInfo;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.AddressInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.StoreGoodsDetailInfo;
import com.glavesoft.pawnuser.wxapi.WXPay;
import com.glavesoft.util.Arith;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2018/1/24
 * @company:常州宝丰
 */
public class OrderBuyActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_buy_submitbuy;
    private ImageView iv_account_alipay,iv_account_wechat;
    private int payTool = -1;  //2支付宝 3微信

    private String id,goodtype;

    private LinearLayout ll_address_submitbuy,ll_nodata_address,ll_data_address;

    private TextView tv_name_submitbuy,tv_phone_submitbuy,tv_address_submitbuy;

    private ImageView iv_address_jt,iv_yhp_jt;

    private ImageView iv_pic_submitbuy;
    private TextView tv_goodsname_submitbuy,tv_jdprice_submitbuy,tv_price_submitbuy,tv_rz_submitbuy;

    private LinearLayout ll_yhq_submitbuy;
    private TextView tv_yhprice_submitbuy;

    private TextView tv_totleprice_submitbuy;

    private double yhprice=0.0,totleprice=0.0;

    DecimalFormat df  = new DecimalFormat("######0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitbuy);
        setBoardCast();
        initData();
        initView();
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("order_buy");
        registerReceiver(mListenerID, f);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("state").equals("success")){//支付成功
                Intent intent1=new Intent("OrderRefresh");
                sendBroadcast(intent1);
            }
            finish();
        }
    };

    public  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mListenerID);
    }

    private void initData() {
        id=getIntent().getStringExtra("id");
        goodtype=getIntent().getStringExtra("goodtype");
    }

    private void initView() {
        setTitleBack();
        setTitleName("确认购买");
        setTitleNameEn(R.mipmap.confirm_purchase);

        ll_address_submitbuy=getViewById(R.id.ll_address_submitbuy);
        ll_nodata_address=getViewById(R.id.ll_nodata_address);
        ll_data_address=getViewById(R.id.ll_data_address);
        ll_address_submitbuy.setOnClickListener(this);

        tv_name_submitbuy=getViewById(R.id.tv_name_submitbuy);
        tv_phone_submitbuy=getViewById(R.id.tv_phone_submitbuy);
        tv_address_submitbuy=getViewById(R.id.tv_address_submitbuy);
        tv_rz_submitbuy=getViewById(R.id.tv_rz_submitbuy);

        iv_pic_submitbuy=getViewById(R.id.iv_pic_submitbuy);
        tv_goodsname_submitbuy=getViewById(R.id.tv_goodsname_submitbuy);
        tv_jdprice_submitbuy=getViewById(R.id.tv_jdprice_submitbuy);
        tv_price_submitbuy=getViewById(R.id.tv_price_submitbuy);

        ll_yhq_submitbuy=getViewById(R.id.ll_yhq_submitbuy);
        tv_yhprice_submitbuy=getViewById(R.id.tv_yhprice_submitbuy);
        ll_yhq_submitbuy.setOnClickListener(this);

        iv_address_jt=getViewById(R.id.iv_address_jt);
        iv_yhp_jt=getViewById(R.id.iv_yhp_jt);
        iv_address_jt.setVisibility(View.GONE);
        iv_yhp_jt.setVisibility(View.GONE);

        tv_totleprice_submitbuy=getViewById(R.id.tv_totleprice_submitbuy);
        tv_buy_submitbuy=(TextView)findViewById(R.id.tv_buy_submitbuy);
        iv_account_alipay=(ImageView) findViewById(R.id.iv_account_alipay);
        iv_account_wechat=(ImageView) findViewById(R.id.iv_account_wechat);
        tv_buy_submitbuy.setOnClickListener(this);
        iv_account_alipay.setOnClickListener(this);
        iv_account_wechat.setOnClickListener(this);

        if(goodtype.equals("1")){
            tv_jdprice_submitbuy.setVisibility(View.GONE);
            ll_yhq_submitbuy.setVisibility(View.VISIBLE);
            tv_rz_submitbuy.setText("认证商城");
            tv_rz_submitbuy.setBackgroundResource(R.drawable.shape_yellow);
        }else{
            tv_jdprice_submitbuy.setVisibility(View.VISIBLE);
            ll_yhq_submitbuy.setVisibility(View.GONE);
            tv_rz_submitbuy.setText("淘宝贝");
            tv_rz_submitbuy.setBackgroundResource(R.drawable.shape_red);
        }

        myStoreGoodsDetail();
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent();
        switch (v.getId())
        {
            case R.id.tv_buy_submitbuy:
                goToPay();
                break;
            case R.id.iv_account_wechat:////微信支付
                if(!CommonUtils.isWechatInstalled(OrderBuyActivity.this)){
                    CustomToast.show("未检测到微信客户端，请先安装");
                }else{
                    payTool = 2;
                    iv_account_alipay.setImageResource(R.drawable.yuan);
                    iv_account_wechat.setImageResource(R.drawable.goux);
                }
                break;

            case R.id.iv_account_alipay://支付宝支付
                payTool = 1;
                iv_account_alipay.setImageResource(R.drawable.goux);
                iv_account_wechat.setImageResource(R.drawable.yuan);
                break;
        }
    }

    private void goToPay(){

        if (payTool<0) {
            CustomToast.show("请选择支付方式！");
            return;
        }

        buyPay(id);
    }

    private void myStoreGoodsDetail()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/myStoreGoodsDetail");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        OkGo.<DataResult<StoreGoodsDetailInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<StoreGoodsDetailInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<StoreGoodsDetailInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){
                                ll_nodata_address.setVisibility(View.GONE);
                                ll_data_address.setVisibility(View.VISIBLE);
                                tv_name_submitbuy.setText("联系人："+response.body().getData().getShipUser());
                                tv_phone_submitbuy.setText(response.body().getData().getShipPhone());
                                tv_address_submitbuy.setText("地址："+response.body().getData().getShipAddress());

                                String imageurl="";
                                if(!response.body().getData().getImages().equals("")) {
                                    List<String> list = Arrays.asList(response.body().getData().getImages().split(","));
                                    imageurl=list.get(0);
                                    getImageLoader().displayImage(BaseConstant.Image_URL+imageurl,iv_pic_submitbuy,getImageLoaderOptions());
                                }
//                        if(response.getData().getGoodsType().equals("1")){
//                            tv_jdprice_submitbuy.setVisibility(View.GONE);
//                            tv_rz_submitbuy.setText("认证商城");
//                            tv_rz_submitbuy.setBackgroundResource(R.drawable.shape_yellow);
//                        }else{
//                            tv_jdprice_submitbuy.setVisibility(View.VISIBLE);
//                            tv_rz_submitbuy.setText("淘宝贝");
//                            tv_rz_submitbuy.setBackgroundResource(R.drawable.shape_red);
//                        }
                                tv_goodsname_submitbuy.setText(response.body().getData().getGoodsName());
                                tv_price_submitbuy.setText("￥"+response.body().getData().getPrice());
                                tv_jdprice_submitbuy.setText("鉴定价：￥"+response.body().getData().getAuthPrice());
                                if(!response.body().getData().getPrice().equals("")){
                                    if(response.body().getData().getCouponPrice()!=null&&!response.body().getData().getCouponPrice().equals("")
                                            &&!response.body().getData().getCouponPrice().equals("null")){
                                        yhprice=Double.valueOf(response.body().getData().getCouponPrice());
                                        tv_yhprice_submitbuy.setText("-￥"+df.format(yhprice));
                                    }else{
                                        tv_yhprice_submitbuy.setText("-￥0.00");
                                    }
                                    totleprice= Arith.sub(Double.valueOf(response.body().getData().getPrice()),yhprice);
                                    if(totleprice<=0){
                                        tv_totleprice_submitbuy.setText("￥0.00");
                                    }else{
                                        tv_totleprice_submitbuy.setText("￥"+df.format(totleprice));
                                    }
                                }
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<StoreGoodsDetailInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }


    private void buyPay(String orderId)
    {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("userPay/buyPay");
        HttpParams param=new HttpParams();
        param.put("token", LocalData.getInstance().getUserInfo().getToken());
        param.put("platform", payTool+"");
        param.put("orderId",orderId);
        OkGo.<DataResult<PayInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<PayInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<PayInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){
                                if(payTool==1){
                                    AliPay(response.body().getData());
                                }else{
                                    if(!CommonUtils.isWechatInstalled(OrderBuyActivity.this)){
                                        CustomToast.show("未检测到微信客户端，请先安装");
                                        return;
                                    }
                                    PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Pay,"orderbuy");
                                    WXPay.pay(OrderBuyActivity.this, response.body().getData().getId());
                                }
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<PayInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }


    /**
     * 支付宝支付
     */
    private void AliPay(PayInfo payinfo){
        AliPay aliPay=new AliPay(OrderBuyActivity.this, new AliPay.CallBack() {
            @Override
            public void success() {
                Intent intent1=new Intent("OrderRefresh");
                sendBroadcast(intent1);
                CustomToast.show("支付成功");
                finish();
            }

            @Override
            public void fail() {
            }
        });

        aliPay.payV2(payinfo);
    }

}

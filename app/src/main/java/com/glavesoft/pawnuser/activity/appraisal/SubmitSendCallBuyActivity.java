package com.glavesoft.pawnuser.activity.appraisal;

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
import com.glavesoft.pawnuser.activity.personal.AddressActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.AddressInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.JdGoodsInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.SendCallGoodInfo;
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
import com.lzy.okgo.model.Response;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SubmitSendCallBuyActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_buy_submitbuy;
    private ImageView iv_account_alipay,iv_account_wechat;
    private int payTool = -1;  //2支付宝 3微信
    private SendCallGoodInfo sendCallGoodInfo;
//    private String type;
    private JdGoodsInfo jdGoodsInfo;

    private LinearLayout ll_address_submitbuy,ll_nodata_address,ll_data_address;

    private TextView tv_name_submitbuy,tv_phone_submitbuy,tv_address_submitbuy;

    private ImageView iv_pic_submitbuy;
    private TextView tv_goodsname_submitbuy,tv_jdprice_submitbuy,tv_price_submitbuy,tv_rz_submitbuy;

    private LinearLayout ll_yhq_submitbuy;
    private TextView tv_yhprice_submitbuy;

    private TextView tv_totleprice_submitbuy;

    private AddressInfo addressInfo;
    private String addressId="";
    private String couponId="";

    private double yhprice=0.0,totleprice=0.0;
//    private String state="";
    private boolean isfirst=true;
    DecimalFormat df  = new DecimalFormat("######0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_send_call_buy);
        setBoardCast();
        initData();
        initView();
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("wxpay_buy");
        registerReceiver(mListenerID, f);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    public  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mListenerID);
    }

    private void initData() {
//        type=getIntent().getStringExtra("type");
//        state=getIntent().getStringExtra("state");
        sendCallGoodInfo=(SendCallGoodInfo)getIntent().getSerializableExtra("sendCallGoodInfo");
    }

    private void initView() {
        setTitleBack();
        setTitleName("确认购买");

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

        tv_totleprice_submitbuy=getViewById(R.id.tv_totleprice_submitbuy);

        String imageurl="";
        if(!sendCallGoodInfo.getSellImgs().equals("")) {
            List<String> list = Arrays.asList(sendCallGoodInfo.getSellImgs().split(","));
            imageurl=list.get(0);
            getImageLoader().displayImage(BaseConstant.Image_URL+imageurl,iv_pic_submitbuy,getImageLoaderOptions());
        }

        tv_goodsname_submitbuy.setText(sendCallGoodInfo.getName());
        tv_price_submitbuy.setText("￥"+sendCallGoodInfo.getSellPrice());
        tv_jdprice_submitbuy.setText("鉴定价：￥"+sendCallGoodInfo.getAuthPrice());
//        if(sendCallGoodInfo.getSellPrice()!=null){
            //totleprice=Double.valueOf(storeGoodsInfo.getPrice())-yhprice;
            totleprice= Arith.sub(Double.valueOf(sendCallGoodInfo.getSellPrice()),yhprice);
            tv_totleprice_submitbuy.setText("￥"+df.format(totleprice));
//        }

        tv_buy_submitbuy=(TextView)findViewById(R.id.tv_buy_submitbuy);
        iv_account_alipay=(ImageView) findViewById(R.id.iv_account_alipay);
        iv_account_wechat=(ImageView) findViewById(R.id.iv_account_wechat);
        tv_buy_submitbuy.setOnClickListener(this);
        iv_account_alipay.setOnClickListener(this);
        iv_account_wechat.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myAddressList();
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent();
        switch (v.getId())
        {
            case R.id.ll_address_submitbuy:
                intent.setClass(SubmitSendCallBuyActivity.this, AddressActivity.class);
                intent.putExtra("type","select");
                startActivityForResult(intent,10);
                break;
            case R.id.ll_yhq_submitbuy:

                break;
            case R.id.tv_buy_submitbuy:
                goToPay();
                break;
            case R.id.iv_account_wechat:////微信支付
                if(!CommonUtils.isWechatInstalled(SubmitSendCallBuyActivity.this)){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        if(resultCode == 10){
            switch (requestCode) {
                case 10:
                    addressInfo=(AddressInfo)data.getSerializableExtra("AddressInfo");
                    addressId=addressInfo.getId();
                    tv_name_submitbuy.setText("联系人："+addressInfo.getName());
                    tv_phone_submitbuy.setText(addressInfo.getPhone());
                    tv_address_submitbuy.setText("地址："+addressInfo.getArea()+addressInfo.getAddress());
                    break;
                case 20:
                    couponId=data.getStringExtra("couponId");
                    if(!data.getStringExtra("money").equals("")){
                        yhprice=Double.valueOf(data.getStringExtra("money"));
                    }
                    tv_yhprice_submitbuy.setText("-￥"+df.format(yhprice));
                    totleprice= Arith.sub(Double.valueOf(sendCallGoodInfo.getSellPrice()),yhprice);
                    if(totleprice<=0){
                        tv_totleprice_submitbuy.setText("￥0.00");
                    }else{
                        tv_totleprice_submitbuy.setText("￥"+df.format(totleprice));
                    }

                    break;
            }
        }
    }

    private void goToPay(){
        if (addressId.equals("")) {
            CustomToast.show("请选择您的收货地址！");
            return;
        }

        if (payTool<0) {
            CustomToast.show("请选择支付方式！");
            return;
        }

//        CustomToast.show("支付成功,详情可在个人中心的商城订单中查看！");
//        finish();
        createOrder();
    }

    private void myAddressList()
    {
        if(isfirst){
            getlDialog().show();
        }
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/myAddressList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        OkGo.<DataResult<ArrayList<AddressInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<AddressInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<AddressInfo>>> response) {
                        if(isfirst){
                            getlDialog().dismiss();
                        }
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                ll_nodata_address.setVisibility(View.GONE);
                                ll_data_address.setVisibility(View.VISIBLE);
                                if(isfirst){
                                    isfirst=false;
                                    addressId=response.body().getData().get(0).getId();
                                    tv_name_submitbuy.setText("联系人："+response.body().getData().get(0).getName());
                                    tv_phone_submitbuy.setText(response.body().getData().get(0).getPhone());
                                    tv_address_submitbuy.setText("地址："+response.body().getData().get(0).getArea()+response.body().getData().get(0).getAddress());
                                }

                            }else{
                                ll_nodata_address.setVisibility(View.VISIBLE);
                                ll_data_address.setVisibility(View.GONE);
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<AddressInfo>>> response) {
                        if(isfirst){
                            getlDialog().dismiss();
                        }
                        showVolleyError(null);
                    }
                });
    }

    private void createOrder()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/createUserGoodsOrder");
        HttpParams param=new HttpParams();
        param.put("token", token);
//        param.put("platform", payTool+"");
        param.put("userGoodsId", sendCallGoodInfo.getId()+"");

        param.put("addressId",addressId);
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
                                buyPay(response.body().getData().getId());
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

    private void buyPay(String orderId)
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPay/buyPay");
        HttpParams param=new HttpParams();
        param.put("token", token);
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
                                    if(!CommonUtils.isWechatInstalled(SubmitSendCallBuyActivity.this)){
                                        CustomToast.show("未检测到微信客户端，请先安装");
                                        return;
                                    }
                                    PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Pay,"submitbuy");
                                    WXPay.pay(SubmitSendCallBuyActivity.this, response.body().getData().getId());
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
        AliPay aliPay=new AliPay(SubmitSendCallBuyActivity.this, new AliPay.CallBack() {
            @Override
            public void success() {
                CustomToast.show("支付成功");
                finish();
            }

            @Override
            public void fail() {
                finish();
            }
        });
        aliPay.payV2(payinfo);
    }
}

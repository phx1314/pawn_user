package com.glavesoft.pawnuser.activity.main;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.alipay.AliPay;
import com.glavesoft.alipay.util.PayInfo;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.personal.AddressActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.SelectCouponActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.AddressInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.JdGoodsInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.StoreGoodsInfo;
import com.glavesoft.pawnuser.wxapi.WXPay;
import com.glavesoft.util.Arith;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/10/24
 * @company:常州宝丰
 */
public class ShopcarSubmitActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_submit_buy;
    private ImageView iv_account_alipay, iv_account_wechat;
    private int payTool = -1;  //2支付宝 3微信
    private StoreGoodsInfo storeGoodsInfo;

    private String type;
    private JdGoodsInfo jdGoodsInfo;

    private LinearLayout ll_address_submitbuy, ll_nodata_address, ll_data_address;

    private TextView tv_name_submitbuy, tv_phone_submitbuy, tv_address_submitbuy;

    private ImageView iv_pic_submitbuy;
    private TextView tv_goodsname_submitbuy, tv_jdprice_submitbuy, tv_price_submitbuy, 
            tv_rz_submitbuy;

    private LinearLayout ll_yhq_submitbuy;
    private TextView tv_yhprice_submitbuy;

    private TextView tv_totleprice_submitbuy;
    ArrayList<String> ids;
    ArrayList<String> nums;
    private AddressInfo addressInfo;
    private String addressId = "";
    private String couponId = "";
    private ListView lv_goods;
    private MyListGoodsAdapter myListGoodsAdapter;
    private ArrayList<StoreGoodsInfo> storeGoodsInfos;
    private double yhprice = 0.0, totleprice = 0.0;
    private String state = "";
    private boolean isfirst = true;
    DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcar_submit);
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

    BroadcastReceiver mListenerID = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mListenerID);
    }

    private void initData() {
        type = getIntent().getStringExtra("type");
        state = getIntent().getStringExtra("state");
        ids=getIntent().getStringArrayListExtra("ids");
        nums=getIntent().getStringArrayListExtra("nums");
        if (type.equals("goodsdetail")) {
            storeGoodsInfo = (StoreGoodsInfo) getIntent().getSerializableExtra("storeGoodsInfo");
        } else {
            jdGoodsInfo = (JdGoodsInfo) getIntent().getSerializableExtra("jdGoodsInfo");
        }
    }

    private void initView() {
        setTitleBack();
        setTitleName("确认购买");

        ll_address_submitbuy = getViewById(R.id.ll_address_submitbuy);
        ll_nodata_address = getViewById(R.id.ll_nodata_address);
        ll_data_address = getViewById(R.id.ll_data_address);
        ll_address_submitbuy.setOnClickListener(this);

        tv_name_submitbuy = getViewById(R.id.tv_name_submitbuy);
        tv_phone_submitbuy = getViewById(R.id.tv_phone_submitbuy);
        tv_address_submitbuy = getViewById(R.id.tv_address_submitbuy);
//        tv_rz_submitbuy = getViewById(R.id.tv_rz_submitbuy);
//
//        iv_pic_submitbuy = getViewById(R.id.iv_pic_submitbuy);
//        tv_goodsname_submitbuy = getViewById(R.id.tv_goodsname_submitbuy);
//        tv_jdprice_submitbuy = getViewById(R.id.tv_jdprice_submitbuy);
//        tv_price_submitbuy = getViewById(R.id.tv_price_submitbuy);

        ll_yhq_submitbuy = getViewById(R.id.ll_yhq_submitbuy);
        tv_yhprice_submitbuy = getViewById(R.id.tv_yhprice_submitbuy);
        ll_yhq_submitbuy.setOnClickListener(this);

        tv_totleprice_submitbuy = getViewById(R.id.tv_totleprice_submitbuy);

        lv_goods= (ListView) findViewById(R.id.lv_goods);
        storeGoodsInfos=new ArrayList<>();
        myListGoodsAdapter=new MyListGoodsAdapter();
        lv_goods.setAdapter(myListGoodsAdapter);
        for (int i=0;i<ids.size();i++)
        {
            getstoreGoodsDetail(ids.get(i),Integer.valueOf(nums.get(i)));
        }


        ll_submit_buy = (LinearLayout) findViewById(R.id.ll_submit_buy);
        iv_account_alipay = (ImageView) findViewById(R.id.iv_account_alipay);
        iv_account_wechat = (ImageView) findViewById(R.id.iv_account_wechat);
        ll_submit_buy.setOnClickListener(this);
        iv_account_alipay.setOnClickListener(this);
        iv_account_wechat.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myAddressList();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_address_submitbuy:
                intent.setClass(ShopcarSubmitActivity.this, AddressActivity.class);
                intent.putExtra("type", "select");
                startActivityForResult(intent, 10);
                break;
            case R.id.ll_yhq_submitbuy:
                if (state.equals("jd")) {
                    CustomToast.show("淘宝贝中物品无法使用优惠券");
                    return;
                }
                intent.setClass(ShopcarSubmitActivity.this, SelectCouponActivity.class);
                if (type.equals("goodsdetail")) {
                    intent.putExtra("id", storeGoodsInfos.get(0).getId());
                } else {
                    intent.putExtra("id", storeGoodsInfos.get(0).getId());
                }
                startActivityForResult(intent, 20);
                break;
            case R.id.ll_submit_buy:
                goToPay();
                break;
            case R.id.iv_account_wechat://微信支付
                if (!CommonUtils.isWechatInstalled(ShopcarSubmitActivity.this)) {
                    CustomToast.show("未检测到微信客户端，请先安装");
                } else {
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
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == 10) {
            switch (requestCode) {
                case 10:
                    addressInfo = (AddressInfo) data.getSerializableExtra("AddressInfo");
                    addressId = addressInfo.getId();
                    tv_name_submitbuy.setText("联系人：" + addressInfo.getName());
                    tv_phone_submitbuy.setText(addressInfo.getPhone());
                    tv_address_submitbuy.setText("地址：" + addressInfo.getArea() + addressInfo
                            .getAddress());
                    break;
                case 20:
                    couponId = data.getStringExtra("couponId");
                    if (!data.getStringExtra("money").equals("")) {
                        yhprice = Double.valueOf(data.getStringExtra("money"));
                    }
                    tv_yhprice_submitbuy.setText("-￥" + df.format(yhprice));
                    if (type.equals("goodsdetail")) {
                        if (totleprice!=0) {
                            //totleprice=Double.valueOf(storeGoodsInfo.getPrice())-yhprice;
                            totleprice = Arith.sub(Double.valueOf(totleprice),
                                    yhprice);
                            if (totleprice <= 0) {
                                tv_totleprice_submitbuy.setText("￥0.00");
                            } else {
                                tv_totleprice_submitbuy.setText("￥" + df.format(totleprice));
                            }
                        }
                    } else {
                        if (totleprice!=0) {
                            //totleprice=Double.valueOf(jdGoodsInfo.getPrice())-yhprice;
                            totleprice = Arith.sub(Double.valueOf(totleprice),
                                    yhprice);
                            if (totleprice <= 0) {
                                tv_totleprice_submitbuy.setText("￥0.00");
                            } else {
                                tv_totleprice_submitbuy.setText("￥" + df.format(totleprice));
                            }
                        }
                    }

                    break;
            }
        }
    }

    private void goToPay() {
        if (addressId.equals("")) {
            CustomToast.show("请选择您的收货地址！");
            return;
        }

        if (payTool < 0) {
            CustomToast.show("请选择支付方式！");
            return;
        }

//        CustomToast.show("支付成功,详情可在个人中心的商城订单中查看！");
//        finish();
        //todo
        List<String> ids=new ArrayList<>();
        for (int i=0;i<storeGoodsInfos.size();i++){
            ids.add(storeGoodsInfos.get(i).getId());
        }
        List<String> couponIds=new ArrayList<>();
        if (!couponId.equals("")){
            couponIds.add(couponId);
        }

        createOrder(ids,couponIds);
    }

    private void myAddressList() {
        if (isfirst) {
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
                        if (isfirst) {
                            getlDialog().dismiss();
                        }
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                ll_nodata_address.setVisibility(View.GONE);
                                ll_data_address.setVisibility(View.VISIBLE);
                                if (isfirst) {
                                    isfirst = false;
                                    addressId = response.body().getData().get(0).getId();
                                    tv_name_submitbuy.setText("联系人：" + response.body().getData().get(0).getName());
                                    tv_phone_submitbuy.setText(response.body().getData().get(0).getPhone());
                                    tv_address_submitbuy.setText("地址：" + response.body().getData().get(0)
                                            .getArea() + response.body().getData().get(0).getAddress());
                                }

                            } else {
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
                        if (isfirst) {
                            getlDialog().dismiss();
                        }
                        showVolleyError(null);
                    }
                });
    }

    private void createOrder(List<String> ids,List<String> couponIds) {
        getlDialog().show();
        Map [] maps = new Map[ids.size()];
        for (int i=0;i<ids.size();i++){
            Map<String, String> param1 = new HashMap<>();
            if (type.equals("goodsdetail")) {
                param1.put("goodsId", ids.get(i));
                param1.put("num", nums.get(i));
            } else {
                param1.put("goodsId", ids.get(i));
            }
            if (couponIds.size()>0) {
                try {
                    param1.put("couponId", couponIds.get(i));
                }catch (Exception e){

                }

            }
            maps[i]=param1;
        }
        String url=BaseConstant.getApiPostUrl("userPay/createShopCartOrder");
        HttpParams param=new HttpParams();
        String str=new Gson().toJson(maps);
        param.put("data",str);
        param.put("token", LocalData.getInstance().getUserInfo().getToken());
        param.put("platform", payTool + "");
        param.put("addressId", addressId);

        OkGo.<DataResult<List<PayInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<List<PayInfo>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<List<PayInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null) {
                                String id="";
                                for (int i=0;i<response.body().getData().size();i++){
                                    if (i!=response.body().getData().size()-1)
                                        id+=response.body().getData().get(i).getId()+",";
                                    else
                                        id+=response.body().getData().get(i).getId();
                                }

                                Intent intent=new Intent("ShopcarRefresh");
                                sendBroadcast(intent);
                                buyPay(id);
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<List<PayInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void buyPay(String orderId) {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("userPay/buyShopCartPay");
        HttpParams param=new HttpParams();
        param.put("token", LocalData.getInstance().getUserInfo().getToken());
        param.put("platform", payTool + "");
        param.put("orderIds", orderId);
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
                            if (response.body().getData() != null) {
                                if (payTool == 1) {
                                    AliPay(response.body().getData());
                                } else {
                                    if (!CommonUtils.isWechatInstalled(ShopcarSubmitActivity.this)) {
                                        CustomToast.show("未检测到微信客户端，请先安装");
                                        return;
                                    }
                                    PreferencesUtils.setStringPreferences(BaseConstant
                                                    .AccountManager_NAME, BaseConstant.SharedPreferences_Pay,
                                            "submitbuy");
                                    WXPay.pay(ShopcarSubmitActivity.this, response.body().getData().getId());
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
    private void AliPay(PayInfo payinfo) {
        AliPay aliPay = new AliPay(ShopcarSubmitActivity.this, new AliPay.CallBack() {
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
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        MyListGoodsAdapter listAdapter = (MyListGoodsAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i,null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        listView.invalidate();
    }
    private void getstoreGoodsDetail(String id, final int num) {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("storeGoods/storeGoodsDetail");
        HttpParams param=new HttpParams();
        param.put("token", token);
        param.put("id", id);
        OkGo.<DataResult<StoreGoodsInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<StoreGoodsInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<StoreGoodsInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null) {
                                StoreGoodsInfo storeGoodsInfo = response.body().getData();
                                storeGoodsInfos.add(storeGoodsInfo);
                                totleprice+=(Double.parseDouble(storeGoodsInfo.getPrice().trim())*num);
                                myListGoodsAdapter.notifyDataSetChanged();
                                setListViewHeightBasedOnChildren(lv_goods);

                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<StoreGoodsInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    class MyListGoodsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return storeGoodsInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return storeGoodsInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v=View.inflate(ShopcarSubmitActivity.this, R.layout.item_shopcar_bug_goods, null);
            ImageView iv_pic_submitbuy= (ImageView) v.findViewById(R.id.iv_pic_submitbuy);
            TextView tv_rz_submitbuy= (TextView) v.findViewById(R.id.tv_rz_submitbuy);
            TextView tv_goodsname_submitbuy= (TextView) v.findViewById(R.id.tv_goodsname_submitbuy);
            TextView tv_jdprice_submitbuy= (TextView) v.findViewById(R.id.tv_jdprice_submitbuy);
            TextView tv_price_submitbuy= (TextView) v.findViewById(R.id.tv_price_submitbuy);
            StoreGoodsInfo storeGoodsInfo=storeGoodsInfos.get(i);
            if (state.equals("rz")) {
                tv_jdprice_submitbuy.setVisibility(View.GONE);
                ll_yhq_submitbuy.setVisibility(View.VISIBLE);
                tv_rz_submitbuy.setText("新品推荐");
                tv_rz_submitbuy.setBackgroundResource(R.drawable.shape_yellow);
            } else {
                tv_jdprice_submitbuy.setVisibility(View.VISIBLE);
                ll_yhq_submitbuy.setVisibility(View.GONE);
                tv_rz_submitbuy.setText("淘宝贝");
                tv_rz_submitbuy.setBackgroundResource(R.drawable.shape_red);
            }
            if (type.equals("goodsdetail")) {
                String imageurl = "";
//                if (!storeGoodsInfo.getImages().equals("")) {
//                    List<String> list = Arrays.asList(storeGoodsInfo.getImages().split(","));
//                    imageurl = list.get(0);
//                    getImageLoader().displayImage(BaseConstant.Image_URL + imageurl,
//                            iv_pic_submitbuy, getImageLoaderOptions());
//                }

                if (!storeGoodsInfo.getImg().equals("")) {
                    getImageLoader().displayImage(BaseConstant.Image_URL + storeGoodsInfo.getImg(),
                            iv_pic_submitbuy, getImageLoaderOptions());
                }

                tv_goodsname_submitbuy.setText(storeGoodsInfo.getTitle());
                tv_price_submitbuy.setText("￥" + storeGoodsInfo.getPrice());
                tv_jdprice_submitbuy.setText("鉴定价：￥" + storeGoodsInfo.getAuthPrice());
                if (!storeGoodsInfo.getPrice().equals("")) {
                    //totleprice=Double.valueOf(storeGoodsInfo.getPrice())-yhprice;
                    totleprice = Arith.sub(Double.valueOf(totleprice), yhprice);
                    tv_totleprice_submitbuy.setText("￥" + df.format(totleprice));
                }
            } else {
//                String imageurl = "";
//                if (!jdGoodsInfo.getImages().equals("")) {
//                    List<String> list = Arrays.asList(jdGoodsInfo.getImages().split(","));
//                    imageurl = list.get(0);
//                    getImageLoader().displayImage(BaseConstant.Image_URL + imageurl,
//                            iv_pic_submitbuy, getImageLoaderOptions());
//                }
//
//                tv_goodsname_submitbuy.setText(jdGoodsInfo.getGoodsName());
//                tv_price_submitbuy.setText("￥" + jdGoodsInfo.getPrice());
//                tv_jdprice_submitbuy.setText("鉴定价：￥" + jdGoodsInfo.getAuthPrice());
//                if (!jdGoodsInfo.getPrice().equals("")) {
//                    //totleprice=Double.valueOf(jdGoodsInfo.getPrice())-yhprice;
//                    totleprice = Arith.sub(Double.valueOf(storeGoodsInfo.getPrice()), yhprice);
//                    tv_totleprice_submitbuy.setText("￥" + df.format(totleprice));
//                }
            }

            return v;
        }
    }
}


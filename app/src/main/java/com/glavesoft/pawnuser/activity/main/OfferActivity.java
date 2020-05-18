package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.Map;

/**
 * @author 严光
 * @date: 2017/10/23
 * @company:常州宝丰
 */
public class OfferActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_submit_offer;
    private TextView tv_qprice_offer,tv_newprice_offer;

    private CheckBox cb_offer_agree;
    private TextView tv_offer_agreement;
    private EditText et_price;

    private String id="",price="",authPrice="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        initData();
        initView();
    }

    private void initData() {
        id=getIntent().getStringExtra("id");
        price=getIntent().getStringExtra("price");
        authPrice=getIntent().getStringExtra("authPrice");
    }

    private void initView() {
        setTitleBack();
        setTitleName("绝当竞拍出价");

        et_price = (EditText) findViewById(R.id.et_price);

        tv_qprice_offer = (TextView) findViewById(R.id.tv_qprice_offer);
        tv_newprice_offer = (TextView) findViewById(R.id.tv_newprice_offer);

        tv_qprice_offer.setText("￥"+authPrice);
        tv_newprice_offer.setText("￥"+price);

        cb_offer_agree=(CheckBox) findViewById(R.id.cb_offer_agree);
        tv_offer_agreement=(TextView) findViewById(R.id.tv_offer_agreement);

        tv_submit_offer = (TextView) findViewById(R.id.tv_submit_offer);
        tv_submit_offer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.tv_submit_offer:

                if(et_price.getText().toString().length()==0){
                    CustomToast.show("请输入竞拍价");
                    return;
                }

                if(Double.valueOf(et_price.getText().toString())<=Double.valueOf(price)){
                    CustomToast.show("输入的价格要大于当前价");
                    return;
                }

                if (!cb_offer_agree.isChecked()) {
                    CustomToast.show("请先阅读绝当竞买协议条款");
                    return;
                }
                storeJDGoodsJp();
                break;
            case R.id.tv_offer_agreement:

                break;
        }
    }

    //绝当商城竞拍出价
    private void storeJDGoodsJp()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("storeGoods/storeJDGoodsJp");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("price",et_price.getText().toString().trim());
        param.put("id",id);
        OkGo.<DataResult<DataInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<DataInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            Intent intent=new Intent("submitoffer");
                            sendBroadcast(intent);
                            CustomToast.show("出价成功");
                            finish();
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}

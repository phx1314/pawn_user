package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.personal.AddressActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.AddressInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/10/26
 * @company:常州宝丰
 */
public class RetrieveActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_submit_retrieve;
    private TextView tv_name_retrieve;
    private TextView tv_phone_retrieve;
    private TextView tv_ssq_retrieve;
    private TextView tv_dq_retrieve;
    private AddressInfo addressInfo;
    private String id="",addressId="";
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);
        id=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("取回信息确认");
        setTitle_right("其他地址", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(RetrieveActivity.this, AddressActivity.class);
                intent.putExtra("type","select");
                startActivityForResult(intent,10);
            }
        });

        tv_name_retrieve=(TextView)findViewById(R.id.tv_name_retrieve);
        tv_phone_retrieve=(TextView)findViewById(R.id.tv_phone_retrieve);
        tv_ssq_retrieve=(TextView)findViewById(R.id.tv_ssq_retrieve);
        tv_dq_retrieve=(TextView)findViewById(R.id.tv_dq_retrieve);

        tv_submit_retrieve=(TextView)findViewById(R.id.tv_submit_retrieve);
        tv_submit_retrieve.setOnClickListener(this);

        myAddressList();
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.tv_submit_retrieve:
                if(tv_name_retrieve.getText().toString().trim().length()==0){
                    CustomToast.show("请选择取回地址");
                    return;
                }
                getBackGoods();
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
                    tv_name_retrieve.setText(addressInfo.getName());
                    tv_phone_retrieve.setText(addressInfo.getPhone());
                    tv_ssq_retrieve.setText(addressInfo.getArea());
                    tv_dq_retrieve.setText(addressInfo.getAddress());
                    break;
            }
        }
    }

    private void myAddressList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/myAddressList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        OkGo.<DataResult<ArrayList<AddressInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<AddressInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<AddressInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                addressId=response.body().getData().get(0).getId();
                                tv_name_retrieve.setText(response.body().getData().get(0).getName());
                                tv_phone_retrieve.setText(response.body().getData().get(0).getPhone());
                                tv_ssq_retrieve.setText(response.body().getData().get(0).getArea());
                                tv_dq_retrieve.setText(response.body().getData().get(0).getAddress());
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
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    //取回
    private void getBackGoods()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/getBackGoods");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        param.put("addressId",addressId);
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
                            CustomToast.show("取回成功，详情请到个人信息中的物流信息查看");
                            Intent intent = new Intent("AppraisalRefresh");
                            sendBroadcast(intent);
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

package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.IDCardValidate;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.Map;

/**
 * @author 严光
 * @date: 2017/11/10
 * @company:常州宝丰
 */
public class AddBankCardActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_account_bankcard,et_num_bankcard,et_username_bankcard,et_idcard_bankcard;
    private LinearLayout ll_submit_bankcard;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbankcard);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("添加银行卡");
        setTitleNameEn(R.mipmap.add_bank_card);

        et_account_bankcard=(EditText) findViewById(R.id.et_account_bankcard);
        et_num_bankcard=(EditText) findViewById(R.id.et_num_bankcard);
        et_username_bankcard=(EditText) findViewById(R.id.et_username_bankcard);
        et_idcard_bankcard=(EditText) findViewById(R.id.et_idcard_bankcard);

        ll_submit_bankcard=(LinearLayout) findViewById(R.id.ll_submit_bankcard);
        ll_submit_bankcard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ll_submit_bankcard:
                add();
                break;
        }
    }

    private void add(){

        if(et_num_bankcard.getText().toString().trim().length()==0){
            CustomToast.show("请输入银行卡号");
            return;
        }

        if(et_account_bankcard.getText().toString().trim().length()==0){
            CustomToast.show("请输入预留手机号");
            return;
        }

        if (!CommonUtils.isCellphone(et_account_bankcard.getText().toString().trim())) {
            CustomToast.show("请输入正确的手机号");
            return;
        }

        if(et_username_bankcard.getText().toString().trim().length()==0){
            CustomToast.show("请输入您的真实姓名");
            return;
        }

        if(et_idcard_bankcard.getText().toString().trim().length()==0){
            CustomToast.show("请输入您的身份证号码");
            return;
        }

        String idCard=et_idcard_bankcard.getText().toString().trim();
        String validStr =   IDCardValidate.validate_effective(idCard);
        if (!idCard.equals(validStr)){
            CustomToast.show(validStr);
            return;
        }

        addMyBankCard();
    }

    //添加银行卡
    private void addMyBankCard()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/addMyBankCard");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("bankCardNo",et_num_bankcard.getText().toString().trim());
        param.put("account",et_account_bankcard.getText().toString().trim());
        param.put("userName",et_username_bankcard.getText().toString().trim());
        param.put("idCard",et_idcard_bankcard.getText().toString().trim());
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
                            CustomToast.show("添加成功");
                            Intent intent = new Intent("AddBankcardRefresh");
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

package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/6/26
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener{
    EditText et_feedback;
    TextView button_dl;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("问题反馈");
        setTitleNameEn(R.mipmap.problem_feedback);

        et_feedback  = (EditText) findViewById(R.id.et_feedback);
        button_dl  = (TextView) findViewById(R.id.button_dl);
        button_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_feedback.getText().toString().trim().length()==0){
                    CustomToast.show("请输入您的反馈内容");
                }else{
                    Feedback();
                }
            }
        });
//        setTitle_right("提交", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(et_feedback.getText().toString().trim().length()==0){
//                    CustomToast.show("请输入您的反馈内容");
//                }else{
//                    Feedback();
//                }
//            }
//        });

    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
//            case R.id.button_feedback:
//                if(et_feedback.getText().toString().trim().length()==0){
//                    CustomToast.show("请输入您的反馈内容");
//                }else{
//                    Feedback();
//                }
//                break;
        }
    }

    private void Feedback()
    {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("common/feedback");
        HttpParams param=new HttpParams();
        param.put("token", LocalData.getInstance().getUserInfo().getToken());//用户token
        param.put("content", et_feedback.getText().toString().trim());
        param.put("phone", LocalData.getInstance().getUserInfo().getAccount());
        OkGo.<DataResult>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult>() {
                    @Override
                    public void onSuccess(Response<DataResult> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            CustomToast.show("反馈成功");
                            finish();
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}

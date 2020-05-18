package com.glavesoft.pawnuser.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.DeviceUtils;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.WebActivity;
import com.glavesoft.pawnuser.base.AppManager;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.UserInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.view.CustomToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import java.util.Timer;
import java.util.TimerTask;
import cn.jpush.android.api.JPushInterface;

/**
 * @author 严光
 * @date: 2017/9/21
 * @company:常州宝丰
 */
public class RegisterActivity extends BaseActivity {
    private EditText et_reg_phone,et_reg_yzm,et_reg_psw,et_reg_pswagain,et_reg_yqm;
    private TextView bt_register;
    private TextView tv_reg_agreement,tv_reg_privacy,tv_reg_yzm;
    private CheckBox cb_reg_agree;
    private String type;
    private LinearLayout ll_reg_agree;
    private int count;
    private Timer timer;
    private TimerTask timerTask;
    private String token,userid;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int count = msg.what;
            if (count > 0) {
                tv_reg_yzm.setText(count + " 秒");
            } else {
                tv_reg_yzm.setText("获取验证码");
                tv_reg_yzm.setBackgroundResource(R.drawable.shape_login);
                tv_reg_yzm.setClickable(true);
            }
            super.handleMessage(msg);
        }
    };


    //取消读秒
    private void cancleTimer() {
        if(timer!=null){
            timer.cancel();
        }
        tv_reg_yzm.setText("获取验证码");
        tv_reg_yzm.setBackgroundResource(R.drawable.shape_login);
        tv_reg_yzm.setClickable(true);
    }

    /**
     * 短信按钮读秒
     */
    public void startCount() {
        count = 60;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (count < 0) {
                    timer.cancel();
                    return;
                }
                Message message = new Message();
                message.what = count;
                handler.sendMessage(message);
                count--;
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        type=getIntent().getStringExtra("type");
        setView();
        setListener();
    }


    private void setView(){
        setTitleBack();
        et_reg_phone=(EditText) findViewById(R.id.et_reg_phone);
        et_reg_yzm=(EditText) findViewById(R.id.et_reg_yzm);
        et_reg_psw=(EditText) findViewById(R.id.et_reg_psw);
        et_reg_pswagain=(EditText) findViewById(R.id.et_reg_pswagain);
        tv_reg_yzm=(TextView) findViewById(R.id.tv_reg_yzm);
        bt_register=(TextView) findViewById(R.id.bt_register);
        cb_reg_agree=(CheckBox) findViewById(R.id.cb_reg_agree);
        tv_reg_agreement=(TextView) findViewById(R.id.tv_reg_agreement);
        ll_reg_agree=(LinearLayout) findViewById(R.id.ll_reg_agree);
        tv_reg_privacy=(TextView) findViewById(R.id.tv_reg_privacy);

        if(type.equals("Register")){
            setTitleName("注册");
            setTitleNameEn(R.drawable.register);
            ll_reg_agree.setVisibility(View.VISIBLE);
        }else{
            if(type.equals("forgetPwd")){
                setTitleName("找回密码");
                setTitleNameEn(R.drawable.retrieve_password);
            }else{
                setTitleName("修改密码");
                setTitleNameEn(R.drawable.change_password);
            }

            bt_register.setText("确定");
            ll_reg_agree.setVisibility(View.GONE);
        }
    }

    private void setListener(){
        tv_reg_yzm.setOnClickListener(onClickListener);
        bt_register.setOnClickListener(onClickListener);
        tv_reg_agreement.setOnClickListener(onClickListener);
        tv_reg_privacy.setOnClickListener(onClickListener);
    }

    OnClickListener onClickListener =new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_reg_yzm://获取验证码
                    if (et_reg_phone.getText().toString().trim().length()==0) {
                        CustomToast.show("请输入手机号");
                    }else if (!CommonUtils.isCellphone(et_reg_phone.getText().toString().trim())) {
                        CustomToast.show("请输入正确的手机号");
                    }else {
                        tv_reg_yzm.setClickable(false);
                        //IsExistTask();;//判断手机号是否已注册接口
                        SendMessageTask();
                    }
                    break;

                case R.id.tv_reg_agreement://注册协议
                    BaseConstant.gotoAgreement(RegisterActivity.this);
                    break;
                case R.id.tv_reg_privacy://隐私政策
                    BaseConstant.gotoPrivacy(RegisterActivity.this);
                    break;
                case R.id.bt_register://注册
                    GoToRegister();
                    break;
            }
        }
    };


    private void GoToRegister(){

        if (et_reg_phone.getText().toString().trim().length()==0) {
            CustomToast.show("请输入手机号");
        }else if (et_reg_yzm.getText().toString().trim().length()==0) {
            CustomToast.show("请输入验证码");
        }else if (et_reg_psw.getText().toString().trim().length()==0) {
            CustomToast.show("请输入密码");
        }else if (et_reg_pswagain.getText().toString().trim().length()==0) {
            CustomToast.show("再次输入密码不能为空");
        }else if (!et_reg_pswagain.getText().toString().trim()
                .equals(et_reg_psw.getText().toString().trim())) {
            CustomToast.show("两次密码输入不一致");
        }else {
            if(type.equals("Register")){
                if (!cb_reg_agree.isChecked()) {
                    CustomToast.show("请先阅读并同意拍当网用户协议和隐私政策");
                    return;
                }
            }
            RegisterTask();
        }
    }

    /**
     *  发送短信验证码接口
     */
    private void SendMessageTask(){
//        String a="",b="";
//        a= BaseConstant.generateWord();
//        String word1=a.substring(0, 2);
//        String word2=a.substring(3, 8);
//        String word3=a.substring(2, 3);
//        String word4=word1+word2+word3;
//
//        String word5=word4.substring(0, 3);
//        String word6=word4.substring(3, 5);
//        String word7=word4.substring(5, 8);
//        String word8=word5+word7+word6;
//
//        String word9=BaseConstant.stringToMD5(word8);
//
//        String word10=word9.substring(0, 12);
//        String word11=word9.substring(13, 32);
//        String word12=word10+"u"+word11;
//
//        String word13=word12+BaseConstant.Key;
//
//        b=BaseConstant.stringToMD51(word13);

        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("account/getMobileMsg");
        HttpParams param=new HttpParams();
        param.put("phone",et_reg_phone.getText().toString().trim());
        if(type.equals("Register")){//1:注册,绑定手机 2:忘记密码 3:验证码登陆
            param.put("type","1");
        }else{
            param.put("type","2");
        }
        param.put("deviceid",DeviceUtils.getAndroidID());
        OkGo.<DataResult<UserInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<UserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            startCount();//开始数秒
                            tv_reg_yzm.setBackgroundResource(R.drawable.shape_regist);
                            tv_reg_yzm.setClickable(false);
                            CustomToast.show("发送成功");
                        }else {
                            cancleTimer();//取消读秒
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        cancleTimer();//取消读秒
                        CustomToast.show(response.body().getErrorMsg());
                    }
                });
    }

    /**
     *  注册接口
     */
    private void RegisterTask(){
        getlDialog().show();
        String url="";
        if(type.equals("Register")){
            url = BaseConstant.getApiPostUrl("account/regist");
        }else{
            url = BaseConstant.getApiPostUrl("account/forgetPwd");
        }
        HttpParams param=new HttpParams();
        param.put("phone",et_reg_phone.getText().toString().trim());
        param.put("code",et_reg_yzm.getText().toString().trim());
        param.put("password", md5(et_reg_psw.getText().toString().trim()));
        param.put("deviceType","1");
        param.put("deviceid", DeviceUtils.getAndroidID());
        param.put("cid",JPushInterface.getRegistrationID(getApplicationContext()));
        OkGo.<DataResult<UserInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<UserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            token=response.body().getData().getToken();
                            userid=response.body().getData().getUserid();
                            getUserInfo();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    public void getUserInfo()
    {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("home/userInfo");
        HttpParams param=new HttpParams();
        param.put("token",token);
        OkGo.<DataResult<UserInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<UserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            response.body().getData().setToken(token);
                            response.body().getData().setUserid(userid);
                            setUserAndFile(response.body().getData());

                            if(type.equals("Register")){
                                CustomToast.show("注册成功");
                                //LoginActivity.instand.finish();
                                AppManager.getAppManager().finishActivity(LoginActivity.class);
                                finish();
                            }else{
                                if(type.equals("forgetPwd")){
                                    CustomToast.show("找回密码成功");
                                    AppManager.getAppManager().finishActivity(LoginActivity.class);
                                   // LoginActivity.instand.finish();
                                    finish();
                                }else{
                                    CustomToast.show("修改密码成功");
                                    finish();
                                }
                            }
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

}

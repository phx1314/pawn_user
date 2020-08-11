package com.glavesoft.pawnuser.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.WebActivity;
import com.glavesoft.pawnuser.activity.personal.SettingActivity;
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
 * @author yanguang
 * @date 2020/3/11.
 * description：绑定手机号
 */
public class BindPhoneActivity  extends BaseActivity {

    private EditText et_bind_phone,et_bind_yzm;
    private TextView bt_bind_phone;
    private TextView tv_bind_agreement,tv_bind_privacy,tv_bind_yzm;
    private CheckBox cb_bind_agree;
    private LinearLayout ll_bind_agree;
    private int count;
    private Timer timer;
    private TimerTask timerTask;
    private String token,userid;
    private String type="";
    private String openid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone_n);
        type=getIntent().getStringExtra("type");
        openid=getIntent().getStringExtra("openid");
        setView();
        setListener();
    }


    private void setView(){
        setTitleBack();
        setTitleName("联合登录");
        setTitleNameEn(R.drawable.jointlogin);
        et_bind_phone=(EditText) findViewById(R.id.et_bind_phone);
        et_bind_yzm=(EditText) findViewById(R.id.et_bind_yzm);

        tv_bind_yzm=(TextView) findViewById(R.id.tv_bind_yzm);
        bt_bind_phone=(TextView) findViewById(R.id.bt_bind_phone);
        cb_bind_agree=(CheckBox) findViewById(R.id.cb_bind_agree);
        tv_bind_agreement=(TextView) findViewById(R.id.tv_bind_agreement);
        ll_bind_agree=(LinearLayout) findViewById(R.id.ll_bind_agree);
        tv_bind_privacy=(TextView) findViewById(R.id.tv_bind_privacy);
    }

    private void setListener(){
        tv_bind_yzm.setOnClickListener(onClickListener);
        bt_bind_phone.setOnClickListener(onClickListener);
        tv_bind_agreement.setOnClickListener(onClickListener);
        tv_bind_privacy.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener =new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_bind_yzm://获取验证码
                    if (et_bind_phone.getText().toString().trim().length()==0) {
                        CustomToast.show("请输入手机号");
                    }else if (!CommonUtils.isCellphone(et_bind_phone.getText().toString().trim())) {
                        CustomToast.show("请输入正确的手机号");
                    }else {
                        tv_bind_yzm.setClickable(false);
                        //IsExistTask();;//判断手机号是否已注册接口
                        SendMessageTask();
                    }
                    break;

                case R.id.tv_bind_agreement://注册协议
                    BaseConstant.gotoAgreement(BindPhoneActivity.this);
                    break;
                case R.id.tv_bind_privacy://隐私政策
                    BaseConstant.gotoPrivacy(BindPhoneActivity.this);
                    break;
                case R.id.bt_bind_phone://绑定
                    goToBind();
                    break;
            }
        }
    };

    private void goToBind(){

        if (et_bind_phone.getText().toString().trim().length()==0) {
            CustomToast.show("请输入手机号");
        }else if (et_bind_yzm.getText().toString().trim().length()==0) {
            CustomToast.show("请输入验证码");
        }else {
            if (!cb_bind_agree.isChecked()) {
                CustomToast.show("请先阅读并同意拍当网用户协议和隐私政策");
                return;
            }
            bindMobile();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int count = msg.what;
            if (count > 0) {
                tv_bind_yzm.setText(count + " 秒");
            } else {
                tv_bind_yzm.setText("获取验证码");
                tv_bind_yzm.setBackgroundResource(R.drawable.shape_login);
                tv_bind_yzm.setClickable(true);
            }
            super.handleMessage(msg);
        }
    };


    //取消读秒
    private void cancleTimer() {
        if(timer!=null){
            timer.cancel();
        }
        tv_bind_yzm.setText("获取验证码");
        tv_bind_yzm.setBackgroundResource(R.drawable.shape_login);
        tv_bind_yzm.setClickable(true);
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

    /**
     *  发送短信验证码接口
     */
    private void SendMessageTask(){
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("userSms/sendSms");
        HttpParams param=new HttpParams();
        param.put("phone",et_bind_phone.getText().toString().trim());
        if (type.equals("wx")){
            param.put("wxOpenid",openid);
        }else if (type.equals("qq")){
            param.put("qqOpenid",openid);
        }
        param.put("deviceid", DeviceUtils.getAndroidID());
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
                            tv_bind_yzm.setBackgroundResource(R.drawable.shape_regist);
                            tv_bind_yzm.setClickable(false);
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
     *  绑定接口
     */
    private void bindMobile(){
        getlDialog().show();
        String url= BaseConstant.getApiPostUrl("userSms/bindMobile");

        HttpParams param=new HttpParams();
        param.put("phone",et_bind_phone.getText().toString().trim());
        param.put("code",et_bind_yzm.getText().toString().trim());
        if (type.equals("wx")){
            param.put("wxOpenid",openid);
        }else if (type.equals("qq")){
            param.put("qqOpenid",openid);
        }
        param.put("deviceType","1");
        param.put("deviceid", DeviceUtils.getAndroidID());
        param.put("cid", JPushInterface.getRegistrationID(getApplicationContext()));
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
                .tag(this)
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
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            AppManager.finishActivity(LoginActivity.class);
                            finish();
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

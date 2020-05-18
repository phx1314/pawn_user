package com.glavesoft.pawnuser.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.UserInfo;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.blankj.utilcode.util.DeviceUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener, Handler.Callback
{
    private EditText et_phone;
    private EditText et_pwd;
    private TextView btn_login,bt_register,tv_forgetPwd;
    private ImageView iv_login;
    private LinearLayout ll_wx_login,ll_qq_login;

    private boolean isChecked = true;
    private String phone="",password="";
    public static LoginActivity instand;
    private String token,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ShareSDK.initSDK(this);
        instand=this;
        initView();
    }

    private void initView()
    {

        setTitleBack();
        setTitleName("登录");
        setTitleNameEn(R.drawable.signin);

        iv_login = (ImageView) findViewById(R.id.iv_login);
        et_phone = (EditText) findViewById(R.id.dl_phone);
        et_pwd = (EditText) findViewById(R.id.dl_pwd);
        btn_login = (TextView) findViewById(R.id.button_dl);
        bt_register = (TextView) findViewById(R.id.bt_register);
        tv_forgetPwd = (TextView) findViewById(R.id.tv_forgetPwd);
        ll_wx_login = (LinearLayout) findViewById(R.id.ll_wx_login);
        ll_qq_login = (LinearLayout) findViewById(R.id.ll_qq_login);
        RandomTx();
        et_phone.setText(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "account", ""));
        et_pwd.setText(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "account_passage", ""));

        btn_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        tv_forgetPwd.setOnClickListener(this);
        ll_wx_login.setOnClickListener(this);
        ll_qq_login.setOnClickListener(this);
    }

    private void RandomTx(){
        Random rand = new Random();
        int randNum = rand.nextInt(4);
        if (randNum==0){
            iv_login.setImageResource(R.mipmap.mryi);
        }else  if (randNum==1){
            iv_login.setImageResource(R.mipmap.mrer);
        }else  if (randNum==2){
            iv_login.setImageResource(R.mipmap.mrsan);
        }else  if (randNum==3){
            iv_login.setImageResource(R.mipmap.mrsi);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_dl:
                loginByPhone();
                break;
            case R.id.bt_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("type", "Register");
                startActivity(intent);
                break;
            case R.id.tv_forgetPwd:
                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                intent1.putExtra("type", "forgetPwd");
                startActivity(intent1);
                break;
            case R.id.ll_wx_login:
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.ll_qq_login:
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                authorize(qq);
                break;
        }
    }

    private Handler uiHandler = new Handler(this);
    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    private void authorize(Platform plat) {
        getlDialog().show();
//        // 判断指定平台是否已经完成授权
        if (plat.isAuthValid()) {
            //取消授权
            plat.removeAccount(true);
        }
        //监听
        plat.setPlatformActionListener(this);
//        // true不使用SSO授权，false使用SSO授权
        plat.SSOSetting(false);
        // 获取用户资料
        plat.showUser(null);
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MSG_USERID_FOUND:
                CustomToast.show(getString(R.string.wx_toast_login));
                break;
            case MSG_LOGIN:
                CustomToast.show(getString(R.string.wx_toast_loginthird));
                break;
            case MSG_AUTH_CANCEL:
                CustomToast.show(getString(R.string.wx_toast_thirdcancel));
                break;
            case MSG_AUTH_ERROR:
                if (message.obj != null) {
                    String expName = message.obj.getClass().getSimpleName();
                    String messgae = "";
                    if ("WechatClientNotExistException".equalsIgnoreCase(expName) || "WechatTimelineNotSupportedException".equals(expName) || "WechatFavoriteNotSupportedException".equals(expName)) {
                        messgae = getString(R.string.wx_toast_wxinstall);

                    } else if ("QQClientNotExistException".equalsIgnoreCase(expName)) {
                        messgae = getString(R.string.wx_toast_wxupdate);
                    }

                    CustomToast.show(messgae);
                } else {
                    CustomToast.show(getString(R.string.wx_toast_authorization));

                    ((Throwable) message.obj).printStackTrace();
                }
                break;
            case MSG_AUTH_COMPLETE:
                CustomToast.show(getString(R.string.wx_toast_authorizationok));
                loginByThirdparty((Platform) message.obj);
                break;
        }
        return false;
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        getlDialog().dismiss();
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = uiHandler.obtainMessage();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = platform;
            uiHandler.sendMessage(msg);
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        getlDialog().dismiss();
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_ERROR;
            msg.obj = throwable;
            uiHandler.sendMessage(msg);
        }
    }

    @Override
    public void onCancel(Platform platform, int action) {
        getlDialog().dismiss();
        if (action == Platform.ACTION_USER_INFOR) {
            uiHandler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }

    private void loginByPhone()
    {
        phone = et_phone.getText().toString().trim();
        if (phone.length() == 0)
        {
            CustomToast.show("请输入账号");
            et_phone.requestFocus();
            return;
        }

        password = et_pwd.getText().toString().trim();
        if (password.length() ==0)
        {
            CustomToast.show("密码不能为空");
            et_pwd.requestFocus();
            return;
        }

        login();
    }

    private void login()
    {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("account/loginByPwd");
        HttpParams param=new HttpParams();
        param.put("phone",phone);
        param.put("password", md5(password));
        param.put("deviceType","1");
        param.put("deviceid", DeviceUtils.getAndroidID());
        param.put("cid",JPushInterface.getRegistrationID(getApplicationContext()));
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
                            token=response.body().getData().getToken();
                            userid=response.body().getData().getUserid();
                            PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, "account", phone);
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

    /**
     * 第三方登录
     * @param platform
     */
    private void loginByThirdparty(Platform platform) {
        if (platform.getDb().getPlatformNname().equals(Wechat.NAME)){
            isNeedBindMobile("",platform.getDb().getUserId());
        }else if (platform.getDb().getPlatformNname().equals(QQ.NAME)){
            isNeedBindMobile(platform.getDb().getUserId(),"");
        }
    }
    /**
     * 是否需要绑定手机号
     */
    private void isNeedBindMobile(final String qqOpenid, final String wxOpenid)
    {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("userSms/isNeedBindMobile");
        final HttpParams param=new HttpParams();
        if (!StringUtils.isEmpty(qqOpenid)){
            param.put("qqOpenid",qqOpenid);
        }else if (!StringUtils.isEmpty(wxOpenid)){
            param.put("wxOpenid", wxOpenid);
        }
        param.put("deviceType","1");
        param.put("deviceid", DeviceUtils.getAndroidID());
        param.put("cid",JPushInterface.getRegistrationID(getApplicationContext()));
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
                            if (response.body().getData()==null){
                                Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                                if (!StringUtils.isEmpty(qqOpenid)){
                                    intent.putExtra("type", "qq");
                                    intent.putExtra("openid", qqOpenid);
                                }else if (!StringUtils.isEmpty(wxOpenid)){
                                    intent.putExtra("type", "wx");
                                    intent.putExtra("openid", wxOpenid);
                                }
                                startActivity(intent);
                            }else{
                                token=response.body().getData().getToken();
                                userid=response.body().getData().getUserid();
                                PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, "account", phone);
                                getUserInfo();
                            }

                        }else if(response.body().getErrorCode()== -1){
                            Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                            if (!StringUtils.isEmpty(qqOpenid)){
                                intent.putExtra("type", "qq");
                                intent.putExtra("openid", qqOpenid);
                            }else if (!StringUtils.isEmpty(wxOpenid)){
                                intent.putExtra("type", "wx");
                                intent.putExtra("openid", wxOpenid);
                            }
                            startActivity(intent);
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

//    private long secondTime = 0;
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            if ((System.currentTimeMillis() - secondTime) > 2000)
//            {
//                secondTime = System.currentTimeMillis();
//                CustomToast.show("再按一次退出程序");
//            } else
//            {
//                finish();
//                AppManager.getAppManager().exitApp();
//            }
//
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
}

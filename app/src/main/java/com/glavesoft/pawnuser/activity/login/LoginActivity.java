package com.glavesoft.pawnuser.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.AppManager;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.UserInfo;
import com.glavesoft.util.CommonUtils;
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
import com.mdx.framework.service.subscriber.HttpResultSubscriberListener;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener, Handler.Callback {
    public ImageView mImageView_back;
    public TextView mTextView_dl;
    public TextView mTextView_zc;
    public EditText dl_phone;
    public EditText dl_pwd;
    public LinearLayout mLinearLayout_login;
    public EditText et_reg_phone;
    public TextView tv_reg_yzm;
    public EditText et_reg_yzm;
    public EditText et_reg_psw;
    public EditText et_reg_pswagain;
    public CheckBox cb_reg_agree;
    public TextView tv_reg_agreement;
    public TextView tv_reg_privacy;
    public LinearLayout ll_reg_agree;
    public TextView bt_register;
    public LinearLayout mLinearLayout_register;
    private EditText et_phone;
    private EditText et_pwd;
    private TextView btn_login, tv_forgetPwd;
    private ImageView iv_login;
    private LinearLayout ll_wx_login, ll_qq_login;

    private boolean isChecked = true;
    private String phone = "", password = "";
    public static LoginActivity instand;
    private String token, userid;
    private int count;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int count = msg.what;
            if (count > 0) {
                tv_reg_yzm.setText(count + " 秒");
            } else {
                tv_reg_yzm.setText("获取验证码");
                tv_reg_yzm.setClickable(true);
            }
            super.handleMessage(msg);
        }
    };


    //取消读秒
    private void cancleTimer() {
        if (timer != null) {
            timer.cancel();
        }
        tv_reg_yzm.setText("获取验证码");
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
        setContentView(R.layout.activity_login_n);
        ShareSDK.initSDK(this);
        instand = this;
        initView();
    }

    private void initView() {
        mImageView_back = (ImageView) findViewById(R.id.mImageView_back);
        mTextView_dl = (TextView) findViewById(R.id.mTextView_dl);
        mTextView_zc = (TextView) findViewById(R.id.mTextView_zc);
        tv_forgetPwd = (TextView) findViewById(R.id.tv_forgetPwd);
        ll_wx_login = (LinearLayout) findViewById(R.id.ll_wx_login);
        ll_qq_login = (LinearLayout) findViewById(R.id.ll_qq_login);
        mLinearLayout_login = (LinearLayout) findViewById(R.id.mLinearLayout_login);
        et_reg_phone = (EditText) findViewById(R.id.et_reg_phone);
        tv_reg_yzm = (TextView) findViewById(R.id.tv_reg_yzm);
        et_reg_yzm = (EditText) findViewById(R.id.et_reg_yzm);
        et_reg_psw = (EditText) findViewById(R.id.et_reg_psw);
        et_reg_pswagain = (EditText) findViewById(R.id.et_reg_pswagain);
        cb_reg_agree = (CheckBox) findViewById(R.id.cb_reg_agree);
        tv_reg_agreement = (TextView) findViewById(R.id.tv_reg_agreement);
        tv_reg_privacy = (TextView) findViewById(R.id.tv_reg_privacy);
        ll_reg_agree = (LinearLayout) findViewById(R.id.ll_reg_agree);
        bt_register = (TextView) findViewById(R.id.bt_register);
        mLinearLayout_register = (LinearLayout) findViewById(R.id.mLinearLayout_register);
        mImageView_back.setOnClickListener(v -> {
            finish();

        });
        mTextView_dl.post (new Runnable() {
            @Override
            public void run() {
                mTextView_dl.performClick();
            }
        } );
        mTextView_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDl();
            }
        });
        mTextView_zc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView_zc.setTextColor(Color.parseColor("#283962"));
                TextPaint tp = mTextView_zc.getPaint();
                tp.setFakeBoldText(true);
                mTextView_dl.setBackgroundResource(0);
                mTextView_dl.setTextColor(Color.parseColor("#555555"));
                TextPaint tp2 = mTextView_dl.getPaint();
                tp2.setFakeBoldText(false);
                mTextView_zc.setBackgroundResource(R.drawable.shape_white_rz);
                mLinearLayout_login.setVisibility(View.GONE);
                mLinearLayout_register.setVisibility(View.VISIBLE);
            }
        });
        iv_login = (ImageView) findViewById(R.id.iv_login);
        et_phone = (EditText) findViewById(R.id.dl_phone);
        et_pwd = (EditText) findViewById(R.id.dl_pwd);
        btn_login = (TextView) findViewById(R.id.button_dl);
        RandomTx();
        et_phone.setText(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "account", ""));
        et_pwd.setText(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "account_passage", ""));
        tv_reg_yzm.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_reg_yzm.getPaint().setAntiAlias(true);//抗锯齿
        btn_login.setOnClickListener(this);
        tv_forgetPwd.setOnClickListener(this);
        ll_wx_login.setOnClickListener(this);
        ll_qq_login.setOnClickListener(this);
        tv_reg_yzm.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        tv_reg_agreement.setOnClickListener(this);
        tv_reg_privacy.setOnClickListener(this);
    }

    void goDl() {
        mTextView_dl.setTextColor(Color.parseColor("#283962"));
        TextPaint tp = mTextView_dl.getPaint();
        tp.setFakeBoldText(true);
        mTextView_dl.setBackgroundResource(R.drawable.shape_white_rz);
        mTextView_zc.setTextColor(Color.parseColor("#555555"));
        TextPaint tp2 = mTextView_zc.getPaint();
        tp2.setFakeBoldText(false);
        mTextView_zc.setBackgroundResource(0);
        mLinearLayout_login.setVisibility(View.VISIBLE);
        mLinearLayout_register.setVisibility(View.GONE);
    }

    private void RandomTx() {
        Random rand = new Random();
        int randNum = rand.nextInt(4);
        if (randNum == 0) {
            iv_login.setImageResource(R.mipmap.mryi);
        } else if (randNum == 1) {
            iv_login.setImageResource(R.mipmap.mrer);
        } else if (randNum == 2) {
            iv_login.setImageResource(R.mipmap.mrsan);
        } else if (randNum == 3) {
            iv_login.setImageResource(R.mipmap.mrsi);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_dl:
                loginByPhone();
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
            case R.id.tv_reg_yzm://获取验证码
                if (et_reg_phone.getText().toString().trim().length() == 0) {
                    CustomToast.show("请输入手机号");
                } else if (!CommonUtils.isCellphone(et_reg_phone.getText().toString().trim())) {
                    CustomToast.show("请输入正确的手机号");
                } else {
                    tv_reg_yzm.setClickable(false);
                    //IsExistTask();;//判断手机号是否已注册接口
                    SendMessageTask();
                }
                break;

            case R.id.tv_reg_agreement://注册协议
                BaseConstant.gotoAgreement(this);
                break;
            case R.id.tv_reg_privacy://隐私政策
                BaseConstant.gotoPrivacy(this);
                break;
            case R.id.bt_register://注册
                GoToRegister();
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

    private void loginByPhone() {
        phone = et_phone.getText().toString().trim();
        if (phone.length() == 0) {
            CustomToast.show("请输入账号");
            et_phone.requestFocus();
            return;
        }

        password = et_pwd.getText().toString().trim();
        if (password.length() == 0) {
            CustomToast.show("密码不能为空");
            et_pwd.requestFocus();
            return;
        }

        login();
    }

    private void login() {
        getlDialog().show();
        String url = BaseConstant.getApiPostUrl("account/loginByPwd");
        HttpParams param = new HttpParams();
        param.put("phone", phone);
        param.put("password", md5(password));
        param.put("deviceType", "1");
        param.put("deviceid", DeviceUtils.getAndroidID());
        param.put("cid", JPushInterface.getRegistrationID(getApplicationContext()));
        OkGo.<DataResult<UserInfo>>post(url)
                .tag(this)
                .params(param)
                .execute(new JsonCallback<DataResult<UserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            token = response.body().getData().getToken();
                            userid = response.body().getData().getUserid();
                            PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, "account", phone);
                            getUserInfo();
                        } else {
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

    private void GoToRegister() {

        if (et_reg_phone.getText().toString().trim().length() == 0) {
            CustomToast.show("请输入手机号");
        } else if (et_reg_yzm.getText().toString().trim().length() == 0) {
            CustomToast.show("请输入验证码");
        } else if (et_reg_psw.getText().toString().trim().length() == 0) {
            CustomToast.show("请输入密码");
        } else if (et_reg_pswagain.getText().toString().trim().length() == 0) {
            CustomToast.show("再次输入密码不能为空");
        } else if (!et_reg_pswagain.getText().toString().trim()
                .equals(et_reg_psw.getText().toString().trim())) {
            CustomToast.show("两次密码输入不一致");
        } else {
            if (!cb_reg_agree.isChecked()) {
                CustomToast.show("请先阅读并同意拍当网用户协议和隐私政策");
                return;
            }
            RegisterTask();
        }
    }

    /**
     * 发送短信验证码接口
     */
    private void SendMessageTask() {
        getlDialog().show();
        String url = BaseConstant.getApiPostUrl("account/getMobileMsg");
        HttpParams param = new HttpParams();
        param.put("phone", et_reg_phone.getText().toString().trim());
        param.put("type", "1");
        param.put("deviceid", DeviceUtils.getAndroidID());
        OkGo.<DataResult<UserInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<UserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            startCount();//开始数秒
                            tv_reg_yzm.setClickable(false);
                            CustomToast.show("发送成功");
                        } else {
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
     * 注册接口
     */
    private void RegisterTask() {
        getlDialog().show();
        String url = BaseConstant.getApiPostUrl("account/regist");
        HttpParams param = new HttpParams();
        param.put("phone", et_reg_phone.getText().toString().trim());
        param.put("code", et_reg_yzm.getText().toString().trim());
        param.put("password", md5(et_reg_psw.getText().toString().trim()));
        param.put("deviceType", "1");
        param.put("deviceid", DeviceUtils.getAndroidID());
        param.put("cid", JPushInterface.getRegistrationID(getApplicationContext()));
        OkGo.<DataResult<UserInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<UserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            token = response.body().getData().getToken();
                            userid = response.body().getData().getUserid();
                            getUserInfo1();
                        } else {
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

    public void getUserInfo1() {
        getlDialog().show();
        String url = BaseConstant.getApiPostUrl("home/userInfo");
        HttpParams param = new HttpParams();
        param.put("token", token);
        OkGo.<DataResult<UserInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<UserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            response.body().getData().setToken(token);
                            response.body().getData().setUserid(userid);
                            setUserAndFile(response.body().getData());

                            CustomToast.show("注册成功");
                            //LoginActivity.instand.finish();
                            AppManager.getAppManager().finishActivity(LoginActivity.class);
                            finish();
                        } else {
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

    public void getUserInfo() {
        getlDialog().show();
        String url = BaseConstant.getApiPostUrl("home/userInfo");
        HttpParams param = new HttpParams();
        param.put("token", token);
        OkGo.<DataResult<UserInfo>>post(url)
                .tag(this)
                .params(param)
                .execute(new JsonCallback<DataResult<UserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            response.body().getData().setToken(token);
                            response.body().getData().setUserid(userid);
                            setUserAndFile(response.body().getData());
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
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
     *
     * @param platform
     */
    private void loginByThirdparty(Platform platform) {
        if (platform.getDb().getPlatformNname().equals(Wechat.NAME)) {
            isNeedBindMobile("", platform.getDb().getUserId());
        } else if (platform.getDb().getPlatformNname().equals(QQ.NAME)) {
            isNeedBindMobile(platform.getDb().getUserId(), "");
        }
    }

    /**
     * 是否需要绑定手机号
     */
    private void isNeedBindMobile(final String qqOpenid, final String wxOpenid) {
        getlDialog().show();
        String url = BaseConstant.getApiPostUrl("userSms/isNeedBindMobile");
        final HttpParams param = new HttpParams();
        if (!StringUtils.isEmpty(qqOpenid)) {
            param.put("qqOpenid", qqOpenid);
        } else if (!StringUtils.isEmpty(wxOpenid)) {
            param.put("wxOpenid", wxOpenid);
        }
        param.put("deviceType", "1");
        param.put("deviceid", DeviceUtils.getAndroidID());
        param.put("cid", JPushInterface.getRegistrationID(getApplicationContext()));
        OkGo.<DataResult<UserInfo>>post(url)
                .tag(this)
                .params(param)
                .execute(new JsonCallback<DataResult<UserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            if (response.body().getData() == null) {
                                Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                                if (!StringUtils.isEmpty(qqOpenid)) {
                                    intent.putExtra("type", "qq");
                                    intent.putExtra("openid", qqOpenid);
                                } else if (!StringUtils.isEmpty(wxOpenid)) {
                                    intent.putExtra("type", "wx");
                                    intent.putExtra("openid", wxOpenid);
                                }
                                startActivity(intent);
                            } else {
                                token = response.body().getData().getToken();
                                userid = response.body().getData().getUserid();
                                PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, "account", phone);
                                getUserInfo();
                            }

                        } else if (response.body().getErrorCode() == -1) {
                            Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                            if (!StringUtils.isEmpty(qqOpenid)) {
                                intent.putExtra("type", "qq");
                                intent.putExtra("openid", qqOpenid);
                            } else if (!StringUtils.isEmpty(wxOpenid)) {
                                intent.putExtra("type", "wx");
                                intent.putExtra("openid", wxOpenid);
                            }
                            startActivity(intent);
                        } else {
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

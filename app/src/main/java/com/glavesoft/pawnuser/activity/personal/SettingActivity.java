package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.activity.login.RegisterActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.UserInfo;
import com.glavesoft.util.FileUtils;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import java.io.File;

/**
 * Created by Administrator on 2017/8/23.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout setting_clear, setting_feedback,setting_exit,setting_password,setting_about;
    private TextView setting_size;
    private TextView tv_register_agreement,tv_privacy_agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        setTitleName("设置");
        setTitleBack();
        setTitleNameEn(R.mipmap.set_up);
        //清除缓存
        setting_clear = (RelativeLayout) findViewById(R.id.setting_clear);
        setting_clear.setOnClickListener(this);
        setting_size = (TextView) findViewById(R.id.setting_size);
        requestReadPermissions(new CheckPermListener() {
            @Override
            public void superREADPermission() {
                long size = FileUtils.getFileSize(new File(FileUtils.CACHE_SAVE_PATH));
                setting_size.setText(FileUtils.getFormatSize(Double.parseDouble(size + "")));
            }
        });

        //问题反馈
        setting_feedback = (RelativeLayout) findViewById(R.id.setting_feedback);
        setting_feedback.setOnClickListener(this);

        //退出登录
        setting_exit= (RelativeLayout) findViewById(R.id.setting_exit);
        setting_exit.setOnClickListener(this);
//        if(LocalData.getInstance().getUserInfo().getToken()==null||LocalData.getInstance().getUserInfo().getToken().equals("")){
//            setting_exit.setVisibility(View.GONE);
//        }

        setting_password= (RelativeLayout) findViewById(R.id.setting_password);
        setting_password.setOnClickListener(this);

        setting_about= (RelativeLayout) findViewById(R.id.setting_about);
        setting_about.setOnClickListener(this);

        tv_register_agreement= (TextView) findViewById(R.id.tv_register_agreement);
        tv_privacy_agreement= (TextView) findViewById(R.id.tv_privacy_agreement);

        tv_register_agreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_register_agreement.getPaint().setAntiAlias(true);//抗锯齿
        tv_privacy_agreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_privacy_agreement.getPaint().setAntiAlias(true);//抗锯齿

        tv_register_agreement.setOnClickListener(this);
        tv_privacy_agreement.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_clear:
                requestReadPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        //清除缓存
                        File directory = getCacheDir();
                        if (directory != null && directory.exists() && directory.isDirectory()) {
                            for (File item : directory.listFiles()) {
                                item.delete();
                            }
                        }
                        FileUtils.delFile(new File(FileUtils.CACHE_SAVE_PATH));
                        //清除图片缓存
                        getImageLoader().clearDiscCache();
                        getImageLoader().clearMemoryCache();
                        long size = FileUtils.getFileSize(new File(FileUtils.CACHE_SAVE_PATH));
                        setting_size.setText(FileUtils.getFormatSize(Double.parseDouble(size + "")));
                        CustomToast.show("清除成功");
                    }
                });
                break;
            case R.id.setting_feedback:
                startActivity(new Intent(SettingActivity.this, FeedBackActivity.class));
                break;
            case R.id.setting_exit:
                if(LocalData.getInstance().getUserInfo().getToken()==null||LocalData.getInstance().getUserInfo().getToken().equals("")){
                    CustomToast.show("尚未登录");
                    return;
                }

                showPopupWindow();
                break;
            case R.id.setting_password:
                if(BaseConstant.isLogin()){
                    Intent intent1 = new Intent(SettingActivity.this, RegisterActivity.class);
                    intent1.putExtra("type", "forgetPwd1");
                    startActivity(intent1);
                }else{
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }

                break;
            case R.id.setting_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.tv_register_agreement:
                BaseConstant.gotoAgreement(SettingActivity.this);
                break;
            case R.id.tv_privacy_agreement:
                BaseConstant.gotoPrivacy(SettingActivity.this);
                break;
        }

    }

    private PopupWindow popupWindo;
    public void showPopupWindow()
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.pw_dialog3, null);
        Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button)view.findViewById(R.id.btn_ok);
        ((TextView)view.findViewById(R.id.tv_content)).setText("确认退出吗？");

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
                loginout();
            }
        });
        Display display = getWindowManager().getDefaultDisplay();
        popupWindo = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindo.setOutsideTouchable(true);
        popupWindo.setFocusable(true);
        fitPopupWindowOverStatusBar(popupWindo);
        popupWindo.setBackgroundDrawable(new ColorDrawable());
        popupWindo.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    //退出登录
    private void loginout(){
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("account/logout");
        HttpParams param=new HttpParams();
        param.put("token", token);
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

                        PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_LastLogin,null);
                        CustomToast.show("退出成功");
                        finish();
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}

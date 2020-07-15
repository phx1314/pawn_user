package com.glavesoft.pawnuser.activity.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.glavesoft.F;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.activity.personal.FeedBackActivity;
import com.glavesoft.pawnuser.adapter.FragmentTabAdapter;
import com.glavesoft.pawnuser.base.AppManager;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.fragment.HomeFragment;
import com.glavesoft.pawnuser.fragment.PersonalFragment;
import com.glavesoft.pawnuser.fragment.ShoppingCartFragment;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.UpdateInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.HttpUtil;
import com.glavesoft.view.BAlertDialog;
import com.glavesoft.view.CustomToast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.mdx.framework.service.subscriber.S;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.permissions.PermissionRequest;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.glavesoft.util.HttpUtil.installApk;

/**
 * @author 严光
 * @date: 2018/6/13
 * @company:常州宝丰
 */
public class MainActivity extends BaseActivity {
    private Context context = this;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentTabAdapter tabAdapter;
    private RadioGroup main_rg_tab;
    private RadioButton tab_rb_1, tab_rb_2, tab_rb_3, tab_rb_4, tab_rb_5;
    UpdateInfo updateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionRequest() {
            @Override
            public void onGrant(String[] var1, int[] var2) {

            }
        });
//        installApk(this);
        setContentView(R.layout.activity_main2);
        initView();
        if (getIntent().hasExtra("type")) {
            String type = getIntent().getStringExtra("type");
            Intent intent = new Intent();
            if (type.equals("push")) {
                intent.setClass(MainActivity.this, MessageActivity.class);
                startActivity(intent);
            } else if (type.equals("Crowding")) {
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        }

        CommonUtils.getDeviceId(MainActivity.this);

        requestReadPermissions(new CheckPermListener() {
            @Override
            public void superREADPermission() {
                Intent intent = new Intent("Permission_Refresh");
                sendBroadcast(intent);
            }
        });

        if (BaseConstant.isLogin()) {
            isNeedComment();
        }
        checkUpdate();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://有更新
                    if (!isFinishing()) {
                        UpdateDialog();
                    }
                    break;
                case 1://无更新
                    break;
            }

        }
    };

    private void checkUpdate() {
        String url = BaseConstant.getApiPostUrl("common/appVerion");
        HttpParams param = new HttpParams();
        param.put("deviceType", "1");//1:android 2:ios
        OkGo.<DataResult<UpdateInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<UpdateInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UpdateInfo>> response) {
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            updateInfo = response.body().getData();

                            try {
                                PackageManager pm = MainActivity.this.getPackageManager();
                                PackageInfo pi = pm.getPackageInfo(MainActivity.this.getPackageName(), 0);
                                if (updateInfo != null && (updateInfo.getVersion() > pi.versionCode)) {
                                    if (!TextUtils.isEmpty(updateInfo.apkFile)) {
                                        HttpUtil.updateApk(MainActivity.this, updateInfo.apkFile,updateInfo.isFlag);
                                    } else {
                                        handler.sendEmptyMessage(0);//有更新
                                    }
                                } else {
                                    handler.sendEmptyMessage(1);//无更新
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<UpdateInfo>> response) {
                        showVolleyError(null);
                    }
                });
    }


    protected void UpdateDialog() {
        new BAlertDialog(this, false).setMessage(updateInfo.getInfo(), false)
                .setYesButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateInfo.getUrl()));
                            startActivity(intent);
                        } catch (Exception e) {

                        }
                    }
                }, false).setNoButton(getResources().getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    private void initView() {
        fragments.add(HomeFragment.newInstance(fragments.size()));//首页
        fragments.add(ShoppingCartFragment.newInstance(fragments.size()));//
        fragments.add(PersonalFragment.newInstance(fragments.size()));//我的

        main_rg_tab = (RadioGroup) findViewById(R.id.main_rg_tab);
        tab_rb_1 = (RadioButton) findViewById(R.id.main_rb_home);
        tab_rb_4 = (RadioButton) findViewById(R.id.main_rb_gwc);
        tab_rb_5 = (RadioButton) findViewById(R.id.main_rb_personal);

        tabAdapter = new FragmentTabAdapter(this, fragments, R.id.main_fl, main_rg_tab, BaseConstant.index);

        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {

            }
        });

        tab_rb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BaseConstant.index = 0;
            }
        });

//        tab_rb_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                BaseConstant.index= 1;
//            }
//        });
//
//        tab_rb_3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                BaseConstant.index = 2;
//            }
//        });

        tab_rb_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BaseConstant.index = 3;
            }
        });

        tab_rb_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BaseConstant.index = 4;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = BaseConstant.index;
        switch (id) {
            case 0:
                tab_rb_1.setChecked(true);
                break;
//            case 1:
//                tab_rb_2.setChecked(true);
//                break;
//            case 2:
//                tab_rb_3.setChecked(true);
//                break;
            case 3:
                tab_rb_4.setChecked(true);
                break;
            case 4:
                tab_rb_5.setChecked(true);
                break;
            default:
                break;
        }
    }

    private long secondTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (JCVideoPlayer.backPress()) {
                return false;
            }
            if ((System.currentTimeMillis() - secondTime) > 2000) {
                secondTime = System.currentTimeMillis();
                CustomToast.show("再按一次退出程序");
            } else {
                AppManager.getAppManager().exitApp();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    private PopupWindow popupWindo;

    public void showPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.pw_score, null);

        TextView tv_good_score = view.findViewById(R.id.tv_good_score);
        TextView tv_bad_score = view.findViewById(R.id.tv_bad_score);
        TextView tv_close_score = view.findViewById(R.id.tv_close_score);

        tv_good_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindo.dismiss();
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    CustomToast.show("您的手机没有安装Android应用市场");
                    e.printStackTrace();
                }
            }
        });

        tv_bad_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindo.dismiss();
                startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
            }
        });

        tv_close_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindo.dismiss();
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

    private void isNeedComment() {
        String url = BaseConstant.getApiPostUrl("account/isNeedComment");
        HttpParams param = new HttpParams();
        param.put("userId", LocalData.getInstance().getUserInfo().getUserid());
        OkGo.<DataResult<Boolean>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<Boolean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<Boolean>> response) {
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            if (response.body().getData() != null && response.body().getData()) {
                                showPopupWindow();
                            }
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<Boolean>> response) {
                        showVolleyError(null);
                    }
                });
    }

}

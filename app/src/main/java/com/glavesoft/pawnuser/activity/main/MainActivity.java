package com.glavesoft.pawnuser.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import com.glavesoft.util.CommonUtils;
import com.glavesoft.view.CustomToast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

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
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        if(getIntent().hasExtra("type")){
            String type=getIntent().getStringExtra("type");
            Intent intent = new Intent();
            if(type.equals("push")){
                intent.setClass(MainActivity.this, MessageActivity.class);
                startActivity(intent);
            }else if(type.equals("Crowding")){
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        }

        CommonUtils.getDeviceId(MainActivity.this);

        requestReadPermissions(new CheckPermListener() {
            @Override
            public void superREADPermission() {
                Intent intent=new Intent("Permission_Refresh");
                sendBroadcast(intent);
            }
        });

        if (BaseConstant.isLogin()) {
            isNeedComment();
        }
    }

    private void initView() {
        fragments.add(HomeFragment.newInstance(fragments.size()));//首页
//        fragments.add(CommunityFragment.newInstance(fragments.size()));//
//        fragments.add(AuctionFragment.newInstance(fragments.size()));//
        fragments.add(ShoppingCartFragment.newInstance(fragments.size()));//
        fragments.add(PersonalFragment.newInstance(fragments.size()));//我的

        main_rg_tab = (RadioGroup) findViewById(R.id.main_rg_tab);
        tab_rb_1 = (RadioButton) findViewById(R.id.main_rb_home);
//        tab_rb_2 = (RadioButton) findViewById(R.id.main_rb_sq);
//        tab_rb_3 = (RadioButton) findViewById(R.id.main_rb_pm);
        tab_rb_4 = (RadioButton) findViewById(R.id.main_rb_gwc);
        tab_rb_5 = (RadioButton) findViewById(R.id.main_rb_personal);

        tabAdapter = new FragmentTabAdapter(this, fragments, R.id.main_fl, main_rg_tab, BaseConstant.index);

        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener()
        {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index)
            {

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
        int id =  BaseConstant.index;
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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (JCVideoPlayer.backPress()) {
                return false;
            }
            if ((System.currentTimeMillis() - secondTime) > 2000)
            {
                secondTime = System.currentTimeMillis();
                CustomToast.show("再按一次退出程序");
            } else
            {
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
    public void showPopupWindow()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.pw_score, null);

        TextView tv_good_score = view.findViewById(R.id.tv_good_score);
        TextView tv_bad_score = view.findViewById(R.id.tv_bad_score);
        TextView tv_close_score = view.findViewById(R.id.tv_close_score);

        tv_good_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindo.dismiss();
                try{
                    Uri uri = Uri.parse("market://details?id="+getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch(Exception e){
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
        String url=BaseConstant.getApiPostUrl("account/isNeedComment");
        HttpParams param=new HttpParams();
        param.put("userId",  LocalData.getInstance().getUserInfo().getUserid());
        OkGo.<DataResult<Boolean>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<Boolean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<Boolean>> response) {
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData()!=null&&response.body().getData()){
                                showPopupWindow();
                            }
                        }else {
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

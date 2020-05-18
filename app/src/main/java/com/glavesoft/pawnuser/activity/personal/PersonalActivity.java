package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.pawn.MonitorActivity;
import com.glavesoft.pawnuser.activity.pawn.MyPawnActivity;
import com.glavesoft.pawnuser.activity.pawn.PawnRecordActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.OrderActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ItemInfo;
import com.glavesoft.pawnuser.mod.LawInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.view.RoundImageView;
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
 * @date: 2017/10/19
 * @company:常州宝丰
 */
public class PersonalActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_myinfo;
    private ImageView iv_personal_back,iv_personal_set;
    private TextView tv_my_version;
    private TextView tv_wdyhq;
    private TextView tv_htjl;
    private RoundImageView my_photo;
    private TextView tv_my_name,tv_my_phone;

    private GridViewForNoScroll nsgv_home_jdxp;
    int[] img = new int[]{R.drawable.wdyw,R.drawable.scdd,R.drawable.wlxx,R.drawable.ddjl,R.drawable.ddjk,R.drawable.wdyhk,R.drawable.rlyz,R.drawable.wdkf,R.drawable.tgm};
    String[] title = new String[]{"典当业务","商城订单","物流信息","典当记录","典当监控","我的银行卡","人脸认证","客服","推广二维码"};
    ArrayList<ItemInfo> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initView();
        lawList();
    }

    private void initView() {
        ll_myinfo= (LinearLayout) findViewById(R.id.ll_myinfo);
//        iv_personal_back= (ImageView) findViewById(R.id.iv_personal_back);
//        iv_personal_set= (ImageView) findViewById(R.id.iv_personal_set);

        my_photo= (RoundImageView) findViewById(R.id.my_photo);
        tv_my_name= (TextView) findViewById(R.id.tv_my_name);
        tv_my_phone= (TextView) findViewById(R.id.tv_my_phone);

        tv_wdyhq= (TextView) findViewById(R.id.tv_wdyhq);

        nsgv_home_jdxp= (GridViewForNoScroll) findViewById(R.id.nsgv_home_jdxp);

        tv_htjl= (TextView) findViewById(R.id.tv_htjl);
        tv_my_version= (TextView) findViewById(R.id.tv_my_version);

        ll_myinfo.setOnClickListener(this);
        iv_personal_back.setOnClickListener(this);
        iv_personal_set.setOnClickListener(this);

        tv_wdyhq.setOnClickListener(this);
        tv_htjl.setOnClickListener(this);

        try
        {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            tv_my_version.setText("版本号 V" + pi.versionName);
        } catch (PackageManager.NameNotFoundException e)
        {
            //tv_my_version.setText("版本号 V1.0");
            e.printStackTrace();
        }
        for(int i=0;i<img.length;i++){
            ItemInfo info=new ItemInfo();
            info.setTitle(title[i]);
            info.setImg(img[i]);
            list.add(info);
        }
        showgridList(list);

        nsgv_home_jdxp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){//我的业务
                    startActivity(new Intent(PersonalActivity.this, MyPawnActivity.class));
                }else if(position==1){//商城订单
                    startActivity(new Intent(PersonalActivity.this, OrderActivity.class));
                }else if(position==2){//我的物流
                    startActivity(new Intent(PersonalActivity.this, LogisticsActivity.class));
                }else if(position==3){//典当记录
                    startActivity(new Intent(PersonalActivity.this, PawnRecordActivity.class));
                }else if(position==4){//典当监控
                    startActivity(new Intent(PersonalActivity.this, MonitorActivity.class));
                }else if(position==5){//我的银行卡
                    startActivity(new Intent(PersonalActivity.this, BankCardActivity.class));
                }else if(position==6){//人脸识别
                    if(LocalData.getInstance().getUserInfo().getIsBind().equals("1")){//已绑定
                        CustomToast.show("您已绑定");
                    }else{
                        startActivity(new Intent(PersonalActivity.this, BindIDcardActivity.class));
                    }
                }else if(position==7){//客服
                    gotokf(PersonalActivity.this);
                }else if(position==8){//推广码
                    startActivity(new Intent(PersonalActivity.this, EwmActivity.class));
                }
            }
        });
    }

    public void onResume(){
        super.onResume();
        if(!LocalData.getInstance().getUserInfo().getHeadImg().equals("")){
            String imageurl= BaseConstant.Image_URL + LocalData.getInstance().getUserInfo().getHeadImg();
            getImageLoader().displayImage(imageurl,my_photo,getImageLoaderHeadOptions());
        }

        tv_my_name.setText(LocalData.getInstance().getUserInfo().getNickName());
        tv_my_phone.setText(LocalData.getInstance().getUserInfo().getAccount());
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
//            case R.id.iv_personal_back:
//                finish();
//                break;
//            case R.id.iv_personal_set://设置
//                startActivity(new Intent(PersonalActivity.this, SettingActivity.class));
//                break;
            case R.id.ll_myinfo://个人资料
                startActivity(new Intent(PersonalActivity.this, MyinfoActivity.class));
                break;
            case R.id.tv_wdyhq://我的优惠券
                startActivity(new Intent(PersonalActivity.this, CouponActivity.class));
                break;
//            case R.id.tv_wdjdjp://我的绝当竞拍
//                startActivity(new Intent(PersonalActivity.this, MyAuctionActivity.class));
//                break;
            case R.id.tv_htjl://合同记录
                startActivity(new Intent(PersonalActivity.this, ContractActivity.class));
                break;
        }
    }
    private void showgridList(final ArrayList<ItemInfo> result) {
        CommonAdapter commAdapter = new CommonAdapter<ItemInfo>(PersonalActivity.this, result,
                R.layout.item_person) {
            @Override
            public void convert(final ViewHolder helper, final ItemInfo item) {
                helper.setText(R.id.tv_title_menu,item.getTitle());
                helper.getView(R.id.iv_pic_menu).setBackgroundResource(item.getImg());
            }
        };

        nsgv_home_jdxp.setAdapter(commAdapter);
    }

    private void lawList()
    {
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/lawList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        OkGo.<DataResult<ArrayList<LawInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<LawInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<LawInfo>>> response) {
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                if(response.body().getData()!=null&&response.body().getData().size()>0){
                                    for (int i=0;i<response.body().getData().size();i++){
                                        if(response.body().getData().get(i).getCode().equals("ddglbf@law")){
                                            PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, "ddglbf", response.body().getData().get(i).getValue());
                                        }else if(response.body().getData().get(i).getCode().equals("htf@law")){
                                            PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, "htf", response.body().getData().get(i).getValue());
                                        }else if(response.body().getData().get(i).getCode().equals("mfzz@law")){
                                            PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, "mfzz", response.body().getData().get(i).getValue());
                                        }
                                    }

                                }
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<LawInfo>>> response) {
                        showVolleyError(null);
                    }
                });
    }

}

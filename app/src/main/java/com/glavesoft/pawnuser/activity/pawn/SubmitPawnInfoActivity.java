package com.glavesoft.pawnuser.activity.pawn;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.ImagePageActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.MyAppraisalInfo;
import com.glavesoft.pawnuser.mod.NoPawnInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class SubmitPawnInfoActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_name_pawninfo,tv_jdprice_pawninfo;
    private TextView tv_submit_pawninfo;
    private GridViewForNoScroll gv_pics_pawninfo;
    private TextView tv_money_pawninfo,tv_ll_pawninfo,tv_timelong_pawninfo;
    private CheckBox cb_pawn_agree;
    private TextView tv_pawn_agreement;

    private ArrayList<String> list=new ArrayList<>();
    private MyAppraisalInfo myAppraisalInfo;
    private NoPawnInfo noPawnInfo;
    private String type;
    private String loansPrice;
    private String loansRate;
    private int pawnTime;
    private int index;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitpawninfo);
        initData();
        initView();
    }

    private void initData(){
        type=getIntent().getStringExtra("type");
        if(type.equals("appraisal")){
            myAppraisalInfo=(MyAppraisalInfo)getIntent().getSerializableExtra("MyAppraisalInfo");
        }else{
            noPawnInfo=(NoPawnInfo)getIntent().getSerializableExtra("NoPawnInfo");
            index=getIntent().getIntExtra("index",0);
        }
        loansPrice=getIntent().getStringExtra("loansPrice");
        loansRate=getIntent().getStringExtra("loansRate");
        pawnTime=getIntent().getIntExtra("pawnTime",0);

    }

    private void initView() {
        setTitleBack();
        setTitleName("确认典当需求");
        titlebar_kf = (ImageView) findViewById(R.id.titlebar_kf);
        titlebar_kf.setVisibility(View.VISIBLE);
        titlebar_kf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotokf_Z(SubmitPawnInfoActivity.this);
            }
        });
        tv_name_pawninfo=getViewById(R.id.tv_name_pawninfo);
        tv_jdprice_pawninfo=getViewById(R.id.tv_jdprice_pawninfo);
        tv_submit_pawninfo=getViewById(R.id.tv_submit_pawninfo);
        gv_pics_pawninfo=getViewById(R.id.gv_pics_pawninfo);
        tv_money_pawninfo=getViewById(R.id.tv_money_pawninfo);
        tv_ll_pawninfo=getViewById(R.id.tv_ll_pawninfo);
        tv_timelong_pawninfo=getViewById(R.id.tv_timelong_pawninfo);
        cb_pawn_agree=getViewById(R.id.cb_pawn_agree);
        tv_pawn_agreement=getViewById(R.id.tv_pawn_agreement);

        tv_submit_pawninfo.setOnClickListener(this);
        tv_pawn_agreement.setOnClickListener(this);

        tv_money_pawninfo.setText("￥"+loansPrice);
        tv_ll_pawninfo.setText(loansRate+"%/月");
        tv_timelong_pawninfo.setText(pawnTime*15+"天");

        if(type.equals("appraisal")){
            tv_name_pawninfo.setText(myAppraisalInfo.getTitle());
            tv_jdprice_pawninfo.setText("￥"+myAppraisalInfo.getPrice());
            if(!myAppraisalInfo.getImage().equals("")){
                List<String> list1= Arrays.asList(myAppraisalInfo.getImage().split(","));
                for (int i=0;i<list1.size();i++){
                    list.add(BaseConstant.Image_URL+list1.get(i));
                }
            }
        }else{
            tv_name_pawninfo.setText(noPawnInfo.getTitle());
            tv_jdprice_pawninfo.setText("￥"+noPawnInfo.getAuthPrice());
            if(!noPawnInfo.getImage().equals("")){
                List<String> list1= Arrays.asList(noPawnInfo.getImage().split(","));
                for (int i=0;i<list1.size();i++){
                    list.add(BaseConstant.Image_URL+list1.get(i));
                }
            }
        }

        showList(list);

        gv_pics_pawninfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(SubmitPawnInfoActivity.this,ImagePageActivity.class);
                intent.putExtra("picurlList",list);
                intent.putExtra("selectPos",position);
                startActivity(intent);
            }
        });

    }

    private void showList(ArrayList<String> result) {
        CommonAdapter commAdapter = new CommonAdapter<String>(SubmitPawnInfoActivity.this, result,
                R.layout.item_pic_pawn) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

                ImageView iv_pic_pawn=(ImageView) helper.getView(R.id.iv_pic_pawn);
                getImageLoader().displayImage(item,iv_pic_pawn,getImageLoaderOptions());
            }
        };

        gv_pics_pawninfo.setAdapter(commAdapter);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.tv_submit_pawninfo:
//                if (!cb_pawn_agree.isChecked()) {
//                    CustomToast.show("请先阅读并同意典当条款");
//                    return;
//                }
                gotoPawn();
                break;
            case R.id.tv_pawn_agreement:

                break;
        }
    }

    //去典当
    private void gotoPawn()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url="";
        if(type.equals("appraisal")){
            url="userPawn/gotoPawn";
        }else{
            if(index==3){
                url="userPawn/gotoPawn2";
            }else{
                url="userPawn/gotoPawn";
            }
        }
        HttpParams param=new HttpParams();
        param.put("token",token);
        if(type.equals("appraisal")){
            param.put("id",myAppraisalInfo.getId());
        }else{
            param.put("id",noPawnInfo.getId());
        }
        param.put("loansPrice",loansPrice);
        param.put("loansRate",loansRate);
        param.put("pawnTime",pawnTime+"");
        OkGo.<DataResult<DataInfo>>post(BaseConstant.getApiPostUrl(url))
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
                            Intent intent=new Intent("AppraisalRefresh");
                            sendBroadcast(intent);
                            Intent intent1=new Intent("submitpawn");
                            sendBroadcast(intent1);
                            CustomToast.show("发布成功,您可以在个人中心的典当业务中查看！");
                            PawnActivity.pawnActivity.finish();
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

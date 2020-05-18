package com.glavesoft.pawnuser.activity.pawn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.ImagePageActivity;
import com.glavesoft.pawnuser.activity.personal.AutioTakePhotoActivity;
import com.glavesoft.pawnuser.activity.personal.BindIDcardActivity;
import com.glavesoft.pawnuser.activity.personal.LoadingActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.MyAppraisalInfo;
import com.glavesoft.pawnuser.mod.NoPawnInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.FileUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/10/26
 * @company:常州宝丰
 */
public class PawnActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_name_pawn,tv_jdprice_pawn;
    private LinearLayout ll_submit_pawn;
    private GridViewForNoScroll gv_pics_pawn,gv_timelong_pawn;
    private ArrayList<String> list=new ArrayList<>();
    private ArrayList<String> daylist=new ArrayList<>();
    private EditText et_money_pawn,et_ll_pawn;
    private int index=-1;
    private MyAppraisalInfo myAppraisalInfo;
    private NoPawnInfo noPawnInfo;
    private String type;
    public static PawnActivity pawnActivity;
    private String facepath="";
    private String image="";
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pawn);
        pawnActivity=this;
        type=getIntent().getStringExtra("type");
        if(type.equals("appraisal")){
            myAppraisalInfo=(MyAppraisalInfo)getIntent().getSerializableExtra("MyAppraisalInfo");
        }else{
            noPawnInfo=(NoPawnInfo)getIntent().getSerializableExtra("NoPawnInfo");
        }
        setBoardCast();
        initView();
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("pawninfo");
        registerReceiver(mListenerID, f);
    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            File file = new File(FileUtils.FACE_PATH);
            if (file.exists())
            {
                facepath=file.getAbsolutePath();
                UploadFace(file);
            }

        }
    };

    public  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mListenerID);
        try {
            File file = new File(FileUtils.FACE_PATH);
            if (file.exists())
            {
                file.delete();
            }
        }catch (Exception e){

        }

    }

    private void initView() {
        setTitleBack();
        setTitleName("我要典当");
        setTitleNameEn(R.mipmap.i_want_pawn);

        titlebar_kf = (ImageView) findViewById(R.id.titlebar_kf);
        titlebar_kf.setVisibility(View.VISIBLE);
        titlebar_kf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotokf_Z(PawnActivity.this);
            }
        });

        tv_name_pawn=(TextView) findViewById(R.id.tv_name_pawn);
        tv_jdprice_pawn=(TextView) findViewById(R.id.tv_jdprice_pawn);

        gv_pics_pawn=(GridViewForNoScroll)findViewById(R.id.gv_pics_pawn);
        gv_timelong_pawn=(GridViewForNoScroll)findViewById(R.id.gv_timelong_pawn);

        et_money_pawn=(EditText) findViewById(R.id.et_money_pawn);
        et_ll_pawn=(EditText)findViewById(R.id.et_ll_pawn);
        et_money_pawn.setFilters(new InputFilter[] { new BaseConstant.EditInputFilter(et_money_pawn) });
        et_ll_pawn.setFilters(new InputFilter[] { new BaseConstant.EditInputFilter(et_ll_pawn) });

        ll_submit_pawn=(LinearLayout) findViewById(R.id.ll_submit_pawn);
        ll_submit_pawn.setOnClickListener(this);

        tv_jdprice_pawn=(TextView) findViewById(R.id.tv_jdprice_pawn);

        daylist.add("15");daylist.add("30");daylist.add("45");daylist.add("60");
        daylist.add("75");daylist.add("90");daylist.add("105");daylist.add("120");
        daylist.add("135");daylist.add("150");daylist.add("165");daylist.add("180");
        showdayList(daylist);
        if(type.equals("appraisal")){
            tv_name_pawn.setText(myAppraisalInfo.getTitle());
            tv_jdprice_pawn.setText("￥"+myAppraisalInfo.getPrice());
            if(!myAppraisalInfo.getImage().equals("")){
                List<String> list1= Arrays.asList(myAppraisalInfo.getImage().split(","));
                for (int i=0;i<list1.size();i++){
                    list.add(BaseConstant.Image_URL+list1.get(i));
                }
            }

        }else{
            tv_name_pawn.setText(noPawnInfo.getTitle());
            tv_jdprice_pawn.setText("￥"+noPawnInfo.getAuthPrice());
            if(!noPawnInfo.getImage().equals("")){
                List<String> list1= Arrays.asList(noPawnInfo.getImage().split(","));
                for (int i=0;i<list1.size();i++){
                    list.add(BaseConstant.Image_URL+list1.get(i));
                }
            }
        }

        showList(list);

        gv_pics_pawn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(PawnActivity.this,ImagePageActivity.class);
                intent.putExtra("picurlList",list);
                intent.putExtra("selectPos",position);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.ll_submit_pawn:
                gotoSend();
                break;
        }
    }

    private void gotoSend() {

        if (et_money_pawn.getText().toString().trim().length() == 0) {
            CustomToast.show("请输入当款金额");
            return;
        }

        if (et_ll_pawn.getText().toString().trim().length() == 0) {
            CustomToast.show("请输入期望费率");
            return;
        }

        if (index == -1) {
            CustomToast.show("请选择典当时长");
            return;
        }

        if(LocalData.getInstance().getUserInfo().getIsBind().equals("1")){//已绑定
            if(BaseConstant.ISFACE) {//已验证
                gotoSubmit();
            }else{
                showPopupWindow(PawnActivity.this, "", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestReadCameraPermissions(new CheckPermListener() {
                            @Override
                            public void superREADPermission() {
                                getPopupWindow().dismiss();
                                Intent intent=new Intent();
                                intent.setClass(PawnActivity.this, AutioTakePhotoActivity.class);
                                intent.putExtra("type","pawninfo");
                                startActivity(intent);
                            }
                        });

                    }
                });
            }
        }else{
            showPopupWindow(PawnActivity.this, "您还没有绑定身份，去绑定！", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PawnActivity.this, BindIDcardActivity.class));
                }
            });
        }
    }

    private void gotoSubmit() {
        Intent intent=new Intent();
        intent.setClass(PawnActivity.this, SubmitPawnInfoActivity.class);
        intent.putExtra("type",type);
        if(type.equals("appraisal")){
            intent.putExtra("MyAppraisalInfo",myAppraisalInfo);
        }else{
            intent.putExtra("NoPawnInfo",noPawnInfo);
            intent.putExtra("index",getIntent().getIntExtra("index",0));
        }
        intent.putExtra("loansPrice",et_money_pawn.getText().toString().trim());
        intent.putExtra("loansRate",et_ll_pawn.getText().toString().trim());
        intent.putExtra("pawnTime",index+1);
        startActivity(intent);
    }

    private void showList(ArrayList<String> result) {
        CommonAdapter commAdapter = new CommonAdapter<String>(PawnActivity.this, result,
                R.layout.item_pic_pawn) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

                ImageView iv_pic_pawn=(ImageView) helper.getView(R.id.iv_pic_pawn);
                getImageLoader().displayImage(item,iv_pic_pawn,getImageLoaderOptions());
            }
        };

        gv_pics_pawn.setAdapter(commAdapter);
    }

    private void showdayList(ArrayList<String> result) {
        CommonAdapter commAdapter = new CommonAdapter<String>(PawnActivity.this, result,
                R.layout.item_timelong_pawn) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

                helper.setText(R.id.tv_day_pawn,item+"天");

                if(helper.getPosition()==index){
                    helper.getView(R.id.tv_day_pawn).setBackgroundResource(R.drawable.shape_yellow);
                    helper.setTextcolor(R.id.tv_day_pawn,getResources().getColor(R.color.white));
                }else{
                    helper.getView(R.id.tv_day_pawn).setBackgroundResource(R.drawable.shape_kuang);
                    helper.setTextcolor(R.id.tv_day_pawn,getResources().getColor(R.color.black));
                }

                helper.getView(R.id.tv_day_pawn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        index=helper.getPosition();
                        notifyDataSetChanged();
                    }
                });

            }
        };

        gv_timelong_pawn.setAdapter(commAdapter);
    }

    //上传人脸照
    public void UploadFace( File file){
        getlDialog().show();
        String url=BaseConstant.UploadAvatar_URL;
        OkGo.<DataResult<ImageInfo>>post(url)
                .params("file", file)
                .execute(new JsonCallback<DataResult<ImageInfo>>() {
                    @Override
                    public void onStart(Request<DataResult<ImageInfo>, ? extends Request> request) {

                    }
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<ImageInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.msg_error));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK){

                            if(response.body().getData()!=null) {
                                image=response.body().getData().getId();
                                bindUserMsg();
                            }

                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<ImageInfo>> response) {
                        getlDialog().dismiss();
                        CustomToast.show("上传失败,请重新上传");
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }
                });
    }

    //绑定用户信息
    private void bindUserMsg()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userFace/userFace");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("image",image);
        OkGo.<DataResult<DataInfo>>post(url)
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
                            CustomToast.show("验证成功");
                            BaseConstant.ISFACE=true;
                            gotoSubmit();
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

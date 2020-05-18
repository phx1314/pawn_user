package com.glavesoft.pawnuser.activity.pawn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.personal.AddBankCardActivity;
import com.glavesoft.pawnuser.activity.personal.AutioTakePhotoActivity;
import com.glavesoft.pawnuser.activity.personal.BindIDcardActivity;
import com.glavesoft.pawnuser.activity.personal.LoadingActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.BankCardInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.OnlineIdentificationInfo;
import com.glavesoft.pawnuser.mod.OrgPawnDetailInfo;
import com.glavesoft.pawnuser.mod.PawnAuctionListInfo;
import com.glavesoft.pawnuser.mod.PawnDetailInfo;
import com.glavesoft.pawnuser.mod.PlatGetDetailInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.FileUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.MyListView;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/11/20
 * @company:常州宝丰
 */
public class PlatformActivity extends BaseActivity implements View.OnClickListener{

    private MyListView mlv_bankcard;
    private TextView tv_submit_selectpawn,tv_add_bankcard;
    private String id="";
    //private PawnDetailInfo pawnListInfo;
    private TextView tv_name_selectpawnshop,tv_jdprice_selectpawnshop,tv_qwdj_selectpawnshop,tv_time_selectpawnshop;

    private LinearLayout ll_select_pawnshop,ll_pt_pawnshop;
    private TextView tv_ptdj_selectpawnshop,tv_zhll_selectpawnshop,tv_lxlv_selectpawnshop,tv_zs_selectpawnshop;
    private String image="";
    private PlatGetDetailInfo platGetDetailInfo;
    private BankCardInfo bankCardInfo;
    public static PlatformActivity platformActivity;

    private TextView tv_companyname_selectpawnshop,tv_address_selectpawnshop,tv_zczj_selectpawnshop,
            tv_frdb_selectpawnshop,tv_phone_selectpawnshop;

    private String pawnid="";
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpawnshop);
        setBoardCast();
        platformActivity=this;
        pawnid=getIntent().getStringExtra("pawnid");
        initView();
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("Platform");
        registerReceiver(mListenerID, f);

        IntentFilter f1 = new IntentFilter();
        f1.addAction("AddBankcardRefresh");
        registerReceiver(mListenerID1, f1);
    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            File file = new File(FileUtils.FACE_PATH);
            if (file.exists())
            {
                UploadFace(file);
            }

        }
    };

    BroadcastReceiver mListenerID1 = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            myBankCardList();
        }
    };

    public  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mListenerID);
        unregisterReceiver(mListenerID1);
        File file = new File(FileUtils.FACE_PATH);
        if (file.exists())
        {
            file.delete();
        }
    }

    private void initView() {
        setTitleBack();
        setTitleName("选择宝祥典当行");
        tv_name_selectpawnshop=(TextView) findViewById(R.id.tv_name_selectpawnshop);
        tv_jdprice_selectpawnshop=(TextView)findViewById(R.id.tv_jdprice_selectpawnshop);
        tv_qwdj_selectpawnshop=(TextView)findViewById(R.id.tv_qwdj_selectpawnshop);
        tv_time_selectpawnshop=(TextView)findViewById(R.id.tv_time_selectpawnshop);

        mlv_bankcard=(MyListView)findViewById(R.id.mlv_bankcard);

        ll_select_pawnshop=(LinearLayout) findViewById(R.id.ll_select_pawnshop);
        ll_pt_pawnshop=(LinearLayout)findViewById(R.id.ll_pt_pawnshop);
        tv_ptdj_selectpawnshop=(TextView)findViewById(R.id.tv_ptdj_selectpawnshop);
        tv_zhll_selectpawnshop=(TextView)findViewById(R.id.tv_zhll_selectpawnshop);
        tv_lxlv_selectpawnshop=(TextView)findViewById(R.id.tv_lxlv_selectpawnshop);
        tv_zs_selectpawnshop=(TextView)findViewById(R.id.tv_zs_selectpawnshop);

        tv_companyname_selectpawnshop=(TextView)findViewById(R.id.tv_companyname_selectpawnshop);
        tv_address_selectpawnshop=(TextView)findViewById(R.id.tv_address_selectpawnshop);
        tv_zczj_selectpawnshop=(TextView)findViewById(R.id.tv_zczj_selectpawnshop);
        tv_frdb_selectpawnshop=(TextView)findViewById(R.id.tv_frdb_selectpawnshop);
        tv_phone_selectpawnshop=(TextView)findViewById(R.id.tv_phone_selectpawnshop);

        ll_select_pawnshop.setVisibility(View.GONE);
        ll_pt_pawnshop.setVisibility(View.VISIBLE);
        tv_zs_selectpawnshop.setVisibility(View.VISIBLE);

        tv_add_bankcard=(TextView)findViewById(R.id.tv_add_bankcard);
        tv_add_bankcard.setOnClickListener(this);

        tv_submit_selectpawn=(TextView)findViewById(R.id.tv_submit_selectpawn);
        tv_submit_selectpawn.setOnClickListener(this);
        platGetDetail();
        myBankCardList();
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.tv_submit_selectpawn:

                if(id.equals("")){
                    CustomToast.show("请选择入账银行卡");
                    return;
                }

                if(LocalData.getInstance().getUserInfo().getIsBind().equals("1")){//已绑定
                    if(BaseConstant.ISFACE) {//已验证
                        gotoSubmit();
                    }else{
                        showPopupWindow(PlatformActivity.this, "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestReadCameraPermissions(new CheckPermListener() {
                                    @Override
                                    public void superREADPermission() {
                                        getPopupWindow().dismiss();
                                        Intent intent=new Intent();
                                        intent.setClass(PlatformActivity.this, AutioTakePhotoActivity.class);
                                        intent.putExtra("type","Platform");
                                        startActivity(intent);
                                    }
                                });

                            }
                        });
                    }
                }else{
                    showPopupWindow(PlatformActivity.this, "您还没有绑定身份，去绑定！", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(PlatformActivity.this, BindIDcardActivity.class));
                        }
                    });
                }

                break;
            case R.id.tv_add_bankcard://添加银行卡
                if(LocalData.getInstance().getUserInfo().getIsBind().equals("1")){//已绑定
                    startActivity(new Intent(PlatformActivity.this, AddBankCardActivity.class));
                }else{
                    showPopupWindow(PlatformActivity.this, "您还没有绑定身份，去绑定！", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(PlatformActivity.this, BindIDcardActivity.class));
                        }
                    });
                }
                break;
        }
    }

    private void gotoSubmit() {
        Intent intent=new Intent();
        intent.setClass(PlatformActivity.this, SubmitPlatActivity.class);
        intent.putExtra("pawnid",pawnid);
        intent.putExtra("platGetDetailInfo",platGetDetailInfo);
        intent.putExtra("bankCardInfo",bankCardInfo);
        startActivity(intent);
    }


    private void showList(ArrayList<BankCardInfo> result) {
        CommonAdapter commAdapter = new CommonAdapter<BankCardInfo>(PlatformActivity.this, result,
                R.layout.item_bankcard_sellout) {
            @Override
            public void convert(final ViewHolder helper, final BankCardInfo item) {
                helper.setText(R.id.tv_bankname_sellout,"("+item.getBankCardName()+")");
                helper.setText(R.id.tv_banknum_sellout,item.getBankCardNo());

                ImageView mIvBank=helper.getView(R.id.iv_bank_select);
                if(item.getId().equals(id)){
                    mIvBank.setImageResource(R.drawable.goux);
                }else{
                    mIvBank.setImageResource(R.drawable.yuan);
                }

                helper.getView(R.id.iv_bank_select).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        id=item.getId();
                        bankCardInfo=item;
                        notifyDataSetChanged();
                    }
                });

            }
        };
        mlv_bankcard.setAdapter(commAdapter);
    }

    private void myBankCardList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/myBankCardList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page","1");
        param.put("limit","1000");
        OkGo.<DataResult<ArrayList<BankCardInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<BankCardInfo>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<ArrayList<BankCardInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                showList(response.body().getData());
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<ArrayList<BankCardInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void platGetDetail()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/platGetDetail");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",pawnid);
        OkGo.<DataResult<PlatGetDetailInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<PlatGetDetailInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<PlatGetDetailInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){
                                platGetDetailInfo=response.body().getData();
                                tv_name_selectpawnshop.setText(platGetDetailInfo.getTitle());
                                tv_jdprice_selectpawnshop.setText("￥"+platGetDetailInfo.getAuthPrice());
                                tv_qwdj_selectpawnshop.setText("￥"+platGetDetailInfo.getLoansPrice());

                                if(!platGetDetailInfo.getPawnTime().equals("")){
                                    int days=Integer.valueOf(platGetDetailInfo.getPawnTime())*15;
                                    tv_time_selectpawnshop.setText(days+"天");
                                }

                                tv_ptdj_selectpawnshop.setText("￥"+response.body().getData().getBxMoney());
                                tv_zhll_selectpawnshop.setText(response.body().getData().getBxRate()+"%/月");
                                tv_lxlv_selectpawnshop.setText(response.body().getData().getBxMoneyRate()+"%/月");
                                tv_companyname_selectpawnshop.setText(response.body().getData().getComName());
                                tv_address_selectpawnshop.setText(response.body().getData().getComaddress());
                                tv_zczj_selectpawnshop.setText(response.body().getData().getRegMoney());
                                tv_frdb_selectpawnshop.setText(response.body().getData().getManager());
                                tv_phone_selectpawnshop.setText(response.body().getData().getComPhone());

                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<PlatGetDetailInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

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

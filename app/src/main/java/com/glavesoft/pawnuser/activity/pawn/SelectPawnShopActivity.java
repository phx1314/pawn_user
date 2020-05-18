package com.glavesoft.pawnuser.activity.pawn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.glavesoft.pawnuser.mod.PawnAuctionListInfo;
import com.glavesoft.pawnuser.mod.PawnDetailInfo;
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
 * @date: 2017/11/8
 * @company:常州宝丰
 */
public class SelectPawnShopActivity extends BaseActivity implements View.OnClickListener{

    private MyListView mlv_pawnshop,mlv_bankcard;
    private TextView tv_submit_selectpawn,tv_select_pawnshop,tv_add_bankcard;
    private ArrayList<String> list=new ArrayList<>();
    private String id="";
    private String pawnid="";
    private String type="";
    private PawnDetailInfo pawnListInfo;
    private TextView tv_name_selectpawnshop,tv_jdprice_selectpawnshop,tv_qwdj_selectpawnshop,tv_time_selectpawnshop;
    private String facepath="";
    private String image="";
    private BankCardInfo bankCardInfo;
    private PawnAuctionListInfo pawnAuctionListInfo;
    public static SelectPawnShopActivity selectPawnShopActivity;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpawnshop);
        selectPawnShopActivity=this;
        type=getIntent().getStringExtra("type");
        pawnListInfo=(PawnDetailInfo)getIntent().getSerializableExtra("pawnListInfo");
        if(type.equals("xt")){
            pawnAuctionListInfo =(PawnAuctionListInfo)getIntent().getSerializableExtra("pawnAuctionListInfo");
        }
        setBoardCast();
        initView();
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("SelectPawnShop");
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
                facepath=file.getAbsolutePath();
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

        tv_name_selectpawnshop=(TextView) findViewById(R.id.tv_name_selectpawnshop);
        tv_jdprice_selectpawnshop=(TextView)findViewById(R.id.tv_jdprice_selectpawnshop);
        tv_qwdj_selectpawnshop=(TextView)findViewById(R.id.tv_qwdj_selectpawnshop);
        tv_time_selectpawnshop=(TextView)findViewById(R.id.tv_time_selectpawnshop);
        tv_add_bankcard=(TextView)findViewById(R.id.tv_add_bankcard);
        tv_add_bankcard.setOnClickListener(this);

        tv_select_pawnshop=(TextView)findViewById(R.id.tv_select_pawnshop);

        mlv_bankcard=(MyListView)findViewById(R.id.mlv_bankcard);
        mlv_pawnshop=(MyListView)findViewById(R.id.mlv_pawnshop);

        tv_name_selectpawnshop.setText(pawnListInfo.getTitle());
        tv_jdprice_selectpawnshop.setText("￥"+pawnListInfo.getAuthPrice());
        tv_qwdj_selectpawnshop.setText("￥"+pawnListInfo.getLoansPrice());

        if(!pawnListInfo.getPawnTime().equals("")){
            int days=Integer.valueOf(pawnListInfo.getPawnTime())*15;
            tv_time_selectpawnshop.setText(days+"天");
        }

        if(type.equals("zdy")){
            setTitleName("自定义选择典当行");
            mlv_pawnshop.setVisibility(View.VISIBLE);
            if(pawnListInfo.getPawnAuctionList().size()>0){
                showPawnShopList(pawnListInfo.getPawnAuctionList());
            }
        }else{
            mlv_pawnshop.setVisibility(View.GONE);
            setTitleName("选择中拍典当行");
            tv_select_pawnshop.setText("系统择优选择"+pawnAuctionListInfo.getAuctionOrgname()+",月费率"+pawnAuctionListInfo.getRate()+
                    "%/月,月利率"+pawnAuctionListInfo.getMoneyRate()+"%/月,提供当金￥"+pawnAuctionListInfo.getMoney());
        }

        tv_submit_selectpawn=(TextView)findViewById(R.id.tv_submit_selectpawn);
        tv_submit_selectpawn.setOnClickListener(this);

        myBankCardList();
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.tv_submit_selectpawn:
                if(type.equals("zdy")){
                    if(pawnid.equals("")){
                        CustomToast.show("请选择典当行");
                        return;
                    }
                }

                if(id.equals("")){
                    CustomToast.show("请选择入账银行卡");
                    return;
                }

                if(LocalData.getInstance().getUserInfo().getIsBind().equals("1")){//已绑定
                    if(BaseConstant.ISFACE) {//已验证
                        gotoSubmit();
                    }else{
                        showPopupWindow(SelectPawnShopActivity.this, "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestReadCameraPermissions(new CheckPermListener() {
                                    @Override
                                    public void superREADPermission() {
                                        getPopupWindow().dismiss();
                                        Intent intent=new Intent();
                                        intent.setClass(SelectPawnShopActivity.this, AutioTakePhotoActivity.class);
                                        intent.putExtra("type","SelectPawnShop");
                                        startActivity(intent);
                                    }
                                });

                            }
                        });
                    }
                }else{
                    showPopupWindow(SelectPawnShopActivity.this, "您还没有绑定身份，去绑定！", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SelectPawnShopActivity.this, BindIDcardActivity.class));
                        }
                    });
                }
                break;
            case R.id.tv_add_bankcard://添加银行卡
                if(LocalData.getInstance().getUserInfo().getIsBind().equals("1")){//已绑定
                    startActivity(new Intent(SelectPawnShopActivity.this, AddBankCardActivity.class));
                }else{
                    showPopupWindow(SelectPawnShopActivity.this, "您还没有绑定身份，去绑定！", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SelectPawnShopActivity.this, BindIDcardActivity.class));
                        }
                    });
                }
                break;
        }
    }

    private void gotoSubmit() {
        Intent intent=new Intent();
        intent.setClass(SelectPawnShopActivity.this, SubmitPawnActivity.class);
        intent.putExtra("pawnListInfo",pawnListInfo);
        intent.putExtra("pawnAuctionListInfo",pawnAuctionListInfo);
        intent.putExtra("bankCardInfo",bankCardInfo);
        startActivity(intent);
    }

    private void showPawnShopList(ArrayList<PawnAuctionListInfo> result) {
        CommonAdapter commAdapter = new CommonAdapter<PawnAuctionListInfo>(SelectPawnShopActivity.this, result,
                R.layout.item_pawnshop) {
            @Override
            public void convert(final ViewHolder helper, final PawnAuctionListInfo item) {

                helper.setText(R.id.tv_name_pawnshop,item.getAuctionOrgname());
                helper.setText(R.id.tv_price_pawnshop,"￥"+item.getMoney());
                helper.setText(R.id.tv_ll_pawnshop,"月利率"+item.getMoneyRate()+"%/月");
                helper.setText(R.id.tv_zhll_pawnshop,"月费率"+item.getRate()+"%/月");

                if(item.getId().equals(pawnid)){
                    helper.getView(R.id.iv_pawnshop_select).setBackgroundResource(R.drawable.goux);
                }else{
                    helper.getView(R.id.iv_pawnshop_select).setBackgroundResource(R.drawable.yuan);
                }

                helper.getView(R.id.iv_pawnshop_select).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pawnid=item.getId();
                        pawnAuctionListInfo=item;
                        notifyDataSetChanged();
                        tv_select_pawnshop.setText("您已选择"+item.getAuctionOrgname());
                    }
                });

            }
        };
        mlv_pawnshop.setAdapter(commAdapter);
    }

    private void showList(ArrayList<BankCardInfo> result) {
        CommonAdapter commAdapter = new CommonAdapter<BankCardInfo>(SelectPawnShopActivity.this, result,
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

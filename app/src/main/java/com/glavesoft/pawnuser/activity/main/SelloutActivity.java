package com.glavesoft.pawnuser.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.pawn.SelectPawnShopActivity;
import com.glavesoft.pawnuser.activity.personal.AddBankCardActivity;
import com.glavesoft.pawnuser.activity.personal.BindIDcardActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.BankCardInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.MyAppraisalInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.MyListView;
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
 * @date: 2017/10/26
 * @company:常州宝丰
 */
public class SelloutActivity extends BaseActivity implements View.OnClickListener{
    private MyListView lv_bankcard_sellout;
    private TextView tv_submit_sellout;
    private ArrayList<String> list=new ArrayList<>();
    private int index=-1;
    private MyAppraisalInfo myAppraisalInfo;

    private ImageView iv_pic_sellout;
    private TextView tv_name_sellout,tv_jdprice_sellout,tv_sgprice_sellout,tv_add_bankcard;
    private String bankCardId="";
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellout);
        setBoardCast();
        myAppraisalInfo=(MyAppraisalInfo)getIntent().getSerializableExtra("MyAppraisalInfo");
        initView();
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("AddBankcardRefresh");
        registerReceiver(mListenerID, f);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            myBankCardList();
        }
    };

    public  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mListenerID);
    }

    private void initView() {
        setTitleBack();
        setTitleName("卖出藏品");
        titlebar_kf = (ImageView) findViewById(R.id.titlebar_kf);
        titlebar_kf.setVisibility(View.VISIBLE);
        titlebar_kf.setOnClickListener(this);

        iv_pic_sellout=getViewById(R.id.iv_pic_sellout);
        tv_name_sellout=getViewById(R.id.tv_name_sellout);
        tv_jdprice_sellout=getViewById(R.id.tv_jdprice_sellout);
        tv_sgprice_sellout=getViewById(R.id.tv_sgprice_sellout);
        tv_add_bankcard=getViewById(R.id.tv_add_bankcard);
        tv_add_bankcard.setOnClickListener(this);

        getImageLoader().displayImage(BaseConstant.Image_URL + myAppraisalInfo.getImage(),iv_pic_sellout,getImageLoaderOptions());
        tv_name_sellout.setText(myAppraisalInfo.getTitle());
        tv_jdprice_sellout.setText("￥"+myAppraisalInfo.getPrice());
        tv_sgprice_sellout.setText("￥"+myAppraisalInfo.getPrice());

        lv_bankcard_sellout=(MyListView)findViewById(R.id.lv_bankcard_sellout);


        tv_submit_sellout=(TextView)findViewById(R.id.tv_submit_sellout);
        tv_submit_sellout.setOnClickListener(this);

        myBankCardList();
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.titlebar_kf:
                gotokf_Z(SelloutActivity.this);
                break;
            case R.id.tv_submit_sellout:
                if(bankCardId.equals("")){
                    CustomToast.show("请选择收款银行卡");
                    return;
                }
                goPawnContinue();
                break;
            case R.id.tv_add_bankcard:
                if(LocalData.getInstance().getUserInfo().getIsBind().equals("1")){//已绑定
                    startActivity(new Intent(SelloutActivity.this, AddBankCardActivity.class));
                }else{
                    showPopupWindow(SelloutActivity.this, "您还没有绑定身份，去绑定！", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SelloutActivity.this, BindIDcardActivity.class));
                        }
                    });
                }
                break;
        }
    }

    private void showList(ArrayList<BankCardInfo> result) {
        CommonAdapter commAdapter = new CommonAdapter<BankCardInfo>(SelloutActivity.this, result,
                R.layout.item_bankcard_sellout) {
            @Override
            public void convert(final ViewHolder helper, final BankCardInfo item) {
                helper.setText(R.id.tv_bankname_sellout,"("+item.getBankCardName()+")");
                helper.setText(R.id.tv_banknum_sellout,item.getBankCardNo());

                ImageView mIvBank=helper.getView(R.id.iv_bank_select);
                if(item.getId().equals(bankCardId)){
                    mIvBank.setImageResource(R.drawable.goux);
                }else{
                    mIvBank.setImageResource(R.drawable.yuan);
                }

                helper.getView(R.id.iv_bank_select).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bankCardId=item.getId();
                        notifyDataSetChanged();
                    }
                });

            }
        };
        lv_bankcard_sellout.setAdapter(commAdapter);
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
                    public void onSuccess(Response<DataResult<ArrayList<BankCardInfo>>> response) {
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
                    public void onError(Response<DataResult<ArrayList<BankCardInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void goPawnContinue()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/saleToPlatform");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("bankCardId",bankCardId);
        param.put("id",myAppraisalInfo.getId());
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
                            Intent intent=new Intent("AppraisalRefresh");
                            sendBroadcast(intent);
                            CustomToast.show("操作成功");
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

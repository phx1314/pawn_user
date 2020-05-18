package com.glavesoft.pawnuser.activity.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.pawn.BackGoodsActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.AddressInfo;
import com.glavesoft.pawnuser.mod.BankCardInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * @author 严光
 * @date: 2017/10/20
 * @company:常州宝丰
 */
public class BankCardActivity extends BaseActivity implements View.OnClickListener{
    private int page=1;
    private ListView lv_bankcard;
    private ArrayList<BankCardInfo> list=new ArrayList<>();
    private CommonAdapter commAdapter;

    private ImageView tx_image;
    private TextView tv_totleprice_bank,tv_detail,tv_add_bankcard;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcard);
        setBoardCast();
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
        setTitleName("我的银行卡");
        setTitleNameEn(R.mipmap.my_bank_card);

        tx_image=(ImageView) findViewById(R.id.tx_image);
        tv_totleprice_bank=(TextView)findViewById(R.id.tv_totleprice_bank);
        tv_detail=(TextView)findViewById(R.id.tv_detail);
        tv_add_bankcard=(TextView)findViewById(R.id.tv_add_bankcard);
        tv_detail.setOnClickListener(this);
        tv_add_bankcard.setOnClickListener(this);

        lv_bankcard=(ListView)findViewById(R.id.lv_bankcard);

        getImageLoader().displayImage(BaseConstant.Image_URL + LocalData.getInstance().getUserInfo().getHeadImg(), tx_image, getImageLoaderHeadOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {}
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                RandomTx();
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {}

            @Override
            public void onLoadingCancelled(String imageUri, View view) {}
        });

        getMyPayeeTotal();
        myBankCardList();
    }

    private void RandomTx(){
        Random rand = new Random();
        int randNum = rand.nextInt(4);
        if (randNum==0){
            tx_image.setImageResource(R.mipmap.mryi);
        }else  if (randNum==1){
            tx_image.setImageResource(R.mipmap.mrer);
        }else  if (randNum==2){
            tx_image.setImageResource(R.mipmap.mrsan);
        }else  if (randNum==3){
            tx_image.setImageResource(R.mipmap.mrsi);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_detail:
                startActivity(new Intent(BankCardActivity.this, IncomeActivity.class));
                break;
            case R.id.tv_add_bankcard:
                if(LocalData.getInstance().getUserInfo().getIsBind().equals("1")){//已绑定
                    startActivity(new Intent(BankCardActivity.this, AddBankCardActivity.class));
                }else{
                    showPopupWindow(BankCardActivity.this, "您还没有绑定身份，去绑定！", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(BankCardActivity.this, BindIDcardActivity.class));
                        }
                    });
                }
                break;
        }
    }

    private void showList(ArrayList<BankCardInfo> result) {
        CommonAdapter commAdapter = new CommonAdapter<BankCardInfo>(BankCardActivity.this, result,
                R.layout.item_bankcard) {
            @Override
            public void convert(final ViewHolder helper, final BankCardInfo item) {

                helper.setText(R.id.tv_bank_name,item.getBankCardName());
                helper.setText(R.id.tv_bank_num,item.getBankCardNo());

                TextView tv_bank_del=helper.getView(R.id.tv_bank_del);
                tv_bank_del.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                tv_bank_del.getPaint().setAntiAlias(true);//抗锯齿
                tv_bank_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    updateMyBankCard(item.getId());
                    }
                });

                ImageView iv_mr_bank=helper.getView(R.id.iv_mr_bank);
                if(item.getIsDefault().equals("1")){
                    iv_mr_bank.setImageResource(R.drawable.gou);
                }else{
                    iv_mr_bank.setImageResource(R.drawable.gou_);
                }

                helper.getView(R.id.ll_mr_bank).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDefBankCard(item.getId());
                    }
                });
            }
        };

        lv_bankcard.setAdapter(commAdapter);

    }

    private void myBankCardList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/myBankCardList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",page+"");
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
                                list=response.body().getData();
                                showList(list);
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

    private void updateMyBankCard(String id)
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/updateMyBankCard");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
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
                            CustomToast.show("删除成功");
                            myBankCardList();
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

    private void getMyPayeeTotal()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/getMyPayeeTotal");
        HttpParams param=new HttpParams();
        param.put("token",token);
        OkGo.<DataResult<String>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<String>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<String>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            tv_totleprice_bank.setText("￥"+response.body().getData());
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<String>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    //设置默认卡号
    private void setDefBankCard(String id)
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/setDefBankCard");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
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
                            CustomToast.show("设置成功");
                            myBankCardList();
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

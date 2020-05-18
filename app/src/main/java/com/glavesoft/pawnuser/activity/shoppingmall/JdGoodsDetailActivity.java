package com.glavesoft.pawnuser.activity.shoppingmall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.appraisal.EvaluationActivity;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.activity.main.OfferActivity;
import com.glavesoft.pawnuser.activity.main.SubmitBuyActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.JdGoodsInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.TimeTools;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.MyListView;
import com.glavesoft.view.SlideShowView;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/12/4
 * @company:常州宝丰
 */
public class JdGoodsDetailActivity extends BaseActivity implements View.OnClickListener{

    private SlideShowView ssv_goodsdetail_pic;
    private LinearLayout ll_buy_goodsdetail;
    private MyListView mlv_ltjl,mlv_jpjl;
    private ArrayList<String> list=new ArrayList<>();

    private CountDownTimer mCountDownTimer;
    private TextView tv_countTime_goodsdetail1;

    private LinearLayout ll_goodsdetail,ll_goodsdetail1;

    private TextView tv_name_goodsdetail1,tv_qprice_goodsdetail1,tv_jdprice_goodsdetail1,tv_price_goodsdetail1;
    private TextView tv_scyg_goodsdetail,tv_content_goodsdetail;

    private LinearLayout ll_dsfrz_goodsdetail,ll_jpjl_goodsdetail;

    private WebView wv_goodsdetail;

    private JdGoodsInfo jdGoodsInfo;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetail);
        setBoardCast();
        initData();
        initView();
    }


    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("submitoffer");
        registerReceiver(mListenerID, f);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            storeJDGoodsDetail();
        }
    };

    public  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mListenerID);
    }

    private void initData() {
        id=getIntent().getStringExtra("id");
    }

    private void initView() {
        setTitleBack();
        setTitleName("物品详情");
        titlebar_kf = (ImageView) findViewById(R.id.titlebar_kf);
        titlebar_kf.setVisibility(View.VISIBLE);
        titlebar_kf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Information info = new Information();
                info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
                //用户资料
                if(BaseConstant.isLogin()){
                    info.setUid(LocalData.getInstance().getUserInfo().getId());
                    info.setUname(LocalData.getInstance().getUserInfo().getId());
                    info.setTel(LocalData.getInstance().getUserInfo().getAccount());
                    info.setFace(BaseConstant.Image_URL+LocalData.getInstance().getUserInfo().getHeadImg());
                }else{
                    info.setUid(CommonUtils.getDeviceId(JdGoodsDetailActivity.this));
                    info.setUname("游客");
                }
                //1仅机器人 2仅人工 3机器人优先 4人工优先
                info.setInitModeType(4);

                if(jdGoodsInfo!=null){
                    //咨询内容
                    ConsultingContent consultingContent = new ConsultingContent();
                    //咨询内容标题，必填
                    consultingContent.setSobotGoodsTitle(jdGoodsInfo.getGoodsName());
                    //咨询内容图片，选填 但必须是图片地址
                    if(!jdGoodsInfo.getImages().equals("")){
                        List<String> list= Arrays.asList(jdGoodsInfo.getImages().split(","));
                        consultingContent.setSobotGoodsImgUrl(BaseConstant.Image_URL+list.get(0));
                    }
                    //咨询来源页，必填
                    consultingContent.setSobotGoodsFromUrl("www.sobot.com");
                    //描述，选填
                    //consultingContent.setSobotGoodsDescribe("XXX超级电视 S5");
                    //标签，选填
                    consultingContent.setSobotGoodsLable("￥"+jdGoodsInfo.getPrice());
                    //可以设置为null
                    info.setConsultingContent(consultingContent);
                }
                //设置聊天界面标题显示模式
                SobotApi.setChatTitleDisplayMode(JdGoodsDetailActivity.this, SobotChatTitleDisplayMode.Default,"");

                SobotApi.startSobotChat(JdGoodsDetailActivity.this, info);
            }
        });

        ssv_goodsdetail_pic=(SlideShowView)findViewById(R.id.ssv_goodsdetail_pic);

        ll_goodsdetail=getViewById(R.id.ll_goodsdetail);
        ll_goodsdetail1=getViewById(R.id.ll_goodsdetail1);
        ll_goodsdetail1.setVisibility(View.VISIBLE);
        ll_goodsdetail.setVisibility(View.GONE);

        ll_jpjl_goodsdetail=getViewById(R.id.ll_jpjl_goodsdetail);
        ll_dsfrz_goodsdetail=getViewById(R.id.ll_dsfrz_goodsdetail);
        ll_dsfrz_goodsdetail.setVisibility(View.GONE);
        ll_jpjl_goodsdetail.setVisibility(View.VISIBLE);

        tv_name_goodsdetail1=getViewById(R.id.tv_name_goodsdetail1);
        tv_qprice_goodsdetail1=getViewById(R.id.tv_qprice_goodsdetail1);
        tv_jdprice_goodsdetail1=getViewById(R.id.tv_jdprice_goodsdetail1);
        tv_price_goodsdetail1=getViewById(R.id.tv_price_goodsdetail1);

        tv_scyg_goodsdetail=getViewById(R.id.tv_scyg_goodsdetail);
        tv_content_goodsdetail=getViewById(R.id.tv_content_goodsdetail);
        tv_content_goodsdetail.setVisibility(View.VISIBLE);

        mlv_ltjl=(MyListView)findViewById(R.id.mlv_ltjl);
        mlv_jpjl=(MyListView)findViewById(R.id.mlv_jpjl);

        wv_goodsdetail=getViewById(R.id.wv_goodsdetail);

        tv_countTime_goodsdetail1=(TextView) findViewById(R.id.tv_countTime_goodsdetail1);


        ll_buy_goodsdetail=(LinearLayout) findViewById(R.id.ll_buy_goodsdetail);
        ll_buy_goodsdetail.setOnClickListener(this);

        storeJDGoodsDetail();
    }

    public void initCountDownTimer(long millisInFuture) {
        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_countTime_goodsdetail1.setText(TimeTools.getCountTimeByLong(millisUntilFinished)+"后结束");
            }

            public void onFinish() {
                tv_countTime_goodsdetail1.setText("已结束");
            }
        };
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent();
        switch (v.getId())
        {
            case R.id.ll_buy_goodsdetail:
                if(BaseConstant.isLogin()){
                    intent.setClass(JdGoodsDetailActivity.this,OfferActivity.class);
                    intent.putExtra("id",jdGoodsInfo.getId());
                    if(jdGoodsInfo.getPrice()!=null&&!jdGoodsInfo.getPrice().equals("")){
                        if(Double.valueOf(jdGoodsInfo.getPrice())==0){
                            intent.putExtra("price",jdGoodsInfo.getAuthPrice());
                        }else{
                            intent.putExtra("price",jdGoodsInfo.getPrice());
                        }
                    }else{
                        intent.putExtra("price",jdGoodsInfo.getAuthPrice());
                    }

                    intent.putExtra("authPrice",jdGoodsInfo.getAuthPrice());
                    startActivity(intent);
                }else{
                    startActivity(new Intent(JdGoodsDetailActivity.this, LoginActivity.class));
                }

                break;
        }
    }

    private void showList(ArrayList<String> result) {

        CommonAdapter commAdapter = new CommonAdapter<String>(JdGoodsDetailActivity.this, result,
                R.layout.item_ltjl) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

            }
        };

        mlv_ltjl.setAdapter(commAdapter);

    }

    private void showList1(ArrayList<JdGoodsInfo.AuctionListInfo> result) {

        CommonAdapter commAdapter = new CommonAdapter<JdGoodsInfo.AuctionListInfo>(JdGoodsDetailActivity.this, result,
                R.layout.item_jpjl) {
            @Override
            public void convert(final ViewHolder helper, final JdGoodsInfo.AuctionListInfo item) {

                helper.setText(R.id.tv_name_jpjl,item.getUserId());
                helper.setText(R.id.tv_price_ltjl,"￥"+item.getPrice());
                helper.setText(R.id.tv_name_jpjl,item.getUserName());
                helper.setText(R.id.tv_time_ltjl,item.getCreateTime());
            }
        };

        mlv_jpjl.setAdapter(commAdapter);

    }

    private void storeJDGoodsDetail()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("storeGoods/storeJDGoodsDetail");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        OkGo.<DataResult<JdGoodsInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<JdGoodsInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<JdGoodsInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){

                                jdGoodsInfo=response.body().getData();

                                if(!response.body().getData().getImages().equals("")){
                                    List<String> list= Arrays.asList(response.body().getData().getImages().split(","));
                                    String[] headUrls = new String[list.size()] ;
                                    for (int i=0;i<list.size();i++){
                                        headUrls[i]=BaseConstant.Image_URL+list.get(i);
                                    }
                                    ssv_goodsdetail_pic.initAndSetImagesUrl(headUrls,
                                            new SlideShowView.OnImageClickListener() {
                                                @Override
                                                public void onClick(View v, int position) {
                                                }
                                            });
                                }

                                tv_name_goodsdetail1.setText(jdGoodsInfo.getGoodsName());
                                tv_qprice_goodsdetail1.setText("起价：￥"+jdGoodsInfo.getAuthPrice());
                                tv_jdprice_goodsdetail1.setText("鉴定价：￥"+jdGoodsInfo.getAuthPrice());

                                if(jdGoodsInfo.getPrice()!=null&&!jdGoodsInfo.getPrice().equals("")){
                                    if(Double.valueOf(jdGoodsInfo.getPrice())==0){
                                        tv_price_goodsdetail1.setText("￥"+jdGoodsInfo.getAuthPrice());
                                    }else{
                                        tv_price_goodsdetail1.setText("￥"+jdGoodsInfo.getPrice());
                                    }
                                }else{
                                    tv_price_goodsdetail1.setText("￥"+jdGoodsInfo.getAuthPrice());
                                }

//                                tv_buy_goodsdetail.setText("出价购买");

                                tv_content_goodsdetail.setText(jdGoodsInfo.getContent());

                                if(jdGoodsInfo.getGoodsAuctionList()!=null&&jdGoodsInfo.getGoodsAuctionList().size()>0){
                                    showList1(jdGoodsInfo.getGoodsAuctionList());
                                }

                                //倒计时
                                if(!response.body().getData().getTime().equals("")){
                                    if(Integer.valueOf(response.body().getData().getTime())>0){
                                        initCountDownTimer(Long.valueOf(response.body().getData().getTime())*1000);
                                        mCountDownTimer.start();
                                    }

                                }

                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<JdGoodsInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

}

package com.glavesoft.pawnuser.activity.pawn;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.PawnAuctionListInfo;
import com.glavesoft.pawnuser.mod.PawnDetailInfo;
import com.glavesoft.util.TimeTools;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.MyListView;
import com.glavesoft.view.SlideShowView;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/11/8
 * @company:常州宝丰
 */
public class PawnDetailActivity extends BaseActivity implements View.OnClickListener{
    private String id;
    private SlideShowView ssv_pawndetail_pic;
    private MyListView mlv_cjjl;
    private ArrayList<String> list=new ArrayList<>();
    private CountDownTimer mCountDownTimer;
    private TextView tv_countTime_pawndetail;
    private TextView tv_name_pawndetail,tv_jdprice_pawndetail,tv_qwdj_pawndetail,tv_qwll_pawndetail,
            tv_time_pawndetail,tv_num_pawndetail,tv_xxxz_pawndetail;

    private TextView tv_fq_pawndetail,tv_zdyselect_pawndetail,tv_xtselect_pawndetail;
    public static PawnDetailActivity pawnDetailActivity;

    private LinearLayout ll_cjjl_pawndetail,ll_select_pawndetail;
    private TextView tv_nocj_pawndetail,tv_qtcz_pawndetail,tv_pt_pawndetail;

    PawnDetailInfo pawnListInfo;

    PawnAuctionListInfo pawnAuctionListInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pawndetail);
        pawnDetailActivity=this;
        id=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("典当中详情");

        ssv_pawndetail_pic = (SlideShowView) findViewById(R.id.ssv_pawndetail_pic);
        mlv_cjjl = (MyListView) findViewById(R.id.mlv_cjjl);

        tv_countTime_pawndetail = (TextView) findViewById(R.id.tv_countTime_pawndetail);
        tv_name_pawndetail = (TextView) findViewById(R.id.tv_name_pawndetail);
        tv_jdprice_pawndetail = (TextView) findViewById(R.id.tv_jdprice_pawndetail);
        tv_qwdj_pawndetail = (TextView) findViewById(R.id.tv_qwdj_pawndetail);
        tv_qwll_pawndetail = (TextView) findViewById(R.id.tv_qwll_pawndetail);
        tv_time_pawndetail = (TextView) findViewById(R.id.tv_time_pawndetail);
        tv_num_pawndetail = (TextView) findViewById(R.id.tv_num_pawndetail);
        tv_xxxz_pawndetail = (TextView) findViewById(R.id.tv_xxxz_pawndetail);

        ll_cjjl_pawndetail = (LinearLayout) findViewById(R.id.ll_cjjl_pawndetail);
        ll_select_pawndetail = (LinearLayout) findViewById(R.id.ll_select_pawndetail);
        tv_nocj_pawndetail = (TextView) findViewById(R.id.tv_nocj_pawndetail);
        tv_qtcz_pawndetail = (TextView) findViewById(R.id.tv_qtcz_pawndetail);
        tv_pt_pawndetail = (TextView) findViewById(R.id.tv_pt_pawndetail);

        tv_fq_pawndetail = (TextView) findViewById(R.id.tv_fq_pawndetail);
        tv_zdyselect_pawndetail = (TextView) findViewById(R.id.tv_zdyselect_pawndetail);
        tv_xtselect_pawndetail = (TextView) findViewById(R.id.tv_xtselect_pawndetail);
        tv_fq_pawndetail.setOnClickListener(this);
        tv_zdyselect_pawndetail.setOnClickListener(this);
        tv_xtselect_pawndetail.setOnClickListener(this);
        tv_pt_pawndetail.setOnClickListener(this);

        mlv_cjjl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(PawnDetailActivity.this, OrgandetailActivity.class);
                intent.putExtra("id",pawnListInfo.getPawnAuctionList().get(position).getOrgId());
                startActivity(intent);
            }
        });

        pawningDetail();

    }

    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent();
        switch (v.getId())
        {
            case R.id.tv_fq_pawndetail:
                showPopupWindow();
                break;
            case R.id.tv_zdyselect_pawndetail:
                if(pawnListInfo!=null){
                    intent.setClass(PawnDetailActivity.this, SelectPawnShopActivity.class);
                    intent.putExtra("type","zdy");
                    intent.putExtra("pawnListInfo",pawnListInfo);
                    startActivity(intent);
                }
                break;
            case R.id.tv_xtselect_pawndetail:
                if(pawnListInfo!=null){
                    intent.setClass(PawnDetailActivity.this, SelectPawnShopActivity.class);
                    intent.putExtra("type","xt");
                    intent.putExtra("pawnListInfo",pawnListInfo);
                    intent.putExtra("pawnAuctionListInfo",pawnAuctionListInfo);
                    startActivity(intent);
                }

                break;
            case R.id.tv_pt_pawndetail://平台托底
                if(pawnListInfo!=null){
                    intent.setClass(PawnDetailActivity.this, PlatformActivity.class);
                    intent.putExtra("pawnid",pawnListInfo.getId());
                    startActivity(intent);
                }

                break;
        }
    }

    private PopupWindow popupWindo;

    public void showPopupWindow()
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(PawnDetailActivity.this).inflate(R.layout.pw_dialog1, null);
        Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button)view.findViewById(R.id.btn_ok);

        ((TextView)view.findViewById(R.id.tv_content)).setText("您确定要放弃本次竞拍典当吗？");
        btn_cancel.setText("确定放弃");
        btn_ok.setText("不放弃");

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                giveUpPawn();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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

    private void showList(ArrayList<PawnAuctionListInfo> result) {

        CommonAdapter commAdapter = new CommonAdapter<PawnAuctionListInfo>(PawnDetailActivity.this, result,
                R.layout.item_cjjl) {
            @Override
            public void convert(final ViewHolder helper, final PawnAuctionListInfo item) {

                helper.setText(R.id.tv_name_cjjl,item.getAuctionOrgname());
                helper.setText(R.id.tv_tgdj_cjjl,"￥"+item.getMoney());
                helper.setText(R.id.tv_zhll_cjjl,item.getRate()+"%");
                helper.setText(R.id.tv_lxll_cjjl,item.getMoneyRate()+"%");
                helper.setText(R.id.tv_time_cjjl,item.getCreateTime());
            }
        };

        mlv_cjjl.setAdapter(commAdapter);

    }

    private void pawningDetail()
    {
        if(!isFinishing()) {
            getlDialog().show();
        }
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/pawningDetail");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        OkGo.<DataResult<PawnDetailInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<PawnDetailInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<PawnDetailInfo>> response) {
                        if(!isFinishing()) {
                            getlDialog().dismiss();
                        }
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){

                                pawnListInfo=response.body().getData();
                                if(response.body().getData().getState().equals("0")) {
                                    if(!response.body().getData().getTime().equals("")){
                                        if(Integer.valueOf(response.body().getData().getTime())>0){
                                            initCountDownTimer(Long.valueOf(response.body().getData().getTime())*1000);
                                            mCountDownTimer.start();
                                        }

                                    }
                                }

                                if(!response.body().getData().getImage().equals("")){
                                    List<String> list= Arrays.asList(response.body().getData().getImage().split(","));
                                    String[] headUrls = new String[list.size()] ;
                                    for (int i=0;i<list.size();i++){
                                        headUrls[i]=BaseConstant.Image_URL+list.get(i);
                                    }
                                    ssv_pawndetail_pic.initAndSetImagesUrl(headUrls,
                                            new SlideShowView.OnImageClickListener() {
                                                @Override
                                                public void onClick(View v, int position) {
                                                }
                                            });
                                }


                                tv_name_pawndetail.setText(response.body().getData().getTitle());
                                tv_jdprice_pawndetail.setText("￥"+response.body().getData().getAuthPrice());
                                tv_qwdj_pawndetail.setText("￥"+response.body().getData().getLoansPrice());
                                tv_qwll_pawndetail.setText(response.body().getData().getLoansRate()+"%/月");

                                if(!response.body().getData().getPawnTime().equals("")){
                                    int days=Integer.valueOf(response.body().getData().getPawnTime())*15;
                                    tv_time_pawndetail.setText(days+"天");
                                }

                                ArrayList<PawnAuctionListInfo> pawnAuctionList =new ArrayList<>();
                                if(response.body().getData().getPawnAuctionList()!=null){
                                    for (int i=0;i<response.body().getData().getPawnAuctionList().size();i++){
                                        if (!response.body().getData().getPawnAuctionList().get(i).getOrgId().equals("0")){
                                            pawnAuctionList.add(response.body().getData().getPawnAuctionList().get(i));
                                        }
                                    }
                                }

                                tv_num_pawndetail.setText("出价记录"+pawnAuctionList.size()+"次");

                                if(pawnAuctionList.size()>0){
                                    ll_cjjl_pawndetail.setVisibility(View.VISIBLE);
                                    ll_select_pawndetail.setVisibility(View.VISIBLE);
                                    tv_qtcz_pawndetail.setVisibility(View.VISIBLE);
                                    tv_nocj_pawndetail.setVisibility(View.GONE);
//                                    tv_pt_pawndetail.setVisibility(View.INVISIBLE);
                                    showList(pawnAuctionList);
                                    getMax();
                                }else{
                                    ll_cjjl_pawndetail.setVisibility(View.GONE);
                                    ll_select_pawndetail.setVisibility(View.GONE);
                                    tv_qtcz_pawndetail.setVisibility(View.GONE);
                                    tv_nocj_pawndetail.setVisibility(View.VISIBLE);
//                                    if(response.body().getData().getState().equals("1")||Integer.valueOf(response.body().getData().getTime())<=0){//已结束
//                                        tv_pt_pawndetail.setVisibility(View.VISIBLE);
//                                    }else{
//                                        tv_pt_pawndetail.setVisibility(View.INVISIBLE);
//                                    }

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
                    public void onError(com.lzy.okgo.model.Response<DataResult<PawnDetailInfo>> response) {
                        if(!isFinishing()) {
                            getlDialog().dismiss();
                        }
                        showVolleyError(null);
                    }
                });
    }

    public void initCountDownTimer(long millisInFuture) {
        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_countTime_pawndetail.setText(TimeTools.getCountTimeByLong(millisUntilFinished)+"后结束");
            }

            public void onFinish() {
                tv_countTime_pawndetail.setText("已结束");
                if(pawnListInfo.getState().equals("0")) {
                    if(!pawnListInfo.getTime().equals("")){
                        if(Integer.valueOf(pawnListInfo.getTime())>0){
                            pawningDetail();
                        }
                    }
                }
            }
        };
    }

    //放弃本次竞拍
    private void giveUpPawn()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/giveUpPawn");
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
                            popupWindo.dismiss();
                            CustomToast.show("成功");
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

    private void getMax()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/getMax");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        OkGo.<DataResult<PawnAuctionListInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<PawnAuctionListInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<PawnAuctionListInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){
                                pawnAuctionListInfo=response.body().getData();
                                tv_xxxz_pawndetail.setText(response.body().getData().getAuctionOrgname()+"贷款费率最低");
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<PawnAuctionListInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

}

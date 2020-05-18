package com.glavesoft.pawnuser.activity.pawn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.ImagePageActivity;
import com.glavesoft.pawnuser.activity.main.WebActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.TradeRecordInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.view.MyListView;
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
 * @date: 2017/11/27
 * @company:常州宝丰
 */
public class PawnRecordDetailActivity extends BaseActivity {

    private TextView tv_state_pawnrecorddetail,tv_name_pawnrecorddetail;
    private TextView tv_jdprice_pawnrecorddetail;
    private TextView tv_orgname_pawnrecorddetail;
    private TextView tv_no_pawnrecorddetail;
    private GridViewForNoScroll gv_pics_pawnrecorddetail;
    private MyListView mlv_pawnrecorddetail;

    private TextView tv_ckdp_pawnrecord;

    private ArrayList<String> picurlList=new ArrayList<>();
    private TradeRecordInfo tradeRecordInfo;
    private String id,goodsId,isRedeem;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pawnrecorddetail);
        id=getIntent().getStringExtra("id");
        goodsId=getIntent().getStringExtra("goodsId");
        isRedeem=getIntent().getStringExtra("isRedeem");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("业务记录详情");
        setTitleNameEn(R.mipmap.business_record_details);

        tv_state_pawnrecorddetail=getViewById(R.id.tv_state_pawnrecorddetail);
        tv_name_pawnrecorddetail=getViewById(R.id.tv_name_pawnrecorddetail);
        tv_jdprice_pawnrecorddetail=getViewById(R.id.tv_jdprice_pawnrecorddetail);
        tv_orgname_pawnrecorddetail=getViewById(R.id.tv_orgname_pawnrecorddetail);
        tv_no_pawnrecorddetail=getViewById(R.id.tv_no_pawnrecorddetail);
        gv_pics_pawnrecorddetail=getViewById(R.id.gv_pics_pawnrecorddetail);
        mlv_pawnrecorddetail=getViewById(R.id.mlv_pawnrecorddetail);

        tv_ckdp_pawnrecord=getViewById(R.id.tv_ckdp_pawnrecord);

        gv_pics_pawnrecorddetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent();
                intent.setClass(PawnRecordDetailActivity.this, ImagePageActivity.class);
                intent.putExtra("picurlList",picurlList);
                intent.putExtra("selectPos",position);
                startActivity(intent);

            }
        });

        tv_ckdp_pawnrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PawnRecordDetailActivity.this,WebActivity.class);
                intent.putExtra("titleName","典当凭证");
                intent.putExtra("url",BaseConstant.BaseURL+"/m/pawn/toPawnTicket/"+id);
                startActivity(intent);
            }
        });

        tradeRecordDetail();
    }

    private void tradeRecordDetail()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/tradeRecordDetail");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        param.put("goodsId",goodsId);
        OkGo.<DataResult<TradeRecordInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<TradeRecordInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<TradeRecordInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){

                                tradeRecordInfo=response.body().getData();
                                if (isRedeem.equals("0")){
                                    tv_ckdp_pawnrecord.setVisibility(View.GONE);
                                    if(tradeRecordInfo.getType().equals("4")){
                                        tv_state_pawnrecorddetail.setText("绝当");
                                        tv_state_pawnrecorddetail.setBackgroundResource(R.drawable.shape_red1);
                                    }else if(tradeRecordInfo.getType().equals("3")){
                                        tv_state_pawnrecorddetail.setText("未典当");
                                        tv_state_pawnrecorddetail.setBackgroundResource(R.drawable.shape_orange);
                                    }else{
                                        tv_state_pawnrecorddetail.setText("典当中");
                                        tv_state_pawnrecorddetail.setBackgroundResource(R.drawable.shape_green1);
                                    }
                                }else{
                                    tv_ckdp_pawnrecord.setVisibility(View.VISIBLE);
                                    tv_state_pawnrecorddetail.setText("赎回");
                                    tv_state_pawnrecorddetail.setBackgroundResource(R.drawable.shape_green1);
                                }

                                tv_name_pawnrecorddetail.setText(tradeRecordInfo.getTitle());
                                tv_jdprice_pawnrecorddetail.setText("￥"+tradeRecordInfo.getAuthPrice());
                                tv_orgname_pawnrecorddetail.setText("当前典当机构："+tradeRecordInfo.getOrgName());
                                tv_no_pawnrecorddetail.setText("当号："+tradeRecordInfo.getPawnTicketCode());

                                if(tradeRecordInfo.getImages()!=null&&!tradeRecordInfo.getImages().equals("")){
                                    List<String> list= Arrays.asList(tradeRecordInfo.getImages().split(","));
                                    for(int i=0;i<list.size();i++){
                                        picurlList.add(BaseConstant.Image_URL+list.get(i));
                                    }
                                    showList(list);
                                }

                                if(tradeRecordInfo.getList()!=null&&tradeRecordInfo.getList().size()>0){
                                    showRecordList(tradeRecordInfo.getList());
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
                    public void onError(com.lzy.okgo.model.Response<DataResult<TradeRecordInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showList(List<String> result) {
        CommonAdapter commAdapter = new CommonAdapter<String>(PawnRecordDetailActivity.this, result,
                R.layout.item_pic_pawn) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

                ImageView iv_pic_pawn=(ImageView) helper.getView(R.id.iv_pic_pawn);
                getImageLoader().displayImage(BaseConstant.Image_URL+item,iv_pic_pawn,getImageLoaderOptions());
            }
        };

        gv_pics_pawnrecorddetail.setAdapter(commAdapter);
    }

    private void showRecordList(List<TradeRecordInfo.ListInfo> result) {
        CommonAdapter commAdapter = new CommonAdapter<TradeRecordInfo.ListInfo>(PawnRecordDetailActivity.this, result,
                R.layout.item_pawnrecorddetail) {
            @Override
            public void convert(final ViewHolder helper, final TradeRecordInfo.ListInfo item) {

                helper.setText(R.id.tv_time_pawnrecorddetail,item.getBeginTime());
                helper.setText(R.id.tv_price_pawnrecorddetail,"当款：￥"+item.getPrice());
                //0鉴定真品1典当2续当3赎当4绝当5交易6卖给平台
                if(item.getState().equals("0")){
                    helper.setText(R.id.tv_title_pawnrecorddetail,"入录拍当网系统");
                    helper.setText(R.id.tv_price_pawnrecorddetail,"鉴定价：￥"+item.getPrice());
                }else if(item.getState().equals("1")){
                    helper.setText(R.id.tv_title_pawnrecorddetail,"典当于："+item.getOrgName());
                }else if(item.getState().equals("2")){
                    helper.setText(R.id.tv_title_pawnrecorddetail,"续当于："+item.getOrgName());
                }else if(item.getState().equals("3")){
                    helper.setText(R.id.tv_title_pawnrecorddetail,"赎当于："+item.getOrgName());
                }else if(item.getState().equals("4")){
                    helper.setText(R.id.tv_title_pawnrecorddetail,"绝当于："+item.getOrgName());
                }else if(item.getState().equals("5")){
                    helper.setText(R.id.tv_title_pawnrecorddetail,"交易于："+item.getOrgName());
                }else if(item.getState().equals("6")){
                    helper.setText(R.id.tv_title_pawnrecorddetail,"卖给平台");
                }


            }
        };

        mlv_pawnrecorddetail.setAdapter(commAdapter);
    }

}

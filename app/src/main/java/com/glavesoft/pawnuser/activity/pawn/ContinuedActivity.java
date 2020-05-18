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
import com.glavesoft.pawnuser.activity.personal.ContractActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.ContinuedPawnInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/11/21
 * @company:常州宝丰
 */
public class ContinuedActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_name_pawn,tv_jdprice_pawn,tv_no_pawn;
    private GridViewForNoScroll gv_pics_pawn;

    private TextView tv_orgname_continued,tv_rate_continued,tv_money_continued,tv_redeemRate_continued;
    private TextView tv_beginDate_continued,tv_endDate_continued;
    private GridViewForNoScroll gv_timelong_continued;

    private TextView tv_next_continued;
    private String id="";

    private int index=-1;
    private ArrayList<String> daylist=new ArrayList<>();

    private ContinuedPawnInfo continuedPawnInfo;
    private ArrayList<String> picurlList=new ArrayList<>();

    public static  ContinuedActivity continuedActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continued);
        id=getIntent().getStringExtra("id");
        continuedActivity=this;
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("续当办理");
        titlebar_kf = (ImageView) findViewById(R.id.titlebar_kf);
        titlebar_kf.setVisibility(View.VISIBLE);
        titlebar_kf.setOnClickListener(this);

        tv_name_pawn=(TextView) findViewById(R.id.tv_name_pawn);
        tv_jdprice_pawn=(TextView) findViewById(R.id.tv_jdprice_pawn);
        tv_no_pawn=(TextView) findViewById(R.id.tv_no_pawn);
        gv_pics_pawn=(GridViewForNoScroll) findViewById(R.id.gv_pics_pawn);

        tv_orgname_continued=(TextView) findViewById(R.id.tv_orgname_continued);
        tv_rate_continued=(TextView) findViewById(R.id.tv_rate_continued);
        tv_money_continued=(TextView) findViewById(R.id.tv_money_continued);
        tv_redeemRate_continued=(TextView) findViewById(R.id.tv_redeemRate_continued);
        tv_beginDate_continued=(TextView) findViewById(R.id.tv_beginDate_continued);
        tv_endDate_continued=(TextView) findViewById(R.id.tv_endDate_continued);
        gv_timelong_continued=(GridViewForNoScroll) findViewById(R.id.gv_timelong_continued);

        tv_next_continued=(TextView) findViewById(R.id.tv_next_continued);
        tv_next_continued.setOnClickListener(this);

        daylist.add("15");daylist.add("30");daylist.add("45");daylist.add("60");
        daylist.add("75");daylist.add("90");daylist.add("105");daylist.add("120");
        daylist.add("135");daylist.add("150");daylist.add("165");daylist.add("180");
        showdayList(daylist);

        gv_pics_pawn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent();
                intent.setClass(ContinuedActivity.this, ImagePageActivity.class);
                intent.putExtra("picurlList",picurlList);
                intent.putExtra("selectPos",position);
                startActivity(intent);

            }
        });

        pawnConinueDetailFirst();
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent();
        switch (v.getId())
        {
            case R.id.titlebar_kf:
                gotokf_Z();
                break;
            case R.id.tv_next_continued:
                if (index == -1) {
                    CustomToast.show("请选择续当时长");
                    return;
                }
                if(continuedPawnInfo!=null){
                    intent.setClass(ContinuedActivity.this, SubmitConPawnActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("pawnTime",index+1);
                    intent.putExtra("endTime",continuedPawnInfo.getEndTime());
                    startActivity(intent);
                }
                break;
        }
    }

    private void gotokf_Z(){
        Information info = new Information();
        info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
        //用户资料
        info.setUid(LocalData.getInstance().getUserInfo().getId());
        info.setUname(LocalData.getInstance().getUserInfo().getId());
        info.setTel(LocalData.getInstance().getUserInfo().getAccount());
        info.setFace(BaseConstant.Image_URL+LocalData.getInstance().getUserInfo().getHeadImg());
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(4);

        //转接类型(0-可转入其他客服，1-必须转入指定客服)
        info.setTranReceptionistFlag(1);
        //指定客服id
        info.setReceptionistId("b125bade408341d4b1c825ee56a1dbb8");
        if(continuedPawnInfo!=null){
            //咨询内容
            ConsultingContent consultingContent = new ConsultingContent();
            //咨询内容标题，必填
            consultingContent.setSobotGoodsTitle(continuedPawnInfo.getTitle());
            //咨询内容图片，选填 但必须是图片地址
            if(!continuedPawnInfo.getImages().equals("")){
                List<String> list= Arrays.asList(continuedPawnInfo.getImages().split(","));
                consultingContent.setSobotGoodsImgUrl(BaseConstant.Image_URL+list.get(0));
            }
            //咨询来源页，必填
            String url=BaseConstant.BaseURL+"/m/pawn/getPawnContinue?id="+continuedPawnInfo.getId()+"&userId="+LocalData.getInstance().getUserInfo().getId();
            consultingContent.setSobotGoodsFromUrl(url);
            //描述，选填
            //consultingContent.setSobotGoodsDescribe("XXX超级电视 S5");
            //标签，选填
            consultingContent.setSobotGoodsLable("已发放当金：￥"+continuedPawnInfo.getMoney());
            //可以设置为null
            info.setConsultingContent(consultingContent);
        }
        //设置聊天界面标题显示模式
        SobotApi.setChatTitleDisplayMode(ContinuedActivity.this, SobotChatTitleDisplayMode.Default,"");

        SobotApi.startSobotChat(ContinuedActivity.this, info);
    }

    private void showdayList(ArrayList<String> result) {
        CommonAdapter commAdapter = new CommonAdapter<String>(ContinuedActivity.this, result,
                R.layout.item_timelong_pawn) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

                helper.setText(R.id.tv_day_pawn,item+"天");

                if(helper.getPosition()==index){
                    helper.getView(R.id.tv_day_pawn).setBackgroundResource(R.drawable.shape_yellow);
                    helper.setTextcolor(R.id.tv_day_pawn,getResources().getColor(R.color.white));
                }else{
                    helper.getView(R.id.tv_day_pawn).setBackgroundResource(R.drawable.shape_coner_gray);
                    helper.setTextcolor(R.id.tv_day_pawn,getResources().getColor(R.color.text_gray));
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

        gv_timelong_continued.setAdapter(commAdapter);
    }

    private void pawnConinueDetailFirst()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/pawnConinueDetailFirst");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        OkGo.<DataResult<ContinuedPawnInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ContinuedPawnInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<ContinuedPawnInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){
                                continuedPawnInfo=response.body().getData();
                                tv_name_pawn.setText(continuedPawnInfo.getTitle());
                                tv_jdprice_pawn.setText("￥"+continuedPawnInfo.getAuthPrice());
                                tv_no_pawn.setText("当号："+continuedPawnInfo.getGoodsId());

                                tv_orgname_continued.setText("典当行："+continuedPawnInfo.getOrgName());
                                tv_rate_continued.setText("月费率："+continuedPawnInfo.getRate()+"%/月");
                                tv_money_continued.setText("已发放当金：￥"+continuedPawnInfo.getMoney());
                                tv_redeemRate_continued.setText("月利率："+continuedPawnInfo.getMoneyRate()+"%/月");
                                tv_beginDate_continued.setText("借款日期："+continuedPawnInfo.getBeginTime());
                                tv_endDate_continued.setText("当前应还款日期："+continuedPawnInfo.getEndTime());

                                if(continuedPawnInfo.getImages()!=null&&!continuedPawnInfo.getImages().equals("")){
                                    List<String> list= Arrays.asList(continuedPawnInfo.getImages().split(","));
                                    for(int i=0;i<list.size();i++){
                                        picurlList.add(BaseConstant.Image_URL+list.get(i));
                                    }
                                    showList(list);
                                }

                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            finish();
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<ContinuedPawnInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showList(List<String> result) {
        CommonAdapter commAdapter = new CommonAdapter<String>(ContinuedActivity.this, result,
                R.layout.item_pic_pawn) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

                ImageView iv_pic_pawn=(ImageView) helper.getView(R.id.iv_pic_pawn);
                getImageLoader().displayImage(BaseConstant.Image_URL+item,iv_pic_pawn,getImageLoaderOptions());

            }
        };

        gv_pics_pawn.setAdapter(commAdapter);
    }

}

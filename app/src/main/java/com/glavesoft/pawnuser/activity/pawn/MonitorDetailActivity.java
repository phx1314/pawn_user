package com.glavesoft.pawnuser.activity.pawn;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.foamtrace.photopicker.Image;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.AuctionInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.PawnDetailInfo;
import com.glavesoft.pawnuser.mod.PawnVideoInfo;
import com.glavesoft.view.CustomToast;
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

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * @author 严光
 * @date: 2017/11/16
 * @company:常州宝丰
 */
public class MonitorDetailActivity extends BaseActivity {

    private ImageView iv_pic_monitor;
    private TextView tv_name_monitor,tv_jdprice_monitor,tv_no_monitor,tv_orag_monitor;
    private View xian_monitor;
    private ListView lv_listview;
    private ArrayList<AuctionInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    PawnVideoInfo pawnVideoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitordetail);
        pawnVideoInfo=(PawnVideoInfo)getIntent().getSerializableExtra("PawnVideoInfo");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("业务监控详情");
        setTitleNameEn(R.mipmap.details_of_business_monitoring);

        iv_pic_monitor=(ImageView) findViewById(R.id.iv_pic_monitor);
        tv_name_monitor=(TextView) findViewById(R.id.tv_name_monitor);
        tv_jdprice_monitor=(TextView)findViewById(R.id.tv_jdprice_monitor);
        tv_no_monitor=(TextView)findViewById(R.id.tv_no_monitor);
        tv_orag_monitor=(TextView)findViewById(R.id.tv_orag_monitor);
        xian_monitor=(View)findViewById(R.id.xian_monitor);
        xian_monitor.setVisibility(View.GONE);
        lv_listview=(ListView)findViewById(R.id.lv_monitordetail);

        if(!pawnVideoInfo.getImg().equals("")){
            List<String> list= Arrays.asList(pawnVideoInfo.getImg().split(","));
            getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0),iv_pic_monitor,getImageLoaderOptions());
        }
        tv_name_monitor.setText(pawnVideoInfo.getGoodName());

        if(pawnVideoInfo.getAuthPrice().equals("")){
            tv_jdprice_monitor.setText("暂无");
        }else{
            tv_jdprice_monitor.setText("￥"+pawnVideoInfo.getAuthPrice());
        }

        if(pawnVideoInfo.getCode().equals("")){
            tv_no_monitor.setVisibility(View.GONE);
        }else{
            tv_no_monitor.setVisibility(View.VISIBLE);
            tv_no_monitor.setText("当号："+pawnVideoInfo.getCode());
        }

        if(pawnVideoInfo.getOrgName().equals("")){
            tv_orag_monitor.setVisibility(View.GONE);
        }else{
            tv_orag_monitor.setVisibility(View.VISIBLE);
            tv_orag_monitor.setText("机构："+pawnVideoInfo.getOrgName());
        }

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JCVideoPlayerStandard.startFullscreen(MonitorDetailActivity.this, JCVideoPlayerStandard.class,
                        BaseConstant.Video_URL + list.get(position).getUrl(), "");
            }
        });
        myVideoDetail();
    }

    private void showList(ArrayList<AuctionInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<AuctionInfo>(MonitorDetailActivity.this, result,
                    R.layout.item_monitor_video) {
                @Override
                public void convert(final ViewHolder helper, final AuctionInfo item) {
                    ImageView iv_pic_monitor=(ImageView) helper.getView(R.id.iv_pic_monitor);
                    if(!pawnVideoInfo.getImg().equals("")){
                        List<String> list= Arrays.asList(pawnVideoInfo.getImg().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL +list.get(0),iv_pic_monitor,getImageLoaderOptions());
                    }else{
                        getImageLoader().displayImage("",iv_pic_monitor,getImageLoaderOptions());
                    }
                    helper.setText(R.id.tv_title_monitorvideo,item.getName());
                    helper.setText(R.id.tv_time_monitorvideo,"");
                    helper.setText(R.id.tv_long_monitorvideo,"");

                }
            };

            lv_listview.setAdapter(commAdapter);
        } else {
            if (list == null || list.size() == 0) {
                list = result;
            } else {
                for (int i = 0; i < result.size(); i++) {
                    list.add(result.get(i));
                }
            }
            commAdapter.onDateChange(list);
        }
    }

    private void myVideoDetail()
    {
        if(!isFinishing()) {
            getlDialog().show();
        }
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/myVideoDetail");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",pawnVideoInfo.getId());
        OkGo.<DataResult<PawnVideoInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<PawnVideoInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<PawnVideoInfo>> response) {
                        if(!isFinishing()) {
                            getlDialog().dismiss();
                        }
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){

                                if(!response.body().getData().getGoVideo().equals("")&&!response.body().getData().getGoVideo().equals("null")){
                                    AuctionInfo info=new AuctionInfo();
                                    info.setName("打包视频");
                                    info.setUrl(response.body().getData().getGoVideo());
                                    list.add(info);
                                }
                                if(!response.body().getData().getOpenVideo().equals("")&&!response.body().getData().getOpenVideo().equals("null")){
                                    AuctionInfo info1=new AuctionInfo();
                                    info1.setName("拆收视频");
                                    info1.setUrl(response.body().getData().getOpenVideo());
                                    list.add(info1);
                                }
                                if(!response.body().getData().getPlatGoodsAuthVideo().equals("")&&!response.body().getData().getPlatGoodsAuthVideo().equals("null")){
                                    AuctionInfo info2=new AuctionInfo();
                                    info2.setName("鉴定估价过程视频");
                                    info2.setUrl(response.body().getData().getPlatGoodsAuthVideo());
                                    list.add(info2);
                                }

                                showList(list);
                            }

                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<PawnVideoInfo>> response) {
                        if(!isFinishing()) {
                            getlDialog().dismiss();
                            showVolleyError(null);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}

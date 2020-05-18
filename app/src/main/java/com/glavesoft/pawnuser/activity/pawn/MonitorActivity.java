package com.glavesoft.pawnuser.activity.pawn;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.personal.AddressActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.AddressInfo;
import com.glavesoft.pawnuser.mod.AuctionInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.PawnVideoInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2017/11/16
 * @company:常州宝丰
 */
public class MonitorActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<PawnVideoInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;

    private LinearLayout ll_nodata;
    private TextView tv_nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_goods);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("业务监控");
        setTitleNameEn(R.mipmap.business_monitoring);

        ll_nodata=(LinearLayout) findViewById(R.id.ll_nodata);
        tv_nodata=(TextView) findViewById(R.id.tv_nodata);
        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MonitorActivity.this, MonitorDetailActivity.class);
                intent.putExtra("PawnVideoInfo",list.get(position));
                startActivity(intent);
            }
        });

        myVideoList();
    }

    private void showList(ArrayList<PawnVideoInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<PawnVideoInfo>(MonitorActivity.this, result,
                    R.layout.item_monitor) {
                @Override
                public void convert(final ViewHolder helper, final PawnVideoInfo item) {
                    ImageView iv_pic_monitor=(ImageView) helper.getView(R.id.iv_pic_monitor);
                    if(!item.getImg().equals("")){
                        List<String> list= Arrays.asList(item.getImg().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0),iv_pic_monitor,getImageLoaderOptions());
                    }else{
                        getImageLoader().displayImage("",iv_pic_monitor,getImageLoaderOptions());
                    }
                    helper.setText(R.id.tv_name_monitor,item.getGoodName());
                    if(item.getAuthPrice().equals("")){
                        helper.setText(R.id.tv_jdprice_monitor,"暂无");
                    }else{
                        helper.setText(R.id.tv_jdprice_monitor,"￥"+item.getAuthPrice());
                    }

                    if(item.getCode().equals("")){
                        helper.getView(R.id.tv_no_monitor).setVisibility(View.GONE);
                    }else{
                        helper.getView(R.id.tv_no_monitor).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_no_monitor,"当号："+item.getCode());
                    }

                    if(item.getOrgName().equals("")){
                        helper.getView(R.id.tv_orag_monitor).setVisibility(View.GONE);
                    }else{
                        helper.getView(R.id.tv_orag_monitor).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_orag_monitor,"机构："+item.getOrgName());
                    }

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

    private void resetPageData()
    {
        page = 1;
        list.clear();
        commAdapter = null;
        lv_listview.setAdapter(null);
        myVideoList();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            myVideoList();
        }else{
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }


    private void myVideoList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/myVideoList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<PawnVideoInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<PawnVideoInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<PawnVideoInfo>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                ll_nodata.setVisibility(View.GONE);
                                mRefreshLayout.setVisibility(View.VISIBLE);
                                listsize=response.body().getData().size();
                                showList(response.body().getData());
                            }else{
                                if (list.size()==0){
                                    ll_nodata.setVisibility(View.VISIBLE);
                                    mRefreshLayout.setVisibility(View.GONE);
                                    tv_nodata.setText("暂无业务监控信息，快去别的地方逛逛哦~");
                                }
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<PawnVideoInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

}

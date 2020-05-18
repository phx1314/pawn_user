package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
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
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.CheckCertificateInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
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
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2017/12/5
 * @company:常州宝丰
 */
public class CertificateListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<CheckCertificateInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;

    private String type;
    private String number;
    private LinearLayout ll_nodata;
    private TextView tv_nodata;
    private String name,length,width,height,weight,cz,ztcz,qtfc,time,content;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_goods);
        initData();
        initView();
    }

    private void initData() {
        type=getIntent().getStringExtra("type");
        if(type.equals("number")){
            number=getIntent().getStringExtra("number");
        }else{
            name=getIntent().getStringExtra("name");
            length=getIntent().getStringExtra("length");
            height=getIntent().getStringExtra("height");
            width=getIntent().getStringExtra("width");
            weight=getIntent().getStringExtra("weight");
            cz=getIntent().getStringExtra("cz");
            ztcz=getIntent().getStringExtra("ztcz");
            qtfc=getIntent().getStringExtra("qtfc");
            time=getIntent().getStringExtra("time");
            content=getIntent().getStringExtra("content");
        }
    }

    private void initView() {
        setTitleBack();
        setTitleName("查询结果");
        setTitleNameEn(R.mipmap.query_results);

        ll_nodata=(LinearLayout) findViewById(R.id.ll_nodata);
        tv_nodata=(TextView) findViewById(R.id.tv_nodata);
        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);
        http://localhost:82/m/pawn/getCertificate?id=1
        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(CertificateListActivity.this,WebActivity.class);
                intent.putExtra("titleName","证书详情");
                intent.putExtra("url",BaseConstant.BaseURL+"/m/pawn/getCertificate?id="+list.get(position).getId());
                startActivity(intent);
            }
        });

        if(type.equals("number")){
            checkCertificateByNum();
        }else{
            checkCertificate();
        }

    }

    private void showList(ArrayList<CheckCertificateInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<CheckCertificateInfo>(CertificateListActivity.this, result,
                    R.layout.item_certificate) {
                @Override
                public void convert(final ViewHolder helper, final CheckCertificateInfo item) {
                    ImageView iv_pic_certificate=(ImageView) helper.getView(R.id.iv_pic_certificate);
                    if(!item.getImg().equals("")){
                        getImageLoader().displayImage(BaseConstant.Image_URL + item.getImg(),iv_pic_certificate,getImageLoaderOptions());
                    }else{
                        getImageLoader().displayImage("",iv_pic_certificate,getImageLoaderOptions());
                    }
                    helper.setText(R.id.tv_name_certificate,item.getName());
                    helper.setText(R.id.tv_number_certificate,"编号："+item.getCode());
                    helper.setText(R.id.tv_cc_certificate,"尺寸："+item.getSize()+"cm");
                    helper.setText(R.id.tv_weight_certificate,"重量："+item.getWeight()+"kg");
                    helper.setText(R.id.tv_cz_certificate,"材质："+item.getMaterial());
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

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            if(type.equals("number")){
                checkCertificateByNum();
            }else{
                checkCertificate();
            }
        }else{
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    private void resetPageData()
    {
        page = 1;
        list.clear();
        commAdapter = null;
        lv_listview.setAdapter(null);
        if(type.equals("number")){
            checkCertificateByNum();
        }else{
            checkCertificate();
        }
    }

    private void checkCertificateByNum()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/checkCertificateByNumer");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("code",number);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<CheckCertificateInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<CheckCertificateInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<CheckCertificateInfo>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){
                                ll_nodata.setVisibility(View.GONE);
                                mRefreshLayout.setVisibility(View.VISIBLE);
                                ArrayList<CheckCertificateInfo> result=new ArrayList<CheckCertificateInfo>();
                                result.add(response.body().getData());
                                showList(result);
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            //CustomToast.show(response.body().getErrorMsg());
                            ll_nodata.setVisibility(View.VISIBLE);
                            mRefreshLayout.setVisibility(View.GONE);
                            tv_nodata.setText("内容不存在，请输入正确的编号");
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<CheckCertificateInfo>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        showVolleyError(null);
                    }
                });
    }

    private void checkCertificate()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/checkCertificate");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("name",name);
        param.put("length",length);
        param.put("width",width);
        param.put("height",height);
        param.put("weight",weight);
        param.put("material",cz);
        param.put("mainMaterial",ztcz);
        param.put("otherMaterial",qtfc);
        param.put("createYear",time);
        param.put("other",content);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<CheckCertificateInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<CheckCertificateInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<CheckCertificateInfo>>> response) {
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
                                    tv_nodata.setText("内容不存在，请输入正确的信息");
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
                    public void onError(Response<DataResult<ArrayList<CheckCertificateInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

}

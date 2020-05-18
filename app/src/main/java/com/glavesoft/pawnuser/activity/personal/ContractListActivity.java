package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.android.volley.VolleyError;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.AddressInfo;
import com.glavesoft.pawnuser.mod.ContractInfo;
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
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2018/1/25
 */
public class ContractListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<ContractInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;
    private String title;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        title=getIntent().getStringExtra("title");
        id=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName(title);
        setTitleNameEn(R.mipmap.query_results);

        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContractListActivity.this,ContractDetailActivity.class);
                if(list.get(position).getType().equals("1")){
                    intent.putExtra("titleName","典当合同");
                }else{
                    intent.putExtra("titleName","续当合同");
                }
                intent.putExtra("title",title);
                intent.putExtra("time",list.get(position).getVerifyTime());
                intent.putExtra("url",BaseConstant.BaseURL+"/m/pawn/H5GetContract?id="+list.get(position).getId()+"&type="+list.get(position).getType());
                startActivity(intent);
            }
        });

        getContractType();
    }

    private void showList(ArrayList<ContractInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<ContractInfo>(ContractListActivity.this, result,
                    R.layout.item_contract_list) {
                @Override
                public void convert(final ViewHolder helper, final ContractInfo item) {
                    if(item.getType().equals("1")){
                        helper.setText(R.id.tv_title_contractlist,"典当合同");
                    }else{
                        helper.setText(R.id.tv_title_contractlist,"续当合同");
                    }

                    helper.setText(R.id.tv_time_contractlist,"签订时间："+item.getVerifyTime());
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
        getContractType();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            getContractType();
        }else{
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    private void getContractType()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/getContractType");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<ContractInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<ContractInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<ContractInfo>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                listsize=response.body().getData().size();
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
                    public void onError(Response<DataResult<ArrayList<ContractInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

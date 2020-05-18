package com.glavesoft.pawnuser.activity.personal;

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
import com.glavesoft.pawnuser.mod.AddressInfo;
import com.glavesoft.pawnuser.mod.ContractInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.FlexibleRoundedBitmapDisplayer;
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
 * @date: 2018/1/24
 * @company:常州宝丰
 */
public class ContractActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<ContractInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;
    private LinearLayout ll_nodata;
    private TextView tv_nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("合同记录");
        setTitleNameEn(R.mipmap.contract_record);

        ll_nodata=(LinearLayout) findViewById(R.id.ll_nodata);
        tv_nodata=(TextView) findViewById(R.id.tv_nodata);
        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContractActivity.this, ContractListActivity.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("title",list.get(position).getTitle());
                startActivity(intent);
            }
        });

        getContractList();
    }

    private void showList(ArrayList<ContractInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<ContractInfo>(ContractActivity.this, result,
                    R.layout.item_contract) {
                @Override
                public void convert(final ViewHolder helper, final ContractInfo item) {
                    ImageView iv_pic_contract = (ImageView) helper.getView(R.id.iv_pic_contract);

                    if(!item.getImg().equals("")){
                        List<String> list= Arrays.asList(item.getImg().split(","));
//                        setRoundedImage(//圆角图
//                                BaseConstant.Image_URL +  list.get(0),
//                                ScreenUtils.dp2px(mContext,5),
//                                FlexibleRoundedBitmapDisplayer.CORNER_ALL,
//                                R.drawable.shape_coner_white2,
//                                iv_pic_contract
//                        );
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0),iv_pic_contract,getImageLoaderOptions());
                    }else{
                        iv_pic_contract.setImageDrawable(null);
                    }
                    helper.setText(R.id.tv_name_contract,item.getTitle());
                    helper.setText(R.id.tv_code_contract,"当号："+item.getCode());
                    helper.setText(R.id.tv_price_contract,item.getPawnMoney()+"元");
                    helper.setText(R.id.tv_time_contract,"签订时间："+item.getVerifyTime());

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
        getContractList();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            getContractList();
        }else{
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    private void getContractList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/getContractList");
        HttpParams param=new HttpParams();
        param.put("token",token);
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
                                ll_nodata.setVisibility(View.GONE);
                                mRefreshLayout.setVisibility(View.VISIBLE);
                                listsize=response.body().getData().size();
                                showList(response.body().getData());
                            }else{
                                if (list.size()==0){
                                    ll_nodata.setVisibility(View.VISIBLE);
                                    mRefreshLayout.setVisibility(View.GONE);
                                    tv_nodata.setText("暂无合同记录信息，快去别的地方逛逛哦~");
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
                    public void onError(Response<DataResult<ArrayList<ContractInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

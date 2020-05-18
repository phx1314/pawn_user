package com.glavesoft.pawnuser.activity.personal;

import android.os.Bundle;
import android.widget.ListView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ExpressInfo;
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
 * @date: 2018/2/1
 * @company:常州宝丰
 */
public class LogisticsdetailActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    CommonAdapter commAdapter;
    private String expressId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        expressId=getIntent().getStringExtra("expressId");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("物流详情");
        setTitleNameEn(R.mipmap.logistics_details);

        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

        expressInfoByStore();
    }

    private void showList(ArrayList<ExpressInfo> result) {
        commAdapter = new CommonAdapter<ExpressInfo>(LogisticsdetailActivity.this, result,
                R.layout.item_logisticsdetail) {
            @Override
            public void convert(final ViewHolder helper, final ExpressInfo item) {

                helper.setText(R.id.tv_time_logisticsdetail,item.getTime());
                helper.setText(R.id.tv_content_logisticsdetail,item.getContext());
            }
        };

        lv_listview.setAdapter(commAdapter);

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        expressInfoByStore();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    public class Expresslist{
        ArrayList<ExpressInfo> data;
    }

    private void expressInfoByStore()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/expressInfoByStore");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("expressId",expressId);
        OkGo.<DataResult<Expresslist>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<Expresslist>>() {
                    @Override
                    public void onSuccess(Response<DataResult<Expresslist>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().data!=null&&response.body().getData().data.size()>0){
                                showList(response.body().getData().data);
                            }else{
                                CustomToast.show("暂无物流信息");
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<Expresslist>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

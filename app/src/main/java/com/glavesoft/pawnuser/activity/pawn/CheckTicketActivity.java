package com.glavesoft.pawnuser.activity.pawn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.activity.main.ImageActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.CheckTicketInfo;
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
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2017/12/5
 * @company:常州宝丰
 */
public class CheckTicketActivity  extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<CheckTicketInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_goods);
        id=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("打款凭证");

        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        checkTicket();
    }


    private void showList(ArrayList<CheckTicketInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<CheckTicketInfo>(CheckTicketActivity.this, result,
                    R.layout.item_dkpz) {
                @Override
                public void convert(final ViewHolder helper, final CheckTicketInfo item) {
                    // 1机构打款凭证 2续当打款凭证 3赎当打款凭证
                    if(item.getState().equals("1")){
                        helper.setText(R.id.tv_state_dkpz,"机构打款凭证：");
                    }else if(item.getState().equals("2")){
                        helper.setText(R.id.tv_state_dkpz,"续当打款凭证：");
                    }else if(item.getState().equals("3")){
                        helper.setText(R.id.tv_state_dkpz,"赎当打款凭证：");
                    }
                    ImageView iv_dkpz=(ImageView) helper.getView(R.id.iv_dkpz);
                    if(!item.getTicket().equals("")){
                        getImageLoader().displayImage(BaseConstant.Image_URL + item.getTicket(),iv_dkpz,getImageLoaderOptions());
                    }else {
                        getImageLoader().displayImage("",iv_dkpz,getImageLoaderOptions());
                    }

                    iv_dkpz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent();
                            intent.setClass(CheckTicketActivity.this,ImageActivity.class);
                            intent.putExtra("url",BaseConstant.Image_URL + item.getTicket());
                            startActivity(intent);
                        }
                    });
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
            checkTicket();
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
        checkTicket();
    }

    private void checkTicket()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/checkTicket");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<CheckTicketInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<CheckTicketInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<CheckTicketInfo>>> response) {
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
                    public void onError(Response<DataResult<ArrayList<CheckTicketInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

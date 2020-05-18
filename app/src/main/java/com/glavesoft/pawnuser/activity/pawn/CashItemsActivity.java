package com.glavesoft.pawnuser.activity.pawn;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.TradeRecordInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 绝当品展览
 * @author 严光
 * @date: 2017/11/15
 * @company:常州宝丰
 */
public class CashItemsActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<String> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_cashitem);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("绝当品展览");
        setTitle_News(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

//        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(CashItemsActivity.this, PawnRecordDetailActivity.class);
//                intent.putExtra("id",list.get(position).getId());
//                intent.putExtra("goodsId",list.get(position).getGoodsId());
//                startActivity(intent);
//            }
//        });

//        tradeRecordList();


        list.add("");
        list.add("");
        showList(list);
    }

    private void showList(ArrayList<String> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<String>(CashItemsActivity.this, result,
                    R.layout.item_cashitems) {
                @Override
                public void convert(final ViewHolder helper, final String item) {

                    ImageView iv_cash_pic=(ImageView) helper.getView(R.id.iv_cash_pic);
                    switch (helper.getPosition()){
                        case 1:
                            iv_cash_pic.setBackgroundResource(R.drawable.ls_8);
                            break;
                        case 0:
                            iv_cash_pic.setBackgroundResource(R.drawable.ls_7);
                            break;
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

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
//            tradeRecordList();
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
//        list.clear();
//        commAdapter = null;
//        lv_listview.setAdapter(null);
//        tradeRecordList();

        mRefreshLayout.endRefreshing();
        mRefreshLayout.endLoadingMore();
    }

    private void tradeRecordList() {
        String token= LocalData.getInstance().getUserInfo().getToken();
        getlDialog().show();
        Map<String, String> param = VolleyUtil.getRequestMap(CashItemsActivity.this);
        param.put("token",token);
        param.put("page",page+"");
        param.put("limit","10");

        java.lang.reflect.Type classtype = new TypeToken<DataResult<ArrayList<TradeRecordInfo>>>()
        {
        }.getType();

        VolleyUtil.postObjectApi(BaseConstant.getApiPostUrl("home/tradeRecordList"), param, classtype, new ResponseListener<DataResult<ArrayList<TradeRecordInfo>>>()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                getlDialog().dismiss();
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                showVolleyError(error);
            }

            @Override
            public void onResponse(DataResult<ArrayList<TradeRecordInfo>> response)
            {
                getlDialog().dismiss();
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();

                if (response == null)
                {
                    CustomToast.show(getString(R.string.http_request_fail));
                    return;
                }
                if (DataResult.RESULT_OK_ZERO == response.getErrorCode())
                {
                    if(response.getData()!=null&&response.getData().size()>0){
                        listsize=response.getData().size();
//                        showList(response.getData());
                    }

                }else if (DataResult.RESULT_102 == response.getErrorCode())
                {
                    toLogin();
                }else
                {
                    CustomToast.show(response.getErrorMsg());
                }
            }
        });
    }
}

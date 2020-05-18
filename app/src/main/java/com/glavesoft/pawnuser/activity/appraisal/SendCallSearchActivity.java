package com.glavesoft.pawnuser.activity.appraisal;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.SendCallGoodInfo;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.util.ScreenUtils;
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

public class SendCallSearchActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private String keyword="";
    private BGARefreshLayout mRefreshLayout;
    private GridView no_gridView;
    private ArrayList<SendCallGoodInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_call_search);
        initView();
    }

    private void initView() {
        settitle_Searchcancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gettitle_Searchet().setText(keyword);
        gettitle_Searchet().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(!v.getText().toString().trim().equals("")){
                       //savedata(v.getText().toString().trim());
                        keyword=v.getText().toString().trim();
                        resetPageData();
                    }
                    return true;
                }
                return false;
            }
        });

        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(SendCallSearchActivity.this, true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        no_gridView=(GridView)findViewById(R.id.no_gridView);

        no_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(SendCallSearchActivity.this, SendCallGoodDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);
            }
        });

        if(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical, null)!=null){
            java.lang.reflect.Type classtype = new TypeToken<ArrayList<String>>() {}.getType();
            String jsonString=PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical,null);
//            historcalList = CommonUtils.fromJson(jsonString, classtype, CommonUtils.DEFAULT_DATE_PATTERN);
        }

//        resetPageData();
    }
    public void resetPageData()
    {
        page = 1;
        list.clear();
        commAdapter = null;
        no_gridView.setAdapter(null);
        searchInfo();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if(keyword.toString().trim().length()>0&&!keyword.toString().equals("")){
            resetPageData();
        }else{
            keyword="";
            list.clear();
            commAdapter = null;
            no_gridView.setAdapter(null);
            mRefreshLayout.endRefreshing();
            mRefreshLayout.endLoadingMore();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            searchInfo();
        }else{
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    private void searchInfo()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/sellIndex");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("name",keyword);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<SendCallGoodInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<SendCallGoodInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<SendCallGoodInfo>>> response) {
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
                            }else{

                            }

                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<SendCallGoodInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
    private void showList(ArrayList<SendCallGoodInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<SendCallGoodInfo>(SendCallSearchActivity.this, result,
                    R.layout.item_sendcall_find) {
                @Override
                public void convert(final ViewHolder helper, final SendCallGoodInfo item) {
                    helper.setText(R.id.tv_item_goods_title,item.getName());
                    helper.setText(R.id.tv_name,item.getNickName());
                    helper.setText(R.id.tv_official_price,"官方鉴定价："+item.getAuthPrice());
                    helper.setText(R.id.tv_price,"￥"+item.getSellPrice());
                    ImageView iv_good=(ImageView) helper.getView(R.id.iv_item_goods_icon);

                    int width= ScreenUtils.getInstance().getWidth();
                    width=width-ScreenUtils.dp2px(mContext,24);
                    width=width/2;
                    ViewGroup.LayoutParams lp = iv_good.getLayoutParams();
                    lp.width=width;
                    lp.height = width*100/168;
                    iv_good.setLayoutParams(lp);

                    if(!item.getSellImgs().equals("")){
                        List<String> list= Arrays.asList(item.getGoodsImgs().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0),iv_good,getImageLoaderOptions());
                    }else{
                        getImageLoader().displayImage("",iv_good,getImageLoaderOptions());
                    }
                    if(!item.getHeadImg().equals("")){
                        getImageLoader().displayImage(BaseConstant.Image_URL + item.getHeadImg(),
                                (ImageView) helper.getView(R.id.my_photo),getImageLoaderOptions());
                    }else{
                        //getImageLoader().displayImage("",iv_good,getImageLoaderOptions());
                    }

                }
            };

            no_gridView.setAdapter(commAdapter);
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
}

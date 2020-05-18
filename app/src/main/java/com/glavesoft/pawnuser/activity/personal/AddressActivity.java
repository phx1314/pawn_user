package com.glavesoft.pawnuser.activity.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.MessageActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.AddressInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.MyAppraisalInfo;
import com.glavesoft.util.ScreenUtils;
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
 * @date: 2017/10/20
 * @company:常州宝丰
 */
public class AddressActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<AddressInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private LinearLayout ll_submit;
    private String type;
    private int page=1;
    private int listsize=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview1);
        type=getIntent().getStringExtra("type");
        setBoardCast();
        initView();
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("AddressRefresh");
        registerReceiver(mListenerID, f);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            resetPageData();
        }
    };

    public  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mListenerID);
    }

    private void initView() {
        setTitleBack();
        setTitleName("我的地址");
        setTitleNameEn(R.mipmap.my_address);

        ll_submit=(LinearLayout) findViewById(R.id.ll_submit);
        ll_submit.setVisibility(View.VISIBLE);

        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(AddressActivity.this,AddaddressActivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });

        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(type.equals("select")){
                    Intent intent=new Intent();
                    intent.putExtra("AddressInfo", list.get(position));
                    setResult(10, intent);
                    finish();
                }
            }
        });

        myAddressList();
    }

    private void showList(ArrayList<AddressInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<AddressInfo>(AddressActivity.this, result,
                    R.layout.item_address) {
                @Override
                public void convert(final ViewHolder helper, final AddressInfo item) {

                    helper.setText(R.id.tv_name_address,item.getName());
                    helper.setText(R.id.tv_phone_address,item.getPhone());
                    helper.setText(R.id.tv_address,item.getArea()+item.getAddress());

                    ImageView iv_mr_address= helper.getView(R.id.iv_mr_address);
                    if(item.getIs_default().equals("1")){
                        iv_mr_address.setImageResource(R.mipmap.mr);
                    }else{
                        iv_mr_address.setImageResource(R.mipmap.mr_);
                    }

                    helper.getView(R.id.ll_mr_address).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setDefAddress(item.getId());
                        }
                    });

                    helper.getView(R.id.tv_del_address).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateMyAddress(item.getId());
                        }
                    });

                    helper.getView(R.id.tv_update_address).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =new Intent();
                            intent.setClass(AddressActivity.this,AddaddressActivity.class);
                            intent.putExtra("type","update");
                            intent.putExtra("AddressInfo",item);
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

    private void resetPageData()
    {
        page = 1;
        list.clear();
        commAdapter = null;
        lv_listview.setAdapter(null);
        myAddressList();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            myAddressList();
        }else{
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }


    private void myAddressList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/myAddressList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<AddressInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<AddressInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<AddressInfo>>> response) {
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
                    public void onError(Response<DataResult<ArrayList<AddressInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

    //设置默认地址
    private void setDefAddress(String id)
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/setDefAddress");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        OkGo.<DataResult<DataInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<DataInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            CustomToast.show("设置成功");
                            resetPageData();
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    //编辑地址
    private void updateMyAddress(String id)
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/updateMyAddress");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        param.put("type","2");
        OkGo.<DataResult<DataInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<DataInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            CustomToast.show("删除成功");
                            resetPageData();
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}

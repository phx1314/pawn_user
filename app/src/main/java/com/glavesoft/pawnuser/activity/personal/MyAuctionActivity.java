package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.OfferActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.JdGoodsDetailActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.JdStoreGoodsAucInfo;
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
 * @date: 2017/10/23
 * @company:常州宝丰
 */
public class MyAuctionActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<JdStoreGoodsAucInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private TextView tv_beizhu;
    private int page=1;
    private int listsize=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_goods);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("我的绝当竞拍");

        tv_beizhu = (TextView) findViewById(R.id.tv_beizhu);
        tv_beizhu.setVisibility(View.VISIBLE);

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
                Intent intent = new Intent(MyAuctionActivity.this, JdGoodsDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);
            }
        });

        jdStoreGoodsAuc();
    }

    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownCounters= new SparseArray<>();
    private void showList(ArrayList<JdStoreGoodsAucInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<JdStoreGoodsAucInfo>(MyAuctionActivity.this, result,
                    R.layout.item_jplist) {
                @Override
                public void convert(final ViewHolder helper, final JdStoreGoodsAucInfo item) {

                    helper.getView(R.id.tv_buy_jp).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent();
                            intent.setClass(MyAuctionActivity.this,OfferActivity.class);
                            intent.putExtra("id",item.getId());
                            intent.putExtra("price",item.getMaxPirce());
                            intent.putExtra("authPrice",item.getAuthPrice());
                            mContext.startActivity(intent);
                        }
                    });

                    ImageView iv_pic_jp=(ImageView) helper.getView(R.id.iv_pic_jp);
                    if(!item.getGoodsImg().equals("")){
                        List<String> list= Arrays.asList(item.getGoodsImg().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0),iv_pic_jp,getImageLoaderOptions());
                    }else{
                        getImageLoader().displayImage("",iv_pic_jp,getImageLoaderOptions());
                    }

                    helper.setText(R.id.tv_name_jp,item.getTitle());

                    if(item.getState().equals("2")){
                        helper.setText(R.id.tv_status_jp,"已中标");
                    }else if(item.getState().equals("3")){
                        helper.setText(R.id.tv_status_jp,"未中标");
                    }else{
                        helper.setText(R.id.tv_status_jp,"竞拍中");
                    }
                    helper.setText(R.id.tv_jdprice_jp,"￥"+item.getAuthPrice());
                    helper.setText(R.id.tv_qprice_jp,"￥"+item.getPrice());
                    helper.setText(R.id.tv_newprice_jp,"￥"+item.getMaxPirce());
                    helper.setText(R.id.tv_myprice_jp,"￥"+item.getMyPrice());
                    //倒计时
                    TextView tv_countTime_jp = (TextView) helper.getView(R.id.tv_countTime_jp);
                    CountDownTimer countDownTimer = countDownCounters.get(tv_countTime_jp.hashCode());
                    //将前一个缓存清除
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }

                    if (item.getTime() >= 0) {
                        helper.setText(R.id.tv_status_jp,"竞拍中");
                        countDownTimer = new CountDownTimer(item.getTime() * 1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                int day = 0, hour = 0, min = 0, sec = 0, sec11 = (int) item.getTime();
                                if (item.getTime() >= 86400) {
                                    day = sec11 / 86400;
                                    sec11 = sec11 % 86400;
                                }
                                if (item.getTime() >= 3600) {
                                    hour = sec11 / 3600;
                                    sec11 = sec11 % 3600;
                                }
                                if (item.getTime() >= 60) {
                                    min = sec11 / 60;
                                    sec11 = sec11 % 60;
                                }
                                sec = sec11;

                                //(day > 9 ? day : ("0" + day)) + ":" +
                                String certTime = (hour > 9 ? hour : ("0" + hour)) + ":" + (min > 9 ? min : ("0" + min)) + ":" + (sec > 9 ? sec : ("0" + sec));
                                helper.setText(R.id.tv_countTime_jp, certTime + "后结束");

                                item.setTime(item.getTime() - 1);

                            }

                            public void onFinish() {
                                helper.setText(R.id.tv_countTime_jp, "已结束");
                            }
                        }.start();
                        countDownCounters.put(tv_countTime_jp.hashCode(), countDownTimer);
                    }else{
                        helper.setText(R.id.tv_countTime_jp, "已结束");
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
            jdStoreGoodsAuc();
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
        jdStoreGoodsAuc();
    }

    private void jdStoreGoodsAuc()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/jdStoreGoodsAucList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<JdStoreGoodsAucInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<JdStoreGoodsAucInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<JdStoreGoodsAucInfo>>> response) {
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
                    public void onError(Response<DataResult<ArrayList<JdStoreGoodsAucInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

}

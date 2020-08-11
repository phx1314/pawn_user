package com.glavesoft.pawnuser.activity.shoppingmall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.glavesoft.F;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.frg.FrgProductDetail;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.StoreGoodsInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.utility.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2017/11/22
 * @company:常州宝丰
 */
public class StoreGoodsListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<StoreGoodsInfo> list = new ArrayList<>();
    CommonAdapter commAdapter;
    private int page = 1;
    private int listsize = 0;
    private String type = "";//1奢侈品珠宝，2手表，3钻石，4贵金属，5翡翠玉石，6和田玉，7其他
    private String state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_goods);
        type = getIntent().getStringExtra("type");
        state = getIntent().getStringExtra("state");
        initView();
    }

    private void initView() {
        setTitleBack();
        if (type.equals("1")) {
            setTitleName("钟表");
        } else if (type.equals("2")) {
            setTitleName("翡翠");
        } else if (type.equals("3")) {
            setTitleName("和田玉");
        } else if (type.equals("4")) {
            setTitleName("古董艺术品");
        } else if (type.equals("5")) {
            setTitleName("书画");
        } else if (type.equals("6")) {
            setTitleName("彩色珠宝");
        } else if (type.equals("7")) {
            setTitleName("钻石");
        } else if (type.equals("8")) {
            setTitleName("更多");
        } else if (type.equals("9")) {
            setTitleName("明清砚台");
        } else if (type.equals("10")) {
            setTitleName("文玩");
        } else if (type.equals("11")) {
            setTitleName("杂项");
        } else if (type.equals("12")) {
            setTitleName("红蓝宝石");
        } else if (type.equals("13")) {
            setTitleName("祖母绿");
        } else if (type.equals("14")) {
            setTitleName("珍珠");
        } else if (type.equals("15")) {
            setTitleName("碧玺");
        }

        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview = (ListView) findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (state.equals("rz")) {
//                    Intent intent = new Intent(StoreGoodsListActivity.this, GoodsDetailActivity.class);
//                    intent.putExtra("id",list.get(position).getId());
//                    intent.putExtra("type","rz");
//                    startActivity(intent);
                    F.INSTANCE.go2GoodeDetail(StoreGoodsListActivity.this, list.get(position).getId(), "rz");
                } else {
                    if (list.get(position).getType().equals("0")) {
//                        Intent intent = new Intent(StoreGoodsListActivity.this, GoodsDetailActivity.class);
//                        intent.putExtra("id",list.get(position).getId());
//                        intent.putExtra("type","jd");
//                        startActivity(intent);
                        F.INSTANCE.go2GoodeDetail(StoreGoodsListActivity.this, list.get(position).getId(), "jd");
                    } else {
                        Intent intent = new Intent(StoreGoodsListActivity.this, JdGoodsDetailActivity.class);
                        intent.putExtra("id", list.get(position).getId());
                        startActivity(intent);
                    }
                }
            }
        });

        storeGoodsList();
    }

    private void showList(ArrayList<StoreGoodsInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<StoreGoodsInfo>(StoreGoodsListActivity.this, result,
                    R.layout.item_storegoodslist) {
                @Override
                public void convert(final ViewHolder helper, final StoreGoodsInfo item) {

                    ImageView iv_pic_storegoodslist = (ImageView) helper.getView(R.id.iv_pic_storegoodslist);

                    if (!item.getImg().equals("")) {
                        getImageLoader().displayImage(BaseConstant.Image_URL + item.getImg(), iv_pic_storegoodslist, getImageLoaderOptions());
                    } else {
                        getImageLoader().displayImage("", iv_pic_storegoodslist, getImageLoaderOptions());
                    }

                    helper.setText(R.id.tv_jdprice_storegoodslist, "￥" + item.getAuthPrice());
                    helper.setText(R.id.tv_name_storegoodslist, item.getTitle());
                    if (state.equals("rz")) {
                        helper.getView(R.id.ll_jdprice_storegoodslist).setVisibility(View.GONE);
                    } else {
                        helper.getView(R.id.ll_jdprice_storegoodslist).setVisibility(View.VISIBLE);
                    }


                    helper.setText(R.id.tv_price_storegoodslist, "￥" + item.getPrice());
                    helper.setText(R.id.tv_time_storegoodslist, "");
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
        if (listsize == 10) {
            page++;
            storeGoodsList();
        } else {
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    private void resetPageData() {
        page = 1;
        list.clear();
        commAdapter = null;
        lv_listview.setAdapter(null);
        storeGoodsList();
    }

    private void storeGoodsList() {
        getlDialog().show();
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url;
        if (state.equals("rz")) {
            url = BaseConstant.getApiPostUrl("storeGoods/storeGoodsList");
        } else {
            url = BaseConstant.getApiPostUrl("storeGoods/storeJDGoodsLists");
        }
        HttpParams param = new HttpParams();
        param.put("token", token);
        param.put("type", type);
        param.put("page", page + "");
        param.put("limit", "10");
        OkGo.<DataResult<ArrayList<StoreGoodsInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<StoreGoodsInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<StoreGoodsInfo>>> response) {
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                listsize = response.body().getData().size();
                                showList(response.body().getData());
                            } else {

                            }
                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<StoreGoodsInfo>>> response) {
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}

package com.glavesoft.pawnuser.activity.shoppingmall;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;

import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.StoreBannerInfo;
import com.glavesoft.pawnuser.mod.StoreGoodsInfo;
import com.glavesoft.view.CustomToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2017/10/23
 * @company:常州宝丰
 */
public class ShoppingMallActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private BGARefreshLayout mRefreshLayout;
    private RecyclerView mDataRv;
    private ArrayList<StoreGoodsInfo> list = new ArrayList<>();
    //private View headerview;
    //private SlideShowView ssv_header_shoppingmall_pic;
    private StaggeredGridAdapter mStaggeredGridAdapter;
    private List<String> adlist = new ArrayList<>();

    private ArrayList<StoreBannerInfo> StoreBannerList = new ArrayList<>();
    private int page = 1;
    private int listsize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingmall);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("新品推荐");
        setTitleNameEn(R.mipmap.new_arrivals);

        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_recyclerview_refresh);
        mRefreshLayout.setDelegate(this);

//        mRefreshLayout.setCustomHeaderView(headerview, true);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        mDataRv = getViewById(R.id.rv_recyclerview_data);

//        mAdapter = new StaggeredRecyclerViewAdapter(mDataRv);
//        mAdapter.setOnRVItemClickListener(this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(layoutManager);
        //mDataRv.setAdapter(mAdapter);
        mStaggeredGridAdapter = new StaggeredGridAdapter(ShoppingMallActivity.this, list);
        mDataRv.setAdapter(mStaggeredGridAdapter);
//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(ShoppingMallActivity.this, RecyclerView.VERTICAL);
//        mDividerItemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#ECECEC")));
//        DividerItemDecoration mDividerItemDecoration2 = new DividerItemDecoration(ShoppingMallActivity.this, RecyclerView.HORIZONTAL);
//        mDividerItemDecoration2.setDrawable(new ColorDrawable(Color.parseColor("#ECECEC")));
//        mDataRv.addItemDecoration(mDividerItemDecoration);
//        mDataRv.addItemDecoration(mDividerItemDecoration2);
        storeBanner();
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (listsize == 10) {
            page++;
            storeHotGoodsList();
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
//        mStaggeredGridAdapter = null;
//        mDataRv.setAdapter(null);
        storeHotGoodsList();
    }

    private void storeBanner() {
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url = BaseConstant.getApiPostUrl("storeGoods/storeBanner");
        getlDialog().show();
        OkGo.<DataResult<ArrayList<StoreBannerInfo>>>post(url)
                .params("token", "")
                .execute(new JsonCallback<DataResult<ArrayList<StoreBannerInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<StoreBannerInfo>>> response) {
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {

                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                StoreBannerList = response.body().getData();
                                //ssv_header_shoppingmall_pic.setVisibility(View.VISIBLE);
                            } else {
                                //ssv_header_shoppingmall_pic.setVisibility(View.GONE);
                            }

                            View header = LayoutInflater.from(ShoppingMallActivity.this).inflate(R.layout.header_shoppingmall, mDataRv, false);
                            mStaggeredGridAdapter.setHeaderView(header, StoreBannerList);
                            storeHotGoodsList();

                        } else if (DataResult.RESULT_102 == response.body().getErrorCode()) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<StoreBannerInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }


    private void storeHotGoodsList() {
        getlDialog().show();
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url = BaseConstant.getApiPostUrl("storeGoods/storeHotGoodsList");
        HttpParams param = new HttpParams();
        param.put("token", token);
        param.put("page", page + "");
        param.put("limit", "10");
        OkGo.<DataResult<ArrayList<StoreGoodsInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<StoreGoodsInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<StoreGoodsInfo>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {

                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                listsize = response.body().getData().size();
                                if (list.size() == 0) {
                                    list = response.body().getData();
                                } else {
                                    for (int i = 0; i < response.body().getData().size(); i++) {
                                        list.add(response.body().getData().get(i));
                                    }
                                }
                                mStaggeredGridAdapter.refreshDatas(list);

                            } else {
                                if (list.size() > 0) {
                                    CustomToast.show("无更多数据");
                                } else {
                                    mStaggeredGridAdapter.refreshDatas(list);
                                }
                            }


                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<StoreGoodsInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

}

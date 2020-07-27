package com.glavesoft.pawnuser.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.android.volley.VolleyError;
import com.glavesoft.F;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.frg.FrgProductDetail;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.GoodsByOrgInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.utility.Helper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Sinyu on 2018/8/1.
 */

public class StoreGoodsFragment extends BaseFragment implements BGARefreshLayout
        .BGARefreshLayoutDelegate{
    public  String FragType="0";
    private GridView gv;
    public static String orgId;
    private BGARefreshLayout mRefreshLayout;
    private List<GoodsByOrgInfo> goodsByOrgInfos;
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;

    private LinearLayout ll_nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_store_goods, container, false);

        ll_nodata = (LinearLayout) view.findViewById(R.id.ll_nodata);
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_recyclerview_refresh);
        mRefreshLayout.setDelegate(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new
                BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        gv = (GridView) view.findViewById(R.id.grid_all);
        gv.setOnItemClickListener((adapterView, view1, i, l) -> {
//                Intent intent=new Intent(StoreGoodsFragment.this.getContext(), GoodsDetailActivity.class);
//                intent.putExtra("id",goodsByOrgInfos.get(i).getId());
//                intent.putExtra("type","rz");
//                startActivity(intent);
            F.INSTANCE.go2GoodeDetail(getActivity(),    goodsByOrgInfos.get(i).getId(), "rz");
        });
        storeAllGoods();
        return view;
    }
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            storeAllGoods();
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
        goodsByOrgInfos.clear();
//        mStaggeredGridAdapter = null;
//        mDataRv.setAdapter(null);
        storeAllGoods();
    }

    private void storeAllGoods() {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("pawnOrg/getGoodsByOrg");
        HttpParams param=new HttpParams();
        param.put("orgId", orgId);
        param.put("page", page+"");
        param.put("limit", "10");
        param.put("type", FragType);
        OkGo.<DataResult<List<GoodsByOrgInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<List<GoodsByOrgInfo>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<List<GoodsByOrgInfo>>> response) {
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null&&response.body().getData().size()>0) {
                                ll_nodata.setVisibility(View.GONE);
                                mRefreshLayout.setVisibility(View.VISIBLE);
                                listsize=response.body().getData().size();
                                showList(response.body().getData());
                            }else{
                                if ( goodsByOrgInfos==null||goodsByOrgInfos.size()==0){
                                    ll_nodata.setVisibility(View.VISIBLE);
                                    mRefreshLayout.setVisibility(View.GONE);
                                }
                            }

                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<List<GoodsByOrgInfo>>> response) {
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showList(List<GoodsByOrgInfo> result) {

        if (commAdapter == null) {
            goodsByOrgInfos = result;
            commAdapter = new CommonAdapter<GoodsByOrgInfo>(StoreGoodsFragment.this.getContext(), result,
                    R.layout.item_store_main_goods) {
                @Override
                public void convert(final ViewHolder helper, final GoodsByOrgInfo item) {
                    ImageView iv_item_staggered_icon=(ImageView) helper.getView(R.id.iv_item_staggered_icon);

                    int width= ScreenUtils.getInstance().getWidth();
                    width=width-ScreenUtils.dp2px(mContext,24);
                    width=width/2;
                    ViewGroup.LayoutParams lp = iv_item_staggered_icon.getLayoutParams();
                    lp.width=width;
                    lp.height = width*100/168;
                    iv_item_staggered_icon.setLayoutParams(lp);

                    if(!item.getImg().equals("")){
                        getImageLoader().displayImage(BaseConstant.Image_URL + item.getImg(),iv_item_staggered_icon,getImageLoaderOptions());
                    }else{
                        getImageLoader().displayImage("",iv_item_staggered_icon,getImageLoaderOptions());
                    }
                    helper.setText(R.id.tv_price_staggered,"￥"+item.getPrice());
                    helper.setText(R.id.tv_item_staggered_desc,item.getName());
                }
            };
            gv.setAdapter(commAdapter);
        } else {
            if (goodsByOrgInfos == null || goodsByOrgInfos.size() == 0) {
                goodsByOrgInfos = result;
            } else {
                for (int i = 0; i < result.size(); i++) {
                    goodsByOrgInfos.add(result.get(i));
                }
            }
            commAdapter.onDateChange(goodsByOrgInfos);
        }
        int i= gv.getVisibility();

    }
}

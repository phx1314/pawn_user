package com.glavesoft.pawnuser.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
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
import com.glavesoft.pawnuser.mod.MostThreeGoodsInfo;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.utility.Helper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Sinyu on 2018/8/1.
 */

public class StoreMainFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.iv_icon_rank1)
    ImageView ivIconRank1;
    @BindView(R.id.tv_name_rank1)
    TextView tvNameRank1;
    @BindView(R.id.tv_memo_rank1)
    TextView tvMemoRank1;
    @BindView(R.id.iv_icon_rank2)
    ImageView ivIconRank2;
    @BindView(R.id.tv_name_rank2)
    TextView tvNameRank2;
    @BindView(R.id.tv_memo_rank2)
    TextView tvMemoRank2;
    @BindView(R.id.iv_icon_rank3)
    ImageView ivIconRank3;
    @BindView(R.id.tv_name_rank3)
    TextView tvNameRank3;
    @BindView(R.id.tv_memo_rank3)
    TextView tvMemoRank3;
    Unbinder unbinder;
    @BindView(R.id.ll_rank1)
    LinearLayout llRank1;
    @BindView(R.id.ll_rank2)
    LinearLayout llRank2;
    @BindView(R.id.ll_rank3)
    LinearLayout llRank3;
    private ScrollView mScrollView;
    private GridView lv;
    private List<String> mDatas = new ArrayList<String>();
    public static String orgId;
    private List<GoodsByOrgInfo> goodsByOrgInfos;
    CommonAdapter commAdapter;
    private int page = 1;
    private List<MostThreeGoodsInfo> mostThreeGoodsInfos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_main, container, false);
        //initView(view);
        goodsByOrgInfos = new ArrayList<>();
        mostThreeGoodsInfos=new ArrayList<>();
        lv = (GridView) view.findViewById(R.id.grid_like);
        storeGoodsRank();
        storeAllGoods();
        unbinder = ButterKnife.bind(this, view);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(StoreMainFragment.this.getContext(),
//                        GoodsDetailActivity.class);
//                intent.putExtra("id", goodsByOrgInfos.get(i).getId());
//                intent.putExtra("type", "rz");
//                startActivity(intent);
                Helper.startActivity(
                        getActivity(),
                        FrgProductDetail.class,
                        TitleAct.class,
                        "id",
                        goodsByOrgInfos.get(i).getId(), "type", "rz"
                );
            }
        });
        llRank1.setOnClickListener(this);
        llRank2.setOnClickListener(this);
        llRank3.setOnClickListener(this);
        return view;
    }

    private void storeGoodsRank() {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("pawnOrg/getMostThreeGoods");
        HttpParams param=new HttpParams();
        param.put("orgId", orgId);
        OkGo.<DataResult<List<MostThreeGoodsInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<List<MostThreeGoodsInfo>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<List<MostThreeGoodsInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null) {
                                mostThreeGoodsInfos=response.body().getData();
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    MostThreeGoodsInfo mostThreeGoodsInfo = response.body().getData()
                                            .get(i);
                                    switch (i) {
                                        case 0:
                                            if (!mostThreeGoodsInfo.getImg().equals("")) {
                                                DisplayImageOptions imageOptions =
                                                        DisplayImageOptions.createSimple();
                                                ImageLoader.getInstance().displayImage
                                                        (BaseConstant.Image_URL +
                                                                        mostThreeGoodsInfo.getImg(),
                                                                ivIconRank1, imageOptions);
                                            }
                                            tvNameRank1.setText(mostThreeGoodsInfo.getName());
                                            break;
                                        case 1:
                                            if (!mostThreeGoodsInfo.getImg().equals("")) {
                                                DisplayImageOptions imageOptions =
                                                        DisplayImageOptions.createSimple();
                                                ImageLoader.getInstance().displayImage
                                                        (BaseConstant.Image_URL +
                                                                        mostThreeGoodsInfo.getImg(),
                                                                ivIconRank2, imageOptions);
                                            }
                                            tvNameRank2.setText(mostThreeGoodsInfo.getName());
                                            break;
                                        case 2:
                                            if (!mostThreeGoodsInfo.getImg().equals("")) {
                                                DisplayImageOptions imageOptions =
                                                        DisplayImageOptions.createSimple();
                                                ImageLoader.getInstance().displayImage
                                                        (BaseConstant.Image_URL +
                                                                        mostThreeGoodsInfo.getImg(),
                                                                ivIconRank3, imageOptions);
                                            }
                                            tvNameRank3.setText(mostThreeGoodsInfo.getName());
                                            break;
                                    }
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
                    public void onError(com.lzy.okgo.model.Response<DataResult<List<MostThreeGoodsInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void storeAllGoods() {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("pawnOrg/getGoodsByOrg");
        HttpParams param=new HttpParams();
        param.put("orgId", orgId);
        param.put("page", page + "");
        param.put("limit", "10");
        param.put("type", "0");
        OkGo.<DataResult<ArrayList<GoodsByOrgInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<GoodsByOrgInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<GoodsByOrgInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null) {
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
                    public void onError(Response<DataResult<ArrayList<GoodsByOrgInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showList(List<GoodsByOrgInfo> result) {

        if (commAdapter == null) {
            goodsByOrgInfos = result;
            commAdapter = new CommonAdapter<GoodsByOrgInfo>(StoreMainFragment.this.getContext(),
                    result,
                    R.layout.item_store_main_goods) {
                @Override
                public void convert(final ViewHolder helper, final GoodsByOrgInfo item) {

                    ImageView iv_item_staggered_icon = (ImageView) helper.getView(R.id
                            .iv_item_staggered_icon);
                    int width= ScreenUtils.getInstance().getWidth();
                    width=width-ScreenUtils.dp2px(mContext,24);
                    width=width/2;
                    ViewGroup.LayoutParams lp = iv_item_staggered_icon.getLayoutParams();
                    lp.width=width;
                    lp.height = width*100/168;
                    iv_item_staggered_icon.setLayoutParams(lp);

                    if (!item.getImg().equals("")) {
                        getImageLoader().displayImage(BaseConstant.Image_URL + item.getImg(),
                                iv_item_staggered_icon, getImageLoaderOptions());
                    } else {
                        getImageLoader().displayImage("", iv_item_staggered_icon,
                                getImageLoaderOptions());
                    }
                    helper.setText(R.id.tv_price_staggered, "￥" + item.getPrice());
                    helper.setText(R.id.tv_item_staggered_desc, item.getName());

                    if (item.getSource()!=null){
                        if (item.getSource().equals("3")){
                            helper.setText(R.id.tv_type, "自营");
                        }else if (item.getSource().equals("4")){
                            helper.setText(R.id.tv_type, "臻品");
                        }else if (item.getSource().equals("2")){
                            helper.setText(R.id.tv_type, "绝当品");
                        }
                    }

                }
            };

            lv.setAdapter(commAdapter);
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
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ArrayAdapter listAdapter = (ArrayAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        listView.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_rank1:
                if (mostThreeGoodsInfos.size()>1){
//                    Intent intent = new Intent(StoreMainFragment.this.getContext(),
//                            GoodsDetailActivity.class);
//                    intent.putExtra("id", mostThreeGoodsInfos.get(0).getId()+"");
//                    intent.putExtra("type", "rz");
//                    startActivity(intent);
                    Helper.startActivity(
                            getActivity(),
                            FrgProductDetail.class,
                            TitleAct.class,
                            "id",
                            mostThreeGoodsInfos.get(0).getId()+"", "type", "rz"
                    );
                }

                break;
            case R.id.ll_rank2:
                if (mostThreeGoodsInfos.size()>2){
//                    Intent intent = new Intent(StoreMainFragment.this.getContext(),
//                            GoodsDetailActivity.class);
//                    intent.putExtra("id", mostThreeGoodsInfos.get(1).getId()+"");
//                    intent.putExtra("type", "rz");
//                    startActivity(intent);
                    Helper.startActivity(
                            getActivity(),
                            FrgProductDetail.class,
                            TitleAct.class,
                            "id",
                            mostThreeGoodsInfos.get(1).getId()+"", "type", "rz"
                    );
                }

                break;
            case R.id.ll_rank3:
                if (mostThreeGoodsInfos.size()==3){
//                    Intent intent = new Intent(StoreMainFragment.this.getContext(),
//                            GoodsDetailActivity.class);
//                    intent.putExtra("id", mostThreeGoodsInfos.get(2).getId()+"");
//                    intent.putExtra("type", "rz");
//                    startActivity(intent);
                    Helper.startActivity(
                            getActivity(),
                            FrgProductDetail.class,
                            TitleAct.class,
                            "id",
                            mostThreeGoodsInfos.get(2).getId()+"", "type", "rz"
                    );
                }

                break;
        }
    }
}

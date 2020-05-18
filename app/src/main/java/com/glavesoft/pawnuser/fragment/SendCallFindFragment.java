package com.glavesoft.pawnuser.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.appraisal.SendCallGoodDetailActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.SendCallGoodInfo;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.CustomToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Sinyu on 2018/12/7.
 */

public class SendCallFindFragment extends BaseFragment implements BGARefreshLayout
        .BGARefreshLayoutDelegate{

    private RelativeLayout titlebar_types;
    private GridView lv_types;
    private ArrayList<SendCallGoodInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private BGARefreshLayout mRefreshLayout;
    private int page=1;
    private int listsize=0;

    private LinearLayout ll_nodata;
    private TextView tv_nodata;
    public SendCallFindFragment() {
    }

    public static SendCallFindFragment newInstance(int index)
    {
        SendCallFindFragment fragment = new SendCallFindFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_send_find, container, false);
        initView(view);
        pawnCateList("");
        return view;
    }

    private void initView(View view) {
//
//        titlebar_types=(RelativeLayout) view.findViewById(R.id.titlebar_types);
//        titlebar_types.setVisibility(View.GONE);
        ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
        tv_nodata=(TextView) view.findViewById(R.id.tv_nodata);
        lv_types=(GridView)view.findViewById(R.id.lv_types);
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_gridview_refresh);
        mRefreshLayout.setDelegate(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new
                BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), SendCallGoodDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (listsize == 10) {
            page++;
            pawnCateList("");
        } else {
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
        lv_types.setAdapter(null);
        pawnCateList("");
    }
    private void showList(ArrayList<SendCallGoodInfo> result) {
        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<SendCallGoodInfo>(getActivity(), result,
                    R.layout.item_sendcall_find) {
                @Override
                public void convert(final ViewHolder helper, final SendCallGoodInfo item) {
                    helper.setText(R.id.tv_item_goods_title,item.getName());
                    helper.setText(R.id.tv_name,item.getNickName());
                    helper.setText(R.id.tv_official_price,"￥"+item.getAuthPrice());
                    helper.setText(R.id.tv_price,"￥"+item.getSellPrice());
                    ImageView iv_good=(ImageView) helper.getView(R.id.iv_item_goods_icon);

                    int width= ScreenUtils.getInstance().getWidth();
                    width=width-ScreenUtils.dp2px(mContext,24);
                    width=width/2;
                    ViewGroup.LayoutParams lp = iv_good.getLayoutParams();
                    lp.width=width;
                    lp.height = width*100/168;
                    iv_good.setLayoutParams(lp);

                    if(!item.getImages().equals("")){
                        List<String> list= Arrays.asList(item.getImages().split(","));
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

//                helper.setText(R.id.tv_name_types,item.getName());
//                helper.setText(R.id.tv_pp_types,item.getCateType());
//
//                ImageView iv_pic_types=(ImageView) helper.getView(R.id.iv_pic_types);
//                getImageLoader().displayImage(BaseConstant.Image_URL + item.getIcon(),iv_pic_types,getImageLoaderOptions());
//
//                helper.getView(R.id.iv_yj_types).setVisibility(View.VISIBLE);
//                helper.getView(R.id.iv_yj_types).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent();
//                        intent.setClass(getActivity(), MailAppraisalActivity.class);
//                        intent.putExtra("type","mail");
//                        intent.putExtra("pawnCateCode",item.getCode());
//                        startActivity(intent);
//                    }
//                });
                }
            };

            lv_types.setAdapter(commAdapter);
        }else {
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

    private void pawnCateList(String name)
    {
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/sellIndex");
        getlDialog().show();
        OkGo.<DataResult<ArrayList<SendCallGoodInfo>>>post(url)
                .params("token", token)
                .params("page", page+"")
                .params("limit", "10")
                .params("name", name)
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
                                ll_nodata.setVisibility(View.GONE);
                                mRefreshLayout.setVisibility(View.VISIBLE);
                                listsize=response.body().getData().size();
                                showList(response.body().getData());
                            }else{
                                if (list.size()==0){
                                    ll_nodata.setVisibility(View.VISIBLE);
                                    mRefreshLayout.setVisibility(View.GONE);
                                    tv_nodata.setText("暂无商品，请赶快上架吧~");
                                }else{
                                    CustomToast.show("无更多数据");
                                    mRefreshLayout.endLoadingMore();
                                }
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
}

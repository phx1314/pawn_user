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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.pawn.PawnRecordDetailActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.TradeRecordInfo;
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

/**
 * @author 严光
 * @date: 2018/9/28
 * @company:常州宝丰
 */
public class PawnRecordFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private int index;
    private BGARefreshLayout mRefreshLayout;
    private GridView grid_all;
    private ArrayList<TradeRecordInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;

    private LinearLayout ll_nodata;
    private TextView tv_nodata;
    public static PawnRecordFragment newInstance(int index)
    {
        PawnRecordFragment fragment = new PawnRecordFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            index = getArguments().getInt("index");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_gridview, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
        tv_nodata=(TextView) view.findViewById(R.id.tv_nodata);
        mRefreshLayout=(BGARefreshLayout) view.findViewById(R.id.rl_gridview_refresh);
        mRefreshLayout.setDelegate(this);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        grid_all=(GridView) view.findViewById(R.id.grid_all);

        grid_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PawnRecordDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("goodsId",list.get(position).getGoodsId());
                intent.putExtra("isRedeem",index+"");
                startActivity(intent);
            }
        });

        tradeRecordList();
    }

    private void showList(ArrayList<TradeRecordInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<TradeRecordInfo>(getActivity(), result,
                    R.layout.item_pawnrecord) {
                @Override
                public void convert(final ViewHolder helper, final TradeRecordInfo item) {

                    ImageView iv_pic_pawnrecord=(ImageView) helper.getView(R.id.iv_pic_pawnrecord);

                    int width= ScreenUtils.getInstance().getWidth();
                    width=width-ScreenUtils.dp2px(mContext,24);
                    width=width/2;
                    ViewGroup.LayoutParams lp = iv_pic_pawnrecord.getLayoutParams();
                    lp.width=width;
                    lp.height = width*100/168;
                    iv_pic_pawnrecord.setLayoutParams(lp);

                    if(!item.getImages().equals("")){
                        List<String> list= Arrays.asList(item.getImages().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0),iv_pic_pawnrecord,getImageLoaderOptions());
                    }else{
                        getImageLoader().displayImage("",iv_pic_pawnrecord,getImageLoaderOptions());
                    }

                    //1典当2续当3赎当4绝当5交易
//                    helper.getView(R.id.iv_pic_pawnrecord).setBackgroundResource(R.drawable.shape_red);
//                    if(item.getType().equals("1")){
//                        helper.setText(R.id.tv_state_pawnrecord,"典当中");
//                    }else if(item.getType().equals("2")){
//                        helper.setText(R.id.tv_state_pawnrecord,"续当");
//                    }else if(item.getType().equals("3")){
//                        helper.setText(R.id.tv_state_pawnrecord,"赎当");
//                    }else if(item.getType().equals("4")){
//                        helper.setText(R.id.tv_state_pawnrecord,"绝当");
//                        helper.getView(R.id.tv_state_pawnrecord).setBackgroundResource(R.drawable.shape_cancle);
//                    }else if(item.getType().equals("5")){
//                        helper.setText(R.id.tv_state_pawnrecord,"交易中");
//                    }

                    if (index==0){
                        if(item.getType().equals("4")){
                            helper.setText(R.id.tv_state_pawnrecord,"绝当");
                            helper.getView(R.id.tv_state_pawnrecord).setBackgroundResource(R.drawable.shape_red1);
                        }else if(item.getType().equals("3")){
                            helper.setText(R.id.tv_state_pawnrecord,"未典当");
                            helper.getView(R.id.tv_state_pawnrecord).setBackgroundResource(R.drawable.shape_orange);
                        }else{
                            helper.setText(R.id.tv_state_pawnrecord,"典当中");
                            helper.getView(R.id.tv_state_pawnrecord).setBackgroundResource(R.drawable.shape_green1);
                        }
                    }else{
                        helper.setText(R.id.tv_state_pawnrecord,"赎回");
                        helper.getView(R.id.tv_state_pawnrecord).setBackgroundResource(R.drawable.shape_green1);
                    }

                    helper.setText(R.id.tv_name_pawnrecord,item.getTitle());
                    helper.setText(R.id.tv_jdprice_pawnrecord,"￥"+item.getAuthPrice());
                    helper.setText(R.id.tv_time_pawnrecord,item.getCreateTime());
                }
            };

            grid_all.setAdapter(commAdapter);
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
            tradeRecordList();
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
        grid_all.setAdapter(null);
        tradeRecordList();
    }

    private void tradeRecordList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/tradeRecordList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("state",index+"");
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<TradeRecordInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<TradeRecordInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<TradeRecordInfo>>> response) {
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
                                    tv_nodata.setText("暂无业务记录，快去别的地方逛逛吧~");
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
                    public void onError(Response<DataResult<ArrayList<TradeRecordInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

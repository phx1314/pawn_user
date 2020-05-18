package com.glavesoft.pawnuser.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.pawn.PawnDetailActivity;
import com.glavesoft.pawnuser.activity.personal.LogisticsdetailActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseApplication;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.PawnListInfo;
import com.glavesoft.pawnuser.mod.PostInfo;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.FlexibleRoundedBitmapDisplayer;
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
 * @date: 2017/11/10
 * @company:常州宝丰
 */
public class LogisticsFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private int index;
    private RelativeLayout titlebar;
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<PostInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;
    private LinearLayout ll_nodata;
    private TextView tv_nodata;
    public static LogisticsFragment newInstance(int index)
    {
        LogisticsFragment fragment = new LogisticsFragment();
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
        View view = inflater.inflate(R.layout.activity_listview, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        titlebar=(RelativeLayout)view.findViewById(R.id.titlebar);
        titlebar.setVisibility(View.GONE);
        ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
        tv_nodata=(TextView) view.findViewById(R.id.tv_nodata);
        mRefreshLayout=(BGARefreshLayout)view.findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)view.findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(index==0){
                    if(!list.get(position).getPostState().equals("0")){
                        Intent intent = new Intent(getActivity(), LogisticsdetailActivity.class);
                        intent.putExtra("expressId",list.get(position).getId());
                        startActivity(intent);
                    }else{
                        CustomToast.show("暂无物流信息");
                    }
                }else{
                    if(!list.get(position).getBackState().equals("0")){
                        Intent intent = new Intent(getActivity(), LogisticsdetailActivity.class);
                        intent.putExtra("expressId",list.get(position).getId());
                        startActivity(intent);
                    }else{
                        CustomToast.show("暂无物流信息");
                    }
                }

            }
        });

        postList();
    }

    private void showList(ArrayList<PostInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<PostInfo>(getActivity(), result,
                    R.layout.item_logistics) {
                @Override
                public void convert(final ViewHolder helper, final PostInfo item) {
                    ImageView iv_pic_jb = (ImageView) helper.getView(R.id.iv_pic_logistics);

                    if(!item.getImages().equals("")){
                        List<String> list= Arrays.asList(item.getImages().split(","));
                        setRoundedImage(//圆角图
                                BaseConstant.Image_URL +  list.get(0),
                                ScreenUtils.dp2px(mContext,5),
                                FlexibleRoundedBitmapDisplayer.CORNER_ALL,
                                R.drawable.shape_coner_white2,
                                iv_pic_jb
                        );
                    }else{
                        iv_pic_jb.setImageDrawable(null);
                    }


                    helper.setText(R.id.tv_name_logistics, item.getName());
                    helper.setText(R.id.tv_code_logistics, "顺丰快递单号：" + item.getPostExpressCode());

                    if(index==0){
//                        if(item.getPostState().equals("1")){//0：在途1：揽件2：疑难3：签收4：退签或异常签收5：派件6：退回
//                            helper.setText(R.id.tv_state_logistics, "物流状态：未邮寄");
//                            helper.setTextcolor(R.id.tv_state_logistics, getResources().getColor(R.color.red_k));
//                        }else if(item.getPostState().equals("3")){
//                            helper.setText(R.id.tv_state_logistics, "物流状态：已到达");
//                            helper.setTextcolor(R.id.tv_state_logistics, getResources().getColor(R.color.yellow1));
//                        }else {
//                            helper.setText(R.id.tv_state_logistics, "物流状态：运输中");
//                            helper.setTextcolor(R.id.tv_state_logistics, getResources().getColor(R.color.red_k));
//                        }
                        if (item.getPostState()!=null&&!item.getPostState().equals("")){
                            if(item.getPostState().equals("0")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：暂未寄出");
                            }else if(item.getPostState().equals("1")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：揽件");
                            }else if(item.getPostState().equals("2")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：疑难处理");
                            }else if(item.getPostState().equals("3")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：签收");
                            }else if(item.getPostState().equals("4")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：退签或异常签收");
                            }else if(item.getPostState().equals("5")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：派件");
                            }else if(item.getPostState().equals("6")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：退回");
                            }
                        }else{
                            helper.setText(R.id.tv_state_logistics,"物流状态：暂未寄出");
                        }
                    }else{
//                        if(item.getBackState().equals("1")){//0：在途1：揽件2：疑难3：签收4：退签或异常签收5：派件6：退回
//                            helper.setText(R.id.tv_state_logistics, "物流状态：申请取回");
//                            helper.setTextcolor(R.id.tv_state_logistics, getResources().getColor(R.color.red_k));
//                        }else if(item.getBackState().equals("3")){
//                            helper.setText(R.id.tv_state_logistics, "物流状态：已到达");
//                            helper.setTextcolor(R.id.tv_state_logistics, getResources().getColor(R.color.yellow1));
//                        }else{
//                            helper.setText(R.id.tv_state_logistics, "物流状态：运输中");
//                            helper.setTextcolor(R.id.tv_state_logistics, getResources().getColor(R.color.red_k));
//                        }

                        if (item.getBackState()!=null&&!item.getBackState().equals("")){
                            if(item.getBackState().equals("0")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：暂未寄出");
                            }else if(item.getBackState().equals("1")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：揽件");
                            }else if(item.getBackState().equals("2")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：疑难处理");
                            }else if(item.getBackState().equals("3")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：签收");
                            }else if(item.getBackState().equals("4")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：退签或异常签收");
                            }else if(item.getBackState().equals("5")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：派件");
                            }else if(item.getBackState().equals("6")){
                                helper.setText(R.id.tv_state_logistics,"物流状态：退回");
                            }
                        }else{
                            helper.setText(R.id.tv_state_logistics,"物流状态：暂未寄出");
                        }
                    }


                }
            };

            lv_listview.setAdapter(commAdapter);
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

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            postList();
        }else{
            //CustomToast.show("无更多数据");
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
        postList();
    }

    private void postList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/postList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",page+"");
        param.put("limit","10");
        if(index==0){ //1寄往平台 2平台取回
            param.put("type","1");
        }else{
            param.put("type","2");
        }
        OkGo.<DataResult<ArrayList<PostInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<PostInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<PostInfo>>> response) {
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
                                    tv_nodata.setText("暂无物流信息，快去别的地方逛逛哦~");
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
                    public void onError(Response<DataResult<ArrayList<PostInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

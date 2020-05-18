package com.glavesoft.pawnuser.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.glavesoft.pawnuser.activity.appraisal.AddSendCallActivity;
import com.glavesoft.pawnuser.activity.appraisal.EvaluationActivity;
import com.glavesoft.pawnuser.activity.appraisal.SendCallOrderinfoActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.MySendCallInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Sinyu on 2018/12/7.
 */

public class MySendCallFragment extends BaseFragment implements BGARefreshLayout
        .BGARefreshLayoutDelegate {
    private RelativeLayout titlebar_refresh;
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<MySendCallInfo> list = new ArrayList<>();
    CommonAdapter commAdapter;
    private int page = 1;
    private int listsize = 0;
    private LinearLayout ll_nodata;
    private TextView tv_nodata;
    private TextView tv_submit;
    public static MySendCallFragment newInstance(int index) {
        MySendCallFragment fragment = new MySendCallFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_listview_mysend, container, false);
        setBoardCast();
        initView(view);
        return view;
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("MySendCallRefresh");
        getActivity().registerReceiver(mListenerID, f);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            resetPageData();
        }
    };

    public  void onDestroy(){
        super.onDestroy();
        getActivity().unregisterReceiver(mListenerID);
    }

    private void initView(View view) {
        titlebar_refresh = (RelativeLayout) view.findViewById(R.id.titlebar_refresh);
        titlebar_refresh.setVisibility(View.GONE);
        ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
        tv_nodata=(TextView) view.findViewById(R.id.tv_nodata);
        tv_submit=(TextView) view.findViewById(R.id.tv_submit);
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new
                BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview = (ListView) view.findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), EvaluationActivity.class);
                intent.putExtra("pos",0);
                startActivity(intent);
            }
        });

        MyAppraisalList();
    }

    private void showList(ArrayList<MySendCallInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<MySendCallInfo>(getActivity(), result,
                    R.layout.item_my_send_call) {
                @Override
                public void convert(final ViewHolder helper, final MySendCallInfo item) {
                    //todo
                    if (item.getSellStatus() .equals("0")) {
                        helper.setText(R.id.tv_state, "状态：未上架");
                        helper.setText(R.id.tv_ys_state, "未售");
                    } else if (item.getSellStatus() .equals("1")) {
                        helper.setText(R.id.tv_state, "状态：已上架");
                        helper.setText(R.id.tv_ys_state, "未售");
                    } else if (item.getSellStatus() .equals("2")) {
                        helper.setText(R.id.tv_state, "状态：已售");
                        helper.setText(R.id.tv_ys_state, "已售");
                        helper.getView(R.id.tv_ys_state).setOnClickListener(new View
                                .OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MySendCallFragment.this.getContext(),
                                        SendCallOrderinfoActivity.class);
                                intent.putExtra("id", item.getId());
                                startActivity(intent);
                            }
                        });
                    }

                    if (item.getSellCheck().equals("0")){
                        helper.setText(R.id.tv_ys_state, "已拒绝");
                        helper.getView(R.id.tv_sj).setVisibility(View.INVISIBLE);
                        helper.getView(R.id.tv_xj).setVisibility(View.INVISIBLE);
                        helper.getView(R.id.tv_bj).setVisibility(View.INVISIBLE);
                    }else if (item.getSellCheck().equals("2")){
                        helper.setText(R.id.tv_ys_state, "待审核");
                        helper.getView(R.id.tv_sj).setVisibility(View.INVISIBLE);
                        helper.getView(R.id.tv_xj).setVisibility(View.INVISIBLE);
                        helper.getView(R.id.tv_bj).setVisibility(View.INVISIBLE);
                    }else{
                        helper.getView(R.id.tv_sj).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_xj).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_bj).setVisibility(View.VISIBLE);
                    }

                    helper.setText(R.id.tv_sc_name, item.getName());
                    if (item.getSellStartTime() != null) {
                        helper.setText(R.id.tv_sc_sjdate, "上架日期：" + item.getSellStartTime().split
                                (" ")[0]);
                    } else {
                        helper.getView(R.id.ll_date).setVisibility(View.GONE);
                    }
                    if (item.getSellEndTime() != null) {
                        helper.setText(R.id.tv_sc_dqdate, "到期日期：" + item.getSellEndTime().split("" +
                                " ")[0]);
                    }
                    ImageView iv_good = (ImageView) helper.getView(R.id.iv_pic_jb);
                    if (!item.getImages().equals("")) {
                        List<String> list = Arrays.asList(item.getImages().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0),
                                iv_good, getImageLoaderOptions());
                    } else {
                        getImageLoader().displayImage("", iv_good, getImageLoaderOptions());
                    }


                    helper.getView(R.id.tv_sj).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateOnline(item.getId(), "1");
                        }
                    });
                    helper.getView(R.id.tv_xj).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateOnline(item.getId(), "0");
                        }
                    });

                    helper.getView(R.id.tv_bj).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!item.getSellStatus().equals("2")) {
                                Intent intent = new Intent(MySendCallFragment.this.getContext(),
                                        AddSendCallActivity.class);
                                intent.putExtra("id", item.getId());
                                intent.putExtra("type", "update");
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MySendCallFragment.this.getContext(),
                                        SendCallOrderinfoActivity.class);
                                intent.putExtra("id", item.getId());
                                startActivity(intent);
                            }
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
//            if(result.size()==1){
//                lv_order.setSelection(list.size()-1);
//            }
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
            MyAppraisalList();
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
        MyAppraisalList();
    }

    private void MyAppraisalList() {
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/mySell");
        getlDialog().show();
        OkGo.<DataResult<ArrayList<MySendCallInfo>>>post(url)
                .params("token", token)
                .params("page", page+"")
                .params("limit", "10")
                .execute(new JsonCallback<DataResult<ArrayList<MySendCallInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<MySendCallInfo>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                ll_nodata.setVisibility(View.GONE);
                                mRefreshLayout.setVisibility(View.VISIBLE);
                                listsize = response.body().getData().size();
                                showList(response.body().getData());
                            }else{
                                if (list.size()==0){
                                    ll_nodata.setVisibility(View.VISIBLE);
                                    mRefreshLayout.setVisibility(View.GONE);
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
                    public void onError(Response<DataResult<ArrayList<MySendCallInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

    private void updateOnline(String id, String sellStatus) {
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/updateOnline");
        getlDialog().show();
        OkGo.<DataResult<Integer>>post(url)
                .params("token", token)
                .params("id", id)
                .params("sellStatus", sellStatus)//0未上架，1上架
                .execute(new JsonCallback<DataResult<Integer>>() {
                    @Override
                    public void onSuccess(Response<DataResult<Integer>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){

                            if (response.body().getData() != null) {
                                CustomToast.show("成功");
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102)
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<Integer>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

}

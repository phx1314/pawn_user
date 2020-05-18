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
import com.glavesoft.pawnuser.activity.appraisal.MailAppraisalActivity;
import com.glavesoft.pawnuser.activity.main.WebActivity;
import com.glavesoft.pawnuser.activity.pawn.BackGoodsActivity;
import com.glavesoft.pawnuser.activity.pawn.CheckTicketActivity;
import com.glavesoft.pawnuser.activity.pawn.ContinuedActivity;
import com.glavesoft.pawnuser.activity.pawn.PawnDetailActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseApplication;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.PawnListInfo;
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
 * @date: 2017/11/6
 * @company:常州宝丰
 */
public class MyPawnFragment  extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private int index;
    private RelativeLayout titlebar_refresh;
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<PawnListInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;

    private LinearLayout ll_nodata;
    public static MyPawnFragment newInstance(int index)
    {
        MyPawnFragment fragment = new MyPawnFragment();
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
        View view = inflater.inflate(R.layout.activity_listview_goods, container, false);
        setBoardCast();
        initView(view);
        return view;
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("submitpawn");
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
        titlebar_refresh=(RelativeLayout)view.findViewById(R.id.titlebar_refresh);
        titlebar_refresh.setVisibility(View.GONE);
        ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
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
                    Intent intent = new Intent(getActivity(), PawnDetailActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    startActivity(intent);
                }
            }
        });

        myPawnList();
    }

    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownCounters= new SparseArray<>();
    private void showList(ArrayList<PawnListInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<PawnListInfo>(getActivity(), result,
                    R.layout.item_pawn) {
                @Override
                public void convert(final ViewHolder helper, final PawnListInfo item) {
                    ImageView iv_pic_jb=(ImageView) helper.getView(R.id.iv_pic_pawn);
                    if(!item.getImage().equals("")){
                        List<String> list= Arrays.asList(item.getImage().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0),iv_pic_jb,getImageLoaderOptions());
                    }else{
                        getImageLoader().displayImage("",iv_pic_jb,getImageLoaderOptions());
                    }

                    helper.setText(R.id.tv_name_pawn,item.getTitle());
                    helper.setText(R.id.tv_jdprice_pawn,"￥"+item.getAuthPrice());

                    helper.getView(R.id.tv_dkpz_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_ckdp_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_xd_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_sd_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_qdd_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_countTime_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_endTime_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_num_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_yffdj_pawn).setVisibility(View.GONE);
                    if(index==0){
                        helper.setTextcolor(R.id.tv_jdprice_pawn,getResources().getColor(R.color.red_k));
                        helper.getView(R.id.tv_num_pawn).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_countTime_pawn).setVisibility(View.VISIBLE);

                        helper.setText(R.id.tv_num_pawn,item.getCount()+"次出价");

                        TextView tv_countTime_pawn = (TextView) helper.getView(R.id.tv_countTime_pawn);
                        CountDownTimer countDownTimer = countDownCounters.get(tv_countTime_pawn.hashCode());
                        //将前一个缓存清除
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }

                        if (item.getTime() >= 0) {
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
                                    helper.setText(R.id.tv_countTime_pawn, certTime + "后结束");

                                    item.setTime(item.getTime() - 1);

                                }

                                public void onFinish() {
                                    helper.setText(R.id.tv_countTime_pawn, "已结束");
                                }
                            }.start();
                            countDownCounters.put(tv_countTime_pawn.hashCode(), countDownTimer);
                        }else{
                            helper.setText(R.id.tv_countTime_pawn, "已结束");
                        }
                    }else if(index==1){
                        helper.setTextcolor(R.id.tv_jdprice_pawn,getResources().getColor(R.color.text_gray));
                        helper.getView(R.id.tv_dkpz_pawn).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_ckdp_pawn).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_xd_pawn).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_sd_pawn).setVisibility(View.VISIBLE);

                        helper.getView(R.id.tv_endTime_pawn).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_yffdj_pawn).setVisibility(View.VISIBLE);

                        //0未逾期 1逾期
                        if (item.getType().equals("0")){
                            helper.setText(R.id.tv_endTime_pawn,"到期时间："+item.getTime()+"天");
                            helper.setTextcolor(R.id.tv_endTime_pawn,getResources().getColor(R.color.text_gray));
                        }else{
                            helper.setText(R.id.tv_endTime_pawn,"逾期天数："+item.getTime()+"天");
                            helper.setTextcolor(R.id.tv_endTime_pawn,getResources().getColor(R.color.red_k));
                        }

                        if(item.getIsVerify().equals("0")){
                            helper.setText(R.id.tv_yffdj_pawn,"已发放当金：￥0.00");
                            helper.getView(R.id.tv_dkpz_pawn).setVisibility(View.GONE);
                            helper.getView(R.id.tv_xd_pawn).setVisibility(View.GONE);
                            helper.getView(R.id.tv_sd_pawn).setVisibility(View.GONE);
                        }else{
                            helper.setText(R.id.tv_yffdj_pawn,"已发放当金：￥"+item.getMoney());
                        }

                        if(item.getContinueState().equals("1")){//续当状态 0没有续当操作 1续当处理中
                            helper.getView(R.id.tv_xd_pawn).setVisibility(View.GONE);
                            helper.getView(R.id.tv_sd_pawn).setVisibility(View.GONE);
                            helper.setText(R.id.tv_endTime_pawn,"续当处理中");

                        }

                        if(item.getRedeemState().equals("1")){//赎当状态 0没有赎当操作 1赎当处理中
                            helper.getView(R.id.tv_xd_pawn).setVisibility(View.GONE);
                            helper.getView(R.id.tv_sd_pawn).setVisibility(View.GONE);
                            helper.setText(R.id.tv_endTime_pawn,"赎当处理中");

                        }

                        helper.getView(R.id.tv_ckdp_pawn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//查看当票
                                //checkPawnTic(item.getId());
                                Intent intent = new Intent();
                                intent.setClass(getActivity(),WebActivity.class);
                                intent.putExtra("titleName","典当凭证");
                                intent.putExtra("url",BaseConstant.BaseURL+"/m/pawn/toPawnTicket/"+item.getId());
                                startActivity(intent);

                            }
                        });

                        helper.getView(R.id.tv_dkpz_pawn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//打款凭证
                                Intent intent=new Intent();
                                intent.setClass(getActivity(), CheckTicketActivity.class);
                                intent.putExtra("id",item.getId());
                                startActivity(intent);
                            }
                        });

                        helper.getView(R.id.tv_sd_pawn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//赎当
                                Intent intent=new Intent();
                                intent.setClass(getActivity(), BackGoodsActivity.class);
                                intent.putExtra("id",item.getId());
                                startActivity(intent);
                            }
                        });

                        helper.getView(R.id.tv_xd_pawn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//续当
                                Intent intent=new Intent();
                                intent.setClass(getActivity(), ContinuedActivity.class);
                                intent.putExtra("id",item.getId());
                                startActivity(intent);
                            }
                        });
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
        if(listsize==10){
            page++;
            myPawnList();
        }else{
            //if(list.size()>10){
                CustomToast.show("无更多数据");
            //}
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
        myPawnList();
    }

    private void myPawnList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url="";
        if(index==0){ //1竞拍中 2已典当
            url="userPawn/myPawnList";
        }else{
            url="userPawn/myPawnedList";
        }
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<PawnListInfo>>>post(BaseConstant.getApiPostUrl(url))
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<PawnListInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<PawnListInfo>>> response) {
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
                    public void onError(Response<DataResult<ArrayList<PawnListInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

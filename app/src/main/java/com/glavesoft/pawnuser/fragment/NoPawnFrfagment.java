package com.glavesoft.pawnuser.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.RetrieveActivity;
import com.glavesoft.pawnuser.activity.pawn.PawnActivity;
import com.glavesoft.pawnuser.activity.pawn.PawnDetailActivity;
import com.glavesoft.pawnuser.activity.pawn.PlatformActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.MyAppraisalInfo;
import com.glavesoft.pawnuser.mod.NoPawnInfo;
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
public class NoPawnFrfagment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private int index;
    private RelativeLayout titlebar_refresh;
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<NoPawnInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;
    private LinearLayout ll_nodata;
    public static NoPawnFrfagment newInstance(int index)
    {
        NoPawnFrfagment fragment = new NoPawnFrfagment();
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
        mRefreshLayout=(BGARefreshLayout)view.findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)view.findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        noPawnList();
    }

    private PopupWindow popupWindo;
    public void showPopupWindow(final NoPawnInfo info)
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pw_pawn, null);
        Button btn_ok = (Button)view.findViewById(R.id.btn_ok_pawn);
        WebView wv_pawn = (WebView)view.findViewById(R.id.wv_pawn);

        WebSettings webSettings = wv_pawn.getSettings();

        // 支持javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置可以支持缩放
//        webSettings.setSupportZoom(true);
//        // 设置出现缩放工具
//        webSettings.setBuiltInZoomControls(true);
        // 扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        // 自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式
        //webSettings.setUseWideViewPort(true);  //为图片添加放大缩小功能
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        getlDialog().show();

        // 加载数据
        wv_pawn.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                if (newProgress == 100)
                {
                    getlDialog().dismiss();
                }
            }
        });

        // 这个是当网页上的连接被点击的时候
        wv_pawn.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(final WebView view, final String url)
            {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        wv_pawn.loadUrl(BaseConstant.BaseURL+"/m/pawn/popup");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindo.dismiss();
                Intent intent=new Intent();
                intent.setClass(getActivity(), PawnActivity.class);
                intent.putExtra("type","pawn");
                intent.putExtra("NoPawnInfo",info);
                intent.putExtra("index",index);
                startActivity(intent);
            }
        });

        // 自定义view添加触摸事件
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindo != null && popupWindo.isShowing()) {
                    popupWindo.dismiss();
                    popupWindo = null;
                }
                return false;
            }
        });

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        popupWindo = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindo.setOutsideTouchable(true);
        popupWindo.setFocusable(true);
        fitPopupWindowOverStatusBar(popupWindo);
        popupWindo.setBackgroundDrawable(new ColorDrawable());
        popupWindo.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void showList(ArrayList<NoPawnInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<NoPawnInfo>(getActivity(), result,
                    R.layout.item_pawn) {
                @Override
                public void convert(final ViewHolder helper, final NoPawnInfo item) {
                    ImageView iv_pic_jb=(ImageView) helper.getView(R.id.iv_pic_pawn);
                    if(!item.getImage().equals("")){
                        List<String> list= Arrays.asList(item.getImage().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0),iv_pic_jb,getImageLoaderOptions());
                    }

                    helper.setText(R.id.tv_name_pawn,item.getTitle());
                    helper.setText(R.id.tv_jdprice_pawn,"￥"+item.getAuthPrice());
                    if(index==2){
                        helper.setTextcolor(R.id.tv_jdprice_pawn,getResources().getColor(R.color.red_k));
                    }else{
                        helper.setTextcolor(R.id.tv_jdprice_pawn,getResources().getColor(R.color.text_gray));
                    }

                    helper.getView(R.id.tv_dkpz_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_ckdp_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_xd_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_sd_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_countTime_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_endTime_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_num_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_yffdj_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_qh_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_qdd_pawn).setVisibility(View.GONE);
                    helper.getView(R.id.tv_ysh_pawn).setVisibility(View.GONE);

                    helper.getView(R.id.tv_isyj_pawn).setVisibility(View.VISIBLE);

                    if(item.getAuthCnt().equals("0")){
                        helper.getView(R.id.tv_td_pawn).setVisibility(View.GONE);
                    }else{
                        helper.getView(R.id.tv_td_pawn).setVisibility(View.VISIBLE);
                    }

                    if(item.getState().equals("0")){
                        helper.setText(R.id.tv_isyj_pawn,"未邮寄");
                    }else{
                        helper.setText(R.id.tv_isyj_pawn,"已邮寄");
                    }

                    if(item.getIsRedeem()!=null&&item.getIsRedeem().equals("1")){//已赎回
                        helper.getView(R.id.tv_ysh_pawn).setVisibility(View.VISIBLE);
                    }else{
                        helper.getView(R.id.tv_ysh_pawn).setVisibility(View.GONE);
                    }

                    helper.setTextcolor(R.id.tv_isyj_pawn,getResources().getColor(R.color.text_gray));

                    if(!item.getGoSellType().equals("-1")) {//卖给平台
                        //0：未确认 1：确认
                        if(item.getGoSellType().equals("1")){
                            //helper.getView(R.id.tv_ckpz_jb).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_isyj_pawn,"已卖给平台");
                            helper.setTextcolor(R.id.tv_isyj_pawn,getResources().getColor(R.color.red_k));
                        }else{
                            helper.setText(R.id.tv_isyj_pawn,"卖给平台处理中");
                            helper.setTextcolor(R.id.tv_isyj_pawn,getResources().getColor(R.color.red_k));
                        }
                    }else{
                        helper.getView(R.id.tv_qh_pawn).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_qdd_pawn).setVisibility(View.VISIBLE);
                    }

                    //去典当
                    helper.getView(R.id.tv_qdd_pawn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupWindow(item);
                        }
                    });

                    //取回
                    helper.getView(R.id.tv_qh_pawn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent();
                            intent.setClass(getActivity(), RetrieveActivity.class);
                            intent.putExtra("id",item.getId());
                            startActivity(intent);
                        }
                    });

                    //平台托底
                    helper.getView(R.id.tv_td_pawn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent();
                            intent.setClass(getActivity(), PlatformActivity.class);
                            intent.putExtra("pawnid",item.getPawnId());
                            startActivity(intent);
                        }
                    });

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
            noPawnList();
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
        noPawnList();
    }

    private void noPawnList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url="";
        if(index==2){
            url =BaseConstant.getApiPostUrl("userPawn/noPawnList");
        }else{
            url =BaseConstant.getApiPostUrl("userPawn/cancelPawnList");
        }
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<NoPawnInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<NoPawnInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<NoPawnInfo>>> response) {
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
                    public void onError(Response<DataResult<ArrayList<NoPawnInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

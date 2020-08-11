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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.appraisal.AddSendCallActivity;
import com.glavesoft.pawnuser.activity.appraisal.MailAppraisalActivity;
import com.glavesoft.pawnuser.activity.pawn.PawnActivity;
import com.glavesoft.pawnuser.activity.main.RetrieveActivity;
import com.glavesoft.pawnuser.activity.main.SelloutActivity;
import com.glavesoft.pawnuser.activity.personal.VoucherActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.MyAppraisalInfo;
import com.glavesoft.util.GlideLoader;
import com.glavesoft.util.StringUtils;
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
 * @author 严光
 * @date: 2017/10/25
 * @company:常州宝丰
 */
public class MyAppraisalFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private RelativeLayout titlebar_refresh;
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<MyAppraisalInfo> list = new ArrayList<>();
    CommonAdapter commAdapter;
    private int page = 1;
    private int listsize = 0;
    private LinearLayout ll_nodata;
    private TextView tv_nodata;

    public static MyAppraisalFragment newInstance(int index) {
        MyAppraisalFragment fragment = new MyAppraisalFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_listview_goods, container, false);
        setBoardCast();
        initView(view);
        return view;
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("AppraisalRefresh");
        getActivity().registerReceiver(mListenerID, f);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            resetPageData();
        }
    };

    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mListenerID);
    }

    private void initView(View view) {
        titlebar_refresh = (RelativeLayout) view.findViewById(R.id.titlebar_refresh);
        titlebar_refresh.setVisibility(View.GONE);
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);

        ll_nodata = (LinearLayout) view.findViewById(R.id.ll_nodata);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview = (ListView) view.findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        MyAppraisalList();
    }

    private void showList(ArrayList<MyAppraisalInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<MyAppraisalInfo>(getActivity(), result,
                    R.layout.item_myappraisal) {
                @Override
                public void convert(final ViewHolder helper, final MyAppraisalInfo item) {
                    ImageView iv_pic_jb = (ImageView) helper.getView(R.id.iv_pic_jb);
                    if (!item.getImage().equals("")) {
                        List<String> list = Arrays.asList(item.getImage().split(","));
                        GlideLoader.loadRoundImage(BaseConstant.Image_URL + list.get(0), iv_pic_jb, R.drawable.sy_bj);
                    } else {
                        GlideLoader.loadRoundImage("", iv_pic_jb, R.drawable.sy_bj);
                    }

                    helper.setText(R.id.tv_name_jb, item.getTitle());
                    //-1:无 0:未鉴定 1:鉴定中 2:无法鉴定 3:赝品 4:真品
                    if (item.getResult().equals("-1")) {
                        helper.setText(R.id.tv_result_jb, "鉴定结果：无");
                        helper.setText(R.id.tv_jdprice_jb, "预估价：￥" + item.getPriceTest());
                    } else if (item.getResult().equals("0")) {
                        helper.setText(R.id.tv_result_jb, "鉴定结果：未鉴定");
                        helper.setText(R.id.tv_jdprice_jb, "鉴定价：暂无");
                    } else if (item.getResult().equals("1")) {
                        helper.setText(R.id.tv_result_jb, "鉴定结果：鉴定中");
                        helper.setText(R.id.tv_jdprice_jb, "鉴定价：暂无");
                    } else if (item.getResult().equals("2")) {
                        helper.setText(R.id.tv_result_jb, "鉴定结果：无法鉴定");
                        helper.setText(R.id.tv_jdprice_jb, "鉴定价：暂无");
                    } else if (item.getResult().equals("3")) {
                        helper.setText(R.id.tv_result_jb, "鉴定结果：赝品");
                        helper.setText(R.id.tv_jdprice_jb, "鉴定价：暂无");
                    } else if (item.getResult().equals("4")) {
                        helper.setText(R.id.tv_result_jb, "鉴定结果：可以鉴定");
                        helper.setText(R.id.tv_jdprice_jb, "鉴定价：￥" + item.getPrice());
                    }

                    //1未邮寄2邮寄中3平台确认
                    if (item.getPostState().equals("1")) {
                        helper.setText(R.id.tv_isyj_jb, "未邮寄");
                        helper.setTextcolor(R.id.tv_isyj_jb, getResources().getColor(R.color.black));
                    } else {
                        helper.setText(R.id.tv_isyj_jb, "已邮寄");
                        helper.setTextcolor(R.id.tv_isyj_jb, getResources().getColor(R.color.black));
                    }


                    helper.getView(R.id.tv_qh_jb).setVisibility(View.GONE);
                    helper.getView(R.id.tv_mgpt_jb).setVisibility(View.GONE);
                    helper.getView(R.id.tv_qdd_jb).setVisibility(View.GONE);
                    helper.getView(R.id.tv_yjjd_jb).setVisibility(View.GONE);
                    helper.getView(R.id.tv_bblzs_jb).setVisibility(View.GONE);
                    helper.getView(R.id.tv_ckpz_jb).setVisibility(View.GONE);
                    helper.getView(R.id.tv_result_sm).setVisibility(View.GONE);

                    if (!StringUtils.isEmpty(item.getAppraisalDsc())) {
                        helper.getView(R.id.tv_result_sm).setVisibility(View.VISIBLE);
                    }

                    if (!item.getPlatformIsVerify().equals("-1")) {//卖给平台
                        //0：未确认 1：确认
                        if (item.getPlatformIsVerify().equals("1")) {
                            helper.getView(R.id.tv_ckpz_jb).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_isyj_jb, "已卖给平台");
                            helper.setTextcolor(R.id.tv_isyj_jb, getResources().getColor(R.color.red_k));
                        } else {
                            helper.setText(R.id.tv_isyj_jb, "卖给平台处理中");
                            helper.setTextcolor(R.id.tv_isyj_jb, getResources().getColor(R.color.red_k));
                        }
                    } else {
                        if (item.getPostState().equals("1")) {//未邮寄
                            helper.getView(R.id.tv_yjjd_jb).setVisibility(View.VISIBLE);
                        } else if (!item.getResult().equals("4")) {//真品
                            if (!item.getResult().equals("-1") && !item.getResult().equals("0")) {
                                helper.getView(R.id.tv_qh_jb).setVisibility(View.VISIBLE);
                            }
                        } else {
                            helper.getView(R.id.tv_qh_jb).setVisibility(View.VISIBLE);
                            helper.getView(R.id.tv_mgpt_jb).setVisibility(View.VISIBLE);
                            helper.getView(R.id.tv_qdd_jb).setVisibility(View.VISIBLE);
                            helper.getView(R.id.tv_bblzs_jb).setVisibility(View.VISIBLE);
                        }
                    }

                    helper.getView(R.id.tv_mgpt_jb).setVisibility(View.GONE);

                    //邮寄鉴定
                    helper.getView(R.id.tv_yjjd_jb).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), MailAppraisalActivity.class);
                            intent.putExtra("type", "online");
                            intent.putExtra("MyAppraisalInfo", item);
                            startActivity(intent);
                        }
                    });

                    //取回
                    helper.getView(R.id.tv_qh_jb).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), RetrieveActivity.class);
                            intent.putExtra("id", item.getId());
                            startActivity(intent);
                        }
                    });

                    //卖给平台
                    helper.getView(R.id.tv_mgpt_jb).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), SelloutActivity.class);
                            intent.putExtra("MyAppraisalInfo", item);
                            startActivity(intent);
                        }
                    });

                    //寄卖
                    helper.getView(R.id.tv_jm_jb).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sellUserGoods(item.getId());
                        }
                    });

                    //去典当
                    helper.getView(R.id.tv_qdd_jb).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupWindow(item);
                        }
                    });

                    //办理证书
                    helper.getView(R.id.tv_bblzs_jb).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotokf_Z(getActivity());
                        }
                    });

                    //查看凭证
                    helper.getView(R.id.tv_ckpz_jb).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), VoucherActivity.class);
                            intent.putExtra("ticket", item.getPlatformPayTicket());
                            startActivity(intent);
                        }
                    });

                    //鉴定说明
                    helper.getView(R.id.tv_result_sm).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupWindow(item.getAppraisalDsc());
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

    private void gotoPawn(MyAppraisalInfo info) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), PawnActivity.class);
        intent.putExtra("type", "appraisal");
        intent.putExtra("MyAppraisalInfo", info);
        startActivity(intent);
    }

    private PopupWindow popupWindo;

    public void showPopupWindow(final MyAppraisalInfo info) {
        if (popupWindo != null) {
            popupWindo = null;
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pw_pawn, null);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok_pawn);
        WebView wv_pawn = (WebView) view.findViewById(R.id.wv_pawn);

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
        wv_pawn.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    getlDialog().dismiss();
                }
            }
        });

        // 这个是当网页上的连接被点击的时候
        wv_pawn.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        wv_pawn.loadUrl(BaseConstant.BaseURL + "/m/pawn/popup");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindo.dismiss();
                gotoPawn(info);
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

    public void showPopupWindow(String msg) {
        if (popupWindo != null && popupWindo.isShowing()) {
            popupWindo.dismiss();
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pw_dialog2, null);

        TextView tv_ts_dialog = (TextView) view.findViewById(R.id.tv_ts_dialog);
        TextView tv_content_dialog = (TextView) view.findViewById(R.id.tv_content_dialog);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);

        tv_ts_dialog.setText("鉴定说明");
        tv_content_dialog.setText(msg);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindo.dismiss();
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
        String url = BaseConstant.getApiPostUrl("userGoods/myGoods");
        getlDialog().show();
        OkGo.<DataResult<ArrayList<MyAppraisalInfo>>>post(url)
                .params("token", token)
                .params("page", page + "")
                .params("limit", "10")
                .execute(new JsonCallback<DataResult<ArrayList<MyAppraisalInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<MyAppraisalInfo>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                ll_nodata.setVisibility(View.GONE);
                                mRefreshLayout.setVisibility(View.VISIBLE);
                                listsize = response.body().getData().size();
                                showList(response.body().getData());
                            } else {
                                if (list.size() == 0) {
                                    ll_nodata.setVisibility(View.VISIBLE);
                                    mRefreshLayout.setVisibility(View.GONE);
                                    tv_nodata.setText("暂无鉴定信息，快去别的地方逛逛吧~");
                                }
                            }
                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<MyAppraisalInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

    /**
     * 寄卖
     */
    private void sellUserGoods(final String id) {
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url = BaseConstant.getApiPostUrl("userGoods/sellUserGoods");
        getlDialog().show();
        OkGo.<DataResult<Integer>>post(url)
                .params("token", token)
                .params("id", id)
                .execute(new JsonCallback<DataResult<Integer>>() {
                    @Override
                    public void onSuccess(Response<DataResult<Integer>> response) {
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {

                            if (response.body().getData() != null) {
                                showPopupWindow1(id);
                            }
                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
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

    private PopupWindow popupWindow;

    public void showPopupWindow1(final String id) {
        if (popupWindow != null) {
            popupWindow = null;
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pw_dialog1, null);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        ((TextView) view.findViewById(R.id.tv_content)).setText("选择寄卖且成功售出后，平台将收取鉴定" +
                "价的5%作为手续费（包含了银行的结算费用，专家鉴定费等费用）。" +
                "寄卖商品一律采取顺丰保价到付邮费。同意请点击确定前往寄卖，否则关闭该窗口。");

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(MyAppraisalFragment.this.getContext(), AddSendCallActivity.class);
                intent.putExtra("type", "update");
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        popupWindow = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        fitPopupWindowOverStatusBar(popupWindow);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}

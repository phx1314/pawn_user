package com.glavesoft.pawnuser.activity.shoppingmall;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.StoreBannerInfo;
import com.glavesoft.pawnuser.mod.StoreGoodsInfo;
import com.glavesoft.pawnuser.mod.orgInfo;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.SlideShowView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2017/11/30
 * @company:常州宝丰
 */
public class DeadPawnageActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private BGARefreshLayout mRefreshLayout;
    private RecyclerView mDataRv;
    private ArrayList<StoreGoodsInfo> list=new ArrayList<>();
    private View headerview;
    private SlideShowView ssv_header_shoppingmall_pic;
    private JdStaggeredGridAdapter mStaggeredGridAdapter;
    private List<String> adlist=new ArrayList<>();


    private ArrayList<StoreBannerInfo> StoreBannerList=new ArrayList<>();
    private int page=1;
    private int listsize=0;

    private String timeUp="0";//时间排序 0升序 1降序
    private String priceUp="0";//价格 0升序 1降序
    private String orgId="";//商铺id

    private LinearLayout ll_screen;

    private ArrayList<orgInfo> orglist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadpawn);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("淘宝贝");
        setTitleNameEn(R.mipmap.amoy_baby);

        headerview = LayoutInflater.from(DeadPawnageActivity.this).inflate(R.layout.header_jdshop, mDataRv, false);
        ssv_header_shoppingmall_pic = (SlideShowView) headerview.findViewById(R.id.ssv_header_shoppingmall_pic);
        ll_screen= (LinearLayout) headerview.findViewById(R.id.ll_screen);



        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_recyclerview_refresh);
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
        mStaggeredGridAdapter = new JdStaggeredGridAdapter(DeadPawnageActivity.this, list);
        mDataRv.setAdapter(mStaggeredGridAdapter);

        //获取点击的view子控件
        mStaggeredGridAdapter.setmItemOnClickListener(new JdStaggeredGridAdapter.ItemOnClickListener() {

            @Override
            public void itemOnClickListener(String type) {
//                TextView tvName = (TextView)view;
                //提示点击的子控件的文本
                if(type.equals("时间")){
                    if(timeUp.equals("0")){
                        timeUp="1";
                    }else{
                        timeUp="0";
                    }
                    orgId="";
                    resetPageData();
                }

                if(type.equals("价格")){
                    if(priceUp.equals("0")){
                        priceUp="1";
                    }else{
                        priceUp="0";
                    }
                    orgId="";
                    resetPageData();
                }

                if(type.equals("店铺")){
                    orgName();
                }
            }
        });

        jdGoodsBanner();
        storeJDGoodsList();
    }

    private PopupWindow popwindow;
    public void showPopupWindow()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.pw_listview, null);

        ListView lv_district = ((ListView) view.findViewById(R.id.lv_content));
        lv_district.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (!orgId.equals(orglist.get(position).getOrgId())){
                    popwindow.dismiss();
                    orgId=orglist.get(position).getOrgId();
                    resetPageData();
                }
            }
        });

        CommonAdapter commAdapter = new CommonAdapter<orgInfo>(DeadPawnageActivity.this, orglist,
                R.layout.item_listview_simple) {
            @Override
            public void convert(final ViewHolder helper, final orgInfo item) {
                helper.setText(R.id.textview, item.getOrgName());
                if (item.getOrgId().equals(orgId)){
                    helper.getView(R.id.rl_list).setBackgroundColor(getResources().getColor(R.color.bg_title));
                }else{
                    helper.getView(R.id.rl_list).setBackgroundColor(getResources().getColor(R.color.green_bg));
                }
            }
        };
        lv_district.setAdapter(commAdapter);

        int[] location = new int[2];
        headerview.getLocationOnScreen(location);

        int aHeight = headerview.getHeight() + location[1];
        int tHeight = getWindowManager().getDefaultDisplay().getHeight() - aHeight;
        int aWidth = headerview.getWidth();
        popwindow = new PopupWindow(view, aWidth - ScreenUtils.dp2px(this,40), tHeight, true);
        popwindow.setOutsideTouchable(true);
        popwindow.setBackgroundDrawable(new ColorDrawable());
        popwindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0]+ScreenUtils.dp2px(this, 40), aHeight);
    }

    private PopupWindow popupWindo;
    public void showPopupWindow1()
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.pw_shop, null);
        ImageView iv_kk = (ImageView)view.findViewById(R.id.iv_kk);
        ImageView iv_close = (ImageView)view.findViewById(R.id.iv_close);

        iv_kk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
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

        Display display = getWindowManager().getDefaultDisplay();
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
        if(listsize==10){
            page++;
            storeJDGoodsList();
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
//        mStaggeredGridAdapter = null;
//        mDataRv.setAdapter(null);
        storeJDGoodsList();
    }

    private void jdGoodsBanner()
    {
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("storeGoods/jdGoodsBanner");
        getlDialog().show();
        OkGo.<DataResult<ArrayList<StoreBannerInfo>>>post(url)
                .params("token", token)
                .params("page", "1")
                .params("limit", "1000")
                .execute(new JsonCallback<DataResult<ArrayList<StoreBannerInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<StoreBannerInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){

                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                StoreBannerList=response.body().getData();
                                ssv_header_shoppingmall_pic.setVisibility(View.VISIBLE);
                            }else{
                                ssv_header_shoppingmall_pic.setVisibility(View.GONE);
                            }

                            headerview = LayoutInflater.from(DeadPawnageActivity.this).inflate(R.layout.header_jdshop, mDataRv, false);
                            mStaggeredGridAdapter.setHeaderView(headerview,StoreBannerList);
                            //showPopupWindow1();

                        }else if (response.body().getErrorCode()==DataResult.RESULT_102)
                        {
                            toLogin();
                        }else {
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


    private void storeJDGoodsList()
    {
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("storeGoods/storeJDGoodsList");
        getlDialog().show();
        OkGo.<DataResult<ArrayList<StoreGoodsInfo>>>post(url)
                .params("token", token)
                .params("page", page+"")
                .params("limit", "10")
                .params("timeUp", timeUp)
                .params("priceUp", priceUp)
                .params("orgId", orgId)
                .execute(new JsonCallback<DataResult<ArrayList<StoreGoodsInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<StoreGoodsInfo>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){

                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                listsize=response.body().getData().size();
                                if(list.size()==0){
                                    list= response.body().getData();
                                }else {
                                    for (int i = 0; i < response.body().getData().size(); i++) {
                                        list.add(response.body().getData().get(i));
                                    }
                                }
                                mStaggeredGridAdapter.refreshDatas(list);

                            }else{
                                if(list.size()>0){
                                    CustomToast.show("无更多数据");
                                }else{
                                    mStaggeredGridAdapter.refreshDatas(list);
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
                    public void onError(Response<DataResult<ArrayList<StoreGoodsInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

    private void orgName()
    {
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("storeGoods/orgName");
        getlDialog().show();
        OkGo.<DataResult<ArrayList<orgInfo>>>post(url)
                .params("token", token)
                .params("page", "1")
                .params("limit", "1000")
                .execute(new JsonCallback<DataResult<ArrayList<orgInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<orgInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){

                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                orglist=response.body().getData();
                                showPopupWindow();

                            }else{

                            }

                        }else if (response.body().getErrorCode()==DataResult.RESULT_102)
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<orgInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}

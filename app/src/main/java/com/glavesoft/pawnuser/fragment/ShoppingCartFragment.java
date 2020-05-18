package com.glavesoft.pawnuser.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.MyExpandableListAdapter;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.ShopCar;
import com.glavesoft.pawnuser.mod.ShoppingCartBean;
import com.glavesoft.pawnuser.shoppingcar.Constant;
import com.glavesoft.pawnuser.shoppingcar.JsonReponseHandler;
import com.glavesoft.pawnuser.shoppingcar.OnShoppingCartChangeListener;
import com.glavesoft.pawnuser.shoppingcar.ShoppingCartBiz;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2018/6/13
 * @company:常州宝丰
 */
public class ShoppingCartFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.rl_shoppingcart)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.rlShoppingCartEmpty)
    RelativeLayout rlShoppingCartEmpty;
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;
    @BindView(R.id.ivSelectAll)
    ImageView ivSelectAll;
    @BindView(R.id.tvCountMoney)
    TextView tvCountMoney;
    @BindView(R.id.btnSettle)
    TextView btnSettle;
    @BindView(R.id.rlBottomBar)
    RelativeLayout rlBottomBar;

    @BindView(R.id.iv_en)
    ImageView iv_en;
    @BindView(R.id.ll_en)
    LinearLayout ll_en;

    private int index;
    private Unbinder unbinder;

    private List<ShopCar> mListGoods = new ArrayList<>();
    private MyExpandableListAdapter adapter;


    public static ShoppingCartFragment newInstance(int index) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt("index");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoppingcart, container, false);
        //initView(view);
        unbinder = ButterKnife.bind(this, view);

        setBoardCast();
        //ButterKnife.bind(ShoppingCartFragment.this.getActivity());
        setAdapter();
        requestShoppingCartList();
        initView(view);
        return view;
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("ShopcarRefresh");
        getActivity().registerReceiver(mListenerID, f);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            ShoppingCartBiz.checkItem(false, ivSelectAll);
            requestShoppingCartList();
        }
    };

    public  void onDestroy(){
        super.onDestroy();
        getActivity().unregisterReceiver(mListenerID);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * onDestroyView中进行解绑操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        ShoppingCartBiz.checkItem(false, ivSelectAll);
        requestShoppingCartList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    private void initView(View view) {
        titlebar_name = (TextView) view.findViewById(R.id.titlebar_name);
        titlebar_name.setVisibility(View.VISIBLE);
        titlebar_name.setText("购物车");
        iv_en.setVisibility(View.VISIBLE);
        iv_en.setImageResource(R.mipmap.shopping_cart);
        ll_en.setVisibility(View.VISIBLE);
        mRefreshLayout.setDelegate(this);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i,
                                        long l) {
                return true;
            }
        });
    }

    private void setAdapter() {
        adapter = new MyExpandableListAdapter(ShoppingCartFragment.this.getActivity());
        expandableListView.setAdapter(adapter);
        adapter.setOnShoppingCartChangeListener(new OnShoppingCartChangeListener() {
            @Override
            public void onDataChange(String selectCount, String selectMoney) {
                int goodsCount = ShoppingCartBiz.getGoodsCount();
//                if (!isNetworkOk) {//网络状态判断暂时不显示
//                }
                if (goodsCount == 0) {
                    showEmpty(true);
                } else {
                    showEmpty(false);//其实不需要做这个判断，因为没有商品的时候，必须退出去添加商品；
                }
                String countMoney = String.format(getResources().getString(R.string.count_money),
                        selectMoney);
                String countGoods = String.format(getResources().getString(R.string.count_goods),
                        selectCount);
                String title = String.format(getResources().getString(R.string.shop_title),
                        goodsCount + "");
                tvCountMoney.setText(countMoney);
                btnSettle.setText(countGoods);
                //tvTitle.setText(title);
            }

            @Override
            public void onSelectItem(boolean isSelectedAll) {
                ShoppingCartBiz.checkItem(isSelectedAll, ivSelectAll);
            }
        });
        //通过监听器关联Activity和Adapter的关系，解耦；
        View.OnClickListener listener = adapter.getAdapterListener();
        if (listener != null) {
            //即使换了一个新的Adapter，也要将“全选事件”传递给adapter处理；
            ivSelectAll.setOnClickListener(adapter.getAdapterListener());
            //结算时，一般是需要将数据传给订单界面的
            btnSettle.setOnClickListener(adapter.getAdapterListener());
        }

    }

    public void showEmpty(boolean isEmpty) {
        if (isEmpty) {
            expandableListView.setVisibility(View.GONE);
            rlShoppingCartEmpty.setVisibility(View.VISIBLE);
            rlBottomBar.setVisibility(View.GONE);
        } else {
            expandableListView.setVisibility(View.VISIBLE);
            rlShoppingCartEmpty.setVisibility(View.GONE);
            rlBottomBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取购物车列表的数据（数据和网络请求也是非通用部分）
     */
    private void requestShoppingCartList() {
        ShoppingCartBiz.delAllGoods();
        testAddGood();
        //使用本地JSON，作测试用。本来应该是将商品ID发送的服务器，服务器返回对应的商品信息；
        if (BaseConstant.isLogin()) {
            getShopCar();
        } else {
            CustomToast.show("请先登录");
            String countMoney = String.format(getResources().getString(R.string.count_money),
                    "0");
            String countGoods = String.format(getResources().getString(R.string.count_goods),
                    "0");
            tvCountMoney.setText(countMoney);
            btnSettle.setText(countGoods);
        }


    }
    private void getShopCar(){
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("userShopCart/list");
        HttpParams param=new HttpParams();
        param.put("token", LocalData.getInstance().getUserInfo().getToken());
        Log.d("ID", "getShopCar: "+LocalData.getInstance().getUserInfo().getUserid());
        param.put("userId", LocalData.getInstance().getUserInfo().getUserid());
        OkGo.<DataResult<List<ShopCar>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<List<ShopCar>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<List<ShopCar>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                showEmpty(false);
                                mListGoods =response.body().getData();
                                Constant.ShoppingcarGoodsCount=0;
                                ShoppingCartBiz.updateShopList(mListGoods);
                                updateListView();
                            }else{
                                showEmpty(true);
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<List<ShopCar>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        showVolleyError(null);
                    }
                });
    }

    public static String readJson(InputStream is) {
        //从给定位置获取文件
//        File file = new File(path);
        BufferedReader reader = null;
        //返回值,使用StringBuffer
        StringBuffer data = new StringBuffer();
        //
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            //每次读取文件的缓存
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                data.append(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件流
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

    private void updateListView() {
        adapter.setList(mListGoods);
        adapter.notifyDataSetChanged();
        expandAllGroup();
    }

    /**
     * 展开所有组
     */
    private void expandAllGroup() {
        for (int i = 0; i < mListGoods.size(); i++) {
            expandableListView.expandGroup(i);
        }
    }

    /**
     * 测试添加数据 ，添加的动作是通用的，但数据上只是添加ID而已，数据非通用
     */
    private void testAddGood() {
        ShoppingCartBiz.addGoodToCart("279457f3-4692-43bf-9676-fa9ab9155c38", "6");
        ShoppingCartBiz.addGoodToCart("95fbe11d-7303-4b9f-8ca4-537d06ce2f8a", "8");
        ShoppingCartBiz.addGoodToCart("8c6e52fb-d57c-45ee-8f05-50905138801b", "9");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801d", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801e", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801f", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801g", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801h", "3");
    }

    public static List<ShoppingCartBean> handleOrderList(JSONObject response, int errCode) {
        List<ShoppingCartBean> list = null;
        if (true) {
            list = JsonReponseHandler.getListFromJsonWithPageEntity(
                    response, new TypeToken<List<ShoppingCartBean>>() {
                    }.getType(), null, null);
        }
        return list;
    }


}

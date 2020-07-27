package com.glavesoft.pawnuser.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.glavesoft.F;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.activity.main.StoreActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.JdGoodsDetailActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.frg.FrgProductDetail;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.IndexMenuInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.utility.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Sinyu on 2018/8/3.
 */

public class GoodsSeachFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<IndexMenuInfo> list = new ArrayList<>();
    CommonAdapter commAdapter;
    public static String keyword = "";
    private int page = 1;
    private int listsize = 0;
    private ArrayList<String> historcalList = new ArrayList<>();

    public static Fragment newInstance(String str) {
        GoodsSeachFragment fragment = new GoodsSeachFragment();
        keyword = str;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_seach, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(GoodsSeachFragment.this.getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview = (ListView) view.findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getGoodsType().equals("3")) {
                    Intent intent = new Intent(GoodsSeachFragment.this.getActivity(), JdGoodsDetailActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    startActivity(intent);
                } else {
                    if (list.get(position).getGoodsType().equals("1")) {
//                        Intent intent = new Intent(GoodsSeachFragment.this.getActivity(), GoodsDetailActivity.class);
//                        intent.putExtra("id", list.get(position).getId());
//                        intent.putExtra("type", "rz");
//                        startActivity(intent);
                        F.INSTANCE.go2GoodeDetail(getActivity(),     list.get(position).getId(), "rz");
                    } else {
//                        Intent intent = new Intent(GoodsSeachFragment.this.getActivity(), GoodsDetailActivity.class);
//                        intent.putExtra("id", list.get(position).getId());
//                        intent.putExtra("type", "jd");
//                        startActivity(intent);
                        F.INSTANCE.go2GoodeDetail(getActivity(),     list.get(position).getId(), "jd");
                    }
                }
            }
        });

        if (PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical, null) != null) {
            java.lang.reflect.Type classtype = new TypeToken<ArrayList<String>>() {
            }.getType();
            String jsonString = PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical, null);
            historcalList = CommonUtils.fromJson(jsonString, classtype, CommonUtils.DEFAULT_DATE_PATTERN);
        }

        resetPageData();
    }


    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();

    private void showList(ArrayList<IndexMenuInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<IndexMenuInfo>(GoodsSeachFragment.this.getActivity(), result,
                    R.layout.item_seach_goods) {
                @Override
                public void convert(final ViewHolder helper, final IndexMenuInfo item) {

                    helper.getView(R.id.ll_newprice_jp).setVisibility(View.GONE);

                    ImageView iv_pic_jp = (ImageView) helper.getView(R.id.iv_pic_jp);
                    if (!item.getImg().equals("")) {
                        List<String> list = Arrays.asList(item.getImg().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0), iv_pic_jp, getImageLoaderOptions());
                    } else {
                        getImageLoader().displayImage("", iv_pic_jp, getImageLoaderOptions());
                    }
                    if (item.getSource().equals("3")) {
                        helper.setText(R.id.tv_type, "自营");
                    } else if (item.getSource().equals("4")) {
                        helper.setText(R.id.tv_type, "臻品");
                    } else if (item.getSource().equals("2")) {
                        helper.setText(R.id.tv_type, "绝当品");
                    }

                    helper.setText(R.id.tv_name_jp, item.getTitle());
                    helper.setText(R.id.tv_jdprice_jp, "￥" + item.getPrice());
                    helper.setText(R.id.tv_qprice_jp, "￥" + item.getPrice());
                    helper.setText(R.id.tv_qprice1_jp, "售价：");
                    helper.setText(R.id.tv_store_name, item.getOrgName() + "    进店>");
                    helper.getView(R.id.tv_store_name).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setClass(GoodsSeachFragment.this.getContext(), StoreActivity.class);
                            intent.putExtra("storeid", item.getOrgId());
                            startActivity(intent);
                        }
                    });
                    if (item.getState().equals("2")) {
                        helper.getView(R.id.ll_jdprice_jp).setVisibility(View.VISIBLE);
                    }

                    if (item.getState().equals("3")) {
                        helper.getView(R.id.ll_jdprice_jp).setVisibility(View.VISIBLE);
                        helper.getView(R.id.ll_newprice_jp).setVisibility(View.VISIBLE);
                        helper.getView(R.id.ll_myprice_jp).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_countTime_jp).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_qprice1_jp, "起价：");

                        helper.setText(R.id.tv_newprice_jp, "￥" + item.getMaxPrice());
                        if (item.getMyPrice().equals("")) {
                            helper.getView(R.id.ll_myprice_jp).setVisibility(View.GONE);
                        } else {
                            helper.getView(R.id.ll_myprice_jp).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_myprice_jp, "￥" + item.getMyPrice());
                        }

                        TextView tv_countTime_jp = (TextView) helper.getView(R.id.tv_countTime_jp);
                        CountDownTimer countDownTimer = countDownCounters.get(tv_countTime_jp.hashCode());
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
                                    helper.setText(R.id.tv_countTime_jp, certTime + "后结束");

                                    item.setTime(item.getTime() - 1);

                                }

                                public void onFinish() {
                                    helper.setText(R.id.tv_countTime_jp, "已结束");
                                }
                            }.start();
                            countDownCounters.put(tv_countTime_jp.hashCode(), countDownTimer);
                        } else {
                            helper.setText(R.id.tv_countTime_jp, "已结束");
                        }
                    }

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
        }
    }

    public void resetPageData() {
        page = 1;
        list.clear();
        commAdapter = null;
        lv_listview.setAdapter(null);
        searchIndexMenu();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (keyword.toString().trim().length() > 0 && !keyword.toString().equals("")) {
            resetPageData();
        } else {
            keyword = "";
            list.clear();
            commAdapter = null;
            lv_listview.setAdapter(null);
            mRefreshLayout.endRefreshing();
            mRefreshLayout.endLoadingMore();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (listsize == 10) {
            page++;
            searchIndexMenu();
        } else {
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    private void searchIndexMenu() {
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url = BaseConstant.getApiPostUrl("userGoods/searchIndexMenu");
        HttpParams param = new HttpParams();
        param.put("token", token);
        param.put("name", keyword);
        OkGo.<DataResult<ArrayList<IndexMenuInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<IndexMenuInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<IndexMenuInfo>>> response) {
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {

                                listsize = response.body().getData().size();
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    if (response.body().getData().get(i).getState().equals("3")) {
                                        response.body().getData().get(i).setTime(Long.valueOf(response.body().getData().get(i).getEndTime2()));
                                    }
                                }
                                showList(response.body().getData());
                            } else {

                            }
                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<IndexMenuInfo>>> response) {
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

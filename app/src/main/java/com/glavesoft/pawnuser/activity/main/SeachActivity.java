package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.shoppingmall.JdGoodsDetailActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.frg.FrgProductDetail;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.IndexMenuInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.utility.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


public class SeachActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<IndexMenuInfo> list = new ArrayList<>();
    CommonAdapter commAdapter;
    private String keyword = "";
    private int page = 1;
    private int listsize = 0;
    private ArrayList<String> historcalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        keyword = getIntent().getStringExtra("keyword");
        init();
    }

    private void init() {
        settitle_Searchcancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gettitle_Searchet().setText(keyword);
        gettitle_Searchet().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!v.getText().toString().trim().equals("")) {
                        savedata(v.getText().toString().trim());
                        keyword = v.getText().toString().trim();
                        resetPageData();
                    }
                    return true;
                }
                return false;
            }
        });

        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview = (ListView) findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getState().equals("3")) {
                    Intent intent = new Intent(SeachActivity.this, JdGoodsDetailActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    startActivity(intent);
                } else {
                    if (list.get(position).getState().equals("1")) {
//                        Intent intent = new Intent(SeachActivity.this, GoodsDetailActivity.class);
//                        intent.putExtra("id", list.get(position).getId());
//                        intent.putExtra("type", "rz");
//                        startActivity(intent);

                        Helper.startActivity(
                                SeachActivity.this,
                                FrgProductDetail.class,
                                TitleAct.class,
                                "id",
                                list.get(position).getId(), "type", "rz"
                        );
                    } else {
//                        Intent intent = new Intent(SeachActivity.this, GoodsDetailActivity.class);
//                        intent.putExtra("id", list.get(position).getId());
//                        intent.putExtra("type", "jd");
//                        startActivity(intent);
                        Helper.startActivity(
                                SeachActivity.this,
                                FrgProductDetail.class,
                                TitleAct.class,
                                "id",
                                list.get(position).getId(), "type", "jd"
                        );
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

    private void savedata(String keyword) {
        if (historcalList.contains(keyword)) {
            int index = 0;
            for (int i = 0; i < historcalList.size(); i++) {
                if (keyword.equals(historcalList.get(i))) {
                    index = i;
                }
            }
            historcalList.remove(index);
            historcalList.add(keyword);
        } else {
            historcalList.add(keyword);
        }

        if (historcalList.size() > 10) {
            historcalList.remove(0);
        }

        PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical, new Gson().toJson(historcalList));
    }

    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();

    private void showList(ArrayList<IndexMenuInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<IndexMenuInfo>(SeachActivity.this, result,
                    R.layout.item_jplist) {
                @Override
                public void convert(final ViewHolder helper, final IndexMenuInfo item) {

                    helper.getView(R.id.tv_buy_jp).setVisibility(View.GONE);
                    helper.getView(R.id.tv_status_jp).setVisibility(View.GONE);
                    helper.getView(R.id.ll_newprice_jp).setVisibility(View.GONE);
                    helper.getView(R.id.ll_myprice_jp).setVisibility(View.GONE);
                    helper.getView(R.id.tv_countTime_jp).setVisibility(View.GONE);
                    helper.getView(R.id.ll_jdprice_jp).setVisibility(View.GONE);

                    ImageView iv_pic_jp = (ImageView) helper.getView(R.id.iv_pic_jp);
                    if (!item.getImg().equals("")) {
                        List<String> list = Arrays.asList(item.getImg().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0), iv_pic_jp, getImageLoaderOptions());
                    } else {
                        getImageLoader().displayImage("", iv_pic_jp, getImageLoaderOptions());
                    }

                    helper.setText(R.id.tv_name_jp, item.getTitle());
                    helper.setText(R.id.tv_jdprice_jp, "￥" + item.getPrice());
                    helper.setText(R.id.tv_qprice_jp, "￥" + item.getPrice());
                    helper.setText(R.id.tv_qprice1_jp, "售价：");

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

    private void resetPageData() {
        page = 1;
        list.clear();
        commAdapter = null;
        lv_listview.setAdapter(null);
        searchIndexMenu();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (titlebar_et_keywords.getText().toString().trim().length() > 0 && !titlebar_et_keywords.getText().toString().equals("")) {
            keyword = titlebar_et_keywords.getText().toString().trim();
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
        getlDialog().show();
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
                        getlDialog().dismiss();
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
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

}

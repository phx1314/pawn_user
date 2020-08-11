package com.glavesoft.pawnuser.activity.appraisal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.SendCallCollectionInfo;
import com.glavesoft.util.GlideLoader;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendCallCollectionActivity extends BaseActivity {

    @BindView(R.id.lv_listview)
    ListView lvListview;
    CommonAdapter commAdapter;
    List<SendCallCollectionInfo> sendCallCollectionInfos;

    private LinearLayout ll_nodata;
    private TextView tv_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_call_collection);
        ButterKnife.bind(this);
        initView();
        collectSellUserGoods();
    }

    private void initView() {
        setTitleBack();
        setTitleName("我的收藏");
        setTitleNameEn(R.mipmap.my_collection);

        ll_nodata = (LinearLayout) findViewById(R.id.ll_nodata);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
    }

    private void showList() {

        if (commAdapter == null) {
            commAdapter = new CommonAdapter<SendCallCollectionInfo>(SendCallCollectionActivity.this, sendCallCollectionInfos,
                    R.layout.item_sendcall_collection) {
                @Override
                public void convert(final ViewHolder helper, final SendCallCollectionInfo item) {
                    helper.setText(R.id.tv_user_name, item.getNickName());
                    helper.setText(R.id.tv_goodsname, item.getName());
                    helper.setText(R.id.tv_type, "分类：" + item.getSellPawnCodeInfo());
                    helper.setText(R.id.tv_price, "￥" + item.getSellPrice());
                    if (!item.getHeadImg().equals("")) {
                        getImageLoader().displayImage(BaseConstant.Image_URL + item.getHeadImg(),
                                (ImageView) helper.getView(R.id.iv_head), getImageLoaderOptions());
                    } else {
                        getImageLoader().displayImage("", (ImageView) helper.getView(R.id.iv_head), getImageLoaderOptions());
                    }
                    if (!item.getSellImgs().equals("")) {
                        GlideLoader.loadRoundImage(BaseConstant.Image_URL + item.getSellImgs().split(",")[0], (ImageView) helper.getView(R.id.ivGoods), R.drawable.sy_bj);
                    } else {
                        getImageLoader().displayImage("", (ImageView) helper.getView(R.id.ivGoods), getImageLoaderOptions());
                    }
                    if (item.getSellStatus() == 0) {
                        helper.getView(R.id.rl_item_shopcar).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CustomToast.show("未上架");
                            }
                        });
                    } else if (item.getSellStatus() == 1) {
                        helper.getView(R.id.rl_item_shopcar).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SendCallCollectionActivity.this, SendCallGoodDetailActivity.class);
                                intent.putExtra("id", item.getId());
                                startActivity(intent);
                            }
                        });
                    } else if (item.getSellStatus() == 2) {
                        helper.getView(R.id.tv_issell).setVisibility(View.VISIBLE);
                    }

                    helper.getView(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showPopupWindow(item.getId());
                        }
                    });
                }
            };

            lvListview.setAdapter(commAdapter);
        }
    }

    private void collectSellUserGoods() {
        getlDialog().show();
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url = BaseConstant.getApiPostUrl("userGoods/collectSellUserGoods/list");
        HttpParams param = new HttpParams();
        param.put("token", token);
        OkGo.<DataResult<ArrayList<SendCallCollectionInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<SendCallCollectionInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<SendCallCollectionInfo>>> response) {
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                ll_nodata.setVisibility(View.GONE);
                                lvListview.setVisibility(View.VISIBLE);
                                sendCallCollectionInfos = response.body().getData();
                                showList();
                            } else {
                                if (sendCallCollectionInfos == null || sendCallCollectionInfos.size() == 0) {
                                    ll_nodata.setVisibility(View.VISIBLE);
                                    lvListview.setVisibility(View.GONE);
                                }
                            }
                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<SendCallCollectionInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private PopupWindow popupWindo;

    public void showPopupWindow(String id) {
        if (popupWindo != null) {
            popupWindo = null;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.pw_dialog3, null);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindo.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindo.dismiss();
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
}

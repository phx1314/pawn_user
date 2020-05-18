package com.glavesoft.pawnuser.activity.appraisal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.SendCallGoodInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.lang.reflect.Type;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendCallOrderinfoActivity extends BaseActivity {

    @BindView(R.id.tv_order_code)
    TextView tvOrderCode;
    @BindView(R.id.tv_express_code)
    TextView tvExpressCode;
    @BindView(R.id.tv_express_state)
    TextView tvExpressState;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_daikuan_state)
    TextView tvDaikuanState;
    @BindView(R.id.tv_daikuan_price)
    TextView tvDaikuanPrice;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    private String sendCallId;
    private SendCallGoodInfo sendCallGoodInfo;
    private PopupWindow popupWindo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_call_orderinfo);
        ButterKnife.bind(this);
        sendCallId = getIntent().getStringExtra("id");
        initView();
        getSellGoodsDetail();
    }

    private void initView() {
        setTitleBack();
        setTitleName("已售");
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }
    public void showDialog()
    {
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(SendCallOrderinfoActivity.this);
        //    设置Title的图标
        //    设置Title的内容
        builder.setTitle("提示");
        //    设置Content来显示一个信息
        builder.setMessage("平台将收取鉴定价的5%作为手续费（包含了银行的结算费用，专家鉴定费等费用）。寄卖商品一律采取顺丰保价到付邮费。");
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
//                Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }
    private void showInfo() {
//        tvAuthPrice.setText("￥ "+sendCallGoodInfo.getAuthPrice());
//        tvAuthResult.setText(sendCallGoodInfo.getExperterInfo());
        tvOrderCode.setText(sendCallGoodInfo.getOrderCode());
        if(sendCallGoodInfo.getExpressCode().equals("")){
            tvExpressCode.setText("暂无物流信息");
            tvExpressState.setText("暂无物流信息");
        }
        else {
            tvExpressCode.setText(sendCallGoodInfo.getExpressCode());
            switch (sendCallGoodInfo.getExpressState()){
                case "0":
                    tvExpressState.setText("在途");
                    break;
                case "1":
                    tvExpressState.setText("已揽件");
                    break;
                case "2":
                    tvExpressState.setText("疑难");
                    break;
                case "3":
                    tvExpressState.setText("签收");
                    break;
                case "4":
                    tvExpressState.setText("退签或异常签收");
                    break;
                case "5":
                    tvExpressState.setText("派件");
                    break;
                case "6":
                    tvExpressState.setText("退回");
                    break;
            }
        }

        tvPrice.setText("￥"+sendCallGoodInfo.getOrderPrice());
        tvTime.setText(sendCallGoodInfo.getOrderPayTime()==null?null:sendCallGoodInfo.getOrderPayTime());
        if (sendCallGoodInfo.getSettleStatus().equals("0")){
            tvDaikuanState.setText("待结算");
        }else {
            tvDaikuanState.setText("已结算");
        }
        tvDaikuanPrice.setText("￥"+sendCallGoodInfo.getSettleMoney());
    }

    private void getSellGoodsDetail() {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/sellDetail");
        HttpParams param=new HttpParams();
        param.put("token", token);
        param.put("id", String.valueOf(sendCallId));
        OkGo.<DataResult<SendCallGoodInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<SendCallGoodInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<SendCallGoodInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null) {
                                sendCallGoodInfo = response.body().getData();
                                showInfo();
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<SendCallGoodInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

}

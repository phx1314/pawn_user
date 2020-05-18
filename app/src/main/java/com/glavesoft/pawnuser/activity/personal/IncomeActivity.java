package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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
import com.glavesoft.pawnuser.mod.MyPayeeListInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.DatePickerPopWin;
import com.glavesoft.view.OnTimeSetListener;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2017/11/17
 * @company:常州宝丰
 */
public class IncomeActivity extends BaseActivity implements View.OnClickListener,BGARefreshLayout.BGARefreshLayoutDelegate{
    TextView tv_startime_income,tv_endtime_income;
    LinearLayout ll_startime_income,ll_endtime_income;
    private DatePickerPopWin datePickerPopWin;
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<MyPayeeListInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        initView();
    }


    private void initView() {
        setTitleBack();
        setTitleName("资金明细");
        setTitleNameEn(R.mipmap.details_of_funds);

        tv_startime_income  = (TextView) findViewById(R.id.tv_startime_income);
        tv_endtime_income  = (TextView) findViewById(R.id.tv_endtime_income);
        ll_startime_income  = (LinearLayout) findViewById(R.id.ll_startime_income);
        ll_endtime_income  = (LinearLayout) findViewById(R.id.ll_endtime_income);
        ll_startime_income.setOnClickListener(this);
        ll_endtime_income.setOnClickListener(this);

        //当前日期
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        tv_startime_income.setText(date);
        tv_endtime_income.setText(date);

        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        getMyPayeeList();
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.ll_startime_income:
                goToChooseStarDate(tv_startime_income);
                break;
            case R.id.ll_endtime_income:
                goToChooseEndDate(tv_endtime_income);
                break;
        }
    }

    public Long getTime(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date;
        long l = 0;
        try {
            date = sdr.parse(time);
            l = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }


    protected void goToChooseStarDate(final TextView tv) {
        if(datePickerPopWin == null){
            datePickerPopWin = new DatePickerPopWin(this,0);
            datePickerPopWin.setDayView();
            datePickerPopWin.setAnimationStyle(R.style.popwin_anim_style);
        }
        datePickerPopWin.setOnTimeSetListener(new OnTimeSetListener() {
            public void onTimeSet(String text) {
                if(getTime(text)<=getTime(tv_endtime_income.getText().toString())){
                    tv.setText(text);
                    tv.setTextColor(getResources().getColor(R.color.black1));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16); //设置SP
                    resetPageData();
                }else{
                    CustomToast.show("起始时间不能大于结束时间");
                }
            }
        });
        datePickerPopWin.setOnDismissListener(dismissListener);
        setAlpha(0.5f);
        datePickerPopWin.showAtLocation(tv_startime_income, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    protected void goToChooseEndDate(final TextView tv) {
        if(datePickerPopWin == null){
            datePickerPopWin = new DatePickerPopWin(this,0);
            datePickerPopWin.setDayView();
            datePickerPopWin.setAnimationStyle(R.style.popwin_anim_style);
        }
        datePickerPopWin.setOnTimeSetListener(new OnTimeSetListener() {
            public void onTimeSet(String text) {
                if(getTime(text)>=getTime(tv_startime_income.getText().toString())){
                    tv.setText(text);
                    tv.setTextColor(getResources().getColor(R.color.black1));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16); //设置SP
                    resetPageData();
                }else{
                    CustomToast.show("结束时间不能小于起始时间");
                }
            }
        });
        datePickerPopWin.setOnDismissListener(dismissListener);
        setAlpha(0.5f);
        datePickerPopWin.showAtLocation(tv_endtime_income, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    protected PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setAlpha(1f);
        }
    };

    private void showList(ArrayList<MyPayeeListInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<MyPayeeListInfo>(IncomeActivity.this, result,
                    R.layout.item_income) {
                @Override
                public void convert(final ViewHolder helper, final MyPayeeListInfo item) {
                    //1典当 2续当 3赎当 4卖给平台 5 宝祥典当（宝祥兜底）
                    helper.getView(R.id.tv_no_income).setVisibility(View.VISIBLE);
                    if(item.getState().equals("1")){
                        helper.getView(R.id.tv_state_income).setBackgroundResource(R.drawable.shape_orange);
                        helper.setText(R.id.tv_state_income,"典当");
                        helper.setText(R.id.tv_money_income,"+￥"+item.getMoney());
                    }else if(item.getState().equals("2")){
                        helper.getView(R.id.tv_state_income).setBackgroundResource(R.drawable.shape_red1);
                        helper.setText(R.id.tv_state_income,"续当");
                        helper.setText(R.id.tv_money_income,"-￥"+item.getMoney());
                    }else if(item.getState().equals("3")){
                        helper.getView(R.id.tv_state_income).setBackgroundResource(R.drawable.shape_blue);
                        helper.setText(R.id.tv_state_income,"赎当");
                        helper.setText(R.id.tv_money_income,"-￥"+item.getMoney());
                    }else if(item.getState().equals("4")){
                        helper.getView(R.id.tv_state_income).setBackgroundResource(R.drawable.shape_red1);
                        helper.setText(R.id.tv_state_income,"卖给平台");
                        helper.setText(R.id.tv_money_income,"+￥"+item.getMoney());
                        helper.getView(R.id.tv_no_income).setVisibility(View.GONE);
                    }else if(item.getState().equals("5")){
                        helper.getView(R.id.tv_state_income).setBackgroundResource(R.drawable.shape_orange);
                        helper.setText(R.id.tv_state_income,"宝祥典当");
                        helper.setText(R.id.tv_money_income,"+￥"+item.getMoney());
                        helper.getView(R.id.tv_no_income).setVisibility(View.GONE);
                    }

                    helper.setText(R.id.tv_time_income,item.getTime());
                    helper.setText(R.id.tv_orgName_income,"相关机构："+item.getOrgName());
                    helper.setText(R.id.tv_phone_income,"电话："+item.getPhone());
                    helper.setText(R.id.tv_no_income,"当号："+item.getPawnCode());
                    helper.setText(R.id.tv_bankcard_income,"银行帐号：("+item.getBankName()+")  "+item.getBankCode());

                    helper.getView(R.id.tv_pz_income).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =new Intent();
                            intent.setClass(IncomeActivity.this,VoucherActivity.class);
                            intent.putExtra("ticket",item.getTicket());
                            startActivity(intent);
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
        }
    }

    private void resetPageData()
    {
        page = 1;
        list.clear();
        commAdapter = null;
        lv_listview.setAdapter(null);
        getMyPayeeList();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            getMyPayeeList();
        }else{
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }


    private void getMyPayeeList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/getMyPayeeList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<MyPayeeListInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<MyPayeeListInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<MyPayeeListInfo>>> response) {
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
                                showList(response.body().getData());
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<MyPayeeListInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

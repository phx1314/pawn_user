package com.glavesoft.pawnuser.activity.pawn;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.WebActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.BankCardInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.PawnDetailInfo;
import com.glavesoft.pawnuser.mod.PlatGetDetailInfo;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.Map;

/**
 * @author 严光
 * @date: 2017/11/20
 * @company:常州宝丰
 */
public class SubmitPlatActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_name_selectpawnshop,tv_jdprice_selectpawnshop,tv_qwdj_selectpawnshop,tv_time_selectpawnshop;
    private TextView tv_card_pawn;
    private CheckBox cb_pawn_agree;
    private TextView tv_pawn_agreement,tv_submit_pawn;
    private TextView tv_fa_submitpawn;

    private LinearLayout ll_pt_pawnshop,ll_select_selectpawnshop,ll_jj_selectpawnshop;
    private TextView tv_ptdj_selectpawnshop,tv_zhll_selectpawnshop,tv_lxlv_selectpawnshop;

    //private PawnDetailInfo pawnListInfo;
    private BankCardInfo bankCardInfo;
    private PlatGetDetailInfo platGetDetailInfo;

    private String pawnid="";
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitpawn);
        initData();
        initView();
    }

    private void initData() {
        pawnid=getIntent().getStringExtra("pawnid");
        //pawnListInfo=(PawnDetailInfo)getIntent().getSerializableExtra("pawnListInfo");
        bankCardInfo=(BankCardInfo)getIntent().getSerializableExtra("bankCardInfo");
        platGetDetailInfo=(PlatGetDetailInfo)getIntent().getSerializableExtra("platGetDetailInfo");
    }

    private void initView() {
        setTitleBack();
        setTitleName("确认典当信息");

        tv_name_selectpawnshop=(TextView) findViewById(R.id.tv_name_selectpawnshop);
        tv_jdprice_selectpawnshop=(TextView)findViewById(R.id.tv_jdprice_selectpawnshop);
        tv_qwdj_selectpawnshop=(TextView)findViewById(R.id.tv_qwdj_selectpawnshop);
        tv_time_selectpawnshop=(TextView)findViewById(R.id.tv_time_selectpawnshop);

        tv_fa_submitpawn=(TextView) findViewById(R.id.tv_fa_submitpawn);
        tv_name_selectpawnshop.setText(platGetDetailInfo.getTitle());
        tv_jdprice_selectpawnshop.setText("￥"+platGetDetailInfo.getAuthPrice());
        tv_qwdj_selectpawnshop.setText("￥"+platGetDetailInfo.getLoansPrice());

        if(!platGetDetailInfo.getPawnTime().equals("")){
            int days=Integer.valueOf(platGetDetailInfo.getPawnTime())*15;
            tv_time_selectpawnshop.setText(days+"天");
        }

        tv_card_pawn=(TextView) findViewById(R.id.tv_card_pawn);

        cb_pawn_agree=(CheckBox) findViewById(R.id.cb_pawn_agree);
        tv_pawn_agreement=(TextView) findViewById(R.id.tv_pawn_agreement);
        tv_submit_pawn=(TextView) findViewById(R.id.tv_submit_pawn);

        ll_pt_pawnshop=(LinearLayout) findViewById(R.id.ll_pt_pawnshop);
        ll_select_selectpawnshop=(LinearLayout) findViewById(R.id.ll_select_selectpawnshop);
        ll_jj_selectpawnshop=(LinearLayout) findViewById(R.id.ll_jj_selectpawnshop);

        tv_ptdj_selectpawnshop=(TextView)findViewById(R.id.tv_ptdj_selectpawnshop);
        tv_zhll_selectpawnshop=(TextView)findViewById(R.id.tv_zhll_selectpawnshop);
        tv_lxlv_selectpawnshop=(TextView)findViewById(R.id.tv_lxlv_selectpawnshop);

        ll_pt_pawnshop.setVisibility(View.VISIBLE);
        ll_select_selectpawnshop.setVisibility(View.GONE);
        ll_jj_selectpawnshop.setVisibility(View.GONE);

        tv_card_pawn.setText(bankCardInfo.getBankCardName()+"  "+bankCardInfo.getBankCardNo());

        tv_ptdj_selectpawnshop.setText("￥"+platGetDetailInfo.getBxMoney());
        tv_zhll_selectpawnshop.setText(platGetDetailInfo.getBxRate()+"%/月");
        tv_lxlv_selectpawnshop.setText(platGetDetailInfo.getBxMoneyRate()+"%/月");

        tv_submit_pawn.setOnClickListener(this);
        tv_pawn_agreement.setOnClickListener(this);

        fagui();//法规
    }

    private void fagui(){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String text1="根据《合同法》、";
        SpannableString sp1 = new SpannableString(text1);
        sp1.setSpan(new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SubmitPlatActivity.this,WebActivity.class);
                intent.putExtra("titleName","合同法");
                intent.putExtra("url", PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "htf",""));
                startActivity(intent);
            }
        }),2,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan redSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.red_k));
        sp1.setSpan(redSpan1, 2,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体的颜色

        String text2="《民事法》、";
        SpannableString sp2 = new SpannableString(text2);
        sp2.setSpan(new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SubmitPlatActivity.this,WebActivity.class);
                intent.putExtra("titleName","民事法");
                intent.putExtra("url", PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "mfzz",""));
                startActivity(intent);
            }
        }),0,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan redSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.red_k));
        sp2.setSpan(redSpan2, 0,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体的颜色

        String text3="《典当管理法》，我都已理解并认可了本合同的所有内容，同意承担各自应承担的权利和义务，忠实地履行本合同。";
        SpannableString sp3 = new SpannableString(text3);
        sp3.setSpan(new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SubmitPlatActivity.this,WebActivity.class);
                intent.putExtra("titleName","典当管理法");
                intent.putExtra("url", PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "ddglbf",""));
                startActivity(intent);
            }
        }),0,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan redSpan3 = new ForegroundColorSpan(getResources().getColor(R.color.red_k));
        sp3.setSpan(redSpan3, 0,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体的颜色

        builder.append(sp1);//添加
        builder.append(sp2);
        builder.append(sp3);

        tv_fa_submitpawn.setText(builder);
        tv_fa_submitpawn.setMovementMethod(LinkMovementMethod.getInstance());//加上这句话才有效果
    }

    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener mListener) {
            this.mListener = mListener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);    //去除超链接的下划线
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.tv_submit_pawn:
                if (!cb_pawn_agree.isChecked()) {
                    CustomToast.show("请先阅读并同意典当条款");
                    return;
                }
                platGet();
                break;
            case R.id.tv_pawn_agreement:

                break;
        }
    }

    //宝祥兜底提交
    private void platGet()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/platGet");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("bxId",platGetDetailInfo.getBxOrgId());
        param.put("id",pawnid);
        param.put("bankCardId",bankCardInfo.getId());
        OkGo.<DataResult<DataInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<DataInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            Intent intent = new Intent("submitpawn");
                            sendBroadcast(intent);
                            CustomToast.show("操作成功");
                            PawnDetailActivity.pawnDetailActivity.finish();
                            PlatformActivity.platformActivity.finish();
                            finish();
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private PopupWindow popupWindo;

    public void showPopupWindow()
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.pw_ticket, null);
        ImageView iv_pic_tiket = (ImageView)view.findViewById(R.id.iv_pic_tiket);
        TextView tv_no_tiket = (TextView)view.findViewById(R.id.tv_no_tiket);
        TextView tv_ok = (TextView)view.findViewById(R.id.tv_ok);

        tv_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
                PawnDetailActivity.pawnDetailActivity.finish();
                PlatformActivity.platformActivity.finish();
                finish();
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

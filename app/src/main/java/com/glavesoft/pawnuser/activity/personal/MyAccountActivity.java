package com.glavesoft.pawnuser.activity.personal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.alipay.AliPay;
import com.glavesoft.alipay.util.PayInfo;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.MyListView;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 个人中心-我的账户 
 * @author CHAOJAINGFEI
 */
public class MyAccountActivity extends BaseActivity {
	public static MyAccountActivity context;
	
	private GridView nsgv_account;
	private LinearLayout ll_account_youhui,ll_account_account;
	private MyListView mylv_account;
	private TextView tv_aacount_money,tv_account_vip,tv_myaccount_realyPrice;
	private EditText et_account_othermoney;
	private ImageView iv_account_account,iv_account_alipay,iv_account_wechat;
	private Button btn_account_confirm;
	private int payTool = -1;  //付款方式 1余额 2支付宝 3微信
	private String payNum="";//充值金额
	private String isVip="";
	private CommonAdapter priceAdapter;
	private String price="",orderId="",arrivalAmount="";
	boolean isclick=false;
	private ArrayList<String> pricelist=new ArrayList<>();
	private int selectpos=-1;
	CommonAdapter commAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myaccount);
		context = this;
		setView();
		setListener();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//GetAccountTask();//我的账户余额接口
	}
	

	
	private void setView (){
		setTitleName("我的账户");
		setTitleBack();

		tv_aacount_money=(TextView) findViewById(R.id.tv_aacount_money);
		iv_account_account=(ImageView) findViewById(R.id.iv_account_account);
		iv_account_alipay=(ImageView) findViewById(R.id.iv_account_alipay);
		iv_account_wechat=(ImageView) findViewById(R.id.iv_account_wechat);
		btn_account_confirm=(Button) findViewById(R.id.btn_account_confirm);
		nsgv_account=(GridView) findViewById(R.id.nsgv_account);
		ll_account_youhui=(LinearLayout) findViewById(R.id.ll_account_youhui);
		mylv_account=(MyListView) findViewById(R.id.mylv_account);
		tv_account_vip=(TextView) findViewById(R.id.tv_account_vip);
		tv_myaccount_realyPrice=(TextView) findViewById(R.id.tv_myaccount_realyPrice);
		ll_account_account=(LinearLayout) findViewById(R.id.ll_account_account);
		
		et_account_othermoney=(EditText) findViewById(R.id.et_account_othermoney);
		et_account_othermoney.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		et_account_othermoney.setSingleLine(true);

		pricelist.add("100");
		pricelist.add("200");
		pricelist.add("500");
		setview();
	}
	
	private void setListener() {
		iv_account_account.setOnClickListener(onClickListener);
		iv_account_alipay.setOnClickListener(onClickListener);
		btn_account_confirm.setOnClickListener(onClickListener);
		iv_account_wechat.setOnClickListener(onClickListener);

		nsgv_account.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
//				selectpos=position;
//				priceAdapter.notifyDataSetChanged();
//				if (priceList.get(position).getCondition()!=null&&!priceList.get(position).getCondition().equals("0")) {
//					isclick=true;
//					et_account_othermoney.setText("");
//					price=priceList.get(position).getCondition();
//				}
//
			}
		});

		et_account_othermoney.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {

				if(isclick){
					isclick=false;
				}else{
					selectpos=-1;
					commAdapter.notifyDataSetChanged();
					if (!et_account_othermoney.getText().toString().trim().equals("")) {
						price = et_account_othermoney.getText().toString().trim();

					} else {
						price = "";
					}
				}


			}
		});
	}

	
	OnClickListener onClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.titlebar_right://设置密码
					//1代表已经设置了余额支付密码，在用余额支付的时候不用再设置了，0代表没有设置，付款的时候需要先去设置密码
//				Intent intent=null;
//				if (info.getSet_pwd().equals("1")) {
//					intent=new Intent(MyAccountActivity.this, ChangePayPasswordActivity.class);
//					startActivity(intent);
//				}else if (info.getSet_pwd().equals("0")) {
//					intent=new Intent(MyAccountActivity.this, SetPayPasswordActivity.class);
//					startActivity(intent);
//				}
					break;

				case R.id.iv_account_wechat:////微信支付
					payTool = 2;
					iv_account_alipay.setImageResource(R.drawable.xk_);
					iv_account_account.setImageResource(R.drawable.xk_);
					iv_account_wechat.setImageResource(R.drawable.xk);
					break;

				case R.id.iv_account_alipay://支付宝支付
					payTool = 1;
					iv_account_alipay.setImageResource(R.drawable.xk);
					iv_account_account.setImageResource(R.drawable.xk_);
					iv_account_wechat.setImageResource(R.drawable.xk_);
					break;

				case R.id.iv_account_account://账户余额支付
					payTool = 1;
					iv_account_account.setImageResource(R.drawable.xk);
					iv_account_alipay.setImageResource(R.drawable.xk_);
					iv_account_wechat.setImageResource(R.drawable.xk_);
					break;

				case R.id.btn_account_confirm://确认充值
					goToPay();
					break;
			}
		}

	};
	
	
	private void goToPay(){
		if (et_account_othermoney.getText().toString().length()!=0) {
			price=et_account_othermoney.getText().toString().trim();
		}
		if (price.length()==0||price.equals("")) {
			CustomToast.show("充值金额不能为空！");
		}
//		else if (Double.valueOf(price)<2000) {
//			CustomToast.show("充值金额不能为低于2000！");
//		}
		else if (payTool<0) {
			CustomToast.show("请选择充值方式！");
		}else {
			recharge();//充值生成订单接口
		}
	}

	private void setview(){
		commAdapter = new CommonAdapter<String>(MyAccountActivity.this, pricelist,
				R.layout.item_account) {
			@Override
			public void convert(final ViewHolder helper, final String item) {
				helper.setText(R.id.tv_price_recharge,item+"元");
				if(selectpos==helper.getPosition()){
					helper.getView(R.id.tv_price_recharge).setBackgroundResource(R.drawable.shape_price_blue_kuang);
					helper.setTextcolor(R.id.tv_price_recharge,getResources().getColor(R.color.blue));
				}else{
					helper.getView(R.id.tv_price_recharge).setBackgroundResource(R.drawable.shape_price_hui_kuang);
					helper.setTextcolor(R.id.tv_price_recharge,getResources().getColor(R.color.text_gray));
				}

				helper.getView(R.id.tv_price_recharge).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						isclick=true;
						selectpos=helper.getPosition();
						price=item;
						notifyDataSetChanged();
						et_account_othermoney.setText("");
					}
				});
			}
		};
		nsgv_account.setAdapter(commAdapter);
	}


	private void recharge()
	{
		getlDialog().show();
		Map<String, String> param = new HashMap<>();
		param.put("token", LocalData.getInstance().getUserInfo().getToken());
		param.put("platform", payTool+"");
		param.put("money", price);
		java.lang.reflect.Type classtype = new TypeToken<DataResult<PayInfo>>() {}.getType();
		String url = BaseConstant.getApiPostUrl("/api/pay/recharge");
		VolleyUtil.postObjectApi(url, param, classtype, new ResponseListener<DataResult<PayInfo>>()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				getlDialog().dismiss();
				showVolleyError(error);
			}

			@Override
			public void onResponse(DataResult<PayInfo> response)
			{
				getlDialog().dismiss();
				if (response == null)
				{
					CustomToast.show(getString(R.string.http_request_fail));
					return;
				}
				if (DataResult.RESULT_OK_ZERO == response.getErrorCode())
				{
					if(response.getData()!=null){
						if(payTool==1){
							AliPay(response.getData());
						}
					}

				}else if (DataResult.RESULT_102 == response.getErrorCode())
				{
					showPopupWindow(MyAccountActivity.this, getResources().getString(R.string.toast_login), new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if(getPopupWindow()!=null){
								getPopupWindow().dismiss();
							}
							toLogin();
						}
					});

				}else
				{
					CustomToast.show(response.getErrorMsg());
				}
			}
		});
	}

	
	/**
	 * 支付宝支付 
	 */
	private void AliPay(PayInfo payinfo){
		AliPay aliPay=new AliPay(MyAccountActivity.this, new AliPay.CallBack() {
			@Override
			public void success() {
				CustomToast.show("支付成功");
				finish();
			}
			
			@Override
			public void fail() {
			}
		});
		System.out.println("支付宝支付==orderId==>"+orderId+"price==>"+price);
		aliPay.payV2(payinfo);
	}
	
	
//	private void paySuc(){
//		orderPaySuc();
//		Intent intent = new Intent();
//		intent.setClass(context, RechargeableSussessActivity.class);
//		startActivity(intent);
//		finish();
//	}
	
	/**
	 * 微信支付
	 */
//	private void goToWXPay(){
//		if(!CommonUtils.isWechatInstalled(context)){
//			CustomToast.show("未检测到微信客户端，请先安装");
//			return;
//		}
//
//		/**
//		 * 微信统一下单接口
//		 */
//		getlDialog().show();
//		HashMap<String, String>  map=new HashMap<String, String>();
//		map.put("type", "2");//入口 类型 1订单 2充值
//		map.put("order_code", orderId);//提交订单或者充值接口返回的data或者订单列表中的order_code
//		map.put("token", LocalData.getInstance().getUserInfo().getToken());//用户token
//		System.out.println("MyAccountActivity---微信统一下单接口===>"+map);
//		OkHttpClientManager.postAsyn(ApiConfig.PayUnifiedOrder, new OkHttpClientManager.ResultCallback<DataResult<WXPayInfo>>() {
//			@Override
//			public void onBefore(Request response) {
//			}
//			@Override
//			public void onAfter(String json) {
//			    getlDialog().dismiss();
//			}
//			@Override
//			public void onError(Request request, Exception e) {
//				getlDialog().dismiss();
//			}
//			@Override
//			public void onResponse(DataResult<WXPayInfo> response) {
//				getlDialog().dismiss();
//				if (response == null){
//					CustomToast.show("数据解析失败，请重试");
//					return;
//				}
//
//				if (response.getStatus().equals(DataResult.RESULT_OK)){
//					WXPayInfo info = response.getData();
//					if (info != null){
//						WXPay.pay(context, info);
//					}
//				} else{
//					CustomToast.show(response.getMessage());
//				}
//
//			}
//		}, map);
//
//	}
	


}

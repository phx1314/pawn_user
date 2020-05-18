package com.glavesoft.pawnuser.wxapi;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);

		setTitleBack();
		setTitleName("支付成功");

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req){
	}

	@Override
	public void onResp(BaseResp resp){
		Log.d("WXPayEntryActivity", "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
			switch (resp.errCode){
			case BaseResp.ErrCode.ERR_OK:
				CustomToast.show("支付成功");
				if(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Pay,"").equals("submitbuy")){
					Intent intent=new Intent("wxpay_buy");
					sendBroadcast(intent);
				}else{
					Intent intent=new Intent("order_buy");
					intent.putExtra("state","success");
					sendBroadcast(intent);
				}
				finish();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				if(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Pay,"").equals("submitbuy")){
					Intent intent=new Intent("wxpay_buy");
					sendBroadcast(intent);
				}else{
					Intent intent=new Intent("order_buy");
					intent.putExtra("state","cancel");
					sendBroadcast(intent);
				}
				CustomToast.show("支付取消");
				finish();
				break;
			case BaseResp.ErrCode.ERR_COMM:
				if(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Pay,"").equals("submitbuy")){
					Intent intent=new Intent("wxpay_buy");
					sendBroadcast(intent);
				}else{
					Intent intent=new Intent("order_buy");
					intent.putExtra("state","fail");
					sendBroadcast(intent);
				}
				CustomToast.show("支付失败，请检查微信帐号是否登录过期");
				finish();
				break;
			}
		}
	}
	
//	private void showFail()
//	{
//		((TextView) findViewById(R.id.s_payresult_tv_txt)).setText("抱歉，支付失败！");
//
//		Button btn_check = ((Button) findViewById(R.id.s_payresult_btn_check));
//		btn_check.setText("返回");
//		btn_check.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				finish();
//			}
//		});
//	}
	
//	private void orderPaySuc(){		
//		java.lang.reflect.Type classtype = new TypeToken<DataResult<String>>()
//		{
//		}.getType();
//
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("token", LocalData.getInstance().getUserInfo().getToken());
//		param.put("order_id", PayActivity.orderId);
//
//		VolleyUtil.postObjectApi(BaseConfig.getMethodUrl("order/order-paid"), param, classtype, new ResponseListener<DataResult<String>>()
//		{
//			@Override
//			public void onErrorResponse(VolleyError error)
//			{
//				//showVolleyError(error);
//			}
//
//			@Override
//			public void onResponse(DataResult<String> response)
//			{				
//				if (response == null)
//				{
//					return;
//				}
//
//				if (DataResult.RESULT_OK == response.getStatus())
//				{
//
//				} else
//				{
//					//CustomToast.show(response.getMsg());
//				}
//			}
//		});
//	}
}
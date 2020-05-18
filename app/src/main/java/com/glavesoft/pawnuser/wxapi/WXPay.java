package com.glavesoft.pawnuser.wxapi;


import java.util.Locale;
import java.util.Random;

import com.glavesoft.pawnuser.mod.WXPayInfo;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.content.Context;

public class WXPay{
	public static void pay(Context context, String prepayId){
		IWXAPI api = WXAPIFactory.createWXAPI(context, null);
		api.registerApp(Constants.APP_ID);

		PayReq req = new PayReq();
		req.appId = Constants.APP_ID;
		//req.nonceStr = info.getNonce_str();
		req.nonceStr = genNonceStr();
		req.packageValue = "Sign=WXPay";
		req.partnerId = Constants.MCH_ID;
		req.prepayId = prepayId;
		req.timeStamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
		
		//System.out.println("info=====>"+info);

		String txt = "appid=" + Constants.APP_ID + "&noncestr=" + req.nonceStr + "&package=" + req.packageValue + "&partnerid=" + req.partnerId + "&prepayid=" + req.prepayId + "&timestamp="
				+ req.timeStamp + "&key=" + Constants.KEY;
		


		req.sign = MD5.getMessageDigest(txt.getBytes()).toUpperCase(Locale.getDefault());
		System.out.println("req.sign=====>"+ MD5.getMessageDigest(txt.getBytes()).toUpperCase(Locale.getDefault()));

		api.sendReq(req);
	}

	public static String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

}

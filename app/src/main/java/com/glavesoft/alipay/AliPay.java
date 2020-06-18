package com.glavesoft.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.glavesoft.alipay.util.OrderInfoUtil2_0;
import com.glavesoft.alipay.util.PayInfo;
import com.glavesoft.view.CustomToast;

import java.util.Map;

/**
 *  重要说明:
 *  
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
 */
public class AliPay {
	private Activity _context;
	private CallBack callBack;
	/** 支付宝支付业务：入参app_id */
	public static final String APPID = "2017102509511539";
	
	/** 支付宝账户登录授权业务：入参pid值 */
	public static final String PID = "2088621242457555";
	/** 支付宝账户登录授权业务：入参target_id值 */
	public static final String TARGET_ID = "15161166327@163.com";

	/** 商户私钥，pkcs8格式 */
	/** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
	/** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
	/** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
	/** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
	/** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
	public static final String RSA_PRIVATE ="";
	public static final String RSA2_PRIVATE="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCga6Maj9KNrtnydLqGylVDOoZ7GWE+ezAGP1/5mU6FC4QYL99yDRmCTByDF1jPt5cQPs/v0+P+8MhTwup4JoLWHr4L10NhMu4RzXdA4y3NfXpOI8XCIMpVQMOr9tfeGJmkvm6O5x391Sd4NcMRv19JkftxXM/rB+FDa3PCDYhc/suqUA48k4+tktz7ig5zIEZl06ZLqkunTpDZ7F6dkeTIOGldnGF6/vHHUpqG1RYbxqxITwecRq9mvX/k1xUsdhrD4igS/0QZhN2jCHz5X41YB3ewxB8HEKOMGAXMZ4qp6sU54HioA1jaSAXtbUUU/tdQm0tPDEM3ADzmMiSJW2B7AgMBAAECggEAc8hcRz6IxFPv4AuZ2NFOKqCiEVTnF9aHeerh1V9uCZ6fL2nWBxYnVCCk74S5R+qE8Ge1Yq53PzkADVyAKEn7Ypl7p0YtzoiYxWabbMzK8SZgUOeGwy8xecitsX8OnLD6q6n6V8+9mMfwndWHvlZ2ar4fUdmgA02Dq0pxmRx1UFA9mwQbnKXVyvhIcKe2mWW++vAN+u5GrbrdOJXd0faEz36QnNFEVuhwpHRXMJWYB9n2p+dEJ88F2EZWPjbF48HTDMONH67aTAr+HbkWkrea221jHj8pmUru/XIvrab0ifwXLVpPeVZhUmLfCTHv5LbRoVjtb8fQ2ddosrhItxrCeQKBgQDMOMjl4ecZ5wEvsKju8cgYLOtQGbUlslTbmAes5S0A6EAzkafT9YJbprsuPCOUJTlBdOHtBn71cvrxcypcpgHXq2L/zNPtiZazQX7k53rGmugkKq+X2xbr+/jyplzJBNeZjeed9eLoTFFk2AxsRHUv/QITCH34v0ulMtTZqEk5FQKBgQDJF+H4VsTjW0Gu47DHx3ZeEqNZNxekWinVR1UCpMXyiUwiv9EFbpmSJ8YEo4NouU2cWQaFjOR43/pEKiy6dbtu0Tco/h4UFz5H3M2uwjhwihAWjtG8zyHxqm3LN3RzAFCbwUQo62NV7ZQp6yGYPR4md8PPJtFPvw1ojgKHRtJ3TwKBgDZG2U4PriY6ORx9rUhkRjP2rEvLqVNthBQg4dlXx5A4KegKzDoPRCgrtg/ht+sFwaPd9rXSRifh+UP2wcmB7Xdue49U1vaI3N6jh6A6zWy/evcxyR1pAzyQ6WdEQ+DBQrPy/R4EXdwzfGZ67UJRJ3wrmEOKvEBCR06Opfd7PIrJAoGBAI32WEZzKwe+Ur5ea1x+CagbzEmdRQl3xXSVqQP2wu+X0bHubGdDTxMJFRgNPyXdO1cW2i3J6zkSFaGtjGoN10ZCWIiN7ToNtqAQ1vt6jVGVhgoGxTZfm/z5bg06lvDg1gc/BxlEI5pz4Fc/cTYCX5iUkB1bsAR2QccACfTqIIatAoGAA1ZFTbiDvGCe+Ps1C8+Vti6pSTNQxuZCH59VaC2cVXZDguxkr6cIWlE+HpJbHJfZh2LZ97DbLy0/YGfmzhrHtdT1CGb8vR/bgh7V82ExLdckHdNgbklWfe0jV0V8JfvqFQ5WibXHAbWdfqTsjDYrDVURgTfMi+Cu+aaEHaliR+4=";
	public static final String RSA2_PUBLIC="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgFLwC1RfE3Pj9GhpygGYzhAt4c6mpBWrObzJLCg0auxWNxJC5d6qIxRpkuYYDeE8c1h3wtAAldu7qvGkj90QVpwRVU1GMlDpLhEkyhD59LZUNuSqaZfr4Dw/jteOLtM/tK9Ai05fAXID9U92+PwuenguZ2BH/MhAzClnvFg/w+wngIno8iVTmrAHD7zk6GxPnNVby0gAV3+7z8TQqclVg1wv9KDubxgRMc1uA0lvUuIRH/AEFL9Zw1ehR9enmi1afLRU6u+8ohh8EC9/w/hvLt9lL3+/Q3pMRO1fgiYYhC2SnBgXbEtrGr1+9VLeX54uwbnIVjyZIA4Y0wnWl8l0RQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;

	public AliPay(Activity context, CallBack callBack){
		this._context = context;
		this.callBack = callBack;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				/**
				 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为9000则代表支付成功
				if (TextUtils.equals(resultStatus, "9000")) {
					// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
					//ToastUtils.showShort("支付成功");
					callBack.success();
				} else {
					// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
					CustomToast.show("支付失败");
					callBack.fail();
				}
				break;
			}
			case SDK_AUTH_FLAG: {
				@SuppressWarnings("unchecked")
				AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
				String resultStatus = authResult.getResultStatus();

				// 判断resultStatus 为“9000”且result_code
				// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
				if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
					// 获取alipay_open_id，调支付时作为参数extern_token 的value
					// 传入，则支付账户为该授权账户
					ToastUtils.showShort("授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
				} else {
					// 其他状态值则为授权失败
					ToastUtils.showShort("授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
				}
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * 支付宝支付业务
	 * 
	 * @param payinfo
	 */
	public void payV2(PayInfo payinfo) {
		if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
			new AlertDialog.Builder(_context).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							_context.finish();
						}
					}).show();
			return;
		}
		if (ObjectUtils.isEmpty(payinfo)){
			ToastUtils.showShort("订单信息有误");
			return;
		}
	
		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * orderInfo的获取必须来自服务端；
		 */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
		Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID,payinfo,rsa2,"蚌蚌拍当订单");
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

		String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
		String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
		final String orderInfo = orderParam + "&" + sign;

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(_context);
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Log.i("msp", result.toString());
				
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 支付宝账户授权业务
	 * 
	 * @param v
	 */
	public void authV2(View v) {
		if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
				|| (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
				|| TextUtils.isEmpty(TARGET_ID)) {
			new AlertDialog.Builder(_context).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							_context.finish();
						}
					}).show();
			return;
		}

		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * authInfo的获取必须来自服务端；
		 */
		boolean rsa2 = (RSA2_PRIVATE.length() > 0);
		Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
		String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
		
		String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
		String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
		final String authInfo = info + "&" + sign;
		Runnable authRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造AuthTask 对象
				AuthTask authTask = new AuthTask(_context);
				// 调用授权接口，获取授权结果
				Map<String, String> result = authTask.authV2(authInfo, true);

				Message msg = new Message();
				msg.what = SDK_AUTH_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread authThread = new Thread(authRunnable);
		authThread.start();
	}
	
	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(_context);
		String version = payTask.getVersion();
		Toast.makeText(_context, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	 * 
	 * @param v
	 */
	public void h5Pay(View v) {
		Intent intent = new Intent(_context, H5PayDemoActivity.class);
		Bundle extras = new Bundle();
		/**
		 * url 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
		 *
		 * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
		 * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
		 * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
		 * 进行测试。
		 * 
		 * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
		 * 可以参考它实现自定义的 URL 拦截逻辑。
		 */
		String url = "http://m.taobao.com";
		extras.putString("url", url);
		intent.putExtras(extras);
		_context.startActivity(intent);
	}

	public interface CallBack{
		public void success();

		public void fail();
	}

}

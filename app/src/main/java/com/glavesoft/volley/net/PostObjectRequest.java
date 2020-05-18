package com.glavesoft.volley.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.glavesoft.pawnuser.base.Base64;
import com.glavesoft.pawnuser.base.DES;
import com.glavesoft.pawnuser.base.RSAUtils;
import com.glavesoft.util.CommonUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

public class PostObjectRequest<T> extends Request<T>
{
	private ResponseListener<T> mListener;

	private Type mClazz;
	private Map<String, String> mParams;
	private String mUrl = "", mPostStr = "";
	private boolean isStringPost;

	public PostObjectRequest(String url, Map<String, String> params, Type type, ResponseListener<T> listener)
	{
		super(Method.POST, url, listener);

		mListener = listener;
		mClazz = type;
		mUrl = url;
		mParams = params;
		isStringPost = false;

		setShouldCache(false);
	}

	public PostObjectRequest(String url, String postStr, Type type, ResponseListener<T> listener)
	{
		super(Method.POST, url, listener);

		mListener = listener;
		mClazz = type;
		mUrl = url;
		mPostStr = postStr;
		isStringPost = true;

		setShouldCache(false);
	}

	/**
	 * 解析数据
	 */
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			showResponse(jsonString);
			String jsonString1="";
			try {
				//jsonString1 = DES.decrypt(jsonString);

				jsonString1 =  new String(RSAUtils.decryptByPublicKey( Base64.decode(jsonString),RSAUtils.PUBLIC_KEY),"UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showResponse(jsonString1);



			T result = CommonUtils.fromJson(jsonString1, mClazz, CommonUtils.DEFAULT_DATE_PATTERN);

			return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));

		} catch (UnsupportedEncodingException e)
		{
			return Response.error(new ParseError(e));
		}
	}

	/**
	 * 请求结果后的回调
	 */
	@Override
	protected void deliverResponse(T response)
	{
		mListener.onResponse(response);
	}

	/**
	 * 父类获取post数据的回调
	 */
	@Override
	protected Map<String, String> getParams() throws AuthFailureError
	{
		if(!isStringPost)
		{
			return mParams;
		}
		else
		{
			return super.getParams();
		}
	}

	/**
	 * 父类获取post数据的回调
	 */
	@Override
	public byte[] getBody() throws AuthFailureError
	{
		if(isStringPost)
		{
			showRequest(mUrl + "?" + mPostStr);

			return  mPostStr.getBytes();
		}
		else
		{
			showRequest(mUrl + "?" + new String(super.getBody()));

			return super.getBody();
		}
	}

	// 打印发送请求数据
	private void showRequest(String sendStr)
	{
		System.out.println("Request：" + sendStr);
	}

	// 打印接受请求数据
	private void showResponse(String retStr)
	{
		System.out.println("Response：" + retStr);
	}
}

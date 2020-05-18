package com.glavesoft.volley.net;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.glavesoft.util.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GetObjectRequest<T> extends Request<T>{

	/**
	 * 正确数据的时候回掉用
	 */
	private ResponseListener mListener;
	/* 用来解析 json 用的 */
	private Gson mGson;
	/* 在用 gson 解析 json 数据的时候，需要用到这个参数 */
	private Type mClazz;
	private String urlGet = "";

	public GetObjectRequest(String url, Type clazz, ResponseListener listener){
		super(Method.GET, url, listener);
		this.mListener = listener;
		mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		mClazz = clazz;
		urlGet = url;
		Log.d("GetObjectRequest", "url-->" + url);
		setShouldCache(false);
	}

	/**
	 * 这里开始解析数据
	 * @param response Response from the network
	 * @return
	 */
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response){
		try{
			Log.d("GetObjectRequest", "这里开始解析数据");
			T result = null;
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			System.out.println("GetResult===" + jsonString);

			Log.d("GetObjectRequest", "response.headers-->"+response.headers.size());
			result = CommonUtils.fromJson(jsonString, mClazz, CommonUtils.DEFAULT_DATE_PATTERN);
			//result = mGson.fromJson(jsonString, mClazz);

//			if (urlGet.contains("http://push.glavesoft.com/awqc/index.php?c=mobile_api&a=getcode")){
				checkSessionCookie(response.headers);
//			}
			return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e){
			return Response.error(new ParseError(e));
		}
	}

	/**
	 * 回调正确的数据
	 * @param response The parsed response returned by
	 */
	@Override
	protected void deliverResponse(T response){
		mListener.onResponse(response);
	}

	public Map<String, String> getHeaders() throws AuthFailureError{
		Map<String, String> headers = super.getHeaders();

		Log.d("GetObjectRequest", "getHeaders()-->"+headers.size());

		if (headers == null || headers.equals(Collections.emptyMap())){
			headers = new HashMap<String, String>();
		}

		return headers;
	}

	private static final String SET_COOKIE_KEY = "Set-Cookie";
	private static final String COOKIE_KEY = "Cookie";
	private static final String SESSION_COOKIE = "sessionid";

	/**
	 * Checks the response headers for session cookie and saves it if it finds it.
	 * @param headers Response Headers.
	 */
	public final void checkSessionCookie(Map<String, String> headers){
		Log.d("GetObjectRequest", "checkSessionCookie");
		if (headers.containsKey(SET_COOKIE_KEY)){
			String cookie = headers.get(SET_COOKIE_KEY);
			if (cookie.length() > 0){
				/*
				 * String[] splitCookie = cookie.split(";"); String[]
				 * splitSessionId = splitCookie[0].split("="); cookie =
				 * splitSessionId[1];
				 */
				//PreferencesUtils.setStringPreferences(CommonUtils.SETTING_NAME, CommonUtils.SP_COOKIES, cookie);
				Log.d("checkSessionCookie", "cookie-->" + cookie);
			}
		}
	}

	/**
	 * Adds session cookie to headers if exists.
	 *
	 * @param headers
	 */
	public final void addSessionCookie(Map<String, String> headers){
		//String cookie = PreferencesUtils.getStringPreferences(CommonUtils.SETTING_NAME, CommonUtils.SP_COOKIES, "");
		String cookie = "";
		Log.d("GetObjectRequest", "addSessionCookie-->cookie" + cookie);
		if (cookie.length() > 0){
			/*
			 * StringBuilder builder = new StringBuilder();
			 * //builder.append(SESSION_COOKIE); //builder.append("=");
			 * builder.append(cookie); if (headers.containsKey(COOKIE_KEY)) {
			 * builder.append("; "); builder.append(headers.get(COOKIE_KEY)); }
			 * headers.put(COOKIE_KEY, builder.toString());
			 */
			headers.put(COOKIE_KEY, cookie);
		}
	}

}

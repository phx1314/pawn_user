package com.glavesoft.volley.net;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.volley.form.FormImage;
import com.glavesoft.volley.form.FormText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by gyzhong on 15/3/1.
 */
public class MultiPostRequest<T> extends Request<T>{

	/**
     * 正确数据的时候回掉用
     */
    private ResponseListener mListener ;
    /*请求 数据通过参数的形式传入*/
    private List<FormText> mListTextItem ;
    private List<FormImage> mListImageItem ;
    private Type mClazz;

    private String BOUNDARY = "--------------520-13-14"; //数据分隔线
    private String MULTIPART_FORM_DATA = "multipart/form-data";

    public MultiPostRequest(String url, List<FormText> listTextItem, List<FormImage> listImageItem, Type clazz, ResponseListener listener) {
        super(Method.POST, url, listener);
        this.mListener = listener ;
        setShouldCache(false);
        mListTextItem = listTextItem ;
        mListImageItem = listImageItem ;
        mClazz = clazz;
        setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    protected Response<T> parseNetworkResponse(NetworkResponse response){
		try{
			Log.d("MultiPostRequest", "这里开始解析数据");
			T result = null;
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			System.out.println("GetResult===" + jsonString);

			Log.d("MultiPostRequest", "response.headers-->"+response.headers.size());
			result = CommonUtils.fromJson(jsonString, mClazz, CommonUtils.DEFAULT_DATE_PATTERN);
			//result = mGson.fromJson(jsonString, mClazz);

			checkSessionCookie(response.headers);
			return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e)
		{
			return Response.error(new ParseError(e));
		}
	}

    public Map<String, String> getHeaders() throws AuthFailureError{

		Map<String, String> headers = super.getHeaders();
		Log.d("MultiPostRequest", "getHeaders()-->"+headers.size());

		if (headers == null || headers.equals(Collections.emptyMap())){
			headers = new HashMap<String, String>();
		}

		return headers;
	}

	private static final String SET_COOKIE_KEY = "Set-Cookie";
	private static final String COOKIE_KEY = "Cookie";
	private static final String SESSION_COOKIE = "sessionid";

	/**
	 * Checks the response headers for session cookie and saves it if it finds
	 * it.
	 *
	 * @param headers
	 *            Response Headers.
	 */
	public final void checkSessionCookie(Map<String, String> headers)
	{
		Log.d("MultiPostRequest", "checkSessionCookie");
		if (headers.containsKey(SET_COOKIE_KEY))
		{
			String cookie = headers.get(SET_COOKIE_KEY);
			if (cookie.length() > 0)
			{
				/*
				 * String[] splitCookie = cookie.split(";"); String[]
				 * splitSessionId = splitCookie[0].split("="); cookie =
				 * splitSessionId[1];
				 */
				//PreferencesUtils.setStringPreferences(CommonUtils.SETTING_NAME, CommonUtils.SP_COOKIES, cookie);
				Log.d("MultiPostRequest", "cookie-->" + cookie);
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
		Log.d("MultiPostRequest", "addSessionCookie-->cookie" + cookie);
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

    @Override
    public byte[] getBody() throws AuthFailureError {
        if ((mListTextItem == null || mListTextItem.size() == 0)
        		&& (mListImageItem == null || mListImageItem.size() == 0)){
            return super.getBody() ;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        int M = mListTextItem.size() ;
        FormText formText ;
        for (int i = 0; i < M ;i++){
            formText = mListTextItem.get(i) ;
            StringBuffer sb= new StringBuffer() ;
            /*第一行*/
            sb.append("--"+BOUNDARY);
            sb.append("\r\n") ;
            /*第二行*/
            sb.append("Content-Disposition: form-data;");
            sb.append(" name=\"");
            sb.append(formText.getName()) ;
            sb.append("\"") ;
            sb.append("\r\n") ;
            /*第三行*/
            sb.append("\r\n") ;
            /*第四行*/
            sb.append(formText.getValue()) ;
            sb.append("\r\n") ;
            try {
                bos.write(sb.toString().getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int N = mListImageItem.size() ;
        FormImage formImage ;
        for (int i = 0; i < N ;i++){
            formImage = mListImageItem.get(i) ;
            StringBuffer sb= new StringBuffer() ;
            /*第一行*/
            sb.append("--"+BOUNDARY);
            sb.append("\r\n") ;
            /*第二行*/
            sb.append("Content-Disposition: form-data;");
            sb.append(" name=\"");
            sb.append(formImage.getName()) ;
            sb.append("\"") ;
            sb.append("; filename=\"") ;
            sb.append(formImage.getFileName()) ;
            sb.append("\"");
            sb.append("\r\n") ;
            /*第三行*/
            sb.append("Content-Type: ");
            sb.append(formImage.getMime()) ;
            sb.append("\r\n") ;
            /*第四行*/
            sb.append("\r\n") ;
            try {
                bos.write(sb.toString().getBytes("utf-8"));
                /*第五行*/
                bos.write(formImage.getValue());
                bos.write("\r\n".getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*结尾行*/
        String endLine = "--" + BOUNDARY + "--" + "\r\n" ;
        try {
            bos.write(endLine.toString().getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("zgy","=====content====\n"+bos.toString()) ;
        return bos.toByteArray();
    }

    @Override
    public String getBodyContentType() {
        return MULTIPART_FORM_DATA+"; boundary="+BOUNDARY;
    }

	@Override
	protected void deliverResponse(T response) {
		 mListener.onResponse(response);
	}
}

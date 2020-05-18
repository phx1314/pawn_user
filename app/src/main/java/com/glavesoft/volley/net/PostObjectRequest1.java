package com.glavesoft.volley.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.glavesoft.util.CommonUtils;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;


/**
 * @author 严光
 * @date: 2017/9/11
 * @company:常州宝丰
 */
public class PostObjectRequest1  extends Request<String>{
    private ResponseListener mListener;

    private Type mClazz;
    private Map<String, String> mParams;
    private String mUrl = "", mPostStr = "";
    private boolean isStringPost;

    public PostObjectRequest1(String url, Map<String, String> params, Type type, ResponseListener listener)
    {
        super(Request.Method.POST, url, listener);

        mListener = listener;
        mClazz = type;
        mUrl = url;
        mParams = params;
        isStringPost = false;

        setShouldCache(false);
    }

    public PostObjectRequest1(String url, String postStr, Type type, ResponseListener listener)
    {
        super(Request.Method.POST, url, listener);

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
    protected Response parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            showResponse(jsonString);
//
//            T result = jsonString;

            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 请求结果后的回调
     */
    @Override
    protected void deliverResponse(String response)
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

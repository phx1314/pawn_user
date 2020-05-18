package com.glavesoft.volley.net;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.glavesoft.volley.form.FormImage;
import com.glavesoft.volley.form.FormText;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyUtil
{

    private static RequestQueue mRequestQueue;

    public static synchronized void initialize(Context context)
    {
        if (mRequestQueue == null)
        {
            synchronized (VolleyUtil.class)
            {
                if (mRequestQueue == null)
                {
                    mRequestQueue = Volley.newRequestQueue(context);
                }
            }
        }
        // mRequestQueue.start();
    }

    public static RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
            throw new RuntimeException("请先初始化mRequestQueue");
        return mRequestQueue;
    }

    /**
     * 测试数据get网络请求接口
     *
     * @param <T>
     * @param listener 回调接口，包含错误回调和正确的数据回调
     */
    public static <T> void getObjectApi(String url, Type clazz,
                                        boolean isRefresh, ResponseListener listener)
    {
        Request request = new GetObjectRequest(url, clazz, listener);
        request.setShouldCache(true);
        request.setCacheTime(10 * 60);
        request.setRefreshNeeded(isRefresh);
        VolleyUtil.getRequestQueue().add(request);
    }

    /**
     * 测试数据post网络请求接口
     *
     * @param listener 回调接口，包含错误回调和正确的数据回调
     */
    public static <T> void postObjectApi(String url, Map<String, String> param,
                                         Type clazz, ResponseListener listener)
    {
        Request request = new PostObjectRequest(url, param, clazz, listener);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,//默认超时时间，默认2500
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyUtil.getRequestQueue().add(request);
    }

    public static <T> void postObjectApi1(String url, Map<String, String> param,
                                         Type clazz, ResponseListener listener)
    {
        Request request = new PostObjectRequest1(url, param, clazz, listener);
        VolleyUtil.getRequestQueue().add(request);
    }

    public static <T> void postObjectApi(String url, String postStr, Type clazz, ResponseListener<T> listener)
    {
        Request<T> request = new PostObjectRequest(url, postStr, clazz, listener);
        VolleyUtil.getRequestQueue().add(request);
    }

    public static <T> void uplaodObjectApi(String url, List<FormImage> listItem, Type clazz, ResponseListener listener)
    {
        Request request = new PostUploadRequest(url, listItem, clazz, listener);
        VolleyUtil.getRequestQueue().add(request);
    }

    /**
     * 测试数据post网络请求接口
     *
     * @param listener 回调接口，包含错误回调和正确的数据回调
     */
    public static <T> void postMultparamiApi(String url, Map<String, String> params, String filePartName, List<File> files,
                                             Response.Listener<String> listener, Response.ErrorListener errorListener) {
        Request request = new MultipartRequest(url,listener,errorListener,filePartName,files,params);
        //request.setCacheTime(60000);
        VolleyUtil.getRequestQueue().add(request);
    }


    /**
     * 测试数据post网络请求接口
     *
     * @param listener 回调接口，包含错误回调和正确的数据回调
     */
    public static <T> void postMultiApi(String url, List<FormText> textList,
                                        List<FormImage> imageList, Type clazz, ResponseListener listener)
    {
        Request request = new MultiPostRequest(url, textList, imageList, clazz,
                listener);
        VolleyUtil.getRequestQueue().add(request);
    }

    public static Map<String, String> getRequestMap(Context context)
    {
        Map<String, String> param = new HashMap<>();
//        param.put("device", "android");
//        param.put("userid", LocalData.getInstance().getUserInfo().getId());
//        param.put("verify", LocalData.getInstance().getUserInfo().getVerify());
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceId = tm.getDeviceId();
//        if (deviceId == null)
//        {
//            param.put("deviceid", "1234567890");
//        } else
//        {
//            param.put("deviceid", deviceId);
//        }
        return param;
    }
}

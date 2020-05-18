package com.glavesoft.pawnuser.activity.personal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.SimplePageInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.Map;

public class AboutActivity extends BaseActivity {

    Rect mRect=new Rect();
    ImageView iv;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //iv = (ImageView) findViewById(R.id.iv_about);
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.about);
//        setBitmapToImg(bmp);
        //iv.setImageResource(R.drawable.about1);
//        iv.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.about,300,
// 300));
        webView= (WebView) findViewById(R.id.wv_about);
        setTitleName("关于我们");
        setTitleBack();
        getAboutInfo();
    }
    private void getAboutInfo()
    {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("common/simplePage/get");
        HttpParams param=new HttpParams();
        param.put("code","about");
        OkGo.<DataResult<SimplePageInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<SimplePageInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<SimplePageInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){
                                showInfo(response.body().getData());
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<SimplePageInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showInfo(SimplePageInfo info) {
//        webView.loadDataWithBaseURL(null,
//                info.getContent(), "text/html", "utf-8", null);

        WebSettings webSettings = webView.getSettings();

        // 支持javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        // 自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式
        webSettings.setUseWideViewPort(true);  //为图片添加放大缩小功能
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        getlDialog().show();

        // 加载数据
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                if (newProgress == 100)
                {
                    getlDialog().dismiss();
                }
            }
        });

        webView.loadUrl( info.getContent());
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


}

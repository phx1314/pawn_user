package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;

/**
 * @author 严光
 * @date: 2017/8/2
 * @company:常州宝丰
 */
public class WebPicActivity extends BaseActivity implements GestureDetector.OnGestureListener{
    private WebView webView;
    private String titleName, url;
    String html;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpic);

        initView();
    }

    private void initView()
    {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        webView = (WebView) findViewById(R.id.wv);

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
        //webSettings.setUseWideViewPort(true);  //为图片添加放大缩小功能
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

        // 这个是当网页上的连接被点击的时候
        webView.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(final WebView view, final String url)
            {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        //webView.loadUrl(url);
        webView.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{width:100%25;} " +
                "</style></head><body><img src='"+url+"'/></body></html>" ,"text/html",  "UTF-8");

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

}

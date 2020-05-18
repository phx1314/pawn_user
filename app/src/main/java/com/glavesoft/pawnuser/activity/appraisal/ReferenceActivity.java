package com.glavesoft.pawnuser.activity.appraisal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;

/**
 * @author 严光
 * @date: 2018/5/3
 * @company:常州宝丰
 */
public class ReferenceActivity  extends BaseActivity {
    private WebView webView;
    private LinearLayout ll_submit_reference;
    private String titleName, url;
    Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        titleName = intent.getStringExtra("titleName");
        url = intent.getStringExtra("url");

        ll_submit_reference = (LinearLayout) findViewById(R.id.ll_submit_reference);
        ll_submit_reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titlebar_back = (ImageView) findViewById(R.id.titlebar_back);
        titlebar_back.setVisibility(View.VISIBLE);
        titlebar_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();// 返回前一个页面
                } else {
                    finish();
                }
            }
        });
        setTitleName(titleName);
        setTitleNameEn(R.mipmap.photo_reference);

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
        webSettings.setUseWideViewPort(true);  //为图片添加放大缩小功能
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        getlDialog().show();

        // 加载数据
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    getlDialog().dismiss();
                }
            }
        });

        // 这个是当网页上的连接被点击的时候
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

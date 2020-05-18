package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.FlexibleRoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class WebActivity extends BaseActivity
{
	private WebView webView;
	private String titleName, url;
	Bitmap bitmap;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		initView();
	}

	private void initView()
	{
		Intent intent = getIntent();
		titleName = intent.getStringExtra("titleName");
		url = intent.getStringExtra("url");

		titlebar_back = (ImageView) findViewById(R.id.titlebar_back);
		titlebar_back.setVisibility(View.VISIBLE);
		titlebar_back.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (webView.canGoBack()) {
					webView.goBack();// 返回前一个页面
				}else{
					finish();
				}
			}
		});
		setTitleName(titleName);

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
//		webView.setWebViewClient(new WebViewClient()
//		{
//			public boolean shouldOverrideUrlLoading(final WebView view, final String url)
//			{
//				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//				view.loadUrl(url);
//				return true;
//			}
//		});

		webView.loadUrl(url);

		//java回调js代码，不要忘了@JavascriptInterface这个注解，不然点击事件不起作用
		webView.addJavascriptInterface(new JsCallJavaObj() {
			@JavascriptInterface
			@Override
			public void showBigImg(final String url) {

				ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener(){

					@Override
					public void onLoadingComplete(String imageUri, View view,
												  Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						if (loadedImage.getHeight()>loadedImage.getWidth()*2){
							Intent intent=new Intent();
							intent.setClass(WebActivity.this,WebPicActivity.class);
							intent.putExtra("url",url);
							startActivity(intent);
						}else{
							Intent intent=new Intent();
							intent.setClass(WebActivity.this,ImageActivity.class);
							intent.putExtra("url",url);
							startActivity(intent);
						}
					}

				});

			}
		},"jsCallJavaObj");
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				setWebImageClick(view);
			}
		});
	}

	/**
	 * 設置網頁中圖片的點擊事件
	 * @param view
	 */
	private  void setWebImageClick(WebView view) {
		String jsCode="javascript:(function(){" +
				"var imgs=document.getElementsByTagName(\"img\");" +
				"for(var i=0;i<imgs.length;i++){" +
				"imgs[i].onclick=function(){" +
				"window.jsCallJavaObj.showBigImg(this.src);" +
				"}}})()";
		webView.loadUrl(jsCode);
	}

	/**
	 * Js調用Java接口
	 */
	private interface JsCallJavaObj{
		void showBigImg(String url);
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

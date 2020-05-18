package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.WebActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.util.FileUtils;
import com.glavesoft.util.PreferencesUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @author 严光
 * @date: 2018/1/30
 * @company:常州宝丰
 */
public class ContractDetailActivity extends BaseActivity
{
    private WebView webView;
    private String titleName, url,title,time="";
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
        title = intent.getStringExtra("title");
        time = intent.getStringExtra("time");

        titlebar_back = (ImageView) findViewById(R.id.titlebar_back);
        titlebar_back.setVisibility(View.VISIBLE);
        titlebar_back.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (webView.canGoBack()) {
                    webView.goBack();// 返回前一个页面
                    settitlebar();
                }else{
                    finish();
                }
            }
        });

        settitlebar();
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
        webView.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(final WebView view, final String url)
            {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                Intent intent=new Intent();
                intent.setClass(ContractDetailActivity.this,WebActivity.class);
                if(url.contains("htf")){
                    intent.putExtra("titleName","合同法");
                    intent.putExtra("url", PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "htf",""));
                }else if(url.contains("msf")){
                    intent.putExtra("titleName","民事法");
                    intent.putExtra("url", PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "mfzz",""));
                }else if(url.contains("ddgl")){
                    intent.putExtra("titleName","典当管理法");
                    intent.putExtra("url", PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "ddglbf",""));
                }
                startActivity(intent);
                return true;
            }
        });

        webView.loadUrl(url);
    }

    private void settitlebar(){
        setTitleName("电子"+titleName);
        setTitleNameEn(R.mipmap.contract_of_pawn);

        titlebar_search = (ImageView) findViewById(R.id.titlebar_search);
        titlebar_search.setVisibility(View.VISIBLE);
        titlebar_search.setImageResource(R.mipmap.down);
        titlebar_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getScrollViewBitmap();
                showPopupWindow();
            }
        });

    }

    private void Savepdf(){
        float mWebViewTotalHeight = webView.getContentHeight() * webView.getScale();
        bitmap = Bitmap.createBitmap(webView.getWidth(), (int)mWebViewTotalHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);

        float PDFBitmapRatio = (float) bitmap.getHeight() / (float) bitmap.getWidth();

        Document document = new Document(PageSize.A4, 0, 0, 0, 0);

        try {
            String path = FileUtils.CACHE_SAVE_PDF_PATH;
            File tmpfile = new File(path);
            if (!tmpfile.exists())
            {
                tmpfile.mkdirs();
            }
            if(!time.equals("")&&time.length()>=10){
                time=time.substring(0,10);
                time=time.replace("-","");
            }
            String filename=title+"("+titleName+")_"+time+".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(path
                    + filename));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();
        //float PDFBitmapRatio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
        if (PDFBitmapRatio <= 1.4) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            try {
                Image image = Image.getInstance(byteArray);
                image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                document.add(image);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            float BitmapHeightPerPage = (float) bitmap.getWidth() * 1.4f;
            int pages = (int) Math.ceil(bitmap.getHeight() / BitmapHeightPerPage);
            System.out.println("pages:" + pages);
            Bitmap sub_bitmap;
            for (int i = 0; i < pages; i++) {
                if (i == pages - 1) {
                    /**
                     * can not use default setting, or pdf reader cannot read the exported pdf
                     */
                    sub_bitmap = Bitmap.createBitmap(bitmap, 0, (int) BitmapHeightPerPage * i, bitmap.getWidth(), (int) (bitmap.getHeight() - (BitmapHeightPerPage * (pages - 1))));
                } else {
                    sub_bitmap = Bitmap.createBitmap(bitmap, 0, (int) BitmapHeightPerPage * i, bitmap.getWidth(), (int) BitmapHeightPerPage);
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                sub_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                try {
                    Image image = Image.getInstance(byteArray);
                    image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                    document.add(image);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sub_bitmap.recycle();
            }
        }
        document.close();
    }


    private PopupWindow popupWindo;
    public void showPopupWindow()
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(ContractDetailActivity.this).inflate(R.layout.pw_dialog2, null);

        Button btn_ok = (Button)view.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
            }
        });
        Display display = getWindowManager().getDefaultDisplay();
        popupWindo = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindo.setOutsideTouchable(true);
        popupWindo.setFocusable(true);
        fitPopupWindowOverStatusBar(popupWindo);
        popupWindo.setBackgroundDrawable(new ColorDrawable());
        popupWindo.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    /**
     * 截取scrollview的屏幕
     * **/
    private void getScrollViewBitmap() {
        Bitmap bitmap;
        float mWebViewTotalHeight = webView.getContentHeight() * webView.getScale();
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(webView.getWidth(), (int)mWebViewTotalHeight,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {
            String path = FileUtils.CACHE_SAVE_CONTRACT_PATH;
            File tmpfile = new File(path);
            if (!tmpfile.exists())
            {
                tmpfile.mkdirs();
            }
            time=time.replace("-","");
            time=time.replace(":","");
            time=time.replace(" ","");

            String filename=title+"("+titleName+")_"+time+".jpg";
            out = new FileOutputStream(path+filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

                bitmap.recycle();
                bitmap = null;
                System.gc();
            }
        } catch (IOException e) {
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            settitlebar();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

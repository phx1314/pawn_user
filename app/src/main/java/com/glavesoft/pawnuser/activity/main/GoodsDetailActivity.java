package com.glavesoft.pawnuser.activity.main;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.blankj.utilcode.util.StringUtils;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.activity.pawn.GoodsCommentsActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.StoreGoodsInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.MyListView;
import com.glavesoft.view.SlideShowView;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author 严光
 * @date: 2017/10/20
 * @company:常州宝丰
 */

@SuppressLint("SetJavaScriptEnabled")
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.iv_store_icon)
    ImageView ivStoreIcon;
    @BindView(R.id.tv_storename)
    TextView tvStorename;
    @BindView(R.id.tv_store_follow)
    TextView tvStoreFollow;
    @BindView(R.id.tv_store_memo)
    TextView tvStoreMemo;
    @BindView(R.id.ll_store_goodsdetail)
    LinearLayout llStoreGoodsdetail;
    @BindView(R.id.ll_store)
    LinearLayout llStore;
    @BindView(R.id.ll_goods_fllow)
    LinearLayout llGoodsFllow;
    @BindView(R.id.ll_goods_comment)
    LinearLayout llGoodsComment;
    @BindView(R.id.ll_add_shopcar)
    LinearLayout llAddShopcar;
    @BindView(R.id.ll_buy_goodsdetail)
    LinearLayout llBuyGoodsdetail;
//    @BindView(R.id.tv_add_shopcar)
//    TextView tvAddShopcar;
    private SlideShowView ssv_goodsdetail_pic;
//    private TextView tv_buy_goodsdetail;
    private MyListView mlv_ltjl, mlv_jpjl;
    private ArrayList<String> list = new ArrayList<>();

    private CountDownTimer mCountDownTimer;
    private TextView tv_countTime_goodsdetail1;

    private LinearLayout ll_goodsdetail, ll_goodsdetail1;

    private TextView tv_type_goodsdetail, tv_name_goodsdetail, tv_price_goodsdetail,
            tv_jdprice_goodsdetail;
    private TextView tv_scyg_goodsdetail;

    private LinearLayout ll_dsfrz_goodsdetail;

    private WebView wv_goodsdetail;

    private String type = "rz";
    private StoreGoodsInfo storeGoodsInfo;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetail);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
    }

    private void initView() {
        setTitleBack();
        setTitleName("物品详情");
        setTitleNameEn(R.mipmap.wpxq);
        titlebar_kf = (ImageView) findViewById(R.id.titlebar_kf);
        titlebar_kf.setVisibility(View.VISIBLE);
        titlebar_kf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Information info = new Information();
                info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
                //用户资料
                if (BaseConstant.isLogin()) {
                    info.setUid(LocalData.getInstance().getUserInfo().getId());
                    info.setUname(LocalData.getInstance().getUserInfo().getId());
                    info.setTel(LocalData.getInstance().getUserInfo().getAccount());
                    info.setFace(BaseConstant.Image_URL + LocalData.getInstance().getUserInfo()
                            .getHeadImg());
                } else {
                    info.setUid(CommonUtils.getDeviceId(GoodsDetailActivity.this));
                    info.setUname("游客");
                }
                //1仅机器人 2仅人工 3机器人优先 4人工优先
                info.setInitModeType(4);
                //转接类型(0-可转入其他客服，1-必须转入指定客服)
                info.setTranReceptionistFlag(1);
                //指定客服id
                info.setReceptionistId("71764f63c3ca497ba974f938b26389eb");
                if (storeGoodsInfo != null) {
                    //咨询内容
                    ConsultingContent consultingContent = new ConsultingContent();
                    //咨询内容标题，必填
                    consultingContent.setSobotGoodsTitle(storeGoodsInfo.getTitle());
                    //咨询内容图片，选填 但必须是图片地址
                    if (!storeGoodsInfo.getImages().equals("")) {
                        List<String> list = Arrays.asList(storeGoodsInfo.getImages().split(","));
                        consultingContent.setSobotGoodsImgUrl(BaseConstant.Image_URL + list.get(0));
                    }
                    //咨询来源页，必填
                    consultingContent.setSobotGoodsFromUrl("www.sobot.com");
                    //描述，选填
                    //consultingContent.setSobotGoodsDescribe("XXX超级电视 S5");
                    //标签，选填
                    consultingContent.setSobotGoodsLable("￥" + storeGoodsInfo.getPrice());
                    //可以设置为null
                    info.setConsultingContent(consultingContent);
                }
                //设置聊天界面标题显示模式
                SobotApi.setChatTitleDisplayMode(GoodsDetailActivity.this,
                        SobotChatTitleDisplayMode.Default, "");

                SobotApi.startSobotChat(GoodsDetailActivity.this, info);
            }
        });

        ssv_goodsdetail_pic = (SlideShowView) findViewById(R.id.ssv_goodsdetail_pic);

        ll_dsfrz_goodsdetail = getViewById(R.id.ll_dsfrz_goodsdetail);
        ll_goodsdetail = getViewById(R.id.ll_goodsdetail);
        ll_goodsdetail1 = getViewById(R.id.ll_goodsdetail1);

        tv_type_goodsdetail = getViewById(R.id.tv_type_goodsdetail);
        tv_name_goodsdetail = getViewById(R.id.tv_name_goodsdetail);
        tv_price_goodsdetail = getViewById(R.id.tv_price_goodsdetail);
        tv_jdprice_goodsdetail = getViewById(R.id.tv_jdprice_goodsdetail);

        if (type.equals("rz")) {
            tv_jdprice_goodsdetail.setVisibility(View.GONE);
//            tv_type_goodsdetail.setVisibility(View.GONE);
            ll_dsfrz_goodsdetail.setVisibility(View.GONE);

        } else {
            tv_jdprice_goodsdetail.setVisibility(View.VISIBLE);
//            tv_type_goodsdetail.setVisibility(View.GONE);
            ll_dsfrz_goodsdetail.setVisibility(View.GONE);
        }


        tv_scyg_goodsdetail = getViewById(R.id.tv_scyg_goodsdetail);

        mlv_ltjl = (MyListView) findViewById(R.id.mlv_ltjl);
        mlv_jpjl = (MyListView) findViewById(R.id.mlv_jpjl);

        wv_goodsdetail = getViewById(R.id.wv_goodsdetail);

        tv_countTime_goodsdetail1 = (TextView) findViewById(R.id.tv_countTime_goodsdetail1);


        tvShare.setOnClickListener(this);
        llBuyGoodsdetail.setOnClickListener(this);
        llStore.setOnClickListener(this);
        llStoreGoodsdetail.setOnClickListener(this);
        llAddShopcar.setOnClickListener(this);
        llGoodsComment.setOnClickListener(this);

        storeGoodsDetail();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_buy_goodsdetail:

                if (BaseConstant.isLogin()) {
                    if (storeGoodsInfo != null) {
                        intent = new Intent();
                        intent.setClass(GoodsDetailActivity.this, SubmitBuyActivity.class);
                        intent.putExtra("type", "goodsdetail");
                        intent.putExtra("state", type);
                        intent.putExtra("storeGoodsInfo", storeGoodsInfo);
                        startActivity(intent);
                    }
                } else {
                    startActivity(new Intent(GoodsDetailActivity.this, LoginActivity.class));
                }
                break;
            case R.id.ll_store_goodsdetail:
                if (storeGoodsInfo != null) {
                    intent = new Intent();
                    intent.setClass(GoodsDetailActivity.this, StoreActivity.class);
                    intent.putExtra("storeid", storeGoodsInfo.getOrgId());
                    startActivity(intent);
                }

                break;
            case R.id.ll_store:
                if (storeGoodsInfo != null) {
                    intent = new Intent();
                    intent.setClass(GoodsDetailActivity.this, StoreActivity.class);
                    intent.putExtra("storeid", storeGoodsInfo.getOrgId());
                    startActivity(intent);
                }

                break;
            case R.id.ll_add_shopcar:
                if (BaseConstant.isLogin()) {
                    if (storeGoodsInfo != null)
                        addShopcar();
                } else {
                    startActivity(new Intent(GoodsDetailActivity.this, LoginActivity.class));
                }
                break;
            case R.id.ll_goods_comment:
                intent = new Intent();
                intent.setClass(GoodsDetailActivity.this, GoodsCommentsActivity.class);
                if (storeGoodsInfo != null)
                    intent.putExtra("goodsid", storeGoodsInfo.getId());
                intent.putExtra("type", "goods");
                startActivity(intent);
                break;
            case R.id.tv_share:
                getShareText();
                break;
        }
    }

    private void addShopcar() {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("userShopCart/updateCart");
        HttpParams param=new HttpParams();
        param.put("token", LocalData.getInstance().getUserInfo().getToken());
        param.put("goodsId", storeGoodsInfo.getId());
        param.put("num", "1");
        OkGo.<DataResult<String>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<String>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<String>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null) {
                                if (!response.body().getData().equals("1")) {
                                    CustomToast.show("库存不足");
                                } else {
                                    Intent intent=new Intent("ShopcarRefresh");
                                    sendBroadcast(intent);
                                    CustomToast.show("添加成功");
                                }
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<String>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void storeGoodsDetail() {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("storeGoods/storeGoodsDetail");
        HttpParams param=new HttpParams();
        param.put("token", token);
        param.put("id", id);
        OkGo.<DataResult<StoreGoodsInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<StoreGoodsInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<StoreGoodsInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null) {
                                storeGoodsInfo = response.body().getData();
                                if (!storeGoodsInfo.getOrgLogo().equals("")) {
                                    DisplayImageOptions imageOptions = DisplayImageOptions
                                            .createSimple();
                                    ImageLoader.getInstance().displayImage(BaseConstant.Image_URL
                                                    + storeGoodsInfo.getOrgLogo(), ivStoreIcon,
                                            imageOptions);
                                }else{
                                    Random rand = new Random();
                                    int randNum = rand.nextInt(4);
                                    if (randNum==0){
                                        ivStoreIcon.setImageResource(R.mipmap.mryi);
                                    }else  if (randNum==1){
                                        ivStoreIcon.setImageResource(R.mipmap.mrer);
                                    }else  if (randNum==2){
                                        ivStoreIcon.setImageResource(R.mipmap.mrsan);
                                    }else  if (randNum==3){
                                        ivStoreIcon.setImageResource(R.mipmap.mrsi);
                                    }
                                }
                                if (storeGoodsInfo.getSource().equals("3")) {
                                    tv_type_goodsdetail.setText("自营");
//                                    ivStoreIcon.setImageResource(R.drawable.storeicon);
                                } else if (storeGoodsInfo.getSource().equals("4")) {
                                    tv_type_goodsdetail.setText("臻品");
//                                    ivStoreIcon.setImageResource(R.drawable.storeicon);
                                } else if (storeGoodsInfo.getSource().equals("2")) {
//                                    ivStoreIcon.setImageResource(R.drawable.ddh_icon);
                                    tv_type_goodsdetail.setText("绝当品");
                                }

                                tvStorename.setText(storeGoodsInfo.getOrgName());
                                tvStoreMemo.setText(storeGoodsInfo.getOrgIntroduction());


                                if (!response.body().getData().getImages().equals("")) {
                                    final List<String> list = Arrays.asList(response.body().getData()
                                            .getImages().split(","));

                                    final ArrayList<String> picurlList = new ArrayList<>();

                                    int j;
                                    String[] headUrls;
                                    if (!StringUtils.isEmpty(storeGoodsInfo.getBannerVideo())){
                                        j=1;
                                        headUrls = new String[list.size()+1];
                                        headUrls[0] = BaseConstant.Image_URL + storeGoodsInfo.getBannerVideoFace();
                                    }else{
                                        j=0;
                                        headUrls = new String[list.size()];
                                    }

                                    for (int i = 0; i < list.size(); i++) {
                                        headUrls[i+j] = BaseConstant.Image_URL + list.get(i);
                                        picurlList.add(BaseConstant.Image_URL + list.get(i));
                                    }
                                    final int finalJ = j;
                                    ssv_goodsdetail_pic.initAndSetImagesUrl(headUrls,
                                            new SlideShowView.OnImageClickListener() {
                                                @Override
                                                public void onClick(View v, int position) {
                                                    if (!StringUtils.isEmpty(storeGoodsInfo.getBannerVideo())&&position==0){
                                                        JCVideoPlayerStandard.startFullscreen(GoodsDetailActivity.this, JCVideoPlayerStandard.class,
                                                                BaseConstant.Image_URL + storeGoodsInfo.getBannerVideo(), "");
                                                    }else{
                                                        Intent intent = new Intent();
                                                        intent.setClass(GoodsDetailActivity.this,
                                                                ImagePageActivity.class);
                                                        intent.putExtra("picurlList", picurlList);
                                                        intent.putExtra("selectPos", position- finalJ);
                                                        startActivity(intent);
                                                    }

                                                }
                                            });
                                }

                                tv_name_goodsdetail.setText(storeGoodsInfo.getTitle());
                                tv_price_goodsdetail.setText("￥" + storeGoodsInfo.getPrice());
                                tv_jdprice_goodsdetail.setText("鉴定价：￥" + storeGoodsInfo
                                        .getAuthPrice());

                                //tv_scyg_goodsdetail.setText(storeGoodsInfo.getDescription());

                                WebSettings webSettings = wv_goodsdetail.getSettings();

                                // 支持javascript
                                webSettings.setJavaScriptEnabled(true);
                                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                                // 自适应屏幕
                                webSettings.setLoadWithOverviewMode(true);
                                //getlDialog().show();

                                // 加载数据
                                wv_goodsdetail.setWebChromeClient(new WebChromeClient() {
                                    @Override
                                    public void onProgressChanged(WebView view, int newProgress) {
                                        if (newProgress == 100) {
                                            //getlDialog().dismiss();
                                        }
                                    }
                                });

                                // 这个是当网页上的连接被点击的时候
//                        wv_goodsdetail.setWebViewClient(new WebViewClient()
//                        {
//                            public boolean shouldOverrideUrlLoading(final WebView view, final
// String url)
//                            {
//                                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                                view.loadUrl(url);
//                                return true;
//                            }
//                        });
                                wv_goodsdetail.loadUrl(storeGoodsInfo.getGoodsDescription());

                                //java回调js代码，不要忘了@JavascriptInterface这个注解，不然点击事件不起作用
                                wv_goodsdetail.addJavascriptInterface(new JsCallJavaObj() {
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
                                                    intent.setClass(GoodsDetailActivity.this,WebPicActivity.class);
                                                    intent.putExtra("url",url);
                                                    startActivity(intent);
                                                }else{
                                                    Intent intent=new Intent();
                                                    intent.setClass(GoodsDetailActivity.this,ImageActivity.class);
                                                    intent.putExtra("url",url);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }, "jsCallJavaObj");
                                wv_goodsdetail.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public void onPageFinished(WebView view, String url) {
                                        super.onPageFinished(view, url);
                                        setWebImageClick(view);
                                    }
                                });

                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<StoreGoodsInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    /**
     * 設置網頁中圖片的點擊事件
     *
     * @param view
     */
    private void setWebImageClick(WebView view) {
        String jsCode = "javascript:(function(){" +
                "var imgs=document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<imgs.length;i++){" +
                "imgs[i].onclick=function(){" +
                "window.jsCallJavaObj.showBigImg(this.src);" +
                "}}})()";
        wv_goodsdetail.loadUrl(jsCode);
    }

    /**
     * Js調用Java接口
     */
    private interface JsCallJavaObj {
        void showBigImg(String url);
    }

    /**
     * 生成分享文字
     */
    private void getShareText() {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("userGoods/getShareText");
        HttpParams param=new HttpParams();
        param.put("id", storeGoodsInfo.getId());
        if (type.equals("rz")) {
            param.put("type", "1");
        }else{
            param.put("type", "2");
        }

        OkGo.<DataResult<String>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<String>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<String>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (!StringUtils.isEmpty(response.body().getData())) {
                                try {
                                    //获取剪贴板管理器
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    // 创建普通字符型ClipData
                                    ClipData mClipData = ClipData.newPlainText("Label", response.body().getData());
                                    // 将ClipData内容放到系统剪贴板里。
                                    cm.setPrimaryClip(mClipData);
                                    BaseConstant.isCopy=true;
                                    CustomToast.show("已复制口令，快去分享给好友吧！");
                                } catch (Exception e) {

                                }
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<String>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}

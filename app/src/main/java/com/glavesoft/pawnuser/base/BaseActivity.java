package com.glavesoft.pawnuser.base;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.blankj.utilcode.util.StringUtils;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.activity.appraisal.AddSendCallActivity;
import com.glavesoft.pawnuser.activity.appraisal.EmeraldActivity;
import com.glavesoft.pawnuser.activity.appraisal.JewelleryActivity;
import com.glavesoft.pawnuser.activity.appraisal.OtherActivity;
import com.glavesoft.pawnuser.activity.appraisal.SendCallGoodDetailActivity;
import com.glavesoft.pawnuser.activity.appraisal.WatchActivity;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.activity.login.RegisterActivity;
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.activity.main.GuideActivity;
import com.glavesoft.pawnuser.activity.main.ImageActivity;
import com.glavesoft.pawnuser.activity.main.ImagePageActivity;
import com.glavesoft.pawnuser.activity.appraisal.MailAppraisalActivity;
import com.glavesoft.pawnuser.activity.main.MainActivity;
import com.glavesoft.pawnuser.activity.pawn.CashItemsActivity;
import com.glavesoft.pawnuser.activity.pawn.MonitorDetailActivity;
import com.glavesoft.pawnuser.activity.personal.PersonalActivity;
import com.glavesoft.pawnuser.activity.main.StartActivity;
import com.glavesoft.pawnuser.activity.video.SingleVideoActivity;
import com.glavesoft.pawnuser.activity.video.VideoListActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.frg.FrgProductDetail;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.StoreGoodsInfo;
import com.glavesoft.pawnuser.mod.UserInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.util.SystemBarTintManager;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.DownLoadDialog;
import com.glavesoft.view.FlexibleRoundedBitmapDisplayer;
import com.glavesoft.view.LoadingDialog;
import com.glavesoft.pawnuser.R;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.service.subscriber.HttpResultSubscriberListener;
import com.mdx.framework.utility.Helper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.Information;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;

public class BaseActivity extends AppCompatActivity
{
    protected TextView titlebar_left;
    protected TextView titlebar_right;
    protected TextView titlebar_name;
    protected ImageView iv_en;
    protected LinearLayout ll_en;
    protected ImageView titlebar_back,titlebar_search,titlebar_kf,titlebar_news;
    protected TextView titlebar_searchcancel;
    protected EditText titlebar_et_keywords;
    private ImageLoader imageLoader;
    private DisplayImageOptions options, optionsHead;
    private LoadingDialog lDialog;
    protected DownLoadDialog dDialog;
    protected BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    // 创建状态栏的管理实例(发现如果不用沉浸式状态栏，设状态栏颜色没有用)
    public static SystemBarTintManager tintManager;
    // 创建状态栏的管理实例(发现如果不用沉浸式状态栏，设状态栏颜色没有用)
    public static SystemBarTintManager tintManager2;

    public static final String KEY_IMAGE_LIST = "imageList";
    public static final String KEY_CURRENT_INDEX = "currentIndex";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//键盘默认不弹出
        // 屏蔽横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setStatusBarTrans();

        moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (!(this instanceof StartActivity)){
                if ( BaseConstant.isCopy){
                    BaseConstant.isCopy=false;
                }else{
                    ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    if (cm != null) {
                        try {
                            ClipData data = cm.getPrimaryClip();
                            if (data != null && data.getItemCount() > 0) {
                                ClipData.Item item = data.getItemAt(0);
                                String content = item.getText().toString();
                                String id="";
                                if (!StringUtils.isEmpty(content)){
                                    List<String> list = Arrays.asList(content.split("￥"));
                                    if (list.size()>2){
                                        id=list.get(1);
                                    }
                                }
                                getShareInfo(id);
                                /*** 清空剪贴板内容*/
                                cm.setPrimaryClip(cm.getPrimaryClip());
                                cm.setText(null);
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }
    }


    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    public ImageLoader getImageLoader()
    {
        if (imageLoader == null)
        {
            imageLoader = ImageLoader.getInstance();
        }

        return imageLoader;
    }

    public DisplayImageOptions getImageLoaderOptions()
    {
        if(options == null)
        {
            options = new DisplayImageOptions.Builder().showStubImage(R.drawable.sy_bj).showImageForEmptyUri(R.drawable.sy_bj).
                    cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        return options;
    }

    public DisplayImageOptions getImageLoaderHeadOptions()
    {
        if(optionsHead == null)
        {
            optionsHead = new DisplayImageOptions.Builder().showStubImage(R.drawable.tx).showImageForEmptyUri(R.drawable.tx).
                    cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        return optionsHead;
    }

    public void showVolleyError(VolleyError error)
    {
        CustomToast.show(getString(R.string.msg_error));

        if (error != null)
        {
            String msg = error.getMessage();

            if (msg != null)
            {
                Log.e("VolleyError", msg);
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
    }

    public void gotokf(Context context)
    {
        Information info = new Information();
        info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
        //用户资料
        if(BaseConstant.isLogin()){
            info.setUid(LocalData.getInstance().getUserInfo().getId());
            info.setUname(LocalData.getInstance().getUserInfo().getId());
            info.setTel(LocalData.getInstance().getUserInfo().getAccount());
            info.setFace(BaseConstant.Image_URL+LocalData.getInstance().getUserInfo().getHeadImg());
        }else{
            info.setUid(CommonUtils.getDeviceId(this));
            info.setUname("游客");
        }
        //客服模式控制 -1不控制 按照服务器后台设置的模式运行
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(4);

        //设置聊天界面标题显示模式
        SobotApi.setChatTitleDisplayMode(context, SobotChatTitleDisplayMode.Default,"");

        SobotApi.startSobotChat(context, info);
    }

    public void gotokf_Z(Context context)
    {
        Information info = new Information();
        info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
        //用户资料
        if(BaseConstant.isLogin()){
            info.setUid(LocalData.getInstance().getUserInfo().getId());
            info.setUname(LocalData.getInstance().getUserInfo().getId());
            info.setTel(LocalData.getInstance().getUserInfo().getAccount());
            info.setFace(BaseConstant.Image_URL+LocalData.getInstance().getUserInfo().getHeadImg());
        }else{
            info.setUid(CommonUtils.getDeviceId(this));
            info.setUname("游客");
        }
        //客服模式控制 -1不控制 按照服务器后台设置的模式运行
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(4);

        //转接类型(0-可转入其他客服，1-必须转入指定客服)
        info.setTranReceptionistFlag(1);
        //指定客服id
        info.setReceptionistId("b125bade408341d4b1c825ee56a1dbb8");
        //设置聊天界面标题显示模式
        SobotApi.setChatTitleDisplayMode(context, SobotChatTitleDisplayMode.Default,"");
        SobotApi.setNotificationFlag(context,true,R.mipmap.ic_launcher,R.mipmap.ic_launcher);

        SobotApi.startSobotChat(context, info);
    }

    public void setTitleName(String title)
    {
        if (titlebar_name == null)
            titlebar_name = (TextView) findViewById(R.id.titlebar_name);
        titlebar_name.setVisibility(View.VISIBLE);
        titlebar_name.setText(title);
    }

    public void setTitleNameEn(int resId)
    {
        if (iv_en == null)
            iv_en = (ImageView) findViewById(R.id.iv_en);
        if (ll_en == null)
            ll_en = (LinearLayout) findViewById(R.id.ll_en);
        iv_en.setVisibility(View.VISIBLE);
        ll_en.setVisibility(View.VISIBLE);
        iv_en.setImageResource(resId);
    }

    public void setTitle_right(String title,OnClickListener listener){
        if(titlebar_right==null){
            titlebar_right= (TextView) findViewById(R.id.titlebar_right);
        }
        titlebar_right.setVisibility(View.VISIBLE);
        titlebar_right.setText(title);
        if(listener==null){
            titlebar_right.setOnClickListener(null);
        }else{
            titlebar_right.setOnClickListener(listener);
        }

    }

    public void setTitle_News(OnClickListener listener){
        if(titlebar_news==null){
            titlebar_news= (ImageView) findViewById(R.id.titlebar_news);
        }
        titlebar_news.setVisibility(View.VISIBLE);
        if(listener==null){
            titlebar_news.setOnClickListener(null);
        }else{
            titlebar_news.setOnClickListener(listener);
        }

    }

    public void settitle_Searchcancel(OnClickListener listener)
    {
        if (titlebar_searchcancel == null)
            titlebar_searchcancel = (TextView) findViewById(R.id.titlebar_cancel);
        titlebar_searchcancel.setVisibility(View.VISIBLE);
        if(listener==null){
            titlebar_searchcancel.setOnClickListener(null);
        }else{
            titlebar_searchcancel.setOnClickListener(listener);
        }
    }

    public EditText gettitle_Searchet()
    {
        if (titlebar_et_keywords == null)
            titlebar_et_keywords = (EditText) findViewById(R.id.titlebar_et_keywords);
        return titlebar_et_keywords;
    }

    public void setTitleBack()
    {
        if (titlebar_back == null)
            titlebar_back = (ImageView) findViewById(R.id.titlebar_back);
        titlebar_back.setVisibility(View.VISIBLE);
        titlebar_back.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                finish();
            }
        });
    }
    public void setTitleBack(OnClickListener listener)
    {
        if (titlebar_back == null)
            titlebar_back = (ImageView) findViewById(R.id.titlebar_back);
        if(listener!=null){
            titlebar_back.setOnClickListener(listener);
            titlebar_back.setVisibility(View.VISIBLE);
        }
    }
    public void setTitleseach(OnClickListener listener)
    {
        if (titlebar_search == null)
            titlebar_search = (ImageView) findViewById(R.id.titlebar_search);
        if(listener!=null){
            titlebar_search.setOnClickListener(listener);
            titlebar_search.setVisibility(View.VISIBLE);
        }
    }

    public void setTitleVisible(boolean isShow)
    {
        findViewById(R.id.titlebar_ll_main).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public LoadingDialog getlDialog()
    {
        if (lDialog == null)
        {
            lDialog = new LoadingDialog(this);
        }

        return lDialog;
    }

    public DownLoadDialog getDownLoadDialog() {
        if (dDialog == null) {
            dDialog = new DownLoadDialog(this);
        }
        return dDialog;
    }

    public String setLastUpdateTime()
    {
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        return "最后更新时间 :" + text;
    }

    // 设置透明度
    public void setAlpha(Float f)
    {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = f;
        getWindow().setAttributes(params);

    }

    public void hideSoftInput()
    {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // 调接口时，帐号被挤掉
    public void toLogin()
    {
        PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_LastLogin,null);
        CustomToast.show("登录信息失效，请重新登录");
        Intent intent=new Intent();
        intent.setClass(this,MainActivity.class);
        intent.putExtra("type","Crowding");
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();
    }

    private void setStatusBarTrans() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;//状态栏透明
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }

        if (!(this instanceof StartActivity)&&!(this instanceof PersonalActivity) &&!(this instanceof GuideActivity)
                &&!(this instanceof LoginActivity)&&!(this instanceof RegisterActivity)&&!(this instanceof MailAppraisalActivity)
                &&!(this instanceof ImagePageActivity)&&!(this instanceof OtherActivity)&&!(this instanceof VideoListActivity)
                &&!(this instanceof SingleVideoActivity)&&!(this instanceof MonitorDetailActivity)
                &&!(this instanceof EmeraldActivity)&&!(this instanceof JewelleryActivity)
                &&!(this instanceof WatchActivity)&&!(this instanceof ImageActivity)&&!(this instanceof CashItemsActivity)
                &&!(this instanceof SendCallGoodDetailActivity) &&!(this instanceof GoodsDetailActivity)
                &&!(this instanceof AddSendCallActivity)){
            tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setTintColor(getResources().getColor(R.color.bg_title));
        }
    }
    //MD5加密
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    // 登录记住密码
    public static void setUserAndFile(UserInfo info)
    {
        LocalData.getInstance().setUserInfo(info);
        PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_LastLogin, new Gson().toJson(info));
    }

    private PopupWindow popupWindo;
    public PopupWindow getPopupWindow(){
        if(popupWindo==null){
            return null;
        }
        return popupWindo;
    }
    public void showPopupWindow(Activity activity,String content, final OnClickListener makesureListener)
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.pw_dialog1, null);
        Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button)view.findViewById(R.id.btn_ok);
        if(!content.equals("")){
            ((TextView)view.findViewById(R.id.tv_content)).setText(content);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
            }
        });
        btn_ok.setOnClickListener(makesureListener);
        Display display = activity.getWindowManager().getDefaultDisplay();
        popupWindo = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindo.setOutsideTouchable(true);
        popupWindo.setFocusable(true);
        fitPopupWindowOverStatusBar(popupWindo);
        popupWindo.setBackgroundDrawable(new ColorDrawable());
        popupWindo.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void showVideoPopupWindow(Activity activity,final OnClickListener Listener, final OnClickListener Listener1)
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.pw_photo, null);
        Button pw_btn_photograph = (Button)view.findViewById(R.id.pw_btn_photograph);
        Button pw_btn_album = (Button)view.findViewById(R.id.pw_btn_album);
        Button pw_btn_cancle = (Button)view.findViewById(R.id.pw_btn_cancle);

        pw_btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
            }
        });
        pw_btn_photograph.setOnClickListener(Listener);
        pw_btn_album.setOnClickListener(Listener1);
        Display display = activity.getWindowManager().getDefaultDisplay();
        popupWindo = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindo.setOutsideTouchable(true);
        popupWindo.setFocusable(true);
        fitPopupWindowOverStatusBar(popupWindo);
        popupWindo.setBackgroundDrawable(new ColorDrawable());
        popupWindo.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private PopupWindow popwindow;

    public void showPopupWindow(Activity activity,final TextView tv, final ArrayList<String> list)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.pw_listview, null);

        ListView lv_district = ((ListView) view.findViewById(R.id.lv_content));
        lv_district.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                popwindow.dismiss();
                tv.setText(list.get(position));
            }
        });

        CommonAdapter commAdapter = new CommonAdapter<String>(activity, list,
                R.layout.item_listview_simple) {
            @Override
            public void convert(final ViewHolder helper, final String item) {
                helper.setText(R.id.textview, item);
            }
        };
        lv_district.setAdapter(commAdapter);

        int[] location = new int[2];
        tv.getLocationOnScreen(location);

        int aHeight = tv.getHeight() + location[1];
        int tHeight = getWindowManager().getDefaultDisplay().getHeight() - aHeight;
        int aWidth = tv.getWidth();
        popwindow = new PopupWindow(view, aWidth - ScreenUtils.dp2px(this,0), tHeight, true);
        popwindow.setOutsideTouchable(true);
        fitPopupWindowOverStatusBar(popwindow);
        popwindow.setBackgroundDrawable(new ColorDrawable());
        popwindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0]+ScreenUtils.dp2px(this, 0), aHeight);
    }

    public void fitPopupWindowOverStatusBar(PopupWindow popwindow) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(popwindow, true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 设置图片---自定义图片4个角中的指定角为圆角
     * @param url 图片的url
     * @param cornerRadius 圆角像素大小
     * @param corners 自定义圆角:<br>
     * 以下参数为FlexibleRoundedBitmapDisplayer中静态变量:<br>
     * CORNER_NONE　无圆角<br>
     * CORNER_ALL 全为圆角<br>
     * CORNER_TOP_LEFT | CORNER_TOP_RIGHT | CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT　指定圆角（选其中若干组合  ） <br>
     * @param image url为空时加载该图片
     * @param imageView 要设置图片的ImageView
     */

    public void setRoundedImage(String url, int cornerRadius, int corners, int image, ImageView imageView) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(image).showStubImage(image)
                .showImageForEmptyUri(image)//url为空时显示的图片
                .showImageOnFail(image)//加载失败显示的图片
                .cacheInMemory()//内存缓存
                .cacheOnDisc()//磁盘缓存
                .displayer(new FlexibleRoundedBitmapDisplayer(cornerRadius,corners)) // 自定义增强型BitmapDisplayer
                .build();
        imageLoader.displayImage(url, imageView, options);

    }


    /**
     * 权限回调接口
     */
    public interface CheckPermListener {
        //权限通过后的回调方法
        void superREADPermission();
    }

    public void requestReadCameraPermissions(final CheckPermListener listener) {
        /**
         * 点击检查 相机、存储 权限
         */
        PermissionUtil permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                new PermissionListener() {
                    @Override
                    public void onGranted() {
                        //所有权限都已经授权
                        if (listener != null)
                            listener.superREADPermission();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        //Toast第一个被拒绝的权限
                        CustomToast.show(getResources().getString(R.string.ReadCamera_toast));
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        //Toast第一个勾选不在提示的权限
                        CustomToast.show(getResources().getString(R.string.ReadCamera_toast));
                    }
                });
    }

    public void requestReadPermissions(final CheckPermListener listener) {
        /**
         * 点击检查 存储 权限
         */
        PermissionUtil permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new PermissionListener() {
                    @Override
                    public void onGranted() {
                        //所有权限都已经授权
                        if (listener != null)
                            listener.superREADPermission();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        //Toast第一个被拒绝的权限
                        CustomToast.show(getResources().getString(R.string.Read_toast));
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        //Toast第一个勾选不在提示的权限
                        CustomToast.show(getResources().getString(R.string.Read_toast));
                    }
                });
    }

    public void requestCAMERAPermissions(final CheckPermListener listener) {
        /**
         * 点击检查 相机、录音 权限
         */
        PermissionUtil permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO},
                new PermissionListener() {
                    @Override
                    public void onGranted() {
                        //所有权限都已经授权
                        if (listener != null)
                            listener.superREADPermission();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        //Toast第一个被拒绝的权限
                        CustomToast.show(getResources().getString(R.string.Camera_toast));
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        //Toast第一个勾选不在提示的权限
                        CustomToast.show(getResources().getString(R.string.Camera_toast));
                    }
                });
    }

    /**
     * 获取分享详情
     */
    private void getShareInfo(String id) {
        String url=BaseConstant.getApiPostUrl("userGoods/getShareInfo");
        HttpParams param=new HttpParams();
        param.put("id", id);

        OkGo.<DataResult<StoreGoodsInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<StoreGoodsInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<StoreGoodsInfo>> response) {

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData()!=null){
                                showSharePopupWindow(response.body().getData());
                            }
                        }
                    }
                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<StoreGoodsInfo>> response) {

                    }
                });
    }

    public void showSharePopupWindow(final StoreGoodsInfo info)
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.pw_shareinfo, null);
        ImageView iv_goodimage = view.findViewById(R.id.iv_good_image);
        TextView tv_goodname = view.findViewById(R.id.tv_goodname);
        TextView tv_open = view.findViewById(R.id.tv_open);
        ImageView iv_close = view.findViewById(R.id.iv_close);

        ImageLoader.getInstance().displayImage(BaseConstant.Image_URL
                        + info.getImg(), iv_goodimage,getImageLoaderOptions());

        tv_goodname.setText(info.getName());

        iv_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                popupWindo.dismiss();
            }
        });
        tv_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindo.dismiss();
//                Intent intent = new Intent(BaseActivity.this, GoodsDetailActivity.class);
//                if (info.getType().equals("1")){
//                    intent.putExtra("type","rz");
//                }else{
//                    intent.putExtra("type","jd");
//                }
//                intent.putExtra("id",info.getId());
//                startActivity(intent);

                Helper.startActivity(
                        BaseActivity.this,
                        FrgProductDetail.class,
                        TitleAct.class,
                        "id",
                        info.getId(), "type", info.getType().equals("1")?"rz":"jd"
                );
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
}

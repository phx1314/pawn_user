package com.glavesoft.pawnuser.base;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

import androidx.multidex.MultiDex;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.JpushInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.shoppingcar.DBHelper;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.Gson;
import com.guoxiaoxing.phoenix.core.PhoenixConfig;
import com.guoxiaoxing.phoenix.picker.Phoenix;
import com.mdx.framework.Frame;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sobot.chat.SobotApi;
import com.sobot.chat.SobotUIConfig;
import com.xsj.crasheye.Crasheye;

import java.io.File;
import java.lang.reflect.Field;

import cn.jpush.android.api.JPushInterface;

public class BaseApplication extends Application {
    private static BaseApplication instance;
    static String Save_RootFile = "pawn_user"; // 应用的存放的文件夹
    public static final String CACHE_IMG_TEMP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Save_RootFile + "/cache/temp/";
    private Activity activity;
    private boolean isshow = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Frame.init(getApplicationContext());
        Phoenix.config()
                .imageLoader(new com.guoxiaoxing.phoenix.core.listener.ImageLoader() {
                    @Override
                    public void loadImage(Context context, ImageView imageView, String imagePath, int type) {
                        Glide.with(context)
                                .load(imagePath)
                                .into(imageView);
                    }
                });

        if (instance == null) {
            instance = this;
        }
        MultiDex.install(this);
        VolleyUtil.initialize(this);

        initImageLoader(getApplicationContext());
        Crasheye.init(this, "9d7884e0");
        SobotApi.initSobotSDK(this, BaseConstant.appkey, LocalData.getInstance().getUserInfo().getId());
        SobotApi.setNotificationFlag(this, true, R.drawable.smallicon, R.mipmap.ic_launcher);
        SobotUIConfig.sobot_titleBgColor = R.color.bg_title;
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
        setBroadCast();
        DBHelper.init(getApplicationContext());
        Fresco.initialize(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    public static BaseApplication getInstance() {
        if (instance == null) {
            instance = new BaseApplication();
        }
        return instance;
    }

    //接受推送，并显示出对话框
    public void setBroadCast() {
        IntentFilter f = new IntentFilter();
        f.addAction("Jpush_com_glavesoft_pawnuser");
        registerReceiver(mReceiver, f);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonstr = intent.getStringExtra("msg");
            //System.out.println("jpush----------------->" + jsonstr);
            if (AppManager.getAppManager().getActivity() != null && AppManager.getAppManager().getActivity().size() != 0) {//跳推送对话框
                try {
                    JpushInfo jpushInfo = new Gson().fromJson(jsonstr, JpushInfo.class);
                    String token = LocalData.getInstance().getUserInfo().getToken();
                    if (jpushInfo != null && token != null && !token.equals("")) {
                        activity = AppManager.getAppManager().getLastActivity();
                        if (!isshow) {
                            isshow = true;
                            showPopupWindow(activity, jpushInfo.getMsg());
                        }


//						if (activity instanceof MainActivity) {//如果在订单列表页，刷新
//							((MainActivity) activity).getlDialog().show();
//							((MainActivity) activity).refresh();
//							((MainActivity) activity).needRefresh = false;
//						}
                    }

                } catch (Exception e) {
                    JPushInterface.clearNotificationById(context, intent.getIntExtra("notificationId", 0));
                }
            }
        }
    };

    private PopupWindow popupWindo;

    public void showPopupWindow(Activity activity, String pushmsg) {
        if (popupWindo != null && popupWindo.isShowing()) {
            popupWindo.dismiss();
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.pw_dialog2, null);

        TextView tv_ts_dialog = (TextView) view.findViewById(R.id.tv_ts_dialog);
        TextView tv_content_dialog = (TextView) view.findViewById(R.id.tv_content_dialog);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);

        tv_ts_dialog.setText("提示");
        tv_content_dialog.setText(pushmsg);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindo.dismiss();
            }
        });
        Display display = activity.getWindowManager().getDefaultDisplay();
        popupWindo = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindo.setOutsideTouchable(true);
        popupWindo.setFocusable(true);
        fitPopupWindowOverStatusBar(popupWindo);
        popupWindo.setBackgroundDrawable(new ColorDrawable());
        popupWindo.showAtLocation(view, Gravity.CENTER, 0, 0);
        isshow = false;
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

    public static void initImageLoader(Context context) {
        String cacheDir = CACHE_IMG_TEMP_PATH;
        File file = new File(cacheDir);
        if (!file.exists()) {
            file.mkdirs();
        }

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(720, 1280)
                .threadPoolSize(2)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                // .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
                .memoryCache(null)
                .memoryCacheSize((int) (50 * 1024 * 1024))
                .memoryCacheSizePercentage(13)
                // default
                .diskCache(new UnlimitedDiskCache(file))
                // default
                .diskCacheSize(80 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(defaultOptions).writeDebugLogs();// Remove
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

    }
}

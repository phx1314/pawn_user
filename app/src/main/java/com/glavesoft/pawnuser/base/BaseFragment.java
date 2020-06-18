package com.glavesoft.pawnuser.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.MainActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.UserInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.FlexibleRoundedBitmapDisplayer;
import com.glavesoft.view.LoadingDialog;
import com.google.gson.Gson;
import com.mdx.framework.Frame;
import com.mdx.framework.service.subscriber.HttpResultSubscriberListener;
import com.mdx.framework.utility.AbAppUtil;
import com.mdx.framework.utility.Helper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.Information;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseFragment extends Fragment   {
    protected TextView titlebar_left;
    protected TextView titlebar_right;
    protected TextView titlebar_name;
    protected ImageView titlebar_back;
    protected ImageView titlebar_search;
    private ImageLoader imageLoader;
    private DisplayImageOptions options, optionsHead;
    private LoadingDialog lDialog;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }

        return imageLoader;
    }

    public DisplayImageOptions getImageLoaderOptions() {
        if (options == null) {
            options = new DisplayImageOptions.Builder().showStubImage(R.drawable.sy_bj).showImageForEmptyUri(R.drawable.sy_bj).
                    cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        return options;
    }

    public DisplayImageOptions getImageLoaderHeadOptions() {
        if (optionsHead == null) {
            optionsHead = new DisplayImageOptions.Builder().showStubImage(R.drawable.tx).showImageForEmptyUri(R.drawable.tx).
                    cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        return optionsHead;
    }

    public String setLastUpdateTime() {
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        return "最后更新时间 :" + text;
    }

    public LoadingDialog getlDialog() {
        if (lDialog == null) {
            lDialog = new LoadingDialog(getActivity());
        }

        return lDialog;
    }

    public void showVolleyError(VolleyError error) {
        CustomToast.show("网络请求失败，请重试");

        if (error != null) {
            String msg = error.getMessage();

            if (msg != null) {
                Log.e("VolleyError", msg);
            }
        }
    }

    //客服
    public void gotokf(Context context) {
        Information info = new Information();
        info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
        //用户资料
        if (BaseConstant.isLogin()) {
            info.setUid(LocalData.getInstance().getUserInfo().getId());
            info.setUname(LocalData.getInstance().getUserInfo().getId());
            info.setTel(LocalData.getInstance().getUserInfo().getAccount());
            info.setFace(BaseConstant.Image_URL + LocalData.getInstance().getUserInfo().getHeadImg());
        } else {
            info.setUid(CommonUtils.getDeviceId(getActivity()));
            info.setUname("游客");
        }
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(4);
        //设置聊天界面标题显示模式
        SobotApi.setChatTitleDisplayMode(context, SobotChatTitleDisplayMode.Default, "");

        SobotApi.startSobotChat(context, info);
    }

    public void gotokf_J(Context context) {
        Information info = new Information();
        info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
        //用户资料
        if (BaseConstant.isLogin()) {
            info.setUid(LocalData.getInstance().getUserInfo().getId());
            info.setUname(LocalData.getInstance().getUserInfo().getId());
            info.setTel(LocalData.getInstance().getUserInfo().getAccount());
            info.setFace(BaseConstant.Image_URL + LocalData.getInstance().getUserInfo().getHeadImg());
        } else {
            info.setUid(CommonUtils.getDeviceId(getActivity()));
            info.setUname("游客");
        }
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(4);
        //转接类型(0-可转入其他客服，1-必须转入指定客服)
        info.setTranReceptionistFlag(1);
        //指定客服id
        info.setReceptionistId("71764f63c3ca497ba974f938b26389eb");
        //设置聊天界面标题显示模式
        SobotApi.setChatTitleDisplayMode(context, SobotChatTitleDisplayMode.Default, "");
        SobotApi.setNotificationFlag(context, true, R.mipmap.ic_launcher, R.mipmap.ic_launcher);

        SobotApi.startSobotChat(context, info);
    }

    public void gotokf_Z(Context context) {
        Information info = new Information();
        info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
        //用户资料
        if (BaseConstant.isLogin()) {
            info.setUid(LocalData.getInstance().getUserInfo().getId());
            info.setUname(LocalData.getInstance().getUserInfo().getId());
            info.setTel(LocalData.getInstance().getUserInfo().getAccount());
            info.setFace(BaseConstant.Image_URL + LocalData.getInstance().getUserInfo().getHeadImg());
        } else {
            info.setUid(CommonUtils.getDeviceId(getActivity()));
            info.setUname("游客");
        }
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(4);
        //转接类型(0-可转入其他客服，1-必须转入指定客服)
        info.setTranReceptionistFlag(1);
        //指定客服id
        info.setReceptionistId("b125bade408341d4b1c825ee56a1dbb8");
        //设置聊天界面标题显示模式
        SobotApi.setChatTitleDisplayMode(context, SobotChatTitleDisplayMode.Default, "");

        SobotApi.startSobotChat(context, info);
    }

    // 调接口时，帐号被挤掉
    public void toLogin() {
        PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_LastLogin, null);
        CustomToast.show("登录信息失效，请重新登录");
        Intent intent = new Intent();
        intent.setClass(getActivity(), MainActivity.class);
        intent.putExtra("type", "Crowding");
        startActivity(intent);
    }

    // 登录记住密码
    public static void setUserAndFile(UserInfo info) {
        LocalData.getInstance().setUserInfo(info);
        PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_LastLogin, new Gson().toJson(info));
    }

    private PopupWindow popupWindo;

    public PopupWindow getPopupWindow() {
        if (popupWindo == null) {
            return null;
        }
        return popupWindo;
    }

    public void showPopupWindow(Activity activity, String content, final View.OnClickListener makesureListener) {
        if (popupWindo != null) {
            popupWindo = null;
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.pw_dialog, null);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        GradientDrawable myGrad = (GradientDrawable) btn_ok.getBackground();
        myGrad.setColor(activity.getResources().getColor(R.color.blue));
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                .displayer(new FlexibleRoundedBitmapDisplayer(cornerRadius, corners)) // 自定义增强型BitmapDisplayer
                .build();
        imageLoader.displayImage(url, imageView, options);

    }



    /**
     * 权限回调接口
     */
    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }

    public void requestLOCATIONPermissions(final CheckPermListener listener) {
        /**
         * 点击检查 位置 存储权限
         */
        PermissionUtil permissionUtil = new PermissionUtil(getActivity());
        permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                new PermissionListener() {
                    @Override
                    public void onGranted() {
                        //所有权限都已经授权
                        if (listener != null)
                            listener.superPermission();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        //Toast第一个被拒绝的权限
                        CustomToast.show(getResources().getString(R.string.Location_toast));
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        //Toast第一个勾选不在提示的权限
                        CustomToast.show(getResources().getString(R.string.Location_toast));
                    }
                });
    }
}

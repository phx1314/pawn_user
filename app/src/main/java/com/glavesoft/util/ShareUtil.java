package com.glavesoft.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.view.CustomToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2017/8/28.
 */

public class ShareUtil implements View.OnClickListener {

    private String id;
    private PopupWindow sharePop;
    private Context context;
    private String iconPath = FileUtils.CACHE_SAVE_IMG_PATH + "share.png";
    public ShareUtil(Context context,String id){
        this.context=context;
        this.id=id;
        saveImage(context);
    }

    public void showSharePopupWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pw_share, null);

        if (sharePop == null) {
            view.findViewById(R.id.iv_share_qq).setOnClickListener(this);
            view.findViewById(R.id.iv_share_wx).setOnClickListener(this);
            view.findViewById(R.id.iv_share_pyq).setOnClickListener(this);
            view.findViewById(R.id.iv_share_qqkongj).setOnClickListener(this);
            view.findViewById(R.id.share_cancle).setOnClickListener(this);

            sharePop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
            // 设置点击窗口外边窗口消失
            sharePop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_trans));
            sharePop.setOutsideTouchable(true);
            // 设置此参数获得焦点，否则无法点击
            sharePop.setFocusable(true);
        }

        if (!sharePop.isShowing()) {
            sharePop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }

    }

    private void showShare(String platform) {
        ShareSDK.initSDK(context);

        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("蚌蚌拍当专业鉴宝、典当平台");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("宝贝换钱立即到账，省时省力省心，快进来看视频领取优惠券吧！！");

        String url=BaseConstant.BaseURL+"/m/video/getShareVideo?id="+id;
        if(platform.equals(Wechat.NAME)||platform.equals(WechatMoments.NAME)){
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(url);
        }else{
            // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
            oks.setTitleUrl(url);
        }
        if(iconPath != null && ImageUtils.IsImageFileExist(iconPath)){
            oks.setImagePath(iconPath);
        }

        //启动分享
        oks.show(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_share_qq:
                if (!CommonUtils.isQQInstalled(context)) {
                    CustomToast.show("未检测到QQ客户端，请先安装");
                    return;
                }
                showShare(QQ.NAME);
                sharePop.dismiss();
                break;
            case R.id.iv_share_wx:
                if (!CommonUtils.isWechatInstalled(context)) {
                    CustomToast.show("未检测到微信客户端，请先安装");
                    return;
                }
                showShare(Wechat.NAME);
                sharePop.dismiss();
                break;
            case R.id.iv_share_pyq:
                if (!CommonUtils.isWechatInstalled(context)) {
                    CustomToast.show("未检测到微信客户端，请先安装");
                    return;
                }
                showShare(WechatMoments.NAME);
                sharePop.dismiss();
                break;
            case R.id.iv_share_qqkongj:
                if (!CommonUtils.isQQInstalled(context)) {
                    CustomToast.show("未检测到QQ客户端，请先安装");
                    return;
                }
                showShare(QZone.NAME);
                sharePop.dismiss();
                break;
            case R.id.share_cancle:
                if(sharePop!=null&&sharePop.isShowing()){
                    sharePop.dismiss();
                }
                break;
        }

    }

    //保存本地图片
    private void saveImage(Context context) {
//        boolean isExist = ImageUtils.IsImageFileExist(iconPath);
//        if(!isExist){

            InputStream is;
            FileOutputStream fos = null;
            try {
                is = context.getAssets().open("share.png");
                FileUtils.writeTextFile(new File(iconPath), is);
            }catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                    }
                }

//            }
        }
    }
}

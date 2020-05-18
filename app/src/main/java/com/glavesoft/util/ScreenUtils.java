package com.glavesoft.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.glavesoft.pawnuser.base.BaseApplication;


public class ScreenUtils {

	private static ScreenUtils instance;
    private static int mWidth =0;
    private static int mHeight=0;
    private static float mscaledDensity;
    private int default_Width=0;
    private int default_Height=0;

    public static ScreenUtils getInstance()   {
    	if(null == instance){
    		instance = new ScreenUtils();
    		DisplayMetrics metrics = new DisplayMetrics();
    		metrics = BaseApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics();
    		setScreenUtil(metrics.widthPixels, metrics.heightPixels, metrics.density);
    	}
    	return instance;
    }

    public int getDefault_Width() {
    	default_Width = getWidth()*440/480;
		return default_Width;
	}

	public int getDefault_Height() {
    	default_Height = getHeight()*650/800;
		return default_Height;
	}

    public float getScaledDensity() {
		return mscaledDensity;
	}

	public int getWidth() {
		if(mWidth == 0){
			DisplayMetrics metrics = new DisplayMetrics();
    		metrics = BaseApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics();
    		setScreenUtil(metrics.widthPixels, metrics.heightPixels, metrics.density);  //��ȡ��Ļ�ֱ���
		}
        return mWidth;
    }

    public  int getHeight() {
    	if(mHeight == 0){
			DisplayMetrics metrics = new DisplayMetrics();
    		metrics = BaseApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics();
    		setScreenUtil(metrics.widthPixels, metrics.heightPixels, metrics.density);  //��ȡ��Ļ�ֱ���
		}
        return mHeight;
    }

    public static void setScreenUtil(int width,int height,float scaledDensity){
    	mWidth = width;
    	mHeight = height;
    	mscaledDensity = scaledDensity;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context,float px){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(px / scale + 0.5f);
    }

	/**
	 * sp转px的方法。
	 */
	public static int sp2px(Context context,float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result =context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}

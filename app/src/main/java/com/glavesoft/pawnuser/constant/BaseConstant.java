package com.glavesoft.pawnuser.constant;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;

import com.glavesoft.pawnuser.activity.main.WebActivity;
import com.glavesoft.util.PreferencesUtils;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseConstant
{
	//测式
//	public final static String BaseURL = "http://baidu.paidangwang.net/paidangAdmin";
	public final static String URL = "http://baidu.paidangwang.net/paidangUserApi/api/";

    //正式
	public final static String BaseURL = "http://baidu.paidangwang.net/admin";
//	public final static String URL = "http://baidu.paidangwang.net/user-api/api/";

	//保存数据库名称等
	public final static String AccountManager_NAME = "com.glavesoft.pawnuser";
	public final static String SharedPreferences_LastLogin = "LastLogin";
	public final static String SharedPreferences_Historical = "Historical";
	public final static String SharedPreferences_Pay = "pay";

	public final static  String UploadAvatar_URL=URL+"common/upload";//上传文件接口
	public final static  String Image_URL=URL+"common/download?id=";//图片文件接口
	public final static  String Video_URL=URL+"common/download?id=";//视频文件接口
	public final static String API_URL = URL + "/mobile";
	public static boolean ISFACE = false;//是否人脸识别

	public static String appkey = "e9cc7fa955a94500b364641e84adcc35";
	public static int index = 0;
	public static boolean isCopy = false;
	public static int maxSize = 300*1024*1024;

	public static String getApiGetUrl(String apiname,Map<String, String> param){
		String urlGet = "";
		urlGet = URL + apiname;
		if(param != null){
			Iterator<String> iterator = param.keySet().iterator();
			int i=0;
			while(iterator.hasNext()) {
				String key = iterator.next();
				if(i==0){
					urlGet = urlGet +"?"+key +"="+param.get(key);
				}else{
					urlGet = urlGet +"&"+key +"="+param.get(key);
				}
				i++;
			}
		}
		System.out.println("url-->"+urlGet);
		return urlGet;
	}
	public static String getApiPostUrl(String apiname){
		String urlPost = "";
		urlPost = URL+ apiname;
		System.out.println("url-->"+urlPost);
		return urlPost;
	}

	public static boolean isLogin(){
		if(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_LastLogin,null)==null){
			return false;
		}else{
			return true;
		}
	}

	public static boolean isVideo(String url){
		String suffix = url.substring(url.lastIndexOf(".") + 1);
		if(suffix.equals("mp4")||suffix.equals("mkv")||suffix.equals("avi")||
				suffix.equals("rm")||suffix.equals("rmvb")){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 获取指定文件大小
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()){
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		}
		else{
			file.createNewFile();
			Log.e("获取文件大小","文件不存在!");
		}
		return size;
	}

	public static class EditInputFilter implements InputFilter {
		/**
		 * 最大数字
		 */
		public static final int MAX_VALUE = 10000;
		/**
		 * 小数点后的数字的位数
		 */
		public static  int PONTINT_LENGTH = 2;
		Pattern p;
		EditText et;
		public EditInputFilter(EditText et){
			p = Pattern.compile("[0-9]*");   //除数字外的其他的
			this.et=et;
		}
		public EditInputFilter(EditText et,int length){
			p = Pattern.compile("[0-9]*");   //除数字外的其他的
			this.et=et;
			PONTINT_LENGTH=length;
		}

		/**
		 *  source    新输入的字符串
		 *  start    新输入的字符串起始下标，一般为0
		 *  end    新输入的字符串终点下标，一般为source长度-1
		 *  dest    输入之前文本框内容
		 *  dstart    原内容起始坐标，一般为0
		 *  dend    原内容终点坐标，一般为dest长度-1
		 */

		@Override
		public CharSequence filter(CharSequence src, int start, int end,
								   Spanned dest, int dstart, int dend) {
			String oldtext =  dest.toString();
			System.out.println(oldtext);
			//验证删除等按键
			if ("".equals(src.toString())) {
				return null;
			}
			//验证非数字或者小数点的情况
			Matcher m = p.matcher(src);
			if(et.getText().toString().trim().equals("")){
				if(!m.matches()){
					et.setText("");
					return null;
				}
			}else{
				if(oldtext.contains(".")){
					//已经存在小数点的情况下，只能输入数字
					if(!m.matches()){
						return null;
					}
				}else{
					//未输入小数点的情况下，可以输入小数点和数字
					if(!m.matches() && !src.equals(".") ){
						return null;
					}
				}
			}
			//验证输入金额的大小
//	            if(!src.toString().equals("")){
//	                double dold = Double.parseDouble(oldtext+src.toString());
//	                if(dold > MAX_VALUE){
//	                    ForumToast.show("输入的最大金额不能大于MAX_VALUE");
//	                    return dest.subSequence(dstart, dend);
//	                }else if(dold == MAX_VALUE){
//	                    if(src.toString().equals(".")){
//	                    	ForumToast.show("输入的最大金额不能大于MAX_VALUE");
//	                        return dest.subSequence(dstart, dend);
//	                    }
//	                }
//	            }
			//验证小数位精度是否正确
			if(oldtext.contains(".")){
				int index = oldtext.indexOf(".");
				int len = dend - index;
				//小数位只能2位
				if(len > PONTINT_LENGTH){
					CharSequence newText = dest.subSequence(dstart, dend);
					return newText;
				}
			}
			return dest.subSequence(dstart, dend) +src.toString();
		}
	}

	/**
	 * 注册协议
	 */
	public static void gotoAgreement(Context mContext){
		Intent intent = new Intent(mContext, WebActivity.class);
//		intent.putExtra("url", BaseConstant.BaseURL+"/m/pawn/getRegistrations");
		intent.putExtra("url", BaseConstant.BaseURL+"/detail?code=yhxy&type=3&id=0");
		intent.putExtra("titleName", "注册协议");//表明是注册协议
		mContext.startActivity(intent);
	}

	/**
	 * 隐私政策
	 */
	public static void gotoPrivacy(Context mContext){
		Intent intent = new Intent(mContext,WebActivity.class);
		intent.putExtra("url", BaseConstant.BaseURL+"/detail?code=ysxy&type=3&id=0");
		intent.putExtra("titleName", "隐私政策");
		mContext.startActivity(intent);
	}
}

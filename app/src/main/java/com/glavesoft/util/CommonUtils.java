package com.glavesoft.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.ParseException;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.core.app.ActivityCompat;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.blankj.utilcode.util.DeviceUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文 件 名 : CommonUtils 创 建 人： gejian 日 期：2013-11-4 修 改 人：gejian 日 期：2013-11-4 描
 * 述：常用的程序的方法类
 */
public class CommonUtils {

	private final static String TAG = CommonUtils.class.getName();

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static String SETTING_NAME = TAG + "_SETTING_NAME";
	public static String CONTACTS_NAME = TAG + "_CONTACTS_NAME";

	// SharedPreferences的key
	public static final String SP_isInstallShortCut = "isInstallShortCut"; // 是否添加桌面图标的key索引
	public static final String SP_isFirstOpenSoft = "isFirstOpenSoft"; // 是否
	// 首次加载应用的key索引
	public static final String SP_LoginInfo = "LoginInfo"; // 用户列表信息的key索引
	public static final String SP_LastLogin = "LastLogin"; // 最近登录用户信息的key索引
	public static final String SP_SpeechInfo = "SpeechInfo"; // 合成信息的key索引
	public static final String SP_ZcYzm = "ZcYzm"; // 注册验证码key索引
	public static final String SP_FindpswYzm = "FindpswYzm"; // 找回密码验证码key索引

	public static final String SP_COOKIES = "Cookies"; // cookies的key索引
	public static final String SP_CarInfo = "CarInfo"; // 选择的车辆信息的key索引

	public static final String SP_RememberUser = "RememberUser"; // 选择的车辆信息的key索引

	public static final String SP_LastShopInfo = "LastShop";//最后一次的商铺信息记录

	/**
	 * json数据解析
	 *
	 * @param json
	 * @param classtype
	 * @param datePattern
	 * @return
	 */
	public static <T> T fromJson(String json, Type classtype, String datePattern) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		GsonBuilder builder = new GsonBuilder();
		if (StringUtils.isEmpty(datePattern)) {
			datePattern = DEFAULT_DATE_PATTERN;
		}
		builder.setDateFormat(datePattern);

		Gson gson = builder.create();
		T Object = null;
		try {
			try {
				Object = (T) gson.fromJson(json, classtype);
				return Object;
			} catch (JsonParseException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取本地Ip
	 *
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}

	/*
	 * 获取String里面的<img> 的url列表
	 */
	public static ArrayList<String> getPicUrlList(String contentstring) {
		String tmpString = contentstring;
		ArrayList<String> picurl_list = new ArrayList<String>(1);

		Pattern p = Pattern.compile("<img.*src=(.*?)[^>]*?>",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(tmpString); // 找到非表情<img>标签中的url放到list中
		while (m.find()) {
			String imgTmpString = m.group();
			imgTmpString = imgTmpString.replace("<img src=", "")
					.replace("/ >", "").replace("/>", "");
			if (!picurl_list.contains(imgTmpString)) {
				picurl_list.add(imgTmpString);
			}
		}
		return picurl_list;
	}

	/*
	 * 获取String里面的<img> 的url列表
	 */
	public static ArrayList<String> getContentPicUrlList(String contentstring) {
		String tmpString = contentstring;
		ArrayList<String> picurl_list = new ArrayList<String>(1);

		Pattern pattern1 = Pattern
				.compile(
						"(onclick+=\".*?\")|(onload+=\".*?\")|(style+=\".*?\")|(border+=\".*?\")",
						Pattern.CASE_INSENSITIVE);
		Matcher matcher1 = pattern1.matcher(contentstring); // 找到<img>
		// 标签中的非src的属性置空
		while (matcher1.find()) {
			String imgTmpString = matcher1.group();
			tmpString = tmpString.replace(imgTmpString, "");
		}

		Pattern p = Pattern.compile(
				"(?<=src=['\"])http://((?!/post/smile/).)*?(?=['\"])",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(tmpString); // 找到非表情<img>标签中的url放到list中
		while (m.find()) {
			String imgTmpString = m.group();
			if (!picurl_list.contains(imgTmpString)) {
				if (imgTmpString.contains("commonapi/images/back.gif")) {
					continue;
				}
				picurl_list.add(imgTmpString);
			}
		}

		return picurl_list;
	}

	/*
	 * 设置新的String 待解决 表情替换
	 */
	public static String setNewContent(String contentstring) { // 替换img标签
		String tmpString = contentstring;
		Pattern pattern1 = Pattern
				.compile(
						"(onclick+=\".*?\")|(onload+=\".*?\")|(style+=\".*?\")|(border+=\".*?\")|(smilieid+=\".*?\")|(alt+=\".*?\")",
						Pattern.CASE_INSENSITIVE);
		Matcher matcher1 = pattern1.matcher(contentstring); // 找到<img>
		// 标签中的非src的属性置空
		while (matcher1.find()) {
			String imgTmpString = matcher1.group();
			tmpString = tmpString.replace(imgTmpString, "");
		}

		Pattern pattern2 = Pattern.compile("<img[^<>]+src=['\"]http://[^<>]+>",
				Pattern.CASE_INSENSITIVE); // 匹配带有<img src="http:// >
		Matcher matcher2 = pattern2.matcher(tmpString); // 找到非表情<img>标签置空
		while (matcher2.find()) {
			String imgTmpString = matcher2.group();
			tmpString = tmpString.replace(imgTmpString, "");
		}

		// 过滤<p dir=ltr>
		tmpString = tmpString.replaceAll("<p dir=ltr>", "");

		// //替换表情 （对于表情的表达方式）
		// tmpString =
		// tmpString.replace(BaseConstant.FACE_IMG_CONTAIN_PATH,BaseConstant.url+BaseConstant.FACE_IMG_CONTAIN_PATH);
		// //替换表情的img标签

		return tmpString;
	}

	/**
	 * 验证邮箱的正则表达式
	 *
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		// 验证邮箱的正则表达式 //电子邮件
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 检验手机号码的合法性
	 *
	 * @param phoneNo
	 * @return
	 */
//	public static boolean checkPhoneNo(String phoneNo) {
//		Pattern p = Pattern
//				.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//		Matcher m = p.matcher(phoneNo);
//		boolean isMatched = m.matches();
//		return isMatched;
//	}
	public static boolean isCellphone(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 验证身份证号是否符合规则
	 * @param text 身份证号
	 * @return
	 */
	public boolean personIdValidation(String text) {
		String regx = "[0-9]{17}x";
		String reg1 = "[0-9]{15}";
		String regex = "[0-9]{18}";
		return text.matches(regx) || text.matches(reg1) || text.matches(regex);
	}


	/**
	 * 功能：身份证的有效验证
	 *
	 * @param IDStr
	 *            身份证号
	 * @return 有效：返回"" 无效：返回String信息
	 * @throws ParseException
	 */
	public static boolean IDCardValidate(String IDStr) throws ParseException {
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2"};
		String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2"};
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "身份证号码长度应该为15位或18位。";
			return false;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
			return false;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "身份证生日无效。";
			return false;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
					strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				errorInfo = "身份证生日不在有效范围。";
				return false;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "身份证月份无效";
			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "身份证日期无效";
			return false;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "身份证地区编码错误。";
			return false;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equalsIgnoreCase(IDStr) == false) {
				errorInfo = "身份证无效，不是合法的身份证号码";
				return false;
			}
		} else {
			return true;
		}
		// =====================(end)=====================
		return true;
	}


	/**
	 * 功能：判断字符串是否为数字
	 *
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：设置地区编码
	 *
	 * @return Hashtable 对象
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static Hashtable GetAreaCode() {
		Hashtable hashtable = new Hashtable();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/**
	 * 验证日期字符串是否是YYYY-MM-DD格式
	 *
	 * @param str
	 * @return
	 */
	private static boolean isDataFormat(String str) {
		boolean flag = false;
		// String
		// regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
		String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		Pattern pattern1 = Pattern.compile(regxStr);
		Matcher isNo = pattern1.matcher(str);
		if (isNo.matches()) {
			flag = true;
		}
		return flag;
	}


	/*
	  车牌号格式：汉字 + A-Z + 4位A-Z或0-9+学
	  教练车牌号验证
	 */
	public static boolean isCarnumberNO(String carnumber) {
		String carnumRegex = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{4}学";
		if (TextUtils.isEmpty(carnumber)) return false;
		else return carnumber.matches(carnumRegex);
	}


	/**
	 * 限制文本字数
	 *
	 * @param content
	 * @param limit_num
	 * @return
	 */
	public static String setTextLimit(String content, int limit_num) {
		int num = content.length();
		if (num > limit_num) {
			String content_new = content.substring(0, 137);
			content_new = content_new + "...";
			return content_new;
		}
		return content;
	}

	/**
	 * 通过Service的类名来判断是否启动某个服务
	 *
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean IsStartPushService(Context context, String className) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals(className)) {
				return true;
			}
		}
		return false;
	}

	// 调用系统联系人返回的数据
	public static String backContact(Context context, Intent intent) {
		Uri uri = intent.getData();
		// 得到ContentResolver对象
		ContentResolver cr = context.getContentResolver();
		// 取得电话本中开始一项的光标
		Cursor cursor = cr.query(uri, null, null, null, null);
		// 向下移动光标
		while (cursor.moveToNext()) {
			// 取得联系人名字
			int nameFieldColumnIndex = cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			String contact = cursor.getString(nameFieldColumnIndex);
			// 取得电话号码
			String ContactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phone = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
							+ ContactId, null, null);
			// 不只一个电话号码
			while (phone.moveToNext()) {
				String PhoneNumber = phone
						.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				return PhoneNumber;
			}
		}
		cursor.close();
		return "";
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static boolean isWechatInstalled(Context context) {
		return isAPPInstalled(context, "com.tencent.mm");
	}

	public static boolean isAlipayInstalled(Context context) {
		return isAPPInstalled(context, "com.eg.android.AlipayGphone");
	}

	public static boolean isQQInstalled(Context context) {
		return isAPPInstalled(context, "com.tencent.mobileqq");
	}

	public static boolean isSinaWeiboInstalled(Context context) {
		return isAPPInstalled(context, "com.sina.weibo");
	}

	public static boolean isAPPInstalled(Context context, String packageName) {
		List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				if (packageName.equals(pinfo.get(i).packageName)) {
					return true;
				}
			}
		}

		return false;
	}


	public static void setListViewHeightBasedOnChildren(ListView gridView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, gridView);
			// int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;
		gridView.setLayoutParams(params);
	}

	public static void setGridViewHeightBasedOnChildren(GridView gridView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, gridView);
			// int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;
		gridView.setLayoutParams(params);
	}


	public static void setPopGridViewHeight(GridView gridView, int dHeight) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, gridView);
			// int desiredWidth =
			// View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
			// View.MeasureSpec.AT_MOST);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		totalHeight = totalHeight + (listAdapter.getCount() - 1);

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = dHeight < totalHeight ? dHeight : totalHeight;
		// params.height最后得到整个ListView完整显示需要的高度
		gridView.setLayoutParams(params);
	}

	public static void setPopListViewHeight(ListView listView, int dHeight) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			// int desiredWidth =
			// View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
			// View.MeasureSpec.AT_MOST);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		totalHeight = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = dHeight < totalHeight ? dHeight : totalHeight;
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	/**
	 * 获取相机权限
	 */
	public static Boolean getPermisein(Context ctx, String permission) {
		Boolean flag = true;
		try {
			flag = isCameraEnable()
					&& PackageManager.PERMISSION_GRANTED == ctx.checkCallingOrSelfPermission(permission);
		} catch (Exception e) {
			return false;
		}
		return flag;
	}

	private static boolean isCameraEnable() {
		// Safe camera open for checking camera function.
		try {
			Camera camera = Camera.open();
			Camera.Parameters mParameters = camera.getParameters();
			camera.setParameters(mParameters);
			if (camera != null) {
				camera.release();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	/**
	 * 作用：用户是否同意录音权限
	 *
	 * @return true 同意 false 拒绝
	 */
	public static boolean isVoicePermission() {

		try {
			AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, 22050, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(22050, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT));
			record.startRecording();
			record.getRecordingState();
//            if(recordingState == AudioRecord.RECORDSTATE_STOPPED){
//                return false;
//            }
			record.release();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getDeviceId(Context context) {

		return DeviceUtils.getAndroidID();
//		try {
//			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//			if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//				String deviceId = tm.getDeviceId();
//				if (deviceId == null) {
//					return "1234567890";
//				} else {
//					return deviceId;
//				}
//			}else{
//				return "1234567890";
//			}
//
//		} catch (Exception e) {
//			return "1234567890";
//		}
	}

}
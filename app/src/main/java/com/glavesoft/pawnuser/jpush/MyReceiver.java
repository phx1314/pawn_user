package com.glavesoft.pawnuser.jpush;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.glavesoft.pawnuser.activity.main.MessageActivity;
import com.glavesoft.pawnuser.activity.main.StartActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver
{
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//		System.out.println("intent.getAction()--》"+intent.getAction());
//		System.out.println("[JpushReceiver] onReceive--》"+printBundle(bundle));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
		{
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
			sendBroadcast(context, bundle.getString(JPushInterface.EXTRA_EXTRA),bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));//发送广播

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			if(!isAppRunned(context, "com.glavesoft.pawnuser")){//如果应用没打开
				Intent i = new Intent(context, StartActivity.class);
				i.putExtra("type","push");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);
				//sendBroadcast(context, bundle.getString(JPushInterface.EXTRA_EXTRA),bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));//发送广播
			}else{
				Intent i = new Intent();
				i.setClass(context, MessageActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);
			}

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
		{
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
		} else
		{
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle)
	{
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet())
		{
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE))
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA))
			{
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty())
				{
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try
				{
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();

					while (it.hasNext())
					{
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
					}
				} catch (JSONException e)
				{
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	private void sendBroadcast(Context context, String json,int notificationId) {
		Intent broadIntent = new Intent();
		broadIntent.putExtra("msg", json);
		broadIntent.putExtra("notificationId", notificationId);
		broadIntent.setAction("Jpush_com_glavesoft_pawnuser");
		context.sendBroadcast(broadIntent);
	}

	public static boolean isAppRunned(Context context, String packageName)
	{
		boolean isAppRunning = false;

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1000);
		for (RunningTaskInfo info : list)
		{
			if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName))
			{
				isAppRunning = true;
				break;
			}
		}

		return isAppRunning;
	}
}

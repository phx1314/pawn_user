package com.glavesoft.pawnuser.base;

import android.app.Activity;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 */
public class AppManager
{
	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager()
	{

	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager()
	{
		if (instance == null)
		{
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public synchronized void addActivity(Activity activity)
	{
		if (activityStack == null)
		{
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity()
	{
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity()
	{
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public static void finishActivity(Activity activity)
	{
		if (activity != null)
		{
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public static synchronized void finishActivity(Class<?> cls)
	{
		for (int i = 0; i < activityStack.size(); i++)
		{
			Activity activity = activityStack.get(i);

			if (activity.getClass().equals(cls))
			{
				finishActivity(activity);
			}
		}
	}

	/**
	 * 指定类名的Activity是否打开
	 */
	public boolean isActivityExist(Class<?> cls)
	{
		for (int i = 0; i < activityStack.size(); i++)
		{
			Activity activity = activityStack.get(i);

			if (activity.getClass().equals(cls))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity()
	{
		if(activityStack == null) return;

		for (int i = 0; i < activityStack.size(); i++)
		{
			Activity activity = activityStack.get(i);

			if (activity != null)
			{
				activity.finish();
			}
		}

		activityStack.clear();
	}

	/**
	 * 结束除当前所有Activity
	 */
	public void finishAllActivityExceptCurrent()
	{
		for (int i = 0; i < activityStack.size() - 1; i++)
		{
			Activity activity = activityStack.get(i);

			if (activity != null)
			{
				activityStack.remove(activity);
				activity.finish();
			}
		}
	}

	/**
	 * 结束指定Activity之前所有Activity
	 */
	public void finishAllBeforeActivity(Class<?> cls)
	{
		while (activityStack.size() > 1)
		{
			Activity activity = activityStack.get(activityStack.size() - 2);

			if (activity != null)
			{
				if (activity.getClass().equals(cls))
				{
					return;
				}

				activity.finish();
			}

			activityStack.remove(activity);
		}
	}

	/**
	 * 获取Activity
	 */
	public Stack<Activity> getActivity()
	{
		if (activityStack == null)
		{
			activityStack = new Stack<Activity>();
		}
		return activityStack;
	}

	public Activity getLastActivity() {
		return activityStack.get(activityStack.size() - 1);
	}

	/**
	 * 退出应用程序
	 */
	public void exitApp()
	{
		finishAllActivity();
		System.exit(0);
	}
}
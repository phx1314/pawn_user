package com.glavesoft.pawnuser.mod;

import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.util.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

/**
 * 本地缓存数据
 *
 * @author gejian
 * @since 2014-01-09
 *
 */
public class LocalData
{
	private static LocalData localData = null;
	private UserInfo userInfo = null;

	public static synchronized LocalData getInstance()
	{
		if (localData == null)
		{
			localData = new LocalData();
		}
		return localData;
	}

	public UserInfo getUserInfo()
	{
		// 读取本地
		String jsonString_lastLogin = PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_LastLogin, null);
		if (jsonString_lastLogin != null && jsonString_lastLogin.length() > 0)
		{
			try
			{
				userInfo = new Gson().fromJson(jsonString_lastLogin, new TypeToken<UserInfo>()
				{
				}.getType());
			}
			catch (JsonParseException e)
			{
				e.printStackTrace();
				userInfo = new UserInfo();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				userInfo = new UserInfo();
			}
		}
		else
		{
			userInfo = new UserInfo();
		}

		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo)
	{
		this.userInfo = userInfo;
	}

	public UserInfo getUserInfoWithoutLocalFile()
	{
		return userInfo;
	}
}

package com.glavesoft.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.glavesoft.pawnuser.base.BaseApplication;


/**
* 文 件 名 : CustomToast
* 创 建 人： gloria
* 日     期：2015-01-20
* 修 改 人：gloria
* 日    期：2015-01-20
* 描    述：Toast显示
*/
public class CustomToast {

	public static void show(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void show(String text){
		Toast toast = Toast.makeText(BaseApplication.getInstance().getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}

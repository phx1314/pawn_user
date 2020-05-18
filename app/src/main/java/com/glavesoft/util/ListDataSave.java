package com.glavesoft.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.glavesoft.pawnuser.constant.BaseConstant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */
public class ListDataSave {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public ListDataSave(Context mContext) {
        preferences = mContext.getSharedPreferences(BaseConstant.AccountManager_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag,Class<T[]> clazz) {
        List<T> datalist=new ArrayList<T>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        T[] arr = new Gson().fromJson(strJson, clazz);
        return Arrays.asList(arr);

    }

    
}
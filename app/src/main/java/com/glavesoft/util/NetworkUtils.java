package com.glavesoft.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.glavesoft.pawnuser.base.BaseApplication;


public class NetworkUtils {

    public static boolean ONLINE = false; //是否联网
    public static int NETWORKTYPE = 0; //联网类型

    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;

    //网络是否可用
    public static boolean isNetworkAvailable() {
        Context context= BaseApplication.getInstance().getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            ONLINE = false;
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == State.CONNECTED) {
                        ONLINE=true;         //是否联网
                        NETWORKTYPE= getNetworkState();   // 联网方式
                        return true;
                    }
                }
            }
        }
        ONLINE=false;
        return false;
    }

    //获取网络是否状态
    public static int getNetworkState(){
        Context context=BaseApplication.getInstance().getApplicationContext();
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(state == State.CONNECTED||state == State.CONNECTING){
            return NETWORK_WIFI;
        }

        //3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if(state == State.CONNECTED||state == State.CONNECTING){
            return NETWORK_MOBILE;
        }
        return NETWORK_NONE;
    }

  //获取网络是否状态
    public static String getNetworkStateByType(int type){
        String net_type = "";
        switch (type) {
        case NETWORK_MOBILE:
            net_type = "mobile";
            break;
        case NETWORK_NONE:
            net_type = "none";
            break;
        case NETWORK_WIFI:
            net_type = "wifi";
            break;
        default:
            break;
        }
        return net_type;
    }
}

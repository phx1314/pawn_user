package com.glavesoft.pawnuser.activity.video;

import android.content.Context;
import android.util.Log;

import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**s
 * 视频点击状态页
 * Created by Administrator on 2017/8/29.
 */

public
class MyUserActionStandard implements JCUserActionStandard {
    public Context _context;
    public  MyUserActionStandard(Context context){
        _context=context;
    }

    @Override
    public void onEvent(int type, String url, int screen, Object... objects) {
        switch (type) {
            case JCUserAction.ON_CLICK_START_ICON:
                Log.i("USER_EVENT", "ON_CLICK_START_ICON" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_CLICK_START_ERROR:
                Log.i("USER_EVENT", "ON_CLICK_START_ERROR" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_CLICK_START_AUTO_COMPLETE:
                Log.i("USER_EVENT", "ON_CLICK_START_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_CLICK_PAUSE:
                Log.i("USER_EVENT", "ON_CLICK_PAUSE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_CLICK_RESUME:
                Log.i("USER_EVENT", "ON_CLICK_RESUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_SEEK_POSITION:
                Log.i("USER_EVENT", "ON_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_AUTO_COMPLETE:
                Log.i("USER_EVENT", "ON_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_ENTER_FULLSCREEN:
                Log.i("USER_EVENT", "ON_ENTER_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_QUIT_FULLSCREEN:
                JCVideoPlayer.hideSupportActionBar(_context);
                Log.i("USER_EVENT", "ON_QUIT_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_ENTER_TINYSCREEN:
                Log.i("USER_EVENT", "ON_ENTER_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_QUIT_TINYSCREEN:
                Log.i("USER_EVENT", "ON_QUIT_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME:
                Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserAction.ON_TOUCH_SCREEN_SEEK_POSITION:
                Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;

            case JCUserActionStandard.ON_CLICK_START_THUMB:
                Log.i("USER_EVENT", "ON_CLICK_START_THUMB" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            case JCUserActionStandard.ON_CLICK_BLANK:
                Log.i("USER_EVENT", "ON_CLICK_BLANK" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                break;
            default:
                Log.i("USER_EVENT", "unknow");
                break;
        }
    }
}
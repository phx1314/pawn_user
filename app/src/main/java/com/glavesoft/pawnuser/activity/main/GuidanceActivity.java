package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.AppManager;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * @author 严光
 * @date: 2017/9/5
 * @company:常州宝丰
 */
public class GuidanceActivity extends BaseActivity {
    private VideoView videoView;
    private TextView item_img_tg;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        //setLocation();
        videoView = (VideoView) this.findViewById(R.id.id_video);
        item_img_tg = (TextView) this.findViewById(R.id.item_img_tg);
        item_img_tg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.putBoolean(GuidanceActivity.this, "isfirstopensoft", false);
                Intent intent = new Intent(GuidanceActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //设置播放加载路径
        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.welcome_media));
        //播放
        videoView.start();
        //循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null){
            //释放掉占用的内存
            videoView.suspend();
        }
//        mLocationClient.stopLocation();
//        mLocationClient.onDestroy();
    }

    private long secondTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if ((System.currentTimeMillis() - secondTime) > 2000)
            {
                if (JCVideoPlayer.backPress()) {
                }else{
                    secondTime = System.currentTimeMillis();
                    CustomToast.show("再按一次退出程序");
                }
            } else
            {
                AppManager.getAppManager().exitApp();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

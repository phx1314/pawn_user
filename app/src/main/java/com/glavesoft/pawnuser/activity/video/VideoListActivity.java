package com.glavesoft.pawnuser.activity.video;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.VideoInfo;
import com.glavesoft.pullrefresh.PullToRefreshListView;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * @author 严光
 * @date: 2017/11/7
 * @company:常州宝丰
 */
public class VideoListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private int page=1;
    private final int limit=10;
    private int totalVal;
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    VideoListAdapter adapterVideoList;

    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;

    private ArrayList<VideoInfo> videoInfos=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_goods);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("在线视频");
        setTitleNameEn(R.mipmap.online_video);

        mRefreshLayout=(BGARefreshLayout)findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        adapterVideoList = new VideoListAdapter(this,videoInfos);
        lv_listview.setAdapter(adapterVideoList);
        videoList();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page=1;
        videoList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(totalVal>videoInfos.size()){
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }else{
            page=page+1;
            videoList();
        }
        return true;
    }

    // 获取视频列表
    private void videoList(){
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("watchVideo/videoList");
        OkGo.<DataResult<ArrayList<VideoInfo>>>post(url)
                .params("key", "")
                .params("page", page+"")
                .params("limit", limit+"")
                .execute(new JsonCallback<DataResult<ArrayList<VideoInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<VideoInfo>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){

                            if(page==1){
                                videoInfos.clear();
                            }
                            totalVal=limit*page;
                            if(response.body().getData()==null||response.body().getData().size()==0){
                                if(page==1){
                                    CustomToast.show("暂无视频");
                                }
                                return;
                            }
                            videoInfos.addAll(response.body().getData());
                            adapterVideoList.notifyDataSetChanged();

                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<VideoInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(sensorEventListener!=null){
            sensorManager.unregisterListener(sensorEventListener);
            JCVideoPlayer.releaseAllVideos();
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

}

package com.glavesoft.pawnuser.activity.main;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.video.VideoListAdapter;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.VideoInfo;
import com.glavesoft.pullrefresh.PullToRefreshBase;
import com.glavesoft.pullrefresh.PullToRefreshListView;
import com.glavesoft.util.ListDataSave;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Administrator on 2017/8/29.
 */

public class SeachVideoActivity extends BaseActivity {
    //以下搜索基础
    private GridView search_recommend;
    private CommonAdapter grid_commAdapter;
    List<String> grid=new ArrayList<String>();
    private LinearLayout item_search_history;
    ListDataSave listDataSave;
    //以下视频相关
    private int pages=1;
    private final int limit=10;
    private int totalVal;
    private ListView search_listview;
    private PullToRefreshListView pullToRefreshListView;
    VideoListAdapter adapterVideoList;
    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private ArrayList<VideoInfo> videoInfos=new ArrayList<>();
    private String keyword="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init() {
        settitle_Searchcancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        item_search_history.setVisibility(View.VISIBLE);
        listDataSave=new ListDataSave(SeachVideoActivity.this);
        showgrid(listDataSave.getDataList("seachvideo",String[].class));
        gettitle_Searchet().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //保存历史记录
                if (actionId == EditorInfo.IME_ACTION_SEARCH&&gettitle_Searchet().length()>0){
                    ArrayList<String> result=new ArrayList<String>();
                    List<String> stringArrayList=listDataSave.getDataList("seachvideo",String[].class);
                    for(int i=((stringArrayList.size()-5)>=0?(stringArrayList.size()-5):0);i<stringArrayList.size();i++){
                            result.add(stringArrayList.get(i));
                        }
                    int had=0;
                    for(int i=0;i<result.size();i++){
                        if(result.get(i).equals(gettitle_Searchet().getText().toString())){
                            had=had+1;
                        }
                    }
                    if(had==0){
                        result.add(gettitle_Searchet().getText().toString());
                    }
                    listDataSave.setDataList("seachvideo",result);
                    hideSoftInput();
                    videoList(keyword);
                    return true;
                }
                return false;
            }
        });

        gettitle_Searchet().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()>0){
                    item_search_history.setVisibility(View.GONE);
                    pullToRefreshListView.setVisibility(View.VISIBLE);
                    keyword=s.toString();
                }else{
                    keyword="";
                    videoInfos.clear();
                    adapterVideoList.notifyDataSetChanged();
                    item_search_history.setVisibility(View.VISIBLE);
                    pullToRefreshListView.setVisibility(View.GONE);
                   showgrid(listDataSave.getDataList("seachvideo",String[].class));
                }
            }
        });

        //pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.search_listview);
        pullToRefreshListView.setVisibility(View.VISIBLE);
        pullToRefreshListView.setPullLoadEnabled(true);
        pullToRefreshListView.setPullRefreshEnabled(true);
        pullToRefreshListView.setScrollLoadEnabled(false);
        search_listview = pullToRefreshListView.getRefreshableView();
        search_listview.setVerticalScrollBarEnabled(false);
        search_listview.setDivider(null);
        search_listview.setDividerHeight(ScreenUtils.dp2px(this, 1));
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
        {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                pages=1;
                videoList(keyword);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                if(totalVal>videoInfos.size()){
                    CustomToast.show("已经到底咯");
                    pullToRefreshListView.onPullUpRefreshComplete();
                }else{
                    pages=pages+1;
                    videoList(keyword);
                }
            }
        });
        adapterVideoList = new VideoListAdapter(this,videoInfos);
        search_listview.setAdapter(adapterVideoList);
    }

    //读取历史记录
    private void showgrid(List<String> result) {
        grid.clear();
        for(int i=result.size()-1;i>=0;i--){
            grid.add(result.get(i));
        }
        if (grid_commAdapter == null) {
            grid_commAdapter = new CommonAdapter<String>(SeachVideoActivity.this, grid,
                    R.layout.item_search_recommend) {
                @Override
                public void convert(final ViewHolder helper, final String item) {
                    TextView item_search_text=helper.getView(R.id.item_search_text);
                    item_search_text.setText(item);
                    item_search_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            keyword=item;
                            gettitle_Searchet().setText(keyword);
                            gettitle_Searchet().requestFocus();
                            videoList(item);
                            item_search_history.setVisibility(View.GONE);
                            pullToRefreshListView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            };
            search_recommend.setAdapter(grid_commAdapter);
        }else {
            grid_commAdapter.onDateChange(grid);
        }
    }

    // 获取视频列表
    private void videoList(String keywords){
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("/api/news/newsVideoList");
        HttpParams param=new HttpParams();
        param.put("page",pages+"");
        param.put("limit",limit+"");
        param.put("key",keywords);
        OkGo.<DataResult<ArrayList<VideoInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<VideoInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<VideoInfo>>> response) {
                        getlDialog().dismiss();
                        pullToRefreshListView.setLastUpdatedLabel(setLastUpdateTime());
                        pullToRefreshListView.onPullUpRefreshComplete();
                        pullToRefreshListView.onPullDownRefreshComplete();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            if(pages==1){
                                keyword="";
                                videoInfos.clear();
                                adapterVideoList.notifyDataSetChanged();
                                item_search_history.setVisibility(View.VISIBLE);
                                pullToRefreshListView.setVisibility(View.GONE);
                            }
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(pages==1){
                                videoInfos.clear();
                            }
                            totalVal=limit*pages;
                            if(response.body().getData()==null||response.body().getData().size()==0){
                                if(pages==1){
                                    CustomToast.show("暂无与“"+keyword+"”相关的视频");
                                    keyword="";
                                    videoInfos.clear();
                                    adapterVideoList.notifyDataSetChanged();
                                    item_search_history.setVisibility(View.VISIBLE);
                                    pullToRefreshListView.setVisibility(View.GONE);
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
                        pullToRefreshListView.onPullUpRefreshComplete();
                        pullToRefreshListView.onPullDownRefreshComplete();
                        if(pages==1){
                            keyword="";
                            videoInfos.clear();
                            adapterVideoList.notifyDataSetChanged();
                            item_search_history.setVisibility(View.VISIBLE);
                            pullToRefreshListView.setVisibility(View.GONE);
                        }
                    }
                });
    }
    @Override
    protected void onPause() {
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
    @Override
    public void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}

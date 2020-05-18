package com.glavesoft.pawnuser.fragment;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.SeachVideoActivity;
import com.glavesoft.pawnuser.activity.video.VideoListAdapter;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.VideoInfo;
import com.glavesoft.pullrefresh.PullToRefreshBase;
import com.glavesoft.pullrefresh.PullToRefreshListView;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static android.content.Context.SENSOR_SERVICE;

/**
 * @author 严光
 * @date: 2017/8/18
 * @company:常州宝丰
 */
public class VideoFragment extends BaseFragment {
    private static final String ARG_INDEX = "index";
    private int index;
    private int pages=1;
    private final int limit=10;
    private int totalVal;
    private ListView listView;
    private PullToRefreshListView pullToRefreshListView;
    VideoListAdapter adapterVideoList;

    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;

    private ArrayList<VideoInfo> videoInfos=new ArrayList<>();
    public VideoFragment() {
    }

    public static VideoFragment newInstance(int index)
    {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_listview_content, container, false);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listview);
        pullToRefreshListView.setPullLoadEnabled(true);
        pullToRefreshListView.setPullRefreshEnabled(true);
        pullToRefreshListView.setScrollLoadEnabled(false);
        listView = pullToRefreshListView.getRefreshableView();
        listView.setVerticalScrollBarEnabled(false);
        listView.setDivider(null);
        listView.setDividerHeight(ScreenUtils.dp2px(getActivity(), 1));

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        titlebar_name = (TextView) view.findViewById(R.id.titlebar_name);
        titlebar_name.setVisibility(View.VISIBLE);
        titlebar_name.setText("视频");
        titlebar_search= (ImageView) view.findViewById(R.id.titlebar_search);
        titlebar_search.setVisibility(View.VISIBLE);
        titlebar_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SeachVideoActivity.class);
                startActivity(intent);
            }
        });
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
        {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                pages=1;
                videoList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                if(totalVal>videoInfos.size()){
                    CustomToast.show("已经到底咯");
                    pullToRefreshListView.onPullUpRefreshComplete();
                }else{
                    pages=pages+1;
                    videoList();
                }
            }
        });
        adapterVideoList = new VideoListAdapter(getActivity(),videoInfos);
        listView.setAdapter(adapterVideoList);
       videoList();

    }
    // 获取视频列表
    private void videoList(){
        getlDialog().show();
        Map<String, String> param = VolleyUtil.getRequestMap(getActivity());
        param.put("page",pages+"");
        param.put("limit",limit+"");
        param.put("key","");
        java.lang.reflect.Type classtype = new TypeToken<DataResult<ArrayList<VideoInfo>>>() {}.getType();
        String url = BaseConstant.getApiPostUrl("/api/news/newsVideoList");
        VolleyUtil.postObjectApi(url, param, classtype, new ResponseListener<DataResult<ArrayList<VideoInfo>>>()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                pullToRefreshListView.onPullUpRefreshComplete();
                pullToRefreshListView.onPullDownRefreshComplete();
                getlDialog().dismiss();
                showVolleyError(error);
            }

            @Override
            public void onResponse(DataResult<ArrayList<VideoInfo>> response)
            {
                pullToRefreshListView.setLastUpdatedLabel(setLastUpdateTime());
                pullToRefreshListView.onPullUpRefreshComplete();
                pullToRefreshListView.onPullDownRefreshComplete();
                getlDialog().dismiss();
                if (response == null)
                {
                    CustomToast.show(getString(R.string.http_request_fail));
                    return;
                }
                if (DataResult.RESULT_OK_ZERO == response.getErrorCode())
                {
                    if(pages==1){
                        videoInfos.clear();
                    }
                    totalVal=limit*pages;
                    if(response.getData()==null||response.getData().size()==0){
                        if(pages==1){
                            CustomToast.show("暂无视频");
                        }
                        return;
                    }
                    videoInfos.addAll(response.getData());
                    adapterVideoList.notifyDataSetChanged();
                }else
                {
                    CustomToast.show(response.getErrorMsg());
                }
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
}

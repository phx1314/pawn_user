package com.glavesoft.pawnuser.activity.main;

import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.AppManager;

import java.util.ArrayList;
import java.util.List;


public class GPSNaviActivity extends BaseNaviActivity {

	 protected NaviLatLng mEndLatlng;
	 protected NaviLatLng mStartLatlng;
	 protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
	 protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
	 Double startlng,startlat;
	 Double endtlng,endlat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_navi);
        AppManager.getAppManager().addActivity(this);
        startlng=getIntent().getDoubleExtra("startlng", 0.0);
	 	startlat=getIntent().getDoubleExtra("startlat", 0.0);
	 	endtlng=getIntent().getDoubleExtra("endlng", 0.0);
		endlat=getIntent().getDoubleExtra("endlat", 0.0);
		mStartLatlng=new NaviLatLng(startlat, startlng);
		mEndLatlng=new NaviLatLng(endlat, endtlng);
        sList.add(mStartLatlng);
        eList.add(mEndLatlng);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);        
        mAMapNaviView.setAMapNaviViewListener(this);

        AMapNaviViewOptions options = new AMapNaviViewOptions();
        options.setScreenAlwaysBright(false);
        mAMapNaviView.setViewOptions(options);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);

    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }

}

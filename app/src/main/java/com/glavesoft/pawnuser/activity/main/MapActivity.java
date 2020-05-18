package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.view.CustomToast;

import java.util.List;

public class MapActivity extends BaseActivity implements AMapLocationListener,LocationSource,
        AMap.OnMarkerClickListener,AMap.InfoWindowAdapter,PoiSearch.OnPoiSearchListener {

    private AMap aMap;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;// 是否首次定位
    private Marker location_marker;
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private PoiResult poiResult; // poi返回的结果
    private List<PoiItem> poiItems;
    private double lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView)findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("典当伙伴");
        setTitleNameEn(R.mipmap.pawn_partner);

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setCompassEnabled(false);
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式

        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
        deactivate();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                //mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                lat=amapLocation.getLatitude();
                lng=amapLocation.getLongitude();
                LatLng latlng =new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                if (location_marker!=null){
                    location_marker.remove();
                }
                location_marker=aMap.addMarker(new MarkerOptions().title("")
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.location_marker)))
                        .zIndex(-1).position(latlng).draggable(true));
                if (isFirstLoc) {
                    isFirstLoc = false;
                    //Log.e("amap", "定位成功， lat: " + amapLocation.getLatitude() + " lon: " + amapLocation.getLongitude());
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                    LatLonPoint llp = new LatLonPoint(latlng.latitude, latlng.longitude);
                    doSearchQuery(llp);
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
            }

        }
    }

    /**
     * 激活定位
     */
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(location_marker)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.item_infowindow, null);

        TextView tv_title_infowin= ((TextView) infoWindow.findViewById(R.id.tv_title_infowin));
        TextView tv_address_infowin= ((TextView) infoWindow.findViewById(R.id.tv_address_infowin));
        TextView tv_go_infowin= ((TextView) infoWindow.findViewById(R.id.tv_go_infowin));

        tv_title_infowin.setText(poiItems.get((int)marker.getZIndex()).getTitle());
        tv_address_infowin.setText(poiItems.get((int)marker.getZIndex()).getSnippet());
        tv_go_infowin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MapActivity.this, GPSNaviActivity.class);
                intent.putExtra("startlng", lng);
                intent.putExtra("startlat",  lat);
                intent.putExtra("endlng", poiItems.get((int)marker.getZIndex()).getLatLonPoint().getLongitude());
                intent.putExtra("endlat",  poiItems.get((int)marker.getZIndex()).getLatLonPoint().getLatitude());
                startActivity(intent);
            }
        });

        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        setList();
                    } else {
                        CustomToast.show(getResources().getString(R.string.no_result));
                    }
                }
            } else {
                CustomToast.show(getResources().getString(R.string.no_result));
            }
        } else  {
            CustomToast.show(getResources().getString(R.string.no_result));
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(LatLonPoint llp) {
        query = new PoiSearch.Query("", "061302", "江苏省");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageNum(1);
        query.setPageSize(1000000000);
        if (llp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            //poiSearch.setBound(new PoiSearch.SearchBound(llp, 10000000));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    //加载地址点
    private void setList() {

        for (int i=0;i<poiItems.size();i++){
            final View markerview = LayoutInflater.from(this).inflate(
                    R.layout.view_poi, null);
            TextView tv_poi = (TextView) markerview.findViewById(R.id.tv_poi);
            tv_poi.setText(poiItems.get(i).getTitle());
            aMap.addMarker(new MarkerOptions().title("")
                    .icon(BitmapDescriptorFactory.fromView(markerview))
                    .zIndex(i).position(new LatLng(poiItems.get(i).getLatLonPoint().getLatitude(),
                            poiItems.get(i).getLatLonPoint().getLongitude())).draggable(true));
        }

    }
}

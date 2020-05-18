package com.glavesoft.pawnuser.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.VideoSeachInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Sinyu on 2018/8/4.
 */

public class VideoSeachFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<VideoSeachInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    public static String keyword="";
    private int page=1;
    private int listsize=0;
    private ArrayList<String> historcalList=new ArrayList<>();
    public static Fragment newInstance(String str) {
        VideoSeachFragment fragment = new VideoSeachFragment();
        keyword=str;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_seach, container, false);
        init(view);
        return view;
    }
    private void init(View view) {

        mRefreshLayout=(BGARefreshLayout) view.findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(VideoSeachFragment.this.getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)view.findViewById(R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setClass(VideoSeachFragment.this.getContext(), StoreActivity.class);
//                intent.putExtra("storeid", list.get(position).getId()+"");
//                startActivity(intent);
            }
        });

        if(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical, null)!=null){
            java.lang.reflect.Type classtype = new TypeToken<ArrayList<String>>() {}.getType();
            String jsonString=PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical,null);
            historcalList = CommonUtils.fromJson(jsonString, classtype, CommonUtils.DEFAULT_DATE_PATTERN);
        }

        resetPageData();
    }


    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownCounters= new SparseArray<>();
    private void showList(ArrayList<VideoSeachInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<VideoSeachInfo>(VideoSeachFragment.this.getActivity(), result,
                    R.layout.item_seach_video) {
                @Override
                public void convert(final ViewHolder helper, final VideoSeachInfo item) {
                    JCVideoPlayerStandard jcVideoPlayer= helper.getView(R.id.videoplayer);
                    jcVideoPlayer.setUp(BaseConstant.Video_URL+item.getVideo(), JCVideoPlayer.SCREEN_LAYOUT_LIST, item.getTitle());
                    if(item.getImg()!=null&&!item.getImg().equals("")){
                        ImageLoader.getInstance().displayImage(BaseConstant.Image_URL + item.getImg(), jcVideoPlayer.thumbImageView,getImageLoaderOptions());
                    }else{
                        ImageLoader.getInstance().displayImage("", jcVideoPlayer.thumbImageView,getImageLoaderOptions());
                    }
                    helper.setText(R.id.item_video_name,item.getTitle());
                }
            };

            lv_listview.setAdapter(commAdapter);
        } else {
            if (list == null || list.size() == 0) {
                list = result;
            } else {
                for (int i = 0; i < result.size(); i++) {
                    list.add(result.get(i));
                }
            }
            commAdapter.onDateChange(list);
        }
    }

    public void resetPageData()
    {
        page = 1;
        list.clear();
        commAdapter = null;
        lv_listview.setAdapter(null);
        searchIndexMenu();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if(keyword.toString().trim().length()>0&&!keyword.toString().equals("")){
            resetPageData();
        }else{
            keyword="";
            list.clear();
            commAdapter = null;
            lv_listview.setAdapter(null);
            mRefreshLayout.endRefreshing();
            mRefreshLayout.endLoadingMore();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            searchIndexMenu();
        }else{
            CustomToast.show("无更多数据");
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    private void searchIndexMenu()
    {
        String url=BaseConstant.getApiPostUrl("userGoods/searchIndexVideo");
        HttpParams param=new HttpParams();
        param.put("name",keyword);
        OkGo.<DataResult<ArrayList<VideoSeachInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<VideoSeachInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<VideoSeachInfo>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                listsize=response.body().getData().size();
                                showList(response.body().getData());
                            }else{

                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<VideoSeachInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
}

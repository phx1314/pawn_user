package com.glavesoft.pawnuser.activity.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.fragment.GoodsSeachFragment;
import com.glavesoft.pawnuser.fragment.StoreSeachFragment;
import com.glavesoft.pawnuser.fragment.VideoSeachFragment;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.ScrollingTabs;
import com.glavesoft.view.TriangleScrollingTabs;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SeachNewActivity extends BaseActivity {
    private String keyword="";
    private String[] mTitles = new String[] { "全部", "店铺", "视频" };
//    private SimpleViewPagerIndicator mIndicator;

    private TriangleScrollingTabs mScrollingTabs;
    private ViewPager mViewPager;
    private List<Fragment> fragment = new ArrayList<Fragment>();
    private ScrollingTabs.TabAdapter tabAdapter;

    private ArrayList<String> historcalList=new ArrayList<>();
    private GoodsSeachFragment goodsSeachFragment;
    private StoreSeachFragment storeSeachFragment;
    private VideoSeachFragment videoSeachFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_new);
        keyword=getIntent().getStringExtra("keyword");

        GoodsSeachFragment.keyword=keyword;
        StoreSeachFragment.keyword=keyword;
        VideoSeachFragment.keyword=keyword;
        goodsSeachFragment = new GoodsSeachFragment();
        storeSeachFragment = new StoreSeachFragment();

        videoSeachFragment = new VideoSeachFragment();

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("搜索结果");
//        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mScrollingTabs = (TriangleScrollingTabs)findViewById(R.id.stv_search);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
//        settitle_Searchcancel(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        gettitle_Searchet().setText(keyword);
//        gettitle_Searchet().setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH){
//                    if(!v.getText().toString().trim().equals("")){
//                        savedata(v.getText().toString().trim());
//                        keyword=v.getText().toString().trim();
//                        GoodsSeachFragment.keyword=keyword;
//                        StoreSeachFragment.keyword=keyword;
//
//                        VideoSeachFragment.keyword=keyword;
//                        goodsSeachFragment.resetPageData();
//                        storeSeachFragment.resetPageData();
//                        videoSeachFragment.resetPageData();
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });

        fragment.add(goodsSeachFragment);
        fragment.add(storeSeachFragment);
        fragment.add(videoSeachFragment);

        mViewPager.setAdapter( new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragment.get(arg0);
            }
        });
        mViewPager.setOffscreenPageLimit(3);//用来解决数据丢失的
        mViewPager.setCurrentItem(0);
        mScrollingTabs.setEqualWidth(true);
        mScrollingTabs.setViewPager(mViewPager);
        tabAdapter=new ScrollingTabs.TabAdapter() {

            @Override
            public View getView(int position) {
                LayoutInflater inflater =getLayoutInflater();
                final View tab = (View) inflater.inflate(R.layout.triangletab, null);

                TextView tv = (TextView) tab.findViewById(R.id.tv_tabs);
                if (position < mTitles.length)
                    tv.setText(mTitles[position]);
                return tab;
            }

            @Override
            public View getSeparator() {
//                View view = new ImageView(getActivity());
//                view.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
//                view.setBackgroundColor(Color.RED);
                return null;
            }

            @Override
            public void onTabSelected(int position, ViewGroup mContainer) {
                View tab = (View) mContainer.getChildAt(position);
                TextView tv = (TextView) tab.findViewById(R.id.tv_tabs);
                tv.setTextColor(getResources().getColor(R.color.white));
                View v_tabs_xian= (View) tab.findViewById(R.id.v_tabs_xian);
                v_tabs_xian.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnSelected(int position, ViewGroup mContainer) {
                View tab = (View) mContainer.getChildAt(position);
                TextView tv = (TextView) tab.findViewById(R.id.tv_tabs);
                tv.setTextColor(getResources().getColor(R.color.black));
                View v_tabs_xian= (View) tab.findViewById(R.id.v_tabs_xian);
                v_tabs_xian.setVisibility(View.GONE);
            }
        };
        mScrollingTabs.setTabAdapter(tabAdapter);

    }
    private void savedata(String keyword){
        if(historcalList.contains(keyword)){
            int index=0;
            for(int i=0;i<historcalList.size();i++){
                if(keyword.equals(historcalList.get(i))){
                    index=i;
                }
            }
            historcalList.remove(index);
            historcalList.add(keyword);
        }else{
            historcalList.add(keyword);
        }

        if(historcalList.size()>10){
            historcalList.remove(0);
        }

        PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical, new Gson().toJson(historcalList));
    }


}

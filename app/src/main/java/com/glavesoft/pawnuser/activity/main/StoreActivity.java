package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.pawn.GoodsCommentsActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.fragment.StoreGoodsFragment;
import com.glavesoft.pawnuser.fragment.StoreMainFragment;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.StoreDetailInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.ScrollingTabs;
import com.glavesoft.view.SimpleViewPagerIndicator;
import com.glavesoft.view.TriangleScrollingTabs;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreActivity extends BaseActivity {
    @BindView(R.id.iv_store_icon)
    ImageView ivStoreIcon;
    @BindView(R.id.tv_storename)
    TextView tvStorename;
    @BindView(R.id.tv_store_commentlevel)
    TextView tvStoreCommentlevel;
    @BindView(R.id.tv_store_follow)
    TextView tvStoreFollow;
    @BindView(R.id.iv_storedetail)
    ImageView ivStoredetail;
    @BindView(R.id.iv_storecomment)
    ImageView ivStorecomment;
    private List<String> mTitles = new ArrayList<>();
    private SimpleViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragmentArray = new ArrayList<>();
    private StoreDetailInfo storeDetailInfo;
    private StoreMainFragment storeMainFragment;
    private StoreGoodsFragment storeGoodsFragment;
    private StoreGoodsFragment storeNewsFragment;
    private StoreGoodsFragment forthFragment;
    private int mCurrentPos;
    private String orgId;
//    private TabLayout mViewpagerTab;
    private TriangleScrollingTabs mScrollingTabs;
    private ScrollingTabs.TabAdapter tabAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ButterKnife.bind(this);
        orgId = getIntent().getStringExtra("storeid");
        StoreMainFragment.orgId = orgId;
        StoreGoodsFragment.orgId = orgId;
        storeMainFragment = new StoreMainFragment();
        storeGoodsFragment = new StoreGoodsFragment();
        storeGoodsFragment.FragType = "0";
        storeNewsFragment = new StoreGoodsFragment();
        storeNewsFragment.FragType = "1";
        forthFragment = new StoreGoodsFragment();

        setTitleBack();
        setTitleName("店铺详情");
        setTitleNameEn(R.mipmap.store_detais);
        initViews();
        initDatas();
        initEvents();
    }

    private void initEvents() {
//        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//            }
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset,
//                                       int positionOffsetPixels) {
////                mIndicator.setTitleColr(mViewPager, position);
////                mIndicator.scroll(position, positionOffset);
//                mCurrentPos = position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
        ivStoredetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StoreActivity.this,StoreDetailActivity.class);
                intent.putExtra("info",storeDetailInfo);
                startActivity(intent);
            }
        });
        ivStorecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StoreActivity.this,GoodsCommentsActivity.class);
                intent.putExtra("storeid",orgId);
                intent.putExtra("type","store");
                startActivity(intent);
            }
        });

    }

    private void initDatas() {
        mTitles.add("首页");
        mTitles.add("全部");
        mTitles.add("新品");
        storeDetail();
        mFragmentArray.add(storeMainFragment);
        mFragmentArray.add(storeGoodsFragment);
        mFragmentArray.add(storeNewsFragment);
//        for (int i = 0; i < mTitles.size(); i++) {
//            mViewpagerTab.addTab(mViewpagerTab.newTab().setText(mTitles.get(i)));
//        }
//        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(
//                this.getSupportFragmentManager(), mFragmentArray, mTitles
//        );
//        mViewPager.setOffscreenPageLimit(3);
//        //viewpager 加载adapter
//        mViewPager.setAdapter(adapter);
//        //TableLayout加载viewpager
//        mViewpagerTab.setupWithViewPager(mViewPager);
//        mCurrentPos = 0;

        mViewPager.setAdapter( new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return  mTitles.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragmentArray.get(arg0);
            }
        });
        mViewPager.setOffscreenPageLimit(4);//用来解决数据丢失的
        mViewPager.setCurrentItem(0);
        mScrollingTabs.setEqualWidth(true);
        mScrollingTabs.setViewPager(mViewPager);
        tabAdapter=new ScrollingTabs.TabAdapter() {

            @Override
            public View getView(int position) {
                LayoutInflater inflater =getLayoutInflater();
                final View tab = (View) inflater.inflate(R.layout.triangletab, null);

                TextView tv = (TextView) tab.findViewById(R.id.tv_tabs);
                if (position < mTitles.size())
                    tv.setText(mTitles.get(position));
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

    private void storeDetail() {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("pawnOrg/get");
        HttpParams param=new HttpParams();
        param.put("orgId", orgId);
        OkGo.<DataResult<StoreDetailInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<StoreDetailInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<StoreDetailInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null) {
                                storeDetailInfo = response.body().getData();
                                tvStorename.setText(storeDetailInfo.getName());
                                if (storeDetailInfo.getType().equals("1")){
                                    ivStoreIcon.setImageResource(R.drawable.ddh_icon);
                                }else {
                                    ivStoreIcon.setImageResource(R.drawable.storeicon);
                                }
                                if (!storeDetailInfo.getOrgLogo().equals("")) {
                                    DisplayImageOptions imageOptions = DisplayImageOptions
                                            .createSimple();
                                    ImageLoader.getInstance().displayImage(BaseConstant.Image_URL
                                                    + storeDetailInfo.getOrgLogo(), ivStoreIcon,
                                            imageOptions);
                                }
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<StoreDetailInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void initViews() {
//        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
//        mViewpagerTab = (TabLayout) findViewById(R.id.home_viewpager_tab);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mScrollingTabs = (TriangleScrollingTabs)findViewById(R.id.store_goods);
    }
}
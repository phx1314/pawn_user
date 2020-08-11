package com.glavesoft.pawnuser.activity.appraisal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.fragment.AppraisalFragment;
import com.glavesoft.pawnuser.fragment.MyAppraisalFragment;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.view.ScrollingTabs;
import com.glavesoft.view.TriangleScrollingTabs;
import com.sobot.chat.SobotApi;
import com.sobot.chat.utils.ZhiChiConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 严光
 * @date: 2017/10/24
 * @company:常州宝丰
 */
public class EvaluationActivity extends BaseActivity{
    private final String[] titles = { "在线鉴定", "我的宝贝"};
    private int pos=1;
    private ViewPager mViewPager;
    private TriangleScrollingTabs mScrollingTabs;
    private List<Fragment> fragment = new ArrayList<Fragment>();
    private ScrollingTabs.TabAdapter tabAdapter;
    private FrameLayout fl_evaluation_kf;
    private ImageView iv_evaluation_kf;
    private TextView tv_evaluation_kf;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        setBoardCast();
        pos=getIntent().getIntExtra("pos",0);
        initView();
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ZhiChiConstant.sobot_unreadCountBrocast);
        registerReceiver(mListenerID, filter);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            int noReadNum = intent.getIntExtra("noReadCount", 0);//未读消息数
            String content = intent.getStringExtra("content"); //新消息内容

            if(SobotApi.getUnreadMsg(EvaluationActivity.this, LocalData.getInstance().getUserInfo().getId())==0){
                tv_evaluation_kf.setVisibility(View.GONE);
            }else{
                tv_evaluation_kf.setVisibility(View.VISIBLE);
                tv_evaluation_kf.setText(noReadNum+"");
            }
        }
    };

    public  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mListenerID);
    }

    public void onResume(){
        super.onResume();

        if(SobotApi.getUnreadMsg(EvaluationActivity.this,LocalData.getInstance().getUserInfo().getId())==0){
            tv_evaluation_kf.setVisibility(View.GONE);
        }else{
            tv_evaluation_kf.setVisibility(View.VISIBLE);
            tv_evaluation_kf.setText(SobotApi.getUnreadMsg(EvaluationActivity.this,LocalData.getInstance().getUserInfo().getId())+"");
        }
    }

    private void initView() {
        setTitleBack();
        setTitleName("估价");
        setTitleNameEn(R.mipmap.evaluation);

        setTitle_titlebar_kf( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotokf_Z(EvaluationActivity.this);
            }
        });
//        titlebar_kf = (ImageView) findViewById(R.id.titlebar_kf);
//        titlebar_kf.setVisibility(View.VISIBLE);
//        titlebar_kf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gotokf(EvaluationActivity.this);
//            }
//        });

        fl_evaluation_kf = (FrameLayout) findViewById(R.id.fl_evaluation_kf);
        //fl_evaluation_kf.setVisibility(View.VISIBLE);
        tv_evaluation_kf= (TextView) findViewById(R.id.tv_evaluation_kf);
        iv_evaluation_kf= (ImageView) findViewById(R.id.iv_evaluation_kf);
//        Glide.with(this).load(R.drawable.kf1).into(iv_evaluation_kf);
//        iv_evaluation_kf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gotokf(EvaluationActivity.this);
//            }
//        });

        mViewPager = (ViewPager) findViewById(R.id.vp_goods);
        mScrollingTabs = (TriangleScrollingTabs)findViewById(R.id.stv_goods);

        fragment.add(AppraisalFragment.newInstance(0));
        fragment.add(MyAppraisalFragment.newInstance(1));

        mViewPager.setAdapter( new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragment.get(arg0);
            }
        });
        mViewPager.setOffscreenPageLimit(2);//用来解决数据丢失的
        mViewPager.setCurrentItem(pos);
        mScrollingTabs.setEqualWidth(true);
        mScrollingTabs.setViewPager(mViewPager);
        tabAdapter=new ScrollingTabs.TabAdapter() {

            @Override
            public View getView(int position) {
                LayoutInflater inflater =getLayoutInflater();
                final View tab = (View) inflater.inflate(R.layout.triangletab, null);

                TextView tv = (TextView) tab.findViewById(R.id.tv_tabs);
                if (position < titles.length)
                    tv.setText(titles[position]);
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
}

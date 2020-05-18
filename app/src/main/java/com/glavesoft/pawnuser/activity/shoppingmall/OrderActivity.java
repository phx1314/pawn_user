package com.glavesoft.pawnuser.activity.shoppingmall;

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
import com.glavesoft.pawnuser.fragment.OrderFragment;
import com.glavesoft.view.ScrollingTabs;
import com.glavesoft.view.TriangleScrollingTabs;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 严光
 * @date: 2017/11/15
 * @company:常州宝丰
 */
public class OrderActivity extends BaseActivity {
    private final String[] titles = { "全部", "待付款","待发货","待收货","待评价", "已完成","退款"};
    private int pos=1;
    private ViewPager mViewPager;
    private TriangleScrollingTabs mScrollingTabs;
    private List<Fragment> fragment = new ArrayList<Fragment>();
    private ScrollingTabs.TabAdapter tabAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        pos=getIntent().getIntExtra("pos",0);
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("商城订单");
        setTitleNameEn(R.mipmap.mallorder);
        mViewPager = (ViewPager) findViewById(R.id.vp_goods);
        mScrollingTabs = (TriangleScrollingTabs)findViewById(R.id.stv_goods);

        fragment.add(OrderFragment.newInstance(0,0));
        fragment.add(OrderFragment.newInstance(1,0));
        fragment.add(OrderFragment.newInstance(2,0));
        fragment.add(OrderFragment.newInstance(3,0));
        fragment.add(OrderFragment.newInstance(4,0));//待评价
        fragment.add(OrderFragment.newInstance(4,1));//已完成
        fragment.add(OrderFragment.newInstance(5,0));
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
        mViewPager.setOffscreenPageLimit(6);//用来解决数据丢失的
        mViewPager.setCurrentItem(pos);
        mScrollingTabs.setEqualWidth(false);
        mScrollingTabs.setViewPager(mViewPager);
        tabAdapter=new ScrollingTabs.TabAdapter() {

            @Override
            public View getView(int position) {
                LayoutInflater inflater =getLayoutInflater();
                final View tab = (View) inflater.inflate(R.layout.triangletab1, null);

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

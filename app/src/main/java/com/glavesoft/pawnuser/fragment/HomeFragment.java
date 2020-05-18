package com.glavesoft.pawnuser.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.R;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.view.CustomViewPager;
import com.sobot.chat.api.model.Information;
import com.sobot.chat.conversation.SobotChatFragment;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment
{
    private int index;
    private static final String ARG_INDEX = "index";
    private final String[] titles = { "首页", "客服"};
    private List<Fragment> fragment = new ArrayList<Fragment>();
    private CustomViewPager mViewPager;
    public HomeFragment() {
    }

    public static HomeFragment newInstance(int index)
    {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    private void initView(View view)
    {
        Information info = new Information();
        info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
        //用户资料
        if(BaseConstant.isLogin()){
            info.setUid(LocalData.getInstance().getUserInfo().getId());
            info.setUname(LocalData.getInstance().getUserInfo().getId());
            info.setTel(LocalData.getInstance().getUserInfo().getAccount());
            info.setFace(BaseConstant.Image_URL+LocalData.getInstance().getUserInfo().getHeadImg());
        }else{
            info.setUid(CommonUtils.getDeviceId(getActivity()));
            info.setUname("游客");
        }

        //客服模式控制 -1不控制 按照服务器后台设置的模式运行
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(4);


        mViewPager = (CustomViewPager)view.findViewById(R.id.ViewPager);
        mViewPager.setNoScroll(true);//禁止滑动
        fragment.add(MainFragment.newInstance(0));

        Bundle bundle = new Bundle();
        bundle.putSerializable("sobot_bundle_info", info);
        fragment.add(SobotChatFragment.newInstance(bundle));

        mViewPager.setAdapter( new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
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
        mViewPager.setCurrentItem(0);
    }

}

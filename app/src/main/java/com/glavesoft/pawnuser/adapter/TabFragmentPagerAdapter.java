package com.glavesoft.pawnuser.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sinyu on 2018/8/5.
 */


public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    //fragment列表
    private List<Fragment> list_fragment;
    //tab名的列表
    private List<String> list_Title;

    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title ) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }


    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }
    //显示tab上的字
    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position);
    }
}
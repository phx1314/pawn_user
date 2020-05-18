package com.glavesoft.pawnuser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseFragment;

/**
 * @author 严光
 * @date: 2018/5/8
 * @company:常州宝丰
 */
public class SobotFragment extends BaseFragment {

    private int index;
    public static SobotFragment newInstance(int index)
    {
        SobotFragment fragment = new SobotFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            index = getArguments().getInt("index");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_listview_goods, container, false);
        //initView(view);
        return view;
    }
}

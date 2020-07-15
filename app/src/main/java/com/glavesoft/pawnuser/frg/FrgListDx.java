//
//  FrgCxFaxian
//
//  Created by Administrator on 2015-04-21 10:29:49
//  Copyright (c) Administrator All rights reserved.

/**
 *
 */

package com.glavesoft.pawnuser.frg;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.glavesoft.pawnuser.R;
import com.mdx.framework.Frame;
import com.mdx.framework.frg.BaseFrg;
import com.mdx.framework.frg.FrgList;
import com.mdx.framework.model.ModelDx;
import com.mdx.framework.view.Headlayout;

import java.util.ArrayList;
import java.util.List;

public class FrgListDx extends BaseFrg {

    public ListView mMPageListView;
    public String from;
    public String title;
    public int type;
    public List<ModelDx> data = new ArrayList();

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(com.framework.R.layout.frg_cx_sex_list);
        from = getActivity().getIntent().getStringExtra("from");
        title = getActivity().getIntent().getStringExtra("title");
        type = getActivity().getIntent().getIntExtra("type", 0);
        data = (List<ModelDx>) getActivity().getIntent().getSerializableExtra("data");
        initView();
        loaddata();
    }

    private void initView() {
        mMPageListView = (ListView) findViewById(com.framework.R.id.mMPageListView);

    }

    public void loaddata() {
        mMPageListView.setAdapter(new ArrayAdapter<ModelDx>(getActivity(),
                com.framework.R.layout.item_cx_text_ca, data));
        mMPageListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Frame.HANDLES.sentAll(from, type, data.get((int) arg3));
                FrgListDx.this.finish();
            }
        });
    }

    @Override
    public void onClick(View arg0) {
    }


    @Override
    public void setActionBar(LinearLayout actionBar) {
        Headlayout mHeadlayout = new Headlayout(getContext());
        mHeadlayout.setLeftBackground(R.drawable.top_fh);
        mHeadlayout.setBgColor(Color.parseColor("#12595B"));
        mHeadlayout.setGoBack();
        mHeadlayout.tv_title.setTextColor(Color.parseColor("#ffffff"));
        actionBar.addView(mHeadlayout);
        mHeadlayout.setTitle(title);
    }
}
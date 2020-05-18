package com.glavesoft.pawnuser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pullrefresh.PullToRefreshBase;
import com.glavesoft.pullrefresh.PullToRefreshListView;
import com.glavesoft.util.ScreenUtils;

import java.util.ArrayList;

/**
 * @author 严光
 * @date: 2017/8/24
 * @company:常州宝丰
 */
public class PictureFragment extends BaseFragment {
    private static final String ARG_INDEX = "index";
    private int index;
    private PullToRefreshListView pullListView;
    private ListView lv_news;
    private ArrayList<String> list=new ArrayList<>();
    CommonAdapter commAdapter;
    public PictureFragment() {
    }

    public static PictureFragment newInstance(int index)
    {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list, container, false);        initView(view);
        return view;
    }

    private void initView(View view)
    {
        pullListView = (PullToRefreshListView) view.findViewById(R.id.lv_list);
        pullListView.setPullLoadEnabled(true);
        pullListView.setPullRefreshEnabled(true);
        pullListView.setScrollLoadEnabled(false);
        lv_news = pullListView.getRefreshableView();
        lv_news.setVerticalScrollBarEnabled(false);
        lv_news.setDivider(null);
        lv_news.setDividerHeight(ScreenUtils.dp2px(getActivity(), 10));

        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
        {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
            {

            }
        });

        for(int i=0;i<5;i++){
            list.add(i+"");
        }
        showList(list);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showList(ArrayList<String> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<String>(getActivity(), result,
                    R.layout.item_pic) {
                @Override
                public void convert(final ViewHolder helper, final String item) {
                    //helper.setText(R.id.tv_time_order, item.getOrder_date());
                }
            };

            lv_news.setAdapter(commAdapter);
        } else {
            if (list == null || list.size() == 0) {
                list = result;
            } else {
                for (int i = 0; i < result.size(); i++) {
                    list.add(result.get(i));
                }
            }
            commAdapter.onDateChange(list);
            if(result.size()==1){
                lv_news.setSelection(list.size()-1);
            }
        }
    }
}

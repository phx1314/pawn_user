package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.MessageInfo;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Map;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2017/10/18
 * @company:常州宝丰
 */
public class MessageActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    public boolean isClick;
    int size;
    private BGARefreshLayout mRefreshLayout;
    ListView lv_listview;
    private ArrayList<MessageInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("消息");
        setTitleNameEn(R.mipmap.news);

        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

        userNotifyList();
    }

    private void showList(ArrayList<MessageInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<MessageInfo>(MessageActivity.this, result,
                    R.layout.item_mynews) {
                @Override
                public void convert(final ViewHolder helper, final MessageInfo item) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) helper.getView(R.id.item_body).getLayoutParams();
                    lp.width = ScreenUtils.getInstance().getWidth();  //把删除图标挤出屏幕
                    //0:鉴定为真品 1:典当成功通知  2续当到期通知  3赎当通知  4赎当邮寄通知  5邮寄通知  6到款通知 7鉴宝信息 8当品被竞拍通知  9绝当 10交易
                    if(item.getType().equals("1")){
                        helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.xi);
                    }else{
                        if(item.getRedirectType().equals("0")){
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.jian);
                        }else if(item.getRedirectType().equals("1")){
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.dian);
                        }else if(item.getRedirectType().equals("2")){//
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.xu);
                        }else if(item.getRedirectType().equals("3")){//
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.shu);
                        }else if(item.getRedirectType().equals("4")){//
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.shu);
                        }else if(item.getRedirectType().equals("5")){//
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.di);
                        }else if(item.getRedirectType().equals("6")){//
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.qian);
                        }else if(item.getRedirectType().equals("7")){//
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.jian);
                        }else if(item.getRedirectType().equals("8")){//
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.dian);
                        }else if(item.getRedirectType().equals("9")){//
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.jue);
                        }else if(item.getRedirectType().equals("10")){//
                            helper.getView(R.id.mynews_titleimg).setBackgroundResource(R.drawable.qian);
                        }
                    }

                    helper.setText(R.id.mynews_title,item.getTitle());
                    helper.setText(R.id.mynews_content,item.getContent());
                    helper.setText(R.id.mynews_time,item.getCreateTime());

                    helper.getView(R.id.ll_del_new).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            for(int i=0;i<list.size();i++){
                                if(item.getId().equals(list.get(i).getId())){
                                    index=i;
                                }
                            }

                            delUserNotifyList(item.getId());
                        }
                    });

                    helper.getView(R.id.item_body).setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                //不知被谁拦截了 ，up无效（也许是OnClickListener）,利用了activity的dispatchTouchEvent
                                case MotionEvent.ACTION_UP:
                                    break;
                                case MotionEvent.ACTION_DOWN:
                                    size = lv_listview.getLastVisiblePosition() - lv_listview.getFirstVisiblePosition() + 1;
                                    isClick = true;
                                    for (int i = lv_listview.getFirstVisiblePosition(); i < lv_listview.getLastVisiblePosition() + 1; i++) {
                                        int j = i - lv_listview.getFirstVisiblePosition();
                                        if (i != helper.getPosition() && lv_listview.getChildAt(j).getScrollX() > 0) {
                                            while (lv_listview.getChildAt(j).getScrollX() > 0) {
                                                lv_listview.getChildAt(j).scrollTo(0, 0);
                                                lv_listview.getChildAt(j).computeScroll();
                                            }
                                            return true;
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
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

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            userNotifyList();
        }else{
            if(list.size()>10){
                CustomToast.show("无更多数据");
            }
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    private void resetPageData()
    {
        page = 1;
        list.clear();
        commAdapter = null;
        lv_listview.setAdapter(null);
        userNotifyList();
    }

    private void userNotifyList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userNotify/userNotifyList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",page+"");
        param.put("limit","10");
        OkGo.<DataResult<ArrayList<MessageInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<MessageInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<MessageInfo>>> response) {
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
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<MessageInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

    private void delUserNotifyList(String id)
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userNotify/delUserNotifyList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        OkGo.<DataResult<DataInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<DataInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            list.remove(index);
                            commAdapter.onDateChange(list);
                            for (int i = 0; i < lv_listview.getChildCount(); i++) {
                                lv_listview.getChildAt(i).scrollTo(0, 0);
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}

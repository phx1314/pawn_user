package com.glavesoft.pawnuser.activity.pawn;

import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommentsAdapter;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.CommentsInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.GoodsCommentsInfo;
import com.glavesoft.view.CustomToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.List;


public class GoodsCommentsActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    //    SuperSwipeRefreshLayout superSwipeRefreshLayout;
    Handler handler = new Handler();
    List<CommentsInfo> data = new ArrayList<>();
    String headImg = "http://image18-c.poco" +
            ".cn/mypoco/myphoto/20170308/16/18505011120170308160548098_640.jpg";
    CommentsAdapter adapter;
    private String goodsId = "";
    private String type = "goods";
    private TextView comment_num,tv_title_comment;
    List<GoodsCommentsInfo> commentsInfos;
    private LinearLayout ll_nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_comments);
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("商品评价");
        setTitleNameEn(R.mipmap.store_evaluation);

        ll_nodata = (LinearLayout) findViewById(R.id.ll_nodata);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        commentsInfos = new ArrayList<>();
        comment_num = (TextView) findViewById(R.id.comment_num);
        tv_title_comment= (TextView) findViewById(R.id.tv_title_comment);

        adapter = new CommentsAdapter(commentsInfos, GoodsCommentsActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(GoodsCommentsActivity.this));
        mRecyclerView.setAdapter(adapter);
        type = this.getIntent().getStringExtra("type");
//        superSwipeRefreshLayout=(SuperSwipeRefreshLayout)findViewById(R.id.refresh);
        View view = LayoutInflater.from(this).inflate(R.layout.comments_loadview, null, false);
        if (type.equals("goods")) {
            goodsId = this.getIntent().getStringExtra("goodsid");
            getCommentsList();
        } else {
            setTitleName("店铺评价");
            tv_title_comment.setText("店铺评价");
            goodsId = this.getIntent().getStringExtra("storeid");
            getStoreCommentsList();
        }

    }

    private void getCommentsList() {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("userComment/goodsCommentList");
        HttpParams param=new HttpParams();
        param.put("goodsId", goodsId);
        OkGo.<DataResult<List<GoodsCommentsInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<List<GoodsCommentsInfo>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<List<GoodsCommentsInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null&&response.body().getData().size()>0) {
                                ll_nodata.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                commentsInfos = response.body().getData();
                                comment_num.setText("(" + commentsInfos.size() + ")");
                                adapter.addData(commentsInfos);//刷新
                                adapter.notifyDataSetChanged();
                            }else{
                                ll_nodata.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                                comment_num.setText("(0)");
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<List<GoodsCommentsInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void getStoreCommentsList() {
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("userComment/getGoodsComment");
        HttpParams param=new HttpParams();
        param.put("orgId", goodsId);
        OkGo.<DataResult<List<GoodsCommentsInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<List<GoodsCommentsInfo>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<List<GoodsCommentsInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null&&response.body().getData().size()>0) {
                                ll_nodata.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                commentsInfos = response.body().getData();
                                comment_num.setText("(" + commentsInfos.size() + ")");
                                adapter.addData(commentsInfos);//刷新
                                adapter.notifyDataSetChanged();
                            }else{
                                ll_nodata.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                                comment_num.setText("(0)");
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<List<GoodsCommentsInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}

package com.glavesoft.pawnuser.activity.appraisal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.blankj.utilcode.util.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.SendCallGoodInfo;
import com.glavesoft.pawnuser.mod.SendCallGoodsComment;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class SendCallGoodDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.titlebar_back)
    ImageView titlebarBack;
    @BindView(R.id.titlebar_left)
    TextView titlebarLeft;
    @BindView(R.id.titlebar_name)
    TextView titlebarName;
    @BindView(R.id.titlebar_search)
    ImageView titlebarSearch;
    @BindView(R.id.titlebar_kf)
    ImageView titlebarKf;
    @BindView(R.id.titlebar_publish)
    ImageView titlebarPublish;
    @BindView(R.id.titlebar_news)
    ImageView titlebarNews;
    @BindView(R.id.titlebar_share)
    ImageView titlebarShare;
    @BindView(R.id.titlebar_right)
    TextView titlebarRight;
    @BindView(R.id.titlebar_ll_main)
    RelativeLayout titlebarLlMain;
    @BindView(R.id.iv_store_icon)
    ImageView ivStoreIcon;
    @BindView(R.id.tv_storename)
    TextView tvStorename;
    @BindView(R.id.tv_store_time)
    TextView tvStoreTime;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_jd_price)
    TextView tvJdPrice;
    @BindView(R.id.tv_jd_content)
    TextView tvJdContent;
    @BindView(R.id.ll_store_goodsdetail)
    LinearLayout llStoreGoodsdetail;
    @BindView(R.id.tv_content_goodsdetail)
    TextView tvContentGoodsdetail;

    @BindView(R.id.ll_like)
    LinearLayout llLike;
    @BindView(R.id.ll_goods_comment)
    LinearLayout llGoodsComment;
    @BindView(R.id.ll_goods_fllow)
    LinearLayout llGoodsFllow;
    @BindView(R.id.ll_buy_goodsdetail)
    LinearLayout llBuyGoodsdetail;
    @BindView(R.id.sc_detailimg)
    LinearLayout scDetailimg;
    @BindView(R.id.tv_like)
    TextView tvLike;
//    @BindView(R.id.tv_comment_title)
//    TextView tvCommentTitle;
    @BindView(R.id.lv_comments)
    ListView lvComments;
    //    @BindView(R.id.ll_lv_comment)
//    LinearLayout llLvComment;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;


    @BindView(R.id.ll_lv_no_comment)
    LinearLayout llLvNoComment;
    @BindView(R.id.edit_comment)
    EditText editComment;
    @BindView(R.id.ll_edit_comment)
    LinearLayout llEditComment;
    @BindView(R.id.tv_reply)
    TextView tvReply;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.edit_comment1)
    EditText editComment1;
    @BindView(R.id.tv_reply1)
    TextView tvReply1;
    @BindView(R.id.tv_cancel1)
    TextView tvCancel1;
    @BindView(R.id.ll_edit_comment1)
    LinearLayout llEditComment1;
    @BindView(R.id.sl_detail)
    ScrollView slDetail;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.iv_video)
    ImageView ivVideo;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
    @BindView(R.id.iv_video1)
    ImageView ivVideo1;
    @BindView(R.id.fl_video1)
    FrameLayout flVideo1;
    @BindView(R.id.iv_video2)
    ImageView ivVideo2;
    @BindView(R.id.fl_video2)
    FrameLayout flVideo2;

    @BindView(R.id.tv_zan)
    TextView tvZan;
    @BindView(R.id.tv_sc)
    TextView tvSc;
    //    @BindView(R.id.btn_comment)
//    Button btnComment;
    private String goodsId;
    private SendCallGoodInfo sendCallGoodInfo;
    private List<SendCallGoodsComment> sendCallGoodsComments;
    CommonAdapter commAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_call_good_detail);
        ButterKnife.bind(this);
        goodsId = getIntent().getStringExtra("id");
        initView();
        getSellGoodsDetail();
        getSellGoodsCommentList();
    }

    private void initView() {
        setTitleBack();
        setTitleName("商品详情");
        setTitleNameEn(R.mipmap.commodity_details);

        tvReply.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        llComment.setOnClickListener(this);
        llGoodsComment.setOnClickListener(this);
        sendCallGoodsComments = new ArrayList<>();
        tvReply1.setOnClickListener(this);
        tvCancel1.setOnClickListener(this);
        llBuyGoodsdetail.setOnClickListener(this);
        llLike.setOnClickListener(this);
        llGoodsFllow.setOnClickListener(this);
//        btnComment1.setOnClickListener(this);
//        llGoodsComment1.setOnClickListener(this);
        slDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    private void getSellGoodsDetail() {
        String token = LocalData.getInstance().getUserInfo().getToken();
        getlDialog().show();
        Map<String, String> param = VolleyUtil.getRequestMap(this);
        param.put("token", token);
        param.put("id", String.valueOf(goodsId));
        Type classtype = new TypeToken<DataResult<SendCallGoodInfo>>() {
        }.getType();

        VolleyUtil.postObjectApi(BaseConstant.getApiPostUrl("userGoods/sellDetail"), param,
                classtype, new ResponseListener<DataResult<SendCallGoodInfo>>() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getlDialog().dismiss();

                        showVolleyError(error);
                    }

                    @Override
                    public void onResponse(DataResult<SendCallGoodInfo> response) {
                        getlDialog().dismiss();

                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }
                        if (DataResult.RESULT_OK_ZERO == response.getErrorCode()) {
                            if (response.getData() != null) {
                                sendCallGoodInfo = response.getData();
                                showInfo();
                            }

                        } else if (DataResult.RESULT_102 == response.getErrorCode()) {
                            toLogin();
                        } else {
                            CustomToast.show(response.getErrorMsg());
                        }
                    }
                });
    }

    private void getSellGoodsCommentList() {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/userGoodsCommentList");
        HttpParams param=new HttpParams();
        param.put("token", token);
        param.put("userGoodsId", String.valueOf(goodsId));
        OkGo.<DataResult<ArrayList<SendCallGoodsComment>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<SendCallGoodsComment>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<SendCallGoodsComment>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                sendCallGoodsComments = response.body().getData();
                                showCommentInfo();
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<SendCallGoodsComment>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    String commentId = "";//回复评论的id
    String replyId = "";//replyType为2时传值，回复评论id，回复别人的回复
    String replyType = "1";//1为回复评论，2为回复别人的回复

    private void showCommentInfo() {
//        tvCommentTitle.setText("留言·" + sendCallGoodsComments.size());
        lvComments.setVisibility(View.VISIBLE);
        llLvNoComment.setVisibility(View.GONE);
        if (commAdapter == null) {
            commAdapter = new CommonAdapter<SendCallGoodsComment>(this, sendCallGoodsComments,
                    R.layout.item_send_call_comment) {
                @Override
                public void convert(final ViewHolder helper, final SendCallGoodsComment item) {
                    helper.setText(R.id.tv_user_name, item.getNickName());
                    helper.setText(R.id.tv_content, item.getContent());
                    helper.setText(R.id.tv_time, item.getCreateTime());
                    helper.getView(R.id.tv_content).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (llEditComment1.getVisibility()==View.VISIBLE){
                                llEditComment1.setVisibility(View.GONE);
                            }
                            llEditComment.setVisibility(View.VISIBLE);
                            editComment.setHint("回复" + item.getNickName() + ":");
                            showSoftInputFromWindow(SendCallGoodDetailActivity.this, editComment);
                            commentId = item.getId() + "";
                            replyType = "1";
                        }
                    });
                    if (!item.getHeadImg().equals("")) {
                        getImageLoader().displayImage(BaseConstant.Image_URL + item.getHeadImg(),
                                (ImageView) helper.getView(R.id.iv_user_icon),
                                getImageLoaderOptions());
                    }
                    ListView listView = helper.getView(R.id.lv_comment_replay);
                    List<SendCallGoodsComment.CommentReplyExListBean> commentReplyExList = item
                            .getCommentReplyExList();

                    if (commentReplyExList.size() > 0) {
                        CommonAdapter commAdapter = new CommonAdapter<SendCallGoodsComment
                                .CommentReplyExListBean>(SendCallGoodDetailActivity.this,
                                commentReplyExList,
                                R.layout.item_send_call_comment) {
                            @Override
                            public void convert(final ViewHolder helper, final
                            SendCallGoodsComment.CommentReplyExListBean item1) {
                                helper.setText(R.id.tv_user_name, item1.getFromNickname());
                                helper.setText(R.id.tv_content, item1.getContent());
//                                helper.getView(R.id.tv_time).setVisibility(View.GONE);
                                helper.getView(R.id.tv_content).setOnClickListener(new View
                                        .OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (llEditComment1.getVisibility()==View.VISIBLE){
                                            llEditComment1.setVisibility(View.GONE);
                                        }
                                        llEditComment.setVisibility(View.VISIBLE);
//                                        editComment.setFocusable(true);
                                        editComment.setHint("回复" + item1.getFromNickname() + ":");
                                        showSoftInputFromWindow(SendCallGoodDetailActivity.this,
                                                editComment);
                                        replyId = item1.getId() + "";
                                        commentId = item.getId() + "";
                                        replyType = "2";
                                    }
                                });
                                helper.setText(R.id.tv_time, item1.getCreateTime());
                                if (!item1.getFromThumbImg().equals("")) {
                                    getImageLoader().displayImage(BaseConstant.Image_URL +
                                            item1.getFromThumbImg(), (ImageView) helper.getView(R
                                            .id.iv_user_icon), getImageLoaderOptions());
                                }
                            }
                        };
                        listView.setAdapter(commAdapter);
                    }else{
                        listView.setAdapter(null);
                    }

                }
            };

            lvComments.setAdapter(commAdapter);
        } else {
            commAdapter.onDateChange(sendCallGoodsComments);

        }
//        setListViewHeightBasedOnChildren(lvComments);
    }

    private void showInfo() {
        tvStorename.setText(sendCallGoodInfo.getNickName());
        tvStoreTime.setText(sendCallGoodInfo.getCreateTime());
        tvJdPrice.setText("￥" + sendCallGoodInfo.getAuthPrice());
        tvPrice.setText("￥" + sendCallGoodInfo.getSellPrice());
        tvJdContent.setText(sendCallGoodInfo.getAppraisalDsc());
        tvContentGoodsdetail.setText(sendCallGoodInfo.getSellInfo());
        if (!sendCallGoodInfo.getHeadImg().equals("")) {
            getImageLoader().displayImage(BaseConstant.Image_URL + sendCallGoodInfo.getHeadImg(),
                    ivStoreIcon, getImageLoaderOptions());
        }
        if (sendCallGoodInfo.getSellImgs() != null&&!sendCallGoodInfo.getSellImgs().equals(""))
            handlePicList(Arrays.asList(sendCallGoodInfo.getSellImgs().split(",")));

        if (sendCallGoodInfo.getSellVideo() != null&&!sendCallGoodInfo.getSellVideo().equals(""))
            handleVedioList(Arrays.asList(sendCallGoodInfo.getSellVideo().split(",")));


        tvLike.setText(sendCallGoodInfo.getPraiseCount() + "人点赞");
    }

    private void handlePicList(final List<String> paths) {
        LinearLayout rootview = new LinearLayout(this);
        rootview.setOrientation(LinearLayout.VERTICAL);
        View commentView;
        SimpleDraweeView sdv_pic;
        for (int i = 0, len = paths.size(); i < len; i++) {
            commentView = getLayoutInflater().inflate(R.layout.sellgooddetail_pic_item, null);
            sdv_pic = (SimpleDraweeView) commentView.findViewById(R.id.sdv_pic);
            getImageLoader().displayImage(BaseConstant.Image_URL + paths.get(i),
                    sdv_pic, getImageLoaderOptions());

            final int finalI = i;

            AutoUtils.auto(commentView);
            rootview.addView(commentView);
        }
        scDetailimg.removeAllViews();
        scDetailimg.addView(rootview);
    }

    private void handleVedioList(final List<String> paths) {
        for (int i=0;i<paths.size();i++){
            switch (i){
                case 0:
                    final int finalI = i;
                    flVideo.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                           final Bitmap bitmap= createVideoThumbnail
                                    (BaseConstant.Image_URL +paths.get(finalI), 100,
                                            100);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ivVideo.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }).start();
                    flVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            JCVideoPlayerStandard.startFullscreen(SendCallGoodDetailActivity.this,
                                    JCVideoPlayerStandard.class,
                                    BaseConstant.Image_URL +paths.get(finalI), "");
                        }
                    });
                    break;
                case 1:
                    final int finalI1 = i;
                    flVideo1.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Bitmap bitmap= createVideoThumbnail
                                    (BaseConstant.Image_URL +paths.get(finalI1), 100,
                                            100);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ivVideo1.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }).start();
                    flVideo1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            JCVideoPlayerStandard.startFullscreen(SendCallGoodDetailActivity.this,
                                    JCVideoPlayerStandard.class,
                                    BaseConstant.Image_URL +paths.get(finalI1), "");
                        }
                    });
                    break;
                case 2:
                    final int finalI2 = i;
                    flVideo2.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Bitmap bitmap= createVideoThumbnail
                                    (BaseConstant.Image_URL +paths.get(finalI2), 100,
                                            100);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ivVideo1.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }).start();
                    flVideo2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            JCVideoPlayerStandard.startFullscreen(SendCallGoodDetailActivity.this,
                                    JCVideoPlayerStandard.class,
                                    BaseConstant.Image_URL +paths.get(finalI2), "");
                        }
                    });
                    break;
            }

        }

    }

    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils
                    .OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (JCVideoPlayer.backPress()) {
                return false;
            }
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reply:
                //回复按钮
                if (StringUtils.isEmpty(editComment.getText().toString().trim())){
                    CustomToast.show("请输入回复内容");
                    return;
                }
                llEditComment.setVisibility(View.GONE);
                replyUserGoodsComment();
                editComment.setText("");
                break;
            case R.id.tv_cancel:
                llEditComment.setVisibility(View.GONE);
                editComment.setText("");
                break;
            case R.id.ll_goods_comment:
                if (llEditComment.getVisibility()==View.VISIBLE){
                    llEditComment.setVisibility(View.GONE);
                }
                llEditComment1.setVisibility(View.VISIBLE);
                editComment1.setHint("留言:");
                showSoftInputFromWindow(this, editComment1);

                break;
            case R.id.ll_comment:
                if (llEditComment.getVisibility()==View.VISIBLE){
                    llEditComment.setVisibility(View.GONE);
                }
                llEditComment1.setVisibility(View.VISIBLE);
                editComment1.setHint("留言:");
                showSoftInputFromWindow(this, editComment1);
                break;
            case R.id.tv_reply1:
                if (StringUtils.isEmpty(editComment1.getText().toString().trim())){
                    CustomToast.show("请输入留言内容");
                    return;
                }
                llEditComment1.setVisibility(View.GONE);
                addUserGoodsComment();
                editComment1.setText("");
                break;
            case R.id.tv_cancel1:
                llEditComment1.setVisibility(View.GONE);
                editComment1.setText("");
                break;
            case R.id.ll_buy_goodsdetail:
                if (BaseConstant.isLogin()) {
                    if (sendCallGoodInfo != null) {
                        if (sendCallGoodInfo.getUserId().equals(LocalData.getInstance().getUserInfo().getUserid())){
                            CustomToast.show("自己寄卖的商品不能购买");
                            return;
                        }
                        Intent intent = new Intent();
                        intent.setClass(SendCallGoodDetailActivity.this,
                                SubmitSendCallBuyActivity.class);
//                        intent.putExtra("type", "sendcall");
//                        intent.putExtra("state", type);
                        intent.putExtra("sendCallGoodInfo", sendCallGoodInfo);
                        startActivity(intent);
                    }
                } else {
                    startActivity(new Intent(SendCallGoodDetailActivity.this, LoginActivity.class));
                }
                break;
            case R.id.ll_like:
                collectPraiseUserGoods("0");
                ivLike.setImageResource(R.mipmap.zan_);
                tvZan.setText("已赞");
                break;
            case R.id.ll_goods_fllow:
                collectPraiseUserGoods("1");
                ivCollect.setImageResource(R.mipmap.ysc);
                tvSc.setText("已收藏");
                break;
        }
    }

    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
//        //显示软键盘
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams
//                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    private void replyUserGoodsComment() {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/replyUserGoodsComment");
        HttpParams param=new HttpParams();
        param.put("token", token);
        param.put("content", editComment.getText().toString());
        param.put("userGoodsId", sendCallGoodInfo.getId() + "");
//        param.put("headImg", "");
//        param.put("nickName", "");
        param.put("commentId", commentId);
        param.put("replyType", replyType);
        param.put("replyId", replyId);
        OkGo.<DataResult<Integer>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<Integer>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<Integer>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null && response.body().getData() == 1) {
                                CustomToast.show("回复成功");
                                //刷新
                                sendCallGoodsComments.clear();
                                commAdapter = null;
                                lvComments.setAdapter(null);
                                getSellGoodsCommentList();
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<Integer>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void addUserGoodsComment() {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/addUserGoodsComment");
        HttpParams param=new HttpParams();
        param.put("token", token);
        param.put("content", editComment1.getText().toString());
        param.put("userGoodsId", sendCallGoodInfo.getId() + "");
        OkGo.<DataResult<Integer>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<Integer>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<Integer>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null && response.body().getData() == 1) {
                                CustomToast.show("留言成功");
                                //刷新
                                sendCallGoodsComments.clear();
                                commAdapter = null;
                                lvComments.setAdapter(null);
                                getSellGoodsCommentList();
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<Integer>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void collectPraiseUserGoods(String type) {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/collectPraiseUserGoods");
        HttpParams param=new HttpParams();
        param.put("token", token);
        param.put("type", type);
        param.put("userGoodsId", sendCallGoodInfo.getId() + "");
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
                            CustomToast.show("成功");
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        try {
            // 获取ListView对应的Adapter
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }

            int totalHeight = 0;
            for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
                // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                // 计算子项View 的宽高
                listItem.measure(0, 0);
                // 统计所有子项的总高度
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()
                    - 1));
            // listView.getDividerHeight()获取子项间分隔符占用的高度
            // params.height最后得到整个ListView完整显示需要的高度
            listView.setLayoutParams(params);
        } catch (Exception e) {
//            Helper.saveFileLog("setListViewHeightBasedOnChildren___"+e.toString());
        }
    }
}

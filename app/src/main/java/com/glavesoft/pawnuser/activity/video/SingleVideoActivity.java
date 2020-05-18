package com.glavesoft.pawnuser.activity.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.CommentInfo;
import com.glavesoft.pawnuser.mod.CouponInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.MyNewsListInfo;
import com.glavesoft.pullrefresh.PullToRefreshBase;
import com.glavesoft.pullrefresh.PullToRefreshListView;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.util.ShareUtil;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * 单个视频播放页
 * Created by Administrator on 2017/8/25.
 */

public class SingleVideoActivity extends BaseActivity implements View.OnClickListener,BGARefreshLayout.BGARefreshLayoutDelegate {
    JCVideoPlayerStandard mJcVideoPlayerStandard;
    private String url="",name="",videoThumbs="";
    private int type;
    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private ImageView iv_share_video;
    private String isCollect="0";

    private BGARefreshLayout mRefreshLayout;
    //private PullToRefreshListView comment_refreshlistview;
    private PullToRefreshListView reply_refreshlistview;//评论用，回复用（下面同）
    private ListView lv_listview,reply_list;//评论，回复
    private CommonAdapter commonAdapter,reply_commonAdapter;//评论，回复
    private PopupWindow popupWindow,replypopupWindow;//回复评论pop，回复回复pop
    private int pages=1,reply_pages=1;//评论，回复
    private final int limit=10,reply_limit=10;//评论，回复
    private int totalVal,reply_totalVal;//评论，回复
    private String newsId="",commentReply_id="",reply_id="";//新闻，评论，回复
    ArrayList<CommentInfo> commentInfoslist=new ArrayList<>();//评论
    HashMap<Integer,Integer> commentIdpositon=new HashMap<>();//评论
    ArrayList<CommentInfo> reply_Infoslist=new ArrayList<>();//回复
    HashMap<Integer,Integer> reply_Idpositon=new HashMap<>();//回复
    private EditText et_comment_newsdetail,et_reply_newsdetail,et_pwreply_newsdetail;//评论，回复,回复的回复
    private ImageView iv_sendcomment_newsdetail,iv_sendreply_newsdetail,iv_pwreply_newsdetail;//评论，回复,回复的回复
    private ImageView iv_praise,reply_isPraise;//popupWindow中的两个点赞图，点赞评论

    private TextView tv_title_singlevideo,tv_number_singlevideo;

    private boolean showDanmaku;
    //private IDanmakuView mDanmakuView;
    private DanmakuContext mContext;
    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    CouponInfo couponInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlevideo);
        url=getIntent().getStringExtra("url");
        type=getIntent().getIntExtra("type",0);
        name=getIntent().getStringExtra("name");
        videoThumbs=getIntent().getStringExtra("videoThumbs");
        newsId=getIntent().getStringExtra("id");
        setBoardCast();
        init();
        if(BaseConstant.isLogin()){
            userGetCoupon();
        }
        videoCommentCnt();
    }
    private void init() {
        setTitleBack();
        setTitleName("视频详情");
        setTitleNameEn(R.mipmap.video_details);

        mJcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.single_video);
        mJcVideoPlayerStandard.setUp(url,type,"","play");
        //Picasso.with(this).load(videoThumbs).into(mJcVideoPlayerStandard.thumbImageView);
        getImageLoader().displayImage(videoThumbs,mJcVideoPlayerStandard.thumbImageView,getImageLoaderOptions());

        JCVideoPlayer.setJcUserAction(new MyUserActionStandard(this));
//        JCVideoPlayer.showSupportActionBar(this,1);//上锁，界面消除解锁
//        JCVideoPlayer.hideSupportActionBar(this);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        //mJcVideoPlayerStandard.mDanmakuView=(IDanmakuView) findViewById(R.id.danmaku_view);

        //setIDanmakuView();

        tv_title_singlevideo= (TextView) findViewById(R.id.tv_title_singlevideo);
        tv_number_singlevideo= (TextView) findViewById(R.id.tv_number_singlevideo);

        tv_title_singlevideo.setText(name);

        et_comment_newsdetail= (EditText) findViewById(R.id.et_comment_newsdetail);
        iv_sendcomment_newsdetail= (ImageView) findViewById(R.id.iv_sendcomment_newsdetail);
        iv_sendcomment_newsdetail.setOnClickListener(this);
        iv_share_video= (ImageView) findViewById(R.id.iv_share_video);
        iv_share_video.setOnClickListener(this);
        et_comment_newsdetail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND&&et_comment_newsdetail.getText().length()>0){
                    iv_sendcomment_newsdetail.setOnClickListener(null);
                    addcomment();
                    hideSoftInput();
                    return true;
                }
                return false;
            }
        });

        mRefreshLayout=(BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)findViewById(R.id.lv_listview);

        commentList();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        pages=1;
        commentList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(totalVal>commentInfoslist.size()){
            CustomToast.show("已经到底咯");
            mRefreshLayout.endLoadingMore();
            return false;
        }else{
            pages=pages+1;
            commentList();
        }
        return true;
    }

    private void setIDanmakuView(){
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
//                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(20);
        if (mJcVideoPlayerStandard.mDanmakuView != null) {
            mJcVideoPlayerStandard.mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mJcVideoPlayerStandard.mDanmakuView.start();
                    showDanmaku = true;
                    generateSomeDanmaku();
                }
            });
//            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
//
//                @Override
//                public boolean onDanmakuClick(IDanmakus danmakus) {
//                    Log.d("DFM", "onDanmakuClick: danmakus size:" + danmakus.size());
//                    BaseDanmaku latest = danmakus.last();
//                    if (null != latest) {
//                        Log.d("DFM", "onDanmakuClick: text of latest danmaku:" + latest.text);
//                        return true;
//                    }
//                    return false;
//                }
//
//                @Override
//                public boolean onDanmakuLongClick(IDanmakus danmakus) {
//                    return false;
//                }
//
//                @Override
//                public boolean onViewClick(IDanmakuView view) {
//
//                    return false;
//                }
//            });
            mJcVideoPlayerStandard.mDanmakuView.prepare(parser, mContext);
            mJcVideoPlayerStandard.mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    /**
     * 向弹幕View中添加一条弹幕
     * @param content
     *          弹幕的具体内容
     * @param  withBorder
     *          弹幕是否有边框
     */
    private void addDanmaku(String content, boolean withBorder) {
        if(showDanmaku){
            BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
            danmaku.text = content;
            danmaku.padding = 5;
            danmaku.textSize = ScreenUtils.sp2px(SingleVideoActivity.this,14);
            danmaku.textColor = Color.WHITE;
            danmaku.setTime(mJcVideoPlayerStandard.mDanmakuView.getCurrentTime());
            if (withBorder) {
                danmaku.borderColor = Color.GREEN;
            }
            mJcVideoPlayerStandard.mDanmakuView.addDanmaku(danmaku);
        }
    }

    /**
     * 随机生成一些弹幕内容以供测试
     */
    private void generateSomeDanmaku() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(showDanmaku) {
                    int time = new Random().nextInt(300);
                    String content = "" + time + time;
                    addDanmaku(content, false);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // 获取新闻评论列表
    private void commentList(){
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("watchVideo/videoCommentList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",pages+"");
        param.put("limit",limit+"");
        param.put("id",newsId);
        OkGo.<DataResult<ArrayList<CommentInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<CommentInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<CommentInfo>>> response) {
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(pages==1){
                                commentInfoslist.clear();
                            }
                            totalVal=limit*pages;
                            if(response.body().getData()==null||response.body().getData().size()==0){
//                        if(pages==1){
//                            CustomToast.show("暂无评论");
//                        }
                                return;
                            }
                            commentInfoslist.addAll(response.body().getData());
                            showList(commentInfoslist);
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<CommentInfo>>> response) {
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }
    // 新闻评论
    private void addcomment(){
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("watchVideo/videoComment");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("content",et_comment_newsdetail.getText().toString());
        param.put("id",newsId);//新闻ID
        OkGo.<DataResult<Object>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<Object>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<Object>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            et_comment_newsdetail.setText("");
                            pages=1;
                            commentList();
                            videoCommentCnt();
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<Object>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showList(ArrayList<CommentInfo> result) {
        if (commonAdapter == null) {
            commentInfoslist = result;
            commonAdapter = new CommonAdapter<CommentInfo>(SingleVideoActivity.this, result, R.layout.item_singlevideo) {
                @Override
                public void convert(final ViewHolder helper, final CommentInfo item) {
                    final ImageView imageView=helper.getView(R.id.item_video_img);
                    getImageLoader().displayImage(BaseConstant.Image_URL + item.getUserInfo().getHeadImg(), imageView, getImageLoaderHeadOptions(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {}
                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            imageView.setImageResource(R.drawable.tx);
                        }
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {}
                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {}
                    });
                    helper.setText(R.id.item_username,item.getUserInfo().getNickName());
                    helper.setText(R.id.item_content,item.getContent());
                    helper.setText(R.id.item_time,item.getTime());
                }
            };
            lv_listview.setAdapter(commonAdapter);
        } else {
            commonAdapter.onDateChange(result);
//            if(result.size()==1){
//                lv_listview.setSelection(commentInfoslist.size()-1);
//            }
        }
        //记录ID位置
        if(result!=null&&result.size()>0){
            commentIdpositon.clear();
            for(int i=0;i<result.size();i++){
                commentIdpositon.put(result.get(i).getId(),i);
            }
        }
    }
    public void showPopupWindow(final CommentInfo item) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_reply, null);
        if (popupWindow!=null&&popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow=null;
        }
        commentReply_id=item.getId()+"";
        view.findViewById(R.id.reply_finish).setOnClickListener(this);
        TextView reply_count=(TextView)view.findViewById(R.id.reply_count);
        reply_count.setText(item.getReplyCnt()+"条回复");
        TextView reply_name=(TextView)view.findViewById(R.id.reply_name);
        reply_name.setText(item.getUserInfo().getNickName());
        final ImageView reply_img=(ImageView) view.findViewById(R.id.reply_img);
        getImageLoader().displayImage(BaseConstant.Image_URL + item.getUserInfo().getHeadImg(), reply_img, getImageLoaderHeadOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {}
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                reply_img.setImageResource(R.drawable.tx);
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {}
            @Override
            public void onLoadingCancelled(String imageUri, View view) {}
        });
        final TextView reply_praiseCnt=(TextView) view.findViewById(R.id.reply_praiseCnt);
        reply_praiseCnt.setText(item.getPraiseCnt()+"");
        iv_praise= (ImageView) view.findViewById(R.id.iv_praise);
        iv_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acommentPraise(iv_praise,reply_praiseCnt,commentIdpositon.get(item.getId()),item.getId(),"1");
            }
        });
        reply_isPraise=(ImageView) view.findViewById(R.id.reply_isPraise);
        reply_isPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acommentPraise(reply_isPraise,reply_praiseCnt,commentIdpositon.get(item.getId()),item.getId(),"1");
            }
        });
        if(item.getIsPraise()==1){
            reply_isPraise.setImageResource(R.drawable.bf_zand);
            iv_praise.setImageResource(R.drawable.bf_zand);
        }else{
            reply_isPraise.setImageResource(R.drawable.bf_zan);
            iv_praise.setImageResource(R.drawable.bf_zan);
        }

        TextView reply_time=(TextView) view.findViewById(R.id.reply_time);
        reply_time.setText(item.getTime());
        TextView reply_content=(TextView)  view.findViewById(R.id.reply_content);
        reply_content.setText(item.getContent());
        iv_sendreply_newsdetail= (ImageView) view.findViewById(R.id.iv_sendreply_newsdetail);
        iv_sendreply_newsdetail.setOnClickListener(this);
        et_reply_newsdetail= (EditText) view.findViewById(R.id.et_reply_newsdetail);
        et_reply_newsdetail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND&&et_reply_newsdetail.getText().length()>0){
                    iv_sendreply_newsdetail.setOnClickListener(null);
                    addreplay(iv_sendreply_newsdetail,et_reply_newsdetail,item.getId()+"","");
                    hideSoftInput();
                    return true;
                }
                return false;
            }
        });

        reply_refreshlistview = (PullToRefreshListView)view.findViewById(R.id.reply_refreshlistview);
        reply_refreshlistview.setPullLoadEnabled(true);
        reply_refreshlistview.setPullRefreshEnabled(true);
        reply_refreshlistview.setScrollLoadEnabled(false);
        reply_list = reply_refreshlistview.getRefreshableView();
        reply_list.setVerticalScrollBarEnabled(false);
        reply_list.setDivider(null);
        reply_list.setDividerHeight(ScreenUtils.dp2px(this, 1));
        reply_refreshlistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
        {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                reply_pages=1;
                reply_List(item.getId()+"");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                if(reply_totalVal>reply_Infoslist.size()){
                    CustomToast.show("已经到底咯");
                    reply_refreshlistview.onPullUpRefreshComplete();
                }else{
                    reply_pages=reply_pages+1;
                    reply_List(item.getId()+"");
                }
            }
        });
        reply_commonAdapter = null;
        reply_Infoslist.clear();
        showreply_List(reply_Infoslist);
        reply_List(item.getId()+"");
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        // 设置点击窗口外边窗口消失
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_trans));
        popupWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

    }

    public void showreplypopupWindow(final CommentInfo item,int type) {
        View view = LayoutInflater.from(this).inflate(R.layout.pw_reply, null);
        if (replypopupWindow!=null&&replypopupWindow.isShowing()) {
            replypopupWindow.dismiss();
            replypopupWindow=null;
        }

        iv_pwreply_newsdetail= (ImageView) view.findViewById(R.id.iv_pwreply_newsdetail);
        iv_pwreply_newsdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_pwreply_newsdetail.getText().length()>0){
                    iv_pwreply_newsdetail.setOnClickListener(null);
                    addreplay(iv_pwreply_newsdetail,et_pwreply_newsdetail,commentReply_id+"",item.getId()+"");
                    hideSoftInput();
                    replypopupWindow.dismiss();
                }
            }
        });
        et_pwreply_newsdetail= (EditText) view.findViewById(R.id.et_pwreply_newsdetail);
        if(type==1){
            et_pwreply_newsdetail.setHint("回复："+item.getReplyUser().getNickName());
        }else{
            et_pwreply_newsdetail.setHint("回复："+item.getUserInfo().getNickName());
        }
        et_pwreply_newsdetail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND&&et_pwreply_newsdetail.getText().length()>0){
                    iv_pwreply_newsdetail.setOnClickListener(null);
                    addreplay(iv_pwreply_newsdetail,et_pwreply_newsdetail,commentReply_id+"",item.getId()+"");
                    hideSoftInput();
                    replypopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        et_pwreply_newsdetail.requestFocus();
        LinearLayout pw_outlayout= (LinearLayout) view.findViewById(R.id.pw_outlayout);
        pw_outlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_pwreply_newsdetail.getWindowToken(), 0); //强制隐藏键盘
                replypopupWindow.dismiss();
            }
        });
        replypopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        // 设置点击窗口外边窗口消失
        replypopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_trans));
        replypopupWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        replypopupWindow.setFocusable(true);
        replypopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
    // 回复评论列表
    private void reply_List(String commentReply_id){
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("/api/news/newsCommentReply");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",reply_pages+"");
        param.put("limit",reply_limit+"");
        param.put("id",commentReply_id);//评论ID
        OkGo.<DataResult<ArrayList<CommentInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<CommentInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<CommentInfo>>> response) {
                        reply_refreshlistview.setLastUpdatedLabel(setLastUpdateTime());
                        reply_refreshlistview.onPullUpRefreshComplete();
                        reply_refreshlistview.onPullDownRefreshComplete();
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if( reply_pages==1){
                                reply_Infoslist.clear();
                            }
                            reply_totalVal= reply_limit* reply_pages;
                            if(response.body().getData()==null||response.body().getData().size()==0){
//                        if( reply_pages==1){
//                            CustomToast.show("暂无回复");
//                        }
                                return;
                            }
                            reply_Infoslist.addAll(response.body().getData());
                            showreply_List(reply_Infoslist);
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<CommentInfo>>> response) {
                        reply_refreshlistview.setLastUpdatedLabel(setLastUpdateTime());
                        reply_refreshlistview.onPullUpRefreshComplete();
                        reply_refreshlistview.onPullDownRefreshComplete();
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
    private void showreply_List(ArrayList<CommentInfo> result) {
        if (reply_commonAdapter == null) {
            reply_commonAdapter = new CommonAdapter<CommentInfo>(SingleVideoActivity.this, result, R.layout.item_singlevideo) {
                @Override
                public void convert(final ViewHolder helper, final CommentInfo item){
                    final ImageView imageView=helper.getView(R.id.item_video_img);
                    getImageLoader().displayImage(BaseConstant.Image_URL + item.getUserInfo().getHeadImg(), imageView, getImageLoaderHeadOptions(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {}
                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            imageView.setImageResource(R.drawable.tx);
                        }
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {}
                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {}
                    });
                    helper.setText(R.id.item_username,item.getUserInfo().getNickName());
//                    final TextView item_praiseCnt=helper.getView(R.id.item_praiseCnt);
//                    item_praiseCnt.setText(item.getPraiseCnt()+"");
//                    TextView item_content=helper.getView(R.id.item_content);
//                    if((!item.getReplyUser().getId().equals(""))&&(!item.getReplyUser().getId().equals("0"))){
//                        if(item.getUserInfo().getId().equals(item.getReplyUser().getId())){
//                            item_content.setText(item.getContent()+"");
//                        }else{
//                            String content=item.getContent()+"//"+"@"+item.getReplyUser().getNickName()+"："+item.getReplyContent();
//                            String clickTxt = "@"+item.getReplyUser().getNickName();
//                            SpannableStringBuilder spannable = new SpannableStringBuilder(content);
//                            int startIndex = content.indexOf(clickTxt);
//                            int endIndex = startIndex + clickTxt.length();
//                            //文字点击
//                            spannable.setSpan(new TextClick(item.getId()+"",item),startIndex,endIndex
//                                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            //一定要记得设置，不然点击不生效
//                            item_content.setMovementMethod(LinkMovementMethod.getInstance());
//                            item_content.setText(spannable);
//                        }
//                    }else{
//                        item_content.setText(item.getContent()+"");
//                    }
//                    helper.setText(R.id.item_time,item.getTime());
//                    final ImageView praise=helper.getView(R.id.item_isPraise);
//                    if(item.getIsPraise()==1){
//                        praise.setImageResource(R.drawable.bf_zand);
//                    }else{
//                        praise.setImageResource(R.drawable.bf_zan);
//                    }
//                    praise.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            acommentPraise(praise,item_praiseCnt,reply_Idpositon.get(item.getId()),item.getId(),"2");
//                        }
//                    });
//                    TextView textView=helper.getView(R.id.item_singlevideo_toreply);
////                    textView.setVisibility(View.GONE);
//                    textView.setText(item.getReplyCnt()+"回复");
//                    textView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            reply_id=item.getId()+"";
//                            showreplypopupWindow(item,0);
//                        }
//                    });
                }
            };
            reply_list.setAdapter(reply_commonAdapter);
        }else {
            reply_commonAdapter.onDateChange(result);
            if(reply_pages==1){
                reply_list.setSelectionAfterHeaderView();
            }
        }
        //记录ID位置
        if(result!=null&&result.size()>0){
            reply_Idpositon.clear();
            for(int i=0;i<result.size();i++){
                reply_Idpositon.put(result.get(i).getId(),i);
            }
        }
    }
    //第三层循环回复点击事件
    private class TextClick extends ClickableSpan {
        private String replyid;
        private CommentInfo item;
        public  TextClick(String replyID,CommentInfo item){
            this.replyid=replyID;
            this.item=item;
        }
        @Override
        public void onClick(View widget) {
            //在此处理点击事件
//            reply_id=replyid;
//            showreplypopupWindow(item,1);
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.blue));
        }
    }
    // 新闻评论点赞
    private void acommentPraise(final ImageView praise, final TextView item_praiseCnt, final int position, final int id, final String type){
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("/api/news/addNewsCommentOrReplyPraise");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("type",type);//1评论，2回复
        param.put("id",id+"");//评论ID
        OkGo.<DataResult<Object>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<Object>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<Object>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            praise.setImageResource(R.drawable.bf_zand);
                            //本地更新列表
                            if(type.equals("1")){//评论攒
                                item_praiseCnt.setText(commentInfoslist.get(position).getPraiseCnt()+1+"");
                                commentInfoslist.get(position).setIsPraise(1);
                                commentInfoslist.get(position).setPraiseCnt(commentInfoslist.get(position).getPraiseCnt()+1);
                                commonAdapter.notifyDataSetChanged();
                                //同步评论攒
                                if(praise.getId()==R.id.reply_isPraise&&iv_praise!=null){
                                    iv_praise.setImageResource(R.drawable.bf_zand);
                                }else if(praise.getId()==R.id.iv_praise&&reply_isPraise!=null){
                                    reply_isPraise.setImageResource(R.drawable.bf_zand);
                                }
                            }else if(type.equals("2")){//回复攒
                                item_praiseCnt.setText(reply_Infoslist.get(position).getPraiseCnt()+1+"");
                                reply_Infoslist.get(position).setIsPraise(1);
                                reply_Infoslist.get(position).setPraiseCnt(reply_Infoslist.get(position).getPraiseCnt()+1);
                                reply_commonAdapter.notifyDataSetChanged();
                            }

                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }

                        praise.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                acommentPraise(praise,item_praiseCnt,position,id,type);
                            }
                        });
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<Object>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        praise.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                acommentPraise(praise,item_praiseCnt,position,id,type);
                            }
                        });
                    }
                });
    }
    // 新闻评论回复
    private void addreplay(final ImageView senView, EditText inputEt, final String commentReply_id, String replyId){
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("/api/news/addNewsCommentReply");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("content",inputEt.getText().toString());
        param.put("replyId",replyId);//回复ID
        param.put("id",commentReply_id);//评论ID
        OkGo.<DataResult<Object>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<Object>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<Object>> response) {
                        getlDialog().dismiss();
                        senView.setOnClickListener(SingleVideoActivity.this);
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            et_reply_newsdetail.setText("");
                            reply_pages=1;
                            reply_List(commentReply_id);
                            for (int i=0;i<commentInfoslist.size();i++){
                                if(commentInfoslist.get(i).getId()==Integer.parseInt(commentReply_id)){
                                    commentInfoslist.get(i).setReplyCnt(commentInfoslist.get(i).getReplyCnt()+1);
                                    commonAdapter.notifyDataSetChanged();
                                }
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<Object>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reply_finish:
                if(popupWindow!=null&&popupWindow.isShowing()){
                    hideSoftInput();
                    popupWindow.dismiss();
                }
                break;
            case R.id.iv_sendcomment_newsdetail:
                if(!BaseConstant.isLogin()){
                    startActivity(new Intent(SingleVideoActivity.this, LoginActivity.class));
                    return;
                }
                if(et_comment_newsdetail.getText().toString().trim().length()==0){
                    CustomToast.show("请输入评论内容");
                }else{
                    iv_sendcomment_newsdetail.setOnClickListener(null);
                    addcomment();
                }
                break;
            case R.id.iv_sendreply_newsdetail:
                if(et_reply_newsdetail.getText().length()>0){
                    iv_sendreply_newsdetail.setOnClickListener(null);
                    addreplay(iv_sendreply_newsdetail,et_reply_newsdetail,commentReply_id,"");
                }
                hideSoftInput();
                break;
            case R.id.iv_share_video:
//                if(isCollect.equals("0")){
//                    collectNew(iv_collection_newsdetail,newsId);
//                }else{
//                    cancelcollectNew(iv_collection_newsdetail,newsId);
//                }
                ShareUtil share=new ShareUtil(SingleVideoActivity.this,newsId);
                share.showSharePopupWindow();

                System.out.println("============>"+mJcVideoPlayerStandard.getDuration());
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorEventListener!=null){
            sensorManager.unregisterListener(sensorEventListener);
            JCVideoPlayer.releaseAllVideos();
        }
//        if (mJcVideoPlayerStandard.mDanmakuView != null && mJcVideoPlayerStandard.mDanmakuView.isPrepared()) {
//            mJcVideoPlayerStandard.mDanmakuView.pause();
//        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
//        if (mJcVideoPlayerStandard.mDanmakuView != null) {
//            // dont forget release!
//            mJcVideoPlayerStandard.mDanmakuView.release();
//            mJcVideoPlayerStandard.mDanmakuView = null;
//        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        if (mJcVideoPlayerStandard.mDanmakuView != null && mJcVideoPlayerStandard.mDanmakuView.isPrepared() && mJcVideoPlayerStandard.mDanmakuView.isPaused()) {
//            mJcVideoPlayerStandard.mDanmakuView.resume();
//        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //判断是否从 SingleVideoActivity 第一次进入 JCVideoPlayer，用于进入后自动启动播放，此界面结束归零
        JCUtils.isfirst=0;
//        JCVideoPlayer.showSupportActionBar(this,2);//解锁
        unregisterReceiver(mListenerID);
        showDanmaku = false;
//        if (mJcVideoPlayerStandard.mDanmakuView != null) {
//            // dont forget release!
//            mJcVideoPlayerStandard.mDanmakuView.release();
//            mJcVideoPlayerStandard.mDanmakuView = null;
//        }
    }
    private void setBoardCast() {
        IntentFilter f = new IntentFilter();
        f.addAction("to_SingleVideo");
        registerReceiver(mListenerID, f);
    }
    BroadcastReceiver mListenerID = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String type=intent.getStringExtra("type");
            if(type.equals("finish")){
                finish();
            }
        }
    };


    private void userGetCoupon()
    {
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("watchVideo/userGetCoupon");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",newsId);
        OkGo.<DataResult<CouponInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<CouponInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<CouponInfo>> response) {
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){
                                couponInfo=response.body().getData();
                                if(couponInfo.getId()!=null&&!couponInfo.getId().equals("0")){
                                    showPopupWindow();
                                }
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<CouponInfo>> response) {
                        showVolleyError(null);
                    }
                });
    }


    private void watchVideo()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("watchVideo/watchVideo");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",couponInfo.getId());
        param.put("type","2");//1新用户注册 2看视频 3首页领取
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
                            CustomToast.show("领取成功");
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


    private PopupWindow popupWindo;
    public void showPopupWindow()
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.pw_video_yhq, null);
        TextView tv_receive = (TextView)view.findViewById(R.id.tv_receive);
        TextView tv_price_yhq = (TextView)view.findViewById(R.id.tv_price_yhq);
        TextView tv_date_yhq = (TextView)view.findViewById(R.id.tv_date_yhq);
        TextView tv_info_yhq = (TextView)view.findViewById(R.id.tv_info_yhq);
        tv_price_yhq.setText(couponInfo.getValue()+"元");
        if(couponInfo.getEndTime().equals("")){
            tv_date_yhq.setText("");
        }else{
            tv_date_yhq.setText("有效期至："+couponInfo.getEndTime());
        }

        tv_info_yhq.setText(couponInfo.getInfo()+"仅售"+couponInfo.getPrice()+"元,快来抢购！");

        tv_receive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
                if(!BaseConstant.isLogin()){
                    startActivity(new Intent(SingleVideoActivity.this, LoginActivity.class));
                    return;
                }
                watchVideo();
            }
        });

        // 自定义view添加触摸事件
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindo != null && popupWindo.isShowing()) {
                    popupWindo.dismiss();
                    popupWindo = null;
                }
                return false;
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        popupWindo = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindo.setOutsideTouchable(true);
        popupWindo.setFocusable(true);
        fitPopupWindowOverStatusBar(popupWindo);
        popupWindo.setBackgroundDrawable(new ColorDrawable());
        popupWindo.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void videoCommentCnt()
    {
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("watchVideo/videoCommentCnt");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",newsId);
        OkGo.<DataResult<DataInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<DataInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            tv_number_singlevideo.setText(response.body().getData().getReplyCnt()+"次讨论");
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<DataInfo>> response) {
                        showVolleyError(null);
                    }
                });
    }

}
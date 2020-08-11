package com.glavesoft.pawnuser.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.VolleyError;
import com.glavesoft.F;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.appraisal.EvaluationActivity;
import com.glavesoft.pawnuser.activity.appraisal.SendCallActivity;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.activity.main.CertificateActivity;
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.activity.main.HistoricalSearchActivity;
import com.glavesoft.pawnuser.activity.main.MapActivity;
import com.glavesoft.pawnuser.activity.main.MessageActivity;
import com.glavesoft.pawnuser.activity.main.StoreActivity;
import com.glavesoft.pawnuser.activity.main.WebActivity;
import com.glavesoft.pawnuser.activity.personal.EwmActivity;
import com.glavesoft.pawnuser.activity.personal.PersonalActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.DeadPawnageActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.JdGoodsDetailActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.ShoppingMallActivity;
import com.glavesoft.pawnuser.activity.video.SingleVideoActivity;
import com.glavesoft.pawnuser.activity.video.VideoListActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.frg.FrgProductDetail;
import com.glavesoft.pawnuser.mod.CouponInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.IndexBannerInfo;
import com.glavesoft.pawnuser.mod.IndexMenuInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.StoreDetailInfo;
import com.glavesoft.pawnuser.mod.VideoInfo;
import com.glavesoft.pawnuser.model.ModelData;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.GlideLoader;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.util.StringUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.FlexibleRoundedBitmapDisplayer;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.view.MyScrollView;
import com.glavesoft.view.RoundImageView;
import com.glavesoft.view.SlideShowView;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.view.MListView;
import com.sobot.chat.SobotApi;
import com.sobot.chat.utils.ZhiChiConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author 严光
 * @date: 2018/5/8
 * @company:常州宝丰
 */
public class MainFragment extends BaseFragment implements MyScrollView.onScrollChangedListener, View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    private int index;
    CommonAdapter commAdapter;
    private MyScrollView mScrollView;
    private LinearLayout title_home;
    private RelativeLayout fl_home_partent;
    private SlideShowView ssv_home_pic;
    private int mHeight, xHeight;
    // private SwipeRefreshLayout srl_home;
    private ViewFlipper viewFlipper;
    //private MyListView lv_rzsc;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<IndexBannerInfo> newslist = new ArrayList<>();
    ArrayList<ArrayList<IndexBannerInfo>> newslist1 = new ArrayList<>();

    private GridViewForNoScroll nsgv_home_jdxp;
    MListView mMListView;
    private ArrayList<String> goodslist = new ArrayList<>();
    //头部
    private RoundImageView iv_home_tx;
    private ImageView iv_home_sys;
    private TextView tv_home_news;
    private TextView titlebar_home_search;

    private TextView ll_home_mysp, ll_home_jdsc, ll_home_rzhq, ll_home_ddhb, ll_home_kx, ll_home_rzsc;

    private TextView ll_home_jm, ll_home_hw;

    private ImageView iv_rzsc_more, iv_jdxp_more, iv_video_more;


    private ArrayList<IndexBannerInfo> IndexBannerList = new ArrayList<>();
    ArrayList<IndexBannerInfo> IndexBannerList1 = new ArrayList<>();
    ArrayList<IndexBannerInfo> IndexBannerList2 = new ArrayList<>();

    private LinearLayout ll_home_rzsclist, ll_home_jdxplist, ll_home_videolist;

    private ImageView iv_home_rzsc;
    private TextView tv_home_rzsc_name;
    private FrameLayout fl_home_rzsc;
    private GridViewForNoScroll grid_home_rzsc;


    private FrameLayout fl_home_jdxp;
    private ImageView iv_home_jdxp;
    private TextView tv_time_jdxp, tv_name_jdxp, tv_price_jdxp;

    private FrameLayout fl_home_video;
    private ImageView iv_home_video;
    private TextView tv_name_video;

    private BGARefreshLayout mRefreshLayout;

    ArrayList<IndexMenuInfo> IndexMenuList1 = new ArrayList<>();
    ArrayList<IndexMenuInfo> IndexMenuList2 = new ArrayList<>();
    ArrayList<IndexMenuInfo> IndexMenuList3 = new ArrayList<>();
    ArrayList<VideoInfo> VideoList = new ArrayList<>();
    CommonAdapter IndexMenuAdapter;
    CouponInfo couponInfo;

    public static MainFragment newInstance(int index) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt("index");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        setBoardCast();
        initView(view);
        return view;
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ZhiChiConstant.sobot_unreadCountBrocast);
        getActivity().registerReceiver(mListenerID, filter);

        //注册广播
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("Permission_Refresh");
        getActivity().registerReceiver(mListenerID1, filter1);

    }


    BroadcastReceiver mListenerID = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int noReadNum = intent.getIntExtra("noReadCount", 0);//未读消息数
            String content = intent.getStringExtra("content"); //新消息内容

        }
    };

    BroadcastReceiver mListenerID1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            indexBanner();
            getIndexMenu();
            videoList();
        }
    };

    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mListenerID);
        getActivity().unregisterReceiver(mListenerID1);
    }

    private void initView(View view) {
        iv_home_tx = (RoundImageView) view.findViewById(R.id.iv_home_tx);
        iv_home_sys = (ImageView) view.findViewById(R.id.iv_home_sys);
        tv_home_news = (TextView) view.findViewById(R.id.tv_home_news);
        titlebar_home_search = (TextView) view.findViewById(R.id.titlebar_home_search);
        iv_home_tx.setOnClickListener(this);
        iv_home_sys.setOnClickListener(this);
        tv_home_news.setOnClickListener(this);
        titlebar_home_search.setOnClickListener(this);

        //srl_home= (SwipeRefreshLayout)view.findViewById(R.id.srl_home);
        mScrollView = (MyScrollView) view.findViewById(R.id.scrollView_home);
        fl_home_partent = (RelativeLayout) view.findViewById(R.id.fl_home_partent);
        title_home = (LinearLayout) view.findViewById(R.id.title_home);
        ssv_home_pic = (SlideShowView) view.findViewById(R.id.ssv_home_pic);

        ll_home_mysp = (TextView) view.findViewById(R.id.ll_home_mysp);
        ll_home_jdsc = (TextView) view.findViewById(R.id.ll_home_jdsc);
        ll_home_rzhq = (TextView) view.findViewById(R.id.ll_home_rzhq);
        ll_home_ddhb = (TextView) view.findViewById(R.id.ll_home_ddhb);
        ll_home_kx = (TextView) view.findViewById(R.id.ll_home_kx);
        ll_home_rzsc = (TextView) view.findViewById(R.id.ll_home_rzsc);
        ll_home_mysp.setOnClickListener(this);
        ll_home_jdsc.setOnClickListener(this);
        ll_home_rzhq.setOnClickListener(this);
        ll_home_ddhb.setOnClickListener(this);
        ll_home_kx.setOnClickListener(this);
        ll_home_rzsc.setOnClickListener(this);

        ll_home_jm = (TextView) view.findViewById(R.id.ll_home_jm);
        ll_home_hw = (TextView) view.findViewById(R.id.ll_home_hw);
        ll_home_jm.setOnClickListener(this);
        ll_home_hw.setOnClickListener(this);


        viewFlipper = (ViewFlipper) view.findViewById(R.id.vf_home_tip);
        //lv_rzsc= (MyListView) view.findViewById(R.id.mylv_home_rzsc);

        ll_home_rzsclist = (LinearLayout) view.findViewById(R.id.ll_home_rzsclist);
        ll_home_jdxplist = (LinearLayout) view.findViewById(R.id.ll_home_jdxplist);
        ll_home_videolist = (LinearLayout) view.findViewById(R.id.ll_home_videolist);

        iv_rzsc_more = (ImageView) view.findViewById(R.id.iv_rzsc_more);
        iv_jdxp_more = (ImageView) view.findViewById(R.id.iv_jdxp_more);
        iv_video_more = (ImageView) view.findViewById(R.id.iv_video_more);
        iv_rzsc_more.setOnClickListener(this);
        iv_jdxp_more.setOnClickListener(this);
        iv_video_more.setOnClickListener(this);

        iv_home_rzsc = (ImageView) view.findViewById(R.id.iv_home_rzsc);
        tv_home_rzsc_name = (TextView) view.findViewById(R.id.tv_home_rzsc_name);

        grid_home_rzsc = (GridViewForNoScroll) view.findViewById(R.id.grid_home_rzsc);


        fl_home_rzsc = (FrameLayout) view.findViewById(R.id.fl_home_rzsc);


        fl_home_jdxp = (FrameLayout) view.findViewById(R.id.fl_home_jdxp);
        iv_home_jdxp = (ImageView) view.findViewById(R.id.iv_home_jdxp);
        tv_time_jdxp = (TextView) view.findViewById(R.id.tv_time_jdxp);
        tv_name_jdxp = (TextView) view.findViewById(R.id.tv_name_jdxp);
        tv_price_jdxp = (TextView) view.findViewById(R.id.tv_price_jdxp);

        fl_home_video = (FrameLayout) view.findViewById(R.id.fl_home_video);
        iv_home_video = (ImageView) view.findViewById(R.id.iv_home_video);
        tv_name_video = (TextView) view.findViewById(R.id.tv_name_video);

        nsgv_home_jdxp = (GridViewForNoScroll) view.findViewById(R.id.nsgv_home_jdxp);
        mMListView = (MListView) view.findViewById(R.id.mMListView);

        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_home_refresh);
        mRefreshLayout.setDelegate(this);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getContext(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
        mRefreshLayout.setIsShowLoadingMoreView(false);

        mScrollView.addOnScrollChangedListener(this);
        fl_home_partent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fl_home_partent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mHeight = ssv_home_pic.getHeight();
                onScrollChanged(mScrollView.getScrollY());
            }
        });


        nsgv_home_jdxp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                F.INSTANCE.go2GoodeDetail(getActivity(), IndexMenuList2.get(position + 1).getId(), "jd");
            }
        });
        getIndexMenu();
        videoList();
        if (BaseConstant.isLogin()) {
            userGetCoupon();
        }
    }

    public void onResume() {
        super.onResume();
        if (!LocalData.getInstance().getUserInfo().getHeadImg().equals("")) {
            String imageurl = BaseConstant.Image_URL + LocalData.getInstance().getUserInfo().getHeadImg();
            getImageLoader().displayImage(imageurl, iv_home_tx, getImageLoaderHeadOptions());
        }
        indexBanner();
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_home_tx://个人资料
                startActivity(new Intent(getActivity(), PersonalActivity.class));
                break;

            case R.id.iv_home_sys://扫一扫二维码
                startActivity(new Intent(getActivity(), EwmActivity.class));
                break;

            case R.id.tv_home_news://消息
                if (BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), MessageActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.titlebar_home_search://搜索
                startActivity(new Intent(getActivity(), HistoricalSearchActivity.class));
                break;
            case R.id.ll_home_mysp://买艺术品
                startActivity(new Intent(getActivity(), ShoppingMallActivity.class));
                break;
            case R.id.ll_home_jdsc://绝当商城
                startActivity(new Intent(getActivity(), DeadPawnageActivity.class));
                break;
            case R.id.ll_home_rzhq://融资换钱
                if (BaseConstant.isLogin()) {
                    intent.setClass(getActivity(), EvaluationActivity.class);
                    intent.putExtra("pos", 0);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

                break;
            case R.id.ll_home_ddhb://典当伙伴
                requestLOCATIONPermissions(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        startActivity(new Intent(getActivity(), MapActivity.class));
                    }
                });
                break;
            case R.id.ll_home_rzsc://认证证书查询
                startActivity(new Intent(getActivity(), CertificateActivity.class));
                break;
            case R.id.ll_home_kx://看秀
                startActivity(new Intent(getActivity(), VideoListActivity.class));
                break;
//            case R.id.ll_home_fl://分类
//                //startActivity(new Intent(getActivity(), TypesActivity.class));
//                intent.setClass(getActivity(), WebActivity.class);
//                intent.putExtra("titleName", "了解我们");
//                intent.putExtra("url", BaseConstant.BaseURL + "/dd_about/about.html");
//                startActivity(intent);
//                break;
            case R.id.iv_rzsc_more:
                startActivity(new Intent(getActivity(), ShoppingMallActivity.class));
                break;
            case R.id.iv_jdxp_more:
                startActivity(new Intent(getActivity(), DeadPawnageActivity.class));
                break;
            case R.id.iv_video_more:
                startActivity(new Intent(getActivity(), VideoListActivity.class));
                break;
            case R.id.ll_home_jm://寄卖
                //寄拍
                if (BaseConstant.isLogin()) {
                    intent.setClass(getActivity(), SendCallActivity.class);
                    intent.putExtra("pos", 0);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_home_hw://好物
                startActivity(new Intent(getActivity(), DeadPawnageActivity.class));
                break;

//            case R.id.ll_home_help:
//                intent.setClass(getActivity(), WebActivity.class);
//                intent.putExtra("titleName", "帮助");
//                intent.putExtra("url", BaseConstant.BaseURL + "/dist/index.html");
//                startActivity(intent);
//                break;
        }
    }

    @Override
    public void onScrollChanged(int y) {

    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        indexBanner();
        getIndexMenu();
        videoList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mRefreshLayout.endLoadingMore();
        return false;
    }

    private void pawnmsg() {
        newslist = new ArrayList<>();
        for (int i = 0; i < IndexBannerList2.size(); i++) {
            newslist.add(IndexBannerList2.get(i));
        }

        int totlecount = 0;
        int count = 0;
        if ((newslist.size()) % 2 == 0) {
            totlecount = newslist.size() / 2;
        } else {
            totlecount = (newslist.size() / 2) + 1;
        }

        for (int i = 0; i < totlecount; i++) {
            ArrayList<IndexBannerInfo> list = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                if (count < newslist.size()) {
                    list.add(newslist.get(count));
                    count++;
                }
            }

            newslist1.add(list);
        }
        timer.cancel();
        timer = new Timer();
        if (newslist1.size() > 0) {
            setView(mCurrPos, mCurrPos);
        }
        initRollNotice();//拍档头条
    }

    private int mCurrPos;
    Timer timer = new Timer();

    private void initRollNotice() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        moveNext();
                    }
                });
            }
        };
        timer.schedule(task, 4000, 4000);
    }

    private void moveNext() {
        setView(mCurrPos, mCurrPos + 1);
        viewFlipper.setInAnimation(getActivity(), R.anim.in_bottomtop);
        viewFlipper.setOutAnimation(getActivity(), R.anim.out_bottomtop);
        viewFlipper.showNext();
    }

    private void setView(int curr, int next) {
        View noticeView = getActivity().getLayoutInflater().inflate(R.layout.notice_item, null);
        TextView notice_tv = (TextView) noticeView.findViewById(R.id.notice_tv);
        TextView notice_tv1 = (TextView) noticeView.findViewById(R.id.notice_tv1);

        if ((curr < next) && (next > (newslist1.size() - 1))) {
            next = 0;
        } else if ((curr > next) && (next < 0)) {
            next = newslist1.size() - 1;
        }
        notice_tv.setText(newslist1.get(next).get(0).getPawnMsg());
        if (newslist1.get(next).size() > 1) {
            notice_tv1.setText(newslist1.get(next).get(1).getPawnMsg());
            notice_tv1.setVisibility(View.VISIBLE);
        } else {
            notice_tv1.setVisibility(View.INVISIBLE);
        }

        final int finalNext = next;
        notice_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String type = newslist1.get(finalNext).get(0).getType();
                String content = newslist1.get(finalNext).get(0).getContent();
                String State = newslist1.get(finalNext).get(0).getCstate();
                gotoJump(type, content, State);
            }
        });

        notice_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String type = newslist1.get(finalNext).get(1).getType();
                String content = newslist1.get(finalNext).get(1).getContent();
                String State = newslist1.get(finalNext).get(1).getCstate();
                gotoJump(type, content, State);

            }
        });

        if (viewFlipper.getChildCount() > 1) {
            viewFlipper.removeViewAt(0);
        }
        viewFlipper.addView(noticeView, viewFlipper.getChildCount());
        mCurrPos = next;

    }

    private void gotoJump(String type, String content, String State) {
        //0不跳转；1网址；2富文本；3认证商城商品详情页；4绝当商城商品详情页；5视频详情页
        if (type.equals("1")) {
            if (BaseConstant.isVideo(content)) {
                JCVideoPlayerStandard.startFullscreen(getActivity(), JCVideoPlayerStandard.class,
                        content, "");
            } else {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("titleName", "详情");
                intent.putExtra("url", content);
                startActivity(intent);
            }

        } else if (type.equals("2")) {
            if (BaseConstant.isVideo(content)) {
                JCVideoPlayerStandard.startFullscreen(getActivity(), JCVideoPlayerStandard.class,
                        content, "");
            } else {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("titleName", "详情");
                intent.putExtra("url", content);
                startActivity(intent);
            }

        } else if (type.equals("3")) {

            F.INSTANCE.go2GoodeDetail(getActivity(), content, "rz");
        } else if (type.equals("4")) {
            if (State.equals("1")) {//竞拍
                Intent intent = new Intent(getActivity(), JdGoodsDetailActivity.class);
                intent.putExtra("id", content);
                startActivity(intent);
            } else {
                F.INSTANCE.go2GoodeDetail(getActivity(), content, "jd");
            }

        } else if (type.equals("6")) {
            StoreDetailInfo info;
            if (!com.blankj.utilcode.util.StringUtils.isEmpty(content)) {
                java.lang.reflect.Type classtype = new TypeToken<StoreDetailInfo>() {
                }.getType();
                info = CommonUtils.fromJson(content, classtype, CommonUtils.DEFAULT_DATE_PATTERN);

                Intent intent = new Intent();
                intent.setClass(getActivity(), StoreActivity.class);
                intent.putExtra("storeid", info.getId());
                startActivity(intent);
            }
        }
    }


    private void showList(final ArrayList<IndexMenuInfo> result) {
        GlideLoader.loadRoundImage(BaseConstant.Image_URL + result.get(0).getImg(), iv_home_rzsc, R.drawable.sy_bj);

        tv_home_rzsc_name.setText(result.get(0).getTitle());

        if (result.size() > 1) {
            final ArrayList<IndexMenuInfo> result1 = new ArrayList<>();
            for (int i = 1; i < result.size(); i++) {
                result1.add(result.get(i));
            }
            CommonAdapter commAdapter = new CommonAdapter<IndexMenuInfo>(getActivity(), result1,
                    R.layout.item_home_rzsc1) {
                @Override
                public void convert(final ViewHolder helper, final IndexMenuInfo item) {
                    helper.setText(R.id.tv_home_rzsc_type, item.getGoodsType());
                    helper.setText(R.id.tv_home_rzsc_title, item.getTitle());

                    setOvelImage(//圆角图
                            BaseConstant.Image_URL + item.getImg(),
                            R.drawable.shape_coner_white2,
                            helper.getView(R.id.mImageView)
                    );
                }
            };

            grid_home_rzsc.setAdapter(commAdapter);

            grid_home_rzsc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    gotoGoodsDetail(result1.get(i).getId());
                }
            });
        }


//
        fl_home_rzsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGoodsDetail(result.get(0).getId());
            }
        });
    }

    private void gotoGoodsDetail(String id) {
        F.INSTANCE.go2GoodeDetail(getActivity(), id, "rz");
    }

    private void showgridList(final ArrayList<IndexMenuInfo> result) {
        GlideLoader.loadRoundImage(BaseConstant.Image_URL + result.get(0).getImg(), iv_home_jdxp, R.drawable.sy_bj);
        tv_time_jdxp.setText("结拍时间：" + result.get(0).getEndTime());
        tv_name_jdxp.setText(result.get(0).getTitle());
        tv_price_jdxp.setText("￥" + result.get(0).getPrice());
        tv_time_jdxp.setVisibility(View.GONE);
        nsgv_home_jdxp.setVisibility(View.GONE);
        fl_home_jdxp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                F.INSTANCE.go2GoodeDetail(getActivity(), result.get(0).getId(), "jd");
            }
        });
        if (result.size() > 1) {
            nsgv_home_jdxp.setVisibility(View.VISIBLE);
            ArrayList<IndexMenuInfo> IndexMenuList = new ArrayList<>();
            for (int i = 1; i < result.size(); i++) {
                IndexMenuList.add(result.get(i));
            }
            CommonAdapter commAdapter = new CommonAdapter<IndexMenuInfo>(getActivity(), IndexMenuList,
                    R.layout.item_home_gridview) {
                @Override
                public void convert(final ViewHolder helper, final IndexMenuInfo item) {
                    if (helper.getPosition() % 2 == 0) {
                        helper.getView(R.id.mLinearLayout1).setVisibility(View.VISIBLE);
                        helper.getView(R.id.mLinearLayout2).setVisibility(View.GONE);
                        GlideLoader.loadRoundTopImage(BaseConstant.Image_URL + item.getImg(), helper.getView(R.id.iv_home_grid), R.drawable.sy_bj);
                        helper.setText(R.id.tv_title_homegrid, item.getTitle());
                        helper.setText(R.id.tv_price_homegrid, "￥" + item.getPrice());
                    } else {
                        helper.getView(R.id.mLinearLayout1).setVisibility(View.GONE);
                        helper.getView(R.id.mLinearLayout2).setVisibility(View.VISIBLE);
                        GlideLoader.loadRoundBottomImage(BaseConstant.Image_URL + item.getImg(), helper.getView(R.id.iv_home_grid2), R.drawable.sy_bj);
                        helper.setText(R.id.tv_title_homegrid2, item.getTitle());
                        helper.setText(R.id.tv_price_homegrid2, "￥" + item.getPrice());

                    }


                }
            };

            nsgv_home_jdxp.setAdapter(commAdapter);
        }
    }

    private void showgridVideoList(final ArrayList<VideoInfo> result) {
        GlideLoader.loadRoundImage(BaseConstant.Image_URL + result.get(0).getImg(), iv_home_video, R.drawable.sy_bj);
        tv_name_video.setText(result.get(0).getTitle());
        mMListView.setVisibility(View.GONE);
        fl_home_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SingleVideoActivity.class);
                intent.putExtra("url", BaseConstant.Video_URL + result.get(0).getVideo());
                intent.putExtra("type", JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL);
                intent.putExtra("name", result.get(0).getTitle());
                intent.putExtra("id", result.get(0).getId());
                startActivity(intent);
            }
        });
        if (result.size() > 1) {
            mMListView.setVisibility(View.VISIBLE);
            ArrayList<VideoInfo> VideoList = new ArrayList<>();
            List<ModelData<VideoInfo>> mModelDatas = new ArrayList<>();
            for (int i = 1; i < result.size(); i++) {
//                VideoList.add(result.get(i));
                if (i % 5 == 1) {
                    ModelData<VideoInfo> item = new ModelData<>();
                    item.count = 2;
                    for (int j = i; j < Math.min(result.size(), i + 2); j++) {
                        item.mList.add(result.get(j));
                    }
                    mModelDatas.add(item);
                } else if (i % 5 == 3) {
                    ModelData<VideoInfo> item = new ModelData<>();
                    item.count = 3;
                    for (int j = i; j < Math.min(result.size(), i + 3); j++) {
                        item.mList.add(result.get(j));
                    }
                    mModelDatas.add(item);
                }

            }

            CommonAdapter commAdapter = new CommonAdapter<ModelData<VideoInfo>>(getActivity(), mModelDatas,
                    R.layout.item_cao) {
                @Override
                public void convert(final ViewHolder helper, final ModelData<VideoInfo> item) {
                    CommonAdapter commAdapter_son = new CommonAdapter<VideoInfo>(getActivity(), item.mList,
                            R.layout.item_home_video) {
                        @Override
                        public void convert(final ViewHolder helper, final VideoInfo item) {
                            ImageView iv_item_video = helper.getView(R.id.iv_item_video);
                            GlideLoader.loadRoundImage(BaseConstant.Image_URL + item.getImg(), iv_item_video, R.drawable.sy_bj);
                            helper.setText(R.id.tv_item_video, item.getTitle());
                        }
                    };
                    ((GridViewForNoScroll) helper.getView(R.id.mGridViewForNoScroll)).setNumColumns(item.count);
                    ((GridViewForNoScroll) helper.getView(R.id.mGridViewForNoScroll)).setAdapter(commAdapter_son);
                    ((GridViewForNoScroll) helper.getView(R.id.mGridViewForNoScroll)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), SingleVideoActivity.class);
                            intent.putExtra("url", BaseConstant.Video_URL + item.mList.get(position).getVideo());
                            intent.putExtra("type", JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL);
                            intent.putExtra("name", item.mList.get(position).getTitle());
                            intent.putExtra("id", item.mList.get(position).getId());
                            startActivity(intent);
                        }
                    });
                }
            };


            mMListView.setAdapter(commAdapter);
        }
    }

    private void indexBanner() {
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url = BaseConstant.getApiPostUrl("userGoods/indexBanner");
        OkGo.<DataResult<ArrayList<IndexBannerInfo>>>post(url)
                .params("token", token)
                .execute(new JsonCallback<DataResult<ArrayList<IndexBannerInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<IndexBannerInfo>>> response) {

                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {

                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                IndexBannerList = response.body().getData();
                                ssv_home_pic.setVisibility(View.VISIBLE);
                                IndexBannerList1 = new ArrayList<IndexBannerInfo>();
                                IndexBannerList2 = new ArrayList<IndexBannerInfo>();
                                for (int i = 0; i < IndexBannerList.size(); i++) {
                                    if (IndexBannerList.get(i).getState().equals("1")) {
                                        IndexBannerList1.add(IndexBannerList.get(i));
                                    } else if (IndexBannerList.get(i).getState().equals("2")) {
                                        IndexBannerList2.add(IndexBannerList.get(i));
                                    }
                                }

                                if (IndexBannerList1 != null && IndexBannerList1.size() > 0) {
                                    String[] headUrls = new String[IndexBannerList1.size()];
                                    for (int i = 0; i < IndexBannerList1.size(); i++) {
                                        headUrls[i] = BaseConstant.Image_URL + IndexBannerList1.get(i).getIndex_images();
                                    }
                                    ssv_home_pic.initAndSetImagesUrl(headUrls,
                                            new SlideShowView.OnImageClickListener() {
                                                @Override
                                                public void onClick(View v, int position) {
                                                    String type = IndexBannerList1.get(position).getType();
                                                    String content = IndexBannerList1.get(position).getContent();
                                                    String State = IndexBannerList1.get(position).getCstate();
                                                    gotoJump(type, content, State);

                                                }
                                            });
                                }

                                //拍当头条
                                if (IndexBannerList2 != null && IndexBannerList2.size() > 0) {
                                    pawnmsg();
                                }

                            } else {
                                ssv_home_pic.setVisibility(View.GONE);
                            }

                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<IndexBannerInfo>>> response) {
                        showVolleyError(null);
                    }
                });
    }


    private void getIndexMenu() {
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url = BaseConstant.getApiPostUrl("storeGoods/getIndexMenu");
        getlDialog().show();
        OkGo.<DataResult<ArrayList<IndexMenuInfo>>>post(url)
                .params("token", token)
                .execute(new JsonCallback<DataResult<ArrayList<IndexMenuInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<IndexMenuInfo>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {

                            if (response.body().getData() != null && response.body().getData().size() > 0) {

                                IndexMenuList1 = new ArrayList<>();
                                IndexMenuList2 = new ArrayList<>();
                                IndexMenuList3 = new ArrayList<>();
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    if (response.body().getData().get(i).getState().equals("1")) {//1.认证商场 2.绝当新品 3.热门竞拍
                                        if (IndexMenuList1.size() < 7) {
                                            IndexMenuList1.add(response.body().getData().get(i));
                                        }
                                    } else if (response.body().getData().get(i).getState().equals("2")) {//1.认证商场 2.绝当新品 3.热门竞拍
                                        if (IndexMenuList2.size() < 4) {
                                            IndexMenuList2.add(response.body().getData().get(i));
                                        }
                                    } else if (response.body().getData().get(i).getState().equals("3")) {//1.认证商场 2.绝当新品 3.热门竞拍
                                        if (IndexMenuList3.size() < 4) {
                                            IndexMenuList3.add(response.body().getData().get(i));
                                        }
                                    }
                                }

                                if (IndexMenuList1.size() > 0) {
                                    ll_home_rzsclist.setVisibility(View.VISIBLE);
                                    showList(IndexMenuList1);
                                } else {
                                    ll_home_rzsclist.setVisibility(View.GONE);
                                }
                                if (IndexMenuList2.size() > 0) {
                                    ll_home_jdxplist.setVisibility(View.VISIBLE);
                                    showgridList(IndexMenuList2);
                                } else {
                                    ll_home_jdxplist.setVisibility(View.GONE);
                                }

                            } else {

                            }

                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<IndexMenuInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

    private void userGetCoupon() {
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url = BaseConstant.getApiPostUrl("watchVideo/userGetCoupon");
        OkGo.<DataResult<CouponInfo>>post(url)
                .params("token", token)
                .execute(new JsonCallback<DataResult<CouponInfo>>() {
                    @Override
                    public void onSuccess(Response<DataResult<CouponInfo>> response) {

                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {

                            if (response.body().getData() != null) {
                                couponInfo = response.body().getData();
                                if (couponInfo.getId() != null && !couponInfo.getId().equals("0")) {
                                    showPopupWindow();
                                }

                            }

                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<CouponInfo>> response) {
                        showVolleyError(null);
                    }
                });
    }

    private PopupWindow popupWindo;

    public void showPopupWindow() {
        if (popupWindo != null) {
            popupWindo = null;
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pw_video_yhq, null);
        TextView tv_receive = (TextView) view.findViewById(R.id.tv_receive);
        TextView tv_price_yhq = (TextView) view.findViewById(R.id.tv_price_yhq);
        TextView tv_date_yhq = (TextView) view.findViewById(R.id.tv_date_yhq);
        TextView tv_info_yhq = (TextView) view.findViewById(R.id.tv_info_yhq);
        LinearLayout ll_lq = (LinearLayout) view.findViewById(R.id.ll_lq);
        tv_price_yhq.setText(couponInfo.getValue() + "元");
        if (couponInfo.getEndTime().equals("")) {
            tv_date_yhq.setText("");
        } else {
            tv_date_yhq.setText("有效期至：" + couponInfo.getEndTime());
        }

        tv_info_yhq.setText(couponInfo.getInfo() + "仅售" + couponInfo.getPrice() + "元,快来抢购！");

        tv_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindo.dismiss();
                watchVideo();
            }
        });

        ll_lq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindo.dismiss();
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

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        popupWindo = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindo.setOutsideTouchable(true);
        popupWindo.setFocusable(true);
        fitPopupWindowOverStatusBar(popupWindo);
        popupWindo.setBackgroundDrawable(new ColorDrawable());
        popupWindo.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void watchVideo() {
        getlDialog().show();
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url = BaseConstant.getApiPostUrl("watchVideo/watchVideo");
        OkGo.<DataResult<DataInfo>>post(url)
                .params("token", token)
                .params("id", couponInfo.getId())
                .params("type", "3")////1新用户注册 2看视频 3首页领取
                .execute(new JsonCallback<DataResult<DataInfo>>() {
                    @Override
                    public void onSuccess(Response<DataResult<DataInfo>> response) {

                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            CustomToast.show("领取成功");
                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<DataInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    // 获取视频列表
    private void videoList() {
        getlDialog().show();
        String url = BaseConstant.getApiPostUrl("storeGoods/getVideoForIndex");
        OkGo.<DataResult<ArrayList<VideoInfo>>>post(url)
                .params("key", "")
                .execute(new JsonCallback<DataResult<ArrayList<VideoInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<VideoInfo>>> response) {
                        getlDialog().dismiss();
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {

                            if (response.body().getData() != null || response.body().getData().size() > 0) {
                                VideoList = new ArrayList<VideoInfo>();
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    //if(VideoList.size()<3){
                                    VideoList.add(response.body().getData().get(i));
                                    //}
                                }

                                if (VideoList.size() > 0) {
                                    ll_home_videolist.setVisibility(View.VISIBLE);
                                    showgridVideoList(VideoList);
                                } else {
                                    ll_home_videolist.setVisibility(View.GONE);
                                }
                            }

                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<VideoInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}

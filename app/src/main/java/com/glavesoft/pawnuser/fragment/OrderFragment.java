package com.glavesoft.pawnuser.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.glavesoft.F;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.appraisal.SendCallGoodDetailActivity;
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.activity.personal.GoodsCommentActivity;
import com.glavesoft.pawnuser.activity.personal.LogisticsdetailActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.OrderBuyActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.frg.FrgProductDetail;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.OrderInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.utility.Helper;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 严光
 * @date: 2017/11/15
 * @company:常州宝丰
 */
public class OrderFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private int index;
    private int commentState;
    private RelativeLayout titlebar_refresh;
    private BGARefreshLayout mRefreshLayout;
    private ListView lv_listview;
    private ArrayList<OrderInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private int page=1;
    private int listsize=0;
    private PopupWindow popupWindo;
    private LinearLayout ll_nodata;
    private TextView tv_nodata;
    public static OrderFragment newInstance(int index,int commentState)//
    {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putInt("commentState",commentState);
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
            commentState=getArguments().getInt("commentState");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_listview_goods, container, false);
        setBoardCast();
        initView(view);
        return view;
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("OrderRefresh");
        getActivity().registerReceiver(mListenerID, f);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            resetPageData();
        }
    };

    public  void onDestroy(){
        super.onDestroy();
        getActivity().unregisterReceiver(mListenerID);
    }

    private void initView(View view) {
        titlebar_refresh=(RelativeLayout)view.findViewById(R.id.titlebar_refresh);
        titlebar_refresh.setVisibility(View.GONE);
        ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
        tv_nodata=(TextView) view.findViewById(R.id.tv_nodata);
        mRefreshLayout=(BGARefreshLayout)view.findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.custom_mooc_icon);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.bg_title);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        lv_listview=(ListView)view.findViewById(R.id.lv_listview);

        myStoreGoodsList();
    }

    private void showList(ArrayList<OrderInfo> result) {

        if (commAdapter == null) {
            list = result;
            commAdapter = new CommonAdapter<OrderInfo>(getActivity(), result,
                    R.layout.item_order) {
                @Override
                public void convert(final ViewHolder helper, final OrderInfo item) {
                    ImageView iv_pic_order=(ImageView) helper.getView(R.id.iv_pic_order);
                    if(!item.getImages().equals("")){
                        List<String> list= Arrays.asList(item.getImages().split(","));
                        getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0),iv_pic_order,getImageLoaderOptions());
                    }else{
                        getImageLoader().displayImage("",iv_pic_order,getImageLoaderOptions());
                    }
                    helper.setText(R.id.tv_no_order,"订单号："+item.getOrderCode());
                    helper.setText(R.id.tv_name_order,item.getGoodsName());
                    helper.setText(R.id.tv_price_order,"售价：￥"+item.getPrice());

                    helper.getView(R.id.ll_wl_order).setVisibility(View.INVISIBLE);

                    helper.getView(R.id.tv_ckwl_order).setVisibility(View.GONE);
                    helper.getView(R.id.tv_kf_order).setVisibility(View.GONE);
                    helper.getView(R.id.tv_fk_order).setVisibility(View.GONE);
                    helper.getView(R.id.tv_qrqs_order).setVisibility(View.GONE);
                    helper.getView(R.id.tv_delete_order).setVisibility(View.GONE);
                    helper.getView(R.id.tv_refund_order).setVisibility(View.GONE);
                    helper.getView(R.id.tv_comment_order).setVisibility(View.GONE);
                    helper.getView(R.id.tv_number_order).setVisibility(View.GONE);
                    helper.getView(R.id.tv_reasons_order).setVisibility(View.GONE);
                    //-1已取消1待付款2已付款3已发货4确认收货5已评价6请求退款 7同意退款 8已退款 9拒绝退款
                    if(item.getState().equals("-1")){
                        helper.setText(R.id.tv_state_order,"已取消");
                        helper.getView(R.id.tv_delete_order).setVisibility(View.VISIBLE);
                    }else if(item.getState().equals("1")){
                        helper.setText(R.id.tv_state_order,"待付款");
                        helper.getView(R.id.tv_kf_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_fk_order).setVisibility(View.VISIBLE);
                    }else if(item.getState().equals("2")){
                        helper.setText(R.id.tv_state_order,"待发货");
                        helper.getView(R.id.tv_kf_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.ll_wl_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_refundtype_order).setVisibility(View.GONE);
                        helper.getView(R.id.tv_ckwl_order).setVisibility(View.GONE);
                        helper.getView(R.id.tv_wl_order).setVisibility(View.GONE);
                        helper.getView(R.id.tv_refund_order).setVisibility(View.VISIBLE);

                    }else if(item.getState().equals("3")){
                        helper.setText(R.id.tv_state_order,"待收货");
                        helper.getView(R.id.tv_kf_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_qrqs_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.ll_wl_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_ckwl_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_ckwl_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_wl_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_refundtype_order).setVisibility(View.GONE);
                        //1认证商品 2绝当商品
                        if(item.getGoodsType().equals("1")){
                            helper.getView(R.id.tv_refund_order).setVisibility(View.VISIBLE);
                        }

                        //0：在途1：揽件2：疑难3：签收4：退签或异常签收5：派件6：退回
                        if(item.getExpressState().equals("0")){
                            helper.setText(R.id.tv_wl_order,"在途中");
                        }else if(item.getExpressState().equals("1")){
                            helper.setText(R.id.tv_wl_order,"揽件中");
                        }else if(item.getExpressState().equals("2")){
                            helper.setText(R.id.tv_wl_order,"疑难处理");
                        }else if(item.getExpressState().equals("3")){
                            helper.setText(R.id.tv_wl_order,"待签收");
                        }else if(item.getExpressState().equals("4")){
                            helper.setText(R.id.tv_wl_order,"退签或异常签收");
                        }else if(item.getExpressState().equals("5")){
                            helper.setText(R.id.tv_wl_order,"派件中");
                        }else if(item.getExpressState().equals("6")){
                            helper.setText(R.id.tv_wl_order,"已退回");
                        }
                    }else if(item.getState().equals("4")&&item.getCommentState().equals("1")){
                        helper.setText(R.id.tv_state_order,"已完成");
                        helper.getView(R.id.tv_kf_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_delete_order).setVisibility(View.VISIBLE);
//                        helper.getView(R.id.tv_comment_order).setVisibility(View.VISIBLE);
//                        helper.setText(R.id.tv_state_order,"待评价");
                    }else if(item.getState().equals("5")){
                        helper.setText(R.id.tv_state_order,"退款");
                        helper.getView(R.id.tv_kf_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.ll_wl_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_refundtype_order).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_ckwl_order).setVisibility(View.GONE);
                        helper.getView(R.id.tv_wl_order).setVisibility(View.GONE);
                        helper.getView(R.id.tv_refund_order).setVisibility(View.GONE);

                        //退款状态 0未退款 1申请退款 2同意退款 3已填写单号 4已退款 5拒绝退款
                        if(item.getRefState().equals("1")){
                            helper.setText(R.id.tv_refundtype_order,"退款申请中");
                        }else if(item.getRefState().equals("2")){
                            helper.setText(R.id.tv_refundtype_order,"同意退款");
                            helper.getView(R.id.tv_number_order).setVisibility(View.VISIBLE);
                        }else if(item.getRefState().equals("3")){
                            helper.setText(R.id.tv_refundtype_order,"退款处理中");
                        }else if(item.getRefState().equals("4")){
                            helper.setText(R.id.tv_refundtype_order,"已退款");
                        }else if(item.getRefState().equals("5")){
                            helper.setText(R.id.tv_refundtype_order,"已被拒绝");
                            helper.getView(R.id.tv_reasons_order).setVisibility(View.VISIBLE);
                        }

                    }else if(item.getState().equals("4")&&item.getCommentState().equals("0")){
                        helper.getView(R.id.tv_comment_order).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_state_order,"待评价");
                    }

                    //拒绝原因
                    helper.getView(R.id.tv_reasons_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupWindow(item.getRefundNotVerifyReason());
                        }
                    });

                    //填写单号
                    helper.getView(R.id.tv_number_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupWindow("refund_number",item.getId());
                        }
                    });

                    //退款
                    helper.getView(R.id.tv_refund_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupWindow("refund_reason",item.getId());
                        }
                    });
                    //评价
                    helper.getView(R.id.tv_comment_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getActivity(), GoodsCommentActivity.class);
                            intent.putExtra("goodsId",item.getGoodsId());
                            intent.putExtra("orderId",item.getId());
                            intent.putExtra("img",item.getImages());
                            startActivity(intent);
                        }
                    });
                    //查看物流
                    helper.getView(R.id.tv_ckwl_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), LogisticsdetailActivity.class);
                            intent.putExtra("expressId",item.getExpressId());
                            startActivity(intent);
                        }
                    });
                    //客服
                    helper.getView(R.id.tv_kf_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotokf(item);
                        }
                    });

                    //付款
                    helper.getView(R.id.tv_fk_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), OrderBuyActivity.class);
                            intent.putExtra("id",item.getId());
                            intent.putExtra("goodtype",item.getGoodsType());
                            startActivity(intent);
                        }
                    });

                    //确认签收
                    helper.getView(R.id.tv_qrqs_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMyGoods(item.getId());
                        }
                    });

                    //删除订单
                    helper.getView(R.id.tv_delete_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delMyStoreGoods(item.getId());
                        }
                    });

                }
            };

            lv_listview.setAdapter(commAdapter);
        }else {
            if (list == null || list.size() == 0) {
                list = result;
            } else {
                for (int i = 0; i < result.size(); i++) {
                    list.add(result.get(i));
                }
            }
            commAdapter.onDateChange(list);
        }
        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.get(i).getIsSell().equals("1")){//寄拍
                    Intent intent = new Intent(OrderFragment.this.getActivity(), SendCallGoodDetailActivity.class);
                    intent.putExtra("id",list.get(i).getUserGoodsId());
//                    intent.putExtra("type","rz");
                    startActivity(intent);
                }else {
//                    Intent intent = new Intent(OrderFragment.this.getActivity(), GoodsDetailActivity.class);
//                    intent.putExtra("id",list.get(i).getGoodsId());
//                    intent.putExtra("type","rz");
//                    startActivity(intent);

                    F.INSTANCE.go2GoodeDetail(getActivity(),    list.get(i).getGoodsId(), "rz");
                }

            }
        });

    }

    private void gotokf(OrderInfo item){
        Information info = new Information();
        info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
        //用户资料
        info.setUid(LocalData.getInstance().getUserInfo().getId());
        info.setUname(LocalData.getInstance().getUserInfo().getId());
        info.setTel(LocalData.getInstance().getUserInfo().getAccount());
        info.setFace(BaseConstant.Image_URL+LocalData.getInstance().getUserInfo().getHeadImg());
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(4);

        //转接类型(0-可转入其他客服，1-必须转入指定客服)
        info.setTranReceptionistFlag(1);
        //指定客服id
        info.setReceptionistId("71764f63c3ca497ba974f938b26389eb");
        if(item!=null){
            //咨询内容
            ConsultingContent consultingContent = new ConsultingContent();
            //咨询内容标题，必填
            consultingContent.setSobotGoodsTitle(item.getGoodsName());
            //咨询内容图片，选填 但必须是图片地址
            if(!item.getImages().equals("")){
                List<String> list= Arrays.asList(item.getImages().split(","));
                consultingContent.setSobotGoodsImgUrl(BaseConstant.Image_URL+list.get(0));
            }
            //咨询来源页，必填
            String url=BaseConstant.BaseURL+"/m/pawn/H5GetMyGoods?id="+item.getGoodsId()+"&user_id="+LocalData.getInstance().getUserInfo().getId();
            consultingContent.setSobotGoodsFromUrl(url);
            //描述，选填
            //consultingContent.setSobotGoodsDescribe(item.getGoodsDescription());
            //标签，选填
            //consultingContent.setSobotGoodsLable("还款金额：￥"+item.getAllMoney());
            //可以设置为null
            info.setConsultingContent(consultingContent);
        }
        //设置聊天界面标题显示模式
        SobotApi.setChatTitleDisplayMode(getActivity(), SobotChatTitleDisplayMode.Default,"");

        SobotApi.startSobotChat(getActivity(), info);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        resetPageData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(listsize==10){
            page++;
            myStoreGoodsList();
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
        myStoreGoodsList();
    }

    private void myStoreGoodsList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/myStoreGoodsList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("page",page+"");
        param.put("limit","10");
        param.put("orderState",index+"");// 0全部 1待付款 3待收货 4完成(确认收货) 5退款 6待评价
        param.put("commentState",commentState+"");//评价状态0未评价，1已经评价  配合
        OkGo.<DataResult<ArrayList<OrderInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<OrderInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<OrderInfo>>> response) {
                        getlDialog().dismiss();
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                ll_nodata.setVisibility(View.GONE);
                                mRefreshLayout.setVisibility(View.VISIBLE);
                                listsize=response.body().getData().size();
                                showList(response.body().getData());
                            }else{
                                if (list.size()==0){
                                    ll_nodata.setVisibility(View.VISIBLE);
                                    mRefreshLayout.setVisibility(View.GONE);
                                    tv_nodata.setText("暂无订单信息，快去别的地方逛逛哦~");
                                }
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<OrderInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                        mRefreshLayout.endRefreshing();
                        mRefreshLayout.endLoadingMore();
                    }
                });
    }

    //删除订单
    private void delMyStoreGoods(String id)
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/delMyStoreGoods");
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
                            Intent intent=new Intent("OrderRefresh");
                            getActivity().sendBroadcast(intent);
                            CustomToast.show("删除成功");
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

    //签收商城商品
    private void getMyGoods(String id)
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/getMyGoods");
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
                            Intent intent=new Intent("OrderRefresh");
                            getActivity().sendBroadcast(intent);
                            CustomToast.show("签收成功");
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

    public void showPopupWindow(final String type, final String id)
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pw_refund, null);
        final TextView tv_title_refund= (TextView)view.findViewById(R.id.tv_title_refund);
        Button btn_ok = (Button)view.findViewById(R.id.btn_ok);
        ImageView iv_close_refund = (ImageView)view.findViewById(R.id.iv_close_refund);
        final EditText et_reason_refund = (EditText)view.findViewById(R.id.et_reason_refund);
        TextView tv_ts_refund= (TextView)view.findViewById(R.id.tv_ts_refund);

        if(type.equals("refund_reason")){
            tv_title_refund.setText("退款原因：");
            et_reason_refund.setHint("请输入退款原因");
            tv_ts_refund.setVisibility(View.VISIBLE);
        }else{
            tv_title_refund.setText("填写单号：");
            et_reason_refund.setHint("请输入单号");
            tv_ts_refund.setVisibility(View.GONE);
        }

        iv_close_refund.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(type.equals("refund_reason")){
                    if(et_reason_refund.getText().toString().trim().length()==0){
                        CustomToast.show("请输入退款原因");
                    }else{
                        popupWindo.dismiss();
                        refundOrder(id,et_reason_refund.getText().toString().trim());
                    }
                }else{
                    if(et_reason_refund.getText().toString().trim().length()==0){
                        CustomToast.show("请输入单号");
                    }else{
                        popupWindo.dismiss();
                        refundOrderSendCode(id,et_reason_refund.getText().toString().trim());
                    }
                }

            }
        });
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        popupWindo = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindo.setOutsideTouchable(true);
        popupWindo.setFocusable(true);
        popupWindo.setBackgroundDrawable(new ColorDrawable());
        popupWindo.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    //申请退款
    private void refundOrder(String id,String reason)
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/refundOrder");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("orderId",id);
        param.put("refundReason",reason);
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
                            Intent intent=new Intent("OrderRefresh");
                            getActivity().sendBroadcast(intent);
                            CustomToast.show("请等待，客服正在处理中。");
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

    //申请成功后填写物流单号
    private void refundOrderSendCode(String id,String expressCode)
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/refundOrderSendCode");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("orderId",id);
        param.put("expressCode",expressCode);
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
                            Intent intent=new Intent("OrderRefresh");
                            getActivity().sendBroadcast(intent);
                            CustomToast.show("请等待，客服正在处理中。");
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

    public void showPopupWindow(String reason)
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pw_dialog2, null);

        TextView tv_ts_dialog = (TextView)view.findViewById(R.id.tv_ts_dialog);
        TextView tv_content_dialog = (TextView)view.findViewById(R.id.tv_content_dialog);
        Button btn_ok = (Button)view.findViewById(R.id.btn_ok);

        tv_ts_dialog.setText("拒绝原因");
        tv_content_dialog.setText(reason);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
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
}

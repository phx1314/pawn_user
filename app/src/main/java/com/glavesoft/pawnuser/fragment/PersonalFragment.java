package com.glavesoft.pawnuser.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.glavesoft.F;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.activity.pawn.MonitorActivity;
import com.glavesoft.pawnuser.activity.pawn.MyPawnActivity;
import com.glavesoft.pawnuser.activity.pawn.PawnRecordActivity;
import com.glavesoft.pawnuser.activity.personal.BankCardActivity;
import com.glavesoft.pawnuser.activity.personal.BindIDcardActivity;
import com.glavesoft.pawnuser.activity.personal.ContractActivity;
import com.glavesoft.pawnuser.activity.personal.CouponActivity;
import com.glavesoft.pawnuser.activity.personal.EwmActivity;
import com.glavesoft.pawnuser.activity.personal.LogisticsActivity;
import com.glavesoft.pawnuser.activity.personal.MyinfoActivity;
import com.glavesoft.pawnuser.activity.personal.SettingActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.OrderActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.frg.FrgPjzx;
import com.glavesoft.pawnuser.frg.FrgRenzheng;
import com.glavesoft.pawnuser.frg.FrgShangjiaduan;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ItemInfo;
import com.glavesoft.pawnuser.mod.LawInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.view.RoundImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.service.subscriber.S;
import com.mdx.framework.utility.Helper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import static com.glavesoft.pawnuser.constant.BaseConstant.URL;
import static com.mdx.framework.service.ServiceFactoryKt.gB;

/**
 * @author 严光
 * @date: 2017/10/19
 * @company:常州宝丰
 */
//18262963098 15151963763
public class PersonalFragment extends BaseFragment implements View.OnClickListener {

    public ImageView mImageView_set;
    public TextView tv_wdyhq;
    public TextView tv_htjl;
    public LinearLayout mLinearLayout_1;
    public LinearLayout mLinearLayout_2;
    public LinearLayout mLinearLayout_3;
    public LinearLayout mLinearLayout_4;
    public LinearLayout mLinearLayout_5;
    public LinearLayout mLinearLayout_6;
    public LinearLayout mLinearLayout_7;
    public LinearLayout mLinearLayout_8;
    public LinearLayout mLinearLayout_9;
    public LinearLayout mLinearLayout_10;
    private LinearLayout ll_myinfo;
    //    private ImageView iv_personal_back,iv_personal_set;
    private TextView tv_my_version;
    private LinearLayout ll_wdyhq;
    private LinearLayout ll_htjl;
    private RoundImageView my_photo;
    private TextView tv_my_name, tv_my_phone;


//    int[] img = new int[]{R.drawable.wdyw, R.drawable.scdd, R.drawable.wlxx, R.drawable.pjzx, R.drawable.ddjl, R.drawable.ddjk, R.drawable.wdyhk, R.drawable.rlyz, R.drawable.wdkf };
//    String[] title = new String[]{"业务中", "商城订单", "物流信息", "票据中心", "业务记录", "业务监控", "我的银行卡", "人脸认证", "客服" };
//    int[] img = new int[]{R.drawable.wdyw, R.drawable.scdd, R.drawable.wlxx, R.drawable.pjzx, R.drawable.ddjl, R.drawable.ddjk, R.drawable.wdyhk, R.drawable.rlyz, R.drawable.wdkf, R.drawable.sjd};
//    String[] title = new String[]{"业务中", "商城订单", "物流信息", "票据中心", "业务记录", "业务监控", "我的银行卡", "人脸认证", "客服", "商家端"};

    public static PersonalFragment newInstance(int index) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    private int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt("index");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_n, container, false);
        initView(view);
        lawList();
        return view;
    }

    private void initView(View view) {
        ll_myinfo = (LinearLayout) view.findViewById(R.id.ll_myinfo);
        mLinearLayout_1 = (LinearLayout) view.findViewById(R.id.mLinearLayout_1);
        mImageView_set = (ImageView) view.findViewById(R.id.mImageView_set);
        mLinearLayout_2 = (LinearLayout) view.findViewById(R.id.mLinearLayout_2);
        mLinearLayout_3 = (LinearLayout) view.findViewById(R.id.mLinearLayout_3);
        mLinearLayout_4 = (LinearLayout) view.findViewById(R.id.mLinearLayout_4);
        mLinearLayout_5 = (LinearLayout) view.findViewById(R.id.mLinearLayout_5);
        mLinearLayout_6 = (LinearLayout) view.findViewById(R.id.mLinearLayout_6);
        mLinearLayout_7 = (LinearLayout) view.findViewById(R.id.mLinearLayout_7);
        mLinearLayout_8 = (LinearLayout) view.findViewById(R.id.mLinearLayout_8);
        mLinearLayout_9 = (LinearLayout) view.findViewById(R.id.mLinearLayout_9);
        mLinearLayout_10 = (LinearLayout) view.findViewById(R.id.mLinearLayout_10);

        my_photo = (RoundImageView) view.findViewById(R.id.my_photo);
        RandomTx();

        tv_my_name = (TextView) view.findViewById(R.id.tv_my_name);
        tv_my_phone = (TextView) view.findViewById(R.id.tv_my_phone);

        ll_wdyhq = (LinearLayout) view.findViewById(R.id.ll_wdyhq);


        ll_htjl = (LinearLayout) view.findViewById(R.id.ll_htjl);
        tv_my_version = (TextView) view.findViewById(R.id.tv_my_version);

        ll_myinfo.setOnClickListener(this);
        mImageView_set.setOnClickListener(this);
        mLinearLayout_1.setOnClickListener(this);
        mLinearLayout_2.setOnClickListener(this);
        mLinearLayout_3.setOnClickListener(this);
        mLinearLayout_4.setOnClickListener(this);
        mLinearLayout_5.setOnClickListener(this);
        mLinearLayout_6.setOnClickListener(this);
        mLinearLayout_7.setOnClickListener(this);
        mLinearLayout_8.setOnClickListener(this);
        mLinearLayout_9.setOnClickListener(this);
        mLinearLayout_10.setOnClickListener(this);

        ll_wdyhq.setOnClickListener(this);
        ll_htjl.setOnClickListener(this);

        tv_my_version.setText("版本号 V" + AppUtils.getAppVersionName());

    }

    @Override
    public void onSuccess(@org.jetbrains.annotations.Nullable String data, @NotNull String method) {
        if (method.equals("passOrNot")) {
            F.INSTANCE.toast("认证审核中");

        }
    }

    @Override
    public void onError(@org.jetbrains.annotations.Nullable String code, @org.jetbrains.annotations.Nullable String msg, @org.jetbrains.annotations.Nullable String data, @NotNull String mehotd) {
        if (code.equals("1")) {
            Helper.startActivity(getContext(), FrgShangjiaduan.class, TitleAct.class);
        } else if (code.equals("2")) {
            Helper.startActivity(getContext(), FrgRenzheng.class, TitleAct.class);
        } else if (Integer.valueOf(code) == DataResult.RESULT_102) {
            toLogin();
        } else {
            Helper.startActivity(getContext(), FrgRenzheng.class, TitleAct.class);
        }
    }

    public void onResume() {
        super.onResume();
        if (BaseConstant.isLogin()) {
            if (!LocalData.getInstance().getUserInfo().getHeadImg().equals("")) {
                String imageurl = BaseConstant.Image_URL + LocalData.getInstance().getUserInfo().getHeadImg();
                getImageLoader().displayImage(imageurl, my_photo, getImageLoaderHeadOptions());
            } else {
//                getImageLoader().displayImage("",my_photo,getImageLoaderHeadOptions());
                RandomTx();
            }
            tv_my_name.setText(LocalData.getInstance().getUserInfo().getNickName());
            tv_my_phone.setVisibility(View.VISIBLE);
            tv_my_phone.setText(LocalData.getInstance().getUserInfo().getAccount());
        } else {
            RandomTx();
            tv_my_name.setText("未登录");
            tv_my_phone.setVisibility(View.GONE);
        }

    }

    private void RandomTx() {
        Random rand = new Random();
        int randNum = rand.nextInt(4);
        if (randNum == 0) {
            my_photo.setImageResource(R.mipmap.mryi);
        } else if (randNum == 1) {
            my_photo.setImageResource(R.mipmap.mrer);
        } else if (randNum == 2) {
            my_photo.setImageResource(R.mipmap.mrsan);
        } else if (randNum == 3) {
            my_photo.setImageResource(R.mipmap.mrsi);
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.mImageView_set://设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.ll_myinfo://个人资料
                if (BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), MyinfoActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_wdyhq://我的优惠券
                if (BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), CouponActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
//            case R.id.tv_wdjdjp://我的绝当竞拍
//                startActivity(new Intent(PersonalActivity.this, MyAuctionActivity.class));
//                break;
            case R.id.ll_htjl://合同记录
                if (BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), ContractActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.mLinearLayout_1:
                if (!BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }

                startActivity(new Intent(getActivity(), MyPawnActivity.class));
                break;
            case R.id.mLinearLayout_2:
                if (!BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), OrderActivity.class));
                break;
            case R.id.mLinearLayout_3:
                if (!BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), LogisticsActivity.class));
                break;
            case R.id.mLinearLayout_4:
                if (!BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                Helper.startActivity(getContext(), FrgPjzx.class, TitleAct.class);
                break;
            case R.id.mLinearLayout_5:
                if (!BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), PawnRecordActivity.class));
                break;
            case R.id.mLinearLayout_6:
                if (!BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MonitorActivity.class));
                break;
            case R.id.mLinearLayout_7:
                if (!BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), BankCardActivity.class));
                break;
            case R.id.mLinearLayout_8:
                if (!BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                if (LocalData.getInstance().getUserInfo().getIsBind().equals("1")) {//已绑定
                    CustomToast.show("您已绑定");
                } else {
                    startActivity(new Intent(getActivity(), BindIDcardActivity.class));
                }
            case R.id.mLinearLayout_9:
                if (!BaseConstant.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                gotokf_J(getActivity());
                break;
            case R.id.mLinearLayout_10:
                load(F.INSTANCE.gB(50).passOrNot(LocalData.getInstance().getUserInfo().getToken()), "passOrNot", true, "加载中", new S(PersonalFragment.this, new ProgressDialog(getContext()), "passOrNot", true));
                break;
        }
    }

    private void showgridList(final ArrayList<ItemInfo> result) {
        CommonAdapter commAdapter = new CommonAdapter<ItemInfo>(getActivity(), result,
                R.layout.item_person) {
            @Override
            public void convert(final ViewHolder helper, final ItemInfo item) {
                helper.setText(R.id.tv_title_menu, item.getTitle());
                helper.getView(R.id.iv_pic_menu).setBackgroundResource(item.getImg());
            }
        };

    }

    private void lawList() {
        String token = LocalData.getInstance().getUserInfo().getToken();
        String url = BaseConstant.getApiPostUrl("home/lawList");
        HttpParams param = new HttpParams();
        param.put("token", token);
        OkGo.<DataResult<ArrayList<LawInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<LawInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<LawInfo>>> response) {
                        if (response == null) {
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if (response.body().getErrorCode() == DataResult.RESULT_OK_ZERO) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                if (response.body().getData() != null && response.body().getData().size() > 0) {
                                    for (int i = 0; i < response.body().getData().size(); i++) {
                                        if (response.body().getData().get(i).getCode().equals("ddglbf@law")) {
                                            PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, "ddglbf", response.body().getData().get(i).getValue());
                                        } else if (response.body().getData().get(i).getCode().equals("htf@law")) {
                                            PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, "htf", response.body().getData().get(i).getValue());
                                        } else if (response.body().getData().get(i).getCode().equals("mfzz@law")) {
                                            PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, "mfzz", response.body().getData().get(i).getValue());
                                        }
                                    }

                                }
                            }
                        } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                            toLogin();
                        } else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<LawInfo>>> response) {
                        showVolleyError(null);
                    }
                });
    }


}

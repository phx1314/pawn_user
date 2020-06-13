package com.glavesoft.pawnuser.fragment;

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
import com.mdx.framework.utility.Helper;

import java.util.ArrayList;
import java.util.Random;

import static com.glavesoft.pawnuser.constant.BaseConstant.URL;

/**
 * @author 严光
 * @date: 2017/10/19
 * @company:常州宝丰
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout ll_myinfo;
    //    private ImageView iv_personal_back,iv_personal_set;
    private TextView tv_my_version;
    private LinearLayout ll_wdyhq;
    private LinearLayout ll_htjl;
    private RoundImageView my_photo;
    private TextView tv_my_name, tv_my_phone;

    private ImageView titlebar_kf;
    private ImageView iv_en;
    private LinearLayout ll_en;

    private GridViewForNoScroll nsgv_home_jdxp;
    //    int[] img = new int[]{R.drawable.wdyw,R.drawable.scdd,R.drawable.wlxx,R.drawable.ddjl,R.drawable.ddjk,R.drawable.wdyhk,R.drawable.rlyz,R.drawable.wdkf,R.drawable.tgm};
//    String[] title = new String[]{"典当业务","商城订单","物流信息","典当记录","典当监控","我的银行卡","人脸认证","客服","推广二维码"};
    int[] img = new int[]{R.drawable.wdyw, R.drawable.scdd, R.drawable.wlxx, R.drawable.ddjl, R.drawable.ddjk, R.drawable.wdyhk, R.drawable.rlyz, R.drawable.wdkf, R.drawable.sjd};
    String[] title = new String[]{"业务中", "商城订单", "物流信息", "业务记录", "业务监控", "我的银行卡", "人脸认证", "客服", "商家端"};
    ArrayList<ItemInfo> list = new ArrayList<>();

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
        View view = inflater.inflate(R.layout.activity_personal, container, false);
        initView(view);
        lawList();
        return view;
    }

    private void initView(View view) {
        ll_myinfo = (LinearLayout) view.findViewById(R.id.ll_myinfo);
//        iv_personal_back= (ImageView) view.findViewById(R.id.iv_personal_back);
//        iv_personal_set= (ImageView) view.findViewById(R.id.iv_personal_set);

        titlebar_name = (TextView) view.findViewById(R.id.titlebar_name);
        iv_en = (ImageView) view.findViewById(R.id.iv_en);
        titlebar_kf = (ImageView) view.findViewById(R.id.titlebar_kf);
        ll_en = (LinearLayout) view.findViewById(R.id.ll_en);

        titlebar_name.setVisibility(View.VISIBLE);
        titlebar_name.setText("个人中心");
        iv_en.setVisibility(View.VISIBLE);
        iv_en.setImageResource(R.mipmap.personal_center);
        ll_en.setVisibility(View.VISIBLE);
        titlebar_kf.setVisibility(View.VISIBLE);
        titlebar_kf.setImageResource(R.drawable.sz);

        my_photo = (RoundImageView) view.findViewById(R.id.my_photo);
        RandomTx();

        tv_my_name = (TextView) view.findViewById(R.id.tv_my_name);
        tv_my_phone = (TextView) view.findViewById(R.id.tv_my_phone);

        ll_wdyhq = (LinearLayout) view.findViewById(R.id.ll_wdyhq);

        nsgv_home_jdxp = (GridViewForNoScroll) view.findViewById(R.id.nsgv_home_jdxp);

        ll_htjl = (LinearLayout) view.findViewById(R.id.ll_htjl);
        tv_my_version = (TextView) view.findViewById(R.id.tv_my_version);

        ll_myinfo.setOnClickListener(this);
        titlebar_kf.setOnClickListener(this);

        ll_wdyhq.setOnClickListener(this);
        ll_htjl.setOnClickListener(this);

        tv_my_version.setText("版本号 V" + AppUtils.getAppVersionName());
//        try {
//            PackageManager pm = getActivity().getPackageManager();
//            PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(), 0);
//            tv_my_version.setText("版本号 V" + pi.versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            //tv_my_version.setText("版本号 V1.0");
//            e.printStackTrace();
//        }
        for (int i = 0; i < img.length; i++) {
            ItemInfo info = new ItemInfo();
            info.setTitle(title[i]);
            info.setImg(img[i]);
            list.add(info);
        }
        showgridList(list);

        nsgv_home_jdxp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position != 7 && position != 8) {
                    if (!BaseConstant.isLogin()) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                }

                if (position == 0) {//我的业务
                    startActivity(new Intent(getActivity(), MyPawnActivity.class));
                } else if (position == 1) {//商城订单
                    startActivity(new Intent(getActivity(), OrderActivity.class));
                } else if (position == 2) {//我的物流
                    startActivity(new Intent(getActivity(), LogisticsActivity.class));
                } else if (position == 3) {//典当记录
                    startActivity(new Intent(getActivity(), PawnRecordActivity.class));
                } else if (position == 4) {//典当监控
                    startActivity(new Intent(getActivity(), MonitorActivity.class));
                } else if (position == 5) {//我的银行卡
                    startActivity(new Intent(getActivity(), BankCardActivity.class));
                } else if (position == 6) {//人脸识别
                    if (LocalData.getInstance().getUserInfo().getIsBind().equals("1")) {//已绑定
                        CustomToast.show("您已绑定");
                    } else {
                        startActivity(new Intent(getActivity(), BindIDcardActivity.class));
                    }
                } else if (position == 7) {//客服
                    gotokf_J(getActivity());
                } else if (position == 8) {
                    HttpParams param = new HttpParams();
                    param.put("token", LocalData.getInstance().getUserInfo().getToken());//8d223eae-eeda-4a59-a038-4b0478363df9
                    OkGo.<DataResult<ArrayList<LawInfo>>>get(URL + "auth/passOrNot")
                            .params(param)
                            .execute(new JsonCallback<DataResult<ArrayList<LawInfo>>>() {
                                @Override
                                public void onSuccess(Response<DataResult<ArrayList<LawInfo>>> response) {
                                    if (response == null) {
                                        CustomToast.show(getString(R.string.http_request_fail));
                                        return;
                                    }
                                    if (response.body().getErrorCode() == 1) {
                                        Helper.startActivity(getContext(), FrgShangjiaduan.class, TitleAct.class);
                                    } else if (response.body().getErrorCode() == 0) {
                                        F.INSTANCE.toast("认证审核中");
                                    } else if (response.body().getErrorCode() == 2) {
                                        Helper.startActivity(getContext(), FrgRenzheng.class, TitleAct.class);
                                    } else if (response.body().getErrorCode() == DataResult.RESULT_102) {
                                        toLogin();
                                    } else {
                                        Helper.startActivity(getContext(), FrgRenzheng.class, TitleAct.class);
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
        });
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
            case R.id.titlebar_kf://设置
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

        nsgv_home_jdxp.setAdapter(commAdapter);
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

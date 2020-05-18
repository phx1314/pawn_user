package com.glavesoft.pawnuser.activity.main;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.StoreDetailInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreDetailActivity extends BaseActivity {

    @BindView(R.id.iv_store_icon)
    ImageView ivStoreIcon;
    @BindView(R.id.tv_storename)
    TextView tvStorename;
    @BindView(R.id.tv_store_commentlevel)
    TextView tvStoreCommentlevel;
    @BindView(R.id.id_stickynavlayout_topview)
    RelativeLayout idStickynavlayoutTopview;
    @BindView(R.id.tv_store_rate)
    TextView tvStoreRate;
    @BindView(R.id.tv_store_createtime)
    TextView tvStoreCreatetime;
    @BindView(R.id.tv_store_memo)
    TextView tvStoreMemo;
    @BindView(R.id.tv_store_service)
    TextView tvStoreService;
    @BindView(R.id.tv_store_wuliu)
    TextView tvStoreWuliu;
    @BindView(R.id.tv_store_phone)
    TextView tvStorePhone;
    @BindView(R.id.tv_store_zizhi)
    TextView tvStoreZizhi;
    private StoreDetailInfo storeDetailInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        storeDetailInfo=(StoreDetailInfo) this.getIntent().getSerializableExtra("info");
        if (storeDetailInfo!=null){
            tvStorename.setText(storeDetailInfo.getName());
            if (storeDetailInfo.getType().equals("1")){
                ivStoreIcon.setImageResource(R.drawable.ddh_icon);
            }else {
                ivStoreIcon.setImageResource(R.drawable.storeicon);
            }
            if (!storeDetailInfo.getOrgLogo().equals("")) {
                DisplayImageOptions imageOptions = DisplayImageOptions
                        .createSimple();
                ImageLoader.getInstance().displayImage(BaseConstant.Image_URL
                                + storeDetailInfo.getOrgLogo(), ivStoreIcon,
                        imageOptions);
            }
            tvStoreCreatetime.setText(storeDetailInfo.getCreateTime());
            tvStorePhone.setText(storeDetailInfo.getPhone());
        }

    }

    private void initView() {
        setTitleBack();
        setTitleName("店铺简介");
        setTitleNameEn(R.mipmap.store_detais);

    }
}

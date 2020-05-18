package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;

/**
 * @author 严光
 * @date: 2017/11/17
 * @company:常州宝丰
 */
public class VoucherActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_voucher;
    private String ticket;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        ticket=getIntent().getStringExtra("ticket");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("打款凭证");
        iv_voucher=(ImageView)findViewById(R.id.iv_voucher);
        iv_voucher.setOnClickListener(this);
        getImageLoader().displayImage(BaseConstant.Image_URL+ticket,iv_voucher,getImageLoaderOptions());

    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.iv_voucher:

                break;
        }
    }
}

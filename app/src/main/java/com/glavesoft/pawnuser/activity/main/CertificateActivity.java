package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.appraisal.EvaluationActivity;
import com.glavesoft.pawnuser.base.BaseActivity;

/**
 * @author 严光
 * @date: 2017/11/20
 * @company:常州宝丰
 */
public class CertificateActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_number_certificate,ll_gj_certificate;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("认证证书查询");
        setTitleNameEn(R.mipmap.certificate_query);

        ll_number_certificate = (LinearLayout) findViewById(R.id.ll_number_certificate);
        ll_gj_certificate = (LinearLayout) findViewById(R.id.ll_gj_certificate);

        ll_number_certificate.setOnClickListener(this);
        ll_gj_certificate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent();
        switch (v.getId())
        {
            case R.id.ll_number_certificate:
                intent.setClass(CertificateActivity.this, SearchCertificateActivity.class);
                intent.putExtra("type","number");
                startActivity(intent);
                break;
            case R.id.ll_gj_certificate:
                intent.setClass(CertificateActivity.this, SearchCertificateActivity.class);
                intent.putExtra("type","gj");
                startActivity(intent);
                break;
        }
    }
}

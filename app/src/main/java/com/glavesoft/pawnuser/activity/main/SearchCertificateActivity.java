package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.view.CustomToast;


/**
 * @author 严光
 * @date: 2017/12/5
 * @company:常州宝丰
 */
public class SearchCertificateActivity extends BaseActivity implements View.OnClickListener{
    private String type;
    private LinearLayout ll_submit_search;
    private LinearLayout ll_num_collection,ll_senior;

    private EditText et_num_collection;

    private EditText et_name_collection,et_length_collection,et_width_collection,et_height_collection
            ,et_weight_collection,et_cz_collection, et_ztcz_collection,et_qtfc_collection,
            et_time_collection,et_content_collection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcertificate);
        type=getIntent().getStringExtra("type");
        initView();
    }


    private void initView() {
        setTitleBack();

        ll_num_collection=getViewById(R.id.ll_num_collection);
        ll_senior=getViewById(R.id.ll_senior);

        et_num_collection=getViewById(R.id.et_num_collection);

        et_name_collection=getViewById(R.id.et_name_collection);
        et_length_collection=getViewById(R.id.et_length_collection);
        et_width_collection=getViewById(R.id.et_width_collection);
        et_height_collection=getViewById(R.id.et_height_collection);
        et_weight_collection=getViewById(R.id.et_weight_collection);
        et_cz_collection=getViewById(R.id.et_cz_collection);
        et_ztcz_collection=getViewById(R.id.et_ztcz_collection);
        et_qtfc_collection=getViewById(R.id.et_qtfc_collection);
        et_time_collection=getViewById(R.id.et_time_collection);
        et_content_collection=getViewById(R.id.et_content_collection);

        ll_submit_search=getViewById(R.id.ll_submit_search);
        ll_submit_search.setOnClickListener(this);

        if(type.equals("number")){
            setTitleName("编号查询");
            setTitleNameEn(R.mipmap.numbering_query);
            ll_num_collection.setVisibility(View.VISIBLE);
            ll_senior.setVisibility(View.GONE);
        }else{
            setTitleName("高级搜索");
            setTitleNameEn(R.mipmap.numbering_advanced_search);
            ll_num_collection.setVisibility(View.GONE);
            ll_senior.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent();
        switch (v.getId())
        {
            case R.id.ll_submit_search:
                if(type.equals("number")){
                    if(et_num_collection.getText().toString().trim().length()==0){
                        CustomToast.show("请输入藏品编号");
                        return;
                    }

                    intent.setClass(SearchCertificateActivity.this, CertificateListActivity.class);
                    intent.putExtra("type","number");
                    intent.putExtra("number",et_num_collection.getText().toString().trim());
                    startActivity(intent);
                }else{
                    if(et_name_collection.getText().toString().trim().length()==0){
                        CustomToast.show("请输入藏品名称");
                        return;
                    }

                    if(et_cz_collection.getText().toString().trim().length()==0){
                        CustomToast.show("请输入藏品材质");
                        return;
                    }

                    intent.setClass(SearchCertificateActivity.this, CertificateListActivity.class);
                    intent.putExtra("type","gj");
                    intent.putExtra("name",et_name_collection.getText().toString().trim());
                    intent.putExtra("length",et_length_collection.getText().toString().trim());
                    intent.putExtra("width",et_width_collection.getText().toString().trim());
                    intent.putExtra("height",et_height_collection.getText().toString().trim());
                    intent.putExtra("weight",et_weight_collection.getText().toString().trim());
                    intent.putExtra("cz",et_cz_collection.getText().toString().trim());
                    intent.putExtra("ztcz",et_ztcz_collection.getText().toString().trim());
                    intent.putExtra("qtfc",et_qtfc_collection.getText().toString().trim());
                    intent.putExtra("time",et_time_collection.getText().toString().trim());
                    intent.putExtra("content",et_content_collection.getText().toString().trim());
                    startActivity(intent);
                }
                break;
        }
    }
}

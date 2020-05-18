package com.glavesoft.pawnuser.activity.pawn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.ImagePageActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.OrgPawnDetailInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/11/30
 * @company:常州宝丰
 */
public class OrgandetailActivity extends BaseActivity {

    private TextView tv_name_organdetail,tv_dealAmount_organdetail,tv_registeredCapital_organdetail
            ,tv_lagalPerson_organdetail,tv_address_organdetail,tv_introduction_organdetail;
    private GridViewForNoScroll gv_pics_organdetail;
    private ArrayList<String> picurlList=new ArrayList<>();
    private OrgPawnDetailInfo orgPawnDetailInfo;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organdetail);
        id=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("典当行详情");
        tv_name_organdetail=getViewById(R.id.tv_name_organdetail);
        tv_dealAmount_organdetail=getViewById(R.id.tv_dealAmount_organdetail);
        tv_registeredCapital_organdetail=getViewById(R.id.tv_registeredCapital_organdetail);
        tv_lagalPerson_organdetail=getViewById(R.id.tv_lagalPerson_organdetail);
        tv_address_organdetail=getViewById(R.id.tv_address_organdetail);
        tv_introduction_organdetail=getViewById(R.id.tv_introduction_organdetail);

        gv_pics_organdetail=getViewById(R.id.gv_pics_organdetail);

        gv_pics_organdetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent();
                intent.setClass(OrgandetailActivity.this, ImagePageActivity.class);
                intent.putExtra("picurlList",picurlList);
                intent.putExtra("selectPos",position);
                startActivity(intent);

            }
        });

        checkOrgPawnDetail();
    }

    private void checkOrgPawnDetail()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/checkOrgPawnDetail");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        OkGo.<DataResult<OrgPawnDetailInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<OrgPawnDetailInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<OrgPawnDetailInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){

                                orgPawnDetailInfo=response.body().getData();

                                tv_name_organdetail.setText(orgPawnDetailInfo.getOrgName());
                                tv_dealAmount_organdetail.setText(orgPawnDetailInfo.getDealAmount()+"笔");
                                tv_registeredCapital_organdetail.setText(orgPawnDetailInfo.getRegisteredCapital()+"元");
                                tv_lagalPerson_organdetail.setText(orgPawnDetailInfo.getLagalPerson());
                                tv_address_organdetail.setText(orgPawnDetailInfo.getAddress());
                                tv_introduction_organdetail.setText(orgPawnDetailInfo.getIntroduction());

                                if(orgPawnDetailInfo.getOrgImages()!=null&&!orgPawnDetailInfo.getOrgImages().equals("")){
                                    List<String> list= Arrays.asList(orgPawnDetailInfo.getOrgImages().split(","));
                                    for(int i=0;i<list.size();i++){
                                        picurlList.add(BaseConstant.Image_URL+list.get(i));
                                    }
                                    showList(list);
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
                    public void onError(com.lzy.okgo.model.Response<DataResult<OrgPawnDetailInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showList(List<String> result) {
        CommonAdapter commAdapter = new CommonAdapter<String>(OrgandetailActivity.this, result,
                R.layout.item_pic_pawn) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

                ImageView iv_pic_pawn=(ImageView) helper.getView(R.id.iv_pic_pawn);
                getImageLoader().displayImage(BaseConstant.Image_URL+item,iv_pic_pawn,getImageLoaderOptions());

            }
        };

        gv_pics_organdetail.setAdapter(commAdapter);
    }

}

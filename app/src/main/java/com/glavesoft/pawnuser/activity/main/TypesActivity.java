package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.shoppingmall.StoreGoodsListActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.PawnCateInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/10/24
 * @company:常州宝丰
 */
public class TypesActivity extends BaseActivity {
    private ListView lv_types;
    private ArrayList<PawnCateInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    private String state;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);
        state=getIntent().getStringExtra("state");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("分类");

        lv_types=(ListView)findViewById(R.id.lv_types);
        pawnCateList();

        lv_types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(TypesActivity.this, StoreGoodsListActivity.class);
            intent.putExtra("type", list.get(position).getCode());
            intent.putExtra("state", state);
            startActivity(intent);

            }
        });

    }

    private void showList(ArrayList<PawnCateInfo> result) {

        commAdapter = new CommonAdapter<PawnCateInfo>(TypesActivity.this, result,
                R.layout.item_types) {
            @Override
            public void convert(final ViewHolder helper, final PawnCateInfo item) {
                helper.setText(R.id.tv_name_types,item.getName());
                helper.setText(R.id.tv_pp_types,item.getCateType());

                ImageView iv_pic_types=(ImageView) helper.getView(R.id.iv_pic_types);
                getImageLoader().displayImage(BaseConstant.Image_URL + item.getIcon(),iv_pic_types,getImageLoaderOptions());


            }
        };

        lv_types.setAdapter(commAdapter);

    }

    private void pawnCateList()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("pawnCateList/pawnCateList");
        HttpParams param=new HttpParams();
        param.put("token",token);
        OkGo.<DataResult<ArrayList<PawnCateInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<PawnCateInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<PawnCateInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                list=response.body().getData();
                                showList(list);
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<PawnCateInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

}

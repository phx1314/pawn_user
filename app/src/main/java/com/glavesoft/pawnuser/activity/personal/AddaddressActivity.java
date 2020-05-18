package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bigkoo.pickerview.OptionsPickerView;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.appraisal.OtherActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.AddressInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.JsonBean;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.JsonFileReader;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/10/20
 * @company:常州宝丰
 */
public class AddaddressActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_select_ssq;
    private EditText et_address,et_name_address,et_phone_address;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String province,city,district;
    private LinearLayout ll_submit_address;
    private ImageView iv_submit_address;
    private String type;
    private AddressInfo addressInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        type=getIntent().getStringExtra("type");
        initJsonData();
        initView();
    }

    private void initView() {
        setTitleBack();

        tv_select_ssq=(TextView) findViewById(R.id.tv_select_ssq);
        et_address=(EditText) findViewById(R.id.et_address);
        et_name_address=(EditText) findViewById(R.id.et_name_address);
        et_phone_address=(EditText) findViewById(R.id.et_phone_address);

        ll_submit_address=(LinearLayout) findViewById(R.id.ll_submit_address);
        iv_submit_address=(ImageView) findViewById(R.id.iv_submit_address);

        tv_select_ssq.setOnClickListener(this);
        ll_submit_address.setOnClickListener(this);

        if(type.equals("add")){
            setTitleName("新增地址信息");
            setTitleNameEn(R.mipmap.modify_addr);
            iv_submit_address.setImageResource(R.mipmap.confirm_add);
        }else{
            setTitleName("修改地址信息");
            setTitleNameEn(R.mipmap.modify_addr);
            iv_submit_address.setImageResource(R.mipmap.confirm_modification);
            addressInfo=(AddressInfo)getIntent().getSerializableExtra("AddressInfo");
            et_address.setText(addressInfo.getAddress());
            et_name_address.setText(addressInfo.getName());
            et_phone_address.setText(addressInfo.getPhone());
            tv_select_ssq.setText(addressInfo.getArea());
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        switch (v.getId())
        {
            case R.id.tv_select_ssq:
                //判断输入法的隐藏状态
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                showPickerView();//调用CityPicker选取区域
                break;
            case R.id.ll_submit_address:
                add();
                break;
        }
    }

    private void add(){
        if(et_name_address.getText().toString().trim().length()==0){
            CustomToast.show("请输入收件人");
            return;
        }
        if(et_phone_address.getText().toString().trim().length()==0){
            CustomToast.show("请输入联系电话");
            return;
        }
        if(tv_select_ssq.getText().toString().trim().length()==0){
            CustomToast.show("请选择省市区");
            return;
        }
        if(et_address.getText().toString().trim().length()==0){
            CustomToast.show("请输入详细地址");
            return;
        }
        if(type.equals("add")){
            addMyAddress();
        }else{
            updateMyAddress();
        }
    }

    int Options1=0;int Options2=0;int Options3=0;
    private void showPickerView() {
        OptionsPickerView pvOptions=new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Options1=options1;
                Options2=options2;
                Options3=options3;
                //返回的分别是三个级别的选中位置
                province= options1Items.get(options1).getPickerViewText();
                city= options2Items.get(options1).get(options2);
                district=options3Items.get(options1).get(options2).get(options3);

                String text = province + city + district;
                tv_select_ssq.setText(text);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(getResources().getColor(R.color.black1))
                .setContentTextSize(18)
                .setCancelColor(getResources().getColor(R.color.black1))
                .setSubmitColor(getResources().getColor(R.color.black1))
                .setSelectOptions(Options1,Options2,Options3)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    private void initJsonData() {   //解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        //  获取json数据
        String JsonData = JsonFileReader.getJson(this, "province_data.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    //添加地址
    private void addMyAddress()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/addMyAddress");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("userName",et_name_address.getText().toString().trim());
        param.put("phone",et_phone_address.getText().toString().trim());
        param.put("area",tv_select_ssq.getText().toString().trim());
        param.put("address",et_address.getText().toString().trim());
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
                            CustomToast.show("添加成功");
                            Intent intent = new Intent("AddressRefresh");
                            sendBroadcast(intent);
                            finish();
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

    //编辑地址
    private void updateMyAddress()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("home/updateMyAddress");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",addressInfo.getId());
        param.put("userName",et_name_address.getText().toString().trim());
        param.put("phone",et_phone_address.getText().toString().trim());
        param.put("area",tv_select_ssq.getText().toString().trim());
        param.put("address",et_address.getText().toString().trim());
        param.put("type","1");
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
                            CustomToast.show("修改成功");
                            Intent intent = new Intent("AddressRefresh");
                            sendBroadcast(intent);
                            finish();
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

}

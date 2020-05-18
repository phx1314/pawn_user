package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.KeywordInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.FlowLayout;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/12/27
 * @company:常州宝丰
 */
public class HistoricalSearchActivity extends BaseActivity{

    private FlowLayout fl_keyword_recommend;
    private ListView lv_historical;
    private ArrayList<String> historcalList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historicalsearch);
        init();
    }

    private void init() {
        settitle_Searchcancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gettitle_Searchet().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(!v.getText().toString().trim().equals("")){
                        savedata(v.getText().toString().trim());
                        Intent intent=new Intent();
                        intent.setClass(HistoricalSearchActivity.this, SeachNewActivity.class);
                        intent.putExtra("keyword",v.getText().toString().trim());
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });
        lv_historical= (ListView) findViewById(R.id.lv_historical);
        fl_keyword_recommend= (FlowLayout) findViewById(R.id.fl_keyword_recommend);

        searchIndexHotMenu();
    }

    public void onResume(){
        super.onResume();
        historcalList=new ArrayList<>();
        if(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical, null)!=null){
            java.lang.reflect.Type classtype = new TypeToken<ArrayList<String>>() {}.getType();
            String jsonString=PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical,null);
            historcalList = CommonUtils.fromJson(jsonString, classtype, CommonUtils.DEFAULT_DATE_PATTERN);
            ArrayList<String> List=new ArrayList<>();
            for(int i=0;i<historcalList.size();i++){
                List.add(historcalList.get(historcalList.size()-1-i));
            }
            showList(List);
        }
    }

    private void savedata(String keyword){
        if(historcalList.contains(keyword)){
            int index=0;
            for(int i=0;i<historcalList.size();i++){
                if(keyword.equals(historcalList.get(i))){
                    index=i;
                }
            }
            historcalList.remove(index);
            historcalList.add(keyword);
        }else{
            historcalList.add(keyword);
        }

        if(historcalList.size()>10){
            historcalList.remove(0);
        }

        PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical, new Gson().toJson(historcalList));
    }

    private void searchIndexHotMenu()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/searchIndexHotMenu");
        HttpParams param=new HttpParams();
        param.put("token",token);
        OkGo.<DataResult<ArrayList<KeywordInfo>>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ArrayList<KeywordInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<KeywordInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                List<String> list = new ArrayList<>();
                                for (int i=0;i<response.body().getData().size();i++){
                                    list.add(response.body().getData().get(i).getName());
                                }
                                fl_keyword_recommend.setFlowLayout(list, new FlowLayout.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String content) {
                                        savedata(content);
                                        Intent intent=new Intent();
                                        intent.setClass(HistoricalSearchActivity.this, SeachNewActivity.class);
                                        intent.putExtra("keyword",content);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102 )
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<KeywordInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showList(final ArrayList<String> List) {

        CommonAdapter commAdapter = new CommonAdapter<String>(HistoricalSearchActivity.this, List,
                R.layout.item_historical) {
            @Override
            public void convert(final ViewHolder helper, final String item) {
                helper.setText(R.id.tv_key,item);
                helper.getView(R.id.iv_key_del).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index=0;
                        for(int i=0;i<List.size();i++){
                            if(item.equals(List.get(i))){
                                index=i;
                            }
                        }

                        List.remove(index);
                        notifyDataSetChanged();

                        ArrayList<String> historcalList1=new ArrayList<>();
                        for(int i=0;i<List.size();i++){
                            historcalList1.add(List.get(List.size()-1-i));
                        }
                        PreferencesUtils.setStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_Historical, new Gson().toJson(historcalList1));

                    }
                });

                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savedata(item);
                        Intent intent=new Intent();
                        intent.setClass(HistoricalSearchActivity.this, SeachNewActivity.class);
                        intent.putExtra("keyword",item);
                        startActivity(intent);
                    }
                });
            }
        };

        lv_historical.setAdapter(commAdapter);
    }
}

package com.glavesoft.pawnuser.activity.appraisal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.IdRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.PicAdapter;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.OnlineIdentificationInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.FileUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/10/28
 * @company:常州宝丰
 */
public class NoblemetalActivity extends BaseActivity implements View.OnClickListener{
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ArrayList<String> urlList = new ArrayList<String>();
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类

    private EditText et_weight_noblemetal;
    private GridViewForNoScroll gv_pics_evaluationinfo;
    private LinearLayout ll_submit_noblemetal;
    PicAdapter adapter;

    private RadioGroup rg_metaltype;
    private RadioButton rb_hj,rb_bj;
    private String metaltype="黄金";

    private TextView tv_cd_noblemetal;
    private ArrayList<String> cdlist=new ArrayList<>();
    private ArrayList<String> cdlist1=new ArrayList<>();

    private String picIds="";
    private ArrayList<OnlineIdentificationInfo> Jesonlist=new ArrayList<>();
    private String code="",name="";

    private LinearLayout ll_gj;
    private TextView tv_gj;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noblemetal);
        code=getIntent().getStringExtra("code");
        name=getIntent().getStringExtra("name");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName(name+"鉴定估价资料");
        setTitleNameEn(R.mipmap.precious_metal);

        rg_metaltype=(RadioGroup) findViewById(R.id.rg_metaltype);
        rb_hj=(RadioButton) findViewById(R.id.rb_hj);
        rb_bj=(RadioButton) findViewById(R.id.rb_bj);

        rg_metaltype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==rb_hj.getId()){
                    rb_hj.setChecked(true);
                    rb_bj.setChecked(false);
                    metaltype="黄金";
                }else if(checkedId==rb_bj.getId()){
                    rb_hj.setChecked(false);
                    rb_bj.setChecked(true);
                    metaltype="铂金";
                }
            }
        });

        et_weight_noblemetal=(EditText) findViewById(R.id.et_weight_noblemetal);
        //et_weight_noblemetal.setFilters(new InputFilter[] { new BaseConstant.EditInputFilter(et_weight_noblemetal,1) });
        gv_pics_evaluationinfo=(GridViewForNoScroll)findViewById(R.id.gv_pics_evaluationinfo);

        tv_cd_noblemetal=(TextView) findViewById(R.id.tv_cd_noblemetal);
        ll_submit_noblemetal=(LinearLayout) findViewById(R.id.ll_submit_noblemetal);
        ll_submit_noblemetal.setOnClickListener(this);
        tv_cd_noblemetal.setOnClickListener(this);

        adapter = new PicAdapter(NoblemetalActivity.this,urlList,imagePaths);
        gv_pics_evaluationinfo.setAdapter(adapter);
        gv_pics_evaluationinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                if (position == adapter.geturlLists().size()) {
                    requestReadCameraPermissions(new CheckPermListener() {
                        @Override
                        public void superREADPermission() {
                            PhotoPickerIntent intent = new PhotoPickerIntent(NoblemetalActivity.this);
                            intent.setSelectModel(SelectModel.MULTI);
                            intent.setShowCarema(true);
                            intent.setMaxTotal(9); // 最多选择照片数量，默认为9
                            intent.setSelectedPaths(adapter.getimagePaths()); // 已选中的照片地址， 用于回显选中状态
                            startActivityForResult(intent, REQUEST_CAMERA_CODE);
                        }
                    });
                }
            }
        });

        ll_gj=(LinearLayout) findViewById(R.id.ll_gj);
        tv_gj=(TextView) findViewById(R.id.tv_gj);

        cdlist.add("足金(含金量≥99.0%)");cdlist.add("22K金(含金量91.6%)");cdlist.add("18K(含金量75%)");
        cdlist.add("14K(含金量58.5%)");cdlist.add("Pt990(铂金含量99%)");cdlist.add("Pt950(铂金含量95%)");
        cdlist.add("Pt900(铂金含量90%)");

        cdlist1.add("0.99");cdlist1.add("0.916");cdlist1.add("0.75");
        cdlist1.add("0.585");cdlist1.add("0.99");cdlist1.add("0.95");
        cdlist1.add("0.90");
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.tv_cd_noblemetal://纯度
                showPopupWindow(NoblemetalActivity.this,tv_cd_noblemetal,cdlist);
                break;
            case R.id.ll_submit_noblemetal:
                gotoSend();
                break;
        }
    }

    private void gotoSend() {
        //if(adapter.geturlLists().size()>0){

            if(et_weight_noblemetal.getText().toString().trim().length()==0){
                CustomToast.show("请输入克重");
                return;
            }

            if(tv_cd_noblemetal.getText().toString().trim().length()==0){
                CustomToast.show("请选择纯度");
                return;
            }

        OnlineIdentificationInfo info1 =new OnlineIdentificationInfo();
        info1.setName("金属种类");
        info1.setContent(metaltype);
        info1.setContentType("1");
        Jesonlist.add(info1);
        OnlineIdentificationInfo info2 =new OnlineIdentificationInfo();
        info2.setName("克重");
        info2.setContent(et_weight_noblemetal.getText().toString().trim());
        info2.setContentType("1");
        Jesonlist.add(info2);
        OnlineIdentificationInfo info3 =new OnlineIdentificationInfo();
        info3.setName("纯度");
        int cdindex = 0;
        for (int i=0;i<cdlist.size();i++){
            if(tv_cd_noblemetal.getText().toString().trim().equals(cdlist.get(i))){
                cdindex=i;
            }
        }
        info3.setContent(cdlist1.get(cdindex));
        info3.setContentType("1");
        Jesonlist.add(info3);

        gotoAuthGood();

//            List<File> files=new ArrayList<>();
//            File file= new File(adapter.geturlLists().get(0));
//            files.add(file);
//            UploadPic(files);
//        }else{
//            CustomToast.show("请上传外观照");
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    loadimagePaths(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    loadimagePaths(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(captureManager.getCurrentPhotoPath());
                        loadimagePaths(paths);
                    }
                    break;
            }
        }
    }

    private void loadimagePaths(ArrayList<String> paths) {
        imagePaths= new ArrayList<>();
        urlList= new ArrayList<>();
        urlList.addAll(paths);
        imagePaths.addAll(paths);
        adapter = new PicAdapter(NoblemetalActivity.this,urlList,imagePaths);
        gv_pics_evaluationinfo.setAdapter(adapter);
//        comImg(paths);
    }
    // 压缩
    public void comImg(final ArrayList<String> paths){
        new AsyncTask<Void,Void,Void>(){
            private ProgressDialog dialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(NoblemetalActivity.this,"","图片处理中...");
            }
            @Override
            protected Void doInBackground(Void... params) {
                for(int i=0;i<paths.size();i++){
                    try {
                        File file = FileUtils.compressImg(new File(paths.get(i)), FileUtils.FILE_IMAGE_MAXSIZE);
                        if (file != null) {
                            urlList.add( file.getAbsolutePath());
                        }
                    } catch (IOException e) {
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(dialog!=null && dialog.isShowing()) dialog.dismiss();
                adapter = new PicAdapter(NoblemetalActivity.this,urlList,imagePaths);
                gv_pics_evaluationinfo.setAdapter(adapter);
            }
        }.execute();
    }

    //上传图片
    int startPos_PicList = 0;
    public void UploadPic( List<File> files){
        getlDialog().show();
        Map<String, String> param = new HashMap<String, String>();
        String url= BaseConstant.UploadAvatar_URL;
        VolleyUtil.postMultparamiApi(url,param, "name",files, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                getlDialog().dismiss();
                java.lang.reflect.Type classtype = new TypeToken<DataResult<ImageInfo>>() {}.getType();
                DataResult<ImageInfo> response= CommonUtils.fromJson(s, classtype, CommonUtils.DEFAULT_DATE_PATTERN);
                if (DataResult.RESULT_OK_ZERO==response.getErrorCode() ) {

                    if(picIds.equals("")){
                        picIds =response.getData().getId();
                    }else{
                        picIds=picIds+","+response.getData().getId();
                    }

                    startPos_PicList++;
                    if (startPos_PicList < adapter.geturlLists().size()) {
                        List<File> files=new ArrayList<>();
                        File file= new File(adapter.geturlLists().get(startPos_PicList));
                        files.add(file);
                        UploadPic(files);
                    } else {
                        OnlineIdentificationInfo info =new OnlineIdentificationInfo();
                        info.setName("外观照");
                        info.setContent(picIds);
                        info.setContentType("3");
                        Jesonlist.add(info);
                        OnlineIdentificationInfo info1 =new OnlineIdentificationInfo();
                        info1.setName("金属种类");
                        info1.setContent(metaltype);
                        info1.setContentType("1");
                        Jesonlist.add(info1);
                        OnlineIdentificationInfo info2 =new OnlineIdentificationInfo();
                        info2.setName("克重");
                        info2.setContent(et_weight_noblemetal.getText().toString().trim());
                        info2.setContentType("1");
                        Jesonlist.add(info2);
                        OnlineIdentificationInfo info3 =new OnlineIdentificationInfo();
                        info3.setName("纯度");
                        info3.setContent(tv_cd_noblemetal.getText().toString().trim());
                        info3.setContentType("1");
                        Jesonlist.add(info3);

                        gotoAuthGood();
                    }
                } else if (DataResult.RESULT_102 == response.getErrorCode())
                {
                    toLogin();
                }else
                {
                    CustomToast.show(response.getErrorMsg());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                getlDialog().dismiss();
                CustomToast.show("上传失败,请重新上传");
            }
        });

    }

    //在线鉴定
    private void gotoAuthGood()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/gotoAuthGood");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("content",new Gson().toJson(Jesonlist));
        param.put("code",code);
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
                            ll_gj.setVisibility(View.VISIBLE);
                            tv_gj.setText("￥"+response.body().getData().getMsg());
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

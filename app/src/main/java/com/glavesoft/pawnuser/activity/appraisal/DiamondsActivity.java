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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/10/27
 * @company:常州宝丰
 */
public class DiamondsActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ArrayList<String> urlList = new ArrayList<String>();
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类

    private GridViewForNoScroll gv_pics_evaluationinfo;
    private LinearLayout ll_submit_diamonds;
    PicAdapter adapter;

    private RadioGroup rg_shape;
    private RadioButton rb_yuan,rb_yi;
    private String shape="圆形钻";

    private TextView tv_klqj_diamonds,tv_color_diamonds,tv_jd_diamonds,tv_qg_diamonds;
    private EditText et_kl_diamond;

    private ArrayList<String> klqjlist=new ArrayList<>();
    private ArrayList<String> colorlist=new ArrayList<>();
    private ArrayList<String> colorlist1=new ArrayList<>();
    private ArrayList<String> jdlist=new ArrayList<>();
    private ArrayList<String> jdlist1=new ArrayList<>();
    private ArrayList<String> qglist=new ArrayList<>();

    private String picIds="";
    private ArrayList<OnlineIdentificationInfo> Jesonlist=new ArrayList<>();
    private String code="",name="";

    private LinearLayout ll_gj;
    private TextView tv_gj;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diamonds);
        code=getIntent().getStringExtra("code");
        name=getIntent().getStringExtra("name");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName(name+"鉴定估价资料");
        setTitleNameEn(R.mipmap.diamond);

        rg_shape=(RadioGroup) findViewById(R.id.rg_shape);
        rb_yuan=(RadioButton) findViewById(R.id.rb_yuan);
        rb_yi=(RadioButton) findViewById(R.id.rb_yi);

        rg_shape.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==rb_yuan.getId()){
                    rb_yuan.setChecked(true);
                    rb_yi.setChecked(false);
                    shape="圆形钻";
                }else if(checkedId==rb_yi.getId()){
                    rb_yuan.setChecked(false);
                    rb_yi.setChecked(true);
                    shape="异形钻";
                }
            }
        });

        tv_klqj_diamonds=(TextView)findViewById(R.id.tv_klqj_diamonds);
        tv_color_diamonds=(TextView)findViewById(R.id.tv_color_diamonds);
        tv_jd_diamonds=(TextView)findViewById(R.id.tv_jd_diamonds);
        tv_qg_diamonds=(TextView)findViewById(R.id.tv_qg_diamonds);

        et_kl_diamond=(EditText) findViewById(R.id.et_kl_diamond);

        gv_pics_evaluationinfo=(GridViewForNoScroll)findViewById(R.id.gv_pics_evaluationinfo);

        ll_submit_diamonds=(LinearLayout) findViewById(R.id.ll_submit_diamonds);
        ll_submit_diamonds.setOnClickListener(this);

        tv_klqj_diamonds.setOnClickListener(this);
        tv_color_diamonds.setOnClickListener(this);
        tv_jd_diamonds.setOnClickListener(this);
        tv_qg_diamonds.setOnClickListener(this);

        adapter = new PicAdapter(DiamondsActivity.this,urlList,imagePaths);
        gv_pics_evaluationinfo.setAdapter(adapter);
        gv_pics_evaluationinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == adapter.geturlLists().size()) {
                    requestReadCameraPermissions(new CheckPermListener() {
                        @Override
                        public void superREADPermission() {
                            PhotoPickerIntent intent = new PhotoPickerIntent(DiamondsActivity.this);
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

        klqjlist.add("0.18-0.22");klqjlist.add("0.23-0.29");klqjlist.add("0.30-0.39");klqjlist.add("0.40-0.49");
        klqjlist.add("0.50-0.69");klqjlist.add("0.70-0.89");klqjlist.add("0.90-0.99");klqjlist.add("1.00-1.49");
        klqjlist.add("1.50-1.99");klqjlist.add("2.00-2.99");klqjlist.add("3.00-3.99");klqjlist.add("4.00-4.99");
        klqjlist.add("5.00-5.99");klqjlist.add("10.00-10.99");

        colorlist.add("D(极白)"); colorlist.add("E(极白)"); colorlist.add("F(优白)"); colorlist.add("G(优白)");
        colorlist.add("H(白)"); colorlist.add("I(微黄白)"); colorlist.add("J(微黄白)"); colorlist.add("K(浅黄白)");
        colorlist.add("L(浅黄白)"); colorlist.add("M(浅黄)"); //colorlist.add("N(浅黄)");
        colorlist1.add("D"); colorlist1.add("E"); colorlist1.add("F"); colorlist1.add("G");
        colorlist1.add("H"); colorlist1.add("I"); colorlist1.add("J"); colorlist1.add("K");
        colorlist1.add("L"); colorlist1.add("M"); //colorlist1.add("N");

        jdlist.add("FL/IF(无瑕级)"); jdlist.add("VVS1(极微瑕1级)"); jdlist.add("VVS2(极微瑕2级)"); jdlist.add("VS1(微瑕1级)");
        jdlist.add("VS2(微瑕2级)"); jdlist.add("SI1(小瑕疵1级)"); jdlist.add("SI2(小瑕疵2级)"); jdlist.add("SI3(小瑕疵3级)");
        jdlist.add("I1(重瑕疵1级)"); jdlist.add("I2(重瑕疵2级)"); jdlist.add("I3(重瑕疵3级)");
        jdlist1.add("IF"); jdlist1.add("VVS1"); jdlist1.add("VVS2"); jdlist1.add("VS1");
        jdlist1.add("VS2"); jdlist1.add("SI1"); jdlist1.add("SI2"); jdlist1.add("SI3");
        jdlist1.add("I1"); jdlist1.add("I2"); jdlist1.add("I3");

        qglist.add("3EX(异性2EX)极好切工"); qglist.add("VG非常好切工"); qglist.add("G好切工");
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.tv_klqj_diamonds://克拉区间
                showPopupWindow(DiamondsActivity.this,tv_klqj_diamonds,klqjlist);
                break;
            case R.id.tv_color_diamonds://颜色
                showPopupWindow(DiamondsActivity.this,tv_color_diamonds,colorlist);
                break;
            case R.id.tv_jd_diamonds://净度
                showPopupWindow(DiamondsActivity.this,tv_jd_diamonds,jdlist);
                break;
            case R.id.tv_qg_diamonds://切工
                showPopupWindow(DiamondsActivity.this,tv_qg_diamonds,qglist);
                break;
            case R.id.ll_submit_diamonds:
                gotoSend();
                break;
        }
    }



    private void gotoSend() {
 //       if(adapter.geturlLists().size()>0){

//            if(tv_klqj_diamonds.getText().toString().trim().length()==0){
//                CustomToast.show("请选择克拉区间");
//                return;
//            }

            if(et_kl_diamond.getText().toString().trim().length()==0){
                CustomToast.show("请输入克拉数");
                return;
            }

            if(tv_color_diamonds.getText().toString().trim().length()==0){
                CustomToast.show("请选择颜色");
                return;
            }

            if(tv_jd_diamonds.getText().toString().trim().length()==0){
                CustomToast.show("请选择净度");
                return;
            }

            if(tv_qg_diamonds.getText().toString().trim().length()==0){
                CustomToast.show("请选择切工");
                return;
            }

        OnlineIdentificationInfo info1 =new OnlineIdentificationInfo();
        info1.setName("形状");
        info1.setContent(shape);
        info1.setContentType("1");
        Jesonlist.add(info1);
        OnlineIdentificationInfo info2 =new OnlineIdentificationInfo();
        info2.setName("克拉区间");
        info2.setContent(tv_klqj_diamonds.getText().toString().trim());
        info2.setContentType("1");
        Jesonlist.add(info2);
        OnlineIdentificationInfo info3 =new OnlineIdentificationInfo();
        info3.setName("克拉重量");
        info3.setContent(et_kl_diamond.getText().toString().trim());
        info3.setContentType("1");
        Jesonlist.add(info3);
        OnlineIdentificationInfo info4 =new OnlineIdentificationInfo();
        info4.setName("颜色");
        int colorindex = 0;
        for (int i=0;i<colorlist.size();i++){
            if(tv_color_diamonds.getText().toString().trim().equals(colorlist.get(i))){
                colorindex=i;
            }
        }
        info4.setContent(colorlist1.get(colorindex));
        info4.setContentType("1");
        Jesonlist.add(info4);
        OnlineIdentificationInfo info5 =new OnlineIdentificationInfo();
        info5.setName("净度");

        int jdindex = 0;
        for (int i=0;i<jdlist.size();i++){
            if(tv_jd_diamonds.getText().toString().trim().equals(jdlist.get(i))){
                jdindex=i;
            }
        }
        info5.setContent(jdlist1.get(jdindex));
        info5.setContentType("1");
        Jesonlist.add(info5);
        OnlineIdentificationInfo info6 =new OnlineIdentificationInfo();
        info6.setName("切工");
        info6.setContent(tv_qg_diamonds.getText().toString().trim());
        info6.setContentType("1");
        Jesonlist.add(info6);

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
        adapter = new PicAdapter(DiamondsActivity.this,urlList,imagePaths);
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
                dialog = ProgressDialog.show(DiamondsActivity.this,"","图片处理中...");
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
                adapter = new PicAdapter(DiamondsActivity.this,urlList,imagePaths);
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
                        info1.setName("形状");
                        info1.setContent(shape);
                        info1.setContentType("1");
                        Jesonlist.add(info1);
                        OnlineIdentificationInfo info2 =new OnlineIdentificationInfo();
                        info2.setName("克拉区间");
                        info2.setContent(tv_klqj_diamonds.getText().toString().trim());
                        info2.setContentType("1");
                        Jesonlist.add(info2);
                        OnlineIdentificationInfo info3 =new OnlineIdentificationInfo();
                        info3.setName("克拉");
                        info3.setContent(et_kl_diamond.getText().toString().trim());
                        info3.setContentType("1");
                        Jesonlist.add(info3);
                        OnlineIdentificationInfo info4 =new OnlineIdentificationInfo();
                        info4.setName("颜色");
                        info4.setContent(tv_color_diamonds.getText().toString().trim());
                        info4.setContentType("1");
                        Jesonlist.add(info4);
                        OnlineIdentificationInfo info5 =new OnlineIdentificationInfo();
                        info5.setName("净度");
                        info5.setContent(tv_jd_diamonds.getText().toString().trim());
                        info5.setContentType("1");
                        Jesonlist.add(info5);
                        OnlineIdentificationInfo info6 =new OnlineIdentificationInfo();
                        info6.setName("切工");
                        info6.setContent(tv_qg_diamonds.getText().toString().trim());
                        info6.setContentType("1");
                        Jesonlist.add(info6);

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
        OkGo.<DataResult<DataInfo>>post(url)
                .params("token", token)
                .params("content",new Gson().toJson(Jesonlist))
                .params("code",code)
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

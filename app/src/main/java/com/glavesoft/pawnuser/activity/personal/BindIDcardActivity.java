package com.glavesoft.pawnuser.activity.personal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.appraisal.MailAppraisalActivity;
import com.glavesoft.pawnuser.activity.appraisal.OtherActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.UserInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.FaceUtil;
import com.glavesoft.util.FileUtils;
import com.glavesoft.view.CustomProgressDialog;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import com.megvii.faceppidcardui.IDCardScanActivity;
import com.megvii.faceppidcardui.util.ConUtil;
import com.megvii.faceppidcardui.util.Util;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/11/3
 * @company:常州宝丰
 */
public class BindIDcardActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_zm_addpic,iv_face_addpic,iv_fm_addpic;
    private ImageView iv_zm_showpic,iv_face_showpic,iv_fm_showpic;
    private TextView tv_submit_bd;
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_CAMERA_CODE1 = 111;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ImageCaptureManager captureManager; // 相机拍照处理类

    private String zmpath="";
    private String fmpath="";
    private String facepath="";
    private String idCardImg;//身份证正面
    private String idCardReverse;//身份证反面
    private String headImage;

    private String name="",id_card_number="",address="";

    private boolean isFaceback=false;

    private LinearLayout ll_idcard;
    private TextView tv_update_bd;
    private TextView tv_name_idcard,tv_address_idcard;
    private TextView tv_number_idcard,tv_updateaddress_bd;

    //总大小
    private long totalSize;
    //当前总上传大小
    private long UploadTotalSize=0;
    //之前上传大小
    private long beforeUploadSize=0;
    //当前单个上传大小
    private long currentUploadSize=0;
    private CustomProgressDialog mProcessingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindidcard);
        setBoardCast();
        initView();
    }

    private void setBoardCast() {
        //注册广播
        IntentFilter f = new IntentFilter();
        f.addAction("face");
        registerReceiver(mListenerID, f);

        IntentFilter f1 = new IntentFilter();
        f1.addAction("idcard");
        registerReceiver(mListenerID1, f1);

    }

    BroadcastReceiver mListenerID = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            File file = new File(FileUtils.FACE_PATH);
            if (file.exists())
            {
                facepath=file.getAbsolutePath();
                iv_face_addpic.setVisibility(View.GONE);
                iv_face_showpic.setVisibility(View.VISIBLE);
                //iv_face_showpic.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                Glide.with(BindIDcardActivity.this).load(facepath).into(iv_face_showpic);
            }

        }
    };

    BroadcastReceiver mListenerID1 = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            zmpath=intent.getStringExtra("path");

            iv_zm_addpic.setVisibility(View.GONE);
            iv_zm_showpic.setVisibility(View.VISIBLE);
//            iv_zm_showpic.setImageBitmap(BitmapFactory.decodeFile(zmpath));
            Glide.with(BindIDcardActivity.this).load(zmpath).into(iv_zm_showpic);
            doOCR(zmpath);

        }
    };

    public  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mListenerID);
        unregisterReceiver(mListenerID1);
        File file = new File(FileUtils.FACE_PATH);
        if (file.exists())
        {
            file.delete();
        }

    }

    private void initView() {
        setTitleBack();
        setTitleName("身份绑定");

        iv_zm_addpic=(ImageView) findViewById(R.id.iv_zm_addpic);
        iv_zm_showpic=(ImageView) findViewById(R.id.iv_zm_showpic);
        iv_face_addpic=(ImageView) findViewById(R.id.iv_face_addpic);
        iv_face_showpic=(ImageView) findViewById(R.id.iv_face_showpic);
        iv_fm_addpic=(ImageView) findViewById(R.id.iv_fm_addpic);
        iv_fm_showpic=(ImageView) findViewById(R.id.iv_fm_showpic);

        ll_idcard=(LinearLayout) findViewById(R.id.ll_idcard);
        tv_name_idcard=(TextView) findViewById(R.id.tv_name_idcard);
        tv_number_idcard=(TextView) findViewById(R.id.tv_number_idcard);
        tv_address_idcard=(TextView) findViewById(R.id.tv_address_idcard);
        tv_update_bd=(TextView) findViewById(R.id.tv_update_bd);
        tv_updateaddress_bd=(TextView) findViewById(R.id.tv_updateaddress_bd);

        tv_submit_bd=(TextView) findViewById(R.id.tv_submit_bd);

        iv_zm_addpic.setOnClickListener(this);
        iv_face_addpic.setOnClickListener(this);
        iv_face_showpic.setOnClickListener(this);
        iv_zm_showpic.setOnClickListener(this);
        tv_submit_bd.setOnClickListener(this);
        iv_fm_addpic.setOnClickListener(this);
        iv_fm_showpic.setOnClickListener(this);
        tv_update_bd.setOnClickListener(this);
        tv_updateaddress_bd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.iv_zm_addpic:
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(BindIDcardActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                });
                //startActivity(new Intent(BindIDcardActivity.this, LoadingActivity1.class));
                break;
            case R.id.iv_zm_showpic:
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(BindIDcardActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                });

                //startActivity(new Intent(BindIDcardActivity.this, LoadingActivity1.class));
                break;
            case R.id.iv_fm_addpic:
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(BindIDcardActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE1);
                    }
                });

                break;
            case R.id.iv_fm_showpic:
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(BindIDcardActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE1);
                    }
                });

                break;
            case R.id.iv_face_addpic:
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        Intent intent1=new Intent();
                        //intent1.setClass(BindIDcardActivity.this, LoadingActivity.class);
                        intent1.setClass(BindIDcardActivity.this, AutioTakePhotoActivity.class);
                        intent1.putExtra("type","face");
                        startActivity(intent1);
                    }
                });

                break;
            case R.id.iv_face_showpic:
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        Intent intent1=new Intent();
                        intent1.setClass(BindIDcardActivity.this, AutioTakePhotoActivity.class);
                        intent1.putExtra("type","face");
                        startActivity(intent1);
                    }
                });

                break;
            case R.id.tv_update_bd:
                customDialog_input("修改姓名",tv_name_idcard.getText().toString().trim());
                break;
            case R.id.tv_updateaddress_bd:
                customDialog_input("修改地址",tv_address_idcard.getText().toString().trim());
                break;
            case R.id.tv_submit_bd:
                gotoSend();
                break;
        }
    }

    private void gotoSend() {
        if(zmpath.equals("")){
            CustomToast.show("请上传身份证正面照");
            return;
        }

        if(name.equals("")){
            CustomToast.show("请上传正确的身份证正面照");
            return;
        }

        if(fmpath.equals("")){
            CustomToast.show("请上传身份证反面照");
            return;
        }

        if (!isFaceback){
            CustomToast.show("请上传正确的身份证反面照");
            return;
        }

        if(facepath.equals("")){
            CustomToast.show("请人脸识别");
            return;
        }

        showPopupWindow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                case REQUEST_CAMERA_CODE1:
                    loadAdpater1(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if(captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();

                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(captureManager.getCurrentPhotoPath());
                        loadAdpater(paths);
                    }
                    break;

            }
        }
    }

    private void loadAdpater(ArrayList<String> paths){
        if(paths!=null&&paths.size()!=0){
            comImg(paths.get(0));
        }
    }

    // 压缩
    private void comImg(String path)
    {
        try
        {
            File file = FileUtils.compressImg(new File(path), FileUtils.FILE_IMAGE_MAXSIZE);
            if (file != null)
            {
                zmpath=file.getAbsolutePath();
                iv_zm_addpic.setVisibility(View.GONE);
                iv_zm_showpic.setVisibility(View.VISIBLE);
//                iv_zm_showpic.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                Glide.with(BindIDcardActivity.this).load(zmpath).into(iv_zm_showpic);

                ll_idcard.setVisibility(View.GONE);
                name="";
                id_card_number="";
                address="";

                doOCR(zmpath);

            } else
            {
                CustomToast.show("获取图片失败");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadAdpater1(ArrayList<String> paths){
        if(paths!=null&&paths.size()!=0){
            comImg1(paths.get(0));
        }
    }

    // 压缩
    private void comImg1(String path)
    {
        try
        {
            File file = FileUtils.compressImg(new File(path), FileUtils.FILE_IMAGE_MAXSIZE);
            if (file != null)
            {
                fmpath=file.getAbsolutePath();
                iv_fm_addpic.setVisibility(View.GONE);
                iv_fm_showpic.setVisibility(View.VISIBLE);
//                iv_fm_showpic.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                Glide.with(BindIDcardActivity.this).load(fmpath).into(iv_fm_showpic);

                doOCR(fmpath);
            } else
            {
                CustomToast.show("获取图片失败");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void UploadZM( File file){
        beforeUploadSize=beforeUploadSize+currentUploadSize;
        currentUploadSize=0;
        String url=BaseConstant.UploadAvatar_URL;
        OkGo.<DataResult<ImageInfo>>post(url)
                .params("file", file)
                .execute(new JsonCallback<DataResult<ImageInfo>>() {
                    @Override
                    public void onStart(Request<DataResult<ImageInfo>, ? extends Request> request) {

                    }
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<ImageInfo>> response) {
                        if (response==null){
                            mProcessingDialog.dismiss();
                            CustomToast.show(getString(R.string.msg_error));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK){

                            if(response.body().getData()!=null){

                                idCardImg=response.body().getData().getId();
                                UploadFM(new File(fmpath));
                            }

                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<ImageInfo>> response) {
                        mProcessingDialog.dismiss();
                        UploadTotalSize=0;
                        beforeUploadSize=0;
                        currentUploadSize=0;
                        CustomToast.show("上传失败,请重新上传");
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        //System.out.println("uploadProgress: " + progress);

                        currentUploadSize=progress.currentSize;

                        UploadTotalSize=beforeUploadSize+currentUploadSize;
                        System.out.println("UploadTotalSize: " + UploadTotalSize+"     totalSize"+totalSize);

                        final float currentProgress = (float)(UploadTotalSize)/totalSize * 100;
                        System.out.println("currentProgress: " + currentProgress);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProcessingDialog.setProgress((int) currentProgress);
                            }
                        });
                    }
                });
    }

    public void UploadFM( File file){
        beforeUploadSize=beforeUploadSize+currentUploadSize;
        currentUploadSize=0;
        String url=BaseConstant.UploadAvatar_URL;
        OkGo.<DataResult<ImageInfo>>post(url)
                .params("file", file)
                .execute(new JsonCallback<DataResult<ImageInfo>>() {
                    @Override
                    public void onStart(Request<DataResult<ImageInfo>, ? extends Request> request) {

                    }
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<ImageInfo>> response) {
                        if (response==null){
                            mProcessingDialog.dismiss();
                            CustomToast.show(getString(R.string.msg_error));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK){

                            if(response.body().getData()!=null){

                                idCardReverse=response.body().getData().getId();
                                UploadFace(new File(facepath));
                            }

                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<ImageInfo>> response) {
                        mProcessingDialog.dismiss();
                        UploadTotalSize=0;
                        beforeUploadSize=0;
                        currentUploadSize=0;
                        CustomToast.show("上传失败,请重新上传");
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        //System.out.println("uploadProgress: " + progress);

                        currentUploadSize=progress.currentSize;

                        UploadTotalSize=beforeUploadSize+currentUploadSize;
                        System.out.println("UploadTotalSize: " + UploadTotalSize+"     totalSize"+totalSize);

                        final float currentProgress = (float)(UploadTotalSize)/totalSize * 100;
                        System.out.println("currentProgress: " + currentProgress);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProcessingDialog.setProgress((int) currentProgress);
                            }
                        });
                    }
                });
    }

    public void UploadFace( File file){
        beforeUploadSize=beforeUploadSize+currentUploadSize;
        currentUploadSize=0;
        String url=BaseConstant.UploadAvatar_URL;
        OkGo.<DataResult<ImageInfo>>post(url)
                .params("file", file)
                .execute(new JsonCallback<DataResult<ImageInfo>>() {
                    @Override
                    public void onStart(Request<DataResult<ImageInfo>, ? extends Request> request) {

                    }
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<ImageInfo>> response) {
                        if (response==null){
                            mProcessingDialog.dismiss();
                            CustomToast.show(getString(R.string.msg_error));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK){

                            if(response.body().getData()!=null){

                                mProcessingDialog.dismiss();
                                headImage=response.body().getData().getId();
                                System.out.println("headImage===>"+headImage);
                                bindUserMsg();
                            }

                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<ImageInfo>> response) {
                        mProcessingDialog.dismiss();
                        UploadTotalSize=0;
                        beforeUploadSize=0;
                        currentUploadSize=0;
                        CustomToast.show("上传失败,请重新上传");
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        //System.out.println("uploadProgress: " + progress);

                        currentUploadSize=progress.currentSize;

                        UploadTotalSize=beforeUploadSize+currentUploadSize;
                        System.out.println("UploadTotalSize: " + UploadTotalSize+"     totalSize"+totalSize);

                        final float currentProgress = (float)(UploadTotalSize)/totalSize * 100;
                        System.out.println("currentProgress: " + currentProgress);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProcessingDialog.setProgress((int) currentProgress);
                            }
                        });
                    }
                });
    }



    //绑定用户信息
    private void bindUserMsg()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userFace/bindUserMsg");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("idCardImg",idCardImg);
        param.put("idCardReverse",idCardReverse);
        param.put("headImage",headImage);
        param.put("userName",name);
        param.put("idCard",id_card_number);
        param.put("address",address);
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
                            UserInfo info=LocalData.getInstance().getUserInfo();
                            info.setIdCardImg(idCardImg);
                            info.setIdCardReverse(idCardReverse);
                            info.setIsBind("1");
                            setUserAndFile(info);
                            CustomToast.show("绑定成功");
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
    ProgressDialog dialog = null;
    public void doOCR(final String path) {

        dialog = ProgressDialog.show(BindIDcardActivity.this,"","获取身份证信息...");
        try {
            String url = "https://api-cn.faceplusplus.com/cardpp/v1/ocridcard";
            RequestParams rParams = new RequestParams();
            Log.w("ceshi", "Util.API_OCRKEY===" + Util.API_SECRET + ", Util.API_OCRSECRET===" + Util.API_SECRET);
            rParams.put("api_key", FaceUtil.API_KEY);
            rParams.put("api_secret", FaceUtil.API_SECRET);
            rParams.put("image_file", new File(path));
            rParams.put("legality", 1 + "");
            AsyncHttpClient asyncHttpclient = new AsyncHttpClient();
            asyncHttpclient.post(url, rParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseByte) {
                    if(dialog!=null && dialog.isShowing()) dialog.dismiss();

                    try {
                        String successStr = new String(responseByte);
                        Log.w("ceshi", "ocr  onSuccess: " + successStr);
                        String info = "";
                        JSONObject jObject = new JSONObject(successStr).getJSONArray("cards").getJSONObject(0);
                        if ("back".equals(jObject.getString("side"))) {
                            String officeAdress = jObject
                                    .getString("issued_by");
                            String useful_life = jObject
                                    .getString("valid_date");
                            info = info + "officeAdress:  " + officeAdress
                                    + "\nuseful_life:  " + useful_life;

                            isFaceback=true;
                        } else {
                            String birthday = jObject.getString("birthday");
                            String gender = jObject.getString("gender");
                            id_card_number = jObject.getString("id_card_number");
                            name = jObject.getString("name");
                            address = jObject.getString("address");

                            ll_idcard.setVisibility(View.VISIBLE);
                            tv_name_idcard.setText(name);
                            tv_number_idcard.setText("身份证号："+id_card_number);
                            tv_address_idcard.setText(address);

                        }
                        //contentText.setText(contentText.getText().toString() + info);

                    } catch (Exception e) {
                        e.printStackTrace();
                        if(dialog!=null && dialog.isShowing()) dialog.dismiss();
                        CustomToast.show("识别失败，请重新识别！");
                        isFaceback=false;
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        Log.w("ceshi", "responseBody==="
                                + new String(responseBody));
                    }
                    if(dialog!=null && dialog.isShowing()) dialog.dismiss();
                    CustomToast.show("识别失败，请重新识别！");
                    isFaceback=false;
                }
            });
        } catch (FileNotFoundException e1) {
            if(dialog!=null && dialog.isShowing()) dialog.dismiss();
            e1.printStackTrace();
            CustomToast.show("识别失败，请重新识别！");
            isFaceback=false;
        }
    }

    /**
     * 输入密码提示弹出框
     */
    private void customDialog_input(final String message, String username) {
        final AlertDialog builder = new AlertDialog.Builder(BindIDcardActivity.this).create();
        builder.setView((BindIDcardActivity.this).getLayoutInflater()
                .inflate(R.layout.dialog_input,null));
        View view = LayoutInflater.from(BindIDcardActivity.this)
                .inflate(R.layout.dialog_input, null);

        TextView tv_message_update= (TextView) view.findViewById(R.id.tv_message_update);
        tv_message_update.setText(message);
        TextView cancel= (TextView) view.findViewById(R.id.tv_pwd_cancel);
        final TextView ok= (TextView) view.findViewById(R.id.tv_pwd_confirm);
        final EditText et_name= (EditText) view.findViewById(R.id.et_name_update);
        et_name.setText(username);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(message.equals("修改姓名")){
                    if (et_name.getText().toString().trim().isEmpty()){
                        CustomToast.show("请输入姓名");
                    }else{
                        name=et_name.getText().toString().trim();
                        tv_name_idcard.setText(name);
                        ok.setFocusable(false);
                        builder.dismiss();
                    }
                }else{
                    if (et_name.getText().toString().trim().isEmpty()){
                        CustomToast.show("请输入地址");
                    }else{
                        address=et_name.getText().toString().trim();
                        tv_address_idcard.setText(address);
                        ok.setFocusable(false);
                        builder.dismiss();
                    }
                }

            }
        });

        builder.show();
        builder.setCancelable(true);
        builder.getWindow().setContentView(view);
        builder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private PopupWindow popupWindo;
    public void showPopupWindow()
    {
        if (popupWindo!=null){
            popupWindo=null;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.pw_dialog4, null);
        Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button)view.findViewById(R.id.btn_ok);

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindo.dismiss();
                final ArrayList<File> uploadPicList=new ArrayList<>();
                File zmfile=new File(zmpath);
                uploadPicList.add(zmfile);
                File fmfile=new File(fmpath);
                uploadPicList.add(fmfile);
                File facefile=new File(facepath);
                uploadPicList.add(facefile);

                requestReadPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        if (uploadPicList.size()>0){
                            totalSize = FileUtils.getFilesSize(uploadPicList);
                        }
                        mProcessingDialog=new CustomProgressDialog(BindIDcardActivity.this,"文件上传中...");
                        mProcessingDialog.show();
                        UploadZM(new File(zmpath));
                    }
                });
            }
        });
        Display display = getWindowManager().getDefaultDisplay();
        popupWindo = new PopupWindow(view, display.getWidth(), display.getHeight(), true);
        popupWindo.setOutsideTouchable(true);
        popupWindo.setFocusable(true);
        fitPopupWindowOverStatusBar(popupWindo);
        popupWindo.setBackgroundDrawable(new ColorDrawable());
        popupWindo.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}

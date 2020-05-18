package com.glavesoft.pawnuser.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.IdRes;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.FileUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.UserInfo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MyinfoActivity extends BaseActivity implements View.OnClickListener
{
    private ImageView tx_image;
    private TextView myinfo_phone;
    private Context context = this;
    private Bitmap photoBitmap;
    private EditText myinfo_username;
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private String  newname="",newimg="";
    private UserInfo userInfo;
    HttpParams addparam=new HttpParams();
    private String token,userid;
    private LinearLayout ll_myinfo_address,ll_myinfo_idcard;
    private TextView tv_myinfo_bd,tv_myinfo_ybd;

    private boolean isSubmit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_myinfo);
        super.onCreate(savedInstanceState);
        userInfo=LocalData.getInstance().getUserInfo();
        initView();
    }

    private void initView()
    {
        setTitleBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newname=myinfo_username.getText().toString();
                if(newname.length()==0){
                    CustomToast.show("请输入昵称");
                    return;
                }

                isSubmit=false;

                if(newname.length()>0&&(!newname.equals(LocalData.getInstance().getUserInfo().getNickName()))){
                    //checkName(newname);
                    addparam.put("nickName",newname);
                    isSubmit=true;
                }

                if(!newimg.equals(LocalData.getInstance().getUserInfo().getHeadImg())){
                    addparam.put("headImg",newimg);
                    isSubmit=true;
                }

                if(isSubmit){
                    changgeInfo();
                }else{
                    finish();
                }

            }
        });
        setTitleName("个人资料");
        setTitleNameEn(R.mipmap.personal_data);

        tx_image = (ImageView) findViewById(R.id.myinfo_tx_image);
        myinfo_username = (EditText) findViewById(R.id.myinfo_username);
        myinfo_phone = (TextView) findViewById(R.id.myinfo_phone);

        ll_myinfo_idcard = (LinearLayout) findViewById(R.id.ll_myinfo_idcard);
        ll_myinfo_address = (LinearLayout) findViewById(R.id.ll_myinfo_address);
        tv_myinfo_bd = (TextView) findViewById(R.id.tv_myinfo_bd);
        tv_myinfo_ybd = (TextView) findViewById(R.id.tv_myinfo_ybd);

        getImageLoader().displayImage(BaseConstant.Image_URL + userInfo.getHeadImg(), tx_image, getImageLoaderHeadOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {}
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                RandomTx();
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {}

            @Override
            public void onLoadingCancelled(String imageUri, View view) {}
        });

        token=userInfo.getToken();
        userid=userInfo.getUserid();
        myinfo_username.setText(userInfo.getNickName());
        myinfo_phone.setText(userInfo.getAccount());
        tx_image.setOnClickListener(this);
        newname=userInfo.getNickName();
        newimg=userInfo.getHeadImg();

//        if(userInfo.getIsBind().equals("1")){
//            tv_myinfo_ybd.setVisibility(View.VISIBLE);
//            tv_myinfo_bd.setVisibility(View.GONE);
//        }else{
//            tv_myinfo_ybd.setVisibility(View.GONE);
//            tv_myinfo_bd.setVisibility(View.VISIBLE);
//        }

        ll_myinfo_idcard.setOnClickListener(this);
        ll_myinfo_address.setOnClickListener(this);
        tv_myinfo_bd.setOnClickListener(this);

    }

    private void RandomTx(){
        Random rand = new Random();
        int randNum = rand.nextInt(4);
        if (randNum==0){
            tx_image.setImageResource(R.mipmap.mryi);
        }else  if (randNum==1){
            tx_image.setImageResource(R.mipmap.mrer);
        }else  if (randNum==2){
            tx_image.setImageResource(R.mipmap.mrsan);
        }else  if (randNum==3){
            tx_image.setImageResource(R.mipmap.mrsi);
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent1 =new Intent();
        switch (v.getId())
        {
            case R.id.myinfo_tx_image:
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                //showPopup();
                PhotoPickerIntent intent = new PhotoPickerIntent(MyinfoActivity.this);
                intent.setSelectModel(SelectModel.SINGLE);
                intent.setShowCarema(true);
                startActivityForResult(intent, REQUEST_CAMERA_CODE);
                break;
            case R.id.pw_btn_photograph:// 去拍照
//                pop.dismiss();
//                try {
//                    if(captureManager == null){
//                        captureManager = new ImageCaptureManager(MyinfoActivity.this);
//                    }
//                    Intent intent = captureManager.dispatchTakePictureIntent();
//                    startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
//                } catch (IOException e) {
//                    Toast.makeText(MyinfoActivity.this, com.foamtrace.photopicker.R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
                break;
            case R.id.pw_btn_album:// 去手机图库
//                pop.dismiss();
//                PhotoPickerIntent intent = new PhotoPickerIntent(MyinfoActivity.this);
//                intent.setSelectModel(SelectModel.SINGLE);
//                intent.setShowCarema(true);
//                startActivityForResult(intent, REQUEST_CAMERA_CODE);
                break;
            case R.id.ll_myinfo_address:// 地址
                intent1.setClass(MyinfoActivity.this,AddressActivity.class);
                intent1.putExtra("type","showlist");
                startActivity(intent1);
                break;
            case R.id.tv_myinfo_bd:// 去绑定
                intent1.setClass(MyinfoActivity.this,IdentitycardActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_myinfo_idcard:
                if(userInfo.getIsBind().equals("1")){
                    startActivity(new Intent(MyinfoActivity.this, IdCardActivity.class));
                }else{
                    startActivity(new Intent(MyinfoActivity.this, BindIDcardActivity.class));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
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
        if (imagePaths == null) {
            imagePaths = new ArrayList<>();
        }
        comImg(paths.get(0));
    }

    // 压缩
    private void comImg(String path)
    {
        try
        {
            File file = FileUtils.compressImg(new File(path), FileUtils.FILE_IMAGE_MAXSIZE);
//            photoBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (file != null)
            {
                Glide.with(MyinfoActivity.this).load(file.getAbsolutePath()).into(tx_image);
                UploadAvatar(file);
            } else
            {
                CustomToast.show("图片不存在，请选择其他图片！");
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

//    //上传头像
    public void UploadAvatar( File file){
        getlDialog().show();
        String url=BaseConstant.UploadAvatar_URL;
        OkGo.<DataResult<ImageInfo>>post(url)
                .params("file", file)
                .execute(new JsonCallback<DataResult<ImageInfo>>() {
                    @Override
                    public void onStart(Request<DataResult<ImageInfo>, ? extends Request> request) {

                    }
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<ImageInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.msg_error));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK){

                            if(response.body().getData()!=null) {
                                newimg=response.body().getData().getId();
                            }

                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<ImageInfo>> response) {
                        getlDialog().dismiss();
                        CustomToast.show("上传失败,请重新上传");
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }
                });
    }

    private PopupWindow pop;
    protected void showPopup()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.pw_photo, null);
        Button pw_btn_photograph = (Button) view.findViewById(R.id.pw_btn_photograph);
        Button pw_btn_album = (Button) view.findViewById(R.id.pw_btn_album);
        Button pw_btn_cancle = (Button) view.findViewById(R.id.pw_btn_cancle);

        if (pop == null)
        {
            pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
            // 设置点击窗口外边窗口消失
            pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_trans));
            pop.setOutsideTouchable(true);
            // 设置此参数获得焦点，否则无法点击
            pop.setFocusable(true);
            pop.setAnimationStyle(R.style.popwin_anim_style);
        }

        if (!pop.isShowing())
        {
            pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
        pw_btn_photograph.setOnClickListener(this);
        pw_btn_album.setOnClickListener(this);
        pw_btn_cancle.setOnClickListener(this);
    }

    public  void onDestroy(){
        super.onDestroy();
        if(photoBitmap != null && !photoBitmap.isRecycled()){
            photoBitmap.recycle();
            System.gc();
        }
    }


    //更新资料
    private void changgeInfo(){
        getlDialog().show();
        String url=BaseConstant.getApiPostUrl("home/editUser");
        addparam.put("token",userInfo.getToken());
        OkGo.<DataResult<UserInfo>>post(url)
                .params(addparam)
                .execute(new JsonCallback<DataResult<UserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            response.body().getData().setToken(token);
                            response.body().getData().setUserid(userid);
                            setUserAndFile(response.body().getData());
                            finish();
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<UserInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            newname=myinfo_username.getText().toString();
            if(newname.length()==0){
                CustomToast.show("请输入昵称");
                return true;
            }
            isSubmit=false;
            if(newname.length()>0&&(!newname.equals(LocalData.getInstance().getUserInfo().getNickName()))){
                //checkName(newname);
                addparam.put("nickName",newname);
                isSubmit=true;
            }

            if(!newimg.equals(LocalData.getInstance().getUserInfo().getHeadImg())){
                addparam.put("headImg",newimg);
                isSubmit=true;
            }

            if(isSubmit){
                changgeInfo();
            }else{
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}

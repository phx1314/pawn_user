package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.util.FileUtils;
import com.glavesoft.view.CustomToast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author 严光
 * @date: 2017/10/20
 * @company:常州宝丰
 */
public class IdentitycardActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_zm_addpic,iv_fm_addpic,iv_sc_addpic;
    private ImageView iv_zm_showpic,iv_fm_showpic,iv_sc_showpic;
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private String type="";
    private String zmpath="",fmpath="",scpath="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identitycard);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("上传身份证");

        iv_zm_addpic=(ImageView) findViewById(R.id.iv_zm_addpic);
        iv_fm_addpic=(ImageView) findViewById(R.id.iv_fm_addpic);
        iv_sc_addpic=(ImageView) findViewById(R.id.iv_sc_addpic);
        iv_zm_showpic=(ImageView) findViewById(R.id.iv_zm_showpic);
        iv_fm_showpic=(ImageView) findViewById(R.id.iv_fm_showpic);
        iv_sc_showpic=(ImageView) findViewById(R.id.iv_sc_showpic);

        iv_zm_addpic.setOnClickListener(this);
        iv_fm_addpic.setOnClickListener(this);
        iv_sc_addpic.setOnClickListener(this);
        iv_zm_showpic.setOnClickListener(this);
        iv_fm_showpic.setOnClickListener(this);
        iv_sc_showpic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        PhotoPickerIntent intent;
        switch (v.getId())
        {
            case R.id.iv_zm_addpic:
                type="zm";
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(IdentitycardActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                });

                break;
            case R.id.iv_zm_showpic:
                type="zm";
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(IdentitycardActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                });

                break;
            case R.id.iv_fm_addpic:
                type="fm";
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(IdentitycardActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                });

                break;
            case R.id.iv_fm_showpic:
                type="fm";
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(IdentitycardActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                });

                break;
            case R.id.iv_sc_addpic:
                type="sc";
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(IdentitycardActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                });

                break;
            case R.id.iv_sc_showpic:
                type="sc";
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(IdentitycardActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                });

                break;
        }
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
//                List<File> files=new ArrayList<>();
//                files.add(file);
//                UploadAvatar(files);
               if(type.equals("zm")){
                   zmpath=file.getAbsolutePath();
                   iv_zm_addpic.setVisibility(View.GONE);
                    iv_zm_showpic.setVisibility(View.VISIBLE);
                   Glide.with(IdentitycardActivity.this).load(zmpath).into( iv_zm_showpic);
                }else if(type.equals("fm")){
                   fmpath=file.getAbsolutePath();
                   iv_fm_addpic.setVisibility(View.GONE);
                   iv_fm_showpic.setVisibility(View.VISIBLE);
                   Glide.with(IdentitycardActivity.this).load(fmpath).into( iv_fm_showpic);
                }else if(type.equals("sc")){
                   scpath=file.getAbsolutePath();
                   iv_sc_addpic.setVisibility(View.GONE);
                   iv_sc_showpic.setVisibility(View.VISIBLE);
                   Glide.with(IdentitycardActivity.this).load(scpath).into( iv_sc_showpic);
               }
            } else
            {
                CustomToast.show("获取图片失败");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}

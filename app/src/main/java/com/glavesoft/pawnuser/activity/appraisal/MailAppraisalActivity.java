package com.glavesoft.pawnuser.activity.appraisal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.activity.main.ImageActivity;
import com.glavesoft.pawnuser.activity.video.VideoActivity;
import com.glavesoft.pawnuser.adapter.PicAdapter;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.BxAddressInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.MyAppraisalInfo;
import com.glavesoft.pawnuser.mod.OnlineIdentificationInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.FileUtils;
import com.glavesoft.util.ImageUtils;
import com.glavesoft.view.CustomProgressDialog;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

/**
 * @author 严光
 * @date: 2017/10/25
 * @company:常州宝丰
 */
public class MailAppraisalActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_submit_mail;
    private static final int REQUEST_VIDEO_CODE = 10;//拍摄视频
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_CAMERA_CODE1 = 12;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private static final int REQUEST_MEDIA = 100;//本地视频
    private ArrayList<String> urlList = new ArrayList<String>();
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类

    private String videopath="";
    private String VideoPreviewPicture="";
    private Bitmap bmp;
    private String type;
    private String onepath="",twopath="";
    private ImageView iv_one_mail,iv_two_mail;
    private GridViewForNoScroll gv_pics_mail;
    private PicAdapter adapter;
    //视频
    private ImageView iv_video;
    private ImageView iv_video_add;
    private ImageView iv_video_play;
    private ImageView iv_video_del;

    private TextView tv_address_mail,tv_lxr_mail,tv_phone_mail;

    private String submittype;
    private String pawnCateCode;
    private MyAppraisalInfo myAppraisalInfo;

    private LinearLayout ll_pic_mail,ll_pics_mail;
    private ImageView iv_pic_mail;
    private TextView tv_name_mail;

    private String video="";
    private String images="";

    private EditText et_wldh_mail,et_money_mail;

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
        setContentView(R.layout.activity_mail_appraisal);
        getData();
        initView();
    }

    private void getData() {
        submittype=getIntent().getStringExtra("type");
        if(submittype.equals("online")){
            myAppraisalInfo=(MyAppraisalInfo)getIntent().getSerializableExtra("MyAppraisalInfo");
        }else{
            pawnCateCode=getIntent().getStringExtra("pawnCateCode");
        }

    }

    private void initView() {
        setTitleBack();
        setTitleName("填写物流信息");
        setTitleNameEn(R.mipmap.fill_in_information);

        et_wldh_mail = (EditText) findViewById(R.id.et_wldh_mail);
        et_money_mail = (EditText) findViewById(R.id.et_money_mail);
        //价格输入限制
        et_money_mail.setFilters(new InputFilter[] { new BaseConstant.EditInputFilter(et_money_mail)});

        tv_address_mail  = (TextView) findViewById(R.id.tv_address_mail);
        tv_lxr_mail  = (TextView) findViewById(R.id.tv_lxr_mail);
        tv_phone_mail  = (TextView) findViewById(R.id.tv_phone_mail);

        ll_pic_mail = (LinearLayout) findViewById(R.id.ll_pic_mail);
        ll_pics_mail = (LinearLayout) findViewById(R.id.ll_pics_mail);

        iv_pic_mail  = (ImageView) findViewById(R.id.iv_pic_mail);
        tv_name_mail  = (TextView) findViewById(R.id.tv_name_mail);

        if(submittype.equals("online")){
            ll_pic_mail.setVisibility(View.VISIBLE);
            ll_pics_mail.setVisibility(View.GONE);
            tv_name_mail.setText(myAppraisalInfo.getTitle());
            if(!myAppraisalInfo.getImage().equals("")){
                List<String> list1= Arrays.asList(myAppraisalInfo.getImage().split(","));
                getImageLoader().displayImage(BaseConstant.Image_URL + list1.get(0),iv_pic_mail,getImageLoaderOptions());
            }


        }else{
            ll_pic_mail.setVisibility(View.GONE);
            ll_pics_mail.setVisibility(View.VISIBLE);
        }

        iv_one_mail = (ImageView) findViewById(R.id.iv_one_mail);
        iv_two_mail = (ImageView) findViewById(R.id.iv_two_mail);
        iv_video = (ImageView) findViewById(R.id.iv_video);
        iv_video_add = (ImageView) findViewById(R.id.iv_video_add);
        iv_video_play = (ImageView) findViewById(R.id.iv_video_play);
        iv_video_del = (ImageView) findViewById(R.id.iv_video_del);
        iv_one_mail.setOnClickListener(this);
        iv_two_mail.setOnClickListener(this);
        iv_video_add.setOnClickListener(this);
        iv_video_play.setOnClickListener(this);
        iv_video_del.setOnClickListener(this);

        ll_submit_mail  = (LinearLayout) findViewById(R.id.ll_submit_mail);
        ll_submit_mail.setOnClickListener(this);

        gv_pics_mail  = (GridViewForNoScroll) findViewById(R.id.gv_pics_mail);
        adapter = new PicAdapter(MailAppraisalActivity.this,urlList,imagePaths);
        gv_pics_mail.setAdapter(adapter);
        gv_pics_mail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                if (position == adapter.geturlLists().size()) {
                    requestReadCameraPermissions(new CheckPermListener() {
                        @Override
                        public void superREADPermission() {
                            PhotoPickerIntent intent = new PhotoPickerIntent(MailAppraisalActivity.this);
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

        bxAddress();
    }

    @Override
    public void onClick(View v)
    {
        //PhotoPickerIntent intent;
        Intent intent;
        switch (v.getId())
        {
            case R.id.iv_one_mail:
//                type="one";
//                intent = new PhotoPickerIntent(MailAppraisalActivity.this);
//                intent.setSelectModel(SelectModel.SINGLE);
//                intent.setShowCarema(true);
//                startActivityForResult(intent, REQUEST_CAMERA_CODE1);
                intent=new Intent();
                intent.setClass(MailAppraisalActivity.this,ImageActivity.class);
                intent.putExtra("url","1");
                startActivity(intent);
                break;
            case R.id.iv_two_mail:
//                type="two";
//                intent = new PhotoPickerIntent(MailAppraisalActivity.this);
//                intent.setSelectModel(SelectModel.SINGLE);
//                intent.setShowCarema(true);
//                startActivityForResult(intent, REQUEST_CAMERA_CODE1);
                intent=new Intent();
                intent.setClass(MailAppraisalActivity.this,ImageActivity.class);
                intent.putExtra("url","2");
                startActivity(intent);
                break;
            case R.id.iv_video_add:
                getVideo();
                break;
            case R.id.iv_video_del://删除视频
                videopath="";
                iv_video_add.setVisibility(View.VISIBLE);
                iv_video.setVisibility(View.GONE);
                iv_video_play.setVisibility(View.GONE);
                iv_video_del.setVisibility(View.GONE);
                break;
            case R.id.iv_video_play://播放
                Uri uri=Uri.parse(videopath);
                JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class,uri.toString(), "");
                break;
            case R.id.ll_submit_mail:
                gotoSend();
                break;
        }
    }

    private void getVideo() {
        showVideoPopupWindow(MailAppraisalActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCAMERAPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        //拍摄
                        try {
                            if (!CommonUtils.getPermisein(MailAppraisalActivity.this, "android.permission.CAMERA")) {
                                Toast.makeText(MailAppraisalActivity.this, "您的相机不可用，请检查相机功能是否禁用", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (!CommonUtils.isVoicePermission()) {
                                Toast.makeText(MailAppraisalActivity.this, "您的录音不可用，请检查录音功能是否禁用", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (Exception e) {

                        }
                        Intent intent1=new Intent(MailAppraisalActivity.this, VideoActivity.class);
                        startActivityForResult(intent1,REQUEST_VIDEO_CODE);
                        getPopupWindow().dismiss();
                    }
                });

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReadPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        //本机
                        MediaOptions.Builder builder = new MediaOptions.Builder();
                        MediaOptions options = builder.selectVideo().canSelectMultiVideo(false).build();
                        if (options != null) {
                            MediaPickerActivity.open(MailAppraisalActivity.this, REQUEST_MEDIA, options);
                        }
                        getPopupWindow().dismiss();
                    }
                });

            }
        });
    }

    private void gotoSend() {
        if(submittype.equals("mail")){
            if(adapter.geturlLists().size()==0){
                CustomToast.show("请添加藏品图片");
                return;
            }

            if(adapter.geturlLists().size()<3){
                CustomToast.show("藏品图片最少上传三张");
                return;
            }
        }

        if(videopath.equals("")){
            CustomToast.show("请上传打包视频");
            return;
        }

        if(et_wldh_mail.getText().toString().trim().length()==0){
            CustomToast.show("请输入寄送的物流单号");
            return;
        }

        if(et_money_mail.getText().toString().trim().length()==0){
            CustomToast.show("请输入保价金额");
            return;
        }

        final ArrayList<File> uploadPicList=new ArrayList<>();
        File Videofile=new File(videopath);
        uploadPicList.add(Videofile);
        for (int i=0;i<adapter.geturlLists().size();i++){
            uploadPicList.add(new File(adapter.geturlLists().get(i)));
        }

        requestReadPermissions(new CheckPermListener() {
            @Override
            public void superREADPermission() {
                if (uploadPicList.size()>0){
                    totalSize = FileUtils.getFilesSize(uploadPicList);
                }
                mProcessingDialog=new CustomProgressDialog(MailAppraisalActivity.this,"文件上传中...");
                mProcessingDialog.show();
                UploadVideo(new File(videopath));
            }
        });
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
                case REQUEST_CAMERA_CODE1:
                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
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
                case REQUEST_MEDIA:
                    final ArrayList<File> uploadPicList=new ArrayList<>();
                    File Videofile=new File(MediaPickerActivity.getMediaItemSelected(data).get(0).getPathOrigin(this));
                    uploadPicList.add(Videofile);
                    totalSize = FileUtils.getFilesSize(uploadPicList);
                    if (totalSize<=BaseConstant.maxSize){
                        videopath=MediaPickerActivity.getMediaItemSelected(data).get(0).getPathOrigin(this);
                        Video();//视频处理
                    }else{
                        CustomToast.show(getString(R.string.maxSize));
                    }
                    break;

            }
        }else if(resultCode == REQUEST_VIDEO_CODE){
            switch (requestCode) {
                case REQUEST_VIDEO_CODE:
                    videopath=data.getStringExtra("path");
                    Video();//视频处理
                    break;
            }
        }
    }

    private void loadimagePaths(ArrayList<String> paths) {

       for (int i=0;i<paths.size();i++){
           System.out.println(paths.get(i));
       }
        imagePaths= new ArrayList<>();
        urlList= new ArrayList<>();
        urlList.addAll(paths);
        imagePaths.addAll(paths);
        adapter = new PicAdapter(MailAppraisalActivity.this,urlList,imagePaths);
        gv_pics_mail.setAdapter(adapter);
//        comImg(paths);
    }
    // 压缩
    public void comImg(final ArrayList<String> paths){
        new AsyncTask<Void,Void,Void>(){
            private ProgressDialog dialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(MailAppraisalActivity.this,"","图片处理中...");
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
                adapter = new PicAdapter(MailAppraisalActivity.this,urlList,imagePaths);
                gv_pics_mail.setAdapter(adapter);
            }
        }.execute();
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
                if(type.equals("one")){
                    onepath=file.getAbsolutePath();
                    Glide.with(MailAppraisalActivity.this).load(onepath).into(iv_one_mail);
                }else if(type.equals("two")){
                    twopath=file.getAbsolutePath();
                    Glide.with(MailAppraisalActivity.this).load(twopath).into(iv_two_mail);
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

    //视频处理
    public void Video(){
        new AsyncTask<Void,Void,Void>(){
            private ProgressDialog dialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(MailAppraisalActivity.this,"","视频处理中...");
            }
            @Override
            protected Void doInBackground(Void... params) {
                // 获取第一个关键帧
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(videopath);
                bmp = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                try {
                    VideoPreviewPicture=FileUtils.saveImg(bmp);
                    if(bmp != null && !bmp.isRecycled()){
                        bmp.recycle();
                        bmp = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(dialog!=null && dialog.isShowing()) dialog.dismiss();
                iv_video_add.setVisibility(View.GONE);
                iv_video.setVisibility(View.VISIBLE);
                iv_video_play.setVisibility(View.VISIBLE);
                iv_video_del.setVisibility(View.VISIBLE);
                Glide.with(MailAppraisalActivity.this).load(VideoPreviewPicture).into(iv_video);
            }
        }.execute();
    }


    public  void onDestroy(){
        super.onDestroy();
        if(bmp != null && !bmp.isRecycled()){
            bmp.recycle();
            bmp = null;
        }
        System.gc();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    private void bxAddress()
    {
        String token= LocalData.getInstance().getUserInfo().getToken();
        getlDialog().show();
        Map<String, String> param = VolleyUtil.getRequestMap(MailAppraisalActivity.this);
        param.put("token",token);

        java.lang.reflect.Type classtype = new TypeToken<DataResult<BxAddressInfo>>()
        {
        }.getType();

        VolleyUtil.postObjectApi(BaseConstant.getApiPostUrl("userGoods/bxAddress"), param, classtype, new ResponseListener<DataResult<BxAddressInfo>>()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                getlDialog().dismiss();

                showVolleyError(error);
            }

            @Override
            public void onResponse(DataResult<BxAddressInfo> response)
            {
                getlDialog().dismiss();

                if (response == null)
                {
                    CustomToast.show(getString(R.string.http_request_fail));
                    return;
                }
                if (DataResult.RESULT_OK_ZERO == response.getErrorCode())
                {
                    tv_address_mail.setText("公司地址："+response.getData().getAddress());
                    tv_lxr_mail.setText("联系人："+response.getData().getName());
                    tv_phone_mail.setText("联系电话："+response.getData().getPhone());

                }else if (DataResult.RESULT_102 == response.getErrorCode())
                {
                    toLogin();
                }else
                {
                    CustomToast.show(response.getErrorMsg());
                }
            }
        });
    }

    private void UploadVideo(File file){
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

                                video=response.body().getData().getId();
                                if(submittype.equals("mail")){
                                    images="";
                                    UploadPic(new File(adapter.geturlLists().get(0)));
                                }else{
                                    mProcessingDialog.dismiss();
                                    gotoPost();
                                }
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

    //上传图片
    int startPos_PicList = 0;
    public void UploadPic( File file){
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

                            if(response.body().getData()!=null) {
                                if(images.equals("")){
                                    images =response.body().getData().getId();
                                }else{
                                    images=images+","+response.body().getData().getId();
                                }

                                startPos_PicList++;
                                if (startPos_PicList < adapter.geturlLists().size()) {
                                    UploadPic(new File(adapter.geturlLists().get(startPos_PicList)));
                                } else {
                                    mProcessingDialog.dismiss();
                                    gotoPost();
                                }
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


    //去邮寄
    private void gotoPost()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/gotoPost");
        HttpParams param=new HttpParams();
        param.put("token",token);
        if(submittype.equals("online")){
            param.put("id",myAppraisalInfo.getId());
        }else{
            param.put("pawnCateCode",pawnCateCode);
            param.put("images",images);
        }
        param.put("pid",et_wldh_mail.getText().toString().trim());
        param.put("video",video);
        param.put("sfProtectPrice",et_money_mail.getText().toString().trim());
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
                            Intent intent=new Intent("AppraisalRefresh");
                            sendBroadcast(intent);
                            CustomToast.show("发送成功");
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

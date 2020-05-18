package com.glavesoft.pawnuser.activity.appraisal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.glavesoft.pawnuser.activity.video.VideoActivity;
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
import com.glavesoft.util.ImageUtils;
import com.glavesoft.view.CustomProgressDialog;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

/**
 * @author 严光
 * @date: 2017/10/31
 * @company:常州宝丰
 */
public class OtherActivity extends BaseActivity implements View.OnClickListener{
    private static final int REQUEST_VIDEO_CODE = 10;
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private static final int REQUEST_MEDIA = 100;//本地视频
    private ArrayList<String> urlList = new ArrayList<String>();
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类

    private GridViewForNoScroll gv_pics_evaluationinfo;
    private LinearLayout ll_submit_other;
    PicAdapter adapter;

    private EditText et_require_other;

    //视频
    private ImageView iv_video;
    private ImageView iv_video_add;
    private ImageView iv_video_play;
    private ImageView iv_video_del;
    private String videopath="";
    private String VideoPreviewPicture="";
    private Bitmap bmp;

    private String video;
    private String picIds="";
    private ArrayList<OnlineIdentificationInfo> Jesonlist=new ArrayList<>();
    private String code;
    private TextView tv_pic_ck;
    private boolean isfirst=true;

    //总大小
    private long totalSize;
    //当前总上传大小
    private long UploadTotalSize=0;
    //之前上传大小
    private long beforeUploadSize=0;
    //当前单个上传大小
    private long currentUploadSize=0;
    private CustomProgressDialog mProcessingDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        code=getIntent().getStringExtra("code");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("填写鉴定估价资料");
        setTitleNameEn(R.mipmap.fill_in_information);

        tv_pic_ck=(TextView)findViewById(R.id.tv_pic_ck);
        tv_pic_ck.setVisibility(View.VISIBLE);
        tv_pic_ck.setOnClickListener(this);

        iv_video = (ImageView) findViewById(R.id.iv_video);
        iv_video_add = (ImageView) findViewById(R.id.iv_video_add);
        iv_video_play = (ImageView) findViewById(R.id.iv_video_play);
        iv_video_del = (ImageView) findViewById(R.id.iv_video_del);
        iv_video_add.setOnClickListener(this);
        iv_video_play.setOnClickListener(this);
        iv_video_del.setOnClickListener(this);

        et_require_other=(EditText) findViewById(R.id.et_require_other);

        gv_pics_evaluationinfo=(GridViewForNoScroll)findViewById(R.id.gv_pics_evaluationinfo);

        ll_submit_other=(LinearLayout) findViewById(R.id.ll_submit_other);
        ll_submit_other.setOnClickListener(this);

        adapter = new PicAdapter(OtherActivity.this,urlList,imagePaths);
        gv_pics_evaluationinfo.setAdapter(adapter);
        gv_pics_evaluationinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(isfirst){
                    isfirst=false;
                    Intent intent=new Intent();
                    intent.setClass(OtherActivity.this,ReferenceActivity.class);
                    intent.putExtra("titleName","照片参考");
                    intent.putExtra("url",BaseConstant.BaseURL+"/m/pawn/getReference?code="+code);
                    startActivity(intent);
                }else{
                    if (position == adapter.geturlLists().size()) {
                        requestReadCameraPermissions(new CheckPermListener() {
                            @Override
                            public void superREADPermission() {
                                PhotoPickerIntent intent = new PhotoPickerIntent(OtherActivity.this);
                                intent.setSelectModel(SelectModel.MULTI);
                                intent.setShowCarema(true);
                                intent.setMaxTotal(9); // 最多选择照片数量，默认为9
                                intent.setSelectedPaths(adapter.getimagePaths()); // 已选中的照片地址， 用于回显选中状态
                                startActivityForResult(intent, REQUEST_CAMERA_CODE);
                            }
                        });

                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent();
        switch (v.getId())
        {
            case R.id.tv_pic_ck://照片参考
                intent.setClass(OtherActivity.this,ReferenceActivity.class);
                intent.putExtra("titleName","照片参考");
                intent.putExtra("url",BaseConstant.BaseURL+"/m/pawn/getReference?code="+code);
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
            case R.id.ll_submit_other:
                gotoSend();
                break;
        }
    }

    private void getVideo() {
        showVideoPopupWindow(OtherActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCAMERAPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        //拍摄
                        try {
                            if (!CommonUtils.getPermisein(OtherActivity.this, "android.permission.CAMERA")) {
                                Toast.makeText(OtherActivity.this, "您的相机不可用，请检查相机功能是否禁用", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (!CommonUtils.isVoicePermission()) {
                                Toast.makeText(OtherActivity.this, "您的录音不可用，请检查录音功能是否禁用", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (Exception e) {

                        }
                        Intent intent1=new Intent(OtherActivity.this, VideoActivity.class);
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
                            MediaPickerActivity.open(OtherActivity.this, REQUEST_MEDIA, options);
                        }
                        getPopupWindow().dismiss();
                    }
                });

            }
        });
    }

    private void gotoSend() {

        if(adapter.geturlLists().size()==0){
            CustomToast.show("请添加藏品图片");
            return;
        }

        if(adapter.geturlLists().size()<3){
            CustomToast.show("藏品图片最少上传三张");
            return;
        }

        if(videopath.equals("")){
            CustomToast.show("请上传藏品视频");
            return;
        }

        if(et_require_other.getText().toString().trim().length()==0){
            CustomToast.show("请输入藏品描述及鉴定要求");
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
                mProcessingDialog=new CustomProgressDialog(OtherActivity.this,"文件上传中...");
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
                case REQUEST_MEDIA://本地视频
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
        imagePaths= new ArrayList<>();
        urlList= new ArrayList<>();
        urlList.addAll(paths);
        imagePaths.addAll(paths);
        adapter = new PicAdapter(OtherActivity.this,urlList,imagePaths);
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
                dialog = ProgressDialog.show(OtherActivity.this,"","图片处理中...");
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
                adapter = new PicAdapter(OtherActivity.this,urlList,imagePaths);
                gv_pics_evaluationinfo.setAdapter(adapter);
            }
        }.execute();
    }

    //视频处理
    public void Video(){
        new AsyncTask<Void,Void,Void>(){
            private ProgressDialog dialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(OtherActivity.this,"","视频处理中...");
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
                Glide.with(OtherActivity.this).load(VideoPreviewPicture).into(iv_video);
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

                                picIds="";
                                UploadPic(new File(adapter.geturlLists().get(0)));

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
                                if (picIds.equals("")) {
                                    picIds = response.body().getData().getId();
                                } else {
                                    picIds = picIds + "," + response.body().getData().getId();
                                }

                                startPos_PicList++;
                                if (startPos_PicList < adapter.geturlLists().size()) {
                                    UploadPic(new File(adapter.geturlLists().get(startPos_PicList)));
                                } else {
                                    mProcessingDialog.dismiss();

                                    OnlineIdentificationInfo info =new OnlineIdentificationInfo();
                                    info.setName("藏品照片");
                                    info.setContent(picIds);
                                    info.setContentType("3");
                                    Jesonlist.add(info);
                                    OnlineIdentificationInfo info1 =new OnlineIdentificationInfo();
                                    info1.setName("藏品视频");
                                    info1.setContent(video);
                                    info1.setContentType("4");
                                    Jesonlist.add(info1);
                                    OnlineIdentificationInfo info2 =new OnlineIdentificationInfo();
                                    info2.setName("藏品描述及鉴定要求");
                                    info2.setContent(et_require_other.getText().toString().trim());
                                    info2.setContentType("1");
                                    Jesonlist.add(info2);

                                    gotoAuthGood();
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


    //在线鉴定
    private void gotoAuthGood()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/gotoAuthGood");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("images",picIds);
        param.put("video",video);
        param.put("authenticateRequire",et_require_other.getText().toString().trim());
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
                            Intent intent=new Intent("AppraisalRefresh");
                            sendBroadcast(intent);
                            CustomToast.show(getString(R.string.toast_appraisal));
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

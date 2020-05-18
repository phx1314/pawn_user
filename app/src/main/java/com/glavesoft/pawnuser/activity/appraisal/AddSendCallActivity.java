package com.glavesoft.pawnuser.activity.appraisal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.video.VideoActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.UpdatepicAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.SendCallGoodInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.FileUtils;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.util.StringUtils;
import com.glavesoft.view.CustomProgressDialog;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

public class AddSendCallActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_memo)
    TextView tvMemo;
    @BindView(R.id.tv_ykj)
    TextView tvYkj;
    @BindView(R.id.tv_jdj)
    TextView tvJdj;
//    @BindView(R.id.tv_brand_jewellery)
//    TextView tvBrandJewellery;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.ll_ykj)
    LinearLayout llYkj;
    @BindView(R.id.ll_jdj)
    LinearLayout llJdj;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    int selectTv = 0;//0 一口价  1鉴定价
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.ll_select_type)
    LinearLayout llSelectType;
//    @BindView(R.id.hsv_video)
//    HorizontalScrollView hsvVideo;
//    @BindView(R.id.iv_video)
//    ImageView ivVideo;
    @BindView(R.id.tv_auth_price)
    TextView tvAuthPrice;
    @BindView(R.id.tv_auth_result)
    TextView tvAuthResult;
//    @BindView(R.id.hsv_update_imgs)
//    HorizontalScrollView hsvUpdateImgs;
//    @BindView(R.id.hsv_update_video)
//    HorizontalScrollView hsvUpdateVideo;

    @BindView(R.id.gv_pics)
    GridViewForNoScroll mGvPics;

    private EditText et_comment_content;
    private TextView tv_submit;

    private ArrayList<String> typelist = new ArrayList<>();

    private SendCallGoodInfo sendCallGoodInfo;
    private String sendCallId;
    private String sendCalltype;

    private static final int REQUEST_CAMERA_CODE = 11;
    private UpdatepicAdapter adapter;
    private ArrayList<String> urlList = new ArrayList<String>();
    private ArrayList<String> imagePaths= new ArrayList<String>();
    private ArrayList<String> picList = new ArrayList<String>();

    private ArrayList<String> uploadurlList = new ArrayList<String>();

    //视频
    private ImageView iv_video;
    private ImageView iv_video_add;
    private ImageView iv_video_play;
    private ImageView iv_video_del;
    private String videopath="";
    private String VideoPreviewPicture="";
    private Bitmap bmp;
    private static final int REQUEST_VIDEO_CODE = 10;
    private static final int REQUEST_MEDIA = 100;//本地视频

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_send_call);
        ButterKnife.bind(this);
        sendCallId = getIntent().getStringExtra("id");
        sendCalltype = getIntent().getStringExtra("type");

        initData();
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("发布");
        setTitleNameEn(R.mipmap.release);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        et_comment_content = (EditText) findViewById(R.id.et_comment_content);
        tvYkj.setOnClickListener(this);
        tvJdj.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        llSelectType.setOnClickListener(this);

        iv_video = (ImageView) findViewById(R.id.iv_video);
        iv_video_add = (ImageView) findViewById(R.id.iv_video_add);
        iv_video_play = (ImageView) findViewById(R.id.iv_video_play);
        iv_video_del = (ImageView) findViewById(R.id.iv_video_del);
        iv_video_add.setOnClickListener(this);
        iv_video_play.setOnClickListener(this);
        iv_video_del.setOnClickListener(this);

        getSellGoodsDetail();
        typelist.add("彩色宝石");
        typelist.add("奢侈品珠宝");
        typelist.add("手表");
        typelist.add("钻石");
        typelist.add("贵金属");
        typelist.add("翡翠玉石");
        typelist.add("和田玉");
        typelist.add("其他");

        adapter = new UpdatepicAdapter(AddSendCallActivity.this,urlList,picList,imagePaths,9);
        mGvPics.setAdapter(adapter);
        mGvPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == adapter.geturlLists().size()) {
                    requestReadCameraPermissions(new CheckPermListener() {
                        @Override
                        public void superREADPermission() {
                            PhotoPickerIntent intent = new PhotoPickerIntent(AddSendCallActivity.this);
                            intent.setSelectModel(SelectModel.MULTI);
                            intent.setShowCarema(true);
                            intent.setMaxTotal(9-adapter.getpicLists().size()); // 最多选择照片数量，默认为9
                            intent.setSelectedPaths(adapter.getimagePaths()); // 已选中的照片地址， 用于回显选中状态
                            startActivityForResult(intent, REQUEST_CAMERA_CODE);
                        }
                    });

                }
            }
        });
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ykj:
                if (selectTv == 1) {
                    selectTv = 0;
                    tvYkj.setBackgroundColor(getResources().getColor(R.color.green_bg));
                    tvJdj.setBackgroundColor(getResources().getColor(R.color.white));
                    tvYkj.setTextColor(getResources().getColor(R.color.white));
                    tvJdj.setTextColor(getResources().getColor(R.color.black));
                    llJdj.setVisibility(View.GONE);
                    llYkj.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_jdj:
                if (selectTv == 0) {
                    selectTv = 1;
                    tvYkj.setBackgroundColor(getResources().getColor(R.color.white));
                    tvJdj.setBackgroundColor(getResources().getColor(R.color.green_bg));
                    tvYkj.setTextColor(getResources().getColor(R.color.black));
                    tvJdj.setTextColor(getResources().getColor(R.color.white));
                    llJdj.setVisibility(View.VISIBLE);
                    llYkj.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_submit:
                //提交 先上传图片，
                submit();
                break;
            case R.id.ll_select_type:
                showPopupWindow(AddSendCallActivity.this, tvType, typelist);
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
                if(videopath.substring(0,4).equals("http")){
                    JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class,videopath, "");
                }else{
                    Uri uri=Uri.parse(videopath);
                    JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class,uri.toString(), "");
                }
                break;
        }
    }

    private void getVideo() {
        showVideoPopupWindow(AddSendCallActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCAMERAPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        //拍摄
                        try {
                            if (!CommonUtils.getPermisein(AddSendCallActivity.this, "android.permission.CAMERA")) {
                                Toast.makeText(AddSendCallActivity.this, "您的相机不可用，请检查相机功能是否禁用", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (!CommonUtils.isVoicePermission()) {
                                Toast.makeText(AddSendCallActivity.this, "您的录音不可用，请检查录音功能是否禁用", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (Exception e) {

                        }
                        Intent intent1=new Intent(AddSendCallActivity.this, VideoActivity.class);
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
                            MediaPickerActivity.open(AddSendCallActivity.this, REQUEST_MEDIA, options);
                        }
                        getPopupWindow().dismiss();
                    }
                });

            }
        });
    }

    private void submit() {
        String content = et_comment_content.getText().toString();
        if (TextUtils.isEmpty(content)) {
            CustomToast.show("描述内容不能为空");
            return;
        }
        String ykj_price = etPrice.getText().toString();
        if (selectTv == 0) {
            if (TextUtils.isEmpty(ykj_price)) {
                CustomToast.show("请输入寄卖价格");
                return;
            }
        }

        if (TextUtils.isEmpty(tvType.getText().toString())) {
            CustomToast.show("请选择分类");
            return;
        }

        if (adapter.geturlLists()==null||adapter.geturlLists().size()==0) {
            CustomToast.show("请选择寄卖图片");
            return;
        }

        if (adapter.geturlLists()!=null&&adapter.geturlLists().size()<3) {
            CustomToast.show("图片最少上传三张");
            return;
        }
        imageId = "";
        uploadurlList=new ArrayList<>();
        for(int i=0;i<adapter.geturlLists().size();i++){
            if(adapter.geturlLists().get(i).substring(0,4).equals("http")){
                String url=adapter.geturlLists().get(i);
                String fileid=url.substring(url.indexOf("=")+1,url.length());
                if (imageId.trim().equals("")) {
                    imageId = fileid;
                } else {
                    imageId = imageId + "," + fileid;
                }
            }else{
                uploadurlList.add(adapter.geturlLists().get(i));
            }
        }

        final ArrayList<File> uploadList=new ArrayList<>();
        if (!videopath.equals("")&&!videopath.substring(0,4).equals("http")){
            File Videofile=new File(videopath);
            uploadList.add(Videofile);
        }
        for (int i=0;i<uploadurlList.size();i++){
            uploadList.add(new File(uploadurlList.get(i)));
        }

        requestReadPermissions(new CheckPermListener() {
            @Override
            public void superREADPermission() {
                if (uploadList.size()>0){
                    totalSize = FileUtils.getFilesSize(uploadList);
                    mProcessingDialog=new CustomProgressDialog(AddSendCallActivity.this,"文件上传中...");
                    mProcessingDialog.show();
                }

                if (!videopath.equals("")&&!videopath.substring(0,4).equals("http")){
                    File file = new File(videopath);
                    UploadVideo(file);
                }else if (uploadurlList.size()>0){
                    File file = new File(uploadurlList.get(0));
                    UploadImage(file);
                }else{
                    //提交参数
                    addNewSC();
                }
            }
        });
    }

    /**
     * 网络请求
     */
    private void addNewSC() {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/updateSell");
        String code = "7";
        switch (tvType.getText().toString()) {
            case "彩色宝石":
                code = "8";
                break;
            case "奢侈品珠宝":
                code = "1";
                break;
            case "手表":
                code = "2";
                break;
            case "钻石":
                code = "3";
                break;
            case "贵金属":
                code = "4";
                break;
            case "翡翠玉石":
                code = "5";
                break;
            case "和田玉":
                code = "6";
                break;
            case "其他":
                code = "7";
                break;
        }
        OkGo.<DataResult<String>>post(url)
                .params("token", token)
                .params("id", sendCallId )
                .params("sellInfo", et_comment_content.getText().toString().trim())
                .params("sellImgs", imageId)
                .params("sellVideo", videoIds)
                .params("sellPrice", etPrice.getText().toString().trim())
                .params("sellPawnCode", code)
                .execute(new JsonCallback<DataResult<String>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<String>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null) {
                                if (response.body().getData().equals("1")) {
                                    Intent intent=new Intent("MySendCallRefresh");
                                    sendBroadcast(intent);
                                    CustomToast.show("编辑成功");
                                    AddSendCallActivity.this.finish();
                                }
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102)
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<String>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void getSellGoodsDetail() {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/sellDetail");
        OkGo.<DataResult<SendCallGoodInfo>>post(url)
                .params("token", token)
                .params("id", String.valueOf(sendCallId))
                .execute(new JsonCallback<DataResult<SendCallGoodInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<SendCallGoodInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if (response.body().getData() != null) {
                                sendCallGoodInfo = response.body().getData();
                                showJDInfo();
                            }
                        }else if (response.body().getErrorCode()==DataResult.RESULT_102)
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<DataResult<SendCallGoodInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showJDInfo() {
        tvAuthPrice.setText("￥ " + sendCallGoodInfo.getAuthPrice());
        tvAuthResult.setText(sendCallGoodInfo.getExperterInfo());
        et_comment_content.setText(sendCallGoodInfo.getAppraisalDsc());
        if (sendCalltype.equals("update")) {
            //是编辑就获取之前的信息
            showUpdateInfo();
        }

        picList = new ArrayList<>();
        if (!StringUtils.isEmpty(sendCallGoodInfo.getSellImgs())){
            List<String> list= Arrays.asList(sendCallGoodInfo.getSellImgs().split(","));
            for (int i=0;i<list.size();i++){
                picList.add(BaseConstant.Image_URL+list.get(i));
                urlList.add(BaseConstant.Image_URL+list.get(i));
            }
        }
        adapter = new UpdatepicAdapter(AddSendCallActivity.this,urlList,picList,imagePaths,9);
        mGvPics.setAdapter(adapter);

        if (!StringUtils.isEmpty(sendCallGoodInfo.getSellVideo())){
            videoIds=sendCallGoodInfo.getSellVideo();
            videopath=BaseConstant.Image_URL+videoIds;
            iv_video_add.setVisibility(View.GONE);
            iv_video.setVisibility(View.VISIBLE);
            iv_video_play.setVisibility(View.VISIBLE);
            iv_video_del.setVisibility(View.VISIBLE);
        }
    }

    private void showUpdateInfo() {
        et_comment_content.setText(sendCallGoodInfo.getSellInfo());
        etPrice.setText(sendCallGoodInfo.getSellPrice());
        String type = "";
        switch (sendCallGoodInfo.getSellPawnCode()) {
            case "8":
                type = "彩色宝石";
                break;
            case "1":
                type = "奢侈品珠宝";
                break;
            case "2":
                type = "手表";
                break;
            case "3":
                type = "钻石";
                break;
            case "4":
                type = "贵金属";
                break;
            case "5":
                type = "翡翠玉石";
                break;
            case "6":
                type = "和田玉";
                break;
            case "7":
                type = "其他";
                break;
        }
        tvType.setText(type);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限允许
            } else {
                openSettingActivity(this, "您没有打开相机或文件存储权限，请在设置中打开授权");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_CODE:
                    loadimagePaths(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
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

    //视频处理
    public void Video(){
        new AsyncTask<Void,Void,Void>(){
            private ProgressDialog dialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(AddSendCallActivity.this,"","视频处理中...");
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
                Glide.with(AddSendCallActivity.this).load(VideoPreviewPicture).into(iv_video);
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

    private void loadimagePaths(ArrayList<String> paths) {
        imagePaths= new ArrayList<>();
        urlList= new ArrayList<>();
        picList=adapter.getpicLists();
        for(int i=0;i<adapter.getpicLists().size();i++){
            urlList.add(adapter.getpicLists().get(i));
        }
        for(int i=0;i<paths.size();i++){
            urlList.add(paths.get(i));
        }
        imagePaths.addAll(paths);
        adapter = new UpdatepicAdapter(AddSendCallActivity.this,urlList,picList,imagePaths,9);
        mGvPics.setAdapter(adapter);
//        comImg(paths);
    }
    // 压缩
    public void comImg(final ArrayList<String> paths){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getlDialog().setMessage(getResources().getString(R.string.Picture_processing));
                getlDialog().show();
            }
            @Override
            protected Void doInBackground(Void... params) {
                for(int i=0;i<paths.size();i++){
                    try {
                        File file = FileUtils.compressImg(new File(paths.get(i)));
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
                getlDialog().dismiss();
                adapter = new UpdatepicAdapter(AddSendCallActivity.this,urlList,picList,imagePaths,9);
                mGvPics.setAdapter(adapter);
            }
        }.execute();
    }


    String imageId = "";
    int startPos_PicList = 0;

    public void UploadImage( File file){
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
                            if(imageId.equals("")){
                                imageId = response.body().getData().getId();
                            } else {
                                imageId =imageId + ","+response.body().getData().getId();
                            }
                            startPos_PicList++;
                            if (startPos_PicList < uploadurlList.size()) {
                                File file = new File(uploadurlList.get(startPos_PicList));
                                UploadImage(file);
                            } else {
                                mProcessingDialog.dismiss();
                                //提交参数
                                addNewSC();
                            }

                        }else {
                            mProcessingDialog.dismiss();
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


    String videoIds = "";
    public void UploadVideo( File file){
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
                            //先上传视频在，再上传图片

                            videoIds = response.body().getData().getId();

                            Log.d("", response.body().getData().getId());

                            if (uploadurlList.size()>0){
                                File file = new File(uploadurlList.get(0));
                                UploadImage(file);
                            }else{
                                mProcessingDialog.dismiss();
                                //上传完视频，提交参数
                                addNewSC();
                            }

                        }else {
                            mProcessingDialog.dismiss();
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

    private void openSettingActivity(final Activity activity, String message) {
        showMessageOKCancel(activity, message, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //打开权限设置页面
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                startActivity(intent);
            }
        });
    }

    private void showMessageOKCancel(Activity context, String message, DialogInterface
            .OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private PopupWindow popwindow;

    public void showPopupWindow(Activity activity, final TextView tv, final ArrayList<String> list)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.pw_listview, null);

        ListView lv_district = ((ListView) view.findViewById(R.id.lv_content));
        lv_district.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                popwindow.dismiss();
                tv.setText(list.get(position));
            }
        });

        CommonAdapter commAdapter = new CommonAdapter<String>(activity, list,
                R.layout.item_listview_simple) {
            @Override
            public void convert(final ViewHolder helper, final String item) {
                helper.setText(R.id.textview, item);
            }
        };
        lv_district.setAdapter(commAdapter);

        int[] location = new int[2];
        llSelectType.getLocationOnScreen(location);

        int aHeight = llSelectType.getHeight() + location[1];
        int tHeight = getWindowManager().getDefaultDisplay().getHeight() - aHeight;
        int aWidth = llSelectType.getWidth();
        popwindow = new PopupWindow(view, aWidth - ScreenUtils.dp2px(this,0), tHeight, true);
        popwindow.setOutsideTouchable(true);
        fitPopupWindowOverStatusBar(popwindow);
        popwindow.setBackgroundDrawable(new ColorDrawable());
        popwindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0]+ScreenUtils.dp2px(this, 0), aHeight);
    }
}

package com.glavesoft.pawnuser.activity.appraisal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.IdRes;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.video.VideoActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.FjPicAdapter;
import com.glavesoft.pawnuser.adapter.PicAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.OnlineIdentificationInfo;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.FileUtils;
import com.glavesoft.view.CustomProgressDialog;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.DatePickerPopWin;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.view.OnTimeSetListener;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

/**
 * @author 严光
 * @date: 2017/10/27
 * @company:常州宝丰
 */
public class WatchActivity extends BaseActivity implements View.OnClickListener{
    private static final int REQUEST_VIDEO_CODE = 10;
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private static final int REQUEST_CAMERA_CODE_FJ = 111;
    private static final int REQUEST_MEDIA = 100;//本地视频
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private ArrayList<String> urlList = new ArrayList<String>();
    private ArrayList<String> imagePaths = null;

    private ArrayList<String> enclosurelist=new ArrayList<>();
    private ArrayList<String> selectenclosurelist=new ArrayList<>();

    private TextView tv_pic_ck;
    private LinearLayout ll_submit_watch;
    private GridViewForNoScroll gv_pics_evaluationinfo,gv_pics_fj,gv_enclosure_watch;
    private EditText et_price_watch;
    private TextView tv_texture_watch,tv_jxtype_watch,tv_degree_watch,tv_time_watch;
    private RadioGroup rg_sex;
    private RadioButton rg_man,rg_woman;
    private String sex="男款";

    /** 品牌 */
    private TextView tv_brand_watch;
    private ArrayList<String> brandlist=new ArrayList<>();
    private ArrayList<String> texturelist=new ArrayList<>();
    private ArrayList<String> jxtypelist=new ArrayList<>();
    private ArrayList<String> degreelist=new ArrayList<>();

    private PicAdapter adapter;

    private String picIds="";
    private String enclosures="";
    private ArrayList<OnlineIdentificationInfo> Jesonlist=new ArrayList<>();
    private String code="",name="";

    private FjPicAdapter fjadapter;
    private String fjpicIds="";
    private ArrayList<String> urlList_fj = new ArrayList<String>();
    private ArrayList<String> imagePaths_fj = null;

    //视频
    private ImageView iv_video;
    private ImageView iv_video_add;
    private ImageView iv_video_play;
    private ImageView iv_video_del;
    private String video="";
    private String videopath="";
    private String VideoPreviewPicture="";
    private Bitmap bmp;

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        code=getIntent().getStringExtra("code");
        name=getIntent().getStringExtra("name");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName(name+"鉴定估价资料");
        setTitleNameEn(R.mipmap.watches);

        tv_pic_ck=(TextView)findViewById(R.id.tv_pic_ck);
        tv_pic_ck.setVisibility(View.VISIBLE);
        tv_pic_ck.setOnClickListener(this);

        gv_pics_evaluationinfo=(GridViewForNoScroll)findViewById(R.id.gv_pics_evaluationinfo);
        gv_enclosure_watch=(GridViewForNoScroll)findViewById(R.id.gv_enclosure_watch);
        gv_pics_fj=(GridViewForNoScroll)findViewById(R.id.gv_pics_fj);

        rg_sex=(RadioGroup) findViewById(R.id.rg_sex);
        rg_man=(RadioButton) findViewById(R.id.rg_man);
        rg_woman=(RadioButton) findViewById(R.id.rg_woman);

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==rg_man.getId()){
                    rg_man.setChecked(true);
                    rg_woman.setChecked(false);
                    sex="男款";
                }else if(checkedId==rg_woman.getId()){
                    rg_man.setChecked(false);
                    rg_woman.setChecked(true);
                    sex="女款";
                }
            }
        });

        et_price_watch=(EditText) findViewById(R.id.et_price_watch);
        et_price_watch.setFilters(new InputFilter[] { new BaseConstant.EditInputFilter(et_price_watch) });

        tv_brand_watch=(TextView) findViewById(R.id.tv_brand_watch);
        tv_texture_watch=(TextView)findViewById(R.id.tv_texture_watch);
        tv_jxtype_watch=(TextView)findViewById(R.id.tv_jxtype_watch);
        tv_degree_watch=(TextView)findViewById(R.id.tv_degree_watch);
        tv_time_watch=(TextView)findViewById(R.id.tv_time_watch);
        tv_brand_watch.setOnClickListener(this);
        tv_texture_watch.setOnClickListener(this);
        tv_jxtype_watch.setOnClickListener(this);
        tv_time_watch.setOnClickListener(this);
        tv_degree_watch.setOnClickListener(this);

        ll_submit_watch=(LinearLayout) findViewById(R.id.ll_submit_watch);
        ll_submit_watch.setOnClickListener(this);

        iv_video = (ImageView) findViewById(R.id.iv_video);
        iv_video_add = (ImageView) findViewById(R.id.iv_video_add);
        iv_video_play = (ImageView) findViewById(R.id.iv_video_play);
        iv_video_del = (ImageView) findViewById(R.id.iv_video_del);
        iv_video_add.setOnClickListener(this);
        iv_video_play.setOnClickListener(this);
        iv_video_del.setOnClickListener(this);

        adapter = new PicAdapter(WatchActivity.this,urlList,imagePaths);
        gv_pics_evaluationinfo.setAdapter(adapter);
        gv_pics_evaluationinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if(isfirst){
                    isfirst=false;
                    Intent intent=new Intent();
                    intent.setClass(WatchActivity.this,ReferenceActivity.class);
                    intent.putExtra("titleName","照片参考");
                    intent.putExtra("url",BaseConstant.BaseURL+"/m/pawn/getReference?code="+code);
                    startActivity(intent);
                }else{
                    if (position == adapter.geturlLists().size()) {
                        requestReadCameraPermissions(new CheckPermListener() {
                            @Override
                            public void superREADPermission() {
                                PhotoPickerIntent intent = new PhotoPickerIntent(WatchActivity.this);
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

        fjadapter = new FjPicAdapter(WatchActivity.this,urlList_fj,imagePaths_fj);
        gv_pics_fj.setAdapter(fjadapter);
        gv_pics_fj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == fjadapter.geturlLists().size()) {
                    PhotoPickerIntent intent = new PhotoPickerIntent(WatchActivity.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true);
                    intent.setMaxTotal(3); // 最多选择照片数量，默认为9
                    intent.setSelectedPaths(fjadapter.getimagePaths()); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, REQUEST_CAMERA_CODE_FJ);
                }
            }
        });

        brandlist.add("百达翡丽");brandlist.add("江诗丹顿");brandlist.add("爱彼");brandlist.add("朗格");
        brandlist.add("宝玑");brandlist.add("宝珀");brandlist.add("芝柏");brandlist.add("雅典");
        brandlist.add("积家");brandlist.add("格拉苏蒂");brandlist.add("雅克德罗");brandlist.add("伯爵");
        brandlist.add("劳力士");brandlist.add("万国");brandlist.add("卡地亚");brandlist.add("宝格丽");
        brandlist.add("欧米茄");brandlist.add("其它");

        texturelist.add("全钢");texturelist.add("K金/间金");texturelist.add("铂金");
        texturelist.add("钯金");texturelist.add("陶瓷");texturelist.add("钛合金");
        texturelist.add("其他");

        jxtypelist.add("石英(电子表、石英表)");jxtypelist.add("机械(自动上弦)");
        jxtypelist.add("机械(手动上弦)");

        degreelist.add("9.9成新");degreelist.add("9.5成新");degreelist.add("9.0成新");degreelist.add("8.5成新");

        enclosurelist.add("保修卡"); enclosurelist.add("包装盒"); enclosurelist.add("说明书");
        enclosurelist.add("收据或发票");
        showenclosureList(enclosurelist);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent();
        switch (v.getId())
        {
            case R.id.tv_pic_ck://照片参考
                intent.setClass(WatchActivity.this,ReferenceActivity.class);
                intent.putExtra("titleName","照片参考");
                intent.putExtra("url",BaseConstant.BaseURL+"/m/pawn/getReference?code="+code);
                startActivity(intent);
                break;
            case R.id.tv_brand_watch://品牌
                showPopupWindow(WatchActivity.this,tv_brand_watch,brandlist);
                break;
            case R.id.tv_texture_watch://表体材质
                showPopupWindow(WatchActivity.this,tv_texture_watch,texturelist);
                break;
            case R.id.tv_jxtype_watch://机芯类型
                showPopupWindow(WatchActivity.this,tv_jxtype_watch,jxtypelist);
                break;
            case R.id.tv_degree_watch://使用情况
                showPopupWindow(WatchActivity.this,tv_degree_watch,degreelist);
                break;
            case R.id.tv_time_watch://购买时间
                goToChooseDate(tv_time_watch);
                break;
            case R.id.ll_submit_watch:
                gotoSend();
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
        }
    }

    private void getVideo() {
        showVideoPopupWindow(WatchActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCAMERAPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        //拍摄
                        try {
                            if (!CommonUtils.getPermisein(WatchActivity.this, "android.permission.CAMERA")) {
                                Toast.makeText(WatchActivity.this, "您的相机不可用，请检查相机功能是否禁用", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (!CommonUtils.isVoicePermission()) {
                                Toast.makeText(WatchActivity.this, "您的录音不可用，请检查录音功能是否禁用", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (Exception e) {

                        }
                        Intent intent1=new Intent(WatchActivity.this, VideoActivity.class);
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
                            MediaPickerActivity.open(WatchActivity.this, REQUEST_MEDIA, options);
                        }
                        getPopupWindow().dismiss();
                    }
                });

            }
        });
    }

    private void gotoSend() {

        if(videopath.equals("")){
            CustomToast.show("请上传外观视频");
            return;
        }

        if(adapter.geturlLists().size()>0){

            if(adapter.geturlLists().size()<3){
                CustomToast.show("外观照最少上传三张");
                return;
            }


            if(tv_degree_watch.getText().toString().trim().length()==0){
                CustomToast.show("请选择使用情况");
                return;
            }


            final ArrayList<File> uploadPicList=new ArrayList<>();
            File Videofile=new File(videopath);
            uploadPicList.add(Videofile);
            for (int i=0;i<adapter.geturlLists().size();i++){
                uploadPicList.add(new File(adapter.geturlLists().get(i)));
            }
            if(fjadapter.geturlLists().size()>0){
                for (int i=0;i<fjadapter.geturlLists().size();i++){
                    uploadPicList.add(new File(fjadapter.geturlLists().get(i)));
                }
            }

            requestReadPermissions(new CheckPermListener() {
                @Override
                public void superREADPermission() {
                    if (uploadPicList.size()>0){
                        totalSize = FileUtils.getFilesSize(uploadPicList);
                    }
                    mProcessingDialog=new CustomProgressDialog(WatchActivity.this,"文件上传中...");
                    mProcessingDialog.show();
                    UploadVideo(new File(videopath));
                }
            });
        }else{
            CustomToast.show("请上传外观照");
        }
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
                // 选择照片
                case REQUEST_CAMERA_CODE_FJ:
                    loadimagePaths1(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
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
        adapter = new PicAdapter(WatchActivity.this,urlList,imagePaths);
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
                dialog = ProgressDialog.show(WatchActivity.this,"","图片处理中...");
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
                adapter = new PicAdapter(WatchActivity.this,urlList,imagePaths);
                gv_pics_evaluationinfo.setAdapter(adapter);
            }
        }.execute();
    }

    private void loadimagePaths1(ArrayList<String> paths) {
        imagePaths_fj= new ArrayList<>();
        urlList_fj= new ArrayList<>();
        urlList_fj.addAll(paths);
        imagePaths_fj.addAll(paths);
        fjadapter = new FjPicAdapter(WatchActivity.this,urlList_fj,imagePaths_fj);
        gv_pics_fj.setAdapter(fjadapter);
//        comImg1(paths);
    }
    // 压缩
    public void comImg1(final ArrayList<String> paths){
        new AsyncTask<Void,Void,Void>(){
            private ProgressDialog dialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(WatchActivity.this,"","图片处理中...");
            }
            @Override
            protected Void doInBackground(Void... params) {
                for(int i=0;i<paths.size();i++){
                    try {
                        File file = FileUtils.compressImg(new File(paths.get(i)), FileUtils.FILE_IMAGE_MAXSIZE);
                        if (file != null) {
                            urlList_fj.add( file.getAbsolutePath());
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
                fjadapter = new FjPicAdapter(WatchActivity.this,urlList_fj,imagePaths_fj);
                gv_pics_fj.setAdapter(fjadapter);
            }
        }.execute();
    }

    private void showenclosureList(ArrayList<String> result) {
        CommonAdapter commAdapter = new CommonAdapter<String>(WatchActivity.this, result,
                R.layout.item_enclosure) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

                helper.setText(R.id.tv_enclosure,item);

                if(selectenclosurelist.contains(item)){
                    helper.getView(R.id.iv_enclosure).setBackgroundResource(R.drawable.xz_);
                }else{
                    helper.getView(R.id.iv_enclosure).setBackgroundResource(R.drawable.xz);
                }

                helper.getView(R.id.ll_enclosure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectenclosurelist.contains(item)){
                            int pos = 0;
                            for (int i=0;i<selectenclosurelist.size();i++){
                                if(item.equals(selectenclosurelist.get(i))){
                                    pos=i;
                                }
                            }
                            selectenclosurelist.remove(pos);
                        }else{
                            selectenclosurelist.add(item);
                        }
                        notifyDataSetChanged();
                    }
                });

            }
        };

        gv_enclosure_watch.setAdapter(commAdapter);
    }

    private DatePickerPopWin datePickerPopWin;
    protected void goToChooseDate(final TextView tv) {
        if(datePickerPopWin == null){
            datePickerPopWin = new DatePickerPopWin(this,0);
            datePickerPopWin.setDayView();
            datePickerPopWin.setAnimationStyle(R.style.popwin_anim_style);
        }
        datePickerPopWin.setOnTimeSetListener(new OnTimeSetListener() {
            public void onTimeSet(String text) {

                tv.setText(text);
            }
        });
        datePickerPopWin.setOnDismissListener(dismissListener);
        setAlpha(0.5f);
        datePickerPopWin.showAtLocation(tv, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    protected PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setAlpha(1f);
        }
    };

    //视频处理
    public void Video(){
        new AsyncTask<Void,Void,Void>(){
            private ProgressDialog dialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(WatchActivity.this,"","视频处理中...");
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
                Glide.with(WatchActivity.this).load(VideoPreviewPicture).into(iv_video);
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

                                fjpicIds="";
                                if(fjadapter.geturlLists().size()>0){
                                    UploadPic_fj(new File(fjadapter.geturlLists().get(0)));
                                }else{
                                    picIds="";
                                    UploadPic(new File(adapter.geturlLists().get(0)));
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

    int startPos_PicList_fj = 0;
    public void UploadPic_fj(File file){
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
                                if(fjpicIds.equals("")){
                                    fjpicIds =response.body().getData().getId();
                                }else{
                                    fjpicIds=fjpicIds+","+response.body().getData().getId();
                                }

                                startPos_PicList_fj++;
                                if (startPos_PicList_fj < fjadapter.geturlLists().size()) {
                                    UploadPic_fj(new File(fjadapter.geturlLists().get(startPos_PicList_fj)));
                                } else {
                                    picIds="";
                                    UploadPic(new File(adapter.geturlLists().get(0)));
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
                                    OnlineIdentificationInfo info10 =new OnlineIdentificationInfo();
                                    info10.setName("外观视频");
                                    info10.setContent(video);
                                    info10.setContentType("4");
                                    Jesonlist.add(info10);
                                    OnlineIdentificationInfo info =new OnlineIdentificationInfo();
                                    info.setName("外观照");
                                    info.setContent(picIds);
                                    info.setContentType("3");
                                    Jesonlist.add(info);
                                    OnlineIdentificationInfo info8 =new OnlineIdentificationInfo();
                                    info8.setName("男女款");
                                    info8.setContent(sex);
                                    info8.setContentType("1");
                                    Jesonlist.add(info8);


                                    if(tv_brand_watch.getText().toString().trim().length()!=0){
                                        OnlineIdentificationInfo info1 =new OnlineIdentificationInfo();
                                        info1.setName("品牌");
                                        info1.setContent(tv_brand_watch.getText().toString().trim());
                                        info1.setContentType("1");
                                        Jesonlist.add(info1);
                                    }

                                    if(tv_texture_watch.getText().toString().trim().length()!=0){
                                        OnlineIdentificationInfo info2 =new OnlineIdentificationInfo();
                                        info2.setName("表体材质");
                                        info2.setContent(tv_texture_watch.getText().toString().trim());
                                        info2.setContentType("1");
                                        Jesonlist.add(info2);
                                    }

                                    if(tv_jxtype_watch.getText().toString().trim().length()!=0){
                                        OnlineIdentificationInfo info3 =new OnlineIdentificationInfo();
                                        info3.setName("机芯材质");
                                        info3.setContent(tv_jxtype_watch.getText().toString().trim());
                                        info3.setContentType("1");
                                        Jesonlist.add(info3);
                                    }

                                    OnlineIdentificationInfo info4 =new OnlineIdentificationInfo();
                                    info4.setName("使用情况");
                                    info4.setContent(tv_degree_watch.getText().toString().trim());
                                    info4.setContentType("1");
                                    Jesonlist.add(info4);

                                    if(tv_time_watch.getText().toString().trim().length()!=0){
                                        OnlineIdentificationInfo info5 =new OnlineIdentificationInfo();
                                        info5.setName("购买时间");
                                        info5.setContent(tv_time_watch.getText().toString().trim());
                                        info5.setContentType("2");
                                        Jesonlist.add(info5);
                                    }

                                    if(et_price_watch.getText().toString().trim().length()!=0){
                                        OnlineIdentificationInfo info6 =new OnlineIdentificationInfo();
                                        info6.setName("购买价格");
                                        info6.setContent(et_price_watch.getText().toString().trim());
                                        info6.setContentType("1");
                                        Jesonlist.add(info6);
                                    }

                                    for (int i=0;i<selectenclosurelist.size();i++){
                                        if(i==0){
                                            enclosures=selectenclosurelist.get(i);
                                        }else{
                                            enclosures=enclosures+","+ selectenclosurelist.get(i);
                                        }
                                    }

                                    OnlineIdentificationInfo info7 =new OnlineIdentificationInfo();
                                    info7.setName("配件");
                                    info7.setContent(enclosures);
                                    info7.setContentType("1");
                                    Jesonlist.add(info7);
                                    OnlineIdentificationInfo info9 =new OnlineIdentificationInfo();
                                    info9.setName("附件照");
                                    info9.setContent(fjpicIds);
                                    info9.setContentType("3");
                                    Jesonlist.add(info9);

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
        param.put("video",video);
        param.put("images",picIds);
        param.put("goodsImgs",fjpicIds);
        param.put("content",new Gson().toJson(Jesonlist));
        param.put("price",et_price_watch.getText().toString().trim());
        param.put("code",code);
        param.put("buyTime",tv_time_watch.getText().toString().trim());
        param.put("info","");
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

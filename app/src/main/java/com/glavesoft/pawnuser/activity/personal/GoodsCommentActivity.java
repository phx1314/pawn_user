package com.glavesoft.pawnuser.activity.personal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.BitmapUtils;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.FileUtils;
import com.glavesoft.util.KeyBoardManager;
import com.glavesoft.util.PermissionCheckUtil;
import com.glavesoft.view.CustomProgressDialog;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class GoodsCommentActivity extends BaseActivity implements View.OnClickListener,
        TextWatcher {
    @BindView(R.id.iv_comment_star2_1)
    ImageView ivCommentStar21;
    @BindView(R.id.iv_comment_star2_2)
    ImageView ivCommentStar22;
    @BindView(R.id.iv_comment_star2_3)
    ImageView ivCommentStar23;
    @BindView(R.id.iv_comment_star2_4)
    ImageView ivCommentStar24;
    @BindView(R.id.iv_comment_star2_5)
    ImageView ivCommentStar25;
    @BindView(R.id.iv_comment_star3_1)
    ImageView ivCommentStar31;
    @BindView(R.id.iv_comment_star3_2)
    ImageView ivCommentStar32;
    @BindView(R.id.iv_comment_star3_3)
    ImageView ivCommentStar33;
    @BindView(R.id.iv_comment_star3_4)
    ImageView ivCommentStar34;
    @BindView(R.id.iv_comment_star3_5)
    ImageView ivCommentStar35;
    @BindView(R.id.tv_memo)
    TextView tvMemo;
    private EditText et_comment_content;
    private TextView tv_submit;
    private ImageView iv_choose_goods_pic;
    private HorizontalScrollView hsv_comment_imgs;
    private ImageView iv_comment_star_1, iv_comment_star_2, iv_comment_star_3, iv_comment_star_4,
            iv_comment_star_5;

    private List<ImageView> starList, starList2, starList3;
    private List<String> imageUrls;//所有晒图图片路径
    private int currentStarCount, currentStarCount2, currentStarCount3;
    private InputMethodManager manager;

    public static final String KEY_IMAGE_LIST = "imageList";
    public static final String KEY_CURRENT_INDEX = "currentIndex";
    private final int REQUEST_CODE_PICTURE = 1;
    private final int RESULT_CODE_LARGE_IMAGE = 1;
    //晒单图片最多选择四张
    private final int MAX_PIC = 4;
    private String goodsId = "";
    private String orderId = "";

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
        setContentView(R.layout.activity_goods_comment);
        ButterKnife.bind(this);
        initData();
        initView();

    }

    private void initView() {
        setTitleBack();
        setTitleName("发布评价");
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        et_comment_content = (EditText) findViewById(R.id.et_comment_content);
        iv_choose_goods_pic = (ImageView) findViewById(R.id.iv_choose_goods_pic);
        hsv_comment_imgs = (HorizontalScrollView) findViewById(R.id.hsv_comment_imgs);
        starList.add(iv_comment_star_1 = (ImageView) findViewById(R.id.iv_comment_star_1));
        starList.add(iv_comment_star_2 = (ImageView) findViewById(R.id.iv_comment_star_2));
        starList.add(iv_comment_star_3 = (ImageView) findViewById(R.id.iv_comment_star_3));
        starList.add(iv_comment_star_4 = (ImageView) findViewById(R.id.iv_comment_star_4));
        starList.add(iv_comment_star_5 = (ImageView) findViewById(R.id.iv_comment_star_5));
        starList2.add(ivCommentStar21);
        starList2.add(ivCommentStar22);
        starList2.add(ivCommentStar23);
        starList2.add(ivCommentStar24);
        starList2.add(ivCommentStar25);
        starList3.add(ivCommentStar31);
        starList3.add(ivCommentStar32);
        starList3.add(ivCommentStar33);
        starList3.add(ivCommentStar34);
        starList3.add(ivCommentStar35);
        tv_submit.setOnClickListener(this);
        iv_choose_goods_pic.setOnClickListener(this);
        iv_comment_star_1.setOnClickListener(this);
        iv_comment_star_2.setOnClickListener(this);
        iv_comment_star_3.setOnClickListener(this);
        iv_comment_star_4.setOnClickListener(this);
        iv_comment_star_5.setOnClickListener(this);
        ivCommentStar21.setOnClickListener(this);
        ivCommentStar22.setOnClickListener(this);
        ivCommentStar23.setOnClickListener(this);
        ivCommentStar24.setOnClickListener(this);
        ivCommentStar25.setOnClickListener(this);
        ivCommentStar31.setOnClickListener(this);
        ivCommentStar32.setOnClickListener(this);
        ivCommentStar33.setOnClickListener(this);
        ivCommentStar34.setOnClickListener(this);
        ivCommentStar35.setOnClickListener(this);
        et_comment_content.addTextChangedListener(this);
    }

    public void initData() {
        orderId = getIntent().getStringExtra("orderId");
        goodsId = getIntent().getStringExtra("goodsId");
        String image = getIntent().getStringExtra("img");
        ImageView iv_pic_order = (ImageView) findViewById(R.id.sdv_goods_img);
        if (!image.equals("")) {
            List<String> list = Arrays.asList(image.split(","));
            getImageLoader().displayImage(BaseConstant.Image_URL + list.get(0), iv_pic_order,
                    getImageLoaderOptions());
        } else {
            getImageLoader().displayImage("", iv_pic_order, getImageLoaderOptions());
        }
        starList = new ArrayList<>();
        starList2 = new ArrayList<>();
        starList3 = new ArrayList<>();
        imageUrls = new ArrayList<>();
        currentStarCount = 5;//默认为五星好评
        currentStarCount2 = 5;//默认为五星好评
        currentStarCount3 = 5;//默认为五星好评
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String content = et_comment_content.getText().toString();
        if (content.length() >= 255) {
            Toast.makeText(this, "评论内容不能多于255个字符", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (KeyBoardManager.isShouldHideInput(v, ev)) {
                if (manager != null) {
                    manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private void userCommentAdd() {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userComment/add");
        HttpParams param=new HttpParams();
        param.put("token", token);
        param.put("orderId", orderId + "");
        param.put("goodsId", goodsId);
        param.put("info", et_comment_content.getText().toString().trim());
        param.put("img", imageId + "");
        param.put("score", currentStarCount + "");
        param.put("expressScore", currentStarCount2 + "");
        param.put("serviceScore", currentStarCount3 + "");
        OkGo.<DataResult<String>>post(url)
                .params(param)
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
                                    CustomToast.show("评价成功");
                                    GoodsCommentActivity.this.finish();
                                }
                            }
                        }else if (DataResult.RESULT_102 == response.body().getErrorCode())
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                //评价提交
                validateComment();
                break;

            case R.id.iv_choose_goods_pic:
                //检查是否有打开照相机和文件读写的权限
                if (PermissionCheckUtil.checkCameraAndExternalStoragePermission(this))
                    //权限已经开启, 调出图片选择界面
                    MultiImageSelector.create().count(MAX_PIC - imageUrls.size()).start(this,
                            REQUEST_CODE_PICTURE);
                break;

            case R.id.iv_comment_star_1:
                currentStarCount = 1;
                break;

            case R.id.iv_comment_star_2:
                currentStarCount = 2;
                break;

            case R.id.iv_comment_star_3:
                currentStarCount = 3;
                break;

            case R.id.iv_comment_star_4:
                currentStarCount = 4;
                break;

            case R.id.iv_comment_star_5:
                currentStarCount = 5;
                break;
            case R.id.iv_comment_star2_1:
                currentStarCount2 = 1;
                break;

            case R.id.iv_comment_star2_2:
                currentStarCount2 = 2;
                break;

            case R.id.iv_comment_star2_3:
                currentStarCount2 = 3;
                break;

            case R.id.iv_comment_star2_4:
                currentStarCount2 = 4;
                break;

            case R.id.iv_comment_star2_5:
                currentStarCount2 = 5;
                break;
            case R.id.iv_comment_star3_1:
                currentStarCount3 = 1;
                break;

            case R.id.iv_comment_star3_2:
                currentStarCount3 = 2;
                break;

            case R.id.iv_comment_star3_3:
                currentStarCount3 = 3;
                break;

            case R.id.iv_comment_star3_4:
                currentStarCount3 = 4;
                break;

            case R.id.iv_comment_star3_5:
                currentStarCount3 = 5;
                break;
            default:
                break;
        }
        switch (currentStarCount){
            case 1:
                tvMemo.setText("描述相符:很差");
                break;
            case 2:
                tvMemo.setText("描述相符:差");
                break;
            case 3:
                tvMemo.setText("描述相符:还行吧");
                break;
            case 4:
                tvMemo.setText("描述相符:很好");
                break;
            case 5:
                tvMemo.setText("描述相符:非常好");
                break;

        }
        for (int i = 0, len = starList.size(); i < len; i++) {
            starList.get(i).setImageResource(i < currentStarCount ? R.mipmap
                    .icon_comment_star_red : R.mipmap.icon_comment_star_gray);
        }
        for (int i = 0, len = starList2.size(); i < len; i++) {
            starList2.get(i).setImageResource(i < currentStarCount2 ? R.mipmap
                    .icon_comment_star_red : R.mipmap.icon_comment_star_gray);
        }
        for (int i = 0, len = starList3.size(); i < len; i++) {
            starList3.get(i).setImageResource(i < currentStarCount3 ? R.mipmap
                    .icon_comment_star_red : R.mipmap.icon_comment_star_gray);
        }
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICTURE) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity
                        .EXTRA_RESULT);
                imageUrls.addAll(path);
                handleCommentPicList(imageUrls, false);
            }
        } else if (resultCode == RESULT_CODE_LARGE_IMAGE) {
            //晒单大图页返回, 重新设置晒单图片
            handleCommentPicList(imageUrls = data.getStringArrayListExtra(KEY_IMAGE_LIST), true);
        }
    }

    /**
     * 处理选择的评价图片
     *
     * @param paths      图片的路径集合
     * @param isFromBack 是否来自LargeImageActivity返回
     */
    private void handleCommentPicList(final List<String> paths, boolean isFromBack) {
        LinearLayout rootview = new LinearLayout(this);
        View commentView;
        SimpleDraweeView sdv_pic;
        for (int i = 0, len = paths.size(); i < len; i++) {
            commentView = getLayoutInflater().inflate(R.layout.order_comment_pic_item, null);
            sdv_pic = (SimpleDraweeView) commentView.findViewById(R.id.sdv_pic);
            if (isFromBack) {
                //来自LargeImageActivity
                sdv_pic.setImageURI(Uri.parse("file://" + paths.get(i)));
            } else {
                //来自图片选择器
                String path = FileUtils.getCachePath(this);//获取app缓存路径来存放临时图片
                BitmapUtils.compressImage(paths.get(i), path, 95);
                sdv_pic.setImageURI(Uri.parse("file://" + path));
                imageUrls.set(i, path);
            }

            final int finalI = i;
            sdv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击HorizontalScrollView里的晒单图进入图片详情页
                    Intent intent = new Intent(GoodsCommentActivity.this,
                            CommentLargeImageActivity.class);
                    intent.putExtra(KEY_CURRENT_INDEX, finalI);
                    intent.putStringArrayListExtra(KEY_IMAGE_LIST, (ArrayList<String>) paths);
                    startActivityForResult(intent, REQUEST_CODE_PICTURE);
                }
            });
            AutoUtils.auto(commentView);
            rootview.addView(commentView);
        }
        hsv_comment_imgs.removeAllViews();
        hsv_comment_imgs.addView(rootview);
    }

    /**
     * 评价内容验证
     */
    private void validateComment() {
        String content = et_comment_content.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUrls.size() == 0) {
            userCommentAdd();
        }else{
            final ArrayList<File> uploadPicList=new ArrayList<>();
            for (int i=0;i<imageUrls.size();i++){
                uploadPicList.add(new File(imageUrls.get(i)));
            }

            requestReadPermissions(new CheckPermListener() {
                @Override
                public void superREADPermission() {
                    if (uploadPicList.size()>0){
                        totalSize = FileUtils.getFilesSize(uploadPicList);
                    }
                    mProcessingDialog=new CustomProgressDialog(GoodsCommentActivity.this,"文件上传中...");
                    mProcessingDialog.show();
                    UploadPic(new File(imageUrls.get(0)));
                }
            });

        }

//        for (int i = 0; i < imageUrls.size(); i++) {
//            List<File> files = new ArrayList<>();
//            File file = new File(imageUrls.get(i));
//            files.add(file);
//            if (i == imageUrls.size() - 1) {
//                UploadImage(files, 1);//最后一个
//            } else {
//                UploadImage(files, 0);
//            }
//        }
//        if (imageUrls.size() == 0) {
//            userCommentAdd();
//        }

    }

    String imageId = "";
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
                                if(imageId.equals("")){
                                    imageId =response.body().getData().getId();
                                }else{
                                    imageId=imageId+","+response.body().getData().getId();
                                }

                                startPos_PicList++;
                                if (startPos_PicList < imageUrls.size()) {
                                    UploadPic(new File(imageUrls.get(startPos_PicList)));
                                } else {
                                    mProcessingDialog.dismiss();
                                    userCommentAdd();
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

    private void UploadImage(List<File> files, final int index) {
        Map<String, String> param = new HashMap<String, String>();
        String url = BaseConstant.UploadAvatar_URL;
        VolleyUtil.postMultparamiApi(url, param, "name", files, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                getlDialog().dismiss();
                Type classtype = new TypeToken<DataResult<ImageInfo>>() {
                }.getType();
                DataResult<ImageInfo> response = CommonUtils.fromJson(s, classtype, CommonUtils
                        .DEFAULT_DATE_PATTERN);
                if (DataResult.RESULT_OK_ZERO == response.getErrorCode()) {
                    if (index == 0) {
                        imageId += response.getData().getId() + ",";
                    } else {
                        imageId += response.getData().getId();
                        userCommentAdd();
                    }

                } else if (DataResult.RESULT_102 == response.getErrorCode()) {
                    toLogin();
                } else {
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
}

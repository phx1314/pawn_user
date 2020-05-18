package com.glavesoft.pawnuser.activity.pawn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.glavesoft.pawnuser.activity.shoppingmall.JdGoodsDetailActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.BackGoodsInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.FileUtils;
import com.glavesoft.view.CustomToast;
import com.glavesoft.view.GridViewForNoScroll;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/11/20
 * @company:常州宝丰
 */
public class BackGoodsActivity  extends BaseActivity implements View.OnClickListener{
    private TextView tv_name_pawn,tv_jdprice_pawn,tv_no_pawn;
    private GridViewForNoScroll gv_pics_pawn;

    private TextView tv_money_backgoods,tv_term_backgoods,tv_rate_backgoods,tv_redeemRate_backgoods,tv_monetRate_backgoods;
    private TextView tv_beginDate_backgoods,tv_endDate_backgoods,tv_outTime_backgoods,tv_totalBackMoney_backgoods;
    private TextView tv_beginMoney_backgoods,tv_totalMoney_backgoods,tv_redeemOverdue_backgoods,tv_allMoney_backgoods;
    private TextView tv_bankcardname_backgoods,tv_bankcard_backgoods;

    private ImageView iv_dkpz_backgoods;

    private CheckBox cb_pawn_agree;
    private TextView tv_submit_back,tv_pawn_agreement;

    private BackGoodsInfo backGoodsInfo;
    private String id="";

    private String Imagepath="";
    private String platformImage="";
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backgoods);
        id=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("赎当办理");
        titlebar_kf = (ImageView) findViewById(R.id.titlebar_kf);
        titlebar_kf.setVisibility(View.VISIBLE);
        titlebar_kf.setOnClickListener(this);

        tv_name_pawn=(TextView) findViewById(R.id.tv_name_pawn);
        tv_jdprice_pawn=(TextView) findViewById(R.id.tv_jdprice_pawn);
        tv_no_pawn=(TextView) findViewById(R.id.tv_no_pawn);
        gv_pics_pawn=(GridViewForNoScroll) findViewById(R.id.gv_pics_pawn);

        cb_pawn_agree=(CheckBox) findViewById(R.id.cb_pawn_agree);
        tv_pawn_agreement=(TextView) findViewById(R.id.tv_pawn_agreement);
        tv_submit_back = (TextView) findViewById(R.id.tv_submit_back);
        tv_submit_back.setOnClickListener(this);
        tv_pawn_agreement.setOnClickListener(this);

        tv_money_backgoods=(TextView) findViewById(R.id.tv_money_backgoods);
        tv_term_backgoods=(TextView) findViewById(R.id.tv_term_backgoods);
        tv_rate_backgoods=(TextView) findViewById(R.id.tv_rate_backgoods);
        tv_redeemRate_backgoods=(TextView) findViewById(R.id.tv_redeemRate_backgoods);
        tv_monetRate_backgoods=(TextView) findViewById(R.id.tv_monetRate_backgoods);
        tv_beginDate_backgoods=(TextView) findViewById(R.id.tv_beginDate_backgoods);
        tv_endDate_backgoods=(TextView) findViewById(R.id.tv_endDate_backgoods);
        tv_outTime_backgoods=(TextView) findViewById(R.id.tv_outTime_backgoods);
        tv_totalBackMoney_backgoods=(TextView) findViewById(R.id.tv_totalBackMoney_backgoods);
        tv_beginMoney_backgoods=(TextView) findViewById(R.id.tv_beginMoney_backgoods);
        tv_totalMoney_backgoods=(TextView) findViewById(R.id.tv_totalMoney_backgoods);
        tv_redeemOverdue_backgoods=(TextView) findViewById(R.id.tv_redeemOverdue_backgoods);
        tv_allMoney_backgoods=(TextView) findViewById(R.id.tv_allMoney_backgoods);
        tv_bankcardname_backgoods=(TextView) findViewById(R.id.tv_bankcardname_backgoods);
        tv_bankcard_backgoods=(TextView) findViewById(R.id.tv_bankcard_backgoods);

        iv_dkpz_backgoods=(ImageView) findViewById(R.id.iv_dkpz_backgoods);
        iv_dkpz_backgoods.setOnClickListener(this);
        platGetDetail();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.titlebar_kf:
                gotokf_Z();
                break;
            case R.id.iv_dkpz_backgoods:
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(BackGoodsActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                });

                break;
            case R.id.tv_submit_back:
                if(Imagepath.equals("")){
                    CustomToast.show("请上传打款凭证");
                    return;
                }
//                if (!cb_pawn_agree.isChecked()) {
//                    CustomToast.show("请先阅读并同意典当条款");
//                    return;
//                }

                Upload(new File(Imagepath));
                break;
        }
    }

    private void gotokf_Z(){
        Information info = new Information();
        info.setAppkey("e9cc7fa955a94500b364641e84adcc35");
        //用户资料
        info.setUid(LocalData.getInstance().getUserInfo().getId());
        info.setUname(LocalData.getInstance().getUserInfo().getId());
        info.setTel(LocalData.getInstance().getUserInfo().getAccount());
        info.setFace(BaseConstant.Image_URL+LocalData.getInstance().getUserInfo().getHeadImg());
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(4);

        //转接类型(0-可转入其他客服，1-必须转入指定客服)
        info.setTranReceptionistFlag(1);
        //指定客服id
        info.setReceptionistId("b125bade408341d4b1c825ee56a1dbb8");
        if(backGoodsInfo!=null){
            //咨询内容
            ConsultingContent consultingContent = new ConsultingContent();
            //咨询内容标题，必填
            consultingContent.setSobotGoodsTitle(backGoodsInfo.getName());
            //咨询内容图片，选填 但必须是图片地址
            if(!backGoodsInfo.getImages().equals("")){
                List<String> list= Arrays.asList(backGoodsInfo.getImages().split(","));
                consultingContent.setSobotGoodsImgUrl(BaseConstant.Image_URL+list.get(0));
            }
            //咨询来源页，必填
            String url=BaseConstant.BaseURL+"/m/pawn/H5GetMyGoods?id="+backGoodsInfo.getId()+"&user_id="+LocalData.getInstance().getUserInfo().getId();
            consultingContent.setSobotGoodsFromUrl(url);
            //描述，选填
            //consultingContent.setSobotGoodsDescribe("XXX超级电视 S5");
            //标签，选填
            consultingContent.setSobotGoodsLable("还款金额：￥"+backGoodsInfo.getAllMoney());
            //可以设置为null
            info.setConsultingContent(consultingContent);
        }
        //设置聊天界面标题显示模式
        SobotApi.setChatTitleDisplayMode(BackGoodsActivity.this, SobotChatTitleDisplayMode.Default,"");

        SobotApi.startSobotChat(BackGoodsActivity.this, info);
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

            if (file != null)
            {
                Imagepath=file.getAbsolutePath();
                Glide.with(BackGoodsActivity.this).load(Imagepath).into(iv_dkpz_backgoods);

            } else
            {
                CustomToast.show("获取图片失败");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void Upload( File file){
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
                                platformImage=response.body().getData().getId();
                                applyGetBack();
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

    private void platGetDetail()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/getBackGoods");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        OkGo.<DataResult<BackGoodsInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<BackGoodsInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<BackGoodsInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){
                                backGoodsInfo=response.body().getData();
                                tv_name_pawn.setText(backGoodsInfo.getName());
                                tv_jdprice_pawn.setText("￥"+backGoodsInfo.getAuthPrice());
                                tv_no_pawn.setText("当号："+backGoodsInfo.getPawnticketCode());

                                tv_money_backgoods.setText("已发放当金：￥"+backGoodsInfo.getMoney());
                                if(!backGoodsInfo.getPawnTime().equals("")){
                                    int days=Integer.valueOf(backGoodsInfo.getPawnTime())*15;
                                    tv_term_backgoods.setText("上期典当期限："+days+"天");
                                }

                                tv_rate_backgoods.setText("月费率："+backGoodsInfo.getRate()+"%/月");
                                tv_redeemRate_backgoods.setText("逾期滞纳费率："+backGoodsInfo.getRedeemRate()+"%/月");
                                tv_monetRate_backgoods.setText("月利率："+backGoodsInfo.getMonetRate()+"%/月");

                                tv_beginDate_backgoods.setText("贷款日期："+backGoodsInfo.getBeginDate());
                                tv_endDate_backgoods.setText("应还款日期："+backGoodsInfo.getEndDate());
                                tv_outTime_backgoods.setText("逾期天数："+backGoodsInfo.getOutTime()+"天");
                                tv_totalBackMoney_backgoods.setText("￥"+backGoodsInfo.getAllMoney());

                                tv_beginMoney_backgoods.setText("本金：￥"+backGoodsInfo.getBeginMoney());
                                tv_totalMoney_backgoods.setText("利息：￥"+backGoodsInfo.getTotalMoney());
                                tv_redeemOverdue_backgoods.setText("逾期滞纳金：￥"+backGoodsInfo.getRedeemOverdue());
                                tv_allMoney_backgoods.setText("￥"+backGoodsInfo.getAllMoney());

                                tv_bankcardname_backgoods.setText("户名："+backGoodsInfo.getPayeeName());
                                tv_bankcard_backgoods.setText("帐号：("+backGoodsInfo.getPayeeBankName()+")  "+backGoodsInfo.getPayeeBankCardCode());

                                if(backGoodsInfo.getImages()!=null&&!backGoodsInfo.getImages().equals("")){
                                    List<String> list= Arrays.asList(backGoodsInfo.getImages().split(","));
                                    showList(list);
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
                    public void onError(com.lzy.okgo.model.Response<DataResult<BackGoodsInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showList(List<String> result) {
        CommonAdapter commAdapter = new CommonAdapter<String>(BackGoodsActivity.this, result,
                R.layout.item_pic_pawn) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

                ImageView iv_pic_pawn=(ImageView) helper.getView(R.id.iv_pic_pawn);
                getImageLoader().displayImage(BaseConstant.Image_URL+item,iv_pic_pawn,getImageLoaderOptions());
            }
        };

        gv_pics_pawn.setAdapter(commAdapter);
    }


    private void applyGetBack()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userPawn/applyGetBack");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        param.put("redeemTicket",platformImage);
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
                            Intent intent=new Intent("submitpawn");
                            sendBroadcast(intent);
                            CustomToast.show("赎当成功,您可以在未典当列表中查看！");
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

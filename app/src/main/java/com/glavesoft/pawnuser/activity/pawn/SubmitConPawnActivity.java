package com.glavesoft.pawnuser.activity.pawn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
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
import com.glavesoft.pawnuser.activity.main.WebActivity;
import com.glavesoft.pawnuser.activity.personal.MyinfoActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.ContinuedPawnInfo;
import com.glavesoft.pawnuser.mod.DataInfo;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.ImageInfo;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.util.CommonUtils;
import com.glavesoft.util.FileUtils;
import com.glavesoft.util.PreferencesUtils;
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/11/21
 * @company:常州宝丰
 */
public class SubmitConPawnActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_name_pawn,tv_jdprice_pawn,tv_no_pawn;
    private GridViewForNoScroll gv_pics_pawn;

    private TextView tv_orgname_continued,tv_loansPrice_continued,tv_rate_continued,tv_monetRate_continued,tv_redeemRate_continued;
    private TextView tv_beginTime_continued,tv_longTime_continued,tv_coninueTime_continued;
    private TextView tv_lx_continued,tv_zhlxMoney_continued,tv_redeemOverdue_continued,tv_totalMoney_continued;
    private TextView tv_payeeName_continued,tv_bankcard_continued;
    private TextView tv_fa_continued;

    private ImageView iv_dkpz_continued;

    private CheckBox cb_pawn_agree;
    private TextView tv_submit_continued,tv_pawn_agreement;

    ContinuedPawnInfo continuedPawnInfo;
    private String id="";
    private int pawnTime;

    private String Imagepath="";
    private String platformImage="";
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类

    private String endTime;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitconpawn);
        id=getIntent().getStringExtra("id");
        pawnTime=getIntent().getIntExtra("pawnTime",0);
        endTime=getIntent().getStringExtra("endTime");
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("续当办理");
        titlebar_kf = (ImageView) findViewById(R.id.titlebar_kf);
        titlebar_kf.setVisibility(View.VISIBLE);
        titlebar_kf.setOnClickListener(this);

        tv_name_pawn=(TextView) findViewById(R.id.tv_name_pawn);
        tv_jdprice_pawn=(TextView) findViewById(R.id.tv_jdprice_pawn);
        tv_no_pawn=(TextView) findViewById(R.id.tv_no_pawn);
        gv_pics_pawn=(GridViewForNoScroll) findViewById(R.id.gv_pics_pawn);

        cb_pawn_agree=(CheckBox) findViewById(R.id.cb_pawn_agree);
        tv_pawn_agreement=(TextView) findViewById(R.id.tv_pawn_agreement);
        tv_submit_continued = (TextView) findViewById(R.id.tv_submit_continued);
        tv_submit_continued.setOnClickListener(this);
        tv_pawn_agreement.setOnClickListener(this);

        tv_fa_continued=(TextView) findViewById(R.id.tv_fa_continued);

        tv_orgname_continued=(TextView) findViewById(R.id.tv_orgname_continued);
        tv_loansPrice_continued=(TextView) findViewById(R.id.tv_loansPrice_continued);
        tv_rate_continued=(TextView) findViewById(R.id.tv_rate_continued);
        tv_monetRate_continued=(TextView) findViewById(R.id.tv_monetRate_continued);
        tv_redeemRate_continued=(TextView) findViewById(R.id.tv_redeemRate_continued);
        tv_beginTime_continued=(TextView) findViewById(R.id.tv_beginTime_continued);
        tv_longTime_continued=(TextView) findViewById(R.id.tv_longTime_continued);
        tv_coninueTime_continued=(TextView) findViewById(R.id.tv_coninueTime_continued);
        tv_lx_continued=(TextView) findViewById(R.id.tv_lx_continued);
        tv_zhlxMoney_continued=(TextView) findViewById(R.id.tv_zhlxMoney_continued);
        tv_redeemOverdue_continued=(TextView) findViewById(R.id.tv_redeemOverdue_continued);
        tv_totalMoney_continued=(TextView) findViewById(R.id.tv_totalMoney_continued);
        tv_payeeName_continued=(TextView) findViewById(R.id.tv_payeeName_continued);
        tv_bankcard_continued=(TextView) findViewById(R.id.tv_bankcard_continued);

        iv_dkpz_continued=(ImageView) findViewById(R.id.iv_dkpz_continued);
        iv_dkpz_continued.setOnClickListener(this);

        fagui();//法规
        pawnConinueDetailSecond();
    }

    private void fagui(){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String text1="根据《合同法》、";
        SpannableString sp1 = new SpannableString(text1);
        sp1.setSpan(new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SubmitConPawnActivity.this,WebActivity.class);
                intent.putExtra("titleName","合同法");
                intent.putExtra("url", PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "htf",""));
                startActivity(intent);
            }
        }),2,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan redSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.red_k));
        sp1.setSpan(redSpan1, 2,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体的颜色

        String text2="《民事法》、";
        SpannableString sp2 = new SpannableString(text2);
        sp2.setSpan(new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SubmitConPawnActivity.this,WebActivity.class);
                intent.putExtra("titleName","民事法");
                intent.putExtra("url", PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "mfzz",""));
                startActivity(intent);
            }
        }),0,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan redSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.red_k));
        sp2.setSpan(redSpan2, 0,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体的颜色

        String text3="《典当管理法》，我都已理解并认可了本合同的所有内容，同意承担各自应承担的权利和义务，忠实地履行本合同。";
        SpannableString sp3 = new SpannableString(text3);
        sp3.setSpan(new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SubmitConPawnActivity.this,WebActivity.class);
                intent.putExtra("titleName","典当管理法");
                intent.putExtra("url", PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, "ddglbf",""));
                startActivity(intent);
            }
        }),0,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan redSpan3 = new ForegroundColorSpan(getResources().getColor(R.color.red_k));
        sp3.setSpan(redSpan3, 0,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体的颜色

        builder.append(sp1);//添加
        builder.append(sp2);
        builder.append(sp3);

        tv_fa_continued.setText(builder);
        tv_fa_continued.setMovementMethod(LinkMovementMethod.getInstance());//加上这句话才有效果
    }

    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener mListener) {
            this.mListener = mListener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);    //去除超链接的下划线
        }
    }

    /*
     * 将时间转换为时间戳
     */

    public String ConinueTime(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        long d=Long.valueOf(pawnTime)*15*24*60*60*1000;
        Date date1 = new Date(ts+d);
        res = simpleDateFormat.format(date1);
        return res;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.titlebar_kf:
                gotokf_Z();
                break;
            case R.id.tv_submit_continued:
                if(Imagepath.equals("")){
                    CustomToast.show("请上传打款凭证");
                    return;
                }
                if (!cb_pawn_agree.isChecked()) {
                    CustomToast.show("请先阅读并同意典当条款");
                    return;
                }

                Upload(new File(Imagepath));

                break;
            case R.id.iv_dkpz_continued:
                requestReadCameraPermissions(new CheckPermListener() {
                    @Override
                    public void superREADPermission() {
                        PhotoPickerIntent intent = new PhotoPickerIntent(SubmitConPawnActivity.this);
                        intent.setSelectModel(SelectModel.SINGLE);
                        intent.setShowCarema(true);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                });

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
        if(continuedPawnInfo!=null){
            //咨询内容
            ConsultingContent consultingContent = new ConsultingContent();
            //咨询内容标题，必填
            consultingContent.setSobotGoodsTitle(continuedPawnInfo.getTitle());
            //咨询内容图片，选填 但必须是图片地址
            if(!continuedPawnInfo.getImages().equals("")){
                List<String> list= Arrays.asList(continuedPawnInfo.getImages().split(","));
                consultingContent.setSobotGoodsImgUrl(BaseConstant.Image_URL+list.get(0));
            }
            //咨询来源页，必填
            String url=BaseConstant.BaseURL+"/m/pawn/getPawnContinue?id="+continuedPawnInfo.getId()+"&userId="+LocalData.getInstance().getUserInfo().getId();
            consultingContent.setSobotGoodsFromUrl(url);
            //描述，选填
            //consultingContent.setSobotGoodsDescribe("XXX超级电视 S5");
            //标签，选填
            consultingContent.setSobotGoodsLable("已发放当金：￥"+continuedPawnInfo.getMoney());
            //可以设置为null
            info.setConsultingContent(consultingContent);
        }
        //设置聊天界面标题显示模式
        SobotApi.setChatTitleDisplayMode(SubmitConPawnActivity.this, SobotChatTitleDisplayMode.Default,"");

        SobotApi.startSobotChat(SubmitConPawnActivity.this, info);
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
                Glide.with(SubmitConPawnActivity.this).load(Imagepath).into( iv_dkpz_continued);

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
                                goPawnContinue();
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

    private void pawnConinueDetailSecond()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/pawnConinueDetailSecond");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("id",id);
        param.put("pawnTime",pawnTime+"");
        OkGo.<DataResult<ContinuedPawnInfo>>post(url)
                .params(param)
                .execute(new JsonCallback<DataResult<ContinuedPawnInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<DataResult<ContinuedPawnInfo>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
                            if(response.body().getData()!=null){
                                continuedPawnInfo=response.body().getData();
                                tv_name_pawn.setText(continuedPawnInfo.getTitle());
                                tv_jdprice_pawn.setText("￥"+continuedPawnInfo.getAuthPrice());
                                tv_no_pawn.setText("当号："+continuedPawnInfo.getGoodsId());

                                tv_orgname_continued.setText("典当行："+continuedPawnInfo.getOrgName());
                                tv_rate_continued.setText("月费率："+continuedPawnInfo.getRate()+"%/月");
                                tv_loansPrice_continued.setText("已发放当金：￥"+continuedPawnInfo.getMoney());
                                tv_monetRate_continued.setText("月利率："+continuedPawnInfo.getMoneyRate()+"%/月");
                                tv_redeemRate_continued.setText("逾期滞纳费率："+continuedPawnInfo.getOverdueRate()+"%/月");
                                tv_beginTime_continued.setText("借款日期："+continuedPawnInfo.getBeginTime());
                                tv_longTime_continued.setText("续当时长："+(pawnTime*15)+"天");
                                tv_coninueTime_continued.setText("续当至日期："+ConinueTime(endTime));

                                tv_lx_continued.setText("利息：￥"+continuedPawnInfo.getLxMoney());
                                tv_zhlxMoney_continued.setText("综合费用：￥"+continuedPawnInfo.getZhlxMoney());
                                tv_redeemOverdue_continued.setText("逾期滞纳金：￥"+continuedPawnInfo.getRedeemOverdue());
                                tv_totalMoney_continued.setText("￥"+continuedPawnInfo.getTotalMoney());

                                tv_payeeName_continued.setText("户名："+continuedPawnInfo.getPayeeName());
                                tv_bankcard_continued.setText("帐号：("+continuedPawnInfo.getPayeeBankName()+")  "+continuedPawnInfo.getPayeeBankCardCode());

                                if(continuedPawnInfo.getImages()!=null&&!continuedPawnInfo.getImages().equals("")){
                                    List<String> list= Arrays.asList(continuedPawnInfo.getImages().split(","));
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
                    public void onError(com.lzy.okgo.model.Response<DataResult<ContinuedPawnInfo>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }

    private void showList(List<String> result) {
        CommonAdapter commAdapter = new CommonAdapter<String>(SubmitConPawnActivity.this, result,
                R.layout.item_pic_pawn) {
            @Override
            public void convert(final ViewHolder helper, final String item) {

                ImageView iv_pic_pawn=(ImageView) helper.getView(R.id.iv_pic_pawn);
                getImageLoader().displayImage(BaseConstant.Image_URL+item,iv_pic_pawn,getImageLoaderOptions());
            }
        };

        gv_pics_pawn.setAdapter(commAdapter);
    }

    private void goPawnContinue()
    {
        getlDialog().show();
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("userGoods/goPawnContinue");
        HttpParams param=new HttpParams();
        param.put("token",token);
        param.put("pawnTime",pawnTime+"");
        param.put("id",id);
        param.put("platformImage",platformImage);
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
                            CustomToast.show("续当成功");
                            ContinuedActivity.continuedActivity.finish();
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

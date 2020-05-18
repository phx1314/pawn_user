package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.mod.LoadingImgInfo;
import com.glavesoft.util.FileUtils;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.util.StringUtils;
import com.glavesoft.view.BAlertDialog;
import com.glavesoft.view.CustomToast;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.UpdateInfo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends BaseActivity
{
	UpdateInfo updateInfo;
	private ImageView iv_start_img;
	TextView item_img_tg;

	private Handler startHandle = new Handler();
	private final Timer timer = new Timer();
	private TimerTask task;
	boolean isStart = true;
	private int time = 3;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		iv_start_img=(ImageView)findViewById(R.id.iv_start_img);

		item_img_tg = (TextView) findViewById(R.id.item_img_tg);
		item_img_tg.setText("跳过"+time);
		item_img_tg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isStart){
					isStart = false;
					gotoMain();
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				loadingImg();
				checkUpdate();
				startHandle.post(rn);
			}
		}, 1000);

	}

	private Runnable rn = new Runnable() {
		@Override
		public void run() {
			time --;
			item_img_tg.setText("跳过"+time);
			if(time > 0){
				startHandle.postDelayed(this, 1000);
			}else{
				if(isStart){
					isStart = false;
					gotoMain();
				}
			}
		}
	};

	private void gotoMain()
	{
		boolean isFirstOpen = PreferencesUtils.getBoolean(StartActivity.this, "isfirstopensoft", true);
		if (isFirstOpen)
		{
			startActivity(new Intent(StartActivity.this, GuideActivity.class));
			//startActivity(new Intent(StartActivity.this, GuidanceActivity.class));
		} else
		{
//			if(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_LastLogin,null)==null){
//				startActivity(new Intent(StartActivity.this, LoginActivity.class));
//			}else{
				Intent select_i = new Intent();
				select_i.setClass(StartActivity.this, MainActivity.class);
				if(getIntent().hasExtra("type")){
					select_i.putExtra("type","push");
				}
				startActivity(select_i);
//			}
		}

		finish();
	}

	private void loadingImg() {
		String url=BaseConstant.getApiPostUrl("common/loadingImg");
		HttpParams param=new HttpParams();
		param.put("deviceType", "1");//1:android 2:ios
		OkGo.<DataResult<LoadingImgInfo>>post(url)
				.params(param)
				.execute(new JsonCallback<DataResult<LoadingImgInfo>>() {
					@Override
					public void onSuccess(com.lzy.okgo.model.Response<DataResult<LoadingImgInfo>> response) {
						if (response==null){
							CustomToast.show(getString(R.string.http_request_fail));
							return;
						}

						if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
							if(!StringUtils.isEmpty(response.body().getData().getImg())){
								DisplayImageOptions options= new DisplayImageOptions.Builder().showStubImage(R.drawable.bg_trans).showImageForEmptyUri(R.drawable.bg_trans).
										cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
								getImageLoader().displayImage(BaseConstant.Image_URL + response.body().getData().getImg(),iv_start_img,options);
							}
						}else {
							CustomToast.show(response.body().getErrorMsg());
						}
					}

					@Override
					public void onError(com.lzy.okgo.model.Response<DataResult<LoadingImgInfo>> response) {
						showVolleyError(null);
					}
				});
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0://有更新
					startHandle.removeCallbacks(rn);
					if (!isFinishing()){
						UpdateDialog();
					}
					break;
				case 1://无更新
					break;
			}

		}
	};

	private void checkUpdate() {
		String url=BaseConstant.getApiPostUrl("common/appVerion");
		HttpParams param=new HttpParams();
		param.put("deviceType", "1");//1:android 2:ios
		OkGo.<DataResult<UpdateInfo>>post(url)
				.params(param)
				.execute(new JsonCallback<DataResult<UpdateInfo>>() {
					@Override
					public void onSuccess(com.lzy.okgo.model.Response<DataResult<UpdateInfo>> response) {
						if (response==null){
							CustomToast.show(getString(R.string.http_request_fail));
							return;
						}

						if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){
							updateInfo = response.body().getData();

							try {
								PackageManager pm = StartActivity.this.getPackageManager();
								PackageInfo pi = pm.getPackageInfo(StartActivity.this.getPackageName(), 0);
								if (updateInfo != null	&& (updateInfo.getVersion() > pi.versionCode)) {
									handler.sendEmptyMessage(0);//有更新
								}else{
									handler.sendEmptyMessage(1);//无更新
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else {
							CustomToast.show(response.body().getErrorMsg());
						}
					}

					@Override
					public void onError(com.lzy.okgo.model.Response<DataResult<UpdateInfo>> response) {
						showVolleyError(null);
					}
				});
	}


	protected void UpdateDialog() {
		new BAlertDialog(this,false).setMessage(updateInfo.getInfo(), false)
				.setYesButton("确定", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateInfo.getUrl()));
							startActivity(intent);
						}catch (Exception e){

						}
						finish();

//						requestReadPermissions(new CheckPermListener() {
//							@Override
//							public void superREADPermission() {
//								FileDownload("");
//							}
//						});
					}
				},false).setNoButton(getResources().getString(R.string.cancel), new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(updateInfo.getIsFlag().equals("0")){
					gotoMain();
				}else{
					finish();
				}
			}
		}).show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 资源下载
	 */
	public void FileDownload(String fileId) {
		getDownLoadDialog().show();
		OkGo.<File>get(BaseConstant.Image_URL + fileId)
				.tag(this)
				.execute(new FileCallback(FileUtils.CACHE_SAVE_PATH ,"pawn_user.apk") {
					@Override
					public void onStart(Request<File, ? extends Request> request) {
						getDownLoadDialog().setProgress("0%");
					}
					@Override
					public void onSuccess(Response<File> response) {
						getDownLoadDialog().dismiss();
						AppUtils.installApp(FileUtils.CACHE_SAVE_PATH+"pawn_user.apk");
					}

					@Override
					public void onError(Response<File> response) {
						getDownLoadDialog().dismiss();
					}
					@Override
					public void downloadProgress(Progress progress) {
						float mProgress=progress.fraction * 100;
                        if (mProgress>=0){
                            getDownLoadDialog().setProgress((int)mProgress+"%");
                        }else{
                            getDownLoadDialog().setProgress("");
                        }
					}
				});
	}
}
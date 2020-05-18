package com.glavesoft.pawnuser.activity.video;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.util.FileUtils;
import com.glavesoft.view.RoundProgressBar;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author 严光
 * @date: 2017/8/1
 * @company:常州宝丰
 */
public class VideoActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnTouchListener{

    private static final int LISTENER_START = 200;
    private static final String TAG = "MainActivity";
    //预览SurfaceView
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    //进度条线程
    private Thread mProgressThread;
    //录制视频
    private MediaRecorder mMediaRecorder;
    private SurfaceHolder mSurfaceHolder;
    //屏幕分辨率
    private int videoWidth, videoHeight;
    private int videoWidth1, videoHeight1;
    //判断是否正在录制
    private boolean isRecording;
    //段视频保存的目录
    private File mTargetFile;
    //当前进度/时间
    private int mProgress;
    //录制最大时间
    public static final int MAX_TIME = 10;
    //是否上滑取消
    private boolean isCancel;
    //手势处理, 主要用于变焦 (双击放大缩小)
    private GestureDetector mDetector;
    //是否放大
    private boolean isZoomIn = false;

    //当前相机位置
    protected int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private MyHandler mHandler;
    private TextView mTvTip;
    private boolean isRunning;
    RoundProgressBar roundProgressBar;
    LinearLayout ll_press_control;

    List<Camera.Size> prviewSizeList;
    List<Camera.Size> videoSizeList;
    private boolean isFinish=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        // 检查该权限是否已经获取
        int i = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
        if (i != PackageManager.PERMISSION_GRANTED) {
            // 如果没有授予该权限，就去提示用户请求
            Toast.makeText(this, "您拒绝相机授权,会导致应用无法正常使用，可以在系统设置中重新开启权限", Toast.LENGTH_SHORT).show();
            return;
        } else {
            initSurface();
        }
    }

    //查找出最接近的视频录制分辨率
    public int bestVideoSize(int _w){
        //降序排列
        Collections.sort(videoSizeList, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                if (lhs.width > rhs.width) {
                    return -1;
                } else if (lhs.width == rhs.width) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        for(int i=0;i<videoSizeList.size();i++){
            if(videoSizeList.get(i).width<_w){
                return i;
            }
        }
        return 0;
    }

    private void initView() {

        mSurfaceView = (SurfaceView) findViewById(R.id.main_surface_view);

        mDetector = new GestureDetector(this, new ZoomGestureListener());
        /**
         * 单独处理mSurfaceView的双击事件
         */
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

//        mSurfaceHolder = mSurfaceView.getHolder();
//        //设置屏幕分辨率
//        mSurfaceHolder.setFixedSize(videoWidth, videoHeight);
//        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        mSurfaceHolder.addCallback(this);
        mTvTip = (TextView) findViewById(R.id.main_tv_tip);
        //自定义双向进度条    (这个地方差点把我急疯了!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!)
//        mProgressBar = (BothWayProgressBar) findViewById(R.
//                id.main_progress_bar);
//        mProgressBar.setOnProgressEndListener(this);
        ll_press_control=(LinearLayout) findViewById(R.id.ll_press_control);
        roundProgressBar=(RoundProgressBar) findViewById(R.id.roundProgressBar);
        roundProgressBar.setMax(600);
        ll_press_control.setOnTouchListener(this);
        mHandler = new MyHandler(this);
        mMediaRecorder = new MediaRecorder();
    }

    private void initSurface() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera(mCameraId);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // 释放Camera资源
                closeCamera();
            }
        });
    }

    private void initCamera(int i) {
        mCamera = Camera.open(i);//默认开启后置
        mCamera.setDisplayOrientation(90);//摄像头进行旋转90°
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                //设置预览照片的大小
                Point bestPreviewSizeValue1 = findBestPreviewSizeValue(parameters.getSupportedPreviewSizes());
                if (bestPreviewSizeValue1 != null) {
                    parameters.setPreviewSize(bestPreviewSizeValue1.x, bestPreviewSizeValue1.y);
                }
                //设置相机预览照片帧数
//                parameters.setPreviewFpsRange(4, 10);
                //设置图片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                //设置图片的质量
                parameters.set("jpeg-quality", 90);

                if (i == 0) {
                    //自动对焦
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }

                mCamera.setParameters(parameters);
                //通过SurfaceView显示预览
                mCamera.setPreviewDisplay(mSurfaceHolder);
                //开始预览
                mCamera.startPreview();
//                mCamera.unlock();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(VideoActivity.this, "meiyou", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @return 得到与原宽高比例最接近的尺寸
     */
    protected static Point findBestPreviewSizeValue(List<Camera.Size> sizeList) {
        int bestX = 0;
        int bestY = 0;
        int size = 0;
        for (Camera.Size nowSize : sizeList) {
            int newX = nowSize.width;
            int newY = nowSize.height;
            int newSize = Math.abs(newX * newX) + Math.abs(newY * newY);
            float ratio = (float) (newY * 1.0 / newX);
            if (newSize >= size && ratio != 0.75) {//确保图片是16:9
                bestX = newX;
                bestY = newY;
                size = newSize;
            } else if (newSize < size) {
                continue;
            }
        }
        if (bestX > 0 && bestY > 0) {
            return new Point(bestX, bestY);
        }
        return null;

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    ///////////////////////////////////////////////////////////////////////////
    // SurfaceView回调
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        startPreView(holder);
    }

    /**
     * 开启预览
     *
     * @param holder
     */
    private void startPreView(SurfaceHolder holder) {
        Log.d(TAG, "startPreView: ");

        if (mCamera == null) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(holder);
                Camera.Parameters parameters = mCamera.getParameters();
                //实现Camera自动对焦
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes != null) {
                    for (String mode : focusModes) {
                        mode.contains("continuous-video");
                        parameters.setFocusMode("continuous-video");
                    }
                }
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            Log.d(TAG, "surfaceDestroyed: ");
            //停止预览并释放摄像头资源
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 进度条结束后的回调方法
    ///////////////////////////////////////////////////////////////////////////
    public void onProgressEndListener() {
        //视频停止录制
        stopRecordSave();
    }

    /**
     * 开始录制
     */
    private void startRecord() {
        if (mMediaRecorder != null) {
            //没有外置存储, 直接停止录制
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return;
            }
            try {
                //mMediaRecorder.reset();
                mCamera.unlock();
                mMediaRecorder.setCamera(mCamera);
                //从相机采集视频
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                // 从麦克采集音频信息
                //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                // TODO: 2016/10/20 临时写个文件地址, 稍候该!!!
//                File targetDir = Environment.
//                        getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                String path = FileUtils.CACHE_SAVE_VIDEO_PATH;
                File tmpfile = new File(path);
                if (!tmpfile.exists())
                {
                    tmpfile.mkdirs();
                }

                mTargetFile=new File(path+System.currentTimeMillis()+ ".mp4");

                mMediaRecorder.setOutputFile(mTargetFile.getAbsolutePath());
                mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
//                //解决录制视频, 播放器横向问题
//                mMediaRecorder.setOrientationHint(90);
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
                    mMediaRecorder.setOrientationHint(90);//视频旋转90度
                else
                    mMediaRecorder.setOrientationHint(270);//视频旋转90度
                //在Android2.2（API Level8）之前，必须要直接设置输出格式和编码格式参数，而不是使用CamcorderProfile类。
                boolean boo = false;
                try {
                    // Set the recording profile.
                    CamcorderProfile profile = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P)) {
                            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
                        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P)) {
                            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
                        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P)) {
                            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
                        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_HIGH)) {
                            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                        }
                        if (profile != null) {
                            mMediaRecorder.setProfile(profile);
                            boo = true;
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (!boo) {
                    //设置了setProfile的话，下面的输出格式和编码需要注释
                    // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
                    mMediaRecorder
                            .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    // 设置录制的视频编码h264
                    mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                    mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//音频编码
                    mMediaRecorder.setVideoFrameRate(24);  //每秒的帧数
                }
                //mMediaRecorder.setVideoSize(640,480);
                // 设置帧频率，然后就清晰了
                mMediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);

                mMediaRecorder.prepare();
                //正式录制
                mMediaRecorder.start();
                isRecording = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 停止录制 并且保存
     */
    private void stopRecordSave() {
        if (isRecording) {
            isFinish=true;
            isRunning = false;
            mProgress = 0;
            mMediaRecorder.stop();
            isRecording = false;
            //Toast.makeText(this, "视频已经放至" + mTargetFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.putExtra("path", mTargetFile.getAbsolutePath().toString());
            setResult(10, intent);
            finish();
        }
    }

    /**
     * 停止录制, 不保存
     */
    private void stopRecordUnSave() {
        if (isRecording) {
            isRunning = false;
            mProgress = 0;
            if (mMediaRecorder != null) {
                //设置后不会崩
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                try {
                    mMediaRecorder.stop();
                } catch (IllegalStateException e) {
                    Log.w("Yixia", "stopRecord", e);
                } catch (RuntimeException e) {
                    Log.w("Yixia", "stopRecord", e);
                } catch (Exception e) {
                    Log.w("Yixia", "stopRecord", e);
                }
            }
            isRecording = false;
            if (mTargetFile.exists()) {
                //不保存直接删掉
                mTargetFile.delete();
            }

        }
    }

    /**
     * 相机变焦
     *
     * @param zoomValue
     */
    public void setZoom(int zoomValue) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.isZoomSupported()) {//判断是否支持
                int maxZoom = parameters.getMaxZoom();
                if (maxZoom == 0) {
                    return;
                }
                if (zoomValue > maxZoom) {
                    zoomValue = maxZoom;
                }
                parameters.setZoom(zoomValue);
                mCamera.setParameters(parameters);
            }
        }

    }


    ///////////////////////////////////////////////////////////////////////////
    // Handler处理
    ///////////////////////////////////////////////////////////////////////////
    private static class MyHandler extends Handler {
        private WeakReference<VideoActivity> mReference;
        private VideoActivity mActivity;

        public MyHandler(VideoActivity activity) {
            mReference = new WeakReference<VideoActivity>(activity);
            mActivity = mReference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //mActivity.mProgressBar.setProgress(mActivity.mProgress);
                    if(mActivity.mProgress>605){
                        //视频停止录制
                        mActivity.stopRecordSave();
                    }else{
                        mActivity.roundProgressBar.setProgress(mActivity.mProgress);
                    }

                    break;
            }

        }
    }

    /**
     * 触摸事件的触发
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean ret = false;
        int action = event.getAction();
        float ey = event.getY();
        float ex = event.getX();
        //只监听中间的按钮处
        int vW = v.getWidth();
        int left = LISTENER_START;
        int right = vW - LISTENER_START;

        float downY = 0;

        switch (v.getId()) {
            //case R.id.main_press_control: {
            case R.id.ll_press_control: {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (ex > left && ex < right) {
                            //mProgressBar.setCancel(false);
                            //显示上滑取消
                            mTvTip.setVisibility(View.VISIBLE);
                            mTvTip.setText("↑ 上滑取消");
                            //记录按下的Y坐标
                            downY = ey;
                            // TODO: 2016/10/20 开始录制视频, 进度条开始走
                            //mProgressBar.setVisibility(View.VISIBLE);
                            //开始录制
                            //Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show();
                            startRecord();
                            mProgressThread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        mProgress = 0;
                                        isRunning = true;
                                        while (isRunning) {
                                            mProgress++;
                                            mHandler.obtainMessage(0).sendToTarget();
                                            //Thread.sleep(20);
                                            Thread.sleep(100);
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            mProgressThread.start();

                            ret = true;
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        if (ex > left && ex < right) {
                            mTvTip.setVisibility(View.INVISIBLE);
                            //mProgressBar.setVisibility(View.INVISIBLE);
                            //判断是否为录制结束, 或者为成功录制(时间过短)
                            if (!isCancel) {
                                if (mProgress < 30) {
                                    //时间太短不保存
                                    stopRecordUnSave();
                                    finish();
                                    if(!isFinish){
                                        Toast.makeText(this, "时间太短", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                }
                                //停止录制
                                stopRecordSave();
                            } else {
                                //现在是取消状态,不保存
                                stopRecordUnSave();
                                isCancel = false;
                                finish();
                                Toast.makeText(this, "取消录制", Toast.LENGTH_SHORT).show();
                                //mProgressBar.setCancel(false);
                            }

                            ret = false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (ex > left && ex < right) {
                            float currentY = event.getY();
                            if (downY - currentY > 10) {
                                isCancel = true;
                                //mProgressBar.setCancel(true);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        //当弹出权限框的时候，进行暂停处理
//                        stopRecordUnSave();
//                        Toast.makeText(this, "权限设置，请重新录制", Toast.LENGTH_SHORT).show();
//                        finish();
                        break;
                }
                break;
            }

        }
        return ret;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 变焦手势处理类
    ///////////////////////////////////////////////////////////////////////////
    class ZoomGestureListener extends GestureDetector.SimpleOnGestureListener {
        //双击手势事件
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            super.onDoubleTap(e);
            Log.d(TAG, "onDoubleTap: 双击事件");
            if (mMediaRecorder != null) {
                if (!isZoomIn) {
                    setZoom(20);
                    isZoomIn = true;
                } else {
                    setZoom(0);
                    isZoomIn = false;
                }
            }
            return true;
        }
    }

    private void closeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();//停掉原来摄像头的预览
            mCamera.release();//释放资源
        }
        mCamera = null;//取消原来摄像头
    }
}

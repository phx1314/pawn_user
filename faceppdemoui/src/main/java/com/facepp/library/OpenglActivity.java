package com.facepp.library;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facepp.library.util.CamParaUtil;
import com.facepp.library.util.CameraMatrix;
import com.facepp.library.util.ConUtil;
import com.facepp.library.util.DialogUtil;
import com.facepp.library.util.FileUtil;
import com.facepp.library.util.ICamera;
import com.facepp.library.util.ImageUtil;
import com.facepp.library.util.MediaRecorderUtil;
import com.facepp.library.util.OpenGLDrawRect;
import com.facepp.library.util.OpenGLUtil;
import com.facepp.library.util.PointsMatrix;
import com.facepp.library.util.Screen;
import com.facepp.library.util.SensorEventUtil;
import com.megvii.facepp.sdk.Facepp;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.graphics.PixelFormat;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;

public class OpenglActivity extends Activity
        implements PreviewCallback, Renderer, SurfaceTexture.OnFrameAvailableListener {

    private boolean isStartRecorder, is3DPose, isDebug, isROIDetect, is106Points, isBackCamera, isFaceProperty,
            isOneFaceTrackig;
    private String trackModel;
    private boolean isTiming = true; // 是否是定时去刷新界面;
    private int printTime = 31;
    private GLSurfaceView mGlSurfaceView;
    private ICamera mICamera;
    private Camera mCamera;
    private Camera.Parameters mParams;
    private DialogUtil mDialogUtil;
    private TextView debugInfoText, debugPrinttext, AttriButetext,tv,tv_time;
    private HandlerThread mHandlerThread = new HandlerThread("facepp");
    private Handler mHandler;
    private Facepp facepp;
    private MediaRecorderUtil mediaRecorderUtil;
    private int min_face_size = 200;
    private int detection_interval = 25;
    private HashMap<String, Integer> resolutionMap;
    private SensorEventUtil sensorUtil;
    private float roi_ratio = 0.8f;
    private ProgressDialog dialog = null;

    private boolean isface=false;
    private boolean isdt=false;
    private boolean iszyt=false;
    private boolean isyyt=false;
    private boolean isStart=false;
    private CountDownTimer mCountDownTimer;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screen.initialize(this);
        setContentView(R.layout.activity_opengl);

        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startRecorder();
            }
        }, 2000);
    }

    private void init() {
        if (android.os.Build.MODEL.equals("PLK-AL10"))
            printTime = 50;
        type=getIntent().getStringExtra("type");
        isStartRecorder = getIntent().getBooleanExtra("isStartRecorder", false);
        is3DPose = getIntent().getBooleanExtra("is3DPose", false);
        isDebug = getIntent().getBooleanExtra("isdebug", false);
        isROIDetect = getIntent().getBooleanExtra("ROIDetect", false);
        is106Points = getIntent().getBooleanExtra("is106Points", false);
        isBackCamera = getIntent().getBooleanExtra("isBackCamera", false);
        isFaceProperty = getIntent().getBooleanExtra("isFaceProperty", false);
        isOneFaceTrackig = getIntent().getBooleanExtra("isOneFaceTrackig", false);
        trackModel = getIntent().getStringExtra("trackModel");

        min_face_size = getIntent().getIntExtra("faceSize", min_face_size);
        detection_interval = getIntent().getIntExtra("interval", detection_interval);
        resolutionMap = (HashMap<String, Integer>) getIntent().getSerializableExtra("resolution");

        facepp = new Facepp();

        sensorUtil = new SensorEventUtil(this);

        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        mGlSurfaceView = (GLSurfaceView) findViewById(R.id.opengl_layout_surfaceview);
        mGlSurfaceView.setEGLContextClientVersion(2);// 创建一个OpenGL ES 2.0
        // context
        mGlSurfaceView.setRenderer(this);// 设置渲染器进入gl
        // RENDERMODE_CONTINUOUSLY不停渲染
        // RENDERMODE_WHEN_DIRTY懒惰渲染，需要手动调用 glSurfaceView.requestRender() 才会进行更新
        mGlSurfaceView.setRenderMode(mGlSurfaceView.RENDERMODE_WHEN_DIRTY);// 设置渲染器模式
        mGlSurfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                autoFocus();
            }
        });

        mICamera = new ICamera();
        mDialogUtil = new DialogUtil(this);
        debugInfoText = (TextView) findViewById(R.id.opengl_layout_debugInfotext);
        AttriButetext = (TextView) findViewById(R.id.opengl_layout_AttriButetext);
        debugPrinttext = (TextView) findViewById(R.id.opengl_layout_debugPrinttext);
        tv = (TextView) findViewById(R.id.tv);
        tv_time = (TextView) findViewById(R.id.tv_time);
        if (isDebug)
            debugInfoText.setVisibility(View.VISIBLE);
        else
            debugInfoText.setVisibility(View.INVISIBLE);
    }

    /**
     * 开始录制
     */
    private void startRecorder() {
        if (isStartRecorder) {
            int Angle = 360 - mICamera.Angle;
            if (isBackCamera)
                Angle = mICamera.Angle;
            mediaRecorderUtil = new MediaRecorderUtil(this, mCamera, mICamera.cameraWidth, mICamera.cameraHeight);
            isStartRecorder = mediaRecorderUtil.prepareVideoRecorder(Angle);
            if (isStartRecorder) {
                boolean isRecordSucess = mediaRecorderUtil.start();
                if (isRecordSucess)
                    mICamera.actionDetect(this);
                else
                    mDialogUtil.showDialog(getResources().getString(R.string.no_record));
            }
        }

    }

    private void autoFocus() {
        if (mCamera != null && isBackCamera) {
            mCamera.cancelAutoFocus();
            Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(parameters);
            mCamera.autoFocus(null);
        }
    }

    private int Angle;

    @Override
    protected void onResume() {
        super.onResume();
        ConUtil.acquireWakeLock(this);
        startTime = System.currentTimeMillis();
        mCamera = mICamera.openCamera(isBackCamera, this, resolutionMap);
        if (mCamera != null) {
            Angle = 360 - mICamera.Angle;
            if (isBackCamera)
                Angle = mICamera.Angle;

            RelativeLayout.LayoutParams layout_params = mICamera.getLayoutParam();
            mGlSurfaceView.setLayoutParams(layout_params);

            int width = mICamera.cameraWidth;
            int height = mICamera.cameraHeight;

            int left = 0;
            int top = 0;
            int right = width;
            int bottom = height;
            if (isROIDetect) {
                float line = height * roi_ratio;
                left = (int) ((width - line) / 2.0f);
                top = (int) ((height - line) / 2.0f);
                right = width - left;
                bottom = height - top;
            }

            String errorCode = facepp.init(this, ConUtil.getFileContent(this, R.raw.megviifacepp_0_4_7_model));
            Facepp.FaceppConfig faceppConfig = facepp.getFaceppConfig();
            faceppConfig.interval = detection_interval;
            faceppConfig.minFaceSize = min_face_size;
            faceppConfig.roi_left = left;
            faceppConfig.roi_top = top;
            faceppConfig.roi_right = right;
            faceppConfig.roi_bottom = bottom;
            if (isOneFaceTrackig)
                faceppConfig.one_face_tracking = 1;
            else
                faceppConfig.one_face_tracking = 0;
            String[] array = getResources().getStringArray(R.array.trackig_mode_array);
            if (trackModel.equals(array[0]))
                faceppConfig.detectionMode = Facepp.FaceppConfig.DETECTION_MODE_TRACKING;
            else if (trackModel.equals(array[1]))
                faceppConfig.detectionMode = Facepp.FaceppConfig.DETECTION_MODE_TRACKING_ROBUST;
            else if (trackModel.equals(array[2]))
                faceppConfig.detectionMode = Facepp.FaceppConfig.DETECTION_MODE_TRACKING_FAST;

            facepp.setFaceppConfig(faceppConfig);
        } else {
            mDialogUtil.showDialog(getResources().getString(R.string.camera_error));
        }
    }

    private void setConfig(int rotation) {
        Facepp.FaceppConfig faceppConfig = facepp.getFaceppConfig();
        if (faceppConfig.rotation != rotation) {
            faceppConfig.rotation = rotation;
            facepp.setFaceppConfig(faceppConfig);
        }
    }

    /**
     * 画绿色框
     */
    private void drawShowRect() {
        mPointsMatrix.vertexBuffers = OpenGLDrawRect.drawCenterShowRect(isBackCamera, mICamera.cameraWidth,
                mICamera.cameraHeight, roi_ratio);
    }

    boolean isSuccess = false;
    float confidence;
    float pitch, yaw, roll;
    float pitch1, yaw1, roll1;
    long startTime;
    long time_AgeGender_end = 0;
    String AttriButeStr = "";
    int rotation = Angle;

    float eye1_x,eye1_y;
    @Override
    public void onPreviewFrame(final byte[] imgData, final Camera camera) {
        if (isSuccess)
            return;
        isSuccess = true;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int width = mICamera.cameraWidth;
                int height = mICamera.cameraHeight;

                long faceDetectTime_action = System.currentTimeMillis();
                int orientation = sensorUtil.orientation;
                if (orientation == 0)
                    rotation = Angle;
                else if (orientation == 1)
                    rotation = 0;
                else if (orientation == 2)
                    rotation = 180;
                else if (orientation == 3)
                    rotation = 360 - Angle;

                setConfig(rotation);

                final Facepp.Face[] faces = facepp.detect(imgData, width, height, Facepp.IMAGEMODE_NV21);
                final long algorithmTime = System.currentTimeMillis() - faceDetectTime_action;

                if (faces != null) {
                    long actionMaticsTime = System.currentTimeMillis();
                    ArrayList<ArrayList> pointsOpengl = new ArrayList<ArrayList>();
                    confidence = 0.0f;

                    if (faces.length >= 0) {
                        for (int c = 0; c < faces.length; c++) {
                            if (is106Points)
                                facepp.getLandmark(faces[c], Facepp.FPP_GET_LANDMARK106);
                            else
                                facepp.getLandmark(faces[c], Facepp.FPP_GET_LANDMARK81);

                            if (is3DPose) {
                                facepp.get3DPose(faces[c]);
                            }

                            Facepp.Face face = faces[c];


                            if (isFaceProperty) {
                                long time_AgeGender_action = System.currentTimeMillis();
                                facepp.getAgeGender(faces[c]);
                                time_AgeGender_end = System.currentTimeMillis() - time_AgeGender_action;
                                String gender = "man";
                                if (face.female > face.male)
                                    gender = "woman";
                                AttriButeStr = "\nage: " + (int) Math.max(face.age, 1) + "\ngender: " + gender;
                            }

                            pitch = faces[c].pitch;
                            yaw = faces[c].yaw;
                            roll = faces[c].roll;
                            confidence = faces[c].confidence;

                            for(int i=0;i<faces[c].points.length;i++){
                                if(i==21){
                                    eye1_x=faces[c].points[i].x;
                                    System.out.println("eye_x"+i+"====>"+eye1_x);
                                }
                            }
                            eye1_x=faces[c].points[19].x;

                            if (orientation == 1 || orientation == 2) {
                                width = mICamera.cameraHeight;
                                height = mICamera.cameraWidth;
                            }
                            ArrayList<FloatBuffer> triangleVBList = new ArrayList<FloatBuffer>();
                            for (int i = 0; i < faces[c].points.length; i++) {
                                float x = (faces[c].points[i].x / height) * 2 - 1;
                                if (isBackCamera)
                                    x = -x;
                                float y = 1 - (faces[c].points[i].y / width) * 2;
                                float[] pointf = new float[]{x, y, 0.0f};
                                if (orientation == 1)
                                    pointf = new float[]{-y, x, 0.0f};
                                if (orientation == 2)
                                    pointf = new float[]{y, -x, 0.0f};
                                if (orientation == 3)
                                    pointf = new float[]{-x, -y, 0.0f};

                                FloatBuffer fb = mCameraMatrix.floatBufferUtil(pointf);
                                triangleVBList.add(fb);
                            }

                            pointsOpengl.add(triangleVBList);
                        }
                    } else {
                        pitch = 0.0f;
                        yaw = 0.0f;
                        roll = 0.0f;
                    }
                    if (faces.length > 0 && is3DPose)
                        mPointsMatrix.bottomVertexBuffer = OpenGLDrawRect.drawBottomShowRect(0.15f, 0, -0.7f, pitch,
                                -yaw, roll, rotation);
                    else
                        mPointsMatrix.bottomVertexBuffer = null;
                    synchronized (mPointsMatrix) {
                        mPointsMatrix.points = pointsOpengl;
                    }

                    //faces[0].leftEyestatus

                    final long matrixTime = System.currentTimeMillis() - actionMaticsTime;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            String logStr = "\ncameraWidth: " + mICamera.cameraWidth + "\ncameraHeight: "
//                                    + mICamera.cameraHeight + "\nalgorithmTime: " + algorithmTime + "ms"
//                                    + "\nmatrixTime: " + matrixTime + "\nconfidence:" + confidence;

                            if(confidence>0.5){
                                isface=true;
                            }else{
                                isface=false;
                            }

                            if(pitch>0.2){
                                isdt=true;
                            }

                            if(yaw>0.3){
                                iszyt=true;
                            }

                            if(yaw<-0.3){
                                isyyt=true;
                            }

                            if(isface){//是否人脸
                                if(isdt){//是否点头
                                    if(iszyt&&isyyt){//是否摇头
                                        if(!isStart){
                                            tv.setText("验证成功！3秒后获取人脸信息");
                                            isStart=true;
                                            initCountDownTimer(3000);
                                            mCountDownTimer.start();
                                        }

                                    }else{
                                        tv.setText("请左右摇头！");
                                    }
                                }else{
                                    tv.setText("请大幅度点头！");
                                }
                            }else{
                                isface=false;
                                isdt=false;
                                iszyt=false;
                                isyyt=false;
                                tv.setText("请让我看到你的脸！");

                            }



                            //String logStr = "\nx: " + pitch+"\ny: " + yaw+"\nz: " + roll+"\nconfidence: " + confidence;

//                            String logStr = "\neye1_x: " + eye1_x;

//                            debugInfoText.setText(logStr);
//                            if (faces.length > 0 && isFaceProperty && AttriButeStr != null && AttriButeStr.length() > 0)
//                                AttriButetext.setText(AttriButeStr + "\nAgeGenderTime:" + time_AgeGender_end);
//                            else
//                                AttriButetext.setText("");
                        }
                    });
                }
                isSuccess = false;
                if (!isTiming) {
                    timeHandle.sendEmptyMessage(1);
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        ConUtil.releaseWakeLock();
        if (mediaRecorderUtil != null) {
            mediaRecorderUtil.releaseMediaRecorder();
        }
        mICamera.closeCamera();
        mCamera = null;

        timeHandle.removeMessages(0);

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        facepp.release();
    }

    private int mTextureID = -1;
    private SurfaceTexture mSurface;
    private CameraMatrix mCameraMatrix;
    private PointsMatrix mPointsMatrix;

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            // 黑色背景
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

            mTextureID = OpenGLUtil.createTextureID();
            mSurface = new SurfaceTexture(mTextureID);
            // 这个接口就干了这么一件事，当有数据上来后会进到onFrameAvailable方法
            mSurface.setOnFrameAvailableListener(this);// 设置照相机有数据时进入
            mCameraMatrix = new CameraMatrix(mTextureID);
            mPointsMatrix = new PointsMatrix();
            mICamera.startPreview(mSurface);// 设置预览容器
            mICamera.actionDetect(this);
            if (isTiming) {
                timeHandle.sendEmptyMessageDelayed(0, printTime);
            }
            if (isROIDetect)
                drawShowRect();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OpenglActivity.this,"初始化失败,请重新打开！",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置画面的大小
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        ratio = 1; // 这样OpenGL就可以按照屏幕框来画了，不是一个正方形了

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        // Matrix.perspectiveM(mProjMatrix, 0, 0.382f, ratio, 3, 700);
    }

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {
        final long actionTime = System.currentTimeMillis();
        // Log.w("ceshi", "onDrawFrame===");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);// 清除屏幕和深度缓存
        float[] mtx = new float[16];
        mSurface.getTransformMatrix(mtx);
        mCameraMatrix.draw(mtx);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1f, 0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        mPointsMatrix.draw(mMVPMatrix);

        if (isDebug) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final long endTime = System.currentTimeMillis() - actionTime;
                    debugPrinttext.setText("printTime: " + endTime);
                }
            });
        }
        mSurface.updateTexImage();// 更新image，会调用onFrameAvailable方法
    }

    Handler timeHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mGlSurfaceView.requestRender();// 发送去绘制照相机不断去回调
                    timeHandle.sendEmptyMessageDelayed(0, printTime);
                    break;
                case 1:
                    mGlSurfaceView.requestRender();// 发送去绘制照相机不断去回调
                    break;
            }
        }
    };

    private static final String TAG = "pawn_user";
    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    ShutterCallback mShutterCallback = new ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {
        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };
    PictureCallback mRawCallback = new PictureCallback()
            // 拍摄的未压缩原数据的回调,可以为null
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myRawCallback:onPictureTaken...");

        }
    };
    PictureCallback mJpegPictureCallback = new PictureCallback()
            //对jpeg图像数据的回调,最重要的一个回调
    {
        public void onPictureTaken(final byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myJpegCallback:onPictureTaken...");
            new AsyncTask<Void,Void,Void>(){
                private ProgressDialog dialog = null;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    dialog = ProgressDialog.show(OpenglActivity.this,"","人脸处理中...");
                }
                @Override
                protected Void doInBackground(Void... params) {
                    Bitmap b = null;
                    if(null != data){
                        b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                        mCamera.stopPreview();
                    }
                    //保存图片到sdcard
                    if(null != b)
                    {
                        //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                        //图片竟然不能旋转了，故这里要旋转下
                        Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 270.0f);
                        FileUtil.saveBitmap(rotaBitmap);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if(dialog!=null && dialog.isShowing()) dialog.dismiss();
                    Intent intent = new Intent(type);
                    sendBroadcast(intent);
                    finish();

                }
            }.execute();
        }
    };

    public void initCountDownTimer(long millisInFuture) {
        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_time.setText(millisUntilFinished/1000+"秒");
            }

            public void onFinish() {
                tv_time.setText("");
                tv.setText("");
                if(confidence>0.5){
                    //拍照
                    if(isStart){
                        tv.setText("验证成功！");
                        tv_time.setText("获取人脸中...");
                        mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
                    }
                }else{
                    isStart=false;
                    Toast.makeText(OpenglActivity.this,"请让我看到你的脸！",Toast.LENGTH_SHORT).show();
                }

            }
        };
    }
}

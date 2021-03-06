package com.expo.widget.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.expo.R;
import com.expo.base.utils.FileUtils;
import com.expo.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.hardware.Camera.Parameters.FOCUS_MODE_AUTO;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback, View.OnClickListener {

    protected static final int[] VIDEO_320 = {320, 240};
    protected static final int[] VIDEO_480 = {640, 480};
    protected static final int[] VIDEO_720 = {1280, 720};
    protected static final int[] VIDEO_1080 = {1920, 1080};
    private int screenOritation = Configuration.ORIENTATION_PORTRAIT;
    private boolean mOpenBackCamera = true;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceTexture mSurfaceTexture;
    private boolean mRunInBackground = false;
    boolean isAttachedWindow = false;
    private Camera mCamera;
    private Camera.Parameters mParam;
    private byte[] previewBuffer;
    private int mCameraId;
    protected int previewformat = ImageFormat.NV21;
    Context context;

    private String mOutFilePath;
    CameraSurfaceViewListener mListener;

    int mType = 0;

    public CameraSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        cameraState = CameraState.START;
        if (cameraStateListener != null) {
            cameraStateListener.onCameraStateChange(cameraState);
        }
        openCamera();
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenOritation = Configuration.ORIENTATION_LANDSCAPE;
        }
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceTexture = new SurfaceTexture(10);
        setOnClickListener(this);
        post(new Runnable() {
            @Override
            public void run() {
                if (!isAttachedWindow) {
                    mRunInBackground = true;
                    startPreview();
                }
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedWindow = true;
    }

    public void openCamera() {
        if (mOpenBackCamera) {
            mCameraId = findCamera(false);
        } else {
            mCameraId = findCamera(true);
        }
        if (mCameraId == -1) {
            mCameraId = 0;
        }
        try {
            mCamera = Camera.open(mCameraId);
        } catch (Exception ee) {
            mCamera = null;
            cameraState = CameraState.ERROR;
            if (cameraStateListener != null) {
                cameraStateListener.onCameraStateChange(cameraState);
            }
        }
        if (mCamera == null) {
            Toast.makeText(context, R.string.failed_to_turn_on_the_camera, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private int findCamera(boolean front) {
        int cameraCount;
        try {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras();
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                int facing = front ? 1 : 0;
                if (cameraInfo.facing == facing) {
                    return camIdx;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean setDefaultCamera(boolean backCamera) {
        if (mOpenBackCamera == backCamera) return false;
        if (isRecording) {
            Toast.makeText(context, R.string.please_finish_recording_first, Toast.LENGTH_SHORT).show();
            return false;
        }
        mOpenBackCamera = backCamera;
        if (mCamera != null) {
            closeCamera();
            openCamera();
            startPreview();
        }
        return true;
    }


    public void closeCamera() {
        stopRecord();
        stopPreview();
        releaseCamera();
    }

    private void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.setPreviewCallbackWithBuffer(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception ee) {
        }
    }

    private boolean isSupportCameraLight() {
        boolean mIsSupportCameraLight = false;
        try {
            if (mCamera != null) {
                Camera.Parameters parameter = mCamera.getParameters();
                Object a = parameter.getSupportedFlashModes();
                if (a == null) {
                    mIsSupportCameraLight = false;
                } else {
                    mIsSupportCameraLight = true;
                }
            }
        } catch (Exception e) {
            mIsSupportCameraLight = false;
            e.printStackTrace();
        }
        return mIsSupportCameraLight;
    }


    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public synchronized void onPreviewFrame(byte[] data, Camera camera) {
            if (data == null) {
                releaseCamera();
                return;
            }
            //you can code media here
            if (cameraState != CameraState.PREVIEW) {
                cameraState = CameraState.PREVIEW;
                if (cameraStateListener != null) {
                    cameraStateListener.onCameraStateChange(cameraState);
                }
            }
            mCamera.addCallbackBuffer(previewBuffer);
        }
    };

    //设置Camera各项参数
    public void startPreview() {
        if (mCamera == null) return;
        try {
            mParam = mCamera.getParameters();
            mParam.setPreviewFormat(previewformat);
            mParam.setRotation(0);

//            Camera.Size previewSize = CamParaUtil.getSize(mParam.getSupportedPreviewSizes(), 1000,
//                    mCamera.new Size(VIDEO_720[0], VIDEO_720[1]));
//            previewSize.width = this.getWidth();
//            previewSize.height = this.getHeight();
//            mParam.setPreviewSize(previewSize.width, previewSize.height);
            List<Camera.Size> sizeList = mParam.getSupportedPreviewSizes();//获取所有支持的camera尺寸
            Camera.Size pictureSize = getOptimalPreviewSize(sizeList, this.getWidth(), this.getHeight());//获取一个最为适配的camera.size
            mParam.setPictureSize(pictureSize.width, pictureSize.height);//把camera.size赋值到parameters
            int yuv_buffersize = pictureSize.width * pictureSize.height * ImageFormat.getBitsPerPixel(previewformat) / 8;
            previewBuffer = new byte[yuv_buffersize];
//            Camera.Size pictureSize = CamParaUtil.getSize(mParam.getSupportedPictureSizes(), 1500,
//                    mCamera.new Size(VIDEO_1080[0], VIDEO_1080[1]));
//            mParam.setPictureSize(pictureSize.width, pictureSize.height);
//            mParam.setPreviewSize(this.getWidth(), this.getHeight());//把camera.size赋值到parameters
//            mParam.setPreviewSize(previewSize.width, previewSize.height);//把camera.size赋值到parameters
            if (CamParaUtil.isSupportedFormats(mParam.getSupportedPictureFormats(), ImageFormat.JPEG)) {
                mParam.setPictureFormat(ImageFormat.JPEG);
                mParam.setJpegQuality(100);
            }
            if (CamParaUtil.isSupportedFocusMode(mParam.getSupportedFocusModes(), FOCUS_MODE_AUTO)) {
                mParam.setFocusMode(FOCUS_MODE_AUTO);
            }
            if (screenOritation != Configuration.ORIENTATION_LANDSCAPE) {
                mParam.set("orientation", "portrait");
                mCamera.setDisplayOrientation(90);
            } else {
                mParam.set("orientation", "landscape");
                mCamera.setDisplayOrientation(0);
            }
            if (mRunInBackground) {
                mCamera.setPreviewTexture(mSurfaceTexture);
                mCamera.addCallbackBuffer(previewBuffer);
//                mCamera.setPreviewCallbackWithBuffer(previewCallback);//设置摄像头预览帧回调
            } else {
                mCamera.setPreviewDisplay(mSurfaceHolder);
//                mCamera.setPreviewCallback(previewCallback);//设置摄像头预览帧回调
            }
            mCamera.setParameters(mParam);
            mCamera.startPreview();
            if (cameraState != CameraState.START) {
                cameraState = CameraState.START;
                if (cameraStateListener != null) {
                    cameraStateListener.onCameraStateChange(cameraState);
                }
            }
        } catch (Exception e) {
            releaseCamera();
            return;
        }
        try {
            String mode = mCamera.getParameters().getFocusMode();
            if (("auto".equals(mode)) || ("macro".equals(mode))) {
                mCamera.autoFocus(null);
            }
        } catch (Exception e) {
        }
    }

    private void stopPreview() {
        if (mCamera == null) return;
        try {
            if (mRunInBackground) {
                mCamera.setPreviewCallbackWithBuffer(null);
                mCamera.stopPreview();
            } else {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
            }
            if (cameraState != CameraState.STOP) {
                cameraState = CameraState.STOP;
                if (cameraStateListener != null) {
                    cameraStateListener.onCameraStateChange(cameraState);
                }
            }
        } catch (Exception ee) {
        }
    }

    @Override
    public void onClick(View v) {
        if (mCamera != null && mType == 0) {
            mCamera.autoFocus(null);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        stopPreview();
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
        if (mRunInBackground)
            startPreview();
    }

    protected CameraState cameraState;
    private CameraStateListener cameraStateListener;

    public enum CameraState {
        START, PREVIEW, STOP, ERROR
    }

    public void setOnCameraStateListener(CameraStateListener listener) {
        this.cameraStateListener = listener;
    }

    public interface CameraStateListener {
        void onCameraStateChange(CameraState paramCameraState);
    }

    /**
     * ___________________________________前/后台运行______________________________________
     **/
    public void setRunBack(boolean b) {
        if (mCamera == null) return;
        if (b == mRunInBackground) return;
        if (!b && !isAttachedWindow) {
            Toast.makeText(context, R.string.cannot_be_displayed, Toast.LENGTH_SHORT).show();
            return;
        }
        mRunInBackground = b;
        if (b)
            setVisibility(View.GONE);
        else
            setVisibility(View.VISIBLE);
    }

    /**
     * ___________________________________开关闪光灯______________________________________
     **/
    public void switchLight(boolean open) {
        if (mCamera == null) return;
        try {
            if (mCamera != null) {
                if (open) {
                    Camera.Parameters parameter = mCamera.getParameters();
                    if (parameter.getFlashMode().equals("off")) {
                        parameter.setFlashMode("torch");
                        mCamera.setParameters(parameter);
                    } else {
                        parameter.setFlashMode("off");
                        mCamera.setParameters(parameter);
                    }
                } else {
                    Camera.Parameters parameter = mCamera.getParameters();
                    if ((parameter.getFlashMode() != null) &&
                            (parameter.getFlashMode().equals("torch"))) {
                        parameter.setFlashMode("off");
                        mCamera.setParameters(parameter);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ___________________________________以下为拍照模块______________________________________
     **/
    public void capture() {
        if (mCamera == null) return;
        mType = 1;
        mCamera.autoFocus(this);
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            if (mType == 1)
                takePicture();
            else if (mType == 2)
                startRecord(true);
        }
    }

    private void takePicture() {
        try {
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Matrix matrix = new Matrix();
                    if (mOpenBackCamera) {
                        matrix.setRotate(90);
                    } else {
                        matrix.setRotate(270);
                        matrix.postScale(-1, 1);
                    }
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    String picPath = newOutFilePath("jpg");
                    FileUtils.saveBitmap(picPath, bitmap);
//                        Toast.makeText(context, "拍照成功", Toast.LENGTH_SHORT).show();
                    if (mListener != null)
                        mListener.complete();
                }
            });
        } catch (Exception e) {
            if (isRecording) {
                Toast.makeText(context, R.string.please_finish_recording_first, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * ___________________________________以下为视频录制模块______________________________________
     **/
    MediaRecorder mediaRecorder = new MediaRecorder();
    private boolean isRecording = false;

    public boolean isRecording() {
        return isRecording;
    }

    public void startRecord(boolean isAutoFocus) {
        if (isAutoFocus)
            startRecord(-1, null);
        else {
            mType = 2;
            mCamera.autoFocus(this);
        }

    }

    public boolean startRecord(int maxDurationMs, MediaRecorder.OnInfoListener onInfoListener) {
        if (mCamera == null) return false;
//        FileUtils.createFile(getOutFilePath());
        mCamera.unlock();
        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        Camera.Size videoSize = CamParaUtil.getSize(mParam.getSupportedVideoSizes(), 1200,
                mCamera.new Size(VIDEO_1080[0], VIDEO_1080[1]));
        mediaRecorder.setVideoSize(videoSize.width, videoSize.height);
        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
//        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());//设置录制预览surface
        if (mOpenBackCamera) {
            mediaRecorder.setOrientationHint(90);
        } else {
            if (screenOritation == Configuration.ORIENTATION_LANDSCAPE)
                mediaRecorder.setOrientationHint(90);
            else
                mediaRecorder.setOrientationHint(270);
        }
        if (maxDurationMs != -1) {
            mediaRecorder.setMaxDuration(maxDurationMs);
            mediaRecorder.setOnInfoListener(onInfoListener);
        }

        String videoPath = newOutFilePath("mp4");
        mediaRecorder.setOutputFile(videoPath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void stopRecord() {
        if (!isRecording) return;
        mediaRecorder.setPreviewDisplay(null);
        try {
            mediaRecorder.stop();
            isRecording = false;
//            Toast.makeText(context, "视频已保存在根目录", Toast.LENGTH_SHORT).show();
            if (mListener != null)
                mListener.complete();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void setListener(CameraSurfaceViewListener listener) {
        this.mListener = listener;
    }

    /**
     * _________________________________________________________________________________________
     **/


    private String newOutFilePath(String type) {
        mOutFilePath = String.format("%s/%s%s." + type, Constants.Config.ROOT_PATH,
                Constants.Config.TEMP_PATH, String.valueOf(System.currentTimeMillis()));
        FileUtils.createFile(mOutFilePath);
//        boolean isExist = com.blankj.utilcode.util.FileUtils.createOrExistsDir(mOutFilePath);
        new File(mOutFilePath).exists();
        return mOutFilePath;
    }

    /**
     * 拍完照或者录完像后获取路径
     *
     * @return
     */
    public String getOutFilePath() {
        return mOutFilePath;
    }

    public interface CameraSurfaceViewListener {
        void complete();
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;         // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }         // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}

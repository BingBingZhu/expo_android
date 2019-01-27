package com.expo.utils;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.expo.base.utils.LogUtils;
import com.gl.BottomArcsTextureView;

import java.io.IOException;
import java.util.List;

public class CameraManager implements ICameraManager {

    public static final int STATE_IDEL = 0;
    public static final int STATE_STARTING = 1;
    public static final int STATE_STOPPED = 2;
    private static final String TAG = "CameraManager";

    private Camera mCamera;
    private View mDisplayView;
    private int mViewWidth;
    private int mViewHeight;
    private boolean mDisplayIsReady;
    private boolean mCustomStarted;
    private boolean mBack = true;
    private int mState = STATE_IDEL;
    private int mOrientation = -1;
    private Camera.PreviewCallback mPreviewCallback;


    private void initCamera() {
        if (!mDisplayIsReady) return;
        int cameraCount = Camera.getNumberOfCameras();
        if (cameraCount == 0)
            throw new UnsupportedOperationException( "No cameras" );
        //只有一个直接开启
        if (cameraCount == 1) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo( 0, info );
            mBack = info.facing == Camera.CameraInfo.CAMERA_FACING_BACK;
            mCamera = Camera.open();
        } else if (cameraCount > 1) {  //多个摄像头按照需要的方向开启
            for (int i = 0; i < cameraCount; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo( i, info );
                if ((info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) == mBack) {
                    mCamera = Camera.open( i );
                    break;
                }
            }
        }
        Camera.Parameters parameters = mCamera.getParameters();
        //连续对焦
        parameters.setFocusMode( Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE );
        //设置预览帧率
        parameters.setPreviewFrameRate( 30 );
        setPictureSize( parameters );
        parameters.setPictureFormat( ImageFormat.JPEG );
        mCamera.setParameters( parameters );
        int orientation = getOrientation();
        switch (orientation) {
            case Surface.ROTATION_0:
                mCamera.setDisplayOrientation( 90 );
                break;
            case Surface.ROTATION_90:
                mCamera.setDisplayOrientation( 0 );
                break;
            case Surface.ROTATION_180:
                mCamera.setDisplayOrientation( 270 );
                break;
            case Surface.ROTATION_270:
                mCamera.setDisplayOrientation( 180 );
                break;
        }
        try {
            //设置预览回调
            if (mPreviewCallback != null) {
                mCamera.setPreviewCallback( mPreviewCallback );
            }
            //判断预览控件设置预览
            if (mDisplayView == null)
                return;
            if (mDisplayView instanceof SurfaceView) {
                mCamera.setPreviewDisplay( ((SurfaceView) mDisplayView).getHolder() );
                mCamera.startPreview();
            } else if (mDisplayView instanceof TextureView) {
                if (mDisplayView instanceof BottomArcsTextureView) {
                    mCamera.setPreviewTexture( ((BottomArcsTextureView) mDisplayView).getRealSurfaceTexture() );
                } else {
                    mCamera.setPreviewTexture( ((TextureView) mDisplayView).getSurfaceTexture() );
                }
                mCamera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getOrientation() {
        if (mOrientation == -1) {
            //根据屏幕旋转方向设置相机显示方向
            WindowManager wm = (WindowManager) mDisplayView.getContext().getSystemService( Context.WINDOW_SERVICE );
            if (wm != null && wm.getDefaultDisplay() != null) {
                int orientation = wm.getDefaultDisplay().getRotation();
                mOrientation = orientation;
            }
        }
        return mOrientation;
    }

    private void setPictureSize(Camera.Parameters parameters) {
        //设置预览尺寸
        Camera.Size previewSize = getOptimalPreviewSize( mViewWidth, mViewHeight );
        if (previewSize != null) {
            parameters.setPreviewSize( previewSize.width, previewSize.height );
            //改变预览视图尺寸
//            changeDisplayViewSize( previewSize );
        }
        //设置拍照尺寸
        Camera.Size pictureSize = getOptimalPictureSize( mViewWidth, mViewHeight );
        if (pictureSize != null)
            parameters.setPictureSize( pictureSize.width, pictureSize.height );
    }

    private void changeDisplayViewSize(Camera.Size previewSize) {
        if (previewSize == null) return;
        int w = previewSize.width;
        int h = previewSize.height;
        int orientation = getOrientation();
        if (orientation == 0 || orientation == 180) {
            w = w ^ h;
            h = w ^ h;
            w = w ^ h;
        }
        if (mViewWidth == w && mViewHeight == h)
            return;
        float ratio = (float) w / h;
        float viewRatio = (float) mViewWidth / mViewHeight;
        if (ratio > viewRatio) {
            mViewWidth = (int) (mViewHeight * ratio);
            ViewGroup.LayoutParams lp = mDisplayView.getLayoutParams();
            lp.width = mViewWidth;
            mDisplayView.setLayoutParams( lp );
        } else {
            mViewHeight = (int) (mViewWidth / ratio);
            ViewGroup.LayoutParams lp = mDisplayView.getLayoutParams();
            lp.height = mViewHeight;
            mDisplayView.setLayoutParams( lp );
        }
    }

    /* 获取最佳的预览画面尺寸 */
    private Camera.Size getOptimalPreviewSize(int w, int h) {
        int orientation = getOrientation();
        //w,h互换
        if (orientation == 0 || orientation == 180) {
            w = w ^ h;
            h = w ^ h;
            w = w ^ h;
        }
        List<Camera.Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs( ratio - targetRatio ) > ASPECT_TOLERANCE) continue;
            if (Math.abs( size.height - targetHeight ) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs( size.height - targetHeight );
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs( size.height - targetHeight ) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs( size.height - targetHeight );
                }
            }
        }
        return optimalSize;
    }

    /* 获取最佳的拍摄画面尺寸 */
    private Camera.Size getOptimalPictureSize(int w, int h) {
        int orientation = getOrientation();
        //w,h互换
        if (orientation == 0 || orientation == 180) {
            w = w ^ h;
            h = w ^ h;
            w = w ^ h;
        }
        List<Camera.Size> sizes = mCamera.getParameters().getSupportedPictureSizes();
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs( ratio - targetRatio ) > ASPECT_TOLERANCE) continue;
            if (Math.abs( size.height - targetHeight ) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs( size.height - targetHeight );
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs( size.height - targetHeight ) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs( size.height - targetHeight );
                }
            }
        }
        return optimalSize;
    }

    /**
     * 开启关闭闪光灯
     *
     * @param on
     */
    @Override
    public void turnOnFlashLamp(boolean on) {
        if (mCamera == null) return;
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode( on ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF );
        mCamera.setParameters( parameters );
    }

    @Override
    public void startPreview() {
        if (mState == STATE_IDEL) {
            mCustomStarted = true;
            initCamera();
        } else if (mState == STATE_STOPPED && mCamera != null) {
            mCamera.startPreview();
        }
    }


    @Override
    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
    public void toggleBackOrFront() {
        if (!haveFrontCamera()) return;
        release();
        mState = STATE_IDEL;
        mBack = !mBack;
        initCamera();
    }

    @Override
    public void setFacing(boolean back) {
        mBack = back;
    }

    @Override
    public boolean haveFrontCamera() {
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo( i, info );
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void release() {
        if (mCamera == null) return;
        if (mState == STATE_STARTING) {
            mCamera.stopPreview();
        }
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void setDisplayView(TextureView displayView) {
        mDisplayView = displayView;
        mDisplayIsReady = canGetDisplayViewSize();
        displayView.setSurfaceTextureListener( mSurfaceTextureListener );
    }

    @Override
    public void setDisplayView(SurfaceView displayView) {
        mDisplayView = displayView;
        mDisplayIsReady = canGetDisplayViewSize();
        displayView.getHolder().addCallback( mSurfaceCallback );
    }

    @Override
    public void setPreviewCallback(Camera.PreviewCallback callback) {
        mPreviewCallback = callback;
    }

    @Override
    public void tackPicture(Camera.PictureCallback callback) {
        mCamera.takePicture( null, null, callback );
    }

    /*
     * 获取显示控件的尺寸
     * @return true 可获取到  false  无法获取
     */
    private boolean canGetDisplayViewSize() {
        if (mDisplayView.getWidth() != 0) {
            mViewWidth = mDisplayView.getWidth();
            mViewHeight = mDisplayView.getHeight();
            return true;
        }
        if (mDisplayView.getMeasuredWidth() != 0) {
            mViewWidth = mDisplayView.getMeasuredWidth();
            mViewHeight = mDisplayView.getMeasuredHeight();
            return true;
        }
        ViewGroup.LayoutParams lp = mDisplayView.getLayoutParams();
        if (lp != null && lp.width > 0 && lp.height > 0) {
            mViewWidth = lp.width;
            mViewHeight = lp.height;
            return true;
        }
        return false;
    }

    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mDisplayIsReady = true;
            mViewWidth = mDisplayView.getWidth();
            mViewHeight = mDisplayView.getHeight();
            if (mCustomStarted)
                startPreview();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mViewWidth = width;
            mViewHeight = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            release();
        }
    };

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mDisplayIsReady = true;
            mViewWidth = width;
            mViewHeight = height;
            if (mCustomStarted)
                startPreview();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            if (mViewHeight != height) {
                mViewHeight = height;
            }
            if (mViewWidth != width) {
                mViewWidth = width;
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            release();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
}

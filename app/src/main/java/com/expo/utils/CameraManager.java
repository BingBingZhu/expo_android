package com.expo.utils;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.expo.base.utils.LogUtils;

import java.io.IOException;
import java.util.List;

public class CameraManager {

    private static final String TAG = "CameraManager";
    private CameraManager mInstance;

    private Camera mCamera;
    private int mFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
    private View mDisplayView;
    private Camera.Size mPreviewSize;
    private Camera.Size mPictureSize;
    private Camera.PreviewCallback previewCallback;
    private int mViewWidth;
    private int mViewHeight;
    private int mOrientation;

    public CameraManager(View displayView) {
        if (displayView == null)
            throw new ExceptionInInitializerError( "Display View can not be empty" );
        this.mDisplayView = displayView;
        if (mDisplayView instanceof TextureView) {
            ((TextureView) mDisplayView).setSurfaceTextureListener( mTextureListener );
        } else if (mDisplayView instanceof SurfaceView) {
            ((SurfaceView) mDisplayView).getHolder().addCallback( mHolderCallback );
        }
        //获取屏幕旋转方向
        mOrientation = getScreenOrientation();
    }

    private void computeSize(int width, int height) {
        mPreviewSize = getOptimalPreviewSize( width, height );
        mPictureSize = getOptimalPictureSize( width, height );
    }

    public Camera.Size getPreviewSize() {
        return mPreviewSize;
    }

    public Camera.Size getPictureSize() {
        return mPictureSize;
    }

    /* 开启相机 */
    private void openCamera() {
        int cameraCount = Camera.getNumberOfCameras();
        if (cameraCount == 0)
            throw new UnsupportedOperationException( "No cameras" );
        //只有一个直接开启
        if (cameraCount == 1) {
            mCamera = Camera.open();
        } else if (cameraCount > 1) {  //多个摄像头按照需要的方向开启
            for (int i = 0; i < cameraCount; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo( i, info );
                if (info.facing == mFacing) {
                    mCamera = Camera.open( i );
                    break;
                }
            }
        }
        initSetting();
    }

    /**
     * 开始预览
     */
    public void startPreview() {
        if (mCamera == null) {
            openCamera();
        }
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    /**
     * 销毁释放资源
     */
    private void destroyCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback( null );
            mCamera.release();
            mCamera = null;
        }
    }

    /* 初始化设置 */
    private void initSetting() {
        Camera.Parameters parameters = mCamera.getParameters();
        //连续对焦
        parameters.setFocusMode( Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE );
        //设置预览帧率
        parameters.setPreviewFrameRate( 30 );
        //设置预览尺寸
        Camera.Size previewSize = getOptimalPreviewSize( mViewWidth, mViewHeight );
        parameters.setPreviewSize( previewSize.width, previewSize.height );
        //改变预览视图尺寸
        changeDisplayViewSize( previewSize );
        LogUtils.d( TAG, "preview size------>>>[" + previewSize.width + "," + previewSize.height + "]" );
        LogUtils.d( TAG, "view size------>>>[" + mViewWidth + "," + mViewHeight + "]" );
        //设置拍照尺寸
        Camera.Size pictureSize = getOptimalPictureSize( mViewWidth, mViewHeight );
        parameters.setPictureSize( pictureSize.width, pictureSize.height );
        LogUtils.d( TAG, "picture size------>>>[" + pictureSize.width + "," + pictureSize.height + "]" );
        //拍照图片的格式
        parameters.setPictureFormat( ImageFormat.JPEG );
        mCamera.setParameters( parameters );
        //根据屏幕旋转方向设置相机显示方向
        switch (mOrientation) {
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
            //判断预览控件设置预览
            if (mDisplayView != null && mDisplayView instanceof SurfaceView) {
                mCamera.setPreviewDisplay( ((SurfaceView) mDisplayView).getHolder() );
            } else if (mDisplayView != null && mDisplayView instanceof TextureView) {
                mCamera.setPreviewTexture( ((TextureView) mDisplayView).getSurfaceTexture() );
            }
            //设置预览回调
            if (previewCallback != null) {
                mCamera.setPreviewCallback( previewCallback );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeDisplayViewSize(Camera.Size previewSize) {
        int height = mViewWidth * previewSize.width / previewSize.height;
        if (mViewHeight != height) {
            mViewHeight = height;
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mDisplayView.getLayoutParams();
            lp.height = mViewHeight;
            mDisplayView.setLayoutParams( lp );
        }
    }

    /**
     * 开启关闭闪光灯
     *
     * @param open
     */
    public void toggleLight(boolean open) {
        if (mCamera == null) return;
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode( open ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF );
        mCamera.setParameters( parameters );
    }

    public void setPreviewCallback(Camera.PreviewCallback previewCallback) {
        this.previewCallback = previewCallback;
    }

    private SurfaceHolder.Callback mHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            destroyCamera();
        }
    };

    private TextureView.SurfaceTextureListener mTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mViewWidth = width;
            mViewHeight = height;
            startPreview();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            destroyCamera();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    /**
     * 拍照
     *
     * @param callback
     */
    public void takePicture(Camera.PictureCallback callback) {
        if (mCamera == null) return;
        mCamera.takePicture( null, null, callback );
//        mCamera.stopPreview();
    }

    /* 获取屏幕旋转方向 */
    private int getScreenOrientation() {
        WindowManager wm = (WindowManager) mDisplayView.getContext().getSystemService( Context.WINDOW_SERVICE );
        return wm.getDefaultDisplay().getOrientation();
    }

    /* 获取最佳的预览画面尺寸 */
    private Camera.Size getOptimalPreviewSize(int w, int h) {
        //w,h互换
        if (mOrientation == 0 || mOrientation == 180) {
            w = w ^ h;
            h = w ^ h;
            w = w ^ h;
        }
        List<Camera.Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
        Camera.Size optimalSize = null;
        double targetRatio = (double) w / h;
        double ratioMinDiff = 1;
        int heightMinDiff = h;
        if (sizes == null) return null;
        for (Camera.Size size : sizes) {
            if (size.width < w / 2)
                continue;
            double ratio = Math.abs( (double) size.width / size.height - targetRatio );
            if (ratio > ratioMinDiff) {
                continue;
            } else if (ratio == ratioMinDiff) {
                int height = Math.abs( h - size.height );
                if (height < heightMinDiff) {
                    heightMinDiff = height;
                    optimalSize = size;
                }
            } else {
                heightMinDiff = h;
                ratioMinDiff = ratio;
                optimalSize = size;
            }
        }
        return optimalSize;
    }

    /* 获取最佳的预览画面尺寸 */
    private Camera.Size getOptimalPictureSize(int w, int h) {
        if (mOrientation == 0 || mOrientation == 180) {
            w = w ^ h;
            h = w ^ h;
            w = w ^ h;
        }
        List<Camera.Size> sizes = mCamera.getParameters().getSupportedPictureSizes();
        Camera.Size optimalSize = null;
        double targetRatio = (double) w / h;
        double ratioMinDiff = 1;
        int heightMinDiff = h;
        if (sizes == null) return null;
        for (Camera.Size size : sizes) {
            double ratio = Math.abs( (double) size.width / size.height - targetRatio );
            if (ratio > ratioMinDiff) {
                continue;
            } else if (ratio == ratioMinDiff) {
                int height = Math.abs( h - size.height );
                if (height < heightMinDiff) {
                    heightMinDiff = height;
                    optimalSize = size;
                }
            } else {
                heightMinDiff = h;
                ratioMinDiff = ratio;
                optimalSize = size;
            }
        }
        return optimalSize;
    }
}

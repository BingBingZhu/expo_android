package com.expo.module.online.detail.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.module.online.detail.VRImageActivity;
import com.expo.utils.Constants;
import com.expo.utils.MD5Util;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VRImageView implements View.OnClickListener, VRInterfaceView {

    private Context mContext;
    private View mView;

    private VrPanoramaView mPanoramaView;
    public ImageView mFullScreen;
    ImageTask mImageTask;

    String mCurrentUrl;

    public VRImageView(Context context) {
        init(context);
        createView(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    private void createView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.vr_detail_image, null);
        mPanoramaView = mView.findViewById(R.id.m_panorama_view);
        mFullScreen = mView.findViewById(R.id.full_screen);
        mFullScreen.setOnClickListener(this);

        mPanoramaView.setFullscreenButtonEnabled(false);
        mPanoramaView.setInfoButtonEnabled(false);
        mPanoramaView.setStereoModeButtonEnabled(false);

    }

    public View getVrVideoView() {
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.full_screen:
                showFullSceen();
                break;
        }
    }

    public void showFullSceen() {
        VRImageActivity.startActivity(mContext);

    }

    public void showNormalSceen() {
        mPanoramaView.setDisplayMode(1);
    }

    public void showVrSceen() {
        mPanoramaView.setDisplayMode(3);

    }

    public void setUrl(String url) {
        mCurrentUrl = url;
        if (!getImage(url)) {
            new ImageTask().execute(url);
        }
    }

    public void onDestroy() {
        if (mImageTask != null && !mImageTask.isCancelled()) {//销毁任务
            mImageTask.cancel(true);
            mImageTask = null;
        }
        mPanoramaView.pauseRendering();
        mPanoramaView.shutdown();
        ((FrameLayout) mView.getParent()).removeView(mView);
        mView = null;
    }

    private boolean getImage(String url) {
        String path = Constants.Config.ROOT_PATH + File.separator + Constants.Config.TEMP_PATH + MD5Util.getMD5String(url);
        if (FileUtils.isFileExists(path)) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(path));

                VrPanoramaView.Options options = new VrPanoramaView.Options();
                options.inputType = VrPanoramaView.Options.TYPE_MONO;
                mPanoramaView.setEventListener(mVrPanoramaEventListener);
                mPanoramaView.loadImageFromBitmap(bitmap, options);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*     * 访问网络数据     *     * */
    private class ImageTask extends AsyncTask<String, Void, Bitmap> {
        String url;

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                //2.4.1.获取网络流
                url = params[0];
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //2.5.在主线程展示位图
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                ImageUtils.save(bitmap, Constants.Config.ROOT_PATH + File.separator + Constants.Config.TEMP_PATH + MD5Util.getMD5String(url), Bitmap.CompressFormat.JPEG);
                if (!StringUtils.equals(url, mCurrentUrl)) return;
                VrPanoramaView.Options options = new VrPanoramaView.Options();
                options.inputType = VrPanoramaView.Options.TYPE_MONO;
                //3.1.监听加载过程
                mPanoramaView.setEventListener(mVrPanoramaEventListener);
                mPanoramaView.loadImageFromBitmap(bitmap, options);
            }
        }

    }

    VrPanoramaEventListener mVrPanoramaEventListener = new VrPanoramaEventListener() {
        //3.1.1.加载bitmap异常
        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
        }

        //3.1.2.加载bitmap成功
        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();
        }

        @Override
        public void onClick() {
            super.onClick();
        }
    };

}

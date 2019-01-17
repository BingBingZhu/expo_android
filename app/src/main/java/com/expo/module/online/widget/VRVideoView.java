package com.expo.module.online.widget;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.expo.R;
import com.google.vr.sdk.widgets.common.FullScreenDialog;
import com.google.vr.sdk.widgets.common.VrWidgetRenderer;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;
import java.lang.reflect.Field;

public class VRVideoView implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static VRVideoView mVRVrVideoView;
    private Context mContext;
    private View mView;

    private TextView mTips;
    private TextView mDialog;

    private VrVideoView mVrVideoView;
    private SeekBar mSeekBar;
    private ImageView mPlay;
    private TextView mPlayTime;
    private TextView mPlayTotalTime;
    private ImageView mFullScreen;
    private FrameLayout mFullScreenView;

    boolean mIsPlay;

    FullScreenDialog mFullScreenDialog;

    private static final Object LOCK = new Object();

    public static VRVideoView getInstance(Context context) {
        if (mVRVrVideoView == null) {
            synchronized (LOCK) {
                if (mVRVrVideoView == null) {
                    mVRVrVideoView = new VRVideoView();
                    mVRVrVideoView.init(context);
                    mVRVrVideoView.createView(context);
                }
            }
        }
        return mVRVrVideoView;
    }

    private void init(Context context) {
        mContext = context;
        mIsPlay = false;
    }

    private void createView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.vr_detail_video, null);
        mVrVideoView = mView.findViewById(R.id.vr_video_view);
        mPlay = mView.findViewById(R.id.play);
        mPlayTime = mView.findViewById(R.id.play_time);
        mPlayTotalTime = mView.findViewById(R.id.play_total_time);
        mFullScreen = mView.findViewById(R.id.full_screen);
        mSeekBar = mView.findViewById(R.id.seek_bar);

        /**设置加载设置**/
        VrVideoView.Options options = new VrVideoView.Options();
        options.inputType = VrVideoView.Options.TYPE_STEREO_OVER_UNDER;

        mVrVideoView.setInfoButtonEnabled(false);
        mVrVideoView.setStereoModeButtonEnabled(false);
        mVrVideoView.setFullscreenButtonEnabled(false);
        mSeekBar.setOnSeekBarChangeListener(this);

        mPlay.setOnClickListener(this);
        mFullScreen.setOnClickListener(this);

        mVrVideoView.setEventListener(new VrVideoEventListener() {
            /**
             * 视频播放完成回调
             */
            @Override
            public void onCompletion() {
                super.onCompletion();
                /**播放完成后跳转到开始重新播放**/
                mVrVideoView.seekTo(0);
                playVideo(false);
            }

            /**
             * 加载每一帧视频的回调
             */
            @Override
            public void onNewFrame() {
                super.onNewFrame();
                mSeekBar.setProgress((int) mVrVideoView.getCurrentPosition());
            }

            /**
             * 点击VR视频回调
             */
            @Override
            public void onClick() {
                super.onClick();
            }

            /**
             * 加载VR视频失败回调
             * @param errorMessage
             */
            @Override
            public void onLoadError(String errorMessage) {
                super.onLoadError(errorMessage);
            }

            /**
             * 加载VR视频成功回调
             */
            @Override
            public void onLoadSuccess() {
                super.onLoadSuccess();
                /**加载成功后设置回调**/
                mSeekBar.setMax((int) mVrVideoView.getDuration());
            }

            /**
             * 显示模式改变回调
             * 1.默认
             * 2.全屏模式
             * 3.VR观看模式，即横屏分屏模式
             * @param newDisplayMode 模式
             */
            @Override
            public void onDisplayModeChanged(int newDisplayMode) {
                super.onDisplayModeChanged(newDisplayMode);
            }
        });
    }

    public View getVrVideoView() {
        return mView;
    }

    private void setEnable(boolean enable) {
        mPlay.setEnabled(enable);
        mPlayTime.setEnabled(enable);
        mPlayTotalTime.setEnabled(enable);
        mFullScreen.setEnabled(enable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                playVideo(!mIsPlay);
                break;
            case R.id.full_screen:
                showFullSceen();
                break;
        }
    }

    public void showFullSceen() {
        getFullScreenDialog();
        mVrVideoView.setDisplayMode(2);
    }

    public void showNormalSceen() {
        mVrVideoView.setDisplayMode(1);
    }

    public void showVrSceen() {
        mVrVideoView.setDisplayMode(3);

    }

    public void setUrl(String url) {
        try {
            mVrVideoView.loadVideo(Uri.parse(url), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setmFullScreen(FrameLayout fullScreen){
        mFullScreenView = fullScreen;
    }

    private void playVideo(boolean isPlay) {
        mIsPlay = isPlay;
        if (mIsPlay) {
            mVrVideoView.playVideo();
        } else {
            mVrVideoView.pauseVideo();
        }
    }

    private FullScreenDialog getFullScreenDialog() {
        if (mFullScreenDialog == null) {
            synchronized (LOCK) {
                if (mFullScreenDialog == null) {
                    try {
                        Class<?> classu = mVrVideoView.getClass();
                        classu.getFields();
                        for(Field fs: classu.getFields()){
                            System.out.println("fs.getName:" + fs.getName());
                        }
                        Field field1 = classu.getField("innerWidgetView");
                        ViewGroup innerWidgetView = (ViewGroup) field1.get(mVrVideoView);
                        Field field2 = classu.getField("renderer");
                        VrWidgetRenderer renderer = (VrWidgetRenderer) field2.get(mVrVideoView);
                        mFullScreenDialog = new FullScreenDialog(mContext, innerWidgetView, renderer);
                        Field field3 = classu.getField("fullScreenDialog");
                        field3.setAccessible(true);
                        field3.set(mVrVideoView, mFullScreenDialog);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mFullScreenDialog;
    }

    public void onDestroy() {
        mVrVideoView.pauseRendering();
        mVrVideoView.shutdown();
        ((FrameLayout) mView.getParent()).removeView(mView);
        mView = null;
        mVRVrVideoView = null;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            /**调节视频进度**/
            mVrVideoView.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}

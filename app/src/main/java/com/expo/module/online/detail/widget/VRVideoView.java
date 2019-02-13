package com.expo.module.online.detail.widget;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;
import com.expo.entity.VrInfo;
import com.expo.network.Http;
import com.expo.utils.CommUtils;
import com.google.vr.sdk.widgets.common.FullScreenDialog;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class VRVideoView implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, VRInterfaceView {

    private Context mContext;
    private View mView;

    private VrVideoView mVrVideoView;
    private SeekBar mSeekBar;
    private ImageView mPlay;
    private TextView mPlayTime;
    private TextView mPlayTotalTime;
    private ImageView mFullScreen;
    View mNoWifiLayout;
    TextView mNoWifiPlay;

    boolean mIsPlay;
    VrInfo mVrInfo;

    public VRVideoView(Context context) {
        init(context);
        createView(context);
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
        mNoWifiLayout = mView.findViewById(R.id.no_wifi_layout);
        mNoWifiPlay = mView.findViewById(R.id.no_wifi_play);

        /**设置加载设置**/
        VrVideoView.Options options = new VrVideoView.Options();
        options.inputType = VrVideoView.Options.TYPE_STEREO_OVER_UNDER;

        mVrVideoView.setInfoButtonEnabled(false);
        mVrVideoView.setStereoModeButtonEnabled(false);
        mVrVideoView.setFullscreenButtonEnabled(false);
        mSeekBar.setOnSeekBarChangeListener(this);

        mPlay.setOnClickListener(this);
        mFullScreen.setOnClickListener(this);
        mNoWifiPlay.setOnClickListener(this);

        mVrVideoView.fullScreenDialog.setVrVideoView(mVrVideoView);
        mVrVideoView.fullScreenDialog.setListener(new FullScreenDialog.FullScreenClickListener() {
            @Override
            public void play() {
                VRVideoView.this.playVideo(!mIsPlay);
            }
        });

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
                mVrVideoView.fullScreenDialog.setProgress((int) mVrVideoView.getCurrentPosition());

                mPlayTime.setText(TimeUtils.millis2String((int) mVrVideoView.getCurrentPosition(), new SimpleDateFormat("mm:ss")));
                mVrVideoView.fullScreenDialog.setPlayTime(TimeUtils.millis2String((int) mVrVideoView.getCurrentPosition(), new SimpleDateFormat("mm:ss")));
            }

            /**
             * 点击VR视频回调
             */
            @Override
            public void onClick() {
                super.onClick();
                if (mVrVideoView.getDisplayMode() == 3) {
                    mVrVideoView.fullScreenDialog.setIsShowControl(false);
                } else {
                    mVrVideoView.fullScreenDialog.setIsShowControl(!mVrVideoView.fullScreenDialog.mIsShowControl);
                }
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
                mPlayTotalTime.setText(TimeUtils.millis2String((int) mVrVideoView.getDuration(), new SimpleDateFormat("mm:ss")));
                mVrVideoView.fullScreenDialog.setTotalTime(TimeUtils.millis2String((int) mVrVideoView.getDuration(), new SimpleDateFormat("mm:ss")));
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
            case R.id.no_wifi_play:
                startPlay(CommUtils.getPanoramaFullUrl(getImageString(mVrInfo)));
                break;
        }
    }

    private String getImageString(VrInfo vrInfo) {
        String[] urlArray = vrInfo.getUrlArray();
        String networkType = Http.getNetworkType().toLowerCase();
        if (!networkType.contains("wifi") && urlArray.length >= 2) {
            if (urlArray.length == 2) {
                return urlArray[1];
            } else if (urlArray.length == 3) {
                return urlArray[2];
            }else{
                return urlArray[0];
            }
        } else {
            return urlArray[0];
        }
    }

    public void showFullSceen() {
        mVrVideoView.setDisplayMode(2);
        mVrVideoView.fullScreenDialog.setMaxDuration((int) mVrVideoView.getDuration());
        mVrVideoView.fullScreenDialog.setIsShowControl(true);
    }

    public void showNormalSceen() {
        mVrVideoView.setDisplayMode(1);
    }

    public void showVrSceen() {
        mVrVideoView.fullScreenDialog.setIsShowControl(false);
        mVrVideoView.setDisplayMode(3);

    }

    public void setVrInfo(VrInfo vrInfo) {
        mVrInfo = vrInfo;
        if (NetworkUtils.isWifiConnected()) {
            mNoWifiLayout.setVisibility(View.GONE);
            startPlay(CommUtils.getPanoramaFullUrl(getImageString(vrInfo)));
        } else {
            mNoWifiLayout.setVisibility(View.VISIBLE);
        }
    }

    private void startPlay(String url) {
        try {
            mNoWifiLayout.setVisibility(View.GONE);
            VrVideoView.Options options = new VrVideoView.Options();
            options.inputFormat = VrVideoView.Options.FORMAT_HLS;
            mVrVideoView.loadVideo(Uri.parse(url), options);

            mPlay.setSelected(true);
            mVrVideoView.fullScreenDialog.playVideo(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playVideo(boolean isPlay) {
        mIsPlay = isPlay;
        if (mIsPlay) {
            mVrVideoView.playVideo();
        } else {
            mVrVideoView.pauseVideo();
        }
        mPlay.setSelected(isPlay);
        mVrVideoView.fullScreenDialog.playVideo(isPlay);
    }

    public void onDestroy() {
        mVrVideoView.pauseRendering();
        mVrVideoView.shutdown();
        if (mView.getParent() != null)
            ((FrameLayout) mView.getParent()).removeView(mView);
        mView = null;
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

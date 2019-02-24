package com.expo.utils.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.expo.R;
import com.expo.utils.Constants;

public class MediaPlayUtil {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_PLAY = 1;

    private Context mContext;

    public MediaPlayer mediaPlayer;
    public MediaPlayerProxy mediaPlayerHttpProxy;//当前播放器的在线播放代理（用于边播边存等处理）

    private Handler refreshPlayDegreeHandler = new Handler();
    private int currentState = STATE_NORMAL;
    private int currMusicProgress;

    private static MediaPlayUtil mMediaPlayUtil;
    private static final Object LOCK = new Object();

    public static MediaPlayUtil getInstence() {
        if (mMediaPlayUtil == null)
            synchronized (LOCK) {
                if (mMediaPlayUtil == null)
                    mMediaPlayUtil = new MediaPlayUtil();
            }
        return mMediaPlayUtil;
    }

    /**
     * 初始化播放器
     */
    public void initMediaPlayer(Context context) {
        Log.i("========voice==util====", "init");
        mContext = context;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.reset();
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            Log.i("========voice==util====", "结束");
            // 播放结束
            if (null != voicePlayListener){
                voicePlayListener.playOver();
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.i("========voice==util====", "播放");
                mediaPlayer.start();
                if (null != voicePlayListener){
                    voicePlayListener.playStart();
                }
//            refreshPlayDegreeTask();
            }
        });
    }

    private VoicePlayListener voicePlayListener;

    public interface VoicePlayListener{
        void playOver();
        void playStart();
        void playError();
    }

    public void setOnVoicePlayListener(VoicePlayListener voicePlayListener){
        this.voicePlayListener = voicePlayListener;
    }

//    private void refreshPlayDegreeTask() {
//        refreshPlayDegreeHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updatePlayingTime();
//            }
//        }, 1000);
//    }
//
//    private void updatePlayingTime() {
//        try {
//            int poistion = mediaPlayer.getCurrentPosition();
//            currMusicProgress = (int) (Common.div(poistion, mediaPlayer.getDuration(), 5) * 100);
//            if (currMusicProgress <= 100) {
//                refreshPlayDegreeTask();
//            }
//        } catch (Exception e) {
//            LogTool.ex(e);
//        }
//    }

    /**
     * 开始播放
     * @param url
     */
    public void startPlay(String url) {
        switch (currentState) {
            case STATE_NORMAL:
                playMusic(url);
                break;
            case STATE_PLAY:
                Log.i("========voice==util====", "startPlay---stopMusic");
                stopMusic();
                break;
        }
    }

    /**
     * 停止播放
     */
    public void stopMusic() {
        Log.i("========voice==util====", "stopMusic");
        if (null != mediaPlayer) {
            currentState = STATE_NORMAL;
            mediaPlayer.pause();
            mediaPlayer.stop();
        }
    }

    private void playMusic(String url) {
        Log.i("========voice==util====", "playMusic");
        currentState = STATE_PLAY;
        if (mediaPlayer != null && mediaPlayer.getCurrentPosition() > 0) {
            Log.i("========voice==util====", "if");
            mediaPlayerHttpProxy = null;
            mediaPlayer.start();
        } else {
            Log.i("========voice==util====", "else");
            try {
                mediaPlayerHttpProxy = new MediaPlayerProxy( url, true );
                mediaPlayerHttpProxy.setOnCaChedProgressUpdateListener(new MediaPlayerProxy.OnCaChedProgressUpdateListener() {
                    @Override
                    public void updateCachedProgress(int progress) {
                        // 播放进度
                    }

                    @Override
                    public void notFoundResource() {
                        Log.i("========voice==util====", "资源未找到");
                        // 资源未找到
                        if (null != voicePlayListener){
                            voicePlayListener.playError();
                        }
                    }
                });
                Log.i("========voice==util====", "url:  "+Constants.URL.FILE_BASE_URL + url);
                String localProxyUrl = mediaPlayerHttpProxy.getLocalURLAndSetRemotSocketAddr(Constants.URL.FILE_BASE_URL + url);
                mediaPlayerHttpProxy.startProxy();
                Log.i("========voice==util====", "DataSource url:  "+localProxyUrl);
                mediaPlayer.setDataSource(localProxyUrl);
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                LogTool.ex(e);
            }
        }
    }
}

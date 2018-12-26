package com.expo.utils.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
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
    private int currentState;
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
        mContext = context;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.reset();
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            // 播放结束
        });
        mediaPlayer.setOnPreparedListener(mp -> {
            mediaPlayer.start();
            refreshPlayDegreeTask();
        });
    }

    private void refreshPlayDegreeTask() {
        refreshPlayDegreeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updatePlayingTime();
            }
        }, 1000);
    }

    private void updatePlayingTime() {
        try {
            int poistion = mediaPlayer.getCurrentPosition();
            currMusicProgress = (int) (Common.div(poistion, mediaPlayer.getDuration(), 5) * 100);
            if (currMusicProgress <= 100) {
                refreshPlayDegreeTask();
            }
        } catch (Exception e) {
            LogTool.ex(e);
        }
    }

    /**
     * 开始播放
     * @param url
     */
    public void startPlay(String url, ImageView imgView) {
        switch (currentState) {
            case STATE_NORMAL:
                playMusic(url);
                imgView.setImageResource(R.mipmap.ico_audio_play);
                break;
            case STATE_PLAY:
                stopMusic();
                imgView.setImageResource(R.mipmap.ico_audio_pause);
                break;
        }
    }

    /**
     * 停止播放
     */
    public void stopMusic() {
        if (null != mediaPlayer) {
            currentState = STATE_NORMAL;
            mediaPlayer.pause();
//            mediaPlayer.stop();
        }
    }

    private void playMusic(String url) {
        currentState = STATE_PLAY;
        if (mediaPlayer != null && mediaPlayer.getCurrentPosition() > 0) {
            mediaPlayerHttpProxy = null;
            mediaPlayer.start();
        } else {
            try {
                mediaPlayerHttpProxy = new MediaPlayerProxy("url", true);
                mediaPlayerHttpProxy.setOnCaChedProgressUpdateListener(new MediaPlayerProxy.OnCaChedProgressUpdateListener() {
                    @Override
                    public void updateCachedProgress(int progress) {
                        // 播放进度
                    }
                });
                String localProxyUrl = mediaPlayerHttpProxy.getLocalURLAndSetRemotSocketAddr(Constants.URL.FILE_BASE_URL + url);
                mediaPlayerHttpProxy.startProxy();
                mediaPlayer.setDataSource(localProxyUrl);
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                LogTool.ex(e);
            }
        }
    }
}

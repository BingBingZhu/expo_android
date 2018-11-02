package com.expo.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;

import java.io.IOException;

public class MediaPlayerManager implements MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mMediaPlayer;

    private static MediaPlayerManager mMediaPlayerManager = new MediaPlayerManager();
    private static final Object LOCK = new Object();

    private int mDuration;
    private int mProgress;
    Mythred mMyThred;


    public static MediaPlayerManager getInstence() {
        if (mMediaPlayerManager == null)
            synchronized (LOCK) {
                if (mMediaPlayerManager == null)
                    mMediaPlayerManager = new MediaPlayerManager();
            }
        return mMediaPlayerManager;
    }

    public void start(Context context, String url) {
        try {
            if (mMediaPlayer == null) initMediaPlayer();
            mMediaPlayer.reset();
//            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setDataSource(context, Uri.parse(url));
            mMediaPlayer.prepareAsync();
            if (mMyThred != null) {
                mMyThred.exit();
                mMyThred = null;
            }
            mMyThred = new Mythred();
            mMyThred.start();
            if (mListener != null) {
                mListener.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    public void setSeek(int progress) {
        if (mMediaPlayer == null) return;
        mMediaPlayer.seekTo(progress);
    }

    public void onDestory() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying())
            mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        if (mMyThred != null)
            mMyThred.exit();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        int secendProssed = mMediaPlayer.getDuration() / 100 * percent;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mListener != null)
            mListener.complete();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        mDuration = mMediaPlayer.getDuration();
        if (mListener != null)
            mListener.setDuration(mDuration);
    }

    MediaPlayerManagerListener mListener;

    public void setListener(MediaPlayerManagerListener listener) {
        this.mListener = listener;
    }

    public interface MediaPlayerManagerListener {
        void start();

        void setProgress(int seek);

        void setDuration(int duration);

        void error(String error);

        void complete();
    }

    class Mythred extends Thread {

        boolean isExit = false;

        @Override
        public void run() {
            super.run();
            while (!isExit) {
                //设置进度条的进度
                //得到当前音乐的播放位置
                mProgress = mMediaPlayer.getCurrentPosition();
                //让进度条每一秒向前移动
                if (mListener != null) {
                    mListener.setProgress(mProgress);
                }
                SystemClock.sleep(1000);
            }
        }

        public void exit() {
            isExit = true;
        }
    }
}

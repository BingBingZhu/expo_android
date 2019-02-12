//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.vr.sdk.widgets.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.expo.R;
import com.google.vr.cardboard.FullscreenMode;
import com.google.vr.sdk.widgets.video.VrVideoView;

public class FullScreenDialog extends Dialog implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private Context mContext;
    private final View innerWidgetView;
    private ViewGroup innerWidgetViewParent;
    private VrWidgetRenderer renderer;
    private View dialogView;
    private FullscreenMode fullscreenMode;

    private VrVideoView mVrVideoView;

    private View layoutTitle;
    private View layoutBottom;
    private FrameLayout frameLayout;
    private TextView titleName;
    private ImageView play;
    private TextView playTime;
    private TextView totalTime;
    private SeekBar seekBar;

    public boolean mIsShowControl;

    FullScreenClickListener mListener;
    boolean mIsLandscape;

    public FullScreenDialog(Context context, View innerWidgetView, VrWidgetRenderer renderer) {
        super(context, 16973834);
        mContext = context;
        this.innerWidgetView = innerWidgetView;
        this.renderer = renderer;
        this.dialogView = LayoutInflater.from(context).inflate(R.layout.layout_vr_fullscreen, null);
        this.setContentView(this.dialogView);

        layoutTitle = dialogView.findViewById(R.id.layout_title);
        layoutBottom = dialogView.findViewById(R.id.layout_bottom);
        frameLayout = dialogView.findViewById(R.id.frame);
        titleName = dialogView.findViewById(R.id.title_name);
        play = dialogView.findViewById(R.id.play);
        playTime = dialogView.findViewById(R.id.play_time);
        totalTime = dialogView.findViewById(R.id.play_total_time);
        seekBar = dialogView.findViewById(R.id.seek_bar);

        dialogView.findViewById(R.id.close).setOnClickListener(this);
        dialogView.findViewById(R.id.play).setOnClickListener(this);
        dialogView.findViewById(R.id.full_eye).setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        setIsShowControl(true);
    }

    public void setVrVideoView(VrVideoView vrVideoView) {
        this.mVrVideoView = vrVideoView;
    }

    public void setListener(FullScreenClickListener listener) {
        this.mListener = listener;
    }

    protected void onStart() {
        super.onStart();
        this.renderer.onViewDetach();
        this.innerWidgetViewParent = (ViewGroup) this.innerWidgetView.getParent();
        this.innerWidgetViewParent.removeView(this.innerWidgetView);
        this.frameLayout.addView(this.innerWidgetView, -1, -1);
    }

    protected void onStop() {
        this.renderer.onViewDetach();
        this.frameLayout.removeView(this.innerWidgetView);
        this.innerWidgetViewParent.addView(this.innerWidgetView);
        super.onStop();
    }

    public void showLandscape(boolean isLandscape) {
        mIsLandscape = isLandscape;
        if (isLandscape) {
            ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setIsShowControl(false);
        }
        super.show();
        this.fullscreenMode = new FullscreenMode(this.getWindow());
        this.fullscreenMode.goFullscreen();
    }

    public void dismiss() {
        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.dismiss();
    }

    public void setMaxDuration(int duration) {
        seekBar.setMax(duration);
    }

    public void setProgress(int progress) {
        seekBar.setProgress(progress);
    }

    public void setIsShowControl(boolean isShowControl) {
        if (!mIsLandscape) mIsShowControl = false;
        mIsShowControl = isShowControl;
        if (mIsShowControl) {
            layoutTitle.setVisibility(View.VISIBLE);
            layoutBottom.setVisibility(View.VISIBLE);
        } else {
            layoutTitle.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.GONE);
        }
    }

    public void setTotalTime(String time) {
        totalTime.setText(time);
    }

    public void setPlayTime(String time) {
        playTime.setText(time);
    }

    public void playVideo(boolean isPlay) {
        play.setSelected(isPlay);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                mListener.play();
                break;
            case R.id.close:
                mVrVideoView.setDisplayMode(1);
                break;
            case R.id.full_eye:
                mVrVideoView.setDisplayMode(3);
                showLandscape(false);
                break;

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mVrVideoView.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface FullScreenClickListener {
        public void play();
    }
}

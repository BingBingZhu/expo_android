//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.vr.sdk.widgets.common;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.expo.R;
import com.google.vr.cardboard.FullscreenMode;

public class FullScreenDialog extends Dialog implements View.OnClickListener {
    private final View innerWidgetView;
    private ViewGroup innerWidgetViewParent;
    private VrWidgetRenderer renderer;
    private View dialogView;
    private FullscreenMode fullscreenMode;

    private View layoutTitle;
    private View layoutBottom;
    private FrameLayout frameLayout;
    private TextView titleName;
    private ImageView play;
    private TextView playTime;
    private TextView totalTime;
    private SeekBar seekBar;

    public FullScreenDialog(Context context, View innerWidgetView, VrWidgetRenderer renderer) {
        super(context, 16973834);
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

        dialogView.findViewById(R.id.frame).setOnClickListener(this);
        dialogView.findViewById(R.id.close).setOnClickListener(this);
        dialogView.findViewById(R.id.play).setOnClickListener(this);
        dialogView.findViewById(R.id.full_screen).setOnClickListener(this);
    }

    protected void onStart() {
        super.onStart();
        this.renderer.onViewDetach();
        this.innerWidgetViewParent = (ViewGroup)this.innerWidgetView.getParent();
        this.innerWidgetViewParent.removeView(this.innerWidgetView);
        this.frameLayout.addView(this.innerWidgetView, -1, -1);
    }

    protected void onStop() {
        this.renderer.onViewDetach();
        this.frameLayout.removeView(this.innerWidgetView);
        this.innerWidgetViewParent.addView(this.innerWidgetView);
        super.onStop();
    }

    public void show() {
        super.show();
        this.fullscreenMode = new FullscreenMode(this.getWindow());
        this.fullscreenMode.goFullscreen();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame:
                break;
        }
    }
}

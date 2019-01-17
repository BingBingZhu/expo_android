package com.expo.module.online;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.module.online.widget.VRVideoView;

import butterknife.BindView;
import butterknife.OnClick;

public class VRDetailActivity extends BaseActivity {

    @BindView(R.id.vr_frame)
    FrameLayout mFlVrFrame;
    @BindView(R.id.full_screen)
    FrameLayout mFullScreen;

    @Override
    protected int getContentView() {
        return R.layout.activity_vr_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mFlVrFrame.addView(VRVideoView.getInstance(this).getVrVideoView());
        VRVideoView.getInstance(this).setUrl("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        VRVideoView.getInstance(this).setmFullScreen(mFullScreen);
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, VRDetailActivity.class));
    }

    @Override
    protected void onDestroy() {
        VRVideoView.getInstance(this).onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.vr_panorama)
    public void clickVrPanorama(View view) {
        VRVideoView.getInstance(this).showVrSceen();
    }
}

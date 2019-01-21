package com.expo.module.online.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.module.online.detail.widget.VRImageView;
import com.expo.module.online.detail.widget.VRInterfaceView;
import com.expo.module.online.detail.widget.VRVideoView;

import butterknife.BindView;
import butterknife.OnClick;

public class VRDetailActivity extends BaseActivity {

    @BindView(R.id.vr_frame)
    FrameLayout mFlVrFrame;
    @BindView(R.id.full_screen)
    FrameLayout mFullScreen;
    @BindView(R.id.vr_detail_name)
    TextView mTvName;
    @BindView(R.id.vr_detail_scans)
    TextView mTvScans;
    @BindView(R.id.vr_detail_time)
    TextView mTvTime;

    VRInterfaceView mVRView;

    boolean isVideo;

    @Override
    protected int getContentView() {
        return R.layout.activity_vr_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        isVideo = false;
        if (isVideo)
            mVRView = new VRVideoView(this);
        else
            mVRView = new VRImageView(this);
        mFlVrFrame.addView(mVRView.getVrVideoView());
        if (isVideo)
            mVRView.setUrl("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        else
            mVRView.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547707751782&di=ffa3185875f6d3be3c94a6811d8f7317&imgtype=0&src=http%3A%2F%2Fpic1.16pic.com%2F00%2F50%2F47%2F16pic_5047893_b.jpg");

        mTvName.setText("中国馆");
        mTvScans.setText(getString(R.string.scans_scans, 123456));
        mTvTime.setText(getString(R.string.play_times, 123456));
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
        mVRView.onDestroy();
        mVRView = null;
        super.onDestroy();
    }

    @OnClick(R.id.vr_panorama)
    public void clickVrPanorama(View view) {
        mVRView.showVrSceen();
    }

    @Override
    public void recreate() {
    }

}

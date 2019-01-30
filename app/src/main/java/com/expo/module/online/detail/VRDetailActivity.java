package com.expo.module.online.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.VRDetailContract;
import com.expo.entity.VrInfo;
import com.expo.module.online.detail.widget.VRImageView;
import com.expo.module.online.detail.widget.VRInterfaceView;
import com.expo.module.online.detail.widget.VRVideoView;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class VRDetailActivity extends BaseActivity<VRDetailContract.Presenter> implements VRDetailContract.View {

    @BindView(R.id.vr_frame)
    FrameLayout mFlVrFrame;
    @BindView(R.id.vr_detail_name)
    TextView mTvName;
    @BindView(R.id.vr_detail_scans)
    TextView mTvScans;
    @BindView(R.id.vr_detail_time)
    TextView mTvTime;
    @BindView(R.id.vr_detail_img)
    View mVrImg;
    @BindView(R.id.vr_detail_video)
    View mVrVideo;

    VRInterfaceView mVRView;

    VrInfo mVrInfo;

    @Override
    protected int getContentView() {
        return R.layout.activity_vr_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mVrInfo = mPresenter.getVrInfo(getIntent().getLongExtra(Constants.EXTRAS.EXTRA_ID, 0L));
        getVrInfo();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context, Long id) {
        Intent intent = new Intent(context, VRDetailActivity.class);
        intent.putExtra(Constants.EXTRAS.EXTRA_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mVRView != null) {
            mVRView.onDestroy();
            mVRView = null;
        }
        super.onDestroy();
    }

    @OnClick(R.id.vr_panorama)
    public void clickVrPanorama(View view) {
        mVRView.showVrSceen();
    }

    @OnClick({R.id.vr_detail_img})
    public void changeVr(View view) {
        VRImageActivity.startActivity(this, Long.valueOf(mVrInfo.getLinkPanResId()));
    }

    @Override
    public void recreate() {
    }

    public void showVrView() {
//        if (StringUtils.equals(mVrInfo.getType(), "0")) {
        mVRView = new VRVideoView(this);
        mVrVideo.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mVrInfo.getLinkPanResId()))
            mVrImg.setVisibility(View.GONE);
        else
            mVrImg.setVisibility(View.VISIBLE);
//        } else {
//            mVRView = new VRImageView(this);
//            mVrImg.setVisibility(View.GONE);
//            if (TextUtils.isEmpty(mVrInfo.getLinkPanResId()))
//                mVrVideo.setVisibility(View.GONE);
//            else
//                mVrVideo.setVisibility(View.VISIBLE);
//        }
//        mFlVrFrame.addView(mVRView.getVrVideoView());
    }

    public void getVrInfo() {
        if (mVrInfo == null) {
            finish();
            return;
        }

        mTvName.setText(LanguageUtil.chooseTest(mVrInfo.getCaption(), mVrInfo.getCaptionEn()));
        mTvScans.setText(getString(R.string.scans_scans, 123));
        mTvTime.setText(getString(R.string.play_times, mVrInfo.getExtAttr()));

        showVrView();
        mVRView.setVrInfo(mVrInfo);
        mPresenter.setPanResViews(mVrInfo.getId());
    }
}

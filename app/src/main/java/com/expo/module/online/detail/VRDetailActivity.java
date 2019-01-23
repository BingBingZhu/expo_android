package com.expo.module.online.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    boolean mCanGoOther;

    VrInfo mVrInfo;

    @Override
    protected int getContentView() {
        return R.layout.activity_vr_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mCanGoOther = getIntent().getBooleanExtra(Constants.EXTRAS.EXTRA_CAN_GO_OTHER, false);
        mVrInfo = mPresenter.getVrInfo(getIntent().getStringExtra(Constants.EXTRAS.EXTRA_ID));
        getVrInfo();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context, Long id, boolean canGoOther) {
        Intent intent = new Intent(context, VRDetailActivity.class);
        intent.putExtra(Constants.EXTRAS.EXTRA_ID, id);
        intent.putExtra(Constants.EXTRAS.EXTRA_CAN_GO_OTHER, canGoOther);
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

    @OnClick({R.id.vr_detail_img, R.id.vr_detail_video})
    public void changeVr(View view) {
        if (mCanGoOther)
            VRDetailActivity.startActivity(this, mVrInfo.getLinkPanResId(), false);
        else
            finish();
    }

    @Override
    public void recreate() {
    }

    public void showVrView() {
        if (StringUtils.equals(mVrInfo.getType(), "0")) {
            mVRView = new VRVideoView(this);
            mVrVideo.setVisibility(View.GONE);
            if (mVrInfo.getLinkPanResId() == 0)
                mVrImg.setVisibility(View.GONE);
            else
                mVrImg.setVisibility(View.VISIBLE);
        } else {
            mVRView = new VRImageView(this);
            mVrImg.setVisibility(View.GONE);
            if (mVrInfo.getLinkPanResId()==0)
                mVrVideo.setVisibility(View.GONE);
            else
                mVrVideo.setVisibility(View.VISIBLE);
        }
        mFlVrFrame.addView(mVRView.getVrVideoView());
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
    }
}

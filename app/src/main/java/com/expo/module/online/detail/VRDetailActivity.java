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
import com.expo.utils.CommUtils;
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
        mPresenter.setVrInfo(getIntent().getLongExtra(Constants.EXTRAS.EXTRA_ID, 0));

        showVrView();

        mFlVrFrame.addView(mVRView.getVrVideoView());

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context, long id, boolean canGoOther) {
        Intent intent = new Intent(context, VRDetailActivity.class);
        intent.putExtra(Constants.EXTRAS.EXTRA_ID, id);
        intent.putExtra(Constants.EXTRAS.EXTRA_CAN_GO_OTHER, canGoOther);
        context.startActivity(intent);
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

    @OnClick({R.id.vr_detail_img, R.id.vr_detail_video})
    public void changeVr(View view) {
        if (mCanGoOther)
            VRDetailActivity.startActivity(this, mVrInfo.getId(), false);
        else
            finish();
    }

    @Override
    public void recreate() {
    }

    public void showVrView() {
        boolean hasOther = false;
        if (StringUtils.equals(mVrInfo.getType(), "2")) {
            mVRView = new VRVideoView(this);
            mVrVideo.setVisibility(View.GONE);
            if (hasOther)
                mVrImg.setVisibility(View.VISIBLE);
        } else {
            mVRView = new VRImageView(this);
            mVrImg.setVisibility(View.GONE);
            if (hasOther)
                mVrVideo.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void freshVrInfo(VrInfo vrInfo) {
        if (vrInfo == null) finish();

        mVrInfo = vrInfo;
        mTvName.setText(LanguageUtil.chooseTest(vrInfo.getCaption(), vrInfo.getCaptionEn()));
        mTvScans.setText(getString(R.string.scans_scans, 123));
        mTvTime.setText(getString(R.string.play_times, vrInfo.getExtAttr()));

        showVrView();
        mVRView.setUrl(CommUtils.getFullUrl(vrInfo.getUrl()));
    }
}

package com.expo.module.camera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.utils.Constants;
import com.expo.widget.camera.CameraSurfaceView;
import com.senierr.shootbutton.ShootButton;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/*
 * 关于我们
 */
public class CameraActivity extends BaseActivity {

    @BindView(R.id.camera_view)
    CameraSurfaceView mCarmer;
    @BindView(R.id.camera_btn)
    ShootButton mBar;

    private TimeCount mTimeCount;    //验证码获取计时器

    long mTouchTime = 0;
    boolean mIsFinish = false;
    boolean mIsVideo = false;

    CameraSurfaceView.CameraSurfaceViewListener mListener = new CameraSurfaceView.CameraSurfaceViewListener() {
        @Override
        public void complete() {
            cameraFinish();
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_camera;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mCarmer.setListener(mListener);
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, CameraActivity.class);
        context.startActivity(in);
    }

    @OnTouch(R.id.camera_btn)
    public boolean cameraTouch(View view, MotionEvent event) {
        long time = TimeUtils.getNowMills();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchTime = TimeUtils.getNowMills();
                break;
            case MotionEvent.ACTION_UP:
                if (time - mTouchTime < 300) {
                    mCarmer.capture();
                } else {
                    mCarmer.stopRecord();
                    if (mTimeCount != null)
                        mTimeCount.cancel();
                }
                break;
            default:
                if (mIsVideo) ;
                else if (time - mTouchTime > 300) {
                    mIsVideo = true;
                    mCarmer.startRecord();
                    mTimeCount = new TimeCount((10) * 1000, 100);
                    mTimeCount.start();
                    mBar.setProgress(0);
                }
                break;
        }
        return true;
    }

    @OnClick(R.id.carmer_switch)
    public void cameraSwitch(View view) {
        view.setSelected(!view.isSelected());
        mCarmer.setDefaultCamera(view.isSelected());
    }

    public synchronized void cameraFinish() {
        if (mIsFinish) return;
        mIsFinish = true;
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRAS.EXTRAS, mCarmer.getOutFilePath());
        setResult(RESULT_OK, intent);
        ToastHelper.showLong("保存的位置是" + mCarmer.getOutFilePath());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCarmer.closeCamera();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mCarmer.stopRecord();
            mBar.setProgress(100);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            System.out.println("camera video time " + (int) (100 - (millisUntilFinished / 100)));
            mBar.setProgress((int) (100 - (millisUntilFinished / 100)));
        }
    }
}

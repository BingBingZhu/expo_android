package com.expo.module.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.utils.Constants;
import com.expo.widget.camera.CameraSurfaceView;
import com.senierr.shootbutton.ShootButton;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;

/*
 * 关于我们
 */
public class CameraActivity extends BaseActivity {

    @BindView(R.id.camera_view)
    CameraSurfaceView mCarmer;
    @BindView(R.id.camera_btn)
    ShootButton mBar;
    @BindView(R.id.camera_ok)
    View mVOk;

    private TimeCount mTimeCount;    //验证码获取计时器

    long mTouchTime = 0;
    boolean mIsFinish = false;
    boolean mIsVideo = false;

    CameraSurfaceView.CameraSurfaceViewListener mListener = new CameraSurfaceView.CameraSurfaceViewListener() {
        @Override
        public void complete() {
            mVOk.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_camera;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        checkPermission();
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

    @OnClick(R.id.camera_btn)
    public void cameraClick(View view) {
        mCarmer.capture();
        view.setEnabled(false);
    }

    @OnLongClick(R.id.camera_btn)
    public boolean cameraLongClick(View view) {
        mTouchTime = TimeUtils.getNowMills();
        mIsVideo = true;
        mCarmer.startRecord(true);
        mTimeCount = new TimeCount((10) * 1000, 100);
        mTimeCount.start();
        mBar.setProgress(0);
        return false;
    }

    @OnTouch(R.id.camera_btn)
    public boolean cameraTouch(View view, MotionEvent event) {
//        long time = TimeUtils.getNowMills();
        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                System.out.println("MotionEvent MotionEvent.ACTION_DOWN");
//                mTouchTime = TimeUtils.getNowMills();
//                break;
            case MotionEvent.ACTION_UP:
                if (mIsVideo) {
                    mCarmer.stopRecord();
                    if (mTimeCount != null)
                        mTimeCount.cancel();
                }
                mIsVideo = false;
//                System.out.println("MotionEvent MotionEvent.ACTION_UP");
//                if (time - mTouchTime < 300) {
//                    mCarmer.capture();
//                } else {
//                    mCarmer.stopRecord();
//                    if (mTimeCount != null)
//                        mTimeCount.cancel();
//                }
                break;
//            default:
//                System.out.println("MotionEvent MotionEvent.default");
//                if (mIsVideo) ;
//                else if (time - mTouchTime > 300) {
//                    mIsVideo = true;
//                    mCarmer.startRecord(false);
//                    mTimeCount = new TimeCount((10) * 1000, 100);
//                    mTimeCount.start();
//                    mBar.setProgress(0);
//                }
//                break;
        }
        return false;
    }

    @OnClick(R.id.camera_cancle)
    public void cameraCancle(View view) {
        finish();
    }

    @OnClick(R.id.camera_ok)
    public void cameraOk(View view) {
        cameraFinish();
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
//        ToastHelper.showLong("保存的位置是" + mCarmer.getOutFilePath());
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

    /*
     * 检查权限并弹框提示获取权限
     */
    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if (permissionList.size() > 0) {
            //弹出权限请求提示对话框
            int size = permissionList.size();
            String[] permissions = new String[size];
            for (int i = 0; i < size; i++) {
                permissions[i] = permissionList.get(i);
            }
            getPermission(R.string.toast_feed_back_success, permissions);
        }
    }

    private void getPermission(int toast, String[] permission) {
        AndPermission.with(this)
                .runtime()
                .permission(permission)
                .onGranted(permissions -> {
                    if (StringUtils.equals(Manifest.permission.CAMERA, permissions.get(0))) {
                        mCarmer.openCamera();
                        mCarmer.startPreview();
                    }
                })
                .onDenied(permissions -> {
                    ToastHelper.showShort(toast);
                    finish();
                })
                .start();
    }
}

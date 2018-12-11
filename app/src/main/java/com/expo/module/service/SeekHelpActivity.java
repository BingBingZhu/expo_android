package com.expo.module.service;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.base.ExpoApp;
import com.expo.base.utils.CheckUtils;
import com.expo.base.utils.GpsUtil;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.SeekHelpContract;
import com.expo.entity.User;
import com.expo.entity.VisitorService;
import com.expo.map.LocationManager;
import com.expo.module.camera.CameraActivity;
import com.expo.module.service.adapter.SeekHelpAdapter;
import com.expo.utils.Constants;
import com.expo.widget.decorations.SpaceDecoration;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.expo.utils.Constants.EXTRAS.EXTRAS;

/*
 * 游客求助，0:医疗救助、1:人员走失、2:寻物启事、3:治安举报、4:问询咨询通用页面
 */
public class SeekHelpActivity extends BaseActivity<SeekHelpContract.Presenter> implements SeekHelpContract.View, View.OnClickListener {

    @BindView(R.id.seek_help_image_selector)
    RecyclerView mRecycler;
    @BindView(R.id.seek_help_text3)
    EditText mEtEdit;
    @BindView(R.id.seek_help_phone)
    View mPhone;
    @BindView(R.id.seek_help_speech)
    View mSpeech;

    ArrayList<String> mImageList;
    SeekHelpAdapter mAdapter;
    Location mLocation;

    Double mLng, mLat;
    String mCoordinateAssist = "";
    String mPoiId = "";

    boolean mIsLocation;

    int mOpenGPSTimes;

    private EventManager asr;

    int mCameraPosition = 0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (2 == (mOpenGPSTimes = (mOpenGPSTimes + 1) % 3)) {
                        hideLoadingView();
                        ToastHelper.showShort(R.string.gps_open_weak);
                    } else location(null);
                    break;
            }
        }
    };

    BaseAdapterItemClickListener<Integer> mClickListener = new BaseAdapterItemClickListener<Integer>() {
        @Override
        public void itemClick(View view, int position, Integer o) {
            if (position == mImageList.size())
                showImgSelectDialog();
        }
    };

    BaseAdapterItemClickListener<Integer> mDeleteListener = new BaseAdapterItemClickListener<Integer>() {
        @Override
        public void itemClick(View view, int position, Integer o) {
            removeCameraPosition(position);
            mImageList.remove(position);
            mAdapter.refresh(mImageList);
        }
    };

    AMap.OnMyLocationChangeListener mLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            mLocation = location;
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_seek_help;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, getIntent().getStringExtra(Constants.EXTRAS.EXTRA_TITLE));
        if (getIntent().getIntExtra(Constants.EXTRAS.EXTRAS, 0) != 0) {
            mPhone.setVisibility(View.GONE);
        }
        initRecord();
        initRecyclerView();

        mIsLocation = true;
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    private void initRecord() {
        asr = EventManagerFactory.create(this, "asr");
        asr.registerListener(mListener); //  EventListener 中 onEvent方法\
        mSpeech.setOnTouchListener((v, event) -> {
            LogUtils.d("Action", event.getAction() + "");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startRecord();
                    break;
                case MotionEvent.ACTION_UP:
                    stopRecord();
                    break;
            }
            return true;
        });
    }

    private void initRecyclerView() {
        mAdapter = new SeekHelpAdapter(this);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        mRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_10)));
        mRecycler.setAdapter(mAdapter);

        mImageList = new ArrayList<>();
        mAdapter.setClickListener(mClickListener);
        mAdapter.setDeleteListener(mDeleteListener);
        LocationManager.getInstance().registerLocationListener(mLocationChangeListener);

    }

    /**
     * 启动游客服务求助界面
     *
     * @param context
     * @param type    求助类型
     */
    public static void startActivity(@NonNull Context context, int type) {
        Intent in = new Intent(context, SeekHelpActivity.class);
        in.putExtra(EXTRAS, type);
        context.startActivity(in);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCode.REQ_OPEN_GPS) {
            if (GpsUtil.isOPen(this))
                location(null);
        }
        if (resultCode == RESULT_OK)
            if (requestCode == Constants.RequestCode.REQUEST111 && data != null) {
//                mImageList.clear();
                resetCameraPosition();
                mImageList.addAll(data.getStringArrayListExtra(
                        ImageSelectorUtils.SELECT_RESULT));
                mAdapter.refresh(mImageList);
            } else if (requestCode == Constants.RequestCode.REQ_GET_LOCAL && data != null) {
                mCoordinateAssist = data.getStringExtra(Constants.EXTRAS.EXTRAS);
                mLat = data.getDoubleExtra(Constants.EXTRAS.EXTRA_LATITUDE, 0);
                mLng = data.getDoubleExtra(Constants.EXTRAS.EXTRA_LONGITUDE, 0);
                mPoiId = data.getStringExtra(Constants.EXTRAS.EXTRA_ID);
                if (mLat == -1 && mLng == -1) mIsLocation = true;
                else mIsLocation = false;
            } else if (requestCode == Constants.RequestCode.REQ_TO_CAMERA) {
                String path = data.getStringExtra(Constants.EXTRAS.EXTRAS);
                mCameraPosition = mCameraPosition | 1 << mImageList.size();
                mImageList.add(path);
                mAdapter.refresh(mImageList);
            }
    }

    @OnClick(R.id.seek_help_submit)
    public void submit(View view) {
        VisitorService visitorService = initSubmitData();
        if (visitorService != null)
            mPresenter.addVisitorService(visitorService);
    }

    @OnClick(R.id.seek_help_navigation)
    public void navigation(View view) {
        if (null == mLocation || mLocation.getLatitude() == 0){
            ToastHelper.showShort(R.string.trying_to_locate);
            return;
        }
        mPresenter.getServerPoint(mLocation);
    }

    @OnClick(R.id.seek_help_text4)

    public void location(View view) {
        if (!GpsUtil.isOPen(this)) {
            GpsUtil.openGPSSettings(this);
            return;
        }
        showLoadingView();
        if (mLocation != null) {
            hideLoadingView();
            LocationDescribeActivity.startActivityForResult(this, mLocation.getLatitude(), mLocation.getLongitude(), mPoiId, mCoordinateAssist);
        } else
            mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @OnClick(R.id.seek_help_phone)
    public void phone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+"400 818 6666"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_record:
            case R.id.image_record_explain:
                startActivityForResult(new Intent(SeekHelpActivity.this, CameraActivity.class), Constants.RequestCode.REQ_TO_CAMERA);
                dialog.dismiss();
                break;
            case R.id.image_album:
                goImageSelector();
                dialog.dismiss();
                break;
            case R.id.cancle:
                dialog.dismiss();
                break;
        }
    }
    private Dialog dialog;

    private void showImgSelectDialog() {
        dialog = new Dialog(this, R.style.BottomActionSheetDialogStyle);
        //填充对话框的布局
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_image_select, null);
        //初始化控件
        TextView tvCamera1 = v.findViewById(R.id.image_record);
        TextView tvCamera2 = v.findViewById(R.id.image_record_explain);
        TextView tvPhoto = v.findViewById(R.id.image_album);
        TextView tvCancle = v.findViewById(R.id.cancle);
        tvCamera1.setOnClickListener(this);
        tvCamera2.setOnClickListener(this);
        tvPhoto.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(v);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if(dialogWindow == null){
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    private void goImageSelector() {
        int count = getCameraImageCount();
        if (count >= 3)
            ToastHelper.showShort(R.string.today);
        else
            ImageSelector.builder()
                    .useCamera(true) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setMaxSelectCount(Constants.Config.IMAGE_MAX_COUNT - count) // 图片的最大选择数量，小于等于0时，不限数量。
                    .setSelected(mImageList) // 把已选的图片传入默认选中。
                    .setViewImage(true) //是否点击放大图片查看,，默认为true
                    .start(SeekHelpActivity.this, Constants.RequestCode.REQUEST111); // 打开相册
    }

    private int getCameraImageCount() {
        int count = 0;
        int cameraPosition = mCameraPosition;
        while (cameraPosition > 0) {
            if ((cameraPosition | 1) == cameraPosition) {
                count++;
            }
            cameraPosition = cameraPosition >> 1;
        }
        return count;
    }

    private void resetCameraPosition() {
        for (int i = mImageList.size() - 1; i >= 0; i--) {
            if ((mCameraPosition | 1 << i) != mCameraPosition)
                mImageList.remove(i);
        }

        mCameraPosition = 0;
        for (int i = 0; i < mImageList.size(); i++) {
            mCameraPosition += 1 << i;
        }
    }

    private void removeCameraPosition(int position) {
        int right = mCameraPosition & ((1 << position) - 1);
        int left = mCameraPosition >> (position + 1);
        mCameraPosition = (left << position) + right;

    }

    private VisitorService initSubmitData() {
        if (CheckUtils.isEmtpy(mEtEdit.getText().toString(), R.string.check_string_empty_localtion_descriptiong, true)) {
            return null;
        }

        VisitorService visitorService = new VisitorService();
        User user = ExpoApp.getApplication().getUser();
        switch (mImageList.size()) {
            case 3:
                visitorService.setImgUrl3(mImageList.get(2));
            case 2:
                visitorService.setImgUrl2(mImageList.get(1));
            case 1:
                visitorService.setImgUrl1(mImageList.get(0));
        }
        switch (getIntent().getIntExtra(Constants.EXTRAS.EXTRAS, 0)) {
            case 0:
                visitorService.setServiceType("1");
                break;
            case 1:
                visitorService.setServiceType("3");
                break;
            case 2:
                visitorService.setServiceType("5");
                break;
            case 3:
                visitorService.setServiceType("4");
                break;
            case 4:
                visitorService.setServiceType("2");
                break;
        }

        visitorService.setCoordinateAssist(mCoordinateAssist);
        visitorService.setCounttryCode(PrefsHelper.getString(Constants.Prefs.KEY_COUNTRY_CODE, "+86"));
        if (mIsLocation) {
            if (mLocation != null) {
                visitorService.setGpsLatitude(mLocation.getLatitude() + "");
                visitorService.setGpsLongitude(mLocation.getLongitude() + "");
            }
        } else {
            visitorService.setGpsLatitude(mLat + "");
            visitorService.setGpsLongitude(mLng + "");
        }
        visitorService.setPhone(user.getMobile());
        visitorService.setSituation(mEtEdit.getText().toString());
        visitorService.setUserId(user.getUid());
        visitorService.setUserName(user.getNick());
        return visitorService;
    }

    @Override
    public void complete() {
        ToastHelper.showShort(getResources().getString(R.string.submit_success));
        finish();
    }

    @Override
    protected void onDestroy() {
        LocationManager.getInstance().unregisterLocationListener(mLocationChangeListener);
        super.onDestroy();
    }

    //语音识别
    private void startRecord() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 15000);
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PROP ,20000);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
    }

    private void stopRecord() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }

    EventListener mListener = new EventListener() {
        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
//                if (params.contains("\"nlu_result\"")) {
                if (params.contains("\"final_result\"")) {
                    try {
                        JSONObject gson = new JSONObject(params);
                        mEtEdit.setText(mEtEdit.getText().toString() + gson.optString("best_result"));
//                        mEtEdit.setText(mEtEdit.getText().toString() + new String(data, offset, length));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
}

package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.amap.api.maps.AMap;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.base.ExpoApp;
import com.expo.base.utils.CheckUtils;
import com.expo.base.utils.GpsUtil;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.SeekHelpContract;
import com.expo.entity.User;
import com.expo.entity.VisitorService;
import com.expo.map.LocationManager;
import com.expo.module.map.ParkMapActivity;
import com.expo.module.service.adapter.SeekHelpAdapter;
import com.expo.utils.Constants;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.expo.utils.Constants.EXTRAS.EXTRAS;

/*
 * 游客求助，0:医疗救助、1:人员走失、2:寻物启事、3:治安举报、4:问询咨询通用页面
 */
public class SeekHelpActivity extends BaseActivity<SeekHelpContract.Presenter> implements SeekHelpContract.View {

    @BindView(R.id.seek_help_image_selector)
    RecyclerView mRecycler;
    @BindView(R.id.seek_help_text3)
    EditText mEtEdit;
    @BindView(R.id.seek_help_phone)
    View mPhone;

    ArrayList<String> mImageList;
    SeekHelpAdapter mAdapter;
    Location mLocation;

    Double mLng, mLat;
    String mCoordinateAssist = "";

    boolean mIsLocation;

    int mOpenGPSTimes;

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
            ImageSelector.builder()
                    .useCamera(true) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setMaxSelectCount(3) // 图片的最大选择数量，小于等于0时，不限数量。
                    .setSelected(mImageList) // 把已选的图片传入默认选中。
                    .setViewImage(true) //是否点击放大图片查看,，默认为true
                    .start(SeekHelpActivity.this, Constants.RequestCode.REQUEST111); // 打开相册
        }
    };

    BaseAdapterItemClickListener<Integer> mDeleteListener = new BaseAdapterItemClickListener<Integer>() {
        @Override
        public void itemClick(View view, int position, Integer o) {
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
        initRecyclerView();
        mIsLocation = true;
    }

    @Override
    protected boolean hasPresenter() {
        return true;
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
                mImageList.clear();
                mImageList.addAll(data.getStringArrayListExtra(
                        ImageSelectorUtils.SELECT_RESULT));
                mAdapter.refresh(mImageList);
            } else if (requestCode == Constants.RequestCode.REQ_GET_LOCAL && data != null) {
                mCoordinateAssist = data.getStringExtra(Constants.EXTRAS.EXTRAS);
                mLat = data.getDoubleExtra(Constants.EXTRAS.EXTRA_LATITUDE, 0);
                mLng = data.getDoubleExtra(Constants.EXTRAS.EXTRA_LONGITUDE, 0);
                if (mLat == -1 && mLng == -1) mIsLocation = true;
                else mIsLocation = false;
            }
    }

    @OnClick(R.id.seek_help_submit)
    public void submit(View view) {
        VisitorService visitorService = initSubmitData();
        if (visitorService != null)
            mPresenter.addVisitorService(initSubmitData());
    }

    @OnClick(R.id.seek_help_navigation)
    public void navigation(View view) {
        ToastHelper.showShort("跳到导航界面");
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
            LocationDescribeActivity.startActivityForResult(this, mLocation.getLatitude(), mLocation.getLongitude());
        } else
            mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @OnClick(R.id.seek_help_phone)
    public void phone(View view) {

    }

    private VisitorService initSubmitData() {
        if (CheckUtils.isEmtpy(mEtEdit.getText().toString(), R.string.check_string_empty_localtion_descriptiong, true)) {
            return null;
        }

        VisitorService visitorService = new VisitorService();
        User user = ExpoApp.getApplication().getUser();
        switch (mImageList.size()) {
            case 3:
                visitorService.img_url3 = mImageList.get(2);
            case 2:
                visitorService.img_url2 = mImageList.get(1);
            case 1:
                visitorService.img_url1 = mImageList.get(0);
        }
        switch (getIntent().getIntExtra(Constants.EXTRAS.EXTRAS, 0)) {
            case 0:
                visitorService.servicetype = "1";
                break;
            case 1:
                visitorService.servicetype = "3";
                break;
            case 2:
                visitorService.servicetype = "5";
                break;
            case 3:
                visitorService.servicetype = "4";
                break;
            case 4:
                visitorService.servicetype = "2";
                break;
        }

        visitorService.coordinate_assist = mCoordinateAssist;
        visitorService.counttrycode = PrefsHelper.getString(Constants.Prefs.KEY_COUNTRY_CODE, "+86");
        if (mIsLocation) {
            if (mLocation != null) {
                visitorService.gps_latitude = mLocation.getLatitude() + "";
                visitorService.gps_longitude = mLocation.getLongitude() + "";
            }
        } else {
            visitorService.gps_latitude = mLat + "";
            visitorService.gps_longitude = mLng + "";
        }
        visitorService.phone = user.getMobile();
        visitorService.situation = mEtEdit.getText().toString();
        visitorService.userid = user.getUid();
        visitorService.username = user.getNick();
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
}

package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.CheckUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.SeekHelpContract;
import com.expo.entity.User;
import com.expo.entity.Venue;
import com.expo.entity.VisitorService;
import com.expo.module.map.NavigationActivity;
import com.expo.services.TrackRecordService;
import com.expo.utils.Constants;
import com.expo.widget.CustomDefaultDialog;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 游客求助，
3:     // 失物招领
5:     // 医疗救助
6:     // 人员走失
7:     // 治安举报
 */
public class TouristServiceSecondActivity extends BaseActivity<SeekHelpContract.Presenter> implements SeekHelpContract.View {

    @BindView(R.id.banner)
    ImageView mImgBanner;
    @BindView(R.id.rule_view)
    View mRuleView;
    @BindView(R.id.seek_help_text1)
    TextView mTv1;
    @BindView(R.id.seek_help_text2)
    TextView mTv2;
    @BindView(R.id.seek_help_text3)
    TextView mTv3;
    @BindView(R.id.seek_help_text4)
    TextView mTv4;
    @BindView(R.id.seek_help_navigation)
    TextView mTvNavigation;
    @BindView(R.id.seek_help_function)
    TextView mTvFunction;

    private int mTypePosition = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_tourist_service_second;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle( 1, getIntent().getStringExtra( Constants.EXTRAS.EXTRA_TITLE ) );
        mTypePosition = getIntent().getIntExtra( Constants.EXTRAS.EXTRAS, 0 );
        if (mTypePosition == 3) {
            mRuleView.setVisibility( View.VISIBLE );
        }
        initViewData();
    }


    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context, String title, int position){
        Intent intent = new Intent(context, TouristServiceSecondActivity.class);
        intent.putExtra(Constants.EXTRAS.EXTRA_TITLE, title);
        intent.putExtra(Constants.EXTRAS.EXTRAS, position);
        context.startActivity(intent);
    }

    @OnClick(R.id.seek_help_function)
    public void onClick(View view) {
        if (mTypePosition == 5){    // 医疗救助 定位发送
            if (TrackRecordService.getLocation() == null || TrackRecordService.getLocation().getLatitude() == 0) {
                ToastHelper.showShort( R.string.trying_to_locate );
                return;
            }
            if (mPresenter.checkInPark( TrackRecordService.getLocation().getLatitude(), TrackRecordService.getLocation().getLongitude() )) {
                CustomDefaultDialog dialog = new CustomDefaultDialog(getContext());
                dialog.setContent("是否因伤病较重，行动不便需要我们立即到您身边开展医疗救助？")
                        .setOkText("是")
                        .setOkTextColor(R.color.green_02cd9b)
                        .setCancelText("否")
                        .setOnCancelClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                VisitorService visitorService = initSubmitData();
                                if (visitorService != null) {
                                    mPresenter.addVisitorService( visitorService );
                                }
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                ToastHelper.showShort( R.string.unable_to_provide_service );
            }
        }else{  // 跳转页面   填写信息
            TouristServiceSubmitActivity.startActivity(getContext(), mTypePosition);
        }
    }


    private VisitorService initSubmitData() {
//        if (CheckUtils.isEmtpy( mEtEdit.getText().toString(), R.string.check_string_empty_localtion_descriptiong, true )) {
//            return null;
//        }

//        if (mIsLocation && mLocation == null)
//            if (CheckUtils.isEmtpy( mCoordinateAssist, R.string.check_string_no_gps_empty_localtion, true ))
//                return null;

        VisitorService visitorService = new VisitorService();
        User user = ExpoApp.getApplication().getUser();
//        switch (mImageList.size()) {
//            case 3:
//                visitorService.setImgUrl3( mImageList.get( 2 ) );
//            case 2:
//                visitorService.setImgUrl2( mImageList.get( 1 ) );
//            case 1:
//                visitorService.setImgUrl1( mImageList.get( 0 ) );
//        }
        visitorService.setDisposeType( "1" );
        visitorService.setState( "1" );
        visitorService.setServiceType( "1" );
//        visitorService.setCoordinateAssist( mCoordinateAssist );
        visitorService.setCounttryCode( PrefsHelper.getString( Constants.Prefs.KEY_COUNTRY_CODE, "+86" ) );
        visitorService.setGpsLatitude( String.valueOf( TrackRecordService.getLocation().getLatitude() ) );
        visitorService.setGpsLongitude( String.valueOf( TrackRecordService.getLocation().getLongitude() ) );
        visitorService.setPhone( user.getMobile() );
//        visitorService.setSituation( mEtEdit.getText().toString() );
        visitorService.setUserId( user.getUid() );
        visitorService.setUserName( user.getNick() );
        visitorService.setPlatform("1");
        return visitorService;
    }











    @OnClick(R.id.seek_help_navigation)
    public void navigation(View view) {
        if (isNotLocation()) {
            ToastHelper.showShort( R.string.trying_to_locate );
            return;
        }
        if (mPresenter.checkInPark( TrackRecordService.getLocation().getLatitude(), TrackRecordService.getLocation().getLongitude() )) {
            Venue venue = mPresenter.getNearbyServiceCenter( TrackRecordService.getLocation() );
            if (venue != null) {
                NavigationActivity.startActivity( getContext(), venue );
            } else {
                ToastHelper.showShort( R.string.no_service_agencies );
            }
        } else {
            ToastHelper.showShort( R.string.unable_to_provide_service );
        }
    }

    private boolean isNotLocation(){
        return null == TrackRecordService.getLocation() || TrackRecordService.getLocation().getLatitude() == 0;
    }

//    @OnClick(R.id.seek_help_function)
//    public void location(View view) {
//        if (isNotLocation()) {
//            ToastHelper.showShort( R.string.trying_to_locate );
//            return;
//        }
//        LocationDescribeActivity.startActivityForResult( this, mLocation.getLatitude(), mLocation.getLongitude(), mPoiId, mCoordinateAssist );
//    }

    @Override
    public void complete() {
        new CustomDefaultDialog(getContext())
        .setContent("定位已上传，我们记录了您的定位信息，请您在原地等候")
        .setOnlyOK()
        .setOkText("好的")
        .setCancelable(false)
        .show();
    }


    private void initViewData() {
        int drawablePadding = (int) getResources().getDimension(R.dimen.dms_26);
        switch (mTypePosition) {
            case 5:     // 医疗救助
                mImgBanner.setImageResource(R.mipmap.ico_top_banner_medical);
                mTv1.setText("轻伤治疗");
                mTv2.setText("请您判断患者病情或伤势，如果患者可以行动，则请患者前往医疗服务站接受治疗");
                mTvNavigation.setText("导航最近的医疗服务站");
                mTv3.setText("定位救助");
                mTv4.setText("如果患者无法以动，可以点击定位救助功能");
                mTvFunction.setText("定位救助");
                mTvFunction.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.ico_send_location), null);
                mTvFunction.setCompoundDrawablePadding(drawablePadding);
                break;
            case 7:     // 治安举报
                mImgBanner.setImageResource(R.mipmap.ico_top_banner_security);
                mTv1.setText("温馨提示");
                mTv2.setText("请立即寻求您周边的工作人员帮助");
                mTvNavigation.setText("导航最近的治安点");
                mTv3.setText("信息提交");
                mTv4.setText("您可以上传图片、文字作为重要参考");
                mTvFunction.setText("信息填写");
                break;
            case 3:     // 失物招领
                mImgBanner.setImageResource(R.mipmap.ico_top_banner_look_for_something);
                mTv1.setText("信息提交");
                mTv2.setText("您可以上传失物的图片、文字等信息，作为帮助找寻失物的参考。");
                mTvNavigation.setText("为您快速导航最近服务中心");
                mTv3.setText("信息提交");
                mTv4.setText("您可以上传图片、文字作为寻物的重要参考");
                mTvFunction.setText("信息填写");
                mTvFunction.setCompoundDrawables(null, null, null, null);
                int vPadding = (int) getResources().getDimension(R.dimen.dms_18);
                int hPadding = (int) getResources().getDimension(R.dimen.dms_96);
                mTvFunction.setPadding(hPadding, vPadding, hPadding, vPadding);
                mTvFunction.setBackground(getResources().getDrawable(R.drawable.bg_white_99_r5_w1));
                mTvFunction.setTextColor(getResources().getColor(R.color.color_333));
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                param.gravity = Gravity.CENTER_HORIZONTAL;
                param.topMargin = (int) getResources().getDimension(R.dimen.dms_32);
                mTvFunction.setLayoutParams(param);
                break;
            case 6:     // 人员走失
                mImgBanner.setImageResource(R.mipmap.ico_top_banner_look_for_people);
                mTv1.setText("温馨提示");
                mTv2.setText("请立即寻求您周边的工作人员帮助");
                mTvNavigation.setText("导航最近的医服务点");
                mTv3.setText("信息提交");
                mTv4.setText("您可以上传图片、文字作为找人的重要参考");
                mTvFunction.setText("信息填写");
                break;
        }
    }
}

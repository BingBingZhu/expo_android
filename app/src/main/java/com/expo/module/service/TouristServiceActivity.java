package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.TouristServiceContract;
import com.expo.entity.CommonInfo;
import com.expo.entity.Venue;
import com.expo.module.map.NavigationActivity;
import com.expo.module.map.PlayMapActivity;
import com.expo.module.service.adapter.TouristServiceAdapter;
import com.expo.module.webview.WebActivity;
import com.expo.services.TrackRecordService;
import com.expo.utils.Constants;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 游客服务页
 */
public class TouristServiceActivity extends BaseActivity<TouristServiceContract.Presenter> implements TouristServiceContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.tourist_service_call)
    TextView mTvCall;

    private TouristServiceAdapter mAdapter;
    private String mTelePhoneNum;

    BaseAdapterItemClickListener mListener = (view, position, o) -> {
        String title = getResources().getString(getResources().getIdentifier("item_tourist_service_text_" + position, "string", AppUtils.getAppPackageName()));
        switch (position) {
            case 0:
                toWebView(CommonInfo.TOURIST_SERVICE_LOST_AND_FOUND, title, false);
                break;
            case 1:
                toWebView(CommonInfo.TOURIST_SERVICE_LEFT_LUGGAGE, title, false);
                break;
            case 2:
                toWebView(CommonInfo.TOURIST_SERVICE_RENTAL_ITEMS, title, true);
                break;
            case 4:
                toWebView(CommonInfo.TOURIST_SERVICE_MATERNAL_AND_CHILD, title, false);
                break;
            case 3:     // 失物招领
            case 5:     // 医疗救助
            case 6:     // 人员走失
            case 7:     // 治安举报
                TouristServiceSecondActivity.startActivity(getContext(), title, position);
                break;
        }
    };

    private void toWebView(String type, String title, boolean isStartLocation) {
        String url = mPresenter.loadCommonUrlByType(type);
        WebActivity.startActivity(getContext(), TextUtils.isEmpty(url) ? Constants.URL.HTML_404 : url, title, TITLE_COLOR_STYLE_WHITE, isStartLocation);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tourist_service;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, R.string.home_func_item_tourist_service);
        initTitleRightTextView(R.string.service_log, R.color.white, v -> ServiceHistoryActivity.startActivity(getContext()));
        mTelePhoneNum = mPresenter.getParkTelePhone();
        mTvCall.setText(String.format("服务热线：%s", mTelePhoneNum));
        mAdapter = new TouristServiceAdapter(this);
        mAdapter.setListener(mListener);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_30)));
        mRecycler.setAdapter(mAdapter);
    }

    @OnClick( { R.id.tourist_service_navi, R.id.tourist_service_call } )
    public void Onclick(View v) {
        switch (v.getId()){
            case R.id.tourist_service_navi:
                if (null == TrackRecordService.getLocation()) {
                    ToastHelper.showShort(R.string.trying_to_locate);
                    return;
                } else {
                    if (mPresenter.checkInPark(TrackRecordService.getLocation())) {
                        PlayMapActivity.startActivity(getContext(), "\u670d\u52a1");
                    } else {
                        ToastHelper.showShort(R.string.unable_to_provide_service);
                    }
                }
                break;
            case R.id.tourist_service_call:
                Intent intent = new Intent( Intent.ACTION_DIAL, Uri.parse( "tel:" + mTelePhoneNum ) );
                intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity( intent );
                break;
        }
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, TouristServiceActivity.class);
        context.startActivity(in);
    }

}

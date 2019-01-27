package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
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
import com.expo.module.service.adapter.TouristServiceAdapter;
import com.expo.module.webview.WebActivity;
import com.expo.services.TrackRecordService;
import com.expo.utils.Constants;
import com.expo.widget.decorations.SpaceDecoration;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 游客服务页
 */
public class TouristServiceActivity extends BaseActivity<TouristServiceContract.Presenter> implements TouristServiceContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    TouristServiceAdapter mAdapter;

    BaseAdapterItemClickListener mListener = (view, position, o) -> {
        String title = getResources().getString( getResources().getIdentifier( "item_tourist_service_text_" + position, "string", AppUtils.getAppPackageName() ) );
        switch (position){
            case 1:
                toWebView( CommonInfo.TOURIST_SERVICE_LEFT_LUGGAGE, title, false );
                break;
            case 2:
                toWebView( CommonInfo.TOURIST_SERVICE_RENTAL_ITEMS, title, true );
                break;
            case 3:
                toWebView( CommonInfo.TOURIST_SERVICE_LOST_AND_FOUND, title, false );
                break;
            case 4:
                toWebView( CommonInfo.TOURIST_SERVICE_MATERNAL_AND_CHILD, title, false );
                break;
            case 0: case 5: case 6: case 7:
                Intent intent = new Intent( TouristServiceActivity.this, SeekHelpActivity.class );
                intent.putExtra( Constants.EXTRAS.EXTRA_TITLE, title );
                intent.putExtra( Constants.EXTRAS.EXTRAS, position );
                startActivity( intent );
                break;
        }
    };

    private void toWebView(String type, String title, boolean isStartLocation) {
        String url = mPresenter.loadCommonUrlByType( type );
        WebActivity.startActivity( getContext(), TextUtils.isEmpty( url ) ? Constants.URL.HTML_404 : url, title, TITLE_COLOR_STYLE_WHITE, isStartLocation );
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tourist_service;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle( 1, R.string.home_func_item_tourist_service );
        initTitleRightTextView( R.string.service_log, R.color.white, v -> ServiceHistoryActivity.startActivity( getContext() ) );
        mAdapter = new TouristServiceAdapter( this );
        mAdapter.setListener( mListener );

        mRecycler.setLayoutManager( new GridLayoutManager( this, 2 ) );
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.addItemDecoration( new SpaceDecoration( (int) getResources().getDimension( R.dimen.dms_30 ) ) );
        mRecycler.setAdapter( mAdapter );
    }

    @OnClick(R.id.tourist_service_navi)
    public void Onclick(View v){
        if (null == TrackRecordService.getLocation()) {
            ToastHelper.showShort(R.string.trying_to_locate);
            return;
        }else{
            if (mPresenter.checkInPark( TrackRecordService.getLocation() )) {
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
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, TouristServiceActivity.class );
        context.startActivity( in );
    }

}

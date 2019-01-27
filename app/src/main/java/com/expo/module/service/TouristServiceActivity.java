package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.contract.TouristServiceContract;
import com.expo.entity.CommonInfo;
import com.expo.module.service.adapter.TouristServiceAdapter;
import com.expo.module.webview.WebActivity;
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

    BaseAdapterItemClickListener mListener = new BaseAdapterItemClickListener() {
        @Override
        public void itemClick(View view, int position, Object o) {
            String title = getResources().getString( getResources().getIdentifier( "item_tourist_service_text_" + position, "string", AppUtils.getAppPackageName() ) );
            if (position == 0
                    || position == 1
                    || position == 2
                    || position == 4
                    || position == 5
                    ) {
                Intent intent = new Intent( TouristServiceActivity.this, SeekHelpActivity.class );
                intent.putExtra( Constants.EXTRAS.EXTRA_TITLE, title );
                intent.putExtra( Constants.EXTRAS.EXTRAS, position );
                startActivity( intent );
            } else if (position == 3) {
                toWebView( CommonInfo.BARRIER_FREE_SERVICE, title, TITLE_COLOR_STYLE_GREEN, true );
            } else if (position == 6) {
                toWebView( CommonInfo.NOTICE_OF_BUY_TICKETS, title, TITLE_COLOR_STYLE_GREEN, false );
            } else if (position == 7) {
                toWebView( CommonInfo.NOTICE_OF_GARDEN, title, TITLE_COLOR_STYLE_GREEN, false );
            } else if (position == 8) {
                toWebView( CommonInfo.VENUE_BESPEAK, title, TITLE_COLOR_STYLE_WHITE, false );
            }
        }
    };

    private void toWebView(String type, String title, int titleStyle, boolean isStartLocation) {
        String url = mPresenter.loadCommonUrlByType( type );
        WebActivity.startActivity( getContext(), TextUtils.isEmpty( url ) ? Constants.URL.HTML_404 : url, title, titleStyle, isStartLocation );
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

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, TouristServiceActivity.class );
        context.startActivity( in );
    }

    @OnClick(R.id.tourist_services_phone)
    public void click(View view) {

    }

}

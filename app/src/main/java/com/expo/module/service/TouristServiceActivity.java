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
                toWebView( "4", title, TITLE_COLOR_STYLE_GREEN );
            } else if (position == 6) {
                toWebView( "6", title, TITLE_COLOR_STYLE_GREEN );
            } else if (position == 7) {
                toWebView( "3", title, TITLE_COLOR_STYLE_GREEN );
            } else if (position == 8) {
                toWebView( "5", title, TITLE_COLOR_STYLE_WHITE );
            }
        }
    };

    private void toWebView(String type, String title, int titleStyle) {
        String url = mPresenter.loadCommonUrlByType( type );
        WebActivity.startActivity( getContext(), TextUtils.isEmpty( url ) ? Constants.URL.HTML_404 : url, title, titleStyle );
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tourist_service;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

        setTitle( 0, R.string.home_func_item_tourist_service );

        mAdapter = new TouristServiceAdapter( this );
        mAdapter.setListener( mListener );

        mRecycler.setLayoutManager( new GridLayoutManager( this, 3 ) );
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

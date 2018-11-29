package com.expo.module.main.encyclopedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.expo.R;
import com.expo.adapters.Tab;
import com.expo.adapters.TabPagerAdapter;
import com.expo.base.BaseFragment;
import com.expo.base.utils.StatusBarUtils;
import com.expo.contract.EncyclopediasContract;
import com.expo.module.distinguish.DistinguishActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 百科页
 */
public class EncyclopediaFragment extends BaseFragment<EncyclopediasContract.Presenter> implements EncyclopediasContract.View {

    @BindView(R.id.ency_top_view)
    View mTopView;
    @BindView(R.id.ency_tab)
    TabLayout mTabView;
    @BindView(R.id.ency_pager)
    ViewPager mPagerView;

    private TabPagerAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.fragment_encyclopedia;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTopView.setPadding( 0, StatusBarUtils.getStatusBarHeight( getContext() ), 0, 0 );
        initTabLayout();
        mAdapter = new TabPagerAdapter( getFragmentManager() );
        mPagerView.setAdapter( mAdapter );
        mPagerView.addOnPageChangeListener( mOnPageChangeListener );
        mPresenter.loadTabs();
        LocalBroadcastUtil.registerReceiver( getContext(), receiver, Constants.Action.ACTION_CHANGE_LANGUAGE );
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals( Constants.Action.ACTION_CHANGE_LANGUAGE )) {
                mAdapter.needRestoreSave( false );
            }
        }
    };

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState( outState );
    }

    private void initTabLayout() {
        // 设置分割线
        LinearLayout linearLayout = (LinearLayout) mTabView.getChildAt( 0 );
        linearLayout.setShowDividers( LinearLayout.SHOW_DIVIDER_MIDDLE );
        linearLayout.setDividerDrawable( ContextCompat.getDrawable( getContext(), R.drawable.shape_tab_divide_line ) );
        linearLayout.setDividerPadding( 8 );
        mTabView.addOnTabSelectedListener( mTabSelectedListener );
    }

    private ViewPager.SimpleOnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mTabView.getTabAt( position ).select();
        }
    };

    private TabLayout.OnTabSelectedListener mTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mPagerView.setCurrentItem( tab.getPosition() );
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void setTabData(List<Tab> tabs) {
        if (tabs == null || tabs.isEmpty()) {
            mTabView.setVisibility( View.GONE );
            return;
        }
        mAdapter.setTabs( tabs );
        mTabView.setVisibility( View.VISIBLE );
        mTabView.setTabMode( TabLayout.MODE_SCROLLABLE );
        for (Tab type : tabs) {
            String tabText = LanguageUtil.chooseTest( type.getTab(), type.getEnTab() );
            mTabView.addTab( mTabView.newTab().setText( tabText ) );
        }
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.ency_search, R.id.ency_scan})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ency_search:
                EncyclopediaSearchActivity.startActivity( getContext() );
                break;
            case R.id.ency_scan:
                DistinguishActivity.startActivity( getContext() );
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastUtil.unregisterReceiver( getContext(), receiver );
    }
}

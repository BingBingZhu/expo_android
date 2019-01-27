package com.expo.module.online;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.expo.R;
import com.expo.adapters.Tab;
import com.expo.adapters.TabPagerAdapter;
import com.expo.adapters.VrTabAdapter;
import com.expo.base.BaseActivity;
import com.expo.contract.PanoramaListContract;
import com.expo.entity.VrInfo;
import com.expo.entity.VrLableInfo;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.DivideTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PanoramaListActivity extends BaseActivity<PanoramaListContract.Presenter> implements PanoramaListContract.View {

    private int type = 0;       // 1 世园实景 2 文化世园 3 在线导游

    @BindView(R.id.pan_list_tab)
    DivideTabLayout mTabView;
    @BindView(R.id.pan_list_pager)
    ViewPager mPagerView;

    @Override
    protected int getContentView() {
        return R.layout.activity_pannorama_list;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        type = getIntent().getIntExtra(Constants.EXTRAS.EXTRA_PANORAMA_TYPE, 0);
        setTitle(1, getTitleText(type));
        initTabLayout();
        mAdapter = new TabPagerAdapter( getSupportFragmentManager(), TabPagerAdapter.TYPE_VR_PANORAMA, type );
        mPagerView.setAdapter( mAdapter );
        mPagerView.addOnPageChangeListener( mOnPageChangeListener );
        mPresenter.loadTabData(type);
    }

    private void initTabLayout() {
        // 设置分割线
        LinearLayout linearLayout = (LinearLayout) mTabView.getChildAt( 0 );
        linearLayout.setShowDividers( LinearLayout.SHOW_DIVIDER_MIDDLE );
        linearLayout.setDividerDrawable( ContextCompat.getDrawable( getContext(), R.drawable.shape_vr_tab_divide_line ) );
        linearLayout.setDividerPadding( (int)getResources().getDimension(R.dimen.dms_26) );
        mTabView.addOnTabSelectedListener( mTabSelectedListener );
    }

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

    private ViewPager.SimpleOnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mTabView.getTabAt( position ).select();
        }
    };

    private int getTitleText(int type) {
        switch (type){
            case 1:
                return R.string.expo_scene;
            case 2:
                return R.string.expo_culture;
            case 3:
                return R.string.online_guide;
            default:
                return R.string.data_error;
        }
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context, int type){
        Intent intent = new Intent(context, PanoramaListActivity.class);
        intent.putExtra(Constants.EXTRAS.EXTRA_PANORAMA_TYPE, type);
//        intent.putParcelableArrayListExtra(Constants.EXTRAS.EXTRA_VR_DATA, data);
        context.startActivity(intent);
    }

    @Override
    public void loadTabRes(List<VrLableInfo> tabs) {
        if (tabs == null || tabs.isEmpty()) {
            mTabView.setVisibility( View.GONE );
            return;
        }
        mAdapter.setTabs(VrTabAdapter.convertToTabList(tabs));
        mTabView.setVisibility( View.VISIBLE );
        mTabView.setTabMode( TabLayout.MODE_SCROLLABLE );
        for (VrLableInfo type : tabs) {
            String tabText = LanguageUtil.chooseTest( type.getCaption(), type.getCaptionEn() );
            mTabView.addTab( mTabView.newTab().setText( tabText ) );
        }
        mAdapter.notifyDataSetChanged();
    }

    private TabPagerAdapter mAdapter;
}

package com.expo.module.main.encyclopedia;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.expo.R;
import com.expo.adapters.Tab;
import com.expo.adapters.TabPagerAdapter;
import com.expo.base.BaseFragment;
import com.expo.contract.EncyclopediasContract;
import com.expo.module.distinguish.DistinguishActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 百科页
 */
public class EncyclopediaFragment extends BaseFragment<EncyclopediasContract.Presenter> implements EncyclopediasContract.View {

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
        initTabLayout();
        mAdapter = new TabPagerAdapter( getFragmentManager() );
        mPagerView.setAdapter( mAdapter );
        mPagerView.addOnPageChangeListener( mOnPageChangeListener );
        mPresenter.loadTabs();
    }

    private void initTabLayout() {
        // 设置分割线
        LinearLayout linearLayout= (LinearLayout) mTabView.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getContext(),R.drawable.shape_tab_divide_line));
        linearLayout.setDividerPadding(8);
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
            mTabView.addTab( mTabView.newTab().setText( type.getTab() ) );
        }
    }

    @OnClick({R.id.ency_search, R.id.ency_scan})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ency_search:
                EncyclopediaSearchActivity.startActivity(getContext());
                break;
            case R.id.ency_scan:
                DistinguishActivity.startActivity(getContext());
                break;
        }
    }
}

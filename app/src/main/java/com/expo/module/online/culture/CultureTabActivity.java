package com.expo.module.online.culture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.SceneTabContract;
import com.expo.module.main.find.FindTab;
import com.expo.module.online.scene.SceenTabPagerAdapter;

import java.util.List;

import butterknife.BindView;

/*
 * 推荐路线列表
 */
public class CultureTabActivity extends BaseActivity<SceneTabContract.Presenter> implements SceneTabContract.View {

    @BindView(R.id.find_tab)
    TabLayout mTabView;
    @BindView(R.id.find_pager)
    ViewPager mPagerView;

    private SceenTabPagerAdapter mAdapter;

    private ViewPager.SimpleOnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mTabView.getTabAt(position).select();
        }
    };

    private TabLayout.OnTabSelectedListener mTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_scene_tab;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, R.string.title_expo_scene);
        initTabLayout();
        mAdapter = new SceenTabPagerAdapter(getSupportFragmentManager());
        mPagerView.setAdapter(mAdapter);
        mPagerView.addOnPageChangeListener(mOnPageChangeListener);
        mPresenter.loadTabs();
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    /**
     * 实景世园
     *
     * @param context
     */
    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, CultureTabActivity.class);
        context.startActivity(in);
    }

    private void initTabLayout() {
        mTabView.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        mTabView.addOnTabSelectedListener(mTabSelectedListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mTabView.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void setTabData(List<FindTab> tabs) {
        if (tabs == null || tabs.isEmpty()) {
            mTabView.setVisibility(View.GONE);
            return;
        }
        mAdapter.setTabs(tabs);
        mTabView.setVisibility(View.VISIBLE);
        mTabView.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (FindTab type : tabs) {
            mTabView.addTab(mTabView.newTab().setText(type.tab));
        }
        mAdapter.notifyDataSetChanged();
    }
}

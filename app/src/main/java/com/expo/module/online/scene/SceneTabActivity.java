package com.expo.module.online.scene;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.contract.SceneContract;
import com.expo.contract.SceneTabContract;
import com.expo.entity.RouteInfo;
import com.expo.module.main.find.FindTab;
import com.expo.module.main.find.FindTabPagerAdapter;
import com.expo.utils.CommUtils;
import com.expo.utils.LanguageUtil;
import com.expo.widget.decorations.SpaceDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/*
 * 推荐路线列表
 */
public class SceneTabActivity extends BaseActivity<SceneTabContract.Presenter> implements SceneTabContract.View {

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
        Intent in = new Intent(context, SceneTabActivity.class);
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

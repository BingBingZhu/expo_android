package com.expo.module.main.find;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.StatusBarUtils;
import com.expo.contract.FindContract;
import com.expo.module.main.find.examine.FindExamineActivity;
import com.expo.module.main.find.publish.FindPublishActivity;
import com.expo.utils.Constants;
import com.expo.utils.LocalBroadcastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 发现页
 */
public class FindFragment extends BaseFragment<FindContract.Presenter> implements FindContract.View {

    @BindView(R.id.find_top_view)
    View mTopView;
    @BindView(R.id.find_tab)
    TabLayout mTabView;
    @BindView(R.id.find_pager)
    ViewPager mPagerView;

    private FindTabPagerAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.fragment_find;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTopView.setPadding(0, StatusBarUtils.getStatusBarHeight(getContext()), 0, 0);
        initTabLayout();
        mAdapter = new FindTabPagerAdapter(getFragmentManager());
        mPagerView.setAdapter(mAdapter);
        mPagerView.addOnPageChangeListener(mOnPageChangeListener);
        mPresenter.loadTabs();
        LocalBroadcastUtil.registerReceiver(getContext(), receiver, Constants.Action.ACTION_CHANGE_LANGUAGE);
    }

    private FindListFragment getCurrentFragment() {
        FindListFragment fragment = ((FindListFragment)mAdapter.getItem(mPagerView.getCurrentItem()));
        return fragment;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.Action.ACTION_CHANGE_LANGUAGE)) {
                mAdapter.needRestoreSave(false);
            }
        }
    };

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getCurrentFragment().onActivityResult(requestCode, resultCode, data);
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

    private ViewPager.SimpleOnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mTabView.getTabAt(position).select();
        }
    };

    private TabLayout.OnTabSelectedListener mTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mPagerView.setCurrentItem(tab.getPosition());
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

    @OnClick(R.id.find_my_find)
    public void onClick(View v) {
        FindExamineActivity.startActivity(getContext(), R.string.i_found, true);
    }

    @OnClick(R.id.find_add)
    public void clickFindAdd(View view) {
        FindPublishActivity.startActivity(getContext(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastUtil.unregisterReceiver(getContext(), receiver);
    }
}

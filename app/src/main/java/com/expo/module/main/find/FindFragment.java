package com.expo.module.main.find;

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
import com.expo.contract.FindContract;
import com.expo.module.main.encyclopedia.EncyclopediaSearchActivity;
import com.expo.module.main.find.examine.FindExamineActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 百科页
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

    private void initTabLayout() {
        // 设置分割线
        LinearLayout linearLayout = (LinearLayout) mTabView.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_tab_divide_line));
        linearLayout.setDividerPadding(8);
        mTabView.addOnTabSelectedListener(mTabSelectedListener);
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
        FindExamineActivity.startActivity(getContext(), R.string.examine, true);
    }

//    @OnClick(R.id.find_search)
//    public void onClick(View v) {
//        EncyclopediaSearchActivity.startActivity(getContext());
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastUtil.unregisterReceiver(getContext(), receiver);
    }
}

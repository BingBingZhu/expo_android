package com.expo.module.guide;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.expo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhange Zhao on 2017/10/13.
 * 广告带有圆点指示器的ViewPager的适配器
 */

public class CircleTabPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private int mLastPager;                                                                //上一页的下标
    private LinearLayout mTabContainer;                                                             //圆点指示器的盛放容器
    private ViewPager mAdPager;
    private List<View> mPages = new ArrayList<>();
    private int mTabRes;                                                                        //圆点的图片资源
    private boolean mAutoChangePage;                                                                //是否自动翻页
    private long mTime;                                                                          //自动翻页间隔时间
    private boolean mAutoChanging;                                                                  //是否已经在自动改变页面

    public CircleTabPagerAdapter(LinearLayout tabContainer, ViewPager pager, @DrawableRes int tabRes) {
        this.mTabContainer = tabContainer;
        this.mAdPager = pager;
        this.mAdPager.setAdapter( this );
        this.mAdPager.setOnPageChangeListener( this );
        this.mTabRes = tabRes;
    }

    /**
     * 设置自动翻页
     *
     * @param autoChangePage 是否自动翻页
     * @param time           翻页时间间隔
     */
    public void setAutoChangePage(boolean autoChangePage, long time) {
        this.mAutoChangePage = autoChangePage;
        this.mTime = time;
        autoChange();
    }

    /**
     * 开始自动变化
     */
    private void autoChange() {
        if (mAutoChangePage && mPages != null && !mPages.isEmpty()) {
            if (mAutoChanging) return;
            mAutoChanging = true;
            mTabContainer.postDelayed( nextPage, this.mTime );
        } else {
            mAutoChanging = false;
            mTabContainer.removeCallbacks( nextPage );
        }
    }

    /**
     * 设置页面数据
     *
     * @param pages
     */
    public void setPages(List<View> pages) {
        this.mPages = pages;
        if (pages == null)
            initTab( 0 );
        else
            initTab( pages.size() );
        notifyDataSetChanged();
        autoChange();
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView( mPages.get( position ) );
        return mPages.get( position );
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView( (View) object );
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mTabContainer.getChildAt( position ).setSelected( true );
        mTabContainer.getChildAt( mLastPager ).setSelected( false );
        mLastPager = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (!mAutoChangePage) {                                                                     //非自动改变页面不执行此操作
            return;
        }
        //根据滑动状态添加/移除翻页任务
        if (state == 1) {
            mTabContainer.removeCallbacks( nextPage );
        } else if (state == 0) {
            mTabContainer.postDelayed( nextPage, mTime );
        }
    }

    /**
     * 初始化指定数量的小圆点
     *
     * @param count
     */
    private void initTab(int count) {
        mTabContainer.removeAllViews();
        Drawable drawable = mAdPager.getContext().getResources().getDrawable( mTabRes );
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( drawable.getMinimumWidth(), drawable.getMinimumHeight() );
        lp.rightMargin = lp.leftMargin = mAdPager.getContext().getResources().getDimensionPixelSize( R.dimen.dms_8 );
        for (int i = 0; i < count; i++) {
            View view = new View( mAdPager.getContext() );
            view.setBackgroundResource( mTabRes );
            mTabContainer.addView( view, lp );
            if (i == 0) {
                view.setSelected( true );
            } else {
                view.setSelected( false );
            }
        }
    }

    /**
     * 翻页任务
     */
    private Runnable nextPage = new Runnable() {
        @Override
        public void run() {
            int page = mAdPager.getCurrentItem();
            if (page + 1 < getCount()) {
                mAdPager.setCurrentItem( page + 1 );
            } else {
                mAdPager.setCurrentItem( 0 );
            }
        }
    };
}

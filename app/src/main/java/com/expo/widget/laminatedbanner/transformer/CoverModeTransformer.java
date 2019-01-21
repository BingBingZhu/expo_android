package com.expo.widget.laminatedbanner.transformer;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by on 17/8/20.
 */

public class CoverModeTransformer implements ViewPager.PageTransformer {

    private float reduceX = 0.0f;
    private float itemWidth = 0;
    private float offsetPosition = 0f;
    private int mCoverWidth = 50;
    private float mScaleMax = 1.0f;
    private float mScaleMin = 0.9f;
    private ViewPager mViewPager;

    public CoverModeTransformer(ViewPager pager, int coverWidth) {
        mViewPager = pager;
        this.mCoverWidth = coverWidth;
    }

    @Override
    public void transformPage(View view, float position) {
        if (offsetPosition == 0f) {
            float paddingLeft = mViewPager.getPaddingLeft();
            float paddingRight = mViewPager.getPaddingRight();
            float width = mViewPager.getMeasuredWidth();
            offsetPosition = paddingLeft / (width - paddingLeft - paddingRight);
        }
        float currentPos = position - offsetPosition;
        if (itemWidth == 0) {
            itemWidth = view.getWidth();
            //由于左右边的缩小而减小的x的大小的一半
//            reduceX = (2.0f - mScaleMax - mScaleMin) * itemWidth / 2.0f;
            reduceX = (((1.0f - mScaleMax) + (1.0f - mScaleMin)) * itemWidth / 2.0f);
        }
        if (currentPos <= -1.0f) {
            view.setTranslationX(reduceX + mCoverWidth);
            view.setScaleX(mScaleMin);
            view.setScaleY(mScaleMin);
            if (position >= 0) {
                view.setAlpha((1 - position) * 0.4f + 0.6f);
                view.setZ((1 - position) * 0.4f + 0.6f);
//                view.bringToFront();
            } else {
                view.setAlpha((1 + position) * 0.4f + 0.6f);
                view.setZ((1 + position) * 0.4f + 0.6f);
            }
        } else if (currentPos <= 1.0) {
            float scale = (mScaleMax - mScaleMin) * Math.abs(1.0f - Math.abs(currentPos));
            Log.e("aaaa", (1 - scale) + "====" + position);
            if (position >= 0) {
                view.setAlpha((1 - position) * 0.4f + 0.6f);
                view.setZ((1 - position) * 0.4f + 0.6f);
            } else {
                view.setAlpha((1 + position) * 0.4f + 0.6f);
                view.setZ((1 + position) * 0.4f + 0.6f);
            }
            float translationX = currentPos * -reduceX;
            float aaa = Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f;
            if (currentPos <= -0.5) {   //两个view中间的临界，这时两个view在同一层，左侧View需要往X轴正方向移动覆盖的值()
                view.setTranslationX(translationX + mCoverWidth * aaa);
            } else if (currentPos <= 0.0f) {
                view.setTranslationX(translationX);
            } else if (currentPos >= 0.5) { //两个view中间的临界，这时两个view在同一层
                view.setTranslationX(translationX - mCoverWidth * aaa);
            } else {
                view.setTranslationX(translationX);
            }
            view.setScaleX(scale + mScaleMin);
            view.setScaleY(scale + mScaleMin);
        } else {
            if (position < 2) {
                view.setAlpha((position-1) * 0.4f + 0.6f);
                view.setZ((position-1) * 0.4f + 0.6f);
            }
            view.setScaleX(mScaleMin);
            view.setScaleY(mScaleMin);
            view.setTranslationX(-reduceX - mCoverWidth);
        }

    }
}


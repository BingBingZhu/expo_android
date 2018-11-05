package com.expo.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.expo.R;

public class RootView extends LinearLayoutCompat {

    Activity mActivity;

    protected View mTitle;
    protected View mEmptyView;
    protected View mNormalView;
    protected FrameLayout mBodyView;

    public RootView(@NonNull Context context) {
        super(context);
        initView(context);

    }

    public RootView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RootView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mActivity = (Activity) context;
        setLayoutParams(new LinearLayoutCompat.LayoutParams(-1, -1));
        setOrientation(LinearLayoutCompat.VERTICAL);

        initEmptyView();
        initBodyView();

        addView(mBodyView, new LinearLayoutCompat.LayoutParams(-1, -1));
    }

    private void addTitleView() {
        if (mTitle == null)
            return;
        if (getChildCount() > 1)
            return;
        if (getChildCount() == 1 && getChildCount() != 0 || getChildAt(0) != mTitle)
            addView(mTitle, 0, new LinearLayoutCompat.LayoutParams(-1, -2));
    }

    public void setTitle(int layoutId, String title) {
        if (layoutId == 0 || layoutId == 1) {
            if (mTitle == null) {
                mTitle = new AppBarView(mActivity);
                ((AppBarView) mTitle).setOnClickListener(v -> {
                    mActivity.finish();
                });
            }
            if (layoutId == 0) {
                addTitleView();
                setGreenTitle();
            } else {
                addTitleView();
                setWhiteTitle();
            }
            ((AppBarView) mTitle).setTitle(title);
        } else {
            mTitle = LayoutInflater.from(mActivity).inflate(layoutId, null);
            addTitleView();
        }
    }

    public void setGreenTitle() {
        if (mTitle == null) return;
        ((AppBarView) mTitle).setTitleColor(mActivity.getResources().getColor(R.color.white));
        ((AppBarView) mTitle).setTitleSize(TypedValue.COMPLEX_UNIT_PX, SizeUtils.sp2px(22));
        ((AppBarView) mTitle).setBackImageResource(R.drawable.icon_navbar_back_white);
        ((AppBarView) mTitle).onFinishInflate();
        mTitle.setBackgroundResource(R.color.colorAccent);
    }

    public void setWhiteTitle() {
        if (mTitle == null) return;
        ((AppBarView) mTitle).setTitleColor(mActivity.getResources().getColor(R.color.black));
        ((AppBarView) mTitle).setTitleSize(TypedValue.COMPLEX_UNIT_PX, SizeUtils.sp2px(22));
        ((AppBarView) mTitle).setBackImageResource(R.drawable.icon_navbar_back_black);
        ((AppBarView) mTitle).onFinishInflate();
        mTitle.setBackgroundResource(R.color.white);
    }

    public void initBodyView() {
        mBodyView = new FrameLayout(mActivity);
        mBodyView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
    }

    public void initEmptyView() {
        initEmptyView(R.layout.layout_empty);
    }

    public void initEmptyView(int layoutId) {
        mEmptyView = LayoutInflater.from(mActivity).inflate(layoutId, null);

    }

    public void initEmptyView(View view) {
        mEmptyView = view;
    }

    public void setNormalView(int layoutId) {
        setNormalView(LayoutInflater.from(mActivity).inflate(layoutId, null));
    }

    public void setNormalView(View view) {
        mNormalView = view;
        mBodyView.addView(view);

        showNormal();
    }

    public void showNormal() {
        mNormalView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    public void showEmpty() {
        mNormalView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    public View getTitle() {
        return mTitle;
    }
}

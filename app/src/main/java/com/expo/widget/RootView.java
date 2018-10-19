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

        setTitle(0, "");

        initEmptyView();
        initBodyView();

        addView(mTitle, new LinearLayoutCompat.LayoutParams(-1, -2));
        addView(mBodyView, new LinearLayoutCompat.LayoutParams(-1, -1));
    }

    public void setTitle(int layoutId, String title) {
        if (layoutId == 0) {
            if (mTitle == null) {
                mTitle = new AppBarView(mActivity);
                ((AppBarView) mTitle).setOnClickListener(v -> {
                    mActivity.finish();
                });
                ((AppBarView) mTitle).setBackgroundResource(R.color.colorAccent);
                ((AppBarView) mTitle).setTitleColor(mActivity.getResources().getColor(R.color.white));
                ((AppBarView) mTitle).setTitleSize(TypedValue.COMPLEX_UNIT_PX, SizeUtils.sp2px(24));
            }
            ((AppBarView) mTitle).setTitle(title);

        } else {
            mTitle = LayoutInflater.from(mActivity).inflate(layoutId, null);
        }
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

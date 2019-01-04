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
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.expo.R;
import com.expo.base.utils.StatusBarUtils;
import com.squareup.picasso.Picasso;

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
                    mActivity.onBackPressed();
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
        ((AppBarView) mTitle).setTitleSize(TypedValue.COMPLEX_UNIT_PX, SizeUtils.sp2px(17));
        ((AppBarView) mTitle).setBackImageResource(R.mipmap.ico_white_back);
        ((AppBarView) mTitle).onFinishInflate();
        mTitle.setBackgroundResource(R.color.green_02cd9b);
    }

    public void setWhiteTitle() {
        if (mTitle == null) return;
        ((AppBarView) mTitle).setTitleColor(mActivity.getResources().getColor(R.color.black_333333));
        ((AppBarView) mTitle).setTitleSize(TypedValue.COMPLEX_UNIT_PX, SizeUtils.sp2px(17));
        ((AppBarView) mTitle).setBackImageResource(R.mipmap.ico_black_back);
        ((AppBarView) mTitle).onFinishInflate();
        mTitle.setBackgroundResource(R.color.white);
    }

    public void initBodyView() {
        mBodyView = new FrameLayout(mActivity);
        mBodyView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
    }

    public void initEmptyView() {
        initEmptyView(R.mipmap.empty_img2, R.string.empty_content_2, R.string.empty_fresh_2);
    }

    public void initEmptyView(int imgRes, int content, int btn) {
        initEmptyView(R.layout.layout_empty);
        Picasso.with(getContext()).load(imgRes).into((ImageView) mEmptyView.findViewById(R.id.layout_empty_img));
        ((TextView) mEmptyView.findViewById(R.id.layout_empty_text)).setText(content);
        ((TextView) mEmptyView.findViewById(R.id.layout_empty_fresh)).setText(btn);
    }

    public void initEmptyView(int layoutId) {
        initEmptyView(LayoutInflater.from(mActivity).inflate(layoutId, null));
    }

    public void initEmptyView(View view) {
        initEmptyView(view, 0, null);
    }

    public void initEmptyView(View view, int freshId, View.OnClickListener listener) {
        mEmptyView = view;
        mEmptyView.setVisibility(View.GONE);
        addFreshListener(freshId, listener);
        mBodyView.addView(mEmptyView);
    }

    public void addFreshListener(int freshId, View.OnClickListener listener) {
        if (listener == null) {
            return;
        } else if (freshId == 0) {
            mEmptyView.findViewById(R.id.layout_empty_fresh).setVisibility(View.VISIBLE);
            mEmptyView.findViewById(R.id.layout_empty_fresh).setOnClickListener(listener);
        } else {
            mEmptyView.findViewById(freshId).setOnClickListener(listener);
        }
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setTopPadding() {
        setPadding(0, StatusBarUtils.getStatusBarHeight(getContext()), 0, 0);
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
        if (mNormalView != null)
            mNormalView.setVisibility(View.VISIBLE);
        if (mEmptyView != null)
            mEmptyView.setVisibility(View.GONE);
    }

    public void showEmpty() {
        if (mNormalView != null)
            mNormalView.setVisibility(View.GONE);
        if (mEmptyView != null)
            mEmptyView.setVisibility(View.VISIBLE);
    }

    public void setTitleVisibility(int visibility) {
        if (mTitle != null)
            mTitle.setVisibility(visibility);
    }

    public View getTitle() {
        return mTitle;
    }
}

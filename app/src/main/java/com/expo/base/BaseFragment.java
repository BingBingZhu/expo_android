package com.expo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expo.base.utils.StatusBarUtils;
import com.expo.contract.PresenterFactory;

import butterknife.ButterKnife;


/**
 * Created by LS on 2017/10/31.
 * 所有Fragment应该继承此类,完成View类属性的自动绑定
 */

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IView {

    private View mContentView;
    protected P mPresenter;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(getContentView(), null);
            mContentView.setPadding(
                    mContentView.getPaddingLeft(),
                    mContentView.getPaddingTop() + StatusBarUtils.getStatusBarHeight(getContext()),
                    mContentView.getPaddingRight(),
                    mContentView.getPaddingBottom()
            );
            ButterKnife.bind(this, mContentView);
            if (hasPresenter())
                mPresenter = (P) PresenterFactory.getPresenter(this);
            onInitView(savedInstanceState);
        } else if (mContentView.getParent() != null) {
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
        return mContentView;
    }

    public void setTopPadding() {
        mContentView.setPadding(0, StatusBarUtils.getStatusBarHeight(getContext()), 0, 0);
    }

    public abstract int getContentView();

    protected abstract void onInitView(Bundle savedInstanceState);

    protected abstract boolean hasPresenter();

    protected P getPresenter() {
        return mPresenter;
    }

    /**
     * 显示加载等待进度条
     */
    public void showLoadingView() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoadingView();
        }
    }

    /**
     * 隐藏加载等待进度条
     */
    public void hideLoadingView() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideLoadingView();
        }
    }

    public View getContent() {
        return mContentView;
    }
}

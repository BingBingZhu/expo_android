package com.expo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.expo.R;
import com.expo.base.utils.ActivityHelper;
import com.expo.base.utils.StatusBarUtils;
import com.expo.contract.PresenterFactory;

import butterknife.ButterKnife;

/**
 * Created by Zhang Zhao on 2017/10/26.
 * 所有Activity应该继承此类，完成了权限申请的封装，自动对View进行资源绑定
 */

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView {

    // Activity是否双击返回键退出应用 默认false
    private boolean mDoubleTapToExit;
    // 加载进度条视图
    private View mLoadingMaskView;
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        // 设置view中可以使用Vector图片资源
        AppCompatDelegate.setCompatVectorFromResourcesEnabled( true );
        StatusBarUtils.setStatusBarFullTransparent( this );
        StatusBarUtils.setStatusBarLight( this, true );
        setContentView( getContentView() );
        // Activity退出管理类添加开启的Activity
        ActivityHelper.add( this );
        ButterKnife.bind( this );
        if (hasPresenter())
            mPresenter = (P) PresenterFactory.getPresenter( this );
        onInitView( savedInstanceState );
    }

    @Override
    protected void onDestroy() {
        // Activity销毁时移除此Activity
        ActivityHelper.remove( this );
        super.onDestroy();
    }

    /**
     * 设置双击返回键是否退出
     *
     * @param mDoubleTapToExit
     */
    public void setDoubleTapToExit(boolean mDoubleTapToExit) {
        this.mDoubleTapToExit = mDoubleTapToExit;
    }

    /**
     * 获取活动布局资源
     *
     * @return 返回资源id
     */
    protected abstract int getContentView();

    /**
     * 视图初始化回调
     *
     * @param savedInstanceState {@see onCreate(Bundle savedInstanceState)}
     */
    protected abstract void onInitView(Bundle savedInstanceState);

    /**
     * 是否需要创建Presenter实例
     *
     * @return true 需要创建
     */
    protected abstract boolean hasPresenter();

    /**
     * 显示加载等待进度条
     */
    @Override
    public void showLoadingView() {
        if (mLoadingMaskView == null) {
            mLoadingMaskView = getLayoutInflater().inflate( R.layout.layout_progress, null );
        }
        if (mLoadingMaskView.getParent() == null) {
            ((ViewGroup) getWindow().getDecorView()).addView( mLoadingMaskView );
        }
    }

    /**
     * 隐藏加载等待进度条
     */
    @Override
    public void hideLoadingView() {
        if (mLoadingMaskView != null && mLoadingMaskView.getParent() != null) {
            ((ViewGroup) mLoadingMaskView.getParent()).removeView( mLoadingMaskView );
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mDoubleTapToExit && keyCode == KeyEvent.KEYCODE_BACK) {                                 //检测按键，判断是否触发双击退出应用
            BaseApplication.getApplication().appExit();
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }

    @Override
    public Context getContext() {
        return this;
    }
}

package com.expo.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.expo.R;
import com.expo.base.utils.ActivityHelper;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;


/**
 * Created by LS on 2017/10/26.
 * 封装了退出应用
 */

public class BaseApplication extends MultiDexApplication {
    public static final String TAG = "default";
    protected boolean mCanExit;
    private static BaseApplication mApp;
    private int mResumedCount;
    private int mPauseCount;
    private Activity mTopActivity;                                                   //应用最顶层的活动

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Thread.setDefaultUncaughtExceptionHandler( uncaughtExceptionHandler );
        registerActivityLifecycleCallbacks( activityLifecycleCallbacks );
        LogUtils.WRITE_LOG_TO_FILE = true;
        LogUtils.DEBUG = true;
        LogUtils.logPath = "Android/data/" + getPackageName() + "/logs/";
    }

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (t, e) -> {
        LogUtils.e( TAG, "Thread id:" + t.getId(), e );
    };

    /*
     * 监测记录应用中活动状态，记录顶层活动
     */
    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            mTopActivity = activity;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            mResumedCount++;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            mPauseCount++;
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            mResumedCount--;
            mPauseCount--;
        }
    };

    /**
     * 判断应用是否在前台显示
     *
     * @return true 是前台状态 false 不是
     */
    public boolean isForeground() {
        return mResumedCount > mPauseCount;
    }

    public static BaseApplication getApplication() {
        return mApp;
    }

    /**
     * 获取应用最顶层的活动
     *
     * @return 返回当前应用的顶层活动
     */
    public Activity getTopActivity() {
        return mTopActivity;
    }

    /**
     * 退出应用
     */
    public void appExit() {
        if (mCanExit) {                                                                             // 如果退出
            ActivityHelper.finishAll();                                                             // 关闭所有打开的Activity
            onExit();                                                                               // 退出回调
        } else {
            mCanExit = true;
            ToastHelper.showShort( R.string.click_again_to_exit );                                                  // 提示再次点击退出
            new Handler().postDelayed( () -> mCanExit = false, 2000 );                    //2秒未再次点击取消再次点击退出操作
        }
    }

    /*
     * 退出清除数据
     */
    protected void onExit() {
        mCanExit = false;
    }

    @Override
    public void onTerminate() {
        onExit();
        mApp = null;
        super.onTerminate();
    }
}

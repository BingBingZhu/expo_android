package com.expo.module.heart;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.expo.contract.HeartBeatContract;
import com.expo.contract.presenter.HeartBeatPresenterImpl;

/**
 * Created by LS on 2017/11/13.
 * 与服务器保持连接心跳的服务，并将服务器推送的消息获取到
 */

public class HeartBeatService extends Service implements HeartBeatContract.View {
    private boolean mExit;                                                               //结束心跳
    private HeartBeatContract.Presenter mPresenter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = new HeartBeatPresenterImpl( this );
        new Thread() {
            @Override
            public void run() {                                                                         //心跳线程，不断循环向服务器发送请求
                while (!mExit) {
                    try {
                        sleep( 60000 );
                        mPresenter.sendHeartBeat();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        mExit = true;
        super.onDestroy();
    }

    /**
     * 启动服务
     *
     * @param context
     */
    public static void startService(Context context) {
        Intent heartBeat = new Intent( context, HeartBeatService.class );
        context.startService( heartBeat );
    }

    /**
     * 停止服务
     *
     * @param context
     */
    public static void stopService(Context context) {
        Intent heartBeat = new Intent( context, HeartBeatService.class );
        context.stopService( heartBeat );
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}

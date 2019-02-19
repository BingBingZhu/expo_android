package com.expo.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.expo.base.ExpoApp;
import com.expo.upapp.UpdateAppManager;
import com.expo.utils.Constants;
import com.expo.utils.LocalBroadcastUtil;

public class DownloadListenerService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastUtil.registerReceiver( getContext(), receiver,
                Constants.Action.ACTION_DOWNLOAD_VR_APP_SUCCESS,
                Constants.Action.ACTION_DOWNLOAD_APP_SUCCESS
                /*Constants.Action.ACTION_CANCEL_UPDATE*/ );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastUtil.unregisterReceiver(getContext(), receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.Action.ACTION_DOWNLOAD_VR_APP_SUCCESS)){
                installApp(getContext());
            }
            if (intent.getAction().equals(Constants.Action.ACTION_DOWNLOAD_APP_SUCCESS)) {
                installApp(getContext());
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void installApp(Context context) {
        if(!context.getPackageManager().canRequestPackageInstalls()) {
            Uri packageURI = Uri.parse("package:"+ context.getPackageName());
            Intent in = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
            ExpoApp.getApplication().getTopActivity().startActivityForResult(in, Constants.RequestCode.REQ_INSTALL_PERMISS_CODE);
            return;
        }
        UpdateAppManager.getInstance(context).installApp();
    }

    /**
     * 启动服务
     *
     * @param context
     */
    public static void startService(Context context) {
        Intent intent = new Intent(context, DownloadListenerService.class);
        context.startService(intent);
    }

    /**
     * 停止服务
     *
     * @param context
     */
    public static void stopService(Context context) {
        Intent intent = new Intent(context, DownloadListenerService.class);
        context.stopService(intent);
    }

    private Context getContext(){
        return DownloadListenerService.this;
    }
}

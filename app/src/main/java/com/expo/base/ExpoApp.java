package com.expo.base;

import android.graphics.Bitmap;

import com.expo.base.utils.LogUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.db.DBUtil;
import com.expo.entity.User;
import com.expo.map.LocationManager;
import com.expo.utils.Constants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobSDK;
import com.tencent.smtt.sdk.QbSdk;

public class ExpoApp extends BaseApplication {

    private User mUser;
    private boolean debug;
    public Bitmap userHandBitmap;

    @Override
    public void onCreate() {
        super.onCreate();
        debug = true;
        LogUtils.DEBUG = true;
        //数据库
        DBUtil.init( getApplicationContext(), Constants.Config.DB_CLASSES );
        MobSDK.init( this );
        initX5WebView();
        Fresco.initialize( this );
        if (PrefsHelper.getBoolean(Constants.Prefs.KEY_TRACK_ON_OFF, true))
            PrefsHelper.setLong(Constants.Prefs.KEY_RUN_UP_COUNT, PrefsHelper.getLong(Constants.Prefs.KEY_RUN_UP_COUNT, -1)+1);
    }

    private void initX5WebView() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                LogUtils.d( "app", " onViewInitFinished is " + arg0 );
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment( getApplicationContext(), cb );
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public Bitmap getUserHandBitmap() {
        return userHandBitmap;
    }

    public void setUserHandBitmap(Bitmap bitmap) {
        this.userHandBitmap = bitmap;
    }

    public static ExpoApp getApplication() {
        return ((ExpoApp) BaseApplication.getApplication());
    }

    public boolean isDebug() {
        return debug;
    }

    @Override
    public void onTerminate() {
        LocationManager.getInstance().destroy();
        super.onTerminate();
    }

}

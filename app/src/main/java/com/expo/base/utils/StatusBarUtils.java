package com.expo.base.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by LS on 2017/9/25.
 */

public class StatusBarUtils {

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier( "status_bar_height", "dimen", "android" );
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize( resourceId );
        }
        return statusBarHeight;
    }

    /**
     * 设置状态栏颜色 API19及以上生效
     *
     * @param activity
     * @return
     */
    public static void setStatusBarFullTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
            window.getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
            window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );
            setStatusBarColor( activity, Color.TRANSPARENT );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
        }
    }

    /**
     * 设置状态栏颜色 API21及以上生效
     *
     * @param activity
     * @param color
     * @return
     */
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor( color );
        }
    }

    /**
     * 设置状态栏为亮色 API23及以上生效
     *
     * @param activity
     * @param isLight
     * @return
     */
    public static void setStatusBarLight(Activity activity, boolean isLight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            if (isLight) {
                if (systemUiVisibility == (systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR))
                    return;
                decorView.setSystemUiVisibility( systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR );
            } else {
                if (systemUiVisibility != (systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR))
                    return;
                decorView.setSystemUiVisibility( (systemUiVisibility ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) );
            }
        }
    }

    /**
     * 取消状态栏透明
     *
     * @param activity
     * @return
     */
    public static void cancelStatusBarFullTransparent(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
    }
}

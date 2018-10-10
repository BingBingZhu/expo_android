package com.expo.base.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.expo.base.BaseApplication;


/**
 * Created by LS on 2017/10/26.
 */

public class ToastHelper {
    public static Toast mToast;

    private static void cancel() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    public static void showShort(@StringRes int resId) {
        cancel();
        mToast = Toast.makeText( BaseApplication.getApplication(), resId, Toast.LENGTH_SHORT );
        mToast.show();
    }

    public static void showShort(String res) {
        cancel();
        mToast = Toast.makeText( BaseApplication.getApplication(), res, Toast.LENGTH_SHORT );
        mToast.show();
    }

    public static void showLong(@StringRes int resId) {
        cancel();
        mToast = Toast.makeText( BaseApplication.getApplication(), resId, Toast.LENGTH_LONG );
        mToast.show();
    }

    public static void showLong(String res) {
        cancel();
        mToast = Toast.makeText( BaseApplication.getApplication(), res, Toast.LENGTH_LONG );
        mToast.show();
    }
}

package com.expo.upapp;

import android.app.Activity;

/**
 * Created by lenovo on 2018/6/17.
 * Albert
 * 工具类
 */

public class PublicUtile {

    private static PublicUtile mPublicUtile;

    public static PublicUtile getInstance() {
        if (mPublicUtile == null) {
            mPublicUtile = new PublicUtile();
        }
        return mPublicUtile;
    }

    private Activity mActivity;

    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }
}

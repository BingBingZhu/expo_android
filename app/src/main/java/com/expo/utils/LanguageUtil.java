package com.expo.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.StringUtils;
import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;

import java.util.Locale;

public class LanguageUtil {

    public final static String LANGUAGE_CN = "values-zh-rCN";
    public final static String LANGUAGE_EN = "values-en-rUS";


    public static void changeAppLanguage(Context context, @NonNull String language) {
        if (LANGUAGE_CN.equals( language )) {
            changeAppLanguage( context, Locale.CHINA );
        } else if (LANGUAGE_EN.equals( language )) {
            changeAppLanguage( context, Locale.ENGLISH );
        }
    }

    /**
     * 更改应用语言
     *
     * @param context
     * @param locale  语言地区
     */
    public static void changeAppLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale( locale );
        } else {
            configuration.locale = locale;
        }
        resources.updateConfiguration( configuration, metrics );
        LocalBroadcastUtil.sendBroadcast( context, null, Constants.Action.ACTION_CHANGE_LANGUAGE );
    }

    public static boolean isCN() {
        String localLanguage = PrefsHelper.getString( Constants.Prefs.KEY_LANGUAGE_CHOOSE, null );
        if (!TextUtils.isEmpty( localLanguage ) && StringUtils.equals( LANGUAGE_CN, PrefsHelper.getString( Constants.Prefs.KEY_LANGUAGE_CHOOSE, null ) )) {
            return true;
        }
        return false;
    }

    public static boolean systemIsCN() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = ExpoApp.getApplication().getResources().getConfiguration().getLocales().get( 0 );
        } else {
            locale = ExpoApp.getApplication().getResources().getConfiguration().locale;
        }
        String language = locale.getLanguage();
        if (language.endsWith( "zh" ))
            return true;
        return false;
    }


    /**
     * 根据语言选择返回不同的string
     *
     * @param cn 中文
     * @param en 英文
     * @return
     */
    public static String chooseTest(String cn, String en) {
        if (isCN()) return cn;
        else return null == en || en.isEmpty() ? cn : en;
    }

    /**
     * 根据语言选择返回不同的图片资源
     *
     * @param cnImgId 中文图片
     * @param enImgId 英文图片
     * @return
     */
    public static int chooseImg(int cnImgId, int enImgId) {
        if (isCN()) return cnImgId;
        else return enImgId;
    }

}

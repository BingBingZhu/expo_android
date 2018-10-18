package com.expo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import java.util.Locale;

import static android.provider.ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY;
import static android.provider.MediaStore.Video.VideoColumns.LANGUAGE;

public class LanguageUtil {

    public final static String LANGUAGE_CN = "values-zh-rCN";
    public final static String LANGUAGE_EN = "values-en-rUS";


    public static void changeAppLanguage(Context context, @NonNull String language) {
        if (LANGUAGE_CN.equals(language)) {
            changeAppLanguage(context, Locale.CHINA);
        } else if (LANGUAGE_EN.equals(language)) {
            changeAppLanguage(context, Locale.ENGLISH);
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
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration, metrics);
    }

}

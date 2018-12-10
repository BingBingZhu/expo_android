package com.expo.base.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {

    public static boolean isEmail(String email, boolean showToast) {
        if (TextUtils.isEmpty(email)) {
            return true;
        }
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        if (showToast)
            if (!m.matches())
                ToastHelper.showShort(R.string.wrong_email);
        return m.matches();
    }

    public static boolean isEmtpy(String content, int resId, boolean showToast) {
        boolean isEmpty = StringUtils.isEmpty(content);
        if (showToast && isEmpty)
            ToastHelper.showShort(resId);

        return isEmpty;
    }

    public static boolean isIDCard(String IDCard, boolean showToast) {
        if (IDCard != null) {
            String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
            if(IDCard.matches(IDCardRegex)){
                return true;
            }
        }
        if (showToast)
            ToastHelper.showShort(R.string.check_string_id_card);
        return false;
    }
}

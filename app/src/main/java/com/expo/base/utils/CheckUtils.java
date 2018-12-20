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

    /**
     * 身份证验证
     *
     * @param IDCard
     * @param showToast
     * @return
     */
    public static boolean isIDCard(String IDCard, boolean showToast) {
        int textRes = 0;
        if (showToast)
            textRes = R.string.check_string_id_card;
        return isIDCard(IDCard, "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)", textRes);
    }

    public static boolean isCorrectPapersFormat(String papersNum, String type){
        String regex = "";
        if (type.equals("1")){
            regex = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
        } else if (type.equals("2")){
            regex = "^1[45][0-9]{7}$|([P|p|S|s]\\d{7}$)|([Gg|Tt|Ss|Ll|Qq|Dd|Aa|Ee|Ff]\\d{8}$)|([H|h|M|m]\\d{8,10})$";
        } else if (type.equals("3")){
            regex = "^([H|h|M|m]\\d{8,10})$";
        } else if (type.equals("4")){
            regex = "^([0-9]{8}|[0-9]{10})$";
        } else {
            return false;
        }
        if (papersNum.matches(regex)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 身份证验证
     *
     * @param IDCard
     * @param showToast
     * @return
     */
    public static boolean isPassport (String IDCard, boolean showToast) {
//        return isIDCard(IDCard, "^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]$", textRes);
        boolean result = isIDCard(IDCard, "^[a-zA-Z]{5,17}$", 0) || isIDCard(IDCard, "^[a-zA-Z0-9]{5,17}", 0);
        if(!result && showToast)
            ToastHelper.showShort(R.string.check_string_passport);
        return result;
    }

    public static boolean isTWCard (String IDCard, boolean showToast) {
//        return isIDCard(IDCard, "^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]$", textRes);
        boolean result = isIDCard(IDCard, "^[0-9]{8}$", 0) || isIDCard(IDCard, "^[0-9]{10}$", 0);
        if(!result && showToast)
            ToastHelper.showShort(R.string.check_string_tw_card);
        return result;
    }

    public static boolean isIDCard(String IDCard, String IDCardRegex, int textRes) {
        if (IDCard != null) {
            if (IDCard.matches(IDCardRegex)) {
                return true;
            }
        }
        if (textRes != 0)
            ToastHelper.showShort(textRes);
        return false;
    }

    public static void main(String args[]) {
        System.out.println(isPassport("B17672342", false));
    }
}

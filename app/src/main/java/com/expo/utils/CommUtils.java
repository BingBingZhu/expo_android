package com.expo.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class CommUtils {

    public static void hideKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideView(View... view) {
        for (int i = 0; i < view.length; i++)
            if (view[i] != null)
                view[i].setVisibility(View.GONE);
    }

    public static String getFullUrl(String url) {
        String imgUrl = url;
        if (!imgUrl.startsWith(Constants.URL.FILE_BASE_URL))
            imgUrl = Constants.URL.FILE_BASE_URL + imgUrl;
        return imgUrl;
    }
}

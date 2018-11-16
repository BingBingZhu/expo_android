package com.expo.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.squareup.picasso.Picasso;

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
        if (!imgUrl.startsWith("http"))
            imgUrl = Constants.URL.FILE_BASE_URL + imgUrl;
        return imgUrl;
    }

    public static void setImgPic(Context context, String url, ImageView view) {
        Picasso.with(context)
                .load(getFullUrl(url))
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
                .into(view);
    }

    public static void setText(TextView textView, String cn, String en) {
        textView.setText(LanguageUtil.chooseTest(cn, en));
    }
}

package com.expo.utils;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.idst.nls.internal.utils.Base64Encoder;
import com.expo.R;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

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

    public static boolean isLocationEquals(Location l1, Location l2){
        if(l1 == null || l2 == null) return false;
       return l1.getLatitude() == l2.getLatitude() && l1.getLongitude() == l2.getLongitude();
    }

}

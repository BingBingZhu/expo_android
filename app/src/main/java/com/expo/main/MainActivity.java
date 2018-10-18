package com.expo.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.expo.R;
import com.expo.base.utils.PrefsHelper;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public static void startActivity(Context context) {
        Intent in = new Intent(context, MainActivity.class);
        context.startActivity(in);
    }
    public static void startActivityFromSplash(Context context) {
        Intent in = new Intent(context, MainActivity.class);
        if (PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null) != null) {
            LanguageUtil.changeAppLanguage(context, PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null));
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(in);
    }
}

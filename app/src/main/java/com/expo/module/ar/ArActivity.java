package com.expo.module.ar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.expo.R;
import com.expo.base.BaseActivity;

public class ArActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_ar;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.root, new ArFragment(), "ar_fragment")
                .commit();
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(Context context) {
        Intent in = new Intent(context, ArActivity.class);
        context.startActivity(in);
    }

    public static void lunchPhotograph(Context context) {
        Intent in = new Intent("com.casvd.expo_ar.ar");
        in.putExtra("kill", true);
        in.setData(Uri.parse("casvd://ar.casvd.com/2"));
        context.startActivity(in);
    }
}

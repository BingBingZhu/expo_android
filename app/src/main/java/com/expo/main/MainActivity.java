package com.expo.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.expo.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
    }


    public static void startActivity(Context context) {
        Intent in = new Intent( context, MainActivity.class );
        context.startActivity( in );
    }
}

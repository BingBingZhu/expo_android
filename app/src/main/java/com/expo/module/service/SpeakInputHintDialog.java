package com.expo.module.service;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.expo.R;

public class SpeakInputHintDialog extends Dialog {

    public SpeakInputHintDialog(@NonNull Context context) {
        super( context, R.style.SpeakInputDialog );
    }

    private ImageView mCloseView;
    private View.OnClickListener mClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.layout_input_by_speak );
        int padding = getWindow().getDecorView().getResources().getDimensionPixelSize( R.dimen.dms_30 );
        getWindow().getDecorView().setPadding( padding, padding, padding, padding );
        mCloseView = findViewById( R.id.stop_hear );
//        ((AnimationDrawable) mCloseView.getBackground()).start();
        mCloseView.setOnClickListener( closeListener );
        setCanceledOnTouchOutside( false );
    }

    private View.OnClickListener closeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onClick( v );
            }
            dismiss();
        }
    };

    @Override
    public void dismiss() {
        if (mCloseView != null)
            ((AnimationDrawable) mCloseView.getBackground()).stop();
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
        ((AnimationDrawable) mCloseView.getBackground()).start();
    }

    public void setOnCloseListener(View.OnClickListener closeListener) {
        this.mClickListener = closeListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isShowing()) {
            if (mClickListener != null) {
                mClickListener.onClick( null );
            }
        }
        return super.onKeyDown( keyCode, event );
    }
}

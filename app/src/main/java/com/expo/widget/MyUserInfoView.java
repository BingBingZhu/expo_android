package com.expo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.expo.R;

public class MyUserInfoView extends RelativeLayout {

    TextView mTvText;

    public MyUserInfoView(Context context) {
        this(context, null);
    }

    public MyUserInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyUserInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        if (mTvText == null) initTextView();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CameraButton);
            mTvText.setTextColor(a.getColor(R.styleable.MyUserInfoView_textColor, Color.BLACK));
            mTvText.setTextSize(a.getDimension(R.styleable.MyUserInfoView_textColor, 18));
            mTvText.setText(a.getString(R.styleable.MyUserInfoView_text));
            a.recycle();
        }
    }

    private void initTextView() {
        mTvText = new TextView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        mTvText.setLayoutParams(layoutParams);
        mTvText.setGravity(Gravity.CENTER_VERTICAL);
        addView(mTvText);
    }

    public void addRightTextView(View view) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        view.setLayoutParams(layoutParams);
        addView(view);
    }
}

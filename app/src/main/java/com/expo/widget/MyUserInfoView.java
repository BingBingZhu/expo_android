package com.expo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.expo.R;

public class MyUserInfoView extends FrameLayout {

    TextView mTvText;
    View mRightView;

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
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyUserInfoView);
            mTvText.setTextColor(a.getColor(R.styleable.MyUserInfoView_textColor, Color.RED));
            mTvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.MyUserInfoView_textSize, 18));
            mTvText.setText(a.getString(R.styleable.MyUserInfoView_text));
            a.recycle();
        }
    }

    private void initTextView() {
        mTvText = new TextView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        mTvText.setLayoutParams(layoutParams);
        addView(mTvText);
    }

    public void addRightView(View view, int width, int height) {
        mRightView = view;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.rightMargin = (int) getResources().getDimension(R.dimen.dms_10);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

        mRightView.setLayoutParams(layoutParams);
        addView(mRightView);
    }

    public void addRightView(View view) {
        addRightView(view, -2, -2);
    }

    public View getRightView() {
        return mRightView;
    }
}

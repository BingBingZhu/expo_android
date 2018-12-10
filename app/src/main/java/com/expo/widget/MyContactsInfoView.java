package com.expo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expo.R;

public class MyContactsInfoView extends LinearLayout {

    TextView mTvText;
    View mRightView;

    public MyContactsInfoView(Context context) {
        this(context, null);
    }

    public MyContactsInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyContactsInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        if (mTvText == null) initTextView();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyContactsInfoView);
            mTvText.setTextColor(a.getColor(R.styleable.MyContactsInfoView_lefttextColor, Color.RED));
            mTvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.MyContactsInfoView_lefttextSize, 32));
            mTvText.setText(a.getString(R.styleable.MyContactsInfoView_lefttext));
            mTvText.setCompoundDrawablesWithIntrinsicBounds(0, 0, a.getResourceId(R.styleable.MyContactsInfoView_lefttextrightDrawable, 0), 0);
            a.recycle();
        }
    }

    private void initTextView() {
        mTvText = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams((int) getContext().getResources().getDimension(R.dimen.dms_150), -2);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        mTvText.setLayoutParams(layoutParams);
        mTvText.setSingleLine();
        addView(mTvText);
    }

    public void addRightView(View view, int width, int height) {
        mRightView = view;
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = (int) getResources().getDimension(R.dimen.dms_60);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

        mRightView.setLayoutParams(layoutParams);
        addView(mRightView);
        mRightView.setMinimumWidth((int) getContext().getResources().getDimension(R.dimen.dms_50));
    }

    public void addRightView(View view) {
        addRightView(view, -1, -2);
    }

    public View getRightView() {
        return mRightView;
    }
}
